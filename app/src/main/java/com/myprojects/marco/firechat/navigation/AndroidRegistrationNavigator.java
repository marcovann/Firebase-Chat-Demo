package com.myprojects.marco.firechat.navigation;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by marco on 28/07/16.
 */

public class AndroidRegistrationNavigator implements RegistrationNavigator {

    private final AppCompatActivity activity;

    public AndroidRegistrationNavigator(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void toLogin() {
        activity.finish();
    }

    @Override
    public void toMainActivity() {

    }

    @Override
    public void toParent() {

    }

    @Override
    public void attach(RegistrationResultListener registrationResultListener) {
    }

    @Override
    public void detach(RegistrationResultListener registrationResultListener) {
    }
}
