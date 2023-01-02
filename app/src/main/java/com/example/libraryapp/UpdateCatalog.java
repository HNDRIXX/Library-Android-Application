package com.example.libraryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class UpdateCatalog extends AppCompatActivity {

    RecyclerView recyclerView;
    UpdateCatalogAdapter updateCatalogAdapter;
    RelativeLayout imageBookLayout;

    PhotoView imageViewBookZoom;
    AppCompatButton exit;
    private ProgressBar progressBar;
    String bookURL;

    private SearchView searchs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_catalog);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);

        imageBookLayout = findViewById(R.id.imageBookLayout);
        imageViewBookZoom = findViewById(R.id.imageViewBookZoom);
        exit = findViewById(R.id.exit);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);

        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Info").orderByChild("bookTitle"), SearchModel.class)
                        .build();

        updateCatalogAdapter = new UpdateCatalogAdapter(options);
        recyclerView.setAdapter(updateCatalogAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

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

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UpdateCatalog.this, ManageBooks.class);
                startActivity(intent);
            }
        });

    }

//    public void isRecyclerScrollable() {
//        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        if (layoutManager == null || adapter == null)
//            progressBar.setVisibility(View.GONE);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCatalogAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateCatalogAdapter.stopListening();
    }

    private void txtSearch (String str)
    {
        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Info").orderByChild("bookTitle").startAt(str).endAt(str+"~"), SearchModel.class)
                        .build();

        updateCatalogAdapter = new UpdateCatalogAdapter(options);
        updateCatalogAdapter.startListening();
        recyclerView.setAdapter(updateCatalogAdapter);

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            bookURL = intent.getStringExtra("bookURL");

            Glide.with(UpdateCatalog.this)
                    .load(bookURL)
                    .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                    .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                    .into(imageViewBookZoom);

            imageBookLayout.setVisibility(View.VISIBLE);
//            exit.setVisibility(View.VISIBLE);

//            PhotoViewAttacher pAttacher;
//            pAttacher = new PhotoViewAttacher(imageViewBookZoom);
//            pAttacher.update();

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookURL = "";

                    imageBookLayout.setVisibility(View.GONE);
//                    imageViewBookZoom.setVisibility(View.GONE);
//                    exit.setVisibility(View.GONE);
                }
            });

        }
    };

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ManageBooks.class);
        startActivity(intent);
    }
}