package com.example.darwin.umnify.feed.news;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.darwin.umnify.feed.FeedManager;

/**
 * Created by darwin on 8/29/17.
 */

public class NewsOptionViewListener implements AdapterView.OnItemSelectedListener {

    private FeedManager manager;
    private News news;

    public NewsOptionViewListener(News news, FeedManager manager){
        this.manager = manager;
        this.news = news;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView)view).setTextSize(12);

        if(i == 1){
        }else if(i == 2){

            manager.deleteFeedEntry(news);
        }

        adapterView.setSelection(0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
