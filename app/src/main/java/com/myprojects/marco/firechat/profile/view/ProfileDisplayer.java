package com.myprojects.marco.firechat.profile.view;

import android.graphics.Bitmap;
import android.widget.TextView;

import com.myprojects.marco.firechat.user.data_model.User;

/**
 * Created by marco on 09/09/16.
 */

public interface ProfileDisplayer {

    void display(User user);

    void onStartUpload();

    void onFinishUpload();

    void updateProfileImage(Bitmap bitmap);

    void attach(ProfileActionListener profileActionListener);

    void detach(ProfileActionListener profileActionListener);

    interface ProfileActionListener {

        void onUpPressed();

        void onNamePressed(String hint, TextView textView);

        void onPasswordPressed(String hint);

        void onImagePressed();

        void onRemovePressed();

        void onStartUpload();

        void onFinishUpload();

    }

}
