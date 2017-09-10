package com.example.darwin.umnify.calendar;

import android.view.View;

import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.calendar.data_action_wrapper.DeleteEventDataActionWrapper;

/**
 * Created by darwin on 9/9/17.
 */

public class DeleteEventListener implements View.OnClickListener {

    private int id;
    private WebServiceAsync async;
    private DeleteEventDataActionWrapper wrapper;

    public DeleteEventListener(int id){
        this.id = id;
    }

    @Override
    public void onClick(View view) {
        async = new WebServiceAsync();

    }
}
