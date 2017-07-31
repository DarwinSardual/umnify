package com.example.darwin.umnify.feed.blogs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.R;

import java.util.ArrayList;
import java.util.List;

public class BlogFeedEntryManager extends RecyclerView.Adapter<BlogFeedEntryManager.ViewHolder>{

    private List<BlogFeedEntry> feedEntryList;
    private int feedCount;

    public BlogFeedEntryManager(Context context){

        feedEntryList = new ArrayList<>();
        //feedEntryList.add(new BlogFeedEntry("Sample Blog", null, ContextCompat.getDrawable(context, R.drawable.employees)));

        feedCount = 0;

    }

    /* Template for the View */

    public final class ViewHolder extends RecyclerView.ViewHolder{

        private TextView blogEntryTitleView;
        private ImageView blogEntryImageView;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.feed_blogs, parent, false));

            blogEntryTitleView = (TextView) itemView.findViewById(R.id.blog_entry_title);
            blogEntryImageView = (ImageView) itemView.findViewById(R.id.blog_entry_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(isFeedEmpty()){

            if(position >= (feedCount - 1)){
                //fetch data
            }
        }else{
            //BlogFeedEntry feed = feedEntryList.get(position % feedEntryList.size());

            //holder.blogEntryTitleView.setText(feed.getTitle());
            //holder.blogEntryImageView.setImageDrawable(feed.getImage());
        }

    }

    private boolean isFeedEmpty(){
        return feedCount == 0? true : false;
    }

    private class BlogFeedAsyc extends AsyncTask<String, Void, List<BlogFeedEntry>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<BlogFeedEntry> doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(List<BlogFeedEntry> blogFeedEntries) {
            super.onPostExecute(blogFeedEntries);
        }
    }

    @Override
    public int getItemCount() {
        return feedCount;
    }
}