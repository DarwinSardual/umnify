package com.example.darwin.umnify.login;

class AuthenticationException extends Exception{

    private String str;

    public AuthenticationException(String str){
        super(str);
        this.str = str;
    }

    public String getExceptionString(){

        return str;
    }
}
