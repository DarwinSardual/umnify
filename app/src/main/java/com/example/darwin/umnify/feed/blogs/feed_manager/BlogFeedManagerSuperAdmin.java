package com.example.darwin.umnify.feed.blogs.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.darwin.umnify.feed.blogs.Blog;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogCode;
import com.example.darwin.umnify.feed.blogs.view_holder.BlogTileViewHolderGuest;

/**
 * Created by darwin on 8/29/17.
 */

public class BlogFeedManagerSuperAdmin<E extends BlogTileViewHolderGuest>extends BlogFeedManagerAdmin<E> {

    private Bundle userData;

    public BlogFeedManagerSuperAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                     Class<E> cls, int layoutId, Bundle userData) {
        super(activity, swipeRefreshLayout, cls, layoutId, userData);
        this.userData = userData;

    }

    @Override
    public void onBindViewHolder(E holder, int position) {

        if(!(position < super.getIndex().size())) return;

        String key = getIndex().get(position);
        final Blog blog = super.getEntryFromFeedList(key);
        if(blog != null){
            holder.getBlogTileImageView().setImageBitmap(blog.getImage());
            holder.getBlogTileHeadingView().setText(blog.getHeading());

            holder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("STATUS", 1);
                    intent.putExtra("BLOG_ID", blog.getId());
                    intent.putExtra("BLOG_HEADING", blog.getHeading());
                    intent.putExtra("BLOG_CONTENT", blog.getContent());
                    intent.putExtra("BLOG_IMAGE",blog.getImageFile());
                    intent.putExtra("BLOG_AUTHOR",blog.getAuthor());
                    intent.putExtra("BLOG_PUBLISHED_DATE",blog.getPublishedDate());
                    intent.putExtra("BLOG_SIGNATURE",blog.getSignature());

                    intent.putExtra("BLOG_AUTHOR_FIRSTNAME",blog.getAuthorFirstname());
                    intent.putExtra("BLOG_AUTHOR_LASTNAME",blog.getAuthorLastname());
                    intent.putExtra("BLOG_AUTHOR_IMAGE",blog.getAuthorImage());

                    intent.putExtra("BLOG_INDEX", blog.getIndex());
                    intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                    intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));

                    BlogFeedManagerSuperAdmin.super.getActivity().startActivityForResult(intent, BlogCode.VIEW_BLOG);
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
