package com.example.darwin.umnify.calendar.data_action_wrapper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.calendar.CalendarManagerAdmin;
import com.example.darwin.umnify.calendar.OnDeleteEvent;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/9/17.
 */

public class DeleteEventDataActionWrapper implements WebServiceAction{

    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;

    private HashMap<String, String> textDataOutput;
    private Activity activity;
    private OnDeleteEvent action;
    private ViewGroup eventContainer;
    private ViewGroup parent;

    public DeleteEventDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity,
                                        ViewGroup eventContainer, ViewGroup parent, OnDeleteEvent action){

        this.activity = activity;
        this.action = action;
        this.textDataOutput = textDataOutput;
        this.eventContainer = eventContainer;
        this.parent = parent;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.DELETE_EVENT, activity, true, true, true);

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

        action.deleteEvent(parent, eventContainer);

    }
}
