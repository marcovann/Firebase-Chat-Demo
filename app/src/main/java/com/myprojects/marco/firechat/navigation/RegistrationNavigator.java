package com.myprojects.marco.firechat.navigation;

/**
 * Created by marco on 28/07/16.
 */

public interface RegistrationNavigator extends Navigator {

    void toLogin();

    void attach(RegistrationResultListener registrationResultListener);

    void detach(RegistrationResultListener registrationResultListener);

    interface RegistrationResultListener {

        void onRegistrationSuccess(String statusMessage);

        void onRegistrationFailed(String statusMessage);

    }
}
