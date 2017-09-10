package com.example.darwin.umnify.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.darwin.umnify.PostResultAction;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.gallery.blog.GalleryBlogActivity;
import com.example.darwin.umnify.gallery.blog.data_action_wrapper.FetchBlogImageFileDataActionWrapper;
import com.example.darwin.umnify.gallery.news.GalleryNewsActivity;
import com.example.darwin.umnify.gallery.news.data_action_wrapper.FetchNewsImageDataActionWrapper;
import com.example.darwin.umnify.gallery.news.data_action_wrapper.FetchNewsImageFileDataActionWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements PostResultAction{

    private RelativeLayout galleryNews;
    private ImageView newsThumbnail1;
    private ImageView newsThumbnail2;
    private ImageView newsThumbnail3;

    private RelativeLayout galleryBlog;

    private ImageView blogThumbnail1;
    private ImageView blogThumbnail2;
    private ImageView blogThumbnail3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryNews = (RelativeLayout) findViewById(R.id.gallery_news);
        newsThumbnail1 = (ImageView) findViewById(R.id.gallery_news_overlay_1);
        newsThumbnail2 = (ImageView) findViewById(R.id.gallery_news_overlay_2);
        newsThumbnail3 = (ImageView) findViewById(R.id.gallery_news_overlay_3);

        galleryBlog = (RelativeLayout) findViewById(R.id.gallery_blog);
        blogThumbnail1 = (ImageView) findViewById(R.id.gallery_blog_overlay_1);
        blogThumbnail2 = (ImageView) findViewById(R.id.gallery_blog_overlay_2);
        blogThumbnail3 = (ImageView) findViewById(R.id.gallery_blog_overlay_3);

        GalleryHandler galleryHandler = new GalleryHandler();

        galleryNews.setOnClickListener(galleryHandler);
        galleryBlog.setOnClickListener(galleryHandler);

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("offset", 0 + "");
        textDataOutput.put("limit", 3 + "");

        WebServiceAsync asyncNews = new WebServiceAsync();
        WebServiceAsync asyncBlog = new WebServiceAsync();

        FetchNewsImageFileDataActionWrapper fetchNewsImageFileDataActionWrapper =
                new FetchNewsImageFileDataActionWrapper(textDataOutput, this, this);
        FetchBlogImageFileDataActionWrapper fetchBlogImageFileDataActionWrapper =
                new FetchBlogImageFileDataActionWrapper(textDataOutput, this, this);

        asyncNews.execute(fetchNewsImageFileDataActionWrapper);
        //asyncBlog.execute(fetchBlogImageFileDataActionWrapper);
    }

    private class GalleryHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            Intent intent = null;

            if(view == galleryNews){

                intent = new Intent(GalleryActivity.this, GalleryNewsActivity.class);
                startActivity(intent);

            }else if(view == galleryBlog){
                intent = new Intent(GalleryActivity.this, GalleryBlogActivity.class);
                startActivity(intent);

            }
        }
    }

    private void addThumbnail(String jsonData, ImageView view, String urlLocation) throws JSONException{
        JSONObject imageData = new JSONObject(jsonData);
        String imageFile = imageData.getString("image");
        urlLocation = urlLocation + "/" +imageFile;

        WebServiceAsync async = new WebServiceAsync();
        FetchGalleryThumbnailDataActionWrapper fetchGalleryThumbnailDataActionWrapper =
                new FetchGalleryThumbnailDataActionWrapper(view, urlLocation, this);

        async.execute(fetchGalleryThumbnailDataActionWrapper);
    }

    @Override
    public void onPostResultAction(String jsonData) throws JSONException {
        JSONArray dataList = new JSONArray(jsonData);
        List<ImageView> imageViewList = new ArrayList<>();
        imageViewList.add(newsThumbnail1);
        imageViewList.add(newsThumbnail2);
        imageViewList.add(newsThumbnail3);

        imageViewList.add(blogThumbnail1);
        imageViewList.add(blogThumbnail2);
        imageViewList.add(blogThumbnail3);

        List<String> imageLocation = new ArrayList<>();
        imageLocation.add(AuthenticationAddress.NEWS_IMAGE_FOLDER);
        imageLocation.add(AuthenticationAddress.NEWS_IMAGE_FOLDER);
        imageLocation.add(AuthenticationAddress.NEWS_IMAGE_FOLDER);

        imageLocation.add(AuthenticationAddress.BLOG_IMAGE_FOLDER);
        imageLocation.add(AuthenticationAddress.BLOG_IMAGE_FOLDER);
        imageLocation.add(AuthenticationAddress.BLOG_IMAGE_FOLDER);

        for(int i = 0; i < dataList.length(); i++){
            ImageView view = imageViewList.get(i);
            String location = imageLocation.get(i);
            addThumbnail(dataList.getString(i), view, location);
        }

        dataList = null;
    }
}
