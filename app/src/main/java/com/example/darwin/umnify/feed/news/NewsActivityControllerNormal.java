package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchAuthorImageDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsImageDataActionWrapper;

import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.gallery.ViewImageActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by darwin on 9/17/17.
 */

public class NewsActivityControllerNormal{

    private int id;
    private TextView newsContentView;
    private ImageView newsImageView;
    private TextView newsAuthorView;
    private ImageView newsAuthorImageView;
    private TextView newsDateView;

    private Activity activity;
    private Bundle extraData;

    public NewsActivityControllerNormal(Activity activity, Bundle extraData){

        this.activity = activity;
        this.extraData = extraData;

        newsContentView = (TextView) activity.findViewById(R.id.content);
        newsImageView = (ImageView) activity.findViewById(R.id.image);
        newsAuthorView = (TextView) activity.findViewById(R.id.author);
        newsAuthorImageView = (ImageView) activity.findViewById(R.id.author_image);
        newsDateView = (TextView) activity.findViewById(R.id.date);


        if(extraData.getInt("STATUS") == 0){
            //fetch

            id = extraData.getInt("NEWS_ID");
            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("id", id + "");
            WebServiceAsync asyncFetchNews = new WebServiceAsync();
            FetchNewsDataActionWrapper fetchNewsDataActionWrapper =
                    new FetchNewsDataActionWrapper(textDataOutput, activity, new ProcessPostFetchData());

            asyncFetchNews.execute(fetchNewsDataActionWrapper);
        }else if(extraData.getInt("STATUS") == 1){

        }
    }


    private class ProcessPostFetchNewsImage implements PostAsyncImageAction {

        private String imageFile;
        private Activity activity;
        private ImageView imageView;

        public ProcessPostFetchNewsImage(String imageFile, ImageView imageView, Activity activity){
            this.imageFile = imageFile;
            this.imageView = imageView;
            this.activity = activity;
        }

        @Override
        public String getImageFile() {
            return imageFile;
        }

        @Override
        public void processResult(Bitmap image) {
            if(image != null){
                GalleryHelper.saveImageToInternal(image,
                        imageFile, activity, "feed/news");
                imageView.setImageBitmap(image);

            }
        }
    }

    private class ProcessPostFetchAuthorImage implements PostAsyncImageAction {

        private String imageFile;
        private Activity activity;
        private ImageView imageView;

        public ProcessPostFetchAuthorImage(String imageFile, ImageView imageView, Activity activity){
            this.imageFile = imageFile;
            this.imageView = imageView;
            this.activity = activity;
        }

        @Override
        public String getImageFile() {
            return imageFile;
        }

        @Override
        public void processResult(Bitmap image) {
            if(image != null){
                GalleryHelper.saveImageToInternal(image,
                        imageFile, activity, "avatar");
                imageView.setImageBitmap(image);

            }
        }
    }

    private class ProcessPostFetchData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){


                try{

                    JSONObject json = new JSONObject(jsonResponse);
                    String data = json.getString("data");

                    JSONArray dataList = new JSONArray(data);

                    // the length of this should only be 1
                    for(int counter = 0; counter < 1; counter++){

                        String newsData = dataList.getString(counter);
                        JSONObject newsJson = new JSONObject(newsData);

                        newsAuthorView.setText(newsJson.getString("firstname") + " " + newsJson.getString("lastname"));
                        newsContentView.setText(newsJson.getString("content"));
                        String str = null;
                        if(newsJson.isNull("published_date")){
                            str = "not published yet";
                        }else{
                            String temp[] = newsJson.getString("published_date").split(" ");
                            String first = DateHelper.convertDateToMDY(temp[0]);
                            String second = DateHelper.convert24Hourto12Hour(temp[1]);
                            str = first + " " + second;
                        }

                        newsDateView.setText(str);

                        final String newsImageFile = newsJson.isNull("image")? null: newsJson.getString("image");

                        if(newsImageFile != null){
                            Bitmap newsImage = GalleryHelper.loadImageFromInternal(newsImageFile, activity, "feed/news");
                            if(newsImage != null){
                                newsImageView.setImageBitmap(newsImage);
                            }else{
                                WebServiceAsync asnycFetchNewsImage = new WebServiceAsync();
                                ProcessPostFetchNewsImage processOnFetchNewsImage = new ProcessPostFetchNewsImage(newsImageFile, newsImageView, activity);
                                FetchNewsImageDataActionWrapper fetchNewsImageDataActionWrapper =
                                        new FetchNewsImageDataActionWrapper(activity, processOnFetchNewsImage);

                                asnycFetchNewsImage.execute(fetchNewsImageDataActionWrapper);
                            }
                        }

                        String authorImageFile = newsJson.isNull("author_image")? null: newsJson.getString("author_image");
                        if(authorImageFile != null){
                            Bitmap authorImage = GalleryHelper.loadImageFromInternal(authorImageFile, activity, "avatar");
                            if(authorImageFile != null){
                                newsAuthorImageView.setImageBitmap(authorImage);
                            }else{
                                WebServiceAsync asnycFetchAuthorImage = new WebServiceAsync();
                                ProcessPostFetchAuthorImage processPostFetchAuthorImage = new ProcessPostFetchAuthorImage(authorImageFile, newsAuthorImageView, activity);
                                FetchAuthorImageDataActionWrapper fetchAuthorImageDataActionWrapper =
                                        new FetchAuthorImageDataActionWrapper(activity, processPostFetchAuthorImage);

                                asnycFetchAuthorImage.execute(fetchAuthorImageDataActionWrapper);
                            }
                        }

                        newsImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, ViewImageActivity.class);
                                intent.putExtra("ROOT_LOCATION", AuthenticationAddress.NEWS_IMAGE_FOLDER);
                                intent.putExtra("FOLDER", "feed/news");
                                intent.putExtra("IMAGE_FILE", newsImageFile);
                                activity.startActivity(intent);
                            }
                        });

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{

            }
        }
    }
}
