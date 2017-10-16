package com.example.darwin.umnify.feed.notifications.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.LeastRecentlyUsedCache;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.news.NewsActivity;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchAuthorImageDataActionWrapper;
import com.example.darwin.umnify.feed.notifications.Notification;
import com.example.darwin.umnify.feed.notifications.NotificationHelper;
import com.example.darwin.umnify.feed.notifications.NotificationsCode;
import com.example.darwin.umnify.feed.notifications.data_action_wrapper.FetchNotificationDataActionWrapper;
import com.example.darwin.umnify.feed.notifications.view_holder.NotificationViewHolder;
import com.example.darwin.umnify.gallery.GalleryHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by darwin on 9/16/17.
 */

public class NotificationFeedManager<E extends NotificationViewHolder> extends FeedManager<E, Notification> {

    private Class<E> cls;
    private int layoutId;
    private Bundle userData;
    private boolean hasConnection = true;
    private ArrayList<String> index;
    private int offset = 0;

    public NotificationFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                   Class<E> cls, Bundle userData, int layoutId) {
        super(activity, swipeRefreshLayout, 50);
        super.setOnRemoveFromCache(new RemoveFromCache());
        this.cls = cls;
        this.layoutId = layoutId;
        this.userData = userData;
        this.index = new ArrayList<>();
        offset = 0;

        updateFeed(-1);
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType){


        return createViewHolder(parent);
    }

    private E createViewHolder(ViewGroup parent){
        E viewHolder = null;
        try{
            viewHolder=  cls.getConstructor(LayoutInflater.class, ViewGroup.class, int.class).newInstance(LayoutInflater.from(parent.getContext()), parent, layoutId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(E holder, int position) {

        if(!(position < index.size())) return;

        String key = index.get(position);
        final Notification notification = super.getEntryFromFeedList(key);

        if(notification != null){
            String title;

            holder.getNotificationImageView().setImageBitmap(notification.getImage());
            if(notification.getAuthor() == userData.getInt("USER_ID")){
                 title = "You posted a " + notification.getType();
            }else{
                title = notification.getAuthorFirstname() + " " + notification.getAuthorLastname() + " has posted a " + notification.getType();
            }
            holder.getNotificationTitleView().setText(title);
            holder.getNotificationContentView().setText(notification.getContent());
            holder.getNotificationDateView().setText(notification.getPublishedDate());

                holder.getNotificationContainer().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(notification.getType().equalsIgnoreCase("blog")){

                            Intent intent = new Intent(NotificationFeedManager.this.getActivity(), BlogActivity.class);
                            intent.putExtra("STATUS", 0);
                            intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                            intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));
                            intent.putExtra("BLOG_AUTHOR", notification.getAuthor());
                            intent.putExtra("BLOG_INDEX", notification.getIndex());

                            intent.putExtra("NOTIFICATION_ID", notification.getId());
                            intent.putExtra("BLOG_ID", notification.getRefId());

                            NotificationFeedManager.this.getActivity().startActivityForResult(intent, NotificationsCode.VIEW_BLOG);

                        }else if(notification.getType().equalsIgnoreCase("news")){

                            Intent intent = new Intent(NotificationFeedManager.this.getActivity(), NewsActivity.class);
                            intent.putExtra("STATUS", 0);
                            intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                            intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));
                            intent.putExtra("NEWS_AUTHOR", notification.getAuthor());
                            intent.putExtra("NEWS_INDEX", notification.getIndex());

                            intent.putExtra("NOTIFICATION_ID", notification.getId());
                            intent.putExtra("NEWS_ID", notification.getRefId());

                            NotificationFeedManager.this.getActivity().startActivityForResult(intent, NotificationsCode.VIEW_NEWS);
                        }else if(notification.getType().equalsIgnoreCase("announcement")){

                            Intent intent = new Intent(NotificationFeedManager.this.getActivity(), NewsActivity.class);
                            intent.putExtra("STATUS", 0);
                            intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                            intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));
                            intent.putExtra("NEWS_AUTHOR", notification.getAuthor());
                            intent.putExtra("NEWS_INDEX", notification.getIndex());

                            intent.putExtra("NOTIFICATION_ID", notification.getId());
                            intent.putExtra("NEWS_ID", notification.getRefId());


                        }
                    }
                });
        }

    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingFeedEntry()) return;

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("order", "desc");
        textDataOutput.put("id", userData.getInt("USER_ID") + "");
        FetchNotificationDataActionWrapper fetchNotificationDataActionWrapper;

        if(direction == 1){

            if(hasConnection){

                textDataOutput.put("offset", offset + "");
                textDataOutput.put("limit", "3");

                WebServiceAsync asyncFetchNotification = new WebServiceAsync();
                fetchNotificationDataActionWrapper =
                        new FetchNotificationDataActionWrapper(textDataOutput, super.getActivity(), this);

                asyncFetchNotification.execute(fetchNotificationDataActionWrapper);
            }else{

            }
        }else if(direction == -1){
            super.clearFeedList();
            index.clear();
            notifyDataSetChanged();
            super.setFetchingFeedEntry(true);
            hasConnection = true;

            super.setFetchingFeedEntry(true);

            textDataOutput.put("offset", offset + "");
            textDataOutput.put("limit", "6");

            WebServiceAsync asyncFetchNotification = new WebServiceAsync();
            fetchNotificationDataActionWrapper =
                    new FetchNotificationDataActionWrapper(textDataOutput, super.getActivity(), this);

            asyncFetchNotification.execute(fetchNotificationDataActionWrapper);
        }

        textDataOutput = null;
        fetchNotificationDataActionWrapper = null;
    }

    @Override
    public void addFeedEntries(String jsonDataArray) throws JSONException {

        if(jsonDataArray != null){
            JSONArray dataList = new JSONArray(jsonDataArray);

            for(int counter = 0; counter < dataList.length(); counter++){
                addFeedEntry(dataList.getString(counter));
            }
        }

        jsonDataArray = null;
        super.setFetchingFeedEntry(false);
        super.getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {

        JSONObject data = new JSONObject(jsonData);
        Notification notification = NotificationHelper.createNotificationFromJSON(data, -1);

        int position = index.size();
        String key = Integer.toString(notification.getId());

        super.addToFeedList(key, notification);
        index.add(key);
        offset++;
        notifyItemInserted(position);

        WebServiceAsync async = new WebServiceAsync();

        ProcessPostFetchAuthorImage processPostFetchAuthorImage = new ProcessPostFetchAuthorImage(notification, position, super.getActivity());
        FetchAuthorImageDataActionWrapper fetchAuthorImageDataActionWrapper =
                new FetchAuthorImageDataActionWrapper(super.getActivity(), processPostFetchAuthorImage);
        async.execute(fetchAuthorImageDataActionWrapper);

        notification = null;
        fetchAuthorImageDataActionWrapper = null;
    }

    @Override
    public void newFeedEntry(Intent data) {
        //dummy
    }

    @Override
    public void deleteFeedEntry(String key) {
        //dummy
    }

    @Override
    public void updateFeedContent(Intent data) {
        // dummy
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    private class ProcessPostFetchAuthorImage implements PostAsyncImageAction {

        private Notification notification;
        private Activity activity;
        private int position;

        public ProcessPostFetchAuthorImage(Notification notification, int position, Activity activity){

            this.notification = notification;
            this.activity = activity;
            this.position = position;
        }

        @Override
        public String getImageFile() {
            return notification.getAuthorImageFile();
        }

        @Override
        public void processResult(Bitmap image) {
            if(image != null){
                notification.setAuthorImage(Bitmap.createScaledBitmap(image, 100, 100, false));
                GalleryHelper.saveImageToInternal(image,
                        notification.getAuthorImageFile(), activity, "avatar");
            }else{
                    notification.setAuthorImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.missing_avatar));
            }

            notifyItemChanged(position);
        }
    }

    private class RemoveFromCache implements LeastRecentlyUsedCache.OnRemoveFromCache{

        @Override
        public void onRemove(Object key) {
            String k = (String) key;
            int position = getIndex().indexOf(k);
            getIndex().remove(position);
            notifyItemRemoved(position);
        }
    }

    public ArrayList<String> getIndex() {
        return index;
    }

    public Bundle getUserData() {
        return userData;
    }
}
