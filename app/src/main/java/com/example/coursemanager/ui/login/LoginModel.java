package com.example.coursemanager.ui.login;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// ***
// *** This is the Model component of the login module
// ***

public class LoginModel {
    DatabaseReference ref = FirebaseDatabase.getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/").getReference();
    User user;

    public LoginModel(String str1, String str2) {
        this.user = new User(str1, str2);
    }
}
