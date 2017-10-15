package com.example.darwin.umnify.feed.announcements;

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
import com.example.darwin.umnify.feed.blogs.AddBlogActivity;

public class AddAnnouncementActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageButton submitButton;

    private EditText titleField;
    private EditText contentField;

    private Button addImageButton;
    private TextView addImageNameView;

    private Intent data;

    private static final int SELECT_IMAGE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        ClickHandler handler = new ClickHandler();

        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(handler);
        submitButton = (ImageButton) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(handler);

        titleField = (EditText) findViewById(R.id.heading);
        contentField = (EditText) findViewById(R.id.content);

        addImageButton = (Button) findViewById(R.id.add_image);
        addImageNameView = (TextView) findViewById(R.id.image_name);
        addImageButton.setOnClickListener(handler);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), AddAnnouncementActivity.SELECT_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddAnnouncementActivity.SELECT_IMAGE) {
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

    private class ClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view == submitButton) {

                if (data != null) {
                    data.putExtra("ADD_ANNOUNCEMENT_TITLE", titleField.getText().toString());
                    data.putExtra("ADD_ANNOUNCEMENT_CONTENT", contentField.getText().toString());
                    setResult(RESULT_OK, data);
                    AddAnnouncementActivity.this.finish();
                } else if (data == null) {
                    if (contentField.getText().length() == 0 || titleField.getText().length() == 0) {
                        // much better do disable the submit button if content is empty
                        finish();
                    } else {

                        data = new Intent();
                        data.putExtra("ADD_ANNOUNCEMENT_TITLE", titleField.getText().toString());
                        data.putExtra("ADD_ANNOUNCEMENT_CONTENT", contentField.getText().toString());
                        setResult(RESULT_OK, data);
                        AddAnnouncementActivity.this.finish();
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
