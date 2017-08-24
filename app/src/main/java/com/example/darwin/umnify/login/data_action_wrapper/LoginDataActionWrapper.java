package com.example.darwin.umnify.login.data_action_wrapper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.home.HomeActivity;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class LoginDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private WebServiceConnection connection;
    private Activity activity;

    private InputStream inputStream;
    private View source;

    public LoginDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity, View source) {

        this.textDataOutput = textDataOutput;
        this.source = source;
        this.activity = activity;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.AUTHENTICATE_LOGIN,
                activity, true, true, true);

        connection.addAuthentication();
        DataHelper.writeTextUpload(textDataOutput, connection);
        connection.flushOutputStream();

        inputStream = connection.getInputStream();


    }

    @Override
    public void processResult() {

        try{

            String response = DataHelper.parseStringFromStream(inputStream);


            JSONObject json = new JSONObject(response);
            int code = json.getInt("code");

            if (code == AuthenticationCodes.USER_AUTHENTICATED) {

                JSONObject user = new JSONObject(json.getString("user"));
                JSONObject person = new JSONObject(json.getString("person"));

                //store database credentials here
                UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(activity);
                SQLiteDatabase databaseConnectionWrite = databaseConnection.getWritableDatabase();

                // insert person data here
                ContentValues values = new ContentValues();
                values.put(UMnifyContract.UMnifyColumns.Person.ID.toString(), user.getInt("id"));
                values.put(UMnifyContract.UMnifyColumns.Person.FIRSTNAME.toString(), person.getString("firstname"));
                values.put(UMnifyContract.UMnifyColumns.Person.MIDDLENAME.toString(), person.getString("middlename"));
                values.put(UMnifyContract.UMnifyColumns.Person.LASTNAME.toString(), person.getString("lastname"));
                values.put(UMnifyContract.UMnifyColumns.Person.NAME_EXT.toString(), person.getString("name_ext"));
                values.put(UMnifyContract.UMnifyColumns.Person.BIRTHDATE.toString(), person.getString("birthdate"));
                values.put(UMnifyContract.UMnifyColumns.Person.GENDER.toString(), person.getString("gender"));
                values.put(UMnifyContract.UMnifyColumns.Person.ADDRESS.toString(), person.getString("address"));
                values.put(UMnifyContract.UMnifyColumns.Person.CONTACT.toString(), person.getString("contact"));
                values.put(UMnifyContract.UMnifyColumns.Person.IMAGE.toString(), person.getString("image"));
                values.put(UMnifyContract.UMnifyColumns.Person.EMAIL.toString(), person.getString("email"));

                long person_id = databaseConnectionWrite.insert(
                        UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString(),
                        null,
                        values
                );

                //insert user data
                values = new ContentValues();
                values.put(UMnifyContract.UMnifyColumns.User.ID.toString(), user.getInt("id"));
                values.put(UMnifyContract.UMnifyColumns.User.TYPE.toString(), user.getInt("type"));
                values.put(UMnifyContract.UMnifyColumns.User.PASSWORD.toString(), user.getString("password"));

                long user_id = databaseConnectionWrite.insert(
                        UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(),
                        null,
                        values
                );

                Intent intent = new Intent(activity, HomeActivity.class);
                intent.putExtra("USER_ID", user.getInt("id"));
                intent.putExtra("USER_TYPE", user.getInt("type"));
                intent.putExtra("USER_PASSWORD", user.getString("password"));
                intent.putExtra("USER_FIRSTNAME", person.getString("firstname"));
                intent.putExtra("USER_LASTNAME", person.getString("lastname"));
                intent.putExtra("USER_EMAIL", person.getString("email"));
                intent.putExtra("USER_IMAGE_FILE", person.getString("image"));

                activity.startActivity(intent);
                activity.finish();

            } else if (code == AuthenticationCodes.INVALID_USER_ID_PASSWORD) {

                Snackbar.make(source, "User not authenticated!",
                        Snackbar.LENGTH_SHORT).show();
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}