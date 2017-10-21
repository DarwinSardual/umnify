package com.example.darwin.umnify.feed.news;

import android.graphics.Bitmap;

public class News {

    private int id;
    private String content;
    private String imageFile;
    private Bitmap image;
    private String publishedDate;
    private int signature;

    private int authorId;
    private String authorFirstname;
    private String authorLastname;
    private String authorImageFile;
    private int stars;
    private boolean isStarred;

    private Bitmap authorImage;

    private int index;


    public News(int id, String content, String imageFile, int authorId, String publishedDate, int signature, int stars, boolean isStarred,
                int index, String authorFirstname, String authorLastname,  String authorImageFile){

        this.id = id;
        this.content = content;
        this.imageFile = imageFile;
        this.authorId = authorId;
        this.publishedDate = publishedDate;
        this.signature = signature;
        this.stars = stars;
        this.isStarred = isStarred;

        this.index = index;
        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
        this.authorImageFile = authorImageFile;

    }

    public int getStars() {
        return stars;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setAuthorImage(Bitmap authorImage) {
        this.authorImage = authorImage;
    }

    public void setIsStarred(boolean b){
        isStarred = b;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getIndex() {
        return index;
    }

    public int getId(){
        return id;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getContent(){
        return content;
    }

    public Bitmap getImage() {
         return image;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getSignature() {
        return signature;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthorFirstname(){ return authorFirstname;}

    public String getAuthorLastname() {
        return authorLastname;
    }

    public String getAuthorImageFile() {
        return authorImageFile;
    }

    public Bitmap getAuthorImage() {
        return authorImage;
    }
}