package com.example.darwin.umnify.feed.announcements.view_holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.SpinnerExtended;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementViewHolderSuperAdmin extends AnnouncementViewHolder {

    private SpinnerExtended announcementOptionView;

    public AnnouncementViewHolderSuperAdmin(LayoutInflater inflater, ViewGroup parent, int layoutId) {
        super(inflater, parent, layoutId);


        announcementOptionView = (SpinnerExtended) itemView.findViewById(R.id.spinner);
    }

    public SpinnerExtended getAnnouncementOptionView() {
        return announcementOptionView;
    }
}
