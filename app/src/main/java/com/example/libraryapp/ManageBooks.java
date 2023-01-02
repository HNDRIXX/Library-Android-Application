package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ManageBooks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managebooks);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);
        MaterialButton issuebutton = findViewById(R.id.issuebooks);
        MaterialButton returnbutton = findViewById(R.id.returnbooks);
        Button addNewBook = findViewById(R.id.addnewbook);
        Button updatecatalog = findViewById(R.id.updatecatalog);

        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageBooks.this, AddBook.class);
                startActivity(i);
            }
        });

        updatecatalog .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageBooks.this, UpdateCatalog.class);
                startActivity(i);
            }
        });

        issuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageBooks.this, IssueBooks.class);
                startActivity(i);
            }
        });

        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageBooks.this, ReturnBooks.class));
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ManageBooks.this, AdminHome.class);
                startActivity(intent);
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