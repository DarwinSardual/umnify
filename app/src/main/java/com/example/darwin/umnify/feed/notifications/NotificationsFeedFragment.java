package com.example.darwin.umnify.feed.notifications;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;

public class NotificationsFeedFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ViewGroup rootLayout = (ViewGroup) inflater.inflate(R.layout.feed_notifications, container, false);

        return rootLayout;
    }
}
