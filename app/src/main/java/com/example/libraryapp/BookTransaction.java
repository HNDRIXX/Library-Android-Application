package com.example.libraryapp;

import java.io.Serializable;

//These java class fetch data from database
public class BookTransaction implements Serializable{

    String userUID, transactionCode, imageUrl, fullname, bookTitle, date, transactionFlag;

    public BookTransaction(){
        //empty method is required
    }

    public BookTransaction (String userUID, String transactionCode, String imageUrl, String fullname, String bookTitle, String  date, String transactionFlag) {
        this.userUID = userUID;
        this.transactionCode = transactionCode;
        this.imageUrl = imageUrl;
        this.fullname = fullname;
        this.bookTitle = bookTitle;
        this.date = date;
        this.transactionFlag = transactionFlag;
    }

    public  String getUserUID() { return  userUID; }

    public String getTransactionCode() { return transactionCode;}

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getFullname() { return fullname; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBookTitle() { return bookTitle; }

//    public void setBookTitle(String bookTitle) {
//        this.bookTitle = bookTitle;
//    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionFlag() { return transactionFlag; }

    public void setTransactionFlag(String transactionFlag) {
        this.transactionFlag = transactionFlag;
    }

}
