package com.example.darwin.umnify.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by darwin on 9/6/17.
 */

public class ViewImageListener implements View.OnClickListener{

    private Bundle bundle;
    private Activity activity;
    private Intent intent;

    public ViewImageListener(Bundle bundle, Activity activity){
        this.bundle = bundle;
        this.activity = activity;
        intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtras(bundle);
    }

    @Override
    public void onClick(View view) {
        activity.startActivity(intent);
    }
}
