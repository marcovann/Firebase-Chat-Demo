package com.myprojects.marco.firechat.rx;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by marco on 09/09/16.
 */

public class UploadTaskOnSubscribe<T,U> implements Observable.OnSubscribe<U> {

    private final T value;
    private final StorageReference storageReference;
    private final U returnValue;

    UploadTaskOnSubscribe(T value, StorageReference storageReference, U returnValue) {
        this.value = value;
        this.storageReference = storageReference;
        this.returnValue = returnValue;
    }

    @Override
    public void call(Subscriber<? super U> subscriber) {
        UploadTask uploadTask = storageReference.putBytes((byte[])value);
        uploadTask.addOnFailureListener(new RxFailureListener<>(subscriber))
                .addOnSuccessListener(new RxSuccessListener<>(subscriber,returnValue));
    }

    private static class RxSuccessListener<T> implements OnSuccessListener {

        private final Subscriber<? super T> subscriber;
        private final T successValue;

        RxSuccessListener(Subscriber<? super T> subscriber, T successValue) {
            this.subscriber = subscriber;
            this.successValue = successValue;
        }

        @Override
        public void onSuccess(Object o) {
            subscriber.onNext(successValue);
            subscriber.onCompleted();
        }

    }

    private static class RxFailureListener<T> implements OnFailureListener {

        private final Subscriber<? super T> subscriber;

        RxFailureListener(Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            subscriber.onError(e);
        }
    }

}
