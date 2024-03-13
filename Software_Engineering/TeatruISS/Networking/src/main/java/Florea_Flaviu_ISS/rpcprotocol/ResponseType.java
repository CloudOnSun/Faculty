package Florea_Flaviu_ISS.rpcprotocol;

import java.io.Serializable;

public enum ResponseType  implements Serializable {
    OK, ERROR, LOGED_IN, GOT_ALL_SPEC, GOT_FILTRE_SPEC, SPEC_ADDED, GOT_LOCS, REZ_ADDED, LOCS_UPDATE
}
