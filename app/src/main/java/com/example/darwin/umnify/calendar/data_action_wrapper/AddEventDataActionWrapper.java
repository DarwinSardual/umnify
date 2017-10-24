package com.example.darwin.umnify.calendar.data_action_wrapper;

import android.app.Activity;

import android.widget.Toast;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/8/17.
 */

public class AddEventDataActionWrapper implements WebServiceAction{

    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;
    private HashMap<String, String> textDataOutput;
    private Activity activity;

    public AddEventDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.ADD_EVENT, activity, true, true, true);

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
        if(response != null){
            Toast.makeText(activity, "Adding event successful.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(activity, "Failed to add new event.", Toast.LENGTH_LONG).show();
        }
    }
}
