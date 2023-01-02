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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity {

    //Update Class
    EditText mFullname, mAddress, mPassword, mEmail, mPhone, mMemID;
    MaterialButton signup;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    TextView progressBarText;
    ImageView progressBarBackground;
    Spinner mRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        mMemID = findViewById(R.id.memID);
        mFullname = findViewById(R.id.fullname);
        mAddress = findViewById(R.id.address);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);
        mPhone = findViewById(R.id.phone);
        signup = findViewById(R.id.signup);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);
        progressBarBackground = findViewById(R.id.progressBarBackground);

        mRole = findViewById(R.id.role);

        String[] items = new String[]{"Select Role","User", "Admin (Librarian) "};

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



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memID = mMemID.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String address = mAddress.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
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

                progressBar.setVisibility(View.VISIBLE);
                progressBarText.setVisibility(View.VISIBLE);
                progressBarBackground.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(memID, fullname, address, email, password, role);

                            FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, "NEW USER CREATED!",
                                                        Toast.LENGTH_SHORT).show();

                                                progressBar.setVisibility(View.VISIBLE);
                                                progressBarText.setVisibility(View.VISIBLE);
                                                progressBarBackground.setVisibility(View.VISIBLE);

                                                startActivity(new Intent(Register.this, MainActivity.class));
                                            } else
                                                Toast.makeText(Register.this, "REGISTER FAILED!"
                                                        + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }else
                            Toast.makeText(Register.this, "REGISTER FAILED!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            progressBarText.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}