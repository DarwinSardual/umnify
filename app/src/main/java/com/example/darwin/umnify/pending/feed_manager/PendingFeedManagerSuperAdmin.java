package com.example.darwin.umnify.pending.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.DataImageActionWrapper;
import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.LeastRecentlyUsedCache;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.news.NewsActivity;
import com.example.darwin.umnify.pending.Pending;
import com.example.darwin.umnify.pending.PendingCode;
import com.example.darwin.umnify.pending.PendingHelper;
import com.example.darwin.umnify.pending.view_holder.PendingViewHolderSuperAdmin;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by darwin on 9/21/17.
 */

public class PendingFeedManagerSuperAdmin<E extends PendingViewHolderSuperAdmin>  extends FeedManager<E, Pending>{

    private Class<E> cls;
    private int layoutId;
    private Bundle userData;
    private ArrayList<String> index;
    private int offset = 0;

    public PendingFeedManagerSuperAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout, Bundle userData,
                                Class<E> cls, int layoutId){

        super(activity, swipeRefreshLayout, 50);
        super.setOnRemoveFromCache(new RemoveFromCache());
        this.cls = cls;
        this.index = new ArrayList<>();
        this.layoutId = layoutId;
        this.userData = userData;

        updateFeed(-1);
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {
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
        final Pending pending = super.getEntryFromFeedList(key);

        if(pending != null){
            holder.getPendingContentView().setText(pending.getContent());
            holder.getPendingAuthorView().setText("by: " + pending.getAuthorFirstname() + " " + pending.getAuthorLastname());
            String str = null;
            if(pending.getCreatedDate() != null){
                String temp[] = pending.getCreatedDate().split(" ");
                String first = DateHelper.convertDateToMDY(temp[0]);
                String second = DateHelper.convert24Hourto12Hour(temp[1]);
                str = first + " " + second;
            }
            holder.getPendingDateView().setText(str);

            final HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("id", pending.getId() +"");
            textDataOutput.put("type", pending.getType());

            final String type = pending.getType();
            holder.getPendingContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(type.equalsIgnoreCase("news")){

                        Intent intent = new Intent(PendingFeedManagerSuperAdmin.this.getActivity(), NewsActivity.class);
                        intent.putExtra("STATUS", 0);
                        intent.putExtra("NEWS_ID", pending.getId());
                        intent.putExtra("TYPE", "news");

                        intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                        intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));

                        intent.putExtra("AUTHOR_ID", pending.getAuthor());

                        PendingFeedManagerSuperAdmin.super.getActivity().startActivityForResult(intent, PendingCode.VIEW_NEWS);

                    }else if(type.equalsIgnoreCase("blog")){

                        Intent intent = new Intent(PendingFeedManagerSuperAdmin.this.getActivity(), BlogActivity.class);
                        intent.putExtra("STATUS", 0);
                        intent.putExtra("BLOG_ID", pending.getId());
                        intent.putExtra("TYPE", "blog");

                        intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                        intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));

                        intent.putExtra("AUTHOR_ID", pending.getAuthor());

                        PendingFeedManagerSuperAdmin.super.getActivity().startActivityForResult(intent, PendingCode.VIEW_BLOG);
                    }
                }
            });

            holder.getPendingAllowButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebServiceAsync async = new WebServiceAsync();
                    PostAsyncAction postProcess =
                            new ProcessAllowPendingPostAsync(pending.getId() + "_" + pending.getType());
                    WebServiceAction action =
                            new DataActionWrapper(textDataOutput, getActivity(),AuthenticationAddress.ALLOW_PENDING, postProcess);

                    async.execute(action);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingFeedEntry()) return;

        if(direction == 1){

            super.setFetchingFeedEntry(true);

            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("offset", offset + "");
            textDataOutput.put("limit", "3");

            WebServiceAction action =
                    new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_PENDING, new ProcessPostFetchPending());
            WebServiceAsync async = new WebServiceAsync();
            async.execute(action);


        }else if(direction == -1){

            if(super.getFeedListSize() > 0){
                super.clearFeedList();
                index.clear();
                offset = 0;
                notifyDataSetChanged();
                super.setFetchingFeedEntry(true);

            }

            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("offset", offset + "");
            textDataOutput.put("limit", "7");

            WebServiceAction action =
                    new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_PENDING, new ProcessPostFetchPending());
            WebServiceAsync async = new WebServiceAsync();
            async.execute(action);


        }

    }

    @Override
    public void addFeedEntries(String jsonDataArray) throws JSONException {

        if(jsonDataArray != null){
            JSONArray dataList = new JSONArray(jsonDataArray);
            for(int counter = 0; counter < dataList.length(); counter++){
                String json = dataList.getString(counter);
                addFeedEntry(json);
            }
        }
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {

        if(jsonData != null){
            JSONObject pendingData = new JSONObject(jsonData);
            Pending pending = PendingHelper.createPendingFromJSON(pendingData, -1);

            int position = index.size();
            String key = pending.getId() + "_" + pending.getType();

            super.addToFeedList(key, pending);
            index.add(key);
            notifyItemInserted(position);
        }
    }

    @Override
    public void newFeedEntry(Intent data) {}

    @Override
    public void deleteFeedEntry(String key){

        Pending pending = super.getEntryFromFeedList(key);
        WebServiceAsync async = new WebServiceAsync();

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", pending.getId() + "");

        WebServiceAction action = null;

        if(pending.getType().equalsIgnoreCase("news")){
            ProcessPostDeleteNews processPostDeleteNews =
                    new ProcessPostDeleteNews(key);
            action =
                    new DataActionWrapper(textDataOutput, super.getActivity(),AuthenticationAddress.DELETE_NEWS,  processPostDeleteNews);
        }else if(pending.getType().equalsIgnoreCase("blog")){
            ProcessPostDeleteBlog processPostDeleteBlog =
                    new ProcessPostDeleteBlog(key);
            action = new
                    DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.DELETE_BLOG, processPostDeleteBlog);
        }

        async.execute(action);
    }

    @Override
    public void updateFeedContent(Intent data) {

        String type = data.getStringExtra("TYPE");

        if(type.equalsIgnoreCase("news")){
            updateNewsContent(data);
        }else if(type.equalsIgnoreCase("blog")){
            updateBlogContent(data);
        }
    }

    public void updateNewsContent(Intent data) {

        Uri uri = data.getData();
        Cursor returnCursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();

        if(uri != null){

            String mimeType = DataHelper.getMimeType(uri, super.getActivity());

            returnCursor =
                    super.getActivity().getContentResolver()
                            .query(uri,
                                    null, null,
                                    null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();

            String imageFile = returnCursor.getString(nameIndex);
            Bitmap image = null;
            byte[] byteArray = null;
            Bitmap rescaledImage = null;

            try{
                image = MediaStore.Images.Media.getBitmap(super.getActivity().getContentResolver(), uri);
                rescaledImage = DataHelper.resizeImageAspectRatio(image, 1920, 1080);
            }catch (IOException e){
                e.printStackTrace();
            }

            byteArray = DataHelper.bitmapToByteArray(rescaledImage, mimeType);

            textData.put("id", data.getIntExtra("NEWS_ID", -1) +"");
            textData.put("content", data.getStringExtra("EDIT_NEWS_CONTENT"));
            textData.put("remove", data.getIntExtra("EDIT_NEWS_IMAGE_REMOVE", 0) + "");
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") +"");

            fileData.put(imageFile, byteArray);

        }else{

            textData.put("id", data.getIntExtra("NEWS_ID", -1) +"");
            textData.put("remove", data.getIntExtra("EDIT_NEWS_IMAGE_REMOVE", 0) + "");
            textData.put("content", data.getStringExtra("EDIT_NEWS_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("user_type", userData.getInt("USER_TYPE") +"");
        }

        WebServiceAction action =
                new DataImageActionWrapper(textData, fileData, super.getActivity(),AuthenticationAddress.UPDATE_NEWS ,new ProcessPostUpdateNews());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);
    }

    public void updateBlogContent(Intent data) {

        Uri uri = data.getData();
        Cursor cursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();

        if(uri != null){

            String mimeType = DataHelper.getMimeType(uri, super.getActivity());

            cursor = super.getActivity().getContentResolver().query(uri,
                    null, null,
                    null, null);

            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();

            String imageFile = cursor.getString(nameIndex);
            Bitmap image = null;
            int orientation = 0;
            byte[] byteArray = null;
            Bitmap rescaledImage = null;

            try{
                image = MediaStore.Images.Media.getBitmap(super.getActivity().getContentResolver(), uri);
                rescaledImage = DataHelper.resizeImageAspectRatio(image, 1920, 1080);
            }catch (IOException e){
                e.printStackTrace();
            }

            byteArray = DataHelper.bitmapToByteArray(rescaledImage, mimeType);


            textData.put("id", data.getIntExtra("BLOG_ID", -1) +"");
            textData.put("remove", data.getIntExtra("EDIT_NEWS_IMAGE_REMOVE", 0) + "");
            textData.put("heading", data.getStringExtra("EDIT_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("EDIT_BLOG_CONTENT"));
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");


            fileData.put(imageFile, byteArray);

        }else{

            textData.put("id", data.getIntExtra("BLOG_ID", -1) +"");
            textData.put("remove", data.getIntExtra("EDIT_NEWS_IMAGE_REMOVE", 0) + "");
            textData.put("heading", data.getStringExtra("EDIT_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("EDIT_BLOG_CONTENT"));
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");
        }

        WebServiceAction action = new DataImageActionWrapper(textData,
                fileData, super.getActivity(),AuthenticationAddress.UPDATE_BLOG, new ProcessPostUpdateBlog());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);

    }

    private class ProcessPostFetchPending implements PostAsyncAction{

        @Override
        public void processResult(String json) {

            if(json != null){
                try{

                    JSONObject data = new JSONObject(json);
                    String jsonData = data.getString("data");

                    addFeedEntries(jsonData);
                    setFetchingFeedEntry(false);
                    getSwipeRefreshLayout().setRefreshing(false);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private class ProcessPostDeleteNews implements PostAsyncAction {

        private String key;

        public ProcessPostDeleteNews(String key){
            this.key = key;
        }

        @Override
        public void processResult(String jsonResponse) {

            if(jsonResponse != null){
                int position = index.indexOf(key);
                index.remove(position);
                removeFromFeedList(key);
                notifyItemRemoved(position);
            }
        }
    }

    private class ProcessPostDeleteBlog implements PostAsyncAction {

        private String key;

        public ProcessPostDeleteBlog(String key){
            this.key = key;
        }

        @Override
        public void processResult(String jsonResponse) {

            int position = index.indexOf(key);
            index.remove(position);
            removeFromFeedList(key);
            notifyItemRemoved(position);
        }
    }

    private class ProcessAllowPendingPostAsync implements PostAsyncAction{

        private String key;

        public ProcessAllowPendingPostAsync(String key){
            this.key = key;
        }

        @Override
        public void processResult(String response) {

            //int position = index.indexOf(key);
            //index.remove(position);
            removeFromFeedList(key);
            //notifyItemRemoved(position);
        }
    }

    private class ProcessPostUpdateBlog implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){

            }else{

            }
        }
    }

    private class ProcessPostUpdateNews implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){

            }else{

            }
        }
    }

    private class RemoveFromCache implements LeastRecentlyUsedCache.OnRemoveFromCache{

        @Override
        public void onRemove(Object key) {
            String k = (String) key;
            int position = index.indexOf(k);
            index.remove(position);
            notifyItemRemoved(position);
        }
    }
}
