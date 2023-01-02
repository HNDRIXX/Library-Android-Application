package com.example.libraryapp;

import static io.reactivex.rxjava3.internal.util.NotificationLite.getValue;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.senab.photoview.PhotoView;

public class SearchBooksClick extends AppCompatActivity {

    TextView bookTitleView, bookAuthor, bookColNumber, bookPublisher, bookCopies, bookCategory;
    ImageView imageView;
    FirebaseUser user;
    DatabaseReference referenceUser, referenceTotal;
    String userID;

    RelativeLayout imageBookLayout;
    PhotoView imageViewBookZoom;
    AppCompatButton exit;

    String transactionID, transactionCode;
    int count ;
    int transactionCount;
    int zero = 0;

    //vars
    DatabaseReference reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Book Transaction");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbooksclick);

        getSupportActionBar().hide();

        MaterialButton borrowButton = findViewById(R.id.borrowButton);
        AppCompatButton exitButton = findViewById(R.id.exitButton);

        imageView = findViewById(R.id.imageView);
        bookTitleView = findViewById(R.id.bookTitleTxt);
        bookAuthor = findViewById(R.id.bookAuthorTxt);
        bookColNumber = findViewById(R.id.bookColNumberTxt);
        bookCopies = findViewById(R.id.bookCopiesTxt);
        bookPublisher = findViewById(R.id.bookPublisherTxt);
        bookCategory = findViewById(R.id.bookCategoryTxt);

        imageBookLayout = findViewById(R.id.imageBookLayout);
        imageViewBookZoom = findViewById(R.id.imageViewBookZoom);
        exit = findViewById(R.id.exit);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUser = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        Intent data = getIntent();
        String img = data.getStringExtra("Book Image");
        String titleData = data.getStringExtra("Title");
        String authorData = data.getStringExtra("Author");
        String categoryData = data.getStringExtra("Category");
        String copiesData = data.getStringExtra("Copies");
        String publisherData = data.getStringExtra("Publisher");
        String colnumData = data.getStringExtra("Colnum");

        Glide.with(this)
                .load(img)
                .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .into(imageView);

        bookTitleView.setText("Title: "+titleData);
        bookAuthor.setText("Author/s: "+authorData);
        bookCategory.setText("Category: "+categoryData);
        bookPublisher.setText("Publisher: "+publisherData);
        bookCopies.setText(copiesData + " Copies");
        bookColNumber.setText("Col NO.: "+colnumData);

        if (bookCopies.getText().toString().equals("0 Copies")){
            borrowButton.setEnabled(false);
            borrowButton.setAlpha(.5f);

            borrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SearchBooksClick.this, "Unable to borrow this book\n This book has 0 Copies.", Toast.LENGTH_SHORT).show();
                }
            });
        }else
            borrowButton.setEnabled(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(SearchBooksClick.this)
                        .load(img)
                        .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                        .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                        .into(imageViewBookZoom);

                imageBookLayout.setVisibility(View.VISIBLE);

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageBookLayout.setVisibility(View.GONE);
                    }
                });
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchBooksClick.this, SearchBooks.class);
                startActivity(i);
            }
        });

        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(borrowButton.getContext())
                        .setTitle("Borrow Request")
                        .setMessage("Confirm Request?")
                        .setPositiveButton(Html.fromHtml("<font color='#752424'>Confirm</font>"), new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                referenceUser.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);

                                        DatabaseReference referenceTotal = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Book Transaction");

                                        referenceTotal.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                count = (int) snapshot.getChildrenCount();

                                                transactionCount = count + 1;

                                                if (transactionCount >= 9){
                                                    transactionID = "LIBTXN" + transactionCount;
                                                }else
                                                    transactionID = "LIBTXN" + zero + transactionCount;

                                                DateFormat df = new SimpleDateFormat("EEE, MMM d yyyy, HH:mm");
                                                String dateFormat = df.format(Calendar.getInstance().getTime());

                                                String nameFull = user.fullname;

                                                String userUID = userID;
                                                String transactionCode = transactionID;
                                                String imageUrl = img;
                                                String fullname = nameFull;
                                                String bookTitle = titleData;
                                                String date = dateFormat;
                                                String transactionFlag = "1";

                                                BookTransaction bookTransaction = new BookTransaction(userUID, transactionCode, imageUrl, fullname, bookTitle, dateFormat, transactionFlag);
                                                String modelId = reference.push().getKey();
                                                reference.child(modelId).setValue(bookTransaction);

                                                Toast.makeText(SearchBooksClick.this, "BORROW REQUEST SENT", Toast.LENGTH_LONG).show();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


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
        Intent intent = new Intent(this, SearchBooks.class);
        startActivity(intent);
    }
}