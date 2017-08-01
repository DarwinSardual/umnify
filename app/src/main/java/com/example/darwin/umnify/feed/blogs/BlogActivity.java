package com.example.darwin.umnify.feed.blogs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.darwin.umnify.R;

public class BlogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        TextView text = (TextView) findViewById(R.id.blog_activity_heading);
        text.setText(getIntent().getExtras().getInt("id") + "");
        ;
    }
}
