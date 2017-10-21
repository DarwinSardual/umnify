package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by darwin on 9/18/17.
 */

public class NewsActivityControllerSuperAdmin extends NewsActivityControllerAdmin{

    public NewsActivityControllerSuperAdmin(Activity activity, Bundle extraData){
        super(activity, extraData);

        getMoreOptions().setEnabled(true);
        getMoreOptions().setVisibility(View.VISIBLE);
    }
}
