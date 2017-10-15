package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 8/27/17.
 */

public class BlogActivityControllerAdmin extends BlogActivityControllerGuest{

    private View buttonContainer;
    private Button deleteButton;
    private Button editButton;

    public BlogActivityControllerAdmin(Activity activity, Bundle extraData){
        super(activity, extraData);

        buttonContainer = activity.findViewById(R.id.button_container);
        deleteButton = (Button) activity.findViewById(R.id.delete);
        editButton = (Button) activity.findViewById(R.id.edit);

        if(extraData != null){

            if(extraData.getInt("USER_ID") != extraData.getInt("BLOG_AUTHOR")){
                buttonContainer.setVisibility(View.GONE);
                buttonContainer.setEnabled(false);
            }
        }

        ActionHandler actionHandler = new ActionHandler(activity, extraData);
        deleteButton.setOnClickListener(actionHandler);
        editButton.setOnClickListener(actionHandler);
    }

    public View getButtonContainer() {
        return buttonContainer;
    }

    private class ActionHandler implements View.OnClickListener{

        private Bundle extraData;
        private Activity activity;

        public ActionHandler(Activity activity, Bundle extraData){
            this.extraData = extraData;
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {

            if(view == deleteButton){
                Intent intent = new Intent();
                intent.putExtra("ACTION", BlogCode.DELETE_BLOG);
                intent.putExtra("BLOG_ID", extraData.getInt("BLOG_ID"));
                if(extraData.containsKey("NOTIFICATION_ID")){
                    intent.putExtra("NOTIFICATION_ID", extraData.getInt("NOTIFICATION_ID"));
                }

                if(extraData.containsKey("TYPE")){
                    intent.putExtra("TYPE", extraData.getString("TYPE"));
                }
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }else if(view == editButton){
                Intent intent = new Intent(activity, EditBlogActivity.class);
                //intent.putExtra("ACTION", BlogCode.EDIT_BLOG);
                intent.putExtra("BLOG_ID", extraData.getInt("BLOG_ID"));
                if(extraData.containsKey("TYPE")){
                    intent.putExtra("TYPE", extraData.getString("TYPE"));
                }
                //activity.setResult(Activity.RESULT_OK, intent);
                activity.startActivityForResult(intent, BlogCode.EDIT_BLOG);
            }

        }
    }
}
