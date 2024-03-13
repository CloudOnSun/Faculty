package Florea_Flaviu_ISS.rpcprotocol;

import java.io.Serializable;

public enum RequestType implements Serializable {
    LOG_IN, LOG_OUT, ADD_SPEC, FIND_ALL_SPEC, FILTRE_SPEC, DEL_SPEC, UP_SPEC, FIND_LOCS, ADD_REZ, DEL_REZ
}
