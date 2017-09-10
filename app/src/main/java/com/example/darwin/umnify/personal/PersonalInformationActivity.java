package com.example.darwin.umnify.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.darwin.umnify.R;

public class PersonalInformationActivity extends AppCompatActivity {

    private Bundle userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        userData = getIntent().getExtras();


    }
}
