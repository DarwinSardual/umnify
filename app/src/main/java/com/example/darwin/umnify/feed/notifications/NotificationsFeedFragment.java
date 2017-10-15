package com.example.darwin.umnify.feed.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.notifications.feed_manager.NotificationFeedManager;
import com.example.darwin.umnify.feed.notifications.feed_manager.NotificationFeedManagerAdmin;
import com.example.darwin.umnify.feed.notifications.view_holder.NotificationViewHolder;

public class NotificationsFeedFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Bundle userData;
    private FeedManager manager;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        userData = super.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.feed_view, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresher_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        if(userData != null){

            int type = userData.getInt("USER_TYPE");

            if(type == AuthenticationCodes.NORMAL_USER || type == AuthenticationCodes.GUEST_USER){
                manager = new NotificationFeedManager<NotificationViewHolder>(this.getActivity(), swipeRefreshLayout,
                        NotificationViewHolder.class, userData, R.layout.feed_notifications);
            }else if(type == AuthenticationCodes.ADMIN_USER || type == AuthenticationCodes.SUPER_ADMIN_USER){
                manager = new NotificationFeedManagerAdmin(this.getActivity(), swipeRefreshLayout,
                        NotificationViewHolder.class, userData, R.layout.feed_notifications);
            }

        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(dy > 0){
                    if(!recyclerView.canScrollVertically(1)){
                        manager.updateFeed(1);
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                manager.updateFeed(-1);

            }
        });

        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void deleteNotification(Intent data){

        String key = Integer.toString(data.getIntExtra("NOTIFICATION_ID", -1));
        manager.deleteFeedEntry(key);

    }

    public void updateNewsBlog(Intent data){

        manager.updateFeedContent(data);
    }
}
