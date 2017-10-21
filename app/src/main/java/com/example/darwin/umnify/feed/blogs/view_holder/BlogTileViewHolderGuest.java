package com.example.darwin.umnify.feed.blogs.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 8/26/17.
 */

public class BlogTileViewHolderGuest extends RecyclerView.ViewHolder {

    private ImageView blogTileImageView;
    private TextView blogTileHeadingView;
    private RelativeLayout container;

    public BlogTileViewHolderGuest(LayoutInflater inflater, ViewGroup parent, int layoutId){
        super(inflater.inflate(layoutId, parent, false));

        blogTileImageView = (ImageView) itemView.findViewById(R.id.image);
        blogTileHeadingView = (TextView) itemView.findViewById(R.id.heading);
        container = (RelativeLayout) itemView.findViewById(R.id.container);
    }

    public ImageView getBlogTileImageView() {
        return blogTileImageView;
    }

    public TextView getBlogTileHeadingView() {
        return blogTileHeadingView;
    }

    public RelativeLayout getContainer() {
        return container;
    }
}
