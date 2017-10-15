package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

/**
 * Created by darwin on 8/27/17.
 */

public class BlogActivityControllerSuperAdmin extends BlogActivityControllerAdmin{


    public BlogActivityControllerSuperAdmin(Activity activity, Bundle extraData){
        super(activity, extraData);

        getButtonContainer().setEnabled(true);
        getButtonContainer().setVisibility(View.VISIBLE);
    }
}
