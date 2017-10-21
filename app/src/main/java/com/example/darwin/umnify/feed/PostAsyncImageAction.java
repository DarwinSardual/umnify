package com.example.darwin.umnify.feed;

import android.graphics.Bitmap;

/**
 * Created by darwin on 9/30/17.
 */

public interface PostAsyncImageAction {

    public String getImageFile();
    public void processResult(Bitmap image);
}
