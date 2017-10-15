package com.example.darwin.umnify.about.quick_fact;

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
import com.example.darwin.umnify.about.trivia.Trivia;
import com.example.darwin.umnify.about.trivia.TriviaActivity;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.wrapper.DataHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class QuickFactActivity extends AppCompatActivity {

    private Bundle userData;
    private FloatingActionButton fab;
    private TextView contentView;
    private ImageView imageView;
    private QuickFact quickFact;

    private ImageButton backButton;

    public final static int ADD_QUICK_FACT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userData = getIntent().getExtras();

        if(userData != null){
            if(userData.getInt("USER_TYPE") == AuthenticationCodes.SUPER_ADMIN_USER){
                setContentView(R.layout.activity_quick_fact);
                setup();
                fetchData();
                setupSuperAdmin();
            }else{
                setContentView(R.layout.activity_quick_fact);
                setup();
                fetchData();
            }
        }else{
            setContentView(R.layout.activity_quick_fact);
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
            textData.put("content", data.getStringExtra("ADD_QUICK_FACT_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") +"");
            Log.e("image", imageFile);
            fileData.put(imageFile, byteArray);

        }else{
            textData.put("content", data.getStringExtra("ADD_QUICK_FACT_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("user_type", userData.getInt("USER_TYPE") +"");
        }

        DataImageActionWrapper addQuickFact = new DataImageActionWrapper(textData, fileData, this, AuthenticationAddress.ADD_QUICK_FACT, new ProcessPostAddData());

        WebServiceAsync asyncAddNews = new WebServiceAsync();
        asyncAddNews.execute(addQuickFact);
    }

    private void setupSuperAdmin(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuickFactActivity.this, AddQuickFactActivity.class);
                startActivityForResult(intent, ADD_QUICK_FACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_QUICK_FACT){
            if(resultCode == RESULT_OK){
                Log.e("add", data.getStringExtra("ADD_QUICK_FACT_CONTENT"));
                addData(data);
            }
        }
    }

    public void fetchData(){

        WebServiceAsync async = new WebServiceAsync();

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("limit", "1");
        textDataOutput.put("order", "desc");

        DataActionWrapper fetchData = new DataActionWrapper(textDataOutput, this, AuthenticationAddress.FETCH_QUICK_FACT, new ProcessPostFetchData());
        async.execute(fetchData);
    }

    private class ProcessPostFetchData implements PostAsyncAction {

        @Override
        public void processResult(String jsonResponse) {


            try{
                JSONObject response = new JSONObject(jsonResponse);
                JSONArray dataList = new JSONArray(response.getString("data"));

                for(int counter = 0; counter < dataList.length(); counter++){
                    JSONObject data = new JSONObject(dataList.getString(counter));
                    quickFact = new QuickFact(data.getInt("id"), data.getString("content"),  data.isNull("image")? null : data.getString("image"), null, data.getInt("author_id"),
                            data.getString("published_date"), data.getString("author_firstname"), data.getString("author_lastname"));
                }

                contentView.setText(quickFact.getContent());

                if(quickFact.getImageFile() != null){
                    WebServiceAsync async = new WebServiceAsync();
                    ImageActionWrapper fetchImage = new ImageActionWrapper(QuickFactActivity.this, AuthenticationAddress.QUICK_FACT_IMAGE_FOLDER, new ProcessPostFetchImage(quickFact.getImageFile()));
                    async.execute(fetchImage);

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    private class ProcessPostFetchImage implements PostAsyncImageAction {

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
                imageView.setImageBitmap(image);
            }
        }
    }

    private class ProcessPostAddData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){
                Log.e("response", jsonResponse);
            }else{

            }
        }
    }
}
