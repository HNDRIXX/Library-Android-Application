package com.example.libraryapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

public class SearchBooks extends AppCompatActivity{

    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    private SearchView searchs;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbooks);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);

        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Info").orderByChild("bookTitle"), SearchModel.class)
                        .build();

        searchAdapter = new SearchAdapter(options);
        recyclerView.setAdapter(searchAdapter);

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchBooks.this, UserHome.class);
                startActivity(intent);
            }
        });

        SearchView searchs = findViewById(R.id.searchs);
        searchs.clearFocus();

        ImageView searchIcon = searchs.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(getResources().getColor(R.color.barmaroon),
                PorterDuff.Mode.SRC_IN);

        searchs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

    }

//    public void isRecyclerScrollable() {
//        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        if (layoutManager == null || adapter == null)
//        progressBar.setVisibility(View.GONE);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        searchAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }

    private void txtSearch (String str)
    {
        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Info").orderByChild("bookTitle").startAt(str).endAt(str+"~"), SearchModel.class)
                        .build();

        searchAdapter = new SearchAdapter(options);
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }

}