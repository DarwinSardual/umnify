package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsFeedManager;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class StarredNewsDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private News news;
    private View source;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private NewsFeedManager manager;
    private Activity activity;

    public StarredNewsDataActionWrapper(HashMap<String, String> textDataOuput, News news,
                                        View source, Activity activity, NewsFeedManager manager){

        this.textDataOutput = textDataOuput;
        this.news = news;
        this.source = source;
        this.manager = manager;
        this.activity = activity;

    }

    public void setSource(View source) {
        this.source = source;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.STAR_NEWS, activity,
                true, true, true);

        connection.addAuthentication();
        if(news.isStarred()){
            textDataOutput.put("action", "remove");
            textDataOutput.put("news", news.getId() + "");
            DataHelper.writeTextUpload(textDataOutput, connection);

        }else{
            textDataOutput.put("action", "add");
            textDataOutput.put("news", news.getId() + "");
            DataHelper.writeTextUpload(textDataOutput, connection);
        }

        inputStream = connection.getInputStream();

    }

    @Override
    public void processResult() {

        try{
            String response = DataHelper.parseStringFromStream(inputStream);

            if(response != null){

                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");
                int transaction = json.getInt("transaction");

                if(code == AuthenticationCodes.AUTHENTICATED && transaction == AuthenticationCodes.TRANSACTION_SUCCESS){
                    JSONObject data = new JSONObject(json.getString("data"));

                    ImageButton button = (ImageButton) source;

                    if(data.getString("action").equalsIgnoreCase("add")){
                        news.setIsStarred(true);
                        news.setStars(data.getInt("count"));
                        button.setImageDrawable(manager.filledStar);
                        if(news.isStarred()){
                            TextView view = (TextView) source.getTag();
                            view.setText("You and " + news.getStars() + " have starred this");
                        }

                    }else if(data.getString("action").equalsIgnoreCase("remove")){
                        news.setIsStarred(false);
                        button.setImageDrawable(manager.emptyStar);

                        if(!news.isStarred()){
                            TextView view = (TextView) source.getTag();
                            view.setText(news.getStars() + " have starred this");
                        }
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
