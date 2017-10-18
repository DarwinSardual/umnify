package com.example.darwin.umnify.feed.news.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.DataImageActionWrapper;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.OnDeleteFeed;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncDeleteAction;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsOptionViewListener;
import com.example.darwin.umnify.feed.news.data_action_wrapper.AddNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.DeleteNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.UpdateNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderAdmin;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.IOException;
import java.util.HashMap;

public class NewsFeedManagerAdmin<E extends NewsViewHolderAdmin> extends NewsFeedManagerNormal<E>{



    public NewsFeedManagerAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView,
                                Bundle userData, Class<E> cls, int layoutId){
        super(activity, swipeRefreshLayout, recyclerView, userData, cls, layoutId);

    }

    @Override
    public void onBindViewHolder(E holder, int position) {
        super.onBindViewHolder(holder, position);

        if(!(position < super.getIndex().size())) return;

        String id = super.getIndex().get(position);
        News news = super.getEntryFromFeedList(id);
        if(news != null){
            int userId = super.getUserData().getInt("USER_ID");

                holder.getNewsOptionView().setOnItemSelectedListener(new NewsOptionViewListener(super.getActivity(), new ProcessDeleteFeed(Integer.parseInt(id)), news.getId()));
            if(userId != news.getAuthorId()) {
                holder.getNewsOptionView().setVisibility(View.INVISIBLE);
                holder.getNewsOptionView().setEnabled(false);
            }else{
                holder.getNewsOptionView().setVisibility(View.VISIBLE);
                holder.getNewsOptionView().setEnabled(true);

            }
        }
    }

    @Override
    public void deleteFeedEntry(String key) {

        WebServiceAsync async = new WebServiceAsync();

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", key);

        PostAsyncAction postProcess = new ProcessPostDelete(key);
        WebServiceAction action = new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.DELETE_NEWS, postProcess);

        async.execute(action);
        textDataOutput = null;


    }

    public void newFeedEntry(Intent data){
        // add news here
        Uri uri = data.getData();
        Cursor returnCursor;
        Bundle userData = super.getUserData();

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
                byteArray = DataHelper.bitmapToByteArray(rescaledImage, mimeType);
            }catch (IOException e){
                e.printStackTrace();
            }

            //byteArray = DataHelper.bitmapToByteArray(image, mimeType);
            textData.put("content", data.getStringExtra("ADD_NEWS_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") +"");

            fileData.put(imageFile, byteArray);

        }else{
            textData.put("content", data.getStringExtra("ADD_NEWS_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("user_type", userData.getInt("USER_TYPE") +"");
        }

       WebServiceAction action = new DataImageActionWrapper(textData,
                fileData, super.getActivity(), AuthenticationAddress.ADD_NEWS,new ProcessPostAdd());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);

    }

    @Override
    public void updateFeedContent(Intent data) {

        Uri uri = data.getData();
        Cursor returnCursor;
        Bundle userData = super.getUserData();

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
                new DataImageActionWrapper(textData, fileData, super.getActivity(), AuthenticationAddress.UPDATE_NEWS, new ProcessPostUpdateData());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);
    }

    private class ProcessDeleteFeed implements OnDeleteFeed{

        private int pos;

        public ProcessDeleteFeed(int pos){
            this.pos = pos;
        }

        public int getIndex() {
            return pos;
        }

        @Override
        public void onDeleteFeed() {
            deleteFeedEntry(getIndex() + "");
        }
    }

    private class ProcessPostAdd implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {

            if(jsonResponse != null){
                Log.e("Message", jsonResponse);
            }

        }
    }

    private class ProcessPostDelete implements PostAsyncAction {

        private String key;

        public ProcessPostDelete(String key){
            this.key = key;
        }

        @Override
        public void processResult(String response) {

            if(response != null){
                Log.e("Message", "Delete News - " + response);
                /*int position = getIndex().indexOf(key);
                getIndex().remove(position);
                removeFromFeedList(key);
                notifyItemRemoved(position);*/
                removeFromFeedList(key);
            }else{
                Toast.makeText(getActivity(), "Failed to delete.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class ProcessPostUpdateData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){
                Log.e("Message", "Update News - " + jsonResponse);
            }else{

            }
        }
    }
}
