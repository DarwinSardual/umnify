package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogImageDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchAuthorImageDataActionWrapper;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.gallery.ViewImageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by darwin on 8/26/17.
 */

public class BlogActivityControllerGuest{

    private ImageView featuredImageView;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView contentView;
    private CircleImageView authorImageView;
    private TextView authorView;
    private TextView publishedDateView;

    private Bundle extraData;
    private Activity activity;

    private int id;
    private String heading;
    private String content;
    private String imageFile;
    private int author;
    private String publishedDate;
    private int signature;

    private String authorFirstname;
    private String authorLastname;
    private String authorImage;

    public BlogActivityControllerGuest(Activity activity, Bundle extraData){
        this.extraData = extraData;
        this.activity = activity;
        toolbarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
        featuredImageView = (ImageView) activity.findViewById(R.id.featured_image);
        contentView = (TextView) activity.findViewById(R.id.content);

        authorImageView = (CircleImageView) activity.findViewById(R.id.author_image);
        authorView = (TextView) activity.findViewById(R.id.author);
        publishedDateView = (TextView) activity.findViewById(R.id.published_date);

        if(extraData.getInt("STATUS") == 0){

            id = extraData.getInt("BLOG_ID");

            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("id", id + "");

            WebServiceAsync asyncFetchBlog = new WebServiceAsync();
            FetchBlogDataActionWrapper fetchBlogDataActionWrapper =
                    new FetchBlogDataActionWrapper(textDataOutput, activity, new ProcessPostFetchData());

            asyncFetchBlog.execute(fetchBlogDataActionWrapper);

        }else if(extraData.getInt("STATUS") == 1){



            heading =   extraData.getString("BLOG_HEADING");
            content = extraData.getString("BLOG_CONTENT");
            imageFile = extraData.getString("BLOG_IMAGE");
            author = extraData.getInt("BLOG_AUTHOR");
            String str = null;
            if(extraData.getString("BLOG_PUBLISHED_DATE") != null){
                String temp [] = extraData.getString("BLOG_PUBLISHED_DATE").split(" ");
                String first = DateHelper.convertDateToMDY(temp[0]);
                String second = DateHelper.convert24Hourto12Hour(temp[1]);
                str = first + " " + second;
            }


            publishedDate = str;
            signature = extraData.getInt("BLOG_SIGNATURE");

            authorFirstname = extraData.getString("BLOG_AUTHOR_FIRSTNAME");
            authorLastname = extraData.getString("BLOG_AUTHOR_LASTNAME");
            authorImage = extraData.getString("BLOG_AUTHOR_IMAGE");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setBlogActivity();
                }
            }, 1000);
        }


    }

    private void setBlogActivity(){

        toolbarLayout.setTitle(heading);
        contentView.setText(content);
        authorView.setText(authorFirstname + " "  + authorLastname);
        publishedDateView.setText(publishedDate);

        Bitmap image = GalleryHelper.loadImageFromInternal(imageFile, activity, "feed/blog");

        if(image != null){
            featuredImageView.setImageBitmap(image);
        }else{
            WebServiceAsync async = new WebServiceAsync();
            FetchBlogImageDataActionWrapper fetchBlogImageDataActionWrapper =
                    new FetchBlogImageDataActionWrapper(activity, new ProcessPostFetchImage(imageFile));

            async.execute(fetchBlogImageDataActionWrapper);
        }

        Bitmap authorImg = GalleryHelper.loadImageFromInternal(authorImage, activity, "avatar");

        if(authorImg != null){
            authorImageView.setImageBitmap(authorImg);
        }else{
            WebServiceAsync async = new WebServiceAsync();
            FetchAuthorImageDataActionWrapper fetchAuthorImageDataActionWrapper =
                    new FetchAuthorImageDataActionWrapper(activity, new ProcessPostFetchAuthorImage(authorImage));

            async.execute(fetchAuthorImageDataActionWrapper);
        }

        featuredImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlogActivityControllerGuest.this.activity, ViewImageActivity.class);
                intent.putExtra("ROOT_LOCATION", AuthenticationAddress.BLOG_IMAGE_FOLDER_NON);
                intent.putExtra("FOLDER", "feed/blog");
                intent.putExtra("IMAGE_FILE", imageFile);
                BlogActivityControllerGuest.this.activity.startActivity(intent);
            }
        });




    }

    public Bundle getExtraData() {
        return extraData;
    }

    private class ProcessPostFetchData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){
                try{

                    JSONObject json = new JSONObject(jsonResponse);
                    String data = json.getString("data");
                    JSONArray arr = new JSONArray(data);

                    for(int counter = 0; counter < 1; counter++) {

                        JSONObject blogData = new JSONObject(arr.getString(counter));

                        id = blogData.getInt("id");
                        heading = blogData.getString("heading");
                        content = blogData.getString("content");
                        imageFile = blogData.getString("image");;
                        author = blogData.getInt("author");

                        String str = null;
                        if(blogData.isNull("published_date")){
                            str = "Not yet published";
                        }else{
                            String temp[] = blogData.getString("published_date").split(" ");
                            String first = DateHelper.convertDateToMDY(temp[0]);
                            String second = DateHelper.convert24Hourto12Hour(temp[1]);
                            str = first + " " + second;
                        }

                        publishedDate = str;
                        signature = blogData.getInt("signature");

                        authorFirstname = blogData.getString("author_firstname");
                        authorLastname = blogData.getString("author_lastname");
                        authorImage = blogData.getString("author_image");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                setBlogActivity();
            }
        }
    }

    private class ProcessPostFetchImage implements PostAsyncImageAction{

        private String imageFile;

        public ProcessPostFetchImage(String imageFile){
            this.imageFile = imageFile;
        }

        @Override
        public String getImageFile() {
            return imageFile;
        }

        @Override
        public void processResult(Bitmap image) {

        }
    }

    private class ProcessPostFetchAuthorImage implements PostAsyncImageAction{

        private String imageFile;

        public ProcessPostFetchAuthorImage(String imageFile){
            this.imageFile = imageFile;
        }

        @Override
        public String getImageFile() {
            return imageFile;
        }

        @Override
        public void processResult(Bitmap image) {

        }
    }
}
