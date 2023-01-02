package com.example.libraryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.FirebaseDatabase;

public class ReturnedBooksList extends AppCompatActivity {
    RecyclerView recyclerView;
    ReturnedBooksListAdapter returnedBooksListAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returned_books_list);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);
        MaterialTextView notice = findViewById(R.id.notice);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView)findViewById(R.id.returnedBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<BookTransaction> options =
                new FirebaseRecyclerOptions.Builder<BookTransaction>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Transaction").orderByChild("transactionFlag").equalTo("3"), BookTransaction.class)
                        .build();

        returnedBooksListAdapter = new ReturnedBooksListAdapter(options);
        recyclerView.setAdapter(returnedBooksListAdapter);

        int count = returnedBooksListAdapter.getItemCount();

        if (count == 0) {
            notice.setVisibility(View.VISIBLE);
        } else {
            notice.setVisibility(View.GONE);
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReturnedBooksList.this, ViewRecords.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        returnedBooksListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        returnedBooksListAdapter.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ViewRecords.class);
        startActivity(intent);
    }
}