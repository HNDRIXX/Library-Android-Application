package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ScanBooksSuccess extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    private SearchView searchs;
    ProgressBar progressBar;

    String scanBookSearch;
    ScanBooksAdapter scanBooksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_books_success);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);
        MaterialTextView notice = findViewById(R.id.notice);
        recyclerView = findViewById(R.id.scanBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        TextView progressBarText = findViewById(R.id.progressBarText);

        Intent intent = getIntent();
        scanBookSearch = intent.getExtras().getString("scanBookSearch");

        String finalSearch = scanBookSearch.toString().trim();

        Toast.makeText(this, finalSearch, Toast.LENGTH_SHORT).show();
//
//        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), finalSearch, Snackbar.LENGTH_LONG);
//        snackbar.show();

        Query query = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Book Info").orderByChild("bookTitle").startAt(finalSearch).endAt(finalSearch+"~");
        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                        .setQuery(query, SearchModel.class)
                        .build();

        scanBooksAdapter = new ScanBooksAdapter(options);
        recyclerView.setAdapter(scanBooksAdapter);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
//                    Toast.makeText(ScanBooksSuccess.this, "MERON", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    progressBarText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        int count = recyclerView.getChildCount();
//
//        Toast.makeText(this, count, Toast.LENGTH_LONG).show();

//        if (scanBooksAdapter.getItemCount() != 0) {
////            progressBar.setVisibility(View.GONE);
//            Toast.makeText(this, "MERON", Toast.LENGTH_LONG).show();
//        }


        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ScanBooksSuccess.this, UserHome.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanBooksAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanBooksAdapter.stopListening();
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }

}