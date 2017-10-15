package com.example.darwin.umnify.home.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.home.HomeHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

public class FetchUserImageDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private WebServiceConnection connection;

    private InputStream inputStream;

    private ImageView userImageView;
    private Activity activity;
    Bitmap image;

    public FetchUserImageDataActionWrapper(HashMap<String, String> textDataOutput,
                                           Activity activity,
                                           ImageView userImageView){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.userImageView = userImageView;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.AVATAR_IMAGE_FOLDER + "/" + textDataOutput.get("image_file"),
                activity, true, true, false);

        inputStream = connection.getInputStream();
        image = BitmapFactory.decodeStream(inputStream);
    }

    @Override
    public void processResult() {


        if(image != null){
            userImageView.setImageBitmap(image);
            GalleryHelper.saveImageToInternal(image, textDataOutput.get("image_file"), activity, "avatar");

        }
    }
}
