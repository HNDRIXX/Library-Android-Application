package com.example.libraryapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LibraryUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    UserListAdapter userListAdapter;
    ArrayList<UserList> list;

    EditText mFullname, mAddress, mPassword, mEmail, mMemID;
    MaterialButton signup;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    TextView progressBarText;
    ImageView progressBarBackground;
    Spinner mRole;

    int counter;

    String libraryUserID;
    int count ;
    int userCount;
    int zero = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libraryusers);

        getSupportActionBar().hide();

        AppCompatButton adduser = findViewById(R.id.adduser);
        AppCompatButton backbuttonAddUser = findViewById(R.id.backbuttonAddUser);
        RelativeLayout adduserDialog = findViewById(R.id.adduserDialog);
        MaterialButton backbutton = findViewById(R.id.backbutton);

        mMemID = findViewById(R.id.memID);
        mFullname = findViewById(R.id.fullname);
        mAddress = findViewById(R.id.address);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);
        signup = findViewById(R.id.signup);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);
        progressBarBackground = findViewById(R.id.progressBarBackground);

        mRole = findViewById(R.id.role);

        //DISPLAY USER LIST
        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Fetch data from database and place in RecyclerView
        FirebaseRecyclerOptions<UserList> options =
                new FirebaseRecyclerOptions.Builder<UserList>()
                        .setQuery(FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").orderByChild("memID"), UserList.class)
                        .build();

        userListAdapter = new UserListAdapter(options);
        recyclerView.setAdapter(userListAdapter);

        String[] items = new String[]{"Select Role","Student", "Faculty", "Admin (Librarian)"};

        final List<String> roleList = new ArrayList<>(Arrays.asList(items));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, roleList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mRole.setAdapter(spinnerArrayAdapter);

        mRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected Role: " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(1.0f);
                backbuttonAddUser.setVisibility(View.VISIBLE);
                adduserDialog.setVisibility(View.VISIBLE);

                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backbuttonAddUser.setVisibility(View.VISIBLE);

                        DatabaseReference referenceTotal = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

                        referenceTotal.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                count = (int) snapshot.getChildrenCount();

                                userCount = count + 1;

                                if (userCount >= 9){
                                    libraryUserID = "LIBUSER" + userCount;
                                }else
                                    libraryUserID = "LIBUSER" + zero + userCount;

                                String memID = libraryUserID;
                                String email = mEmail.getText().toString().trim();
                                String address = mAddress.getText().toString().trim();
                                String fullname = mFullname.getText().toString().trim();
                                String password = mPassword.getText().toString().trim();
                                String role = mRole.getSelectedItem().toString();


                                if (TextUtils.isEmpty(email)){
                                    mEmail.setError("Email Required!");
                                    return;
                                }

                                if (TextUtils.isEmpty(password)){
                                    mPassword.setError("Password Required!");
                                    return;
                                }

                                if (password.length() < 6){
                                    mPassword.setError("Password must be 6 characters above!");
                                    return;
                                }

                                backbuttonAddUser.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                                progressBarText.setVisibility(View.VISIBLE);
                                progressBarBackground.setVisibility(View.VISIBLE);

                                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            NewUser newUser = new NewUser(memID, fullname, address, email, password, role);
                                            backbuttonAddUser.setVisibility(View.VISIBLE);

                                            FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                mMemID.setText("");
                                                                mAddress.setText("");
                                                                mEmail.setText("");
                                                                mFullname.setText("");
                                                                mPassword.setText("");

                                                                mRole.setAdapter(spinnerArrayAdapter);

                                                                counter = 1;

                                                                Toast.makeText(LibraryUsers.this, "NEW USER CREATED!",
                                                                        Toast.LENGTH_LONG).show();

                                                                backbuttonAddUser.setVisibility(View.VISIBLE);
                                                                progressBar.setVisibility(View.GONE);
                                                                progressBarText.setVisibility(View.GONE);
                                                                progressBarBackground.setVisibility(View.GONE);
                                                                adduserDialog.setVisibility(View.GONE);

//                                                        startActivity(new Intent(LibraryUsers.this, LibraryUsers.class));
                                                            } else
                                                                Toast.makeText(LibraryUsers.this, "REGISTER FAILED!"
                                                                        + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }else
                                            Toast.makeText(LibraryUsers.this, "REGISTER FAILED!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        progressBarText.setVisibility(View.GONE);
                                        progressBarBackground.setVisibility(View.GONE);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });
            }
        });

        backbuttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adduserDialog.setVisibility(View.GONE);
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (counter == 1){
                    Toast.makeText(LibraryUsers.this, "Please SIGN IN again to refresh the newly registered accounts.",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LibraryUsers.this, MainActivity.class);
                    LibraryUsers.this.startActivity(intent);
                    LibraryUsers.this.finishAffinity();
                } else
                    startActivity(new Intent(LibraryUsers.this, ViewRecords.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userListAdapter.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        if (counter == 1 ){
            Toast.makeText(LibraryUsers.this, "Please SIGN IN again to refresh the newly registered accounts.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            this.finishAffinity();
        } else
            startActivity(new Intent(LibraryUsers.this, ViewRecords.class));
    }
}