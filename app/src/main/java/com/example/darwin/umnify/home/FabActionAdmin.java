package com.example.darwin.umnify.home;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class FabActionAdmin implements View.OnClickListener{

    private Activity activity;
    private Class cl;
    private Intent extras = null;
    private int requestCode;

    public FabActionAdmin(Activity activity, Class cl, Intent extras, int requestCode){

        this.activity = activity;
        this.cl = cl;
        this.extras = extras;
        this.requestCode = requestCode;
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(activity, cl);

        if(extras != null)
            intent.putExtras(extras);

        activity.startActivityForResult(intent, requestCode);
    }
}