package com.example.arlyston.proyekandroid;

/**
 * Created by Arlyston on 16/12/2018.
 */

public class User {

    String username,password;
    public User(){}
    public User (String user, String pass){
        username = user;
        password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
