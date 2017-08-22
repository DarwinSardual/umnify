package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.darwin.umnify.R;

public class NewsCommentManager extends RecyclerView.Adapter<NewsCommentManager.ViewHolder>{

    private Activity activity;
    private Bundle extraData;

    private TextView starsView;

    public NewsCommentManager(Activity activity, Bundle extraData){

        starsView = (TextView) activity.findViewById(R.id.activity_news_comment_stars);
        starsView.setText(extraData.getString("NEWS_STARS") + " have starred this");
    }

    public final class ViewHolder extends RecyclerView.ViewHolder{

        private ViewHolder(LayoutInflater inflater, ViewGroup parent){
           super((inflater.inflate(R.layout.feed_comment, parent, false)));

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
