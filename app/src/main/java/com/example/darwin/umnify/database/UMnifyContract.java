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
            IMAGE("image"),
            EMAIL("email");

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

        public enum AcademePerson{

            TABLE_NAME("AcademePerson"),
            ID("id"),
            COURSE("course"),
            YEAR("year"),
            TYPE("type");

            private String str;

            AcademePerson(String str){ this.str = str; }

            public String toString(){ return str;}
        }

        public enum News{

            TABLE_NAME("News"),
            ID("id"),
            CONTENT("content"),
            IMAGE("image"),
            AUTHOR("author"),
            CREATED_DATE("created_date"),
            PUBLISHED_DATE("published_date"),
            SIGNATURE("signature"),
            AUTHOR_FIRSTNAME("author_firstname"),
            AUTHOR_LASTNAME("author_lastname"),
            AUTHOR_IMAGE("author_image");

            private String str;

            News(String str){ this.str = str; }

            public String toString(){ return str;}
        }

        public enum Blog{

            TABLE_NAME("Blog"),
            ID("id"),
            HEADING("heading"),
            CONTENT("content"),
            IMAGE("image"),
            AUTHOR("author"),
            PUBLISHED_DATE("published_date"),
            SIGNATURE("signature"),
            AUTHOR_FIRSTNAME("author_firstname"),
            AUTHOR_LASTNAME("author_lastname"),
            AUTHOR_IMAGE("author_image");

            private String str;

            Blog(String str){ this.str = str; }

            public String toString(){ return str;}
        }

        public enum Announcement{

            TABLE_NAME("Announcement"),
            ID("id"),
            TITLE("title"),
            CONTENT("content"),
            IMAGE("image"),
            AUTHOR("author"),
            CREATED_DATE("created_date"),
            PUBLISHED_DATE("published_date"),
            SIGNATURE("signature"),
            AUTHOR_FIRSTNAME("author_firstname"),
            AUTHOR_LASTNAME("author_lastname");

            private String str;

            Announcement(String str){ this.str = str; }

            public String toString(){ return str;}
        }


    }
}
