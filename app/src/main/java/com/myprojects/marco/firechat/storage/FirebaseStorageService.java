package com.myprojects.marco.firechat.storage;

import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myprojects.marco.firechat.rx.FirebaseObservableListeners;

import java.io.ByteArrayOutputStream;

import rx.Observable;

/**
 * Created by marco on 18/08/16.
 */

public class FirebaseStorageService implements StorageService {

    private final StorageReference firebaseStorage;

    private final FirebaseObservableListeners firebaseObservableListeners;


    public FirebaseStorageService(FirebaseStorage firebaseStorage, FirebaseObservableListeners firebaseObservableListeners) {
        this.firebaseStorage = firebaseStorage.getReference();
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public StorageReference getProfileImageReference(String image) {
        return firebaseStorage.child(image);
    }

    @Override
    public Observable<String> uploadImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();
        final String imageRef = bitmap.hashCode() + System.currentTimeMillis() + ".jpg";

        return firebaseObservableListeners.uploadTask(data, firebaseStorage.child(imageRef), imageRef);
    }

    @Override
    public void removeImage(String image) {
        firebaseStorage.child(image).delete();
    }

}
