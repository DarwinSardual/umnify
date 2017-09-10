package com.example.darwin.umnify.feed.blogs.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogFeedManager;
import com.example.darwin.umnify.feed.blogs.BlogTile;
import com.example.darwin.umnify.feed.blogs.DeleteBlogDialogFragment;
import com.example.darwin.umnify.feed.blogs.view_holder.BlogTileViewHolderGuest;

/**
 * Created by darwin on 8/29/17.
 */

public class BlogFeedManagerSuperAdmin<E extends BlogTileViewHolderGuest>extends BlogFeedManagerAdmin<E> {

    public BlogFeedManagerSuperAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                     Class<E> cls, int layoutId, Bundle userData) {
        super(activity, swipeRefreshLayout, cls, layoutId, userData);


    }

    @Override
    public void onBindViewHolder(E holder, int position) {

        final BlogTile blogTile = super.getEntryFromFeedList(position);
        if (blogTile != null) {
            holder.getBlogTileImageView().setImageBitmap(blogTile.getImage());
            holder.getBlogTileHeadingView().setText(blogTile.getHeading());

            holder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("BLOG_TILE_ID", blogTile.getId());
                    intent.putExtra("BLOG_TILE_HEADING", blogTile.getHeading());
                    intent.putExtra("BLOG_TILE_IMAGE_FILE", blogTile.getImageFile());
                    intent.putExtra("BLOG_TILE_INDEX", blogTile.getIndex());
                    intent.putExtra("BLOG_TILE_AUTHOR", blogTile.getAuthor());
                    intent.putExtra("USER_ID", BlogFeedManagerSuperAdmin.super.getUserData().getInt("USER_ID"));
                    intent.putExtra("USER_TYPE", BlogFeedManagerSuperAdmin.super.getUserData().getInt("USER_TYPE"));

                    view.getContext().startActivity(intent);
                }
            });

            /*holder.getContainer().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Bundle args = new Bundle();
                    args.putInt("BLOG_TILE_ID", blogTile.getId());

                    DeleteBlogDialogFragment deleteBlogDialogFragment = new DeleteBlogDialogFragment();
                    deleteBlogDialogFragment.setArguments(args);
                    deleteBlogDialogFragment.show(BlogFeedManagerSuperAdmin.super.getActivity().getFragmentManager(), "delete");
                    return true;
                }
            });*/
        }
    }
}
