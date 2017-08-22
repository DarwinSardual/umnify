package com.example.darwin.umnify.feed.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.Window;
import com.example.darwin.umnify.R;

public class NewsCommentActivity extends AppCompatActivity {

    private Bundle extraData;
    private NewsCommentManager manager;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_comment);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        extraData = getIntent().getExtras();

        manager = new NewsCommentManager(this, extraData);

        recyclerView = (RecyclerView) findViewById(R.id.activity_news_comment_recycler_view);
        recyclerView.setAdapter(manager);
    }
}
