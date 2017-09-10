package com.example.darwin.umnify.gallery;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.feed.news.NewsHelper;
import com.example.darwin.umnify.wrapper.DataHelper;

public class ViewImageActivity extends AppCompatActivity {

    private String folder;
    private String imageFile;
    private Bitmap image;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_image);

        Bundle data = getIntent().getExtras();
        folder = data.getString("FOLDER");
        imageFile = data.getString("IMAGE_FILE");

        imageView = (ImageView) findViewById(R.id.view_image);

        image = GalleryHelper.loadImageFromInternal(imageFile, this, folder);
        imageView.setImageBitmap(image);

    }
}
