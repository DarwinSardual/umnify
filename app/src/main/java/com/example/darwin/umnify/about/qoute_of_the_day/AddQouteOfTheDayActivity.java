package com.example.darwin.umnify.about.qoute_of_the_day;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.darwin.umnify.R;

public class AddQouteOfTheDayActivity extends AppCompatActivity {

    private EditText contentField;
    private ImageButton backButtton;
    private ImageButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qoute_of_the_day);

        contentField = (EditText) findViewById(R.id.content_field);
        backButtton = (ImageButton) findViewById(R.id.back);
        submitButton = (ImageButton) findViewById(R.id.submit);

        ClickHandler handler = new ClickHandler();
        submitButton.setOnClickListener(handler);
        backButtton.setOnClickListener(handler);
    }

    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            if(view == submitButton){

                Intent data = new Intent();
                data.putExtra("ADD_QOUTE_CONTENT", contentField.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }else if(view ==backButtton){
                finish();
            }

        }
    }
}
