package com.example.darwin.umnify.feed;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.darwin.umnify.feed.news.News;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public abstract class FeedManager<E extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<E> {

    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<News> feedList;
    private boolean isFetchingNews = false;


    public FeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout){

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.feedList = new ArrayList<>();
    }

    public abstract void updateFeed(int direction);
    public abstract void addFeedEntry(String jsonData) throws JSONException;
    public abstract void addFeedEntries(String jsonDataArray) throws JSONException;

    public Activity getActivity() {
        return activity;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void addToFeedList(News news){
        feedList.add(news);
    }

    public void clearFeedList(){
        feedList.clear();
    }

    public int getFeedListSize(){
        return feedList.size();
    }

    public News getNewsFromFeedList(int position){
        return feedList.get(position);
    }

    public void setFetchingNews(boolean fetchingNews) {
        isFetchingNews = fetchingNews;
    }

    public boolean isFetchingNews() {
        return isFetchingNews;
    }
}
