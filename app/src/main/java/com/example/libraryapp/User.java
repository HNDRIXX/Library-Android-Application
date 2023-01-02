package com.example.libraryapp;

import static com.google.firebase.messaging.FcmBroadcastProcessor.reset;

import android.widget.EditText;

public class User {
    public String memID, fullname, address, email, password, role;
    public EditText fullnameEditText;

    public User() {

    }

    public User(String memID, String fullname, String address, String email, String password, String role) {
        this.memID = memID;
        this.fullname = fullname;
        this.address = address;
        this.email = email;
        this.password  = password;
        this.role = role;
    }
}
