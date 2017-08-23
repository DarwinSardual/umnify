package com.example.darwin.umnify.login;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Button;
import android.widget.EditText;
import com.example.darwin.umnify.R;

class LoginActivityLayout{

    private Button login;
    private Button guest;
    private EditText usernameField;
    private EditText passwordField;
    private Activity activity;
    private CoordinatorLayout mainContainer;

    public LoginActivityLayout(Activity activity){

        this.activity = activity;


        init();
    }

    public void init(){
        setLoginButton();
        setUsernameField();
        setPasswordField();
        setGuestButton();
    }

    private void setLoginButton(){
        login = (Button) activity.findViewById(R.id.login);
    }
    private void setGuestButton(){
        guest = (Button) activity.findViewById(R.id.guest);
    }

    private void setUsernameField(){
        usernameField = (EditText) activity.findViewById(R.id.login_username_field);
    }

    private void setPasswordField(){
        passwordField = (EditText) activity.findViewById(R.id.login_password_field);
    }

    private void setMainContainer(){ mainContainer = (CoordinatorLayout) activity.findViewById(R.id.login_main_container);}

    public Button getLoginButton(){

        return login;
    }

    public EditText getUsernameField(){

        return usernameField;
    }

    public Button getGuestButton() {
        return guest;
    }

    public EditText getPasswordField() {
        return passwordField;
    }

    public CoordinatorLayout getMainContainer() {
        return mainContainer;
    }
}