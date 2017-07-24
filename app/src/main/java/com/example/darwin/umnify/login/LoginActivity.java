package com.example.darwin.umnify.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.darwin.umnify.R;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityLayout layout;
    private LoginActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout = new LoginActivityLayout(LoginActivity.this);
        layout.init();

        controller = new LoginActivityController(layout);

    }


    }

