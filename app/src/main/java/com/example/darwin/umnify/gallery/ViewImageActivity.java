package com.example.darwin.umnify.gallery;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsHelper;
import com.example.darwin.umnify.wrapper.DataHelper;

import java.io.File;

public class ViewImageActivity extends AppCompatActivity {

    private String folder;
    private String imageFile;
    private Bitmap image;
    private String rootLocation;
    private String type;

    private ImageView imageView;
    private boolean isHidden = false;
    private View buttonContainer;
    private View viewImageContainer;
    private Spinner downloadButton;
    private ImageButton infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_image);
        Bundle data = getIntent().getExtras();

        if(data != null){

            rootLocation = data.getString("ROOT_LOCATION");
            folder = data.getString("FOLDER");
            imageFile = data.getString("IMAGE_FILE");


            buttonContainer = findViewById(R.id.buttons_container);
            viewImageContainer = findViewById(R.id.container);
            downloadButton = (Spinner) findViewById(R.id.download_spinner);
            infoButton = (ImageButton) findViewById(R.id.info_button);
            imageView = (ImageView) findViewById(R.id.image);

            downloadButton.setOnItemSelectedListener(new DownloadListener());

            viewImageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isHidden){
                        buttonContainer.setVisibility(View.VISIBLE);
                        isHidden = false;
                    }else{
                        buttonContainer.setVisibility(View.INVISIBLE);
                        isHidden= true;
                    }
                }
            });
        }

        image = GalleryHelper.loadImageFromInternal(imageFile, this, folder);
        if(image != null){
            imageView.setImageBitmap(image);
        }else{

            DataActionWrapper wrapper;

            /*if(type.equalsIgnoreCase("news")){

            }else if(type.equalsIgnoreCase("blog")){

            }else if(type.equalsIgnoreCase("announcement")){

            }*/
        }

    }

    private class DownloadListener implements AdapterView.OnItemSelectedListener{

        private String url;
        private DownloadManager.Request request;
        private DownloadManager manager;
        private boolean isFirst = true;

        public DownloadListener(){


            manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if(isFirst){
                isFirst = false;
                return;
            }

            if(i == 0){
                    url = rootLocation + "/preview/" + imageFile;
                Log.e("clicked", url);
            }else if(i == 1){
                url = rootLocation + "/" + imageFile;
                Log.e("clicked", url);
            }

            request = new DownloadManager.Request(Uri.parse(url));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imageFile);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            manager.enqueue(request);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}
