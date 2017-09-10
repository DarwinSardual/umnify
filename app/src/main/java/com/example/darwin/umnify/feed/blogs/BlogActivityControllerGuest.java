package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogDataActionWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by darwin on 8/26/17.
 */

public class BlogActivityControllerGuest implements BlogActivityActions {

    private ImageView featuredImageView;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView contentView;

    private Bundle extraData;
    private Activity activity;

    private String heading;
    private String imageFile;

    public BlogActivityControllerGuest(Activity activity, Bundle extraData){
        this.extraData = extraData;
        this.activity = activity;

        toolbarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.blog_collapsing_toolbar);
        featuredImageView = (ImageView) activity.findViewById(R.id.blog_featured_image);
        contentView = (TextView) activity.findViewById(R.id.blog_content);

        heading =    extraData.getString("BLOG_TILE_HEADING");
        imageFile = extraData.getString("BLOG_TILE_IMAGE_FILE");

        WebServiceAsync async = new WebServiceAsync();
        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", extraData.getInt("BLOG_TILE_ID") + "");
        textDataOutput.put("heading", heading);
        textDataOutput.put("image_file", imageFile);
        textDataOutput.put("type", "activity");


        FetchBlogDataActionWrapper fetchBlogDataActionWrapper = new FetchBlogDataActionWrapper(textDataOutput, activity, this);

        async.execute(fetchBlogDataActionWrapper);

        textDataOutput = null;
    }

    @Override
    public void processResult(String jsonData) {


        try{

            JSONObject json = new JSONObject(jsonData);

            int authenticatedCode = json.getInt("authenticated");
            int fetchCode = json.getInt("fetch");

            if(authenticatedCode == AuthenticationCodes.AUTHENTICATED && fetchCode == AuthenticationCodes.FETCH_OK){

                String str = json.getString("data");
                JSONArray dataList = new JSONArray(str);
                JSONObject data = null;

                for(int i = 0; i < dataList.length(); i++){

                    data = new JSONObject(dataList.getString(i));
                    contentView.setText(data.getString("content"));
                }

                toolbarLayout.setTitle(heading);
                Bitmap image = BlogHelper.loadImageFromInternal(imageFile, activity);

                if(image != null){
                    featuredImageView.setImageBitmap(image);
                }else{

                }
            }

        }catch (JSONException e){

        }
    }

    public Bundle getExtraData() {
        return extraData;
    }
}
