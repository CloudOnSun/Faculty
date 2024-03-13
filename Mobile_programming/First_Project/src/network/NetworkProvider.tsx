import React, { useCallback, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { PluginListenerHandle } from '@capacitor/core';
import { ConnectionStatus, Network } from '@capacitor/network';

export interface NetworkState {
    connected: boolean,
    connectionType: string;
}

const initialState: NetworkState = {
    connected: false,
    connectionType: 'unkown'
};

export const NetworkContext = React.createContext<NetworkState>(initialState);

interface NetworkProviderProps {
    children: PropTypes.ReactNodeLike;
}

export const NetworkProvider: React.FC<NetworkProviderProps> = ({children}) => {
    const [networkStatus, setNetworkStatus] = useState<NetworkState>(initialState)
    const { connected, connectionType } = networkStatus;
    const value = {connected, connectionType};
    useEffect(() => {
        let handler: PluginListenerHandle;
        registerNetworkStatusChange();
        Network.getStatus().then(handleNetworkStatusChange);
        let canceled = false;
        return () => {
          canceled = true;
          handler?.remove();
        }
    
        async function registerNetworkStatusChange() {
          handler = await Network.addListener('networkStatusChange', handleNetworkStatusChange);
        }
    
        async function handleNetworkStatusChange(status: ConnectionStatus) {
          console.log('useNetwork - status change', status);
          if (!canceled) {
            setNetworkStatus(status);
          }
        }
      }, [])
    return (
        <NetworkContext.Provider value={value}>
            {children}
        </NetworkContext.Provider>
    );
}