package com.example.libraryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHome extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    ProgressBar progressBar;
    ImageView progressBarBackground;
    TextView progressBarText;

    AlertDialog.Builder dialogBuilder;
    AppCompatButton exit;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        getSupportActionBar().hide();

        MaterialButton profile = findViewById(R.id.profile);
        MaterialButton search = findViewById(R.id.search);
        MaterialButton scanbooks = findViewById(R.id.scanbooks);
        MaterialButton policy = findViewById(R.id.policy);
        MaterialButton libraryinfo = findViewById(R.id.libraryinfo);
        MaterialButton signout = findViewById(R.id.signout);
        MaterialButton myshelves = findViewById(R.id.myshelves);

        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);
        progressBarBackground = findViewById(R.id.progressBarBackground);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        final TextView welcomeNameTextView = (TextView) findViewById(R.id.welcome);

        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullname = userProfile.fullname;
                    String arr[] = fullname.split(" ", 2);

                    String firstName = arr[0];

                    progressBar.setVisibility(View.GONE);
                    progressBarText.setVisibility(View.GONE);
                    progressBarBackground.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    welcomeNameTextView.setText("Welcome, " + firstName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserHome.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch();
            }
        });

        scanbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserHome.this, ScanBooks.class);
                startActivity(i);
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserHome.this, ManagePolicy.class);
                startActivity(i);
            }
        });

        libraryinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserHome.this, LibraryUserInfo.class);
                startActivity(i);
            }
        });

//        digitalcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(UserHome.this, "No digital card found!",Toast.LENGTH_SHORT).show();
//            }
//        });

        myshelves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserHome.this, Shelves.class);
                startActivity(i);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(signout.getContext())
                        .setTitle("Confirm Signout")
                        .setMessage("Are you sure?")
                        .setPositiveButton(Html.fromHtml("<font color='#752424'>Yes</font>"), new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UserHome.this, MainActivity.class);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this will always start your activity as a new task
                        startActivity(intent);
                        finish();
                    }
                })
                        .setNegativeButton(Html.fromHtml("<font color='#752424'>No</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).setIcon(R.drawable.ic_baseline_warning_24).show();
            }
        });
    }

    public void openProfile(){
        Intent i = new Intent(UserHome.this, Profile.class);
        startActivity(i);
    }

    public void openSearch(){
        Intent i = new Intent(UserHome.this, SearchBooks.class);
        startActivity(i);
    }

    private void showAlertDialog(int layout){
        dialogBuilder = new AlertDialog.Builder(UserHome.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
//        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animate;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        AppCompatButton exit = layoutView.findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}