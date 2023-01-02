package com.example.libraryapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    RelativeLayout mainLayout;
    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    ProgressBar progressBar;
    MaterialButton update;
    ImageView progressBarBackground;
    TextView progressBarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);
        MaterialButton update = findViewById(R.id.update);

        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);
        progressBarBackground = findViewById(R.id.progressBarBackground);

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        final TextView memIDTextView = (TextView) findViewById(R.id.memID);
        final EditText fullnameEditText = (EditText) findViewById(R.id.fullname);
        final EditText addressEditText = (EditText) findViewById(R.id.address);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        final TextView emailTextView = (TextView) findViewById(R.id.email);
        final EditText phoneEditText = (EditText) findViewById(R.id.phone);
        final TextView roleTextView = (TextView) findViewById(R.id.role);

        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullname = userProfile.fullname;
                    String address = userProfile.address;
                    String password = userProfile.password;
                    String email = userProfile.email;
                    String role = userProfile.role;
                    String memID = userProfile.memID;

                    progressBar.setVisibility(View.GONE);
                    progressBarText.setVisibility(View.GONE);
                    progressBarBackground.setVisibility(View.GONE);

                    memIDTextView.setText(memID);
                    fullnameEditText.setText(fullname);
                    addressEditText.setText(address);
                    emailTextView.setText(email);
                    passwordEditText.setText(password);
                    roleTextView.setText(role);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(update.getContext())
                        .setTitle("Update Information")
                        .setMessage("Confirm Changes?")
                        .setPositiveButton(Html.fromHtml("<font color='#752424'>Confirm</font>"), new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(userID);

                                String memID = memIDTextView.getText().toString().trim();
                                String email = emailTextView.getText().toString().trim();
                                String address = addressEditText.getText().toString().trim();
                                String fullname = fullnameEditText.getText().toString().trim();
                                String password = passwordEditText.getText().toString().trim();
                                String role = roleTextView.getText().toString().trim();

                                if (password.length() < 6){
                                    passwordEditText.setError("Password must be 6 characters above!");
                                    passwordEditText.requestFocus();
                                    return;
                                }

                                User user = new User(memID, fullname, address, email, password, role);

                                reference.setValue(user);

                                Toast.makeText(Profile.this, "UPDATE SUCCESFUL!", Toast.LENGTH_LONG).show();
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
}