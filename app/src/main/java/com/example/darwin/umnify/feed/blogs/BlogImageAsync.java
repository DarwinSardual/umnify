package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

//import com.example.darwin.umnify.connection.RemoteDbConn;
import java.io.IOException;
import java.io.InputStream;

public class BlogImageAsync{

}

/*public class BlogImageAsync extends RemoteDbConn <BlogTileWrapper, Void, BlogTileWrapper>{

    public BlogImageAsync(String urlAddress, Activity activity){
        super(urlAddress, activity);
    }

    @Override
    protected BlogTileWrapper doInBackground(BlogTileWrapper... wrapper) {

        try{

            super.setUpConnection();
            super.getUrlConnection().connect();

            if(wrapper[0].tile.getImageFile() != null){

                InputStream imageStream = getUrlConnection().getInputStream();
                Bitmap image = BitmapFactory.decodeStream(imageStream);
                wrapper[0].tile.setImage(image);
            }else{
                Log.e("File", "null");
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return wrapper[0];
    }

    @Override
    protected void onPostExecute(BlogTileWrapper wrapper) {
        wrapper.manager.notifyItemChanged(wrapper.tile.getIndex());
    }
}*/
