package com.example.libraryapp;

public class BookInfo {

    private String imageUrl;
    private String bookTitle;
    private String bookAuthor;
    private String bookColNumber;
    private String bookPublisher;
    private String bookCopies;
    private String bookCategory;

    public BookInfo(String imageUrl, String bookTitle, String bookAuthor, String bookColNumber, String bookCopies, String bookPublisher, String bookCategory){
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
