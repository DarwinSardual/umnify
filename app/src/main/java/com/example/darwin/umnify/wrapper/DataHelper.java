package com.example.darwin.umnify.wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.net.Uri;
import android.util.Log;

import com.example.darwin.umnify.connection.WebServiceConnection;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DataHelper {

    public static void writeTextUpload(HashMap<String, String> data, WebServiceConnection connection){

        for(String key : data.keySet()){

            connection.addTextUpload(key, data.get(key));
        }
    }

    public static void writeFileUpload(String name, HashMap<String, byte[]> data, WebServiceConnection connection){
        for(String key : data.keySet()){

            connection.addFileUpload(name, key, data.get(key));
        }
    }

    public static String parseStringFromStream(InputStream inputStream){

        if(inputStream == null)
            return null;

        try{

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String buffer = "";

            while(true){

                String temp = reader.readLine();
                if(temp != null)
                    buffer += temp;
                else break;
            }

            return buffer;

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }


    }

    public static Bitmap resizeImageAspectRatio(Bitmap image, int maxWidthHeight, int orientation){

        float originalWidth = image.getWidth();
        float originalHeight = image.getHeight();

        Bitmap scaledImage = null;

        if(orientation == 0){ //landscape
            float ratio = originalHeight / originalWidth;
            int newWidthHeight = (int) (ratio * maxWidthHeight);
            scaledImage = Bitmap.createScaledBitmap(image, maxWidthHeight, newWidthHeight, true);
        }else if(orientation == 1){ // portrait
            float ratio = originalWidth / originalHeight;
            int newWidthHeight = (int) (ratio * maxWidthHeight);
            scaledImage = Bitmap.createScaledBitmap(image, newWidthHeight, maxWidthHeight, true);
        }

        return scaledImage;
    }

    public static String getMimeType(Uri uri, Activity activity){

        return activity.getContentResolver().getType(uri);
    }

    public static byte[] bitmapToByteArray(Bitmap image, String imageMimeType){
        byte[] byteArray = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if(imageMimeType.equalsIgnoreCase("image/png")){
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }else if(imageMimeType.equalsIgnoreCase("image/jpeg")){
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
        }else{
            Log.e("Error", "DataHelper.bitmapToByteArray - Unknown mime type");
        }

        return byteArray;
    }
}
