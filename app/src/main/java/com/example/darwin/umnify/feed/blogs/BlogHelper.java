package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;

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

    public static Blog createBlogFromJSON(JSONObject data, int index) throws JSONException{

        int id = data.getInt("id");
        String heading = data.isNull("heading")? null: data.getString("heading");
        String content = data.isNull("content")? null:data.getString("content");
        String imageFile = data.isNull("image")? null: data.getString("image");
        int author = data.getInt("author");
        String createdDate = data.isNull("created_date")? null:data.getString("created_date");
        String publishedDate = data.isNull("published_date")? null:data.getString("published_date");
        int signature = data.getInt("signature");

        String authorFirstname = data.getString("author_firstname");
        String authorLastname = data.getString("author_lastname");
        String authorImage = data.getString("author_image");

        Blog blog = new Blog(id, heading, content, author,createdDate, publishedDate, imageFile, null,
                signature, index, authorFirstname, authorLastname, authorImage);

        return blog;
    }

    public static Blog createBlogFromJSON(String data){

        try{

            JSONObject json = new JSONObject(data);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    public static long addBlogToLocalDb(Blog blog, Context context){

        UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(context);
        SQLiteDatabase databaseWrite = databaseConnection.getWritableDatabase();
        SQLiteDatabase databaseRead = databaseConnection.getReadableDatabase();

        long id = 0;

        String[] selectionArgs = {blog.getId() + ""};
        Cursor cursor = databaseRead.rawQuery("select id from Blog where id =? ",
                selectionArgs);

        if(cursor.getCount() > 0){

            String[] selectionArgs2 = {blog.getId() + "", blog.getSignature() + ""};
            cursor = databaseRead.rawQuery("select id from Blog where id =? and signature != ?",
                    selectionArgs2);

            if(cursor.getCount() > 0){
                updateBlogToLocalDb(blog, context);
            }else{
                // dont do anything
            }

        }else{
            ContentValues values = new ContentValues();
            values.put(UMnifyContract.UMnifyColumns.Blog.ID.toString(), blog.getId());
            values.put(UMnifyContract.UMnifyColumns.Blog.HEADING.toString(), blog.getHeading());
            values.put(UMnifyContract.UMnifyColumns.Blog.CONTENT.toString(), blog.getContent());
            values.put(UMnifyContract.UMnifyColumns.Blog.IMAGE.toString(), blog.getImageFile());
            values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR.toString(), blog.getAuthor());
            values.put(UMnifyContract.UMnifyColumns.Blog.CREATED_DATE.toString(), blog.getCreatedDate());
            values.put(UMnifyContract.UMnifyColumns.Blog.PUBLISHED_DATE.toString(), blog.getPublishedDate());
            values.put(UMnifyContract.UMnifyColumns.Blog.SIGNATURE.toString(), blog.getSignature());

            values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR_FIRSTNAME.toString(), blog.getAuthorFirstname());
            values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR_LASTNAME.toString(), blog.getAuthorLastname());
            values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR_IMAGE.toString(), blog.getAuthorImage());
            id = databaseWrite.insert(UMnifyContract.UMnifyColumns.Blog.TABLE_NAME.toString(), null, values);
        }

        return id;
    }

    public static long updateBlogToLocalDb(Blog blog, Context context){

        UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(context);
        SQLiteDatabase databaseWrite = databaseConnection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UMnifyContract.UMnifyColumns.Blog.HEADING.toString(), blog.getHeading());
        values.put(UMnifyContract.UMnifyColumns.Blog.CONTENT.toString(), blog.getContent());
        values.put(UMnifyContract.UMnifyColumns.Blog.IMAGE.toString(), blog.getImageFile());
        values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR.toString(), blog.getAuthor());
        values.put(UMnifyContract.UMnifyColumns.Blog.CREATED_DATE.toString(), blog.getCreatedDate());
        values.put(UMnifyContract.UMnifyColumns.Blog.PUBLISHED_DATE.toString(), blog.getPublishedDate());
        values.put(UMnifyContract.UMnifyColumns.Blog.SIGNATURE.toString(),blog.getSignature());

        values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR_FIRSTNAME.toString(), blog.getAuthorFirstname());
        values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR_LASTNAME.toString(), blog.getAuthorLastname());
        values.put(UMnifyContract.UMnifyColumns.Blog.AUTHOR_IMAGE.toString(), blog.getAuthorImage());

        String selection = UMnifyContract.UMnifyColumns.Blog.ID.toString() + " = ?";
        String[] selectionArgs = {blog.getId() + ""};

        long count = databaseWrite.update(UMnifyContract.UMnifyColumns.Blog.TABLE_NAME.toString(),
                values, selection, selectionArgs);

        return count;
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
                if(outputStream != null)
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
                if(inputStream != null)
                 inputStream.close();
            }catch (IOException i){
                i.printStackTrace();
            }
        }
        return image;
    }
}
