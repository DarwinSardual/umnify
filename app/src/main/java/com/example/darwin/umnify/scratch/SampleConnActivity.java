package com.example.darwin.umnify.scratch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.darwin.umnify.R;

import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SampleConnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_conn);

        String str = getIntent().getStringExtra("USER_ID");
        str = str + " " + getIntent().getStringExtra("USER_TYPE");

        //sample();

        TextView sample = (TextView) findViewById(R.id.sample_text);
        sample.setText(str);




    }

    /*public void sample(){

            new SampleTask().execute("http://localhost/~darwin/UMnifyMobileScripts/sample.php");

    }

    class SampleTask extends AsyncTask<String, Void, String> {

        private TextView text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            text = (TextView) findViewById(R.id.sample_text);


        }

        protected String doInBackground(String... urls) {
            try {

                URL url = new URL("http://192.168.0.107/~darwin/UMnifyMobileScripts/sample.php");
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();


                return urlConn.getResponseCode() + "";
            } catch (Exception e) {


                return e.toString();
            }


        }

        protected void onPostExecute(String str) {

            super.onPostExecute(str);
            // TODO: check this.exception
            // TODO: do something with the feed

            text.setText(str);
        }
    }*/
}
