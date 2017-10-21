package com.example.darwin.umnify.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.darwin.umnify.R;

public class PersonalInformationActivity extends AppCompatActivity {

    private Bundle userData;
    private ImageButton toolbarBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        userData = getIntent().getExtras();

        toolbarBackButton = (ImageButton) findViewById(R.id.toolbar_back);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
