import React, { useCallback, useContext, useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router';
import {
  IonContent,
  IonFab,
  IonFabButton,
  IonHeader,
  IonIcon,
  IonInfiniteScroll,
  IonInfiniteScrollContent,
  IonList, IonLoading,
  IonPage,
  IonRadio,
  IonRadioGroup,
  IonSearchbar,
  IonTitle,
  IonToolbar
} from '@ionic/react';
import { add, logOut } from 'ionicons/icons';
import Tree from './Tree';
import { getLogger } from '../core';
import { TreeProps } from "./TreeProps";
import { TreeContext, UnsyncedTreesContext } from './TreeProvider';
import { useNetwork } from '../network/useNetwork';
import { NetworkContext, NetworkState } from '../network/NetworkProvider';
import { Preferences } from '@capacitor/preferences';
import { AuthContext, AuthState } from '../auth/AuthProvider';
import AnimationDemo from "./Animation";

interface InfinitScrollState {
  scrollTrees: TreeProps[]
  lastIndex: number
}

const initialScrollState: InfinitScrollState = {
  scrollTrees: [],
  lastIndex: 0
}

const log = getLogger("TreeList");
const TreeList: React.FC<RouteComponentProps> = ({ history}) => {
  const { trees, fetching, fetchingError } = useContext(TreeContext);
  
  const { connected, connectionType } = useContext<NetworkState>(NetworkContext);
  
  const { logout } = useContext<AuthState>(AuthContext);
  
  const unsyncedTrees = useContext(UnsyncedTreesContext);
  
  const [scrollState, setScrollState] = useState<InfinitScrollState>(initialScrollState);
  const [disableInfiniteScroll, setDisableInfiniteScroll] = useState<boolean>(false);
  const { scrollTrees, lastIndex } = scrollState;
  const [filter, setFilter] = useState<boolean | undefined>(undefined);

  const [searchName, setSearchName] = useState<string>('');


  async function fetchData(newFilter: boolean) {
    let filteredData = trees;
    if (filter === true) {
      filteredData = trees?.filter((tree) => tree.isConiferous === true);
    } else if (filter === false) {
      filteredData = trees?.filter((tree) => tree.isConiferous === false);
    }
    if (searchName !== '') {
      filteredData = filteredData?.filter((tree) => tree.name.includes(searchName));
    }
    log("filteredData: " + filteredData?.length)
    if (filteredData) {
      if (newFilter) {
        if (9 > filteredData.length) {
          setScrollState({
            scrollTrees: filteredData, 
            lastIndex: filteredData.length
          });
          setDisableInfiniteScroll(true);
        }
        else {
          setScrollState({
            scrollTrees: [...filteredData.slice(0, 9)], 
            lastIndex: 9
          });
          setDisableInfiniteScroll(false)
        }
      }
      else if (lastIndex + 9 > filteredData.length) {
        setScrollState({
          scrollTrees: filteredData, 
          lastIndex: filteredData.length
        });
        setDisableInfiniteScroll(true);
      }
      else{
        setScrollState({
          scrollTrees: [...filteredData.slice(0, lastIndex + 9)], 
          lastIndex: lastIndex + 9
        });
        setDisableInfiniteScroll(false)
      }
    }
  }

  useEffect(() => {
    fetchData(false);
  }, [trees]);

  useEffect(() => {
    log("filtering trees");
    fetchData(true);
  }, [filter]);

  useEffect(() => {
    log("searching trees");
    fetchData(true);
  }, [searchName]);

  async function searchNext($event: CustomEvent<void>) {
    await fetchData(false);
    await ($event.target as HTMLIonInfiniteScrollElement).complete();
  }

  const handleLogout = useCallback(async () => {
    await logout?.();
    history.push("/login");
  }, []);
  //<IonLoading isOpen={fetching} message="Fetching trees" />

  log("render");
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>Tree App</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <AnimationDemo/>
        <div className="myAnimationClass">Network status is {JSON.stringify({connected: connected? connected : connected, connectionType})}</div>
        
        <IonRadioGroup value={filter} onIonChange={(e) => setFilter(e.detail.value)}>
          <IonRadio value={true}>IsConiferous</IonRadio><br />
          <IonRadio value={false}>NotConiferous</IonRadio><br />
          <IonRadio value={undefined}>Both</IonRadio><br />
        </IonRadioGroup>
        <IonSearchbar
          value={searchName}
          debounce={1000}
          onIonInput={e => setSearchName(e.detail.value!)}>
        </IonSearchbar>
        {unsyncedTrees && (
          <IonList>
            {unsyncedTrees.map(({_id, name, dateOfPlantation, isConiferous, image, lat, lon}) =>
              <div>
                <Tree
                key={_id} 
                _id={_id} 
                name={name} 
                dateOfPlantation={dateOfPlantation} 
                isConiferous={isConiferous}
                image={image}
                lat={lat}
                lon={lon}
                onEdit={_id => history.push(`/tree/${_id}`)}/>
                <span style={{ color: 'red' }}>NOT SENT TO SERVER</span>
              </div>
              )}

          </IonList>
        )}
        {scrollTrees && (
          <IonList>
            {scrollTrees.map(({_id, name, dateOfPlantation, isConiferous, image, lat, lon}) =>
              <Tree 
              key={_id} 
              _id={_id} 
              name={name} 
              dateOfPlantation={dateOfPlantation} 
              isConiferous={isConiferous}
              image={image}
              lat={lat}
              lon={lon}
              onEdit={_id => history.push(`/tree/${_id}`)}/>)}
          </IonList>
        )}
        {fetchingError && (
          <div>{fetchingError.message || "Failed to fetch trees"}</div>
        )}
        <IonInfiniteScroll threshold="100px" disabled={disableInfiniteScroll}
                           onIonInfinite={(e: CustomEvent<void>) => searchNext(e)}>
          <IonInfiniteScrollContent
            loadingText="Loading more trees...">
          </IonInfiniteScrollContent>
        </IonInfiniteScroll>
        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => history.push("/tree")}>
            <IonIcon icon={add} />
          </IonFabButton>
          <IonFabButton onClick={handleLogout}>
            <IonIcon icon={logOut} />
          </IonFabButton>
        </IonFab>
      </IonContent>
    </IonPage>
  );
};

export default TreeList;