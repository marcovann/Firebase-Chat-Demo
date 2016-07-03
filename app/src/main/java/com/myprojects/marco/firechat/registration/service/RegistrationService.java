package com.myprojects.marco.firechat.registration.service;

import rx.Observable;

/**
 * Created by marco on 28/07/16.
 */

public interface RegistrationService {

    Observable<Boolean> getRegistration();

    void registerWithEmailAndPass(String email, String password);

}
