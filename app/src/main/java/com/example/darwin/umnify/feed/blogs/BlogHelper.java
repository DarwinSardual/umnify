package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.util.Log;

import com.example.darwin.umnify.authentication.AuthenticationAddress;

import org.json.JSONException;
import org.json.JSONObject;

public class BlogHelper {

    public static BlogTile createBlogTileFromJSON(JSONObject data, int index) throws JSONException{

        int id = data.getInt("id");
        String heading = data.getString("heading");
        String imageFile = data.getString("image");

        BlogTile blog = new BlogTile(id, heading, imageFile, index);

        return blog;
    }

    public static void fetchImage(BlogTile tile, BlogFeedManager manager, Activity activity){



        BlogTileWrapper wrapper = new BlogTileWrapper(tile, manager);
        BlogImageAsync blogImageAsync = new BlogImageAsync(AuthenticationAddress.BLOG_IMAGE_FOLDER + "/" + wrapper.tile.getImageFile(), activity);
        blogImageAsync.execute(wrapper);
    }
}
