package com.example.libraryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.FirebaseDatabase;

public class ReturnBooks extends AppCompatActivity {

    RecyclerView recyclerView;
    ReturnBooksAdapter returnBooksAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_books);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);
        MaterialTextView notice = findViewById(R.id.notice);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = (RecyclerView)findViewById(R.id.returnList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<BookTransaction> options =
                new FirebaseRecyclerOptions.Builder<BookTransaction>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Transaction").orderByChild("transactionFlag").equalTo("2"), BookTransaction.class)
                        .build();

        returnBooksAdapter = new ReturnBooksAdapter(options);
        recyclerView.setAdapter(returnBooksAdapter);

        int count = returnBooksAdapter.getItemCount();

        if (count == 0) {
            notice.setVisibility(View.VISIBLE);
        } else {
            notice.setVisibility(View.GONE);
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReturnBooks.this, ManageBooks.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        returnBooksAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        returnBooksAdapter.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ManageBooks.class);
        startActivity(intent);
    }


}