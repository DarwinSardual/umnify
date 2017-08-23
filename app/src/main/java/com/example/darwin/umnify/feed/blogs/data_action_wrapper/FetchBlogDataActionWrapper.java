package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogHelper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsDataActionWrapper;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class FetchBlogDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private WebServiceConnection connection;
    private BlogActivity activity;
    private InputStream inputStream;

    private int id;
    private String heading;
    private String imageFile;
    private String response;

    public FetchBlogDataActionWrapper(int id, String heading, String imageFile,
                                      HashMap<String, String> textDataOutput,
                                      BlogActivity activity){

        this(textDataOutput, activity);

        this.id = id;
        this.heading = heading;
        this.imageFile = imageFile;

    }

    public FetchBlogDataActionWrapper(HashMap<String, String> textDataOutput,
                                      BlogActivity activity){
        this.textDataOutput = textDataOutput;
        this.activity = activity;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_BLOGS, activity,
                true, true, true);

        if(connection != null){
            connection.addAuthentication();
            DataHelper.writeTextUpload(textDataOutput, connection);
            connection.flushOutputStream();

            inputStream = connection.getInputStream();
            response = DataHelper.parseStringFromStream(inputStream);
        }else{

        }

    }

    @Override
    public void processResult() {

        try{


            Log.e("fetch blog", response);

            JSONObject json = new JSONObject(response);

            int authenticatedCode = json.getInt("authenticated");
            int fetchCode = json.getInt("fetch");

            if(authenticatedCode == AuthenticationCodes.AUTHENTICATED && fetchCode == AuthenticationCodes.FETCH_OK){

                String str = json.getString("data");
                JSONArray dataList = new JSONArray(str);
                JSONObject data = null;

                for(int i = 0; i < dataList.length(); i++){

                    data = new JSONObject(dataList.getString(i));
                    activity.getContentView().setText(data.getString("content"));
                }

                heading = heading != null? heading : data.getString("heading");
                activity.getToolbarLayout().setTitle(heading);
                imageFile = imageFile != null? imageFile : data.getString( "image");

                Bitmap image = BlogHelper.loadImageFromInternal(imageFile, activity);

                if(image != null){
                    activity.getFeaturedImageView().setImageBitmap(image);
                }else{

                }
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
