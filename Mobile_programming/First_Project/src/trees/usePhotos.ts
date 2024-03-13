import { useEffect, useState } from 'react';
import { useCamera } from './useCamera';
import { useFilesystem } from './useFilesystem';
import { usePreferences } from './usePreferences';

export interface MyPhoto {
  filepath: string;
  webviewPath?: string;
}

const PHOTOS = 'photos';

export function usePhotos() {
  const [photo, setPhoto] = useState<MyPhoto>();
  const { getPhoto } = useCamera();
  const { readFile, writeFile, deleteFile } = useFilesystem();
  const { get, set } = usePreferences();
  // useEffect(loadPhotos, [get, readFile, setPhoto]);
  return {
    photo,
    takePhoto,
    deletePhoto,
  };

  async function takePhoto() {
    const { base64String } = await getPhoto();
    const filepath = new Date().getTime() + '.jpeg';
    await writeFile(filepath, base64String!);
    const webviewPath = `data:image/jpeg;base64,${base64String}`
    const newPhoto = { filepath, webviewPath };
    await set(PHOTOS, JSON.stringify(newPhoto));
    setPhoto(newPhoto);
  }

  async function deletePhoto(photo: MyPhoto) {
    await set(PHOTOS, JSON.stringify(""));
    await deleteFile(photo.filepath);
    setPhoto(undefined);
  }

  // function loadPhotos() {
  //   loadSavedPhotos();
  //
  //   async function loadSavedPhotos() {
  //     const savedPhotoString = await get(PHOTOS);
  //     const savedPhoto = (savedPhotoString ? JSON.parse(savedPhotoString) : []) as MyPhoto[];
  //     console.log('load', savedPhotos);
  //     for (let photo of savedPhotos) {
  //       const data = await readFile(photo.filepath);
  //       photo.webviewPath = `data:image/jpeg;base64,${data}`;
  //     }
  //     setPhotos(savedPhotos);
  //   }
  // }
}
