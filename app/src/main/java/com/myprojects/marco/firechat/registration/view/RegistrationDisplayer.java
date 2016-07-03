package com.myprojects.marco.firechat.registration.view;

/**
 * Created by marco on 28/07/16.
 */

public interface RegistrationDisplayer {

    void attach(RegistrationActionListener actionListener);

    void detach(RegistrationActionListener actionListener);

    void showRegistrationAlertDialog(int id);

    void showErrorFromResourcesString(int id);

    public interface RegistrationActionListener {

        void onRegistrationSubmit(String email, String password, String confirm);

        void onLoginSelected();

        void onAlertDialogDismissed();

    }

}
