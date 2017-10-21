package com.example.darwin.umnify.feed.announcements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.announcements.feed_manager.AnnouncementFeedManager;
import com.example.darwin.umnify.feed.announcements.feed_manager.AnnouncementFeedManagerSuperAdmin;
import com.example.darwin.umnify.feed.announcements.view_holder.AnnouncementViewHolder;
import com.example.darwin.umnify.feed.announcements.view_holder.AnnouncementViewHolderSuperAdmin;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementFeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeedManager manager;
    private Bundle userData;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userData = super.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.feed_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresher_layout);

        if(userData == null){
            manager = new AnnouncementFeedManager<AnnouncementViewHolder>(this.getActivity(), swipeRefreshLayout,
                    AnnouncementViewHolder.class, R.layout.feed_announcement);
        }else{

            int type = userData.getInt("USER_TYPE");

            if(type == AuthenticationCodes.SUPER_ADMIN_USER){
                manager = new AnnouncementFeedManagerSuperAdmin<AnnouncementViewHolderSuperAdmin>(this.getActivity(), swipeRefreshLayout,
                        AnnouncementViewHolderSuperAdmin.class, R.layout.feed_announcement_admin, userData);
            }else{
                manager = new AnnouncementFeedManager<AnnouncementViewHolder>(this.getActivity(), swipeRefreshLayout,
                        AnnouncementViewHolder.class, R.layout.feed_announcement);
            }
        }

        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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

        return view;
    }

    public void addAnnouncement(Intent data){
        manager.newFeedEntry(data);
    }

    public void updateAnnouncement(Intent data){
        manager.updateFeedContent(data);
    }
}
