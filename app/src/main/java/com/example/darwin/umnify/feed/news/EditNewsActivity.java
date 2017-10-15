package com.example.darwin.umnify.feed.news;

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
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsDataActionWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditNewsActivity extends AppCompatActivity{

    private ImageButton backButton;
    private ImageButton submitButton;

    private EditText contentField;
    private Button addImageButton;
    private TextView addImageNameView;

    private Intent data = null;
    private News news = null;
    private Bundle bundle;
    private int remove = 0;
    private int id;

    private static final int SELECT_IMAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);
        bundle = getIntent().getExtras();
        id = bundle.getInt("NEWS_ID");

        backButton = (ImageButton) findViewById(R.id.back_button);
        submitButton = (ImageButton) findViewById(R.id.submit_button);

        contentField = (EditText) findViewById(R.id.content_field);
        addImageButton = (Button) findViewById(R.id.add_image);
        addImageNameView = (TextView) findViewById(R.id.image_name);
        fetchNewsData(id);

       ClickHandler handler = new ClickHandler();

        submitButton.setOnClickListener(handler);
        backButton.setOnClickListener(handler);
        addImageButton.setOnClickListener(handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == EditNewsActivity.SELECT_IMAGE){
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
        startActivityForResult(Intent.createChooser(intent, "Select Image"), EditNewsActivity.SELECT_IMAGE);

    }


    private void fetchNewsData(int id){
        WebServiceAsync async = new WebServiceAsync();
        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", id + "");

        FetchNewsDataActionWrapper fetchNewsDataActionWrapper =
                new FetchNewsDataActionWrapper(textDataOutput, this, new ProcessPostFetchData());
        async.execute(fetchNewsDataActionWrapper);
    }


    private void setContent(News news){

        contentField.setText(news.getContent());
        addImageNameView.setText(news.getImageFile());

        if(news.getImageFile() != null && news.getImageFile().length() > 0){
            addImageButton.setText("Remove image");
        }else{
            addImageButton.setText("Add image");
        }


    }

    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            if(view == submitButton){

                if(data != null){
                    data.putExtra("EDIT_NEWS_CONTENT", contentField.getText().toString());
                    data.putExtra("EDIT_NEWS_IMAGE_REMOVE", remove);
                    data.putExtra("NEWS_ID", id);
                    setResult(RESULT_OK, data);
                    EditNewsActivity.this.finish();

                }else if(data == null){
                    if(contentField.getText().length() == 0){
                        // much better do disable the submit button if content is empty
                        finish();
                    }else{

                        data = new Intent();
                        data.putExtra("NEWS_ID", id);
                        data.putExtra("EDIT_NEWS_CONTENT", contentField.getText().toString());
                        data.putExtra("EDIT_NEWS_IMAGE_REMOVE", remove);
                        setResult(RESULT_OK, data);
                        EditNewsActivity.this.finish();
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

    private class ProcessPostFetchData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){
                try{
                    JSONObject str = new JSONObject(jsonResponse);
                    String data = str.getString("data");
                    JSONArray dataList = new JSONArray(data);
                    for(int counter = 0; counter < dataList.length(); counter++){
                        String newsData = dataList.getString(counter);
                        news = NewsHelper.createNewsFromJSON(new JSONObject(newsData), -1);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                setContent(news);
            }else{

            }
        }
    }
}
