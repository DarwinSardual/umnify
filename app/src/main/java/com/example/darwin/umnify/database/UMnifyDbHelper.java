package com.example.darwin.umnify.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UMnifyDbHelper extends SQLiteOpenHelper{

    private static UMnifyDbHelper databaseConnection = null;
    public static final String DATABASE_NAME = "UMnify.db";
    private static int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_PERSON =
        "CREATE TABLE " + UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString() + "(" +
                UMnifyContract.UMnifyColumns.Person.ID.toString() + " INTEGER PRIMARY KEY," +
                UMnifyContract.UMnifyColumns.Person.FIRSTNAME.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.MIDDLENAME.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.LASTNAME.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.NAME_EXT.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.BIRTHDATE.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.GENDER.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.ADDRESS.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.CONTACT.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.IMAGE.toString() + " TEXT);";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString() + "(" +
                    UMnifyContract.UMnifyColumns.User.ID.toString() + " INTEGER PRIMARY KEY," +
                    UMnifyContract.UMnifyColumns.User.TYPE.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.User.PASSWORD.toString() + " TEXT," +
                    "FOREIGN KEY("+ UMnifyContract.UMnifyColumns.User.ID.toString() + ") REFERENCES " +
                    UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString() + "("+ UMnifyContract.UMnifyColumns.Person.ID.toString() +"));";

    public static synchronized UMnifyDbHelper getInstance(Context context){

        if(databaseConnection == null){
            databaseConnection = new UMnifyDbHelper(context.getApplicationContext());
        }

        return databaseConnection;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_PERSON);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    private UMnifyDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
