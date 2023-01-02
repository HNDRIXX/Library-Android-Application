package com.example.libraryapp;

import java.io.Serializable;

public class UserList implements Serializable {

    String fullname, email, memID, role;

    public UserList(){
        //empty method is required
    }

    public UserList (String fullname, String email, String memID, String role) {
        this.fullname = fullname;
        this.email = email;
        this.memID = memID;
        this.fullname = fullname;
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getMemID() {
        return memID;
    }

    public String getRole() {
        return role;
    }
}
