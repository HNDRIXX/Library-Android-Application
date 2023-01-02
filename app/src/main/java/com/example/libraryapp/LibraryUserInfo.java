package com.example.libraryapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.button.MaterialButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class LibraryUserInfo extends AppCompatActivity {

    AlertDialog.Builder dialogBuilder;
    AppCompatButton exit;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libraryuserinfo);

        getSupportActionBar().hide();


        MaterialButton generalinfo = findViewById(R.id.generalinfo);
        MaterialButton librarycontacts = findViewById(R.id.librarycontacts);
        MaterialButton backbutton = findViewById(R.id.backbutton);

        generalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LibraryUserInfo.this, Information.class);
                startActivity(i);
            }
        });

        librarycontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(LibraryUserInfo.this)
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

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}