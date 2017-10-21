package com.example.darwin.umnify.feed.blogs;

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

public class AddBlogActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageButton submitButton;

    private EditText headingField;
    private EditText contentField;

    private Button addImageButton;
    private TextView addImageNameView;

    private static final int SELECT_IMAGE = 6;

    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        ClickHandler handler = new ClickHandler();


        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(handler);
        submitButton = (ImageButton) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(handler);

        headingField = (EditText) findViewById(R.id.heading);
        contentField = (EditText) findViewById(R.id.content);

        addImageButton = (Button) findViewById(R.id.add_image);
        addImageNameView = (TextView) findViewById(R.id.add_blog_image_name);
        addImageButton.setOnClickListener(handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AddBlogActivity.SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
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

            }else{

            }
        }else{

        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), AddBlogActivity.SELECT_IMAGE);

    }


    private class ClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view == submitButton) {

                if (data != null) {
                    data.putExtra("ADD_BLOG_HEADING", headingField.getText().toString());
                    data.putExtra("ADD_BLOG_CONTENT", contentField.getText().toString());
                    setResult(RESULT_OK, data);
                    AddBlogActivity.this.finish();
                } else if (data == null) {
                    if (contentField.getText().length() == 0 || headingField.getText().length() == 0) {
                        // much better do disable the submit button if content is empty
                        finish();
                    } else {

                        data = new Intent();
                        data.putExtra("ADD_BLOG_HEADING", headingField.getText().toString());
                        data.putExtra("ADD_BLOG_CONTENT", contentField.getText().toString());
                        setResult(RESULT_OK, data);
                        AddBlogActivity.this.finish();
                    }

                }
            }else if (view == backButton) {
                //check here if there's any data entered and prompt if the user wants to exit
                finish();
            } else if (view == addImageButton) {
                showFileChooser();
            }
        }
    }
}
