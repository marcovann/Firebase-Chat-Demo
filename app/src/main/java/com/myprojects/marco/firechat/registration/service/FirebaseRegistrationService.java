package com.myprojects.marco.firechat.registration.service;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxrelay.BehaviorRelay;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * Created by marco on 28/07/16.
 */

public class FirebaseRegistrationService implements RegistrationService {

    private FirebaseAuth firebaseAuth;
    private BehaviorRelay<Boolean> registerRelay;
    private Boolean isRegistrationCompleted;

    public FirebaseRegistrationService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.isRegistrationCompleted = false;
        registerRelay = BehaviorRelay.create();
    }

    @Override
    public Observable<Boolean> getRegistration() {
        return registerRelay.startWith(initRelay());
    }

    private Observable<Boolean> initRelay() {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                if (registerRelay.hasValue() && registerRelay.getValue()) {
                    return Observable.empty();
                } else {
                    return Observable.create(new Observable.OnSubscribe<Boolean>() {
                        @Override
                        public void call(Subscriber<? super Boolean> subscriber) {
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void registerWithEmailAndPass(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            isRegistrationCompleted = true;
                        }
                        registerRelay.call(isRegistrationCompleted);
                    }
                });
    }

}
