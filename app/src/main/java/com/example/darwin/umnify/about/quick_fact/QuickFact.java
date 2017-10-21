package com.example.darwin.umnify.about.quick_fact;

import android.graphics.Bitmap;

/**
 * Created by darwin on 10/1/17.
 */

public class QuickFact {

    private int id;
    private String content;
    private String imageFile;
    private Bitmap image;
    private int authorId;
    private String publishedDate;

    private String authorFirstname;
    private String authorLastname;

    public QuickFact(int id, String content, String imageFile, Bitmap image, int authorId, String publishedDate,
                  String authorFirstname, String authorLastname){

        this.id =id;
        this.content = content;
        this.imageFile = imageFile;
        this.image = image;
        this.authorId = authorId;
        this.publishedDate = publishedDate;

        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getImageFile() {
        return imageFile;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }
}
