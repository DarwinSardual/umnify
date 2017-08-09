package com.example.darwin.umnify.start;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.scratch.SampleConnActivity;

public class StartActivity extends AppCompatActivity {

    private UMnifyDbHelper databaseConnection;
    private SQLiteDatabase databaseConnectionRead;
    private SQLiteDatabase databaseConnectionWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        databaseConnection = UMnifyDbHelper.getInstance(this);
        databaseConnectionRead = databaseConnection.getReadableDatabase();
        databaseConnectionWrite = databaseConnection.getWritableDatabase();

        //SQLiteDatabase dbWrite = databaseConnection.getWritableDatabase();


        /*ContentValues values = new ContentValues();
        values.put(UMnifyContract.UMnifyColumns.User.ID.toString(), 383221);
        values.put(UMnifyContract.UMnifyColumns.User.TYPE.toString(), 1);
        values.put(UMnifyContract.UMnifyColumns.User.PASSWORD.toString(), "darwinsardual");

        long id = dbWrite.insert(UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(), null, values);
        Log.e("ID", id +"");*/

        //Log.e("Calling", "User checker");
        //startService(new Intent(this, UserChecker.class));
        //startActivity(new Intent(this, SampleConnActivity.class));

        //finish();



    }

    // check if a user exist in the database

    private void handleUserCheck(){

        String[] projection = {

                UMnifyContract.UMnifyColumns.User.ID.toString(),
                UMnifyContract.UMnifyColumns.User.TYPE.toString(),
                UMnifyContract.UMnifyColumns.User.PASSWORD.toString()
        };

        Cursor cursor = databaseConnectionRead.query(
                UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(),
                projection,
                null,
                null,
                null,
                null,
                null);

        if(cursor.getCount() == 1){

            //check if the stored credentials is valid

        }else{

        }
    }
}
