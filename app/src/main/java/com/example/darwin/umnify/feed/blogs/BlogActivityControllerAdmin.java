package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 8/27/17.
 */

public class BlogActivityControllerAdmin extends BlogActivityControllerGuest implements BlogActivityActions {

    private FloatingActionButton editButton;

    public BlogActivityControllerAdmin(Activity activity, Bundle extraData){
        super(activity, extraData);

        editButton = (FloatingActionButton) activity.findViewById(R.id.blog_edit);

        Bundle tempBundle = super.getExtraData();

        if(tempBundle.getInt("USER_ID") != tempBundle.getInt("BLOG_TILE_AUTHOR")){
            editButton.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void processResult(String jsonData) {
        super.processResult(jsonData);
    }
}
