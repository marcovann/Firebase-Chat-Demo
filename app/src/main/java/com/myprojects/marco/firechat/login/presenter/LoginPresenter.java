package com.myprojects.marco.firechat.login.presenter;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.login.data_model.Authentication;
import com.myprojects.marco.firechat.login.service.LoginService;
import com.myprojects.marco.firechat.login.view.LoginDisplayer;
import com.myprojects.marco.firechat.navigation.LoginNavigator;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by marco on 27/07/16.
 */

public class LoginPresenter {

    private final LoginService loginService;
    private final LoginDisplayer loginDisplayer;
    private final LoginNavigator navigator;

    private Subscription subscription;

    public LoginPresenter(LoginService loginService,
                          LoginDisplayer loginDisplayer,
                          LoginNavigator navigator) {
        this.loginService = loginService;
        this.loginDisplayer = loginDisplayer;
        this.navigator = navigator;
    }

    public void startPresenting() {
        navigator.attach(loginResultListener,forgotDialogListener);
        loginDisplayer.attach(actionListener);
        subscription = loginService.getAuthentication()
                .subscribe(authenticationSubscriber());
    }

    public void stopPresenting() {
        navigator.detach(loginResultListener,forgotDialogListener);
        loginDisplayer.detach(actionListener);
        subscription.unsubscribe();
    }

    private Subscriber<Authentication> authenticationSubscriber() {
        return new Subscriber<Authentication>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Authentication authentication) {
                if (authentication.isSuccess()) {
                    navigator.toMainActivity();
                } else {
                    loginDisplayer.showAuthenticationError(authentication.getFailure().getLocalizedMessage());
                }
            }
        };
    }

    private final LoginDisplayer.LoginActionListener actionListener = new LoginDisplayer.LoginActionListener() {

        @Override
        public void onGooglePlusLoginSelected() {
            navigator.toGooglePlusLogin();
        }

        @Override
        public void onEmailAndPassLoginSelected(String email, String password) {
            if (email.length() == 0) {
                loginDisplayer.showErrorFromResourcesString(R.string.login_snackbar_email_short);
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginDisplayer.showErrorFromResourcesString(R.string.login_snackbar_email_error);
                return;
            }
            if (password.length() == 0) {
                loginDisplayer.showErrorFromResourcesString(R.string.login_snackbar_password_short);
                return;
            }
            loginService.loginWithEmailAndPass(email, password);
        }

        @Override
        public void onRegistrationSelected() {
            navigator.toRegistration();
        }

        @Override
        public void onForgotSelected() {
            navigator.showForgotDialog();
        }

    };

    private final LoginNavigator.LoginResultListener loginResultListener = new LoginNavigator.LoginResultListener() {

        @Override
        public void onGoogleLoginSuccess(String tokenId) {
            loginService.loginWithGoogle(tokenId);
        }

        @Override
        public void onLoginFailed(String statusMessage) {
            loginDisplayer.showAuthenticationError(statusMessage);
        }
    };

    private final LoginNavigator.ForgotDialogListener forgotDialogListener = new LoginNavigator.ForgotDialogListener() {

        @Override
        public void onPositiveSelected(String email) {
            loginService.sendPasswordResetEmail(email);
        }

    };


}

