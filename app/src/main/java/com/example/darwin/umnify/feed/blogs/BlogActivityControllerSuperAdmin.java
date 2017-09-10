package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

/**
 * Created by darwin on 8/27/17.
 */

public class BlogActivityControllerSuperAdmin extends BlogActivityControllerGuest implements BlogActivityActions {

    private FloatingActionButton editButton;

    public BlogActivityControllerSuperAdmin(Activity activity, Bundle extraData){
        super(activity, extraData);
    }

    @Override
    public void processResult(String jsonData) {
        super.processResult(jsonData);
    }
}
