package com.example.darwin.umnify.login;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import com.example.darwin.umnify.R;

class LoginActivityLayout{

    private Button login;
    private EditText usernameField;
    private EditText passwordField;
    private Activity activity;

    public LoginActivityLayout(Activity activity){

        this.activity = activity;


        init();
    }

    public void init(){
        setLoginButton();
        setPasswordField();
        setPasswordField();
    }

    private void setLoginButton(){
        login = (Button) activity.findViewById(R.id.login);
    }

    private void setUsernameField(){
        usernameField = (EditText) activity.findViewById(R.id.login_username_field);
    }

    private void setPasswordField(){
        passwordField = (EditText) activity.findViewById(R.id.login_password_field);
    }

    public Button getLoginButton(){

        return login;
    }

    public EditText getUsernameField(){

        return usernameField;
    }

    public EditText getPasswordField() {
        return passwordField;
    }
}