package com.example.darwin.umnify.calendar.data_action_wrapper;

import android.app.Activity;
import android.util.Log;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.calendar.CalendarManager;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/7/17.
 */

public class FetchEventDataActionWrapper implements WebServiceAction {

    public HashMap<String, String> textDataOutput;
    private Activity activity;

    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;

    private CalendarManager manager;

    public FetchEventDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity,
                                       CalendarManager manager){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.manager = manager;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_EVENTS, activity, true, true, true);

        if(connection != null){

            connection.addAuthentication();
            DataHelper.writeTextUpload(textDataOutput,connection);
            connection.flushOutputStream();

            inputStream = connection.getInputStream();

            response = DataHelper.parseStringFromStream(inputStream);

        }

    }

    @Override
    public void processResult() {

        Log.e("Rsult", response);

        try{

            JSONObject json = new JSONObject(response);
            String dataArray = json.getString("data");

            manager.addEvents(dataArray);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
