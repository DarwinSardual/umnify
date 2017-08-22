package com.example.darwin.umnify.wrapper;

import com.example.darwin.umnify.connection.WebServiceConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DataHelper {

    public static void writeTextUpload(HashMap<String, String> data, WebServiceConnection connection){

        for(String key : data.keySet()){

            connection.addTextUpload(key, data.get(key));
        }
    }

    public static void writeFileUpload(String name, HashMap<String, byte[]> data, WebServiceConnection connection){
        for(String key : data.keySet()){

            connection.addFileUpload(name, key, data.get(key));
        }
    }

    public static String parseStringFromStream(InputStream inputStream) throws IOException{

        if(inputStream == null)
            return null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String buffer = "";

        while(true){

            String temp = reader.readLine();
            if(temp != null)
                buffer += temp;
            else break;
        }

        return buffer;
    }
}
