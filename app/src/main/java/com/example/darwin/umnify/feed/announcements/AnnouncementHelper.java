package com.example.darwin.umnify.feed.announcements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementHelper {

    public static Announcement createAnnouncementFromJSON(JSONObject data) throws JSONException{

        int id = data.getInt("id");
        String title = data.getString("title");
        String content = data.getString("content");
        String imageFile = data.isNull("image") ? null : data.getString("image");
        String publishedDate = data.isNull("published_date")? null: data.getString("published_date");
        String createdDate = data.isNull("created_date")? null: data.getString("created_date");
        int signature = data.getInt("signature");

        int authorId = data.getInt("author_id");
        String authorFirstname = data.getString("author_firstname");
        String authorLastname = data.getString("author_lastname");

        Announcement announcement = new Announcement(id, title, content, imageFile, null, authorId, createdDate, publishedDate, signature, authorFirstname, authorLastname);

        return announcement;
    }

    public static long addToLocalDB(Announcement announcement, Context context){


        UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(context);
        SQLiteDatabase databaseWrite = databaseConnection.getWritableDatabase();
        SQLiteDatabase databaseRead = databaseConnection.getReadableDatabase();
        long id = 0;

        String[] selectionArgs = {announcement.getId() + ""};
        Cursor cursor = databaseRead.rawQuery("select id from Announcement where id =? ",
                selectionArgs);

        if(cursor.getCount() > 0){

            String[] selectionArgs2 = {announcement.getId() + "", announcement.getSignature() + ""};
            cursor = databaseRead.rawQuery("select id from Announcement where id =? and signature != ?",
                    selectionArgs2);

            if(cursor.getCount() > 0){
                updateToLocalDb(announcement, context);
            }else{
                // dont do anything
            }

        }else{

            ContentValues values = new ContentValues();
            values.put(UMnifyContract.UMnifyColumns.Announcement.ID.toString(), announcement.getId());
            values.put(UMnifyContract.UMnifyColumns.Announcement.TITLE.toString(), announcement.getTitle());
            values.put(UMnifyContract.UMnifyColumns.Announcement.CONTENT.toString(), announcement.getContent());
            values.put(UMnifyContract.UMnifyColumns.Announcement.IMAGE.toString(), announcement.getImageFile());
            values.put(UMnifyContract.UMnifyColumns.Announcement.AUTHOR.toString(), announcement.getAuthorId());
            values.put(UMnifyContract.UMnifyColumns.Announcement.CREATED_DATE.toString(), announcement.getPublishedDate());
            values.put(UMnifyContract.UMnifyColumns.Announcement.PUBLISHED_DATE.toString(), announcement.getPublishedDate());
            values.put(UMnifyContract.UMnifyColumns.Announcement.SIGNATURE.toString(), announcement.getSignature());
            values.put(UMnifyContract.UMnifyColumns.Announcement.AUTHOR_FIRSTNAME.toString(), announcement.getAuthorFirstname());
            values.put(UMnifyContract.UMnifyColumns.Announcement.AUTHOR_LASTNAME.toString(), announcement.getAuthorLastname());

            id = databaseWrite.insert(UMnifyContract.UMnifyColumns.Announcement.TABLE_NAME.toString(), null, values);
        }

        return id;
    }

    public static long updateToLocalDb(Announcement announcement, Context context){

        UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(context);
        SQLiteDatabase databaseWrite = databaseConnection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UMnifyContract.UMnifyColumns.Announcement.TITLE.toString(), announcement.getTitle());
        values.put(UMnifyContract.UMnifyColumns.Announcement.CONTENT.toString(), announcement.getContent());
        values.put(UMnifyContract.UMnifyColumns.Announcement.IMAGE.toString(), announcement.getImageFile());
        values.put(UMnifyContract.UMnifyColumns.Announcement.AUTHOR.toString(), announcement.getAuthorId());
        values.put(UMnifyContract.UMnifyColumns.Announcement.CREATED_DATE.toString(), announcement.getPublishedDate());
        values.put(UMnifyContract.UMnifyColumns.Announcement.PUBLISHED_DATE.toString(), announcement.getPublishedDate());
        values.put(UMnifyContract.UMnifyColumns.Announcement.SIGNATURE.toString(), announcement.getSignature());

        values.put(UMnifyContract.UMnifyColumns.Announcement.AUTHOR_FIRSTNAME.toString(), announcement.getAuthorFirstname());
        values.put(UMnifyContract.UMnifyColumns.Announcement.AUTHOR_LASTNAME.toString(), announcement.getAuthorLastname());

        String selection = UMnifyContract.UMnifyColumns.Announcement.ID.toString() + " = ?";
        String[] selectionArgs = {announcement.getId() + ""};

        long count = databaseWrite.update(UMnifyContract.UMnifyColumns.Announcement.TABLE_NAME.toString(),
                values, selection, selectionArgs);

        return count;
    }
}
