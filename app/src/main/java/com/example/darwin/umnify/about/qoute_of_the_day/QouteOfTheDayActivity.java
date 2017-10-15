package com.example.darwin.umnify.about.qoute_of_the_day;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class QouteOfTheDayActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private TextView contentView;
    private QouteOfTheDay qouteOfTheDay;
    private Bundle userData;

    private ImageButton backButton;
    public final static int ADD_QOUTE_OF_THE_DAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qoute_of_the_day);

        userData = getIntent().getExtras();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        contentView = (TextView) findViewById(R.id.content);

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("limit", "1");
        textDataOutput.put("order", "desc");

        WebServiceAsync async = new WebServiceAsync();
        DataActionWrapper fetchData = new DataActionWrapper(textDataOutput, this, AuthenticationAddress.FETCH_QOUTEOFTHEDAY, new ProcessPostFetchData());
        async.execute(fetchData);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QouteOfTheDayActivity.this, AddQouteOfTheDayActivity.class);
                startActivityForResult(intent, ADD_QOUTE_OF_THE_DAY);
            }
        });

        backButton= (ImageButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_QOUTE_OF_THE_DAY){
            if(resultCode == RESULT_OK){
                add(data);
            }
        }
    }

    private void add(Intent data){
        WebServiceAsync async = new WebServiceAsync();
        HashMap<String, String> textDataOutput = new HashMap<>();

        textDataOutput.put("author", userData.getInt("USER_ID") + "");
        textDataOutput.put("content", data.getStringExtra("ADD_QOUTE_CONTENT"));

        DataActionWrapper addQoute = new DataActionWrapper(textDataOutput, this, AuthenticationAddress.ADD_QOUTEOFTHEDAY, new ProcessPostAddData());
        async.execute(addQoute);
    }

    private class ProcessPostAddData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){
                Log.e("Message", jsonResponse);
            }else{
                Log.e("Message","No internet connection");
            }
        }
    }

    private class ProcessPostFetchData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            if(jsonResponse != null){

                try{
                    JSONObject response = new JSONObject(jsonResponse);
                    JSONArray dataList = new JSONArray(response.getString("data"));

                    for(int counter = 0; counter < dataList.length(); counter++){
                        JSONObject data = new JSONObject(dataList.getString(counter));
                        qouteOfTheDay = new QouteOfTheDay(data.getInt("id"), data.getString("content"),data.getInt("author_id"),
                                data.getString("published_date"), data.getString("author_firstname"), data.getString("author_lastname"));
                    }

                    contentView.setText(qouteOfTheDay.getContent());
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
