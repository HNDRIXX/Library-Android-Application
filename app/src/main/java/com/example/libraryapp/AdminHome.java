package com.example.libraryapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class AdminHome extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    ProgressBar progressBar;
    ImageView progressBarBackground;
    TextView progressBarText;
    Dialog dialog;

    AlertDialog.Builder dialogBuilder;
    AppCompatButton exit;
    AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);

        getSupportActionBar().hide();

        MaterialButton profile = findViewById(R.id.profile);
        MaterialButton managerecords = findViewById(R.id.managerecords);
        MaterialButton managebooks = findViewById(R.id.managebooks);
        MaterialButton managepolicy = findViewById(R.id.managepolicy);
        MaterialButton manageinformation = findViewById(R.id.manageinformation);
        MaterialButton signout = findViewById(R.id.signout);
        MaterialButton contacts = findViewById(R.id.contacts);

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
                Toast.makeText(AdminHome.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }

        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });

        managerecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, ViewRecords.class);
                startActivity(i);
            }
        });

        managebooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, ManageBooks.class);
                startActivity(i);
            }
        });

        managepolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, ManagePolicy.class);
                startActivity(i);
            }
        });

        manageinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, Information.class);
                startActivity(i);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showAlertDialog(R.layout.dialog_positive_layout);

                final DialogPlus dialogPlus = DialogPlus.newDialog(AdminHome.this)
                        .setContentHolder(new ViewHolder(R.layout.dialog_positive_layout))
                        .setExpanded(false)
                        .create();
                //dialogPlus.show();

                View dialogView = dialogPlus.getHolderView();

                AppCompatButton exit = dialogView.findViewById(R.id.exit);

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();
                    }
                });

                dialogPlus.show();
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
                                Intent intent = new Intent(AdminHome.this, MainActivity.class);
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
                        })
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();
            }
        });
    }

    public void openProfile(){
        Intent i = new Intent(AdminHome.this, Profile.class);
        startActivity(i);
    }

    private void showAlertDialog(int layout){
        dialogBuilder = new AlertDialog.Builder(AdminHome.this);
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