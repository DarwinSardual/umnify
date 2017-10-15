package com.example.darwin.umnify.personal.data_action_wrapper;

import android.app.Activity;
import android.util.Log;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.personal.StudentPermanentRecordActivity;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/1/17.
 */

public class FetchStudentPermanentRecordDataActionWrapper implements WebServiceAction{

    // i need the user's id

    private WebServiceConnection connection;
    private Activity activity;
    private HashMap<String, String> textDataOutput;

    private String response;
    private InputStream inputStream;

    public FetchStudentPermanentRecordDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity){
        this.activity = activity;
        this.textDataOutput = textDataOutput;

    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_STUDENT_PERMANENT_RECORD, activity, true, true, true);

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

        if(response != null){
            StudentPermanentRecordActivity studentPermanentRecordActivity = (StudentPermanentRecordActivity) activity;
            try{

                JSONObject json = new JSONObject(response);
                String data = json.getString("data");
                studentPermanentRecordActivity.displayData(data);

            }catch (JSONException e){
                e.printStackTrace();
            }

        }

    }
}
