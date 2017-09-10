package com.example.darwin.umnify.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by darwin on 9/3/17.
 */

public class GalleryHelper {

    public static ImageWrapper createImageWrapperFromJSON(JSONObject data, int index) throws JSONException{

        String imageFile = data.getString("image");
        ImageWrapper wrapper = new ImageWrapper(imageFile, null, index);

        return wrapper;
    }

    public static String saveImageToInternal(Bitmap image, String filename, Activity activity, String folder){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);

        File imageDirectory = new File(root.getAbsolutePath() + "/images/gallery/" + folder);
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

    public static Bitmap loadImageFromInternal(String filename, Activity activity, String folder){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);
        File imageFile = new File(root.getAbsolutePath() + "/images/gallery/" + folder + "/" + filename);
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
