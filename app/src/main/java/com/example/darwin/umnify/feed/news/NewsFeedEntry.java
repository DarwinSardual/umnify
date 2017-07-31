package com.example.darwin.umnify.feed.news;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedEntry {

    private String text;
    private List<Drawable> imageList;

    public NewsFeedEntry(String text, List<Drawable> imageList){

        this.text = text;
        this.imageList = imageList;
    }

    public NewsFeedEntry(String text, Drawable image){

        this.text = text;
        imageList = new ArrayList<>();
        imageList.add(image);
    }

    // dummy constructor, remove this after testing
    public NewsFeedEntry(String text, String str){

        this.text = text;

    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void addImage(Drawable image){
        imageList.add(image);
    }

    public List<Drawable> getImageList(){
        return imageList;
    }

    public Drawable getImageAt(int position){
        if(position >= imageList.size())
            return null;

        return imageList.get(position);
    }
}