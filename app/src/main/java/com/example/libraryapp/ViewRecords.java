package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ViewRecords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrecords);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);
        MaterialButton borrowedBooks = findViewById(R.id.borrowedBooks);
        MaterialButton returnedBooks = findViewById(R.id.returnedBooks);
        Button libraryusers = findViewById(R.id.libraryusers);

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ViewRecords.this, AdminHome.class));
            }
        });

        libraryusers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewRecords.this, LibraryUsers.class);
                startActivity(i);
            }
        });

        borrowedBooks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewRecords.this, BorrowedBooksList.class);
                startActivity(i);
            }
        });

        returnedBooks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewRecords.this, ReturnedBooksList.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, AdminHome.class);
        startActivity(intent);
    }
}