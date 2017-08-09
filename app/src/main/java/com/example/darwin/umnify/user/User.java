package com.example.darwin.umnify.user;

public class User {

    private int id;
    private int type;
    private String password;

    public User(int id, int type, String password){

        this.id = id;
        this.type = type;
        this.password = password;
    }

    public int getId(){
        return id;
    }

    public int getType(){
        return type;
    }

    public String getPassword() {
        return password;
    }
}
