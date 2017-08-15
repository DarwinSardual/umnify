package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.RemoteDbConn;

import java.sql.Array;
import java.util.ArrayList;

public class AddNewsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageButton submitButton;

    private EditText contentField;
    private Button addImageButton;

    private Intent data = null;

    private static final int SELECT_IMAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        backButton = (ImageButton) findViewById(R.id.add_news_back_button);
        submitButton = (ImageButton) findViewById(R.id.add_news_submit_button);

        contentField = (EditText) findViewById(R.id.add_news_content_field);
        addImageButton = (Button) findViewById(R.id.add_news_add_image);

        ClickHandler handler = new ClickHandler();


        submitButton.setOnClickListener(handler);

        backButton.setOnClickListener(handler);

        addImageButton.setOnClickListener(handler);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == AddNewsActivity.SELECT_IMAGE){
            if(resultCode ==  RESULT_OK){
                this.data = data;

            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), AddNewsActivity.SELECT_IMAGE);

    }



    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            if(view == submitButton){

                if(data != null){
                    data.putExtra("ADD_NEWS_CONTENT", contentField.getText().toString());
                    setResult(RESULT_OK, data);
                    AddNewsActivity.this.finish();

                }else if(data == null){
                    if(contentField.getText().length() == 0){
                        // much better do disable the submit button if content is empty
                        finish();
                    }else{

                        data = new Intent();
                        data.putExtra("ADD_NEWS_CONTENT", contentField.getText().toString());
                        setResult(RESULT_OK, data);
                        AddNewsActivity.this.finish();
                    }
                }

            }else if(view == backButton){
                //check here if there's any data entered and prompt if the user wants to exit
                finish();
            }else if(view == addImageButton){
                showFileChooser();
            }
        }
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        finish();
    }
}
