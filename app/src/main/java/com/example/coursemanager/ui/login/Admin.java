package com.example.coursemanager.ui.login;

public class Admin {
    public String email;
    public String password;

    public Admin(){
    }

    public Admin(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPaswword(){
        return this.password;
    }
}
