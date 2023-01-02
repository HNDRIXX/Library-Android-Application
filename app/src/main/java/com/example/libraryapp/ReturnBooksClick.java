package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReturnBooksClick extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser user;
    DatabaseReference referenceUser;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_books_click);

        getSupportActionBar().hide();

        AppCompatButton exitbutton = findViewById(R.id.exitButton);
        MaterialButton returnButton = findViewById(R.id.returnButton);

        ImageView imageView = findViewById(R.id.imageView);
        TextView transactionCodeView = findViewById(R.id.transactionCodeView);
        TextView borrowerNameView = findViewById(R.id.borrowerNameView);
        TextView bookTitleView = findViewById(R.id.bookTitleView);
        TextView requestDateView = findViewById(R.id.requestDateView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUser = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        Intent data = getIntent();

        String key = getIntent().getStringExtra("key");
        String imageData = data.getStringExtra("bookImage");
        String transactionCodeData = data.getStringExtra("transactionCode");
        String fullnameData = data.getStringExtra("fullname");
        String bookTitleData = data.getStringExtra("bookTitle");
        String requestDateData = data.getStringExtra("date");

        Glide.with(this)
                .load(imageData)
                .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .into(imageView);

        transactionCodeView.setText(transactionCodeData);
        borrowerNameView.setText(fullnameData);
        bookTitleView.setText(bookTitleData);
        requestDateView.setText(requestDateData);

        exitbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReturnBooksClick.this, ReturnBooks.class);
                startActivity(intent);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(returnButton.getContext())
                        .setTitle("Confirm Return")
                        .setMessage("Confirm Return Book?")
                        .setPositiveButton(Html.fromHtml("<font color='#752424'>Confirm</font>"), new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Book Transaction").child(key);

                                String userUID = userID;
                                String transactionCode = transactionCodeData;
                                String imageUrl = imageData;
                                String fullname = fullnameData;
                                String bookTitle = bookTitleData;
                                String date = requestDateData;
                                String transactionFlag = "3";//eto yung value kapag returned na yung book.

                                BookTransaction bookTransaction = new BookTransaction(userUID, transactionCode, imageUrl, fullname, bookTitle, date, transactionFlag);
                                reference.setValue(bookTransaction);

                                Toast.makeText(ReturnBooksClick.this, "RETURN SUCCESSFUL.", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(ReturnBooksClick.this, ReturnBooks.class));
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#752424'>Cancel</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(R.drawable.ic_baseline_warning_24).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ReturnBooks.class);
        startActivity(intent);
    }

}