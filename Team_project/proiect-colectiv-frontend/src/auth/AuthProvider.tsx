import PropTypes from "prop-types";
import React, {useCallback, useEffect, useState} from "react";
import { Preferences } from '@capacitor/preferences';
import {getUser, loginApi} from "./auth-api";
import User from "../types/User";
import UserRole from "../types/UserRole";
import {getLogger} from "../core";

type LoginFn = (userEmail?: string, password?: string) => void;
type LogOutFn = () => void;
export type GetUserFn = () => void;

const log = getLogger("AuthProvider")
export interface AuthState {
    authenticationError: Error | null;
    isAuthenticated: boolean;
    isAuthenticating: boolean;
    login?: LoginFn;
    logout?: LogOutFn;
    pendingAuthentication?: boolean;
    userEmail?: string;
    password?: string;
    authenticationToken: string;
    refreshToken: string;
    user?: User;
    userId?: number;
    role?: UserRole;
    getUserFn?: GetUserFn;
}

const initialState: AuthState = {
    isAuthenticated: false,
    isAuthenticating: false,
    authenticationError: null,
    pendingAuthentication: false,
    authenticationToken: '',
    refreshToken: '',
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children}) => {
    const [state, setState] = useState<AuthState>(initialState);
    const { isAuthenticated,
            isAuthenticating,
            authenticationError,
            pendingAuthentication,
            authenticationToken,
            refreshToken,
            user,
            userId,
            role} = state;
    const login = useCallback<LoginFn>(loginCallback, []);
    const logout = useCallback<LogOutFn>(async () => {
        await Preferences.remove({ key: 'authtoken' });
        await Preferences.remove({ key: 'refreshtoken' });
        await Preferences.remove({ key: 'userRole'});
        await Preferences.remove({ key: 'userId'});
        setState(initialState);
    }, []);
    const getUserFn = useCallback<GetUserFn>(async () => {
        if (authenticationToken && role && userId) {
            const userResponse = await getUser(authenticationToken, role, userId)
            if (!userResponse) {
                logout();
                return;
            }
            setState({
                ...state,
                user: userResponse.data
            })
        }
    }, [authenticationToken, role, userId])
    useEffect(authenticationEffect, [pendingAuthentication]);
    const value = {
        isAuthenticated,
        login,
        isAuthenticating,
        authenticationError,
        authenticationToken,
        refreshToken,
        logout,
        user,
        userId,
        role};
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );

    function loginCallback(userEmail?: string, password?: string): void {
        setState({
            ...state,
            pendingAuthentication: true,
            userEmail,
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
                return;
            }
            try {
                setState({
                    ...state,
                    isAuthenticating: true,
                });
                const authTokenRes = await Preferences.get({ key: 'authtoken' });
                const refreshTokenRes = await Preferences.get({ key: 'refreshtoken' });
                const userRoleRes = await Preferences.get({ key: "userRole"});
                const userIdRes = await Preferences.get({key: "userId"});
                console.log(authTokenRes);
                if (authTokenRes.value && refreshTokenRes.value && userRoleRes.value && userIdRes.value) {
                    const userResponse = await getUser(authTokenRes.value,
                        userRoleRes.value as UserRole,
                        +userIdRes.value);
                    const loggedUser = userResponse?.data
                    if (!loggedUser) {
                        logout();
                        return;
                    }
                    console.log(loggedUser);
                    setState({
                        ...state,
                        authenticationToken: authTokenRes.value,
                        refreshToken: refreshTokenRes.value,
                        pendingAuthentication: false,
                        isAuthenticated: true,
                        isAuthenticating: false,
                        user: loggedUser,
                        userId: +userIdRes.value,
                        role: userRoleRes.value as UserRole,
                    });
                } else {
                    const { userEmail, password } = state;
                    if (!userEmail || !password) {
                        setState(initialState)
                        return;
                    }
                    const { data } = await loginApi(userEmail, password);
                    console.log(data.authenticationToken + "    " + data.refreshToken);
                    if (canceled) {
                        return;
                    }
                    const userResponse = await getUser(data.authenticationToken, data.role, data.userId);
                    const loggedUser = userResponse?.data;
                    console.log(loggedUser);
                    setState({
                        ...state,
                        authenticationToken: data.authenticationToken,
                        refreshToken:  data.refreshToken,
                        pendingAuthentication: false,
                        isAuthenticated: true,
                        isAuthenticating: false,
                        user: loggedUser,
                        userId: data.userId,
                        role: data.role
                    });
                    await Preferences.set({
                        key: 'authtoken',
                        value: data.authenticationToken
                    });
                    await Preferences.set({
                        key: 'refreshtoken',
                        value: data.refreshToken
                    })
                    await Preferences.set({
                        key: 'userRole',
                        value: data.role
                    })
                    await Preferences.set({
                        key: 'userId',
                        value: data.userId.toString()
                    })
                }
            } catch (error) {
                log("canceled")
                if (canceled) {
                    return;
                }
                setState({
                    ...state,
                    authenticationError: new Error("Authentification failed"),
                    pendingAuthentication: false,
                    isAuthenticating: false,
                });
                await Preferences.remove({ key: 'authtoken' });
                await Preferences.remove({ key: 'refreshtoken' });
                await Preferences.remove({ key: 'userRole'});
                await Preferences.remove({ key: 'userId'});
            }
        }
    }
}