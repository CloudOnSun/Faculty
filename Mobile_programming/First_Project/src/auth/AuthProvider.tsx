import React, { useCallback, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { getLogger } from '../core';
import { login as loginApi } from './authApi';
import { Preferences } from '@capacitor/preferences';

const log = getLogger('AuthProvider');

type LoginFn = (username?: string, password?: string) => void;
type LogOutFn = () => void;

export interface AuthState {
  authenticationError: Error | null;
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  login?: LoginFn;
  logout?: LogOutFn;
  pendingAuthentication?: boolean;
  username?: string;
  password?: string;
  token: string;
}

const initialState: AuthState = {
  isAuthenticated: false,
  isAuthenticating: false,
  authenticationError: null,
  pendingAuthentication: false,
  token: '',
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
  children: PropTypes.ReactNodeLike,
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>(initialState);
  const { isAuthenticated, isAuthenticating, authenticationError, pendingAuthentication, token } = state;
  const login = useCallback<LoginFn>(loginCallback, []);
  const logout = useCallback<LogOutFn>(async () => {
    await Preferences.remove({ key: 'authtoken' });
    setState(initialState);
  }, []);
  useEffect(authenticationEffect, [pendingAuthentication]);
  const value = { isAuthenticated, login, isAuthenticating, authenticationError, token, logout };
  log('render');
  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );

  function loginCallback(username?: string, password?: string): void {
    log('login');
    setState({
      ...state,
      pendingAuthentication: true,
      username,
      password
    });
  }

  function authenticationEffect() {
    let canceled = false;
    authenticate();
    return () => {
      canceled = true;
    }

    async function authenticate() {
      if (!pendingAuthentication) {
        log('authenticate, !pendingAuthentication, return');
        return;
      }
      try {
        log('authenticate...');
        setState({
          ...state,
          isAuthenticating: true,
        });
        const res = await Preferences.get({ key: 'authtoken' });
        console.log(res);
        if (res.value) {
          log('authenticate succeeded');
          setState({
            ...state,
            token: res.value,
            pendingAuthentication: false,
            isAuthenticated: true,
            isAuthenticating: false,
          });
        } else {
          const { username, password } = state;
          if (!username || !password) {
            setState(initialState)
            log("NOT YET COMPLETATE")
            return;
          }
          const { token } = await loginApi(username, password);
          if (canceled) {
            return;
          }
          log('authenticate succeeded');
          setState({
            ...state,
            token,
            pendingAuthentication: false,
            isAuthenticated: true,
            isAuthenticating: false,
          });
          await Preferences.set({
            key: 'authtoken',
            value: token
          });
        }
      } catch (error) {
        if (canceled) {
          return;
        }
        log('authenticate failed');
        setState({
          ...state,
          authenticationError: error as Error,
          pendingAuthentication: false,
          isAuthenticating: false,
        });
      }
    }
  }
};
