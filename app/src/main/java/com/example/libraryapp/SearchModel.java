package com.example.libraryapp;

import java.io.Serializable;

//These java class fetch data from database
public class SearchModel implements Serializable {

    String imageUrl, bookTitle, bookAuthor, bookColNumber, bookPublisher, bookCopies, bookCategory;

    SearchModel(){
        //empty method is required
    }

    public SearchModel(String imageUrl, String bookTitle, String bookAuthor, String bookColNumber, String bookPublisher, String bookCopies, String bookCategory) {
        this.imageUrl = imageUrl;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookColNumber = bookColNumber;
        this.bookPublisher = bookPublisher;
        this.bookCopies = bookCopies;
        this.bookCategory = bookCategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookColNumber() {
        return bookColNumber;
    }

    public void setBookColNumber(String bookColNumber) {
        this.bookColNumber = bookColNumber;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookCopies() {
        return bookCopies;
    }

    public void setBookCopies(String bookCopies) {
        this.bookCopies = bookCopies;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

}
