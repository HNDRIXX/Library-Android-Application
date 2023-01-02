package com.example.libraryapp;

import static io.reactivex.rxjava3.internal.util.NotificationLite.getValue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Shelves extends AppCompatActivity {

    private ProgressBar progressBar;

    RecyclerView recyclerView;
    ShelvesAdapter shelvesAdapter;
    DatabaseReference reference;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String userID;
    CardView cardItemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelves);

        getSupportActionBar().hide();

        MaterialButton backButton = findViewById(R.id.backbutton);
        MaterialTextView notice = findViewById(R.id.notice);

        progressBar = findViewById(R.id.progressBar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        recyclerView = (RecyclerView)findViewById(R.id.shelvesList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<BookTransaction> options =
                new FirebaseRecyclerOptions.Builder<BookTransaction>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book Transaction").orderByChild("userUID").equalTo(userID), BookTransaction.class)
                        .build();

        shelvesAdapter = new ShelvesAdapter(options);
        recyclerView.setAdapter(shelvesAdapter);

        int count = shelvesAdapter.getItemCount();

        if (count == 0) {
            notice.setVisibility(View.VISIBLE);
        } else {
            notice.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Shelves.this, UserHome.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        shelvesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        shelvesAdapter.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }

}