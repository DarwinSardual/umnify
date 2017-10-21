package com.example.darwin.umnify.feed.blogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.DeleteBlogDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.DeleteNewsDataActionWrapper;

import java.util.HashMap;

/**
 * Created by darwin on 8/29/17.
 */

public class DeleteBlogDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        int id = args.getInt("BLOG_TILE_ID");
        int index = args.getInt("BLOG_TILE_INDEX");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage("Do you want to delete this blog?" + id)
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null);

        return dialogBuilder.create();
    }
}
