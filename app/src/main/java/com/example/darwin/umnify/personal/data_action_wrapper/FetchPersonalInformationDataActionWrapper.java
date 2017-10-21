package com.example.darwin.umnify.personal.data_action_wrapper;

import android.app.Activity;

import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.WebServiceAction;

/**
 * Created by darwin on 9/1/17.
 */

public class FetchPersonalInformationDataActionWrapper implements WebServiceAction{

    //  i need user's id

    private WebServiceConnection connection;
    private Activity activity;


    @Override
    public void processRequest() {

        connection = new WebServiceConnection("", activity, true, true, true);

    }

    @Override
    public void processResult() {

    }
}
