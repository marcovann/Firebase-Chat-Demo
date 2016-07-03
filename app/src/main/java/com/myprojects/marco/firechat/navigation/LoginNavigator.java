package com.myprojects.marco.firechat.navigation;

/**
 * Created by marco on 27/07/16.
 */

public interface LoginNavigator extends Navigator {

    void toGooglePlusLogin();

    void toRegistration();

    void showForgotDialog();

    void attach(LoginResultListener loginResultListener, ForgotDialogListener forgotDialogListener);

    void detach(LoginResultListener loginResultListener, ForgotDialogListener forgotDialogListener);

    interface LoginResultListener {

        void onGoogleLoginSuccess(String tokenId);

        void onLoginFailed(String statusMessage);

    }

    interface ForgotDialogListener {

        void onPositiveSelected(String email);

    }

}
