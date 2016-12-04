package com.myprojects.marco.firechat.navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.login.LoginGoogleApiClient;
import com.myprojects.marco.firechat.registration.RegistrationActivity;

/**
 * Created by marco on 27/07/16.
 */

public class AndroidLoginNavigator implements LoginNavigator {

    private static final int RC_SIGN_IN = 242;

    private final AppCompatActivity activity;
    private final LoginGoogleApiClient googleApiClient;
    private final Navigator navigator;
    private LoginResultListener loginResultListener;
    private ForgotDialogListener forgotDialogListener;


    public AndroidLoginNavigator(AppCompatActivity activity, LoginGoogleApiClient googleApiClient, Navigator navigator) {
        this.activity = activity;
        this.googleApiClient = googleApiClient;
        this.navigator = navigator;
    }

    @Override
    public void toGooglePlusLogin() {
        Intent signInIntent = googleApiClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void toRegistration() {
        activity.startActivity(new Intent(activity, RegistrationActivity.class));
    }

    @Override
    public void showForgotDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.login_dialog_forgot_title)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText(R.string.login_dialog_forgot_ok)
                .negativeText(R.string.login_dialog_forgot_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getInputEditText() != null)
                            forgotDialogListener.onPositiveSelected(dialog.getInputEditText().getText().toString());
                        dialog.dismiss();
                    }
                })
                .input(R.string.login_dialog_forgot_title, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                }).show();
    }

    @Override
    public void attach(LoginResultListener loginResultListener, ForgotDialogListener forgotDialogListener) {
        this.loginResultListener = loginResultListener;
        this.forgotDialogListener = forgotDialogListener;
    }

    @Override
    public void detach(LoginResultListener loginResultListener, ForgotDialogListener ForgotDialogListener) {
        this.loginResultListener = null;
        this.forgotDialogListener = null;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RC_SIGN_IN) {
            return false;
        }
        GoogleSignInResult result = googleApiClient.getSignInResultFromIntent(data);
        if (result != null && result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            loginResultListener.onGoogleLoginSuccess(account.getIdToken());
        } else {
            Log.e("Failed auth Google", result.getStatus().getStatusCode()+"");
            loginResultListener.onLoginFailed(result.getStatus().getStatusMessage());
        }
        return true;
    }

    @Override
    public void toLogin() {

    }

    @Override
    public void toMainActivity() {
        navigator.toMainActivity();
    }

    @Override
    public void toParent() {

    }
}
