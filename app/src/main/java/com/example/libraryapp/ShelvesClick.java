package com.example.libraryapp;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;

public class ShelvesClick extends AppCompatActivity {

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelves_click);

        getSupportActionBar().hide();

        AppCompatButton exitbutton = findViewById(R.id.exitButton);
        MaterialButton allowButton = findViewById(R.id.allowButton);

        ImageView imageView = findViewById(R.id.imageView);
        PhotoView libraryMap = findViewById(R.id.libraryMap);
        TextView transactionCodeView = findViewById(R.id.transactionCodeView);
        TextView bookTitleView = findViewById(R.id.bookTitleView);
        TextView requestDateView = findViewById(R.id.requestDateView);

        Intent data = getIntent();

        String imageData = data.getStringExtra("bookImage");
        String transactionCodeData = data.getStringExtra("transactionCode");
        String bookTitleData = data.getStringExtra("bookTitle");
        String requestDateData = data.getStringExtra("date");

        Glide.with(this).load(getImage("shelveright3")).into(libraryMap);

        Glide.with(this)
                .load(imageData)
                .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .into(imageView);

        transactionCodeView.setText(transactionCodeData);
        bookTitleView.setText(bookTitleData);
        requestDateView.setText(requestDateData);

        exitbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShelvesClick.this, Shelves.class);
                startActivity(intent);
            }
        });
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier("shelveright3", "drawable", this.getPackageName());

        return drawableResourceId;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Shelves.class);
        startActivity(intent);
    }
}