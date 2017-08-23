package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

import android.app.Activity;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.blogs.BlogFeedManager;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class FetchBlogTileDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private Activity activity;
    private BlogFeedManager manager;

    private InputStream inputStream;
    private WebServiceConnection connection = null;
    private String response;

    public FetchBlogTileDataActionWrapper(HashMap<String, String> textDataOutput,
                                          Activity activity, BlogFeedManager manager){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.manager = manager;

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
        }
    }

    @Override
    public void processResult() {

        try {

            JSONObject json = new JSONObject(response);
            String data = json.getString("data");
            manager.addFeedEntries(data);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
