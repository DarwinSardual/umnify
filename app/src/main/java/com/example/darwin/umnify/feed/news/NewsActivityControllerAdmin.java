package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.SpinnerExtended;
import com.example.darwin.umnify.feed.OnDeleteFeed;
import com.example.darwin.umnify.feed.notifications.NotificationsCode;

/**
 * Created by darwin on 9/18/17.
 */

public class NewsActivityControllerAdmin extends NewsActivityControllerNormal {

    private SpinnerExtended moreOptions;
    private int id;

    public NewsActivityControllerAdmin(Activity activity, Bundle extraData){

        super(activity, extraData);
        moreOptions = (SpinnerExtended) activity.findViewById(R.id.spinner);

        if(extraData != null){
            if(extraData.getInt("USER_ID") != extraData.getInt("NEWS_AUTHOR")){
                moreOptions.setVisibility(View.GONE);
            }
        }

        ProcessDeleteFeed processDeleteFeed = new ProcessDeleteFeed(activity, extraData);
        NewsOptionViewListener newsOptionViewListener =
                new NewsOptionViewListener(activity, processDeleteFeed, extraData.getInt("NEWS_ID"));
        moreOptions.setOnItemSelectedListener(newsOptionViewListener);


    }

    public SpinnerExtended getMoreOptions() {
        return moreOptions;
    }

    private class ProcessDeleteFeed implements OnDeleteFeed{

        private Bundle extraData;
        private Activity activity;

        public ProcessDeleteFeed(Activity activity, Bundle extraData){
            this.activity = activity;
            this.extraData = extraData;
        }

        @Override
        public void onDeleteFeed() {
            Intent intent = new Intent();
            intent.putExtra("ACTION", NewsCode.DELETE_NEWS);
            intent.putExtra("NEWS_ID", extraData.getInt("NEWS_ID"));
            if(extraData.containsKey("NOTIFICATION_ID")){
                intent.putExtra("NOTIFICATION_ID", extraData.getInt("NOTIFICATION_ID"));
            }
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }
}
