package com.example.darwin.umnify.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by darwin on 9/11/17.
 */

public class HomeHelper {

    public static String saveImageToInternal(Bitmap image, String filename, Activity activity){

        File root = activity.getDir("umnify", Context.MODE_PRIVATE);

        File imageDirectory = new File(root.getAbsolutePath() + "/images/avatar");
        File myPath = new File(imageDirectory, filename);
        FileOutputStream outputStream = null;


        try{
            outputStream = new FileOutputStream(myPath);
            if(outputStream == null)
                return null;

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

    public static Bitmap loadImageFromInternal(String filename, Activity activity){

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
