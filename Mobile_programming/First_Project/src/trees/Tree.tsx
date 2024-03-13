import { getLogger } from "../core";
import { TreeProps } from "./TreeProps";
import React, { memo } from "react";
import {IonButton, IonImg, IonItem, IonLabel} from '@ionic/react';
import MyMap from "./MyMap";
import {MyModal} from "./MyModal";


const log = getLogger("Tree");

interface TreePropsExt extends TreeProps {
    onEdit: (_id?: string) => void;
}

const Tree: React.FC<TreePropsExt> = ({ _id,
                                          name,
                                          dateOfPlantation,
                                          isConiferous,
                                          image,
                                          lat,
                                          lon,
                                          onEdit }) => {
    return (
        <IonItem>
            <IonLabel>
                <h2>{name}</h2>
                <p>ID: {_id}</p>
                <p>Date of Plantation: {dateOfPlantation ? dateOfPlantation.toString() : "unknown"}</p>
                <p>Is Coniferous: {isConiferous ? 'Yes' : 'No'}</p>
                <p>Latitude: { lat ? lat : "unkown"}</p>
                <p>Longitude: { lon ? lon : "unkown"}</p>
                {lat && lon &&
                    <MyModal lon={lon} lat={lat} onLocationClick={log}/>}
                <p><IonImg src={image}/></p>
            </IonLabel>
            <IonButton onClick={() => onEdit(_id)}>Edit</IonButton>
        </IonItem>
    );
    function log(source: string) {
        return (e: any) => console.log(source, e);
    }
};

export default memo(Tree);