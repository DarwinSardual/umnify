package com.example.darwin.umnify.gallery;

import android.graphics.Bitmap;

/**
 * Created by darwin on 9/2/17.
 */

public class ImageWrapper {

    private Bitmap image;
    private String imageFile;
    private int index;

    public ImageWrapper(String imageFile, Bitmap image, int index){
        this.image = image;
        this.index = index;
        this.imageFile = imageFile;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getIndex() {
        return index;
    }

    public String getImageFile() {
        return imageFile;
    }
}
