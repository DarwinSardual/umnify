package com.example.darwin.umnify.feed.blogs.feed_manager;

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
import android.view.View;
import android.widget.Toast;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.DataImageActionWrapper;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncDeleteAction;
import com.example.darwin.umnify.feed.blogs.Blog;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.AddBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.BlogCode;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.DeleteBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.UpdateBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.view_holder.BlogTileViewHolderGuest;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by darwin on 8/26/17.
 */

public class BlogFeedManagerAdmin<E extends BlogTileViewHolderGuest> extends BlogFeedManagerGuest<E>{

    private Bundle userData;

    public BlogFeedManagerAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Class<E> cls, int layoutId, Bundle userData){
        super(activity, swipeRefreshLayout, cls, layoutId);
        this.userData = userData;

    }

    @Override
    public void onBindViewHolder(E holder, int position) {

        if(!(position < super.getIndex().size())) return;

        String key = getIndex().get(position);
        final Blog blog = super.getEntryFromFeedList(key);

        if(blog != null) {
            holder.getBlogTileImageView().setImageBitmap(blog.getImage());
            holder.getBlogTileHeadingView().setText(blog.getHeading());



            holder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("BLOG_ID", blog.getId());
                    intent.putExtra("STATUS", 1);
                    intent.putExtra("BLOG_HEADING", blog.getHeading());
                    intent.putExtra("BLOG_CONTENT", blog.getContent());
                    intent.putExtra("BLOG_IMAGE", blog.getImageFile());
                    intent.putExtra("BLOG_AUTHOR", blog.getAuthor());
                    intent.putExtra("BLOG_PUBLISHED_DATE", blog.getPublishedDate());
                    intent.putExtra("BLOG_SIGNATURE", blog.getSignature());

                    intent.putExtra("BLOG_AUTHOR_FIRSTNAME", blog.getAuthorFirstname());
                    intent.putExtra("BLOG_AUTHOR_LASTNAME", blog.getAuthorLastname());
                    intent.putExtra("BLOG_AUTHOR_IMAGE", blog.getAuthorImage());

                    intent.putExtra("BLOG_INDEX", blog.getIndex());
                    intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                    intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));

                    BlogFeedManagerAdmin.super.getActivity().startActivityForResult(intent, BlogCode.VIEW_BLOG);
                }
            });
        }
    }

    @Override
    public void newFeedEntry(Intent data) {
        // add blog
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

            textData.put("heading", data.getStringExtra("ADD_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("ADD_BLOG_CONTENT"));
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");


            fileData.put(imageFile, byteArray);

        }else{
            textData.put("heading", data.getStringExtra("ADD_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("ADD_BLOG_CONTENT"));
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");
        }

        WebServiceAction action = new DataImageActionWrapper(textData,
                fileData, super.getActivity(),AuthenticationAddress.ADD_BLOG, new ProcessPostAdd());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);
    }

    @Override
    public void deleteFeedEntry(String key) {

        WebServiceAsync async = new WebServiceAsync();

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", key);

        ProcessPostDelete processPostDeleteBlog =
                new ProcessPostDelete(key);
        WebServiceAction action =
                new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.DELETE_BLOG,processPostDeleteBlog);

        async.execute(action);
        textDataOutput = null;
    }

    @Override
    public void updateFeedContent(Intent data) {

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
            textData.put("remove", data.getIntExtra("EDIT_BLOG_IMAGE_REMOVE", 0) + "");
            textData.put("heading", data.getStringExtra("EDIT_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("EDIT_BLOG_CONTENT"));
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");


            fileData.put(imageFile, byteArray);

        }else{

            textData.put("id", data.getIntExtra("BLOG_ID", -1) +"");
            textData.put("remove", data.getIntExtra("EDIT_BLOG_IMAGE_REMOVE", 0) + "");
            textData.put("heading", data.getStringExtra("EDIT_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("EDIT_BLOG_CONTENT"));
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");
        }

        WebServiceAction action = new DataImageActionWrapper(textData,
                fileData, super.getActivity(),AuthenticationAddress.UPDATE_BLOG, new ProcessPostUpdate());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);

    }

    public Bundle getUserData() {
        return userData;
    }

    private class ProcessPostUpdate implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            Log.e("Message", "Edit Blog - " + jsonResponse);
        }
    }

    private class ProcessPostDelete implements PostAsyncAction{

        private String key;

        public ProcessPostDelete(String key){
            this.key = key;
        }

        @Override
        public void processResult(String jsonResponse) {

            if(jsonResponse != null){
                Log.e("Message", "Delete blog - " + jsonResponse);
                //int position = getIndex().indexOf(key);
                //getIndex().remove(position);
                removeFromFeedList(key);
                //notifyItemRemoved(position);
            }else{
                Toast.makeText(getActivity(), "Failed to delete.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class ProcessPostAdd implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){
                Log.e("Message", "Add Blog - " + jsonResponse);
            }else{

            }
        }
    }
}
