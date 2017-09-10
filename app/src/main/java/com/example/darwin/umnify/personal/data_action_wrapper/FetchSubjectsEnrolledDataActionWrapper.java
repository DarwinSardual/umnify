package com.example.darwin.umnify.personal.data_action_wrapper;

import android.app.Activity;
import android.util.Log;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.personal.SubjectsEnrolledActivity;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/2/17.
 */

public class FetchSubjectsEnrolledDataActionWrapper implements WebServiceAction{

    private WebServiceConnection connection;
    private HashMap<String, String> textDataOutput;
    private Activity activity;

    private InputStream inputStream;
    private String response;

    public FetchSubjectsEnrolledDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity){

        this.activity = activity;
        this.textDataOutput = textDataOutput;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_SUBJECTS_ENROLLED, activity,
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

            SubjectsEnrolledActivity subjectsEnrolledActivity = (SubjectsEnrolledActivity)activity;
            JSONObject json = new JSONObject(response);
            String data = json.getString("data");

            subjectsEnrolledActivity.displayData(data);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
