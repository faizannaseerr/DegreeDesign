package com.example.coursemanager.ui.login;

public class User {
    public String email;
    public String password;

    public User(){
    }

    public User(String email, String password){
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

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(!(obj instanceof User)){
            return false;
        }

        User user = (User) obj;
        if(email == user.email && password == user.password){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return email;
    }
}
