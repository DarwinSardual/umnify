package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class NewsHelper {

    public static News createNewsFromJSON(JSONObject data, int index) throws JSONException{

        int id = data.getInt("id");
        String content = data.getString("content");
        String imageFile = data.isNull("image")? null : data.getString("image");
        int authorId = data.getInt("author");
        String createdDate = data.isNull("created_date")? null : data.getString("created_date");
        String publishedDate = data.isNull("published_date")? null : data.getString("published_date");
        int signature = data.getInt("signature");

        String authorFirstname = data.getString("firstname");
        String authorLastname = data.getString("lastname");
        String authorImageFile = data.getString("author_image");
        //int stars = data.getInt("stars");
        //boolean isStarred = data.getBoolean("starred");

        News news = new News(id, content, imageFile, authorId,createdDate, publishedDate, signature, 0, false,
                index, authorFirstname, authorLastname, authorImageFile);

        return news;

    }

    public static long addNewsToLocalDb(News news, Context context){

        UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(context);
        SQLiteDatabase databaseWrite = databaseConnection.getWritableDatabase();
        SQLiteDatabase databaseRead = databaseConnection.getReadableDatabase();
        long id = 0;

        String[] selectionArgs = {news.getId() + ""};
        Cursor cursor = databaseRead.rawQuery("select id from News where id =? ",
                selectionArgs);

        if(cursor.getCount() > 0){

            String[] selectionArgs2 = {news.getId() + "", news.getSignature() + ""};
            cursor = databaseRead.rawQuery("select id from News where id =? and signature != ?",
                    selectionArgs2);

            if(cursor.getCount() > 0){
                updateNewsToLocalDb(news, context);
            }else{
                // dont do anything
            }

        }else{

            ContentValues values = new ContentValues();
            values.put(UMnifyContract.UMnifyColumns.News.ID.toString(), news.getId());
            values.put(UMnifyContract.UMnifyColumns.News.CONTENT.toString(), news.getContent());
            values.put(UMnifyContract.UMnifyColumns.News.IMAGE.toString(), news.getImageFile());
            values.put(UMnifyContract.UMnifyColumns.News.AUTHOR.toString(), news.getAuthorId());
            values.put(UMnifyContract.UMnifyColumns.News.CREATED_DATE.toString(), news.getCreatedDate());
            values.put(UMnifyContract.UMnifyColumns.News.PUBLISHED_DATE.toString(), news.getPublishedDate());
            values.put(UMnifyContract.UMnifyColumns.News.SIGNATURE.toString(), news.getSignature());
            values.put(UMnifyContract.UMnifyColumns.News.AUTHOR_FIRSTNAME.toString(), news.getAuthorFirstname());
            values.put(UMnifyContract.UMnifyColumns.News.AUTHOR_LASTNAME.toString(), news.getAuthorLastname());
            values.put(UMnifyContract.UMnifyColumns.News.AUTHOR_IMAGE.toString(), news.getAuthorImageFile());

             id = databaseWrite.insert(UMnifyContract.UMnifyColumns.News.TABLE_NAME.toString(), null, values);
        }



        return id;
    }

    public static long updateNewsToLocalDb(News news, Context context){

        UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(context);
        SQLiteDatabase databaseWrite = databaseConnection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UMnifyContract.UMnifyColumns.News.CONTENT.toString(), news.getContent());
        values.put(UMnifyContract.UMnifyColumns.News.IMAGE.toString(), news.getImageFile());
        values.put(UMnifyContract.UMnifyColumns.News.AUTHOR.toString(), news.getAuthorId());
        values.put(UMnifyContract.UMnifyColumns.News.CREATED_DATE.toString(), news.getCreatedDate());
        values.put(UMnifyContract.UMnifyColumns.News.PUBLISHED_DATE.toString(), news.getPublishedDate());
        values.put(UMnifyContract.UMnifyColumns.News.SIGNATURE.toString(), news.getSignature());

        values.put(UMnifyContract.UMnifyColumns.News.AUTHOR_FIRSTNAME.toString(), news.getAuthorFirstname());
        values.put(UMnifyContract.UMnifyColumns.News.AUTHOR_LASTNAME.toString(), news.getAuthorLastname());
        values.put(UMnifyContract.UMnifyColumns.News.AUTHOR_IMAGE.toString(), news.getAuthorImageFile());

        String selection = UMnifyContract.UMnifyColumns.News.ID.toString() + " = ?";
        String[] selectionArgs = {news.getId() + ""};

        long count = databaseWrite.update(UMnifyContract.UMnifyColumns.News.TABLE_NAME.toString(),
                values, selection, selectionArgs);

        return count;
    }

    public static String saveImageToInternal(Bitmap image, String filename, Activity activity){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);

        File imageDirectory = new File(root.getAbsolutePath() + "/images/feed/news");
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
        File imageFile = new File(root.getAbsolutePath() + "/images/feed/news/" + filename);
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

    public static String saveAuthorImageToInternal(Bitmap image, String filename, Activity activity){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);

        File imageDirectory = new File(root.getAbsolutePath() + "/images/avatar");
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

    public static Bitmap loadAuthorImageFromInternal(String filename, Activity activity){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);
        File imageFile = new File(root.getAbsolutePath() + "/images/avatar/" + filename);
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
