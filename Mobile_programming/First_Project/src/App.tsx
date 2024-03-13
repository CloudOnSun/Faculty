import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import Home from './pages/Home';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/* Theme variables */
import './theme/variables.css';
import { TreeProvider } from './trees/TreeProvider';
import { TreeEdit, TreeList } from "./trees";
import { Login } from './auth/Login';
import { PrivateRoute } from './auth/PrivateRoute';
import { AuthProvider } from './auth/AuthProvider';
import { NetworkProvider } from './network/NetworkProvider';

setupIonicReact();

const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
      <IonRouterOutlet>
        <NetworkProvider>
          <AuthProvider>
            <Route path="/login" component={Login} exact={true} />
            <TreeProvider>
              <PrivateRoute path="/trees" component={TreeList} exact={true}/>
              <PrivateRoute path="/tree" component={TreeEdit} exact={true}/>
              <PrivateRoute path="/tree/:id" component={TreeEdit} exact={true}/>
            </TreeProvider>
            <Route exact path="/" render={() => <Redirect to="/trees"/>}/>
          </AuthProvider>
        </NetworkProvider>
      </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;
