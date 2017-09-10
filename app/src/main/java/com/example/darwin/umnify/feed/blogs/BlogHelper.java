package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.darwin.umnify.authentication.AuthenticationAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BlogHelper {

    public static BlogTile createBlogTileFromJSON(JSONObject data, int index) throws JSONException{

        int id = data.getInt("id");
        String heading = data.getString("heading");
        String imageFile = data.getString("image");
        int author = data.getInt("author");

        BlogTile blog = new BlogTile(id, heading, imageFile, author,index);

        return blog;
    }

    public static Blog createBlogFromJSON(int id, String heading, String imageFile, int index, String str){

        try{

            JSONObject data = new JSONObject(str);


            Blog blog = new Blog(id, heading, data.getString("content"), data.getInt("author"),
                    data.getString("published_date"), imageFile, null, data.getInt("signature"));

            return blog;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    public static Blog createBlogFromJSON(String data){

        try{

            JSONObject json = new JSONObject(data);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    public static String saveImageToInternal(Bitmap image, String filename, Activity activity){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);

        File imageDirectory = new File(root.getAbsolutePath() + "/images/feed/blog");
        File myPath = new File(imageDirectory, filename);
        FileOutputStream outputStream = null;

        try{
            outputStream = new FileOutputStream(myPath);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                outputStream.close();
            }catch (IOException i){
                i.printStackTrace();
            }
        }
        return imageDirectory.getAbsolutePath();
    }

    public static Bitmap loadImageFromInternal(String filename, Activity activity){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);
        File imageFile = new File(root.getAbsolutePath() + "/images/feed/blog/" + filename);
        Bitmap image = null;
        FileInputStream inputStream = null;

        try{

            inputStream = new FileInputStream(imageFile);
            image = BitmapFactory.decodeStream(inputStream);

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            try{
                inputStream.close();
            }catch (IOException i){
                i.printStackTrace();
            }
        }
        return image;
    }
}
