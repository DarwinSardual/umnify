package com.example.darwin.umnify.feed.announcements;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.example.darwin.umnify.feed.OnDeleteFeed;
import com.example.darwin.umnify.feed.news.EditNewsActivity;
import com.example.darwin.umnify.feed.news.NewsCode;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementOptionViewListener implements AdapterView.OnItemSelectedListener{

    private Activity activity;
    private OnDeleteFeed onDeleteFeed;
    private boolean isFirst;
    private int id;


    public AnnouncementOptionViewListener(Activity activity, OnDeleteFeed onDeleteFeed, int id){
        this.activity = activity;
        this.onDeleteFeed = onDeleteFeed;
        this.id = id;
        this.isFirst = true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(isFirst){
            isFirst = false;
            return;
        }

        if(i == 0){
            Intent intent = new Intent(activity, EditAnnouncementActivity.class);
            intent.putExtra("ANNOUNCEMENT_ID", id);
            activity.startActivityForResult(intent, AnnouncementCode.EDIT_ANNOUNCEMENT);
        }else if(i == 1){

            if(onDeleteFeed != null){
                onDeleteFeed.onDeleteFeed();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
