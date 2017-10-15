package com.example.darwin.umnify.personal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.gallery.GalleryHelper;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton toolbarBackButton;
    private Bundle userData;

    private SQLiteDatabase databaseRead;
    private UMnifyDbHelper databaseConnection;

    private TextView profileNameView;
    private TextView profileEmailView;
    private TextView profileBirthdateView;
    private TextView profileGenderView;
    private TextView profileAddressView;
    private TextView profileContactView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userData = getIntent().getExtras();

        databaseConnection = UMnifyDbHelper.getInstance(this);
        databaseRead = databaseConnection.getReadableDatabase();

        toolbarBackButton = (ImageButton) findViewById(R.id.back);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileNameView = (TextView) findViewById(R.id.name);
        profileEmailView = (TextView) findViewById(R.id.email);
        profileBirthdateView = (TextView) findViewById(R.id.birthdate);
        profileGenderView = (TextView) findViewById(R.id.gender);
        profileAddressView = (TextView) findViewById(R.id.address);
        profileContactView = (TextView) findViewById(R.id.contact);
        profileImageView = (ImageView) findViewById(R.id.image);

        Cursor cursor;
        String query = "select * from person where id = ?";
        String [] selectionArgs = {userData.getInt("USER_ID") +""};

        cursor = databaseRead.rawQuery(query, selectionArgs);

        while(cursor.moveToNext()){

            String firstname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.FIRSTNAME.toString()));
            String lastname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.LASTNAME.toString()));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.EMAIL.toString()));
            String birthdate = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.BIRTHDATE.toString()));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.GENDER.toString()));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.ADDRESS.toString()));
            String contact = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Person.CONTACT.toString()));

            profileNameView.setText(firstname +" " + lastname);
            profileEmailView.setText(email);
            profileBirthdateView.setText(DateHelper.convertDateToMDY(birthdate));
            String temp = gender.equalsIgnoreCase("1")? "Male":"Female";
            profileGenderView.setText(temp);
            profileAddressView.setText(address);
            profileContactView.setText(contact);
        }

        Bitmap image = GalleryHelper.loadImageFromInternal(userData.getString("USER_IMAGE_FILE"), this, "avatar");
        profileImageView.setImageBitmap(image);
    }
}
