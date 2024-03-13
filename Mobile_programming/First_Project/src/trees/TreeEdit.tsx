import {RouteComponentProps} from "react-router";
import {getLogger} from "../core";
import React, {useCallback, useContext, useEffect, useState} from "react";
import {TreeContext} from "./TreeProvider";
import {TreeProps} from "./TreeProps";
import {
    IonButton,
    IonButtons,
    IonContent,
    IonFab, IonFabButton,
    IonHeader, IonIcon, IonImg,
    IonInput,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from "@ionic/react";
import {camera, text} from "ionicons/icons";
import {usePhotos} from "./usePhotos";
import {MyModal} from "./MyModal";

const log = getLogger("ItemEdit");

interface TreeEditProps extends RouteComponentProps<{
    id?: string;
}> {
}

const TreeEdit: React.FC<TreeEditProps> = ({history, match}) => {
    const {trees, saving, savingError, saveTree} = useContext(TreeContext);
    const [name, setName] = useState('');
    const [lat, setLat] = useState<number | undefined>(0);
    const [lon, setLon] = useState<number | undefined>(0);
    const [image, setImage] = useState<string | undefined>('');
    const [tree, setTree] = useState<TreeProps>();
    const { photo, takePhoto, deletePhoto } = usePhotos();
    useEffect(() => {
        log("useEffect");
        const routeId = match.params.id || "";
        const tree = trees?.find(t => t._id === routeId);
        setTree(tree);
        console.log(tree);
        if (tree) {
            setName(tree.name);
            setImage(tree.image);
            setLat(tree.lat);
            setLon(tree.lon);
        }
        console.log(name);
        console.log(image);
    }, [match.params.id, trees]);
    const handleSave = () => {
        console.log(tree);
        const editedTree = tree ? {...tree, name, image, lat, lon} : {name, image, lat, lon};
        console.log(editedTree);
        saveTree && saveTree(editedTree).then(() => history.goBack());
    };

    useEffect(() => {
        if (photo)
            setImage(photo.webviewPath);
    }, [photo]);

    interface LocationClick {
        latitude: number;
        longitude: number
    }

    function onLocationClick(source: string) {
        return ({latitude, longitude}: LocationClick) => {setLat(latitude); setLon(longitude)};
    }

    log("render");
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Edit</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave}>
                            Save
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <div>Name</div>
                <IonInput value={name} onIonChange={e => setName(e.detail.value || '')}/>
                <IonLoading isOpen={saving}/>
                {savingError && (
                    <div>{savingError.message || 'Failed to save tree'}</div>
                )}
            </IonContent>
            <IonContent>
                <div>Latitudine</div>
                <IonInput value={lat} type="number" onIonChange={e => setLat(e.detail.value ? +(e.detail.value) : 0)}/>
            </IonContent>
            <IonContent>
                <div>Longitudine</div>
                <IonInput value={lon} type="number" onIonChange={e => setLon(e.detail.value ? +(e.detail.value) : 0)}/>
            </IonContent>
            <MyModal lon={lon} lat={lat} onLocationClick={onLocationClick}/>
            <IonContent>
                {image && (<IonImg src={image}/>)}
            </IonContent>
            <IonFab vertical="bottom" horizontal="center" slot="fixed">
                <IonFabButton onClick={() => takePhoto()}>
                    <IonIcon icon={camera}/>
                </IonFabButton>
            </IonFab>
        </IonPage>
    );
}

export default TreeEdit;