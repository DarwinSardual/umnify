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
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.announcements.data_action_wrapper.FetchAnnouncementDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.EditBlogActivity;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogDataActionWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditAnnouncementActivity extends AppCompatActivity{

    private ImageButton backButton;
    private ImageButton submitButton;

    private EditText titleField;
    private EditText contentField;

    private Button addImageButton;
    private TextView addImageNameView;

    private Intent data;

    private static final int SELECT_IMAGE = 6;

    private Bundle bundle;
    private int remove = 0;
    private int id;

    private Announcement announcement = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_announcement);

        bundle = getIntent().getExtras();
        id = bundle.getInt("ANNOUNCEMENT_ID");

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


        fetchAnnouncementData(id);
    }

    private void fetchAnnouncementData(int id){
        WebServiceAsync async = new WebServiceAsync();
        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", id + "");

        FetchAnnouncementDataActionWrapper fetchAnnouncementDataActionWrapper =
                new FetchAnnouncementDataActionWrapper(textDataOutput, this, new ProcessPostFetchAnnouncement());
        async.execute(fetchAnnouncementDataActionWrapper);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == EditAnnouncementActivity.SELECT_IMAGE){
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
        startActivityForResult(Intent.createChooser(intent, "Select Image"), EditAnnouncementActivity.SELECT_IMAGE);

    }

    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            if(view == submitButton){

                if(data != null){
                    data.putExtra("EDIT_ANNOUNCEMENT_TITLE", titleField.getText().toString());
                    data.putExtra("EDIT_ANNOUNCEMENT_CONTENT", contentField.getText().toString());
                    data.putExtra("EDIT_ANNOUNCEMENT_IMAGE_REMOVE", remove);
                    data.putExtra("ANNOUNCEMENT_ID", id);
                    setResult(RESULT_OK, data);
                    EditAnnouncementActivity.this.finish();

                }else if(data == null){
                    if(contentField.getText().length() == 0 || titleField.getText().length() == 0){
                        // much better do disable the submit button if content is empty
                        finish();
                    }else{

                        data = new Intent();
                        data.putExtra("ANNOUNCEMENT_ID", id);
                        data.putExtra("EDIT_ANNOUNCEMENT_TITLE", titleField.getText().toString());
                        data.putExtra("EDIT_ANNOUNCEMENT_CONTENT", contentField.getText().toString());
                        data.putExtra("EDIT_ANNOUNCEMENT_IMAGE_REMOVE", remove);
                        setResult(RESULT_OK, data);
                        EditAnnouncementActivity.this.finish();
                    }
                }

            }else if(view == backButton){
                //check here if there's any data entered and prompt if the user wants to exit
                finish();
            }else if(view == addImageButton){

                if((addImageNameView.getText() == null || addImageNameView.getText().length() == 0) && data == null){
                    showFileChooser();

                }else{
                    addImageButton.setText("Add image");
                    addImageNameView.setText(null);
                    data = null;
                    remove++;
                }

            }
        }
    }

    private void setContent(Announcement announcement){

        if(announcement != null){
            titleField.setText(announcement.getTitle());
            contentField.setText(announcement.getContent());
            addImageNameView.setText(announcement.getImageFile());

            if(announcement.getImageFile() != null && announcement.getImageFile().length() > 0){
                addImageButton.setText("Remove image");
            }else{
                addImageButton.setText("Add image");
            }
        }
    }

    private class ProcessPostFetchAnnouncement implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){
                try{
                    JSONObject str = new JSONObject(jsonResponse);
                    String data = str.getString("data");
                    JSONArray dataList = new JSONArray(data);
                    for(int counter = 0; counter < dataList.length(); counter++){
                        String announcementData = dataList.getString(counter);
                        announcement = AnnouncementHelper.createAnnouncementFromJSON(new JSONObject(announcementData));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                setContent(announcement);
            }else{

            }
        }
    }
}
