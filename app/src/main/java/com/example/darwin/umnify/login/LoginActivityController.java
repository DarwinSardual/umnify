package com.example.darwin.umnify.login;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.database.UMnifyDbHelper;

import com.example.darwin.umnify.authentication.AuthenticationKeys;
import com.example.darwin.umnify.home.HomeActivity;
import com.example.darwin.umnify.login.data_action_wrapper.LoginDataActionWrapper;

import java.util.HashMap;

public class LoginActivityController {

    private LoginActivityLayout layout;
    private Activity activity;

    private Button loginButton;
    private Button guestButton;

    public LoginActivityController(Activity activity, LoginActivityLayout layout){

        this.layout = layout;
        this.activity = activity;

        setUpClickActionsButton();
    }

    private void setUpClickActionsButton(){

        loginButton = layout.getLoginButton();
        guestButton = layout.getGuestButton();

        ClickHandler handler = new ClickHandler();

        loginButton.setOnClickListener(handler);
        guestButton.setOnClickListener(handler);

    }

    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            Button button = (Button) view;

            if(button == loginButton){

                authenticateLogin(button);

            }else if(button == guestButton){
                guestLogin();
            }
        }
    }

    private void authenticateLogin(View source){

        String id = layout.getUsernameField().getText().toString();
        String password = layout.getPasswordField().getText().toString();

        HashMap<String, String> textData = new HashMap<>();
        textData.put(AuthenticationKeys.USER_ID_KEY, id);
        textData.put(AuthenticationKeys.USER_PASSWORD_KEY, password);

        LoginDataActionWrapper loginDataWrapper = new LoginDataActionWrapper(textData,
                activity, source);

        WebServiceAsync async = new WebServiceAsync();
        async.execute(loginDataWrapper);
    }

    private void guestLogin(){

        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra("USER_TYPE", AuthenticationCodes.GUEST_USER);
        activity.startActivity(intent);
    }
}
