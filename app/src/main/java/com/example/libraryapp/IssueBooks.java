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

public class IssueBooks extends AppCompatActivity {

    RecyclerView recyclerView;
    IssueBooksAdapter issueBooksAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_books);

        getSupportActionBar().hide();

        MaterialButton backButton = findViewById(R.id.backbutton);
        MaterialTextView notice = findViewById(R.id.notice);

        recyclerView = (RecyclerView)findViewById(R.id.issueList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);

        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<BookTransaction> options =
                new FirebaseRecyclerOptions.Builder<BookTransaction>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Transaction").orderByChild("transactionFlag").equalTo("1"), BookTransaction.class)
                        .build();

        issueBooksAdapter = new IssueBooksAdapter(options);
        recyclerView.setAdapter(issueBooksAdapter);

        int count = issueBooksAdapter.getItemCount();

        if (count == 0) {
            notice.setVisibility(View.VISIBLE);
        } else {
            notice.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(IssueBooks.this, ManageBooks.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        issueBooksAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        issueBooksAdapter.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ManageBooks.class);
        startActivity(intent);
    }
}