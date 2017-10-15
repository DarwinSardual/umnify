package com.example.darwin.umnify.poseidon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.darwin.umnify.R;

public class PoseidonAlertActivity extends AppCompatActivity {

    private ImageButton toolbarBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poseidon_alert);

        toolbarBackButton = (ImageButton) findViewById(R.id.back);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
