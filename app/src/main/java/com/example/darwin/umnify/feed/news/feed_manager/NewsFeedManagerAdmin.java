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
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsOptionViewListener;
import com.example.darwin.umnify.feed.news.data_action_wrapper.AddNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.DeleteNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderAdmin;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderNormal;
import com.example.darwin.umnify.wrapper.DataHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class NewsFeedManagerAdmin<E extends NewsViewHolderAdmin> extends NewsFeedManagerNormal<E>{



    public NewsFeedManagerAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Bundle userData, Class<E> cls, int layoutId){
        super(activity, swipeRefreshLayout, userData, cls, layoutId);

    }

    @Override
    public void onBindViewHolder(E holder, int position) {
        super.onBindViewHolder(holder, position);

        News news = super.getEntryFromFeedList(position);
        if(news != null){
            int userId = super.getUserData().getInt("USER_ID");
            holder.getNewsOptionView().setOnItemSelectedListener(new NewsOptionViewListener(news, this));
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
    public void deleteFeedEntry(News news) {
        WebServiceAsync async = new WebServiceAsync();
        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", news.getId() + "");
        Log.e("Id", news.getId() + "");
        DeleteNewsDataActionWrapper deleteNewsDataActionWrapper = new DeleteNewsDataActionWrapper(textDataOutput, news, super.getActivity(), this);

        async.execute(deleteNewsDataActionWrapper);

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
            int orientation = 0;
            Bitmap rescaledImage = null;

            try{
                image = MediaStore.Images.Media.getBitmap(super.getActivity().getContentResolver(), uri);

                if(image.getWidth() < 896 && image.getHeight() < 504){
                    rescaledImage = image;
                }else{

                    if(image.getWidth() > image.getHeight()){
                        orientation = 0;
                        rescaledImage = DataHelper.resizeImageAspectRatio(image, 896, orientation);
                    }else{
                        orientation = 1;
                        rescaledImage = DataHelper.resizeImageAspectRatio(image, 504, orientation);
                    }
                }
                byteArray = DataHelper.bitmapToByteArray(rescaledImage, mimeType);
            }catch (IOException e){
                e.printStackTrace();
            }

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

       AddNewsDataActionWrapper addNewsDataActionWrapper = new AddNewsDataActionWrapper(textData,
                fileData, super.getActivity());

        WebServiceAsync asyncAddNews = new WebServiceAsync();
        asyncAddNews.execute(addNewsDataActionWrapper);

    }
}
