package com.example.darwin.umnify.feed.blogs.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogTile;
import com.example.darwin.umnify.feed.blogs.DeleteBlogDialogFragment;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.AddBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.view_holder.BlogTileViewHolderGuest;
import com.example.darwin.umnify.wrapper.DataHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by darwin on 8/26/17.
 */

public class BlogFeedManagerAdmin<E extends BlogTileViewHolderGuest> extends BlogFeedManagerGuest<E>{

    private Bundle userData;

    public BlogFeedManagerAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Class<E> cls, int layoutId, Bundle userData){
        super(activity, swipeRefreshLayout, cls, layoutId);
        this.userData = userData;

    }

    @Override
    public void onBindViewHolder(E holder, int position) {

        final BlogTile blogTile = super.getEntryFromFeedList(position);
        if(blogTile != null){
            holder.getBlogTileImageView().setImageBitmap(blogTile.getImage());
            holder.getBlogTileHeadingView().setText(blogTile.getHeading());

            holder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("BLOG_TILE_ID", blogTile.getId());
                    intent.putExtra("BLOG_TILE_HEADING", blogTile.getHeading());
                    intent.putExtra("BLOG_TILE_IMAGE_FILE",blogTile.getImageFile());
                    intent.putExtra("BLOG_TILE_INDEX",blogTile.getIndex());
                    intent.putExtra("BLOG_TILE_AUTHOR",blogTile.getAuthor());
                    intent.putExtra("USER_ID", userData.getInt("USER_ID"));
                    intent.putExtra("USER_TYPE", userData.getInt("USER_TYPE"));

                    view.getContext().startActivity(intent);
                }
            });


            /*holder.getContainer().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if(blogTile.getAuthor() == userData.getInt("USER_ID")){
                        Bundle args = new Bundle();
                        args.putInt("BLOG_TILE_ID", blogTile.getId());
                        args.putInt("BLOG_TILE_INDEX", blogTile.getIndex());

                        DeleteBlogDialogFragment deleteBlogDialogFragment = new DeleteBlogDialogFragment();
                        deleteBlogDialogFragment.setArguments(args);
                        deleteBlogDialogFragment.show(BlogFeedManagerAdmin.super.getActivity().getFragmentManager(), "delete");
                    }
                    return true;
                }
            });*/
        }
    }

    @Override
    public void newFeedEntry(Intent data) {
        // add blog
        Uri uri = data.getData();
        Cursor cursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();

        if(uri != null){

            String mimeType = DataHelper.getMimeType(uri, super.getActivity());

            cursor = super.getActivity().getContentResolver().query(uri,
                    null, null,
                    null, null);

            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();

            String imageFile = cursor.getString(nameIndex);
            Bitmap image = null;
            int orientation = 0;
            byte[] byteArray = null;
            Bitmap rescaledImage = null;

            try{
                image = MediaStore.Images.Media.getBitmap(super.getActivity().getContentResolver(), uri);
                if(image.getWidth() < 896 && image.getHeight() < 504){
                    rescaledImage = image;
                }else{

                    if(image.getWidth() > image.getHeight()){
                        orientation = 0;
                        rescaledImage = DataHelper.resizeImageAspectRatio(image, 896, orientation);
                    }else{
                        orientation = 1;
                        rescaledImage = DataHelper.resizeImageAspectRatio(image, 504, orientation);
                    }
                }
                byteArray = DataHelper.bitmapToByteArray(rescaledImage, mimeType);

            }catch (IOException e){
                e.printStackTrace();
            }

            textData.put("heading", data.getStringExtra("ADD_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("ADD_BLOG_CONTENT"));
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");


            fileData.put(imageFile, byteArray);

        }else{
            textData.put("heading", data.getStringExtra("ADD_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("ADD_BLOG_CONTENT"));
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");
        }

        AddBlogDataActionWrapper addBlogDataActionWrapper = new AddBlogDataActionWrapper(textData,
                fileData, super.getActivity());

        WebServiceAsync asyncAddBlog = new WebServiceAsync();
        asyncAddBlog.execute(addBlogDataActionWrapper);
    }

    @Override
    public void deleteFeedEntry(BlogTile tile) {

    }

    public Bundle getUserData() {
        return userData;
    }
}
