package com.myprojects.marco.firechat.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.myprojects.marco.firechat.BaseActivity;
import com.myprojects.marco.firechat.Dependencies;
import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.navigation.AndroidProfileNavigator;
import com.myprojects.marco.firechat.profile.presenter.ProfilePresenter;
import com.myprojects.marco.firechat.profile.view.ProfileDisplayer;

/**
 * Created by marco on 19/08/16.
 */

public class ProfileActivity extends BaseActivity {

    private ProfilePresenter presenter;
    private AndroidProfileNavigator navigator;

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.profile_toolbar_title);
        ProfileDisplayer profileDisplayer = (ProfileDisplayer) findViewById(R.id.profileView);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        navigator = new AndroidProfileNavigator(this);
        presenter = new ProfilePresenter(
                Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getProfileService(),
                Dependencies.INSTANCE.getStorageService(),
                profileDisplayer,
                navigator
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                //
                return;
            }
        }
    }

}