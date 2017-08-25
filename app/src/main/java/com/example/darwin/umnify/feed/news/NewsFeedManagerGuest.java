package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchAuthorImageDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsImageDataActionWrapper;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderGuest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class NewsFeedManagerGuest<E extends NewsViewHolderGuest> extends FeedManager<E>{

    private E viewHolder;
    private Bundle userData;
    private Class<E> cls;
    private int layoutId;


    public NewsFeedManagerGuest(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Bundle userData, Class<E> cls, int layoutId){

        super(activity, swipeRefreshLayout);
        this.userData = userData;
        this.cls = cls;
        this.layoutId = layoutId;

        HashMap<String, String> fetchNewsDataParams = new HashMap<>();
        fetchNewsDataParams.put("order", "desc");
        fetchNewsDataParams.put("offset", super.getFeedListSize() + "");
        fetchNewsDataParams.put("limit", "5");
        fetchNewsDataParams.put("id", "-1");

        WebServiceAsync asyncFetchNews = new WebServiceAsync();
        FetchNewsDataActionWrapper fetchNewsWrapper = new FetchNewsDataActionWrapper(fetchNewsDataParams, activity, this);
        asyncFetchNews.execute(fetchNewsWrapper);

        fetchNewsWrapper = null;
        asyncFetchNews = null;
        fetchNewsDataParams = null;
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {

        FetchAuthorImageDataActionWrapper fetchAuthorImageDataActionWrapper;
        FetchNewsImageDataActionWrapper fetchNewsImageDataActionWrapper;
        WebServiceAsync asyncFetchAuthorImage;
        WebServiceAsync asyncFetchNewsImage;

        JSONObject newsData = new JSONObject(jsonData);
        News news = NewsHelper.createNewsFromJSON(newsData, super.getFeedListSize());

        fetchAuthorImageDataActionWrapper = new FetchAuthorImageDataActionWrapper( news, super.getActivity(), this);
        fetchNewsImageDataActionWrapper = new FetchNewsImageDataActionWrapper(news, super.getActivity(), this);
        super.addToFeedList(news);
        notifyItemInserted(news.getIndex());

        asyncFetchAuthorImage = new WebServiceAsync();
        asyncFetchAuthorImage.execute(fetchAuthorImageDataActionWrapper);
        asyncFetchNewsImage = new WebServiceAsync();
        asyncFetchNewsImage.execute(fetchNewsImageDataActionWrapper);
    }

    @Override
    public void addFeedEntries(String jsonDataArray) throws JSONException {

        JSONArray dataList = new JSONArray(jsonDataArray);

        for(int i = 0; i < dataList.length(); i++){

            addFeedEntry(dataList.getString(i));
        }

        dataList = null;
        super.setFetchingNews(false);
        super.getSwipeRefreshLayout().setRefreshing(false);

    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingNews()) return;

        WebServiceAsync asyncFetchNews = new WebServiceAsync();
        FetchNewsDataActionWrapper fetchNewsWrapper;

        if(direction == 1){

            super.setFetchingNews(true);

            HashMap<String, String> fetchNewsDataParamsUpdate = new HashMap<>();
            fetchNewsDataParamsUpdate.put("order", "desc");
            fetchNewsDataParamsUpdate.put("offset", super.getFeedListSize() + "");
            fetchNewsDataParamsUpdate.put("limit", "3");
            fetchNewsDataParamsUpdate.put("id", "-1");

            fetchNewsWrapper = new FetchNewsDataActionWrapper(fetchNewsDataParamsUpdate, super.getActivity(), this);
            asyncFetchNews.execute(fetchNewsWrapper);

            fetchNewsDataParamsUpdate = null;

        }else if(direction == -1){
            super.clearFeedList();
            notifyDataSetChanged();

            super.setFetchingNews(true);

            HashMap<String, String> fetchNewsDataParams = new HashMap<>();
            fetchNewsDataParams.put("order", "desc");
            fetchNewsDataParams.put("offset", super.getFeedListSize() + "");
            fetchNewsDataParams.put("limit", "5");
            fetchNewsDataParams.put("id", "-1");

            fetchNewsWrapper = new FetchNewsDataActionWrapper(fetchNewsDataParams, super.getActivity(), this);
            asyncFetchNews.execute(fetchNewsWrapper);

            fetchNewsDataParams = null;
        }

        fetchNewsWrapper = null;
        asyncFetchNews = null;
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType){


        return createViewHolder(parent);
    }

    private E createViewHolder(ViewGroup parent){
        E viewHolder = null;
        try{
           viewHolder=  cls.getConstructor(LayoutInflater.class, ViewGroup.class, int.class).newInstance(LayoutInflater.from(parent.getContext()), parent, R.layout.feed_news_guest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(E holder, int position) {
        if(position < super.getFeedListSize()){

            final News news = super.getNewsFromFeedList(position);
            holder.getNewsAuthorView().setText(news.getAuthorFirstname() + " " + news.getAuthorLastname());
            holder.getNewsContentView().setText(news.getContent());
            holder.getNewsAuthorImageView().setImageBitmap(news.getAuthorImage());
            holder.getNewsImageView().setImageBitmap(news.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    public E getViewHolder() {
        return viewHolder;
    }
}
