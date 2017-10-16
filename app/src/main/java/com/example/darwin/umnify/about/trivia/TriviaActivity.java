package com.example.darwin.umnify.about.trivia;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.DataImageActionWrapper;
import com.example.darwin.umnify.ImageActionWrapper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.about.qoute_of_the_day.QouteOfTheDay;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.feed.news.data_action_wrapper.AddNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.feed_manager.NewsFeedManagerAdmin;
import com.example.darwin.umnify.wrapper.DataHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class TriviaActivity extends AppCompatActivity {

    private Bundle userData;
    private FloatingActionButton fab;
    private TextView contentView;
    private ImageView imageView;
    private Trivia trivia;
    private ImageButton backButton;

    public final static int ADD_TRIVIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userData = getIntent().getExtras();

        if(userData != null){
            if(userData.getInt("USER_TYPE") == AuthenticationCodes.SUPER_ADMIN_USER){
                setContentView(R.layout.activity_trivia);
                setup();
                fetchData();
                setupSuperAdmin();
            }else{
                setContentView(R.layout.activity_trivia);
                setup();
                fetchData();
            }
        }else{
            setContentView(R.layout.activity_trivia);
            setup();
            fetchData();
        }

        backButton= (ImageButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setup(){
        contentView = (TextView) findViewById(R.id.content);
        imageView = (ImageView) findViewById(R.id.image);
    }

    private void setupSuperAdmin(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TriviaActivity.this, AddTrivia.class);
                startActivityForResult(intent, ADD_TRIVIA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_TRIVIA){
            if(resultCode == RESULT_OK){
                addData(data);
            }
        }
    }

    public void fetchData(){

        WebServiceAsync async = new WebServiceAsync();

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("limit", "1");
        textDataOutput.put("order", "desc");

        DataActionWrapper fetchData = new DataActionWrapper(textDataOutput, this, AuthenticationAddress.FETCH_TRIVIA, new ProcessPostFetchData());
        async.execute(fetchData);
    }

    private void addData(Intent data){

        Uri uri = data.getData();
        Cursor returnCursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();


        if(uri != null){

            String mimeType = DataHelper.getMimeType(uri, this);

            returnCursor =
                    this.getContentResolver()
                            .query(uri,
                                    null, null,
                                    null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();

            String imageFile = returnCursor.getString(nameIndex);
            Bitmap image = null;
            byte[] byteArray = null;
            int orientation = 0;

            Bitmap rescaledImage = null;
            try{
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }

            byteArray = DataHelper.bitmapToByteArray(image, mimeType);
            textData.put("content", data.getStringExtra("ADD_TRIVIA_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") +"");
            Log.e("image", imageFile);
            fileData.put(imageFile, byteArray);

        }else{
            textData.put("content", data.getStringExtra("ADD_TRIVIA_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("user_type", userData.getInt("USER_TYPE") +"");
        }

        DataImageActionWrapper addTrivia = new DataImageActionWrapper(textData, fileData, this, AuthenticationAddress.ADD_TRIVIA, new ProcessPostAddData());

        WebServiceAsync asyncAddNews = new WebServiceAsync();
        asyncAddNews.execute(addTrivia);
    }

    private class ProcessPostFetchData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {

            if(jsonResponse != null){
                try{
                    JSONObject response = new JSONObject(jsonResponse);
                    JSONArray dataList = new JSONArray(response.getString("data"));

                    for(int counter = 0; counter < dataList.length(); counter++){
                        JSONObject data = new JSONObject(dataList.getString(counter));
                        trivia = new Trivia(data.getInt("id"), data.getString("content"),  data.isNull("image")? null : data.getString("image"), null, data.getInt("author_id"),
                                data.getString("published_date"), data.getString("author_firstname"), data.getString("author_lastname"));
                    }

                    contentView.setText(trivia.getContent());

                    if(trivia.getImageFile() != null){
                        WebServiceAsync async = new WebServiceAsync();
                        //ImageActionWrapper fetchImage = new ImageActionWrapper(TriviaActivity.this, AuthenticationAddress.TRIVIA_IMAGE_FOLDER, new ProcessPostFetchImage(trivia.getImageFile()));
                        ImageActionWrapper fetchImage = new ImageActionWrapper(TriviaActivity.this, AuthenticationAddress.TRIVIA_IMAGE_FOLDER + "/" + trivia.getImageFile(), new ProcessPostFetchImage(trivia.getImageFile()));
                        async.execute(fetchImage);

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }
    }

    private class ProcessPostFetchImage implements PostAsyncImageAction{

        String imageFile;
        public ProcessPostFetchImage(String imageFile){
            this.imageFile = imageFile;
        }

        @Override
        public String getImageFile() {
            return imageFile;
        }

        @Override
        public void processResult(Bitmap image) {

            if(image != null){
                Log.e("Trivia image", "not null");
                imageView.setImageBitmap(image);
            }else{
                Log.e("Trivia image", "null");
            }
        }
    }

    private class ProcessPostAddData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){

            }else{

            }
        }
    }

}
