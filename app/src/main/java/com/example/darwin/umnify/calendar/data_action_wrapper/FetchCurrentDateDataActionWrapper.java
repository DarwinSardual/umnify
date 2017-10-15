package com.example.darwin.umnify.calendar.data_action_wrapper;

import android.app.Activity;
import android.util.Log;

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.calendar.CalendarManager;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by darwin on 9/7/17.
 */

public class FetchCurrentDateDataActionWrapper implements WebServiceAction{

    private Activity activity;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;

    private CalendarManager manager;

    public FetchCurrentDateDataActionWrapper(Activity activity, CalendarManager manager){
        this.activity = activity;
        this.manager = manager;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_CURRENT_DATE, activity, true, true, true);

        if(connection !=null){

            connection.addAuthentication();
            connection.flushOutputStream();

            inputStream = connection.getInputStream();
            response = DataHelper.parseStringFromStream(inputStream);

        }
    }

    @Override
    public void processResult() {

        if(response != null){
            try{

                JSONObject json = new JSONObject(response);
                String data = json.getString("current_date");

                int date[] = DateHelper.convertDbDateToCalendarDate(data, "-");
                manager.setFirstCurrentDate(date[0], date[1], date[2]);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }


    }
}
