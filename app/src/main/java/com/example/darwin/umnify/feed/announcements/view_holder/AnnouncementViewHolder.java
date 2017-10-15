package com.example.darwin.umnify.feed.announcements.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView;
    private TextView contentView;
    private ImageView imageView;

    public AnnouncementViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
        super(inflater.inflate(layoutId, parent, false));

        titleView = (TextView) itemView.findViewById(R.id.title);
        contentView = (TextView) itemView.findViewById(R.id.content);
        imageView = (ImageView) itemView.findViewById(R.id.image);

    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getContentView() {
        return contentView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
