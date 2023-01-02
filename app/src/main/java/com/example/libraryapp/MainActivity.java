package com.example.libraryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail;
    TextInputEditText editTextPassword;

    //TextView register;
    FirebaseAuth mAuth;
    MaterialButton loginbutton;
    ProgressBar progressBar;
    ImageView progressBarBackground;
    TextView progressBarText;
    RelativeLayout mainLayout;

    FirebaseUser user;
    DatabaseReference reference;
    String userID;

    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (TextInputEditText) findViewById(R.id.password);

        loginbutton = (MaterialButton) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(this);

//        register = (TextView) findViewById(R.id.register);
//        register.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
        progressBarText = findViewById(R.id.progressBarText);

        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.register:
//                startActivity(new Intent(MainActivity.this, Register.class));
//                break;

            case R.id.loginbutton:
//                startActivity(new Intent(MainActivity.this, UserHome.class));
                userLogin();
                break;
        }
    }

    public void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            editTextEmail.setError("Email Required!");
            return;
        }

        if (TextUtils.isEmpty(password)){
            editTextPassword.setError("Password Required!");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        //hide keyboard when user press the login button.
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
                    userID = user.getUid();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);

                            String pass = userProfile.password;
                            String mail = userProfile.email;
                            String role = userProfile.role;

                            if (mail.equals(email) && pass.equals(password) && role.equals("Admin (Librarian)")){

                                progressBar.setVisibility(View.VISIBLE);
                                progressBarBackground.setVisibility(View.VISIBLE);
                                progressBarText.setVisibility(View.VISIBLE);

                                startActivity(new Intent(MainActivity.this, AdminHome.class));

                            } else if (mail.equals(email) && pass.equals(password) && role.equals("Student") || mail.equals(email) && pass.equals(password) && role.equals("Faculty")) {

                                progressBar.setVisibility(View.VISIBLE);
                                progressBarBackground.setVisibility(View.VISIBLE);
                                progressBarText.setVisibility(View.VISIBLE);

                                startActivity(new Intent(MainActivity.this, UserHome.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            progressBarText.setVisibility(View.GONE);
                        }
                    });
                }else
                    Toast.makeText(MainActivity.this, "LOGIN FAILED! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
                progressBarBackground.setVisibility(View.GONE);
                progressBarText.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        this.finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {

//            startActivity(new Intent(MainActivity.this, InstructionSlider.class));
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }
}

