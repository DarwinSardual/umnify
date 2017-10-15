package com.example.darwin.umnify.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.feed.blogs.AddBlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogCode;
import com.example.darwin.umnify.feed.news.AddNewsActivity;
import com.example.darwin.umnify.feed.news.NewsCode;

public class HomeActivityControllerAdmin extends HomeActivityControllerNormal{

    private FloatingActionButton floatingActionButton;

    public HomeActivityControllerAdmin(AppCompatActivity activity, Bundle userData){
        super(activity, userData);

    }

    @Override
    public void init(){

        super.init();
        setFloatingActionButton();
        setUpViewPagerOnPageChangeListener();
    }

    public void setFloatingActionButton(){

        floatingActionButton = (FloatingActionButton) super.getActivity().findViewById(R.id.home_fab);

    }

    public void setUpViewPagerOnPageChangeListener(){

        final FabActionAdmin addNewsAction = new FabActionAdmin(super.getActivity(),
                AddNewsActivity.class, null, NewsCode.ADD_NEWS);

        final FabActionAdmin addBlogAction = new FabActionAdmin(super.getActivity(),
                AddBlogActivity.class, null, BlogCode.ADD_BLOG);

        floatingActionButton.setOnClickListener(addNewsAction);

        super.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    floatingActionButton.setOnClickListener(addNewsAction);
                    floatingActionButton.show();
                }else if(position == 1){
                    floatingActionButton.setOnClickListener(addBlogAction);
                    floatingActionButton.show();
                }else if(position == 2){
                    floatingActionButton.hide();
                }else if(position == 3){
                    floatingActionButton.hide();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }
}
