package com.myprojects.marco.firechat.login.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.myprojects.marco.firechat.R;

/**
 * Created by marco on 27/07/16.
 */

// Presenter make request to FirebaseLoginService -> FirebaseAuthDatabase -> then I retrieve the model Authentication
// Activity init all the Firebase vars I need and the LoginView and LoginNavigator

public class LoginView extends CoordinatorLayout implements LoginDisplayer {

    private CoordinatorLayout layout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private View loginButton;
    private View forgotButton;
    private View registerButton;
    private View googleButton;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_login_view, this);

        layout = (CoordinatorLayout) this.findViewById(R.id.activity_login);
        emailEditText = (EditText) this.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);

        loginButton = this.findViewById(R.id.loginButton);
        forgotButton = this.findViewById(R.id.forgotButton);
        registerButton = this.findViewById(R.id.registerButton);
        googleButton = this.findViewById(R.id.google);
    }

    @Override
    public void attach(final LoginActionListener actionListener) {
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                actionListener.onEmailAndPassLoginSelected(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });
        forgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onForgotSelected();
            }
        });
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onRegistrationSelected();
            }
        });
        googleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onGooglePlusLoginSelected();
            }
        });
    }

    @Override
    public void detach(LoginActionListener actionListener) {
        googleButton.setOnClickListener(null);
        loginButton.setOnClickListener(null);
    }

    @Override
    public void showAuthenticationError(String message) {
        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorFromResourcesString(int id) {
        Snackbar.make(layout, getContext().getString(id), Snackbar.LENGTH_LONG).show();
    }


}
