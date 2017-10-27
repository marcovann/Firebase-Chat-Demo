package com.myprojects.marco.firechat.profile.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.user.data_model.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marco on 09/09/16.
 */

public class ProfileView extends LinearLayout implements ProfileDisplayer, View.OnClickListener {

    private Toolbar toolbar;

    private TextView emailTextView;
    private TextView nameTextView;
    private TextView passwordTextView;
    private CircleImageView profileImageView;
    private Button removeButton;

    private ProfileActionListener actionListener;

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_profile_view, this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        emailTextView = (TextView) findViewById(R.id.emailTextView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        profileImageView = (CircleImageView) findViewById(R.id.profileImageView);
        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        removeButton = (Button) findViewById(R.id.removeButton);
    }

    @Override
    public void display(User user) {
        emailTextView.setText(user.getEmail());
        nameTextView.setText(user.getName());
        Utils.loadImageElseBlack(user.getImage(),profileImageView,getContext());
    }

    @Override
    public void onStartUpload() {
        actionListener.onStartUpload();
    }

    @Override
    public void onFinishUpload() {
        actionListener.onFinishUpload();
    }

    @Override
    public void updateProfileImage(Bitmap bitmap) {
        profileImageView.setImageBitmap(bitmap);
        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
    }

    @Override
    public void attach(ProfileActionListener profileActionListener) {
        this.actionListener = profileActionListener;

        profileImageView.setOnClickListener(this);
        nameTextView.setOnClickListener(this);
        emailTextView.setOnClickListener(this);
        passwordTextView.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(navigationClickListener);
    }

    @Override
    public void detach(ProfileActionListener profileActionListener) {
        this.actionListener = null;

        profileImageView.setOnClickListener(null);
        nameTextView.setOnClickListener(null);
        emailTextView.setOnClickListener(null);
        passwordTextView.setOnClickListener(null);
        removeButton.setOnClickListener(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nameTextView:
                actionListener.onNamePressed("",nameTextView);
                break;
            case R.id.passwordTextView:
                actionListener.onPasswordPressed("");
                break;
            case R.id.profileImageView:
                actionListener.onImagePressed();
                break;
            case R.id.removeButton:
                actionListener.onRemovePressed();
                break;
        }
    }

    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            actionListener.onUpPressed();
        }
    };

}
