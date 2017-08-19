package com.example.darwin.umnify.async;

import android.os.AsyncTask;
import com.example.darwin.umnify.wrapper.WebServiceAction;

public class WebServiceAsync extends AsyncTask<WebServiceAction, Void, WebServiceAction[]>{

    @Override
    protected WebServiceAction[] doInBackground(WebServiceAction... webServiceActions) {

        for(WebServiceAction actions : webServiceActions){
            actions.processRequest();
        }

        return webServiceActions;
    }

    @Override
    protected void onPostExecute(WebServiceAction[] webServiceActions) {

        for(WebServiceAction action : webServiceActions){
            action.processResult();
        }
    }
}
