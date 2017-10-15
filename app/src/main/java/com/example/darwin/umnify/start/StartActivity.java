package com.example.darwin.umnify.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.about.qoute_of_the_day.QouteOfTheDay;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.authentication.AuthenticationKeys;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.home.HomeActivity;
import com.example.darwin.umnify.login.LoginActivity;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity {

    private UMnifyDbHelper databaseConnection;
    SQLiteDatabase databaseConnectionRead;

    private Button proceedButton;
    private QouteOfTheDay qouteOfTheDay;
    private TextView contentView;
    private TextView qouteTitleView;
    private Bundle extraData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //initialize needed directory
        File directory = this.getDir("umnify", Context.MODE_PRIVATE);
        if(!directory.exists())
            directory.mkdirs();

        //File newsImageDirectory = this.getDir("umnify/images/feed/news/", Context.MODE_PRIVATE);
        /*File newsImageDirectory = new File(directory.getAbsolutePath() + "/images/feed/news");
        if(!newsImageDirectory.exists())
            newsImageDirectory.mkdirs();

        File blogImageDirectory = new File(directory.getAbsolutePath() + "/images/feed/blog");
        if(!blogImageDirectory.exists())
            blogImageDirectory.mkdirs();*/

        File avatarImageDirectory = new File(directory.getAbsolutePath() + "/images/gallery/avatar");
        if(!avatarImageDirectory.exists())
            avatarImageDirectory.mkdirs();

        File galleryNewsImageDirectory = new File(directory.getAbsolutePath() + "/images/gallery/feed/news");
        if(!galleryNewsImageDirectory.exists())
            galleryNewsImageDirectory.mkdirs();

        File galleryBlogImageDirectory = new File(directory.getAbsolutePath() + "/images/gallery/feed/blog");
        if(!galleryBlogImageDirectory.exists())
            galleryBlogImageDirectory.mkdirs();

        File galleryAnnouncementImageDirectory = new File(directory.getAbsolutePath() + "/images/gallery/feed/announcement");
        if(!galleryAnnouncementImageDirectory.exists())
            galleryAnnouncementImageDirectory.mkdirs();

        //handle start
        databaseConnection = UMnifyDbHelper.getInstance(this);
        databaseConnectionRead = databaseConnection.getReadableDatabase();
        //handleUserCheck();

        extraData = getIntent().getExtras();

        //if(extraData != null){
            //Log.e("not null", "sadasdsadsadsadasdsaadsd");
            //handleUserCheck();
        //}else{

            proceedButton = (Button) findViewById(R.id.proceed);
            qouteTitleView = (TextView) findViewById(R.id.qoute_text);
            contentView = (TextView) findViewById(R.id.content);

            proceedButton.setVisibility(View.VISIBLE);
            qouteTitleView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.VISIBLE);

            WebServiceAsync async = new WebServiceAsync();
            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("limit", "1");
            textDataOutput.put("order", "desc");

            DataActionWrapper fetchQoute = new DataActionWrapper(textDataOutput, this, AuthenticationAddress.FETCH_QOUTEOFTHEDAY, new ProcessPostFetchQoute());
            async.execute(fetchQoute);

            proceedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    button.setText("Checking for user...");
                    button.setEnabled(false);
                    handleUserCheck();
                }
            });
        //}
    }

    private void handleUserCheck(){

        //CheckUserAsync checkUserAsync;


        String[] projection = {

                UMnifyContract.UMnifyColumns.User.ID.toString(),
                UMnifyContract.UMnifyColumns.User.TYPE.toString(),
                UMnifyContract.UMnifyColumns.User.PASSWORD.toString()
        };

        Cursor cursor = databaseConnectionRead.query(
                UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(),
                projection, null, null, null, null, null);

        if(cursor.getCount() == 1){
            //check if the stored credentials is valid
            //get user credentials

            int id = 0;
            int type = 0;
            String password = null;

            while(cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.User.ID.toString()));
                type = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.User.TYPE.toString()));
                password = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.User.PASSWORD.toString()));
            }

            StartDataActionWrapper wrapper;
            WebServiceConnection connection;
            HashMap<String, String> textData = new HashMap<>();
            textData.put(AuthenticationKeys.USER_ID_KEY, id + "");
            textData.put("type", type + "");
            textData.put(AuthenticationKeys.USER_PASSWORD_KEY, password);


                wrapper = new StartDataActionWrapper(textData, this);
                WebServiceAsync async = new WebServiceAsync();
                async.execute(wrapper);

        }else{

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    private class StartDataActionWrapper implements WebServiceAction{

        private HashMap<String, String> textDataOutput;
        private WebServiceConnection connection;
        private Activity activity;

        /* Dependent data */

        private InputStream inputStream;
        String response;

        public StartDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity){

            this.textDataOutput = textDataOutput;
            this.activity = activity;
        }

        @Override
        public void processRequest() {


            connection = new WebServiceConnection(AuthenticationAddress.CHECK_USER,
                    activity, true, true, true);

                connection.addAuthentication();
                DataHelper.writeTextUpload(textDataOutput, connection);
                connection.flushOutputStream();

                inputStream = connection.getInputStream();
                response = DataHelper.parseStringFromStream(inputStream);

        }

        @Override
        public void processResult() {

            Intent intent;
            intent = new Intent(StartActivity.this, HomeActivity.class);

            if(response == null){
                //fetch from local db
                String query = "select * from Person left join AcademePerson on Person.id = AcademePerson.id where Person.id = ?";
                String[] selectionArgs = {textDataOutput.get(AuthenticationKeys.USER_ID_KEY)};
                Cursor cursor = databaseConnectionRead.rawQuery(query, selectionArgs);

                while(cursor.moveToNext()){

                    intent.putExtra("USER_ID", Integer.parseInt(textDataOutput.get(AuthenticationKeys.USER_ID_KEY)));
                    intent.putExtra("USER_TYPE", Integer.parseInt(textDataOutput.get("type")));
                    intent.putExtra("USER_PASSWORD", textDataOutput.get(AuthenticationKeys.USER_PASSWORD_KEY));
                    intent.putExtra("USER_FIRSTNAME", cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.FIRSTNAME.toString())));
                    intent.putExtra("USER_LASTNAME", cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.LASTNAME.toString())));
                    intent.putExtra("USER_IMAGE_FILE", cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.IMAGE.toString())));
                    intent.putExtra("USER_EMAIL", cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.EMAIL.toString())));
                    intent.putExtra("USER_COURSE", cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.AcademePerson.COURSE.toString())));

                }

                startActivity(intent);
            }else{
                try{


                    JSONObject json = new JSONObject(response);
                    int code = json.getInt("code");

                    if(code == AuthenticationCodes.USER_AUTHENTICATED){

                        JSONObject data = new JSONObject(json.getString("user"));
                        intent.putExtra("USER_ID", Integer.parseInt(textDataOutput.get(AuthenticationKeys.USER_ID_KEY)));
                        intent.putExtra("USER_TYPE", Integer.parseInt(textDataOutput.get("type")));
                        intent.putExtra("USER_PASSWORD", textDataOutput.get(AuthenticationKeys.USER_PASSWORD_KEY));
                        intent.putExtra("USER_FIRSTNAME", data.getString("firstname"));
                        intent.putExtra("USER_LASTNAME", data.getString("lastname"));
                        intent.putExtra("USER_IMAGE_FILE", data.getString("image"));
                        intent.putExtra("USER_EMAIL", data.getString("email"));
                        intent.putExtra("USER_COURSE", data.getInt("course"));
                        Log.e("Message:", "Stored credentials are valid");
                        startActivity(intent);

                    }else if(code == AuthenticationCodes.INVALID_USER_ID_PASSWORD){

                        SQLiteDatabase databaseConnectionWrite = StartActivity.this.databaseConnection.getWritableDatabase();
                        databaseConnectionWrite.delete(UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(), null, null);

                        String selection = UMnifyContract.UMnifyColumns.Person.ID.toString() + " = ?";
                        String[] selectionArgs = {textDataOutput.get("id")};
                        databaseConnectionWrite.delete(UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString(), selection, selectionArgs);

                        intent = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }catch (JSONException e){
                    Log.e("JSONException", "StartActivity - Can't parse response json");
                }
            }
            finish();

        }
    }

    private class ProcessPostFetchQoute implements PostAsyncAction {

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){

                try{
                    JSONObject response = new JSONObject(jsonResponse);
                    JSONArray dataList = new JSONArray(response.getString("data"));

                    for(int counter = 0; counter < dataList.length(); counter++){
                        JSONObject data = new JSONObject(dataList.getString(counter));
                        qouteOfTheDay = new QouteOfTheDay(data.getInt("id"), data.getString("content"),data.getInt("author_id"),
                                data.getString("published_date"), data.getString("author_firstname"), data.getString("author_lastname"));
                    }

                    contentView.setText("\"" + qouteOfTheDay.getContent() + "\"");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
