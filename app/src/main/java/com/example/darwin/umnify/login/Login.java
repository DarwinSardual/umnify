package com.example.darwin.umnify.login;

public class Login {

    private int id;
    private int type;
    private String password;

    public Login(int id, int type, String password){

        this.id = id;
        this.type = type;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getPassword(){
        return password;
    }
}
