package com.example.darwin.umnify.feed.news;

import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class News {

    private int id;
    private String content;
    private Drawable image;
    private int author;
    private String publishedDate;
    private int signature;

    private JSONObject data;

    public News(String content,Drawable image){

        this.content = content;
        this.image = image;
    }

    public News(int id, String content, Drawable image, int author, String publishedDate, int signature){

        this.id = id;
        this.content = content;
        this.image = null; //replace this
        this.author = author;
        this.publishedDate = publishedDate;
        this.signature = signature;

    }

    public News(JSONObject data) throws JSONException{
        this.data = data;

        setFieldsFromJSON();
    }

    private void setFieldsFromJSON() throws JSONException{

        id = data.getInt("id");
        content = data.getString("content");
        image = null;
        author = data.getInt("author");
        publishedDate = data.getString("published_date");
        signature = data.getInt("signature");
    }

    public int getId(){
        return id;
    }

    public String getContent(){
        return content;
    }

    public Drawable getImage() {
         return image;
    }

    public int getAuthor() {
        return author;
    }

    public int getSignature() {
        return signature;
    }

    public JSONObject getData() {
        return data;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}