package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import uk.co.senab.photoview.PhotoView;

public class ScanBooksSuccessClick extends AppCompatActivity {

    TextView bookTitleView, bookAuthor, bookColNumber, bookPublisher, bookCopies, bookCategory;
    ImageView imageView, imageDetailsDisplay;
    FirebaseUser user;
    DatabaseReference referenceUser;
    String userID;

    RelativeLayout imageBookLayout;
    TextView moreDetailsResult, moreDetailsContent;
    PhotoView imageViewBookZoom;
    AppCompatButton exit;

    String transactionID;
    String categoryData;
    int count ;
    int transactionCount;
    int zero = 0;

    String imageName;
    String imageMoreDetailsName;
    PhotoView libraryMap;

    //vars
    DatabaseReference reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Book Transaction");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_books_success_click);

        getSupportActionBar().hide();

        MaterialButton borrowButton = findViewById(R.id.borrowButton);
        AppCompatButton exitButton = findViewById(R.id.exitButton);
        MaterialButton moreDetails = findViewById(R.id.moreDetails);
        AppCompatButton exitMoreDetails = findViewById(R.id.exitMoreDetails);

        AppCompatButton exitNotice = findViewById(R.id.exitNotice);
        RelativeLayout moreDetailsDisplay = findViewById(R.id.moreDetailsDisplay);

        libraryMap = findViewById(R.id.libraryMap);

        imageView = findViewById(R.id.imageView);
        bookTitleView = findViewById(R.id.bookTitleTxt);
        bookAuthor = findViewById(R.id.bookAuthorTxt);
        bookColNumber = findViewById(R.id.bookColNumberTxt);
        bookCopies = findViewById(R.id.bookCopiesTxt);
        bookPublisher = findViewById(R.id.bookPublisherTxt);
        bookCategory = findViewById(R.id.bookCategoryTxt);

        imageDetailsDisplay = findViewById(R.id.imageDetailsDisplay);
        moreDetailsResult = findViewById(R.id.moreDetailsResult);
        moreDetailsContent = findViewById(R.id.moreDetailsContent);

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

        categoryData = data.getStringExtra("Category");

        String copiesData = data.getStringExtra("Copies");
        String publisherData = data.getStringExtra("Publisher");
        String colnumData = data.getStringExtra("Colnum");

        if(categoryData.equals("Language")){
            moreDetailsResult.setText("Language");
            moreDetailsContent.setText(getString(R.string.languageContent));
            imageMoreDetailsName = "shelveleft1image";
            imageName = "mapshelveleft1";
        }else if(categoryData.equals("Applied Science") || categoryData.equals("Arts and Recreation")){
            if(categoryData.equals("Applied Science")){
                moreDetailsResult.setText("Applied Science");
                moreDetailsContent.setText(getString(R.string.appliedScienceContent));
            }else if(categoryData.equals("Arts and Recreation")) {
                moreDetailsResult.setText("Arts and Recreation");
                moreDetailsContent.setText(getString(R.string.artsRecreationContent));
            }

            imageMoreDetailsName = "shelveleft2image";
            imageName = "mapshelveleft2";
        }else if(categoryData.equals("General Works") || categoryData.equals("Philosophy")){
            if(categoryData.equals("General Works")){
                moreDetailsResult.setText("General Works");
                moreDetailsContent.setText(getString(R.string.generalWorksContent));
            }else if(categoryData.equals("Philosophy")){
                moreDetailsResult.setText("Philosophy");
                moreDetailsContent.setText(getString(R.string.philosphyContent));
            }

            imageMoreDetailsName = "shelveright1image";
            imageName = "mapshelveright1";
        }else if(categoryData.equals("Religion") || categoryData.equals("Social Science")){
            if(categoryData.equals("Religion")){
                moreDetailsResult.setText("Religion");
                moreDetailsContent.setText(getString(R.string.religionContent));
            }else if(categoryData.equals("Social Science")) {
                moreDetailsResult.setText("Social Science");
                moreDetailsContent.setText(getString(R.string.socialScienceContent));
            }

            imageMoreDetailsName = "shelveright1image";
            imageName = "mapshelveright1";

        }else if(categoryData.equals("Pure Science")){
            moreDetailsResult.setText("Pure Science");
            moreDetailsContent.setText(getString(R.string.pureScienceContent));
            imageMoreDetailsName = "shelveright2image";
            imageName = "mapshelveright2";
        }else if(categoryData.equals("Literature")){
            moreDetailsResult.setText("Literature");
            moreDetailsContent.setText(getString(R.string.literatureContent));
            imageMoreDetailsName = "shelveright3image";
            imageName = "mapshelveright3";
        }else if(categoryData.equals("History")){
            moreDetailsResult.setText("History");
            moreDetailsContent.setText(getString(R.string.historyContent));
            imageMoreDetailsName = "shelveright4image";
            imageName = "mapshelveright4";
        }else if(categoryData.equals("Encyclopedia")){
            moreDetailsResult.setText("Encyclopedia");
            moreDetailsContent.setText("Encyclopedia shelves near Librarian Office");
            imageMoreDetailsName = "encylopediaimage";
            imageName = "mapencyclopedia";
        }

        Glide.with(this).asGif().load(getImage(imageName)).into(libraryMap);
        Glide.with(this).load(getImage(imageMoreDetailsName)).into(imageDetailsDisplay);

        Glide.with(this)
                .load(img)
                .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .into(imageView);

        bookTitleView.setText(titleData);
        bookAuthor.setText(authorData);
        bookCategory.setText(categoryData);
        bookPublisher.setText(publisherData);
        bookCopies.setText(copiesData + " Copies");
        bookColNumber.setText(colnumData);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pathfinder, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
//
//        exitNotice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });

        moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreDetailsDisplay.setVisibility(View.VISIBLE);
            }
        });

        exitMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreDetailsDisplay.setVisibility(View.GONE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(ScanBooksSuccessClick.this)
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
                Intent i = new Intent(ScanBooksSuccessClick.this, ScanBooks.class);
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

                                                Toast.makeText(ScanBooksSuccessClick.this, "BORROW REQUEST SENT", Toast.LENGTH_LONG).show();

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

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ScanBooks.class);
        startActivity(intent);
    }


}