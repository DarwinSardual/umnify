package com.example.darwin.umnify.feed.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.notifications.NotificationsCode;

public class NewsActivity extends AppCompatActivity {

    private Bundle extraData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extraData = getIntent().getExtras();


        if(extraData != null){
            if(extraData.containsKey("USER_TYPE")){
                // fetch

                int type = extraData.getInt("USER_TYPE");
                if(type == AuthenticationCodes.SUPER_ADMIN_USER){
                    setContentView(R.layout.activity_news_admin);
                    NewsActivityControllerSuperAdmin newsActivityControllerSuperAdmin =
                            new NewsActivityControllerSuperAdmin(this, extraData);
                }else if(type == AuthenticationCodes.ADMIN_USER){
                    setContentView(R.layout.activity_news_admin);
                    NewsActivityControllerAdmin newsActivityControllerAdmin =
                            new NewsActivityControllerAdmin(this, extraData);

                }else if(type == AuthenticationCodes.NORMAL_USER){
                    setContentView(R.layout.activity_news_normal);
                    NewsActivityControllerNormal newsActivityControllerNormal =
                            new NewsActivityControllerNormal(this, extraData);
                }else{
                    //guest
                }
            }else{
                //guest
            }
        }else{
            //guest
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == NewsCode.EDIT_NEWS){
            if(resultCode == RESULT_OK){

                data.putExtra("ACTION", NewsCode.EDIT_NEWS);
                setResult(RESULT_OK, data);
                this.finish();
            }
        }
    }
}
