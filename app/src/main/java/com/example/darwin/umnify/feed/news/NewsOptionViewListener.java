package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.darwin.umnify.feed.DeleteConfirmationDialog;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.OnDeleteFeed;

/**
 * Created by darwin on 8/29/17.
 */

public class NewsOptionViewListener implements AdapterView.OnItemSelectedListener {

    private Activity activity;
    private OnDeleteFeed onDeleteFeed;
    private boolean isFirst;
    private int id;


    public NewsOptionViewListener(Activity activity, OnDeleteFeed onDeleteFeed, int id){
        this.activity = activity;
        this.onDeleteFeed = onDeleteFeed;
        this.id = id;
       this.isFirst = true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //((TextView)view).setTextSize(12);
        if(isFirst){
            isFirst = false;
            return;
        }

        if(i == 0){
            Intent intent = new Intent(activity, EditNewsActivity.class);
            intent.putExtra("NEWS_ID", id);
            activity.startActivityForResult(intent, NewsCode.EDIT_NEWS);
        }else if(i == 1){

            if(onDeleteFeed != null){
                onDeleteFeed.onDeleteFeed();
            }


            /*DeleteConfirmationDialog deleteConfirmationDialog = new DeleteConfirmationDialog();
            Bundle bundle = new Bundle();
            bundle.putString("MESSAGE", "Are you sure you want to delete this?");
            bundle.putInt("POSITION", pos);
            deleteConfirmationDialog.setArguments(bundle);

            deleteConfirmationDialog.show(activity.getFragmentManager(), "Delete news");*/
            //manager.deleteFeedEntry(pos);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
