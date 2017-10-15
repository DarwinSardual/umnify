package com.example.darwin.umnify.about.trivia;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.feed.news.AddNewsActivity;

public class AddTrivia extends AppCompatActivity {

    private EditText contentField;
    private ImageButton backButtton;
    private ImageButton submitButton;

    private Button addImageButton;
    private TextView addImageNameView;

    private Intent data = null;

    private static final int SELECT_IMAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trivia);

        contentField = (EditText) findViewById(R.id.content_field);
        backButtton = (ImageButton) findViewById(R.id.back);
        submitButton = (ImageButton) findViewById(R.id.submit);
        addImageButton = (Button) findViewById(R.id.add_image);
        addImageNameView = (TextView) findViewById(R.id.image_name);

        ClickHandler handler = new ClickHandler();
        submitButton.setOnClickListener(handler);
        backButtton.setOnClickListener(handler);
        addImageButton.setOnClickListener(handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SELECT_IMAGE){
            if(resultCode ==  RESULT_OK){
                this.data = data;
                if(data != null){

                    Uri uri = data.getData();
                    Cursor returnCursor;
                    returnCursor =
                            this.getContentResolver()
                                    .query(uri,
                                            null, null,
                                            null, null);

                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    returnCursor.moveToFirst();

                    String imageFile = returnCursor.getString(nameIndex);
                    addImageNameView.setText(imageFile);
                    addImageButton.setText("Remove image");
                }

            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);

    }

    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            if(view == submitButton){

                if(data != null){
                    data.putExtra("ADD_TRIVIA_CONTENT", contentField.getText().toString());
                    setResult(RESULT_OK, data);
                    finish();

                }else if(data == null){
                    if(contentField.getText().length() == 0){
                        // much better do disable the submit button if content is empty
                        finish();
                    }else{

                        data = new Intent();
                        data.putExtra("ADD_TRIVIA_CONTENT", contentField.getText().toString());
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
            }else if(view ==backButtton){
                finish();
            }else if(view == addImageButton){

                if(data == null){
                    showFileChooser();

                }else{
                    addImageButton.setText("Add image");
                    addImageNameView.setText(null);
                    data = null;
                }

            }

        }
    }
}
