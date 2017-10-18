package com.example.darwin.umnify.feed.notifications.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncDeleteAction;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.DeleteBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.UpdateBlogDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.DeleteNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.UpdateNewsDataActionWrapper;
import com.example.darwin.umnify.feed.notifications.Notification;
import com.example.darwin.umnify.feed.notifications.view_holder.NotificationViewHolder;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by darwin on 9/24/17.
 */

public class NotificationFeedManagerAdmin<E extends NotificationViewHolder> extends NotificationFeedManager<E>{

    public NotificationFeedManagerAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                        Class<E> cls, Bundle userData, int layoutId){

        super(activity, swipeRefreshLayout, cls, userData, layoutId);
    }

    public void deleteFeedEntry(int pos, String type){

    }

    @Override
    public void deleteFeedEntry(String key) {


        Notification notification = super.getEntryFromFeedList(key);
        WebServiceAsync async = new WebServiceAsync();

        String refKey = Integer.toString(notification.getRefId());

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", refKey);

        WebServiceAction deleteDataActionWrapper = null;

        if(notification.getType().equalsIgnoreCase("news")){

            ProcessPostDeleteNews processPostDeleteNews =
                    new ProcessPostDeleteNews(key, refKey);
            deleteDataActionWrapper =
                    new DeleteNewsDataActionWrapper(textDataOutput, super.getActivity(), processPostDeleteNews);

        }else if(notification.getType().equalsIgnoreCase("blog")){

            ProcessPostDeleteBlog processPostDeleteBlog =
                    new ProcessPostDeleteBlog(key, refKey);
            deleteDataActionWrapper = new
                    DeleteBlogDataActionWrapper(textDataOutput, super.getActivity(), processPostDeleteBlog);
        }

        async.execute(deleteDataActionWrapper);
    }


    @Override
    public void updateFeedContent(Intent data) {

        String type = data.getStringExtra("TYPE");
        if(type.equalsIgnoreCase("news")){
            updateNewsContent(data);
        }else if(type.equalsIgnoreCase("blog")){
            updatBlogContent(data);
        }
    }

    public void updateNewsContent(Intent data) {

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
            Bitmap rescaledImage =null;

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

        UpdateNewsDataActionWrapper updateNewsDataActionWrapper =
                new UpdateNewsDataActionWrapper(textData, fileData, super.getActivity(), new ProcessPostUpdateBlog());

        WebServiceAsync asyncAddNews = new WebServiceAsync();
        asyncAddNews.execute(updateNewsDataActionWrapper);
    }

    public void updatBlogContent(Intent data) {

        Uri uri = data.getData();
        Cursor cursor;
        Bundle userData = super.getUserData();

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

        UpdateBlogDataActionWrapper updateBlogDataActionWrapper = new UpdateBlogDataActionWrapper(textData,
                fileData, super.getActivity(), new ProcessPostUpdateBlog());

        WebServiceAsync asyncAddBlog = new WebServiceAsync();
        asyncAddBlog.execute(updateBlogDataActionWrapper);

    }

    private class ProcessPostDeleteNews implements PostAsyncDeleteAction {

        private String notificationKey;
        private String newsKey;

        public ProcessPostDeleteNews(String notificationKey, String newsKey){

            this.notificationKey = notificationKey;
            this.newsKey = newsKey;
        }

        @Override
        public String getKey() {
            return newsKey;
        }

        @Override
        public void processResult(String response) {

            int position = getIndex().indexOf(notificationKey);
            getIndex().remove(position);
            removeFromFeedList(notificationKey);
            notifyItemRemoved(position);
        }
    }

    private class ProcessPostDeleteBlog implements PostAsyncDeleteAction {

        private String notificationKey;
        private String blogKey;

        public ProcessPostDeleteBlog(String notificationKey, String blogKey){

            this.notificationKey = notificationKey;
            this.blogKey = blogKey;
        }

        @Override
        public String getKey() {
            return blogKey;
        }

        @Override
        public void processResult(String jsonResponse) {

            int position = getIndex().indexOf(notificationKey);
            getIndex().remove(position);
            removeFromFeedList(notificationKey);
            notifyItemRemoved(position);
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
}
