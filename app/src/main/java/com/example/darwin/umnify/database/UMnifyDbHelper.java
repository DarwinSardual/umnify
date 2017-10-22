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
                UMnifyContract.UMnifyColumns.Person.IMAGE.toString() + " TEXT," +
                UMnifyContract.UMnifyColumns.Person.EMAIL.toString() + " TEXT);";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString() + "(" +
                    UMnifyContract.UMnifyColumns.User.ID.toString() + " INTEGER PRIMARY KEY," +
                    UMnifyContract.UMnifyColumns.User.TYPE.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.User.PASSWORD.toString() + " TEXT," +
                    "FOREIGN KEY("+ UMnifyContract.UMnifyColumns.User.ID.toString() + ") REFERENCES " +
                    UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString() + "("+ UMnifyContract.UMnifyColumns.Person.ID.toString() +"));";

    private static final String CREATE_TABLE_ACADEME_PERSON =
            "CREATE TABLE " + UMnifyContract.UMnifyColumns.AcademePerson.TABLE_NAME.toString() + "(" +
                    UMnifyContract.UMnifyColumns.AcademePerson.ID.toString() + " INTEGER PRIMARY KEY," +
                    UMnifyContract.UMnifyColumns.AcademePerson.COURSE.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.AcademePerson.YEAR.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.AcademePerson.TYPE.toString() + " INTEGER," +
                    "FOREIGN KEY("+ UMnifyContract.UMnifyColumns.AcademePerson.ID.toString() + ") REFERENCES " +
                    UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString() + "("+ UMnifyContract.UMnifyColumns.Person.ID.toString() +"));";

    private static final String CREATE_TABLE_NEWS =
            "CREATE TABLE " + UMnifyContract.UMnifyColumns.News.TABLE_NAME.toString() + "(" +
                    UMnifyContract.UMnifyColumns.News.ID.toString() + " INTEGER PRIMARY KEY," +
                    UMnifyContract.UMnifyColumns.News.CONTENT.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.News.IMAGE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.News.AUTHOR.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.News.PUBLISHED_DATE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.News.SIGNATURE.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.News.AUTHOR_FIRSTNAME.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.News.AUTHOR_LASTNAME.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.News.AUTHOR_IMAGE.toString() + " TEXT);";

    private static final String CREATE_TABLE_BLOG =
            "CREATE TABLE " + UMnifyContract.UMnifyColumns.Blog.TABLE_NAME.toString() + "(" +
                    UMnifyContract.UMnifyColumns.Blog.ID.toString() + " INTEGER PRIMARY KEY," +
                    UMnifyContract.UMnifyColumns.Blog.HEADING.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Blog.CONTENT.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Blog.IMAGE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Blog.AUTHOR.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.Blog.PUBLISHED_DATE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Blog.SIGNATURE.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.Blog.AUTHOR_FIRSTNAME.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Blog.AUTHOR_LASTNAME.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Blog.AUTHOR_IMAGE.toString() + " TEXT);";

    private static final String CREATE_TABLE_ANNOUNCEMENT =
            "CREATE TABLE " + UMnifyContract.UMnifyColumns.Announcement.TABLE_NAME.toString() + "(" +
                    UMnifyContract.UMnifyColumns.Announcement.ID.toString() + " INTEGER PRIMARY KEY," +
                    UMnifyContract.UMnifyColumns.Announcement.TITLE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Announcement.CONTENT.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Announcement.IMAGE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Announcement.AUTHOR.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.Announcement.CREATED_DATE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Announcement.PUBLISHED_DATE.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Announcement.SIGNATURE.toString() + " INTEGER," +
                    UMnifyContract.UMnifyColumns.Announcement.AUTHOR_FIRSTNAME.toString() + " TEXT," +
                    UMnifyContract.UMnifyColumns.Announcement.AUTHOR_LASTNAME.toString() + " TEXT);";

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
        db.execSQL(CREATE_TABLE_ACADEME_PERSON);
        db.execSQL(CREATE_TABLE_NEWS);
        db.execSQL(CREATE_TABLE_BLOG);
        db.execSQL(CREATE_TABLE_ANNOUNCEMENT);
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
