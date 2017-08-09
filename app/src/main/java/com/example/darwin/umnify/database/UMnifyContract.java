package com.example.darwin.umnify.database;

import android.provider.BaseColumns;

public final class UMnifyContract {

    private UMnifyContract (){}

    public static class UMnifyColumns implements BaseColumns {

        public enum Person{

            TABLE_NAME("Person"),
            ID("id"),
            FIRSTNAME("firstname"),
            MIDDLENAME("middlename"),
            LASTNAME("lastname"),
            NAME_EXT("name_ext"),
            BIRTHDATE("birthdate"),
            GENDER("gender"),
            ADDRESS("address"),
            CONTACT("contact"),
            IMAGE("image");

            private String str;

            Person(String str){ this.str = str; }

            public String toString(){ return str;}
        }

        public enum User{

            TABLE_NAME("User"),
            ID("id"),
            TYPE("type"),
            PASSWORD("password");

            private String str;

            User(String str){ this.str = str; }

            public String toString(){ return str;}
        }


    }
}
