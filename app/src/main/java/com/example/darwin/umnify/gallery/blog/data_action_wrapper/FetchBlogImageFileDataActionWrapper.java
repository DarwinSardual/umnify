package com.example.darwin.umnify.gallery.blog.data_action_wrapper;

import android.app.Activity;

import com.example.darwin.umnify.PostResultAction;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/3/17.
 */

public class FetchBlogImageFileDataActionWrapper implements WebServiceAction {

    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;
    private Activity activity;

    private PostResultAction action;

    private HashMap<String, String> textDataOutput;

    public FetchBlogImageFileDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity,
                                               PostResultAction action){
        this.activity = activity;
        this.textDataOutput = textDataOutput;
        this.action = action;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_BLOG_IMAGE_FILE, activity,
                true, true, true);


        if(connection != null){
            connection.addAuthentication();
            DataHelper.writeTextUpload(textDataOutput, connection);
            connection.flushOutputStream();

            inputStream = connection.getInputStream();
            response = DataHelper.parseStringFromStream(inputStream);
        }
    }

    @Override
    public void processResult() {

        try{

            JSONObject json = new JSONObject(response);
            String data = json.getString("data");
            action.onPostResultAction(data);

        }catch (JSONException e){
            e.printStackTrace();
        }


    }
}
