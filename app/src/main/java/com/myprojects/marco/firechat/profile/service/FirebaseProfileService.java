package com.myprojects.marco.firechat.profile.service;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by marco on 09/09/16.
 */

public class FirebaseProfileService implements ProfileService {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseUser firebaseUser;

    public FirebaseProfileService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    public void setPassword(String password) {
        try {
            firebaseUser.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // DO something
                        }
                    });
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void removeUser() {
        firebaseUser.delete();
        firebaseAuth.signOut();
    }

}
