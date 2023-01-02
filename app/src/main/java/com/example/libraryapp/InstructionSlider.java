package com.example.libraryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class InstructionSlider extends AppCompatActivity {
    SliderInstructionAdapter sliderInstructionAdapter;
    int currentImage;

    ViewPager2 instructionSlider;
    MaterialButton previousButton, nextButton, startButton;

    TextView aboutContent1, aboutContent2;
    TextView spacing;
    TextView contentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_slider);

        getSupportActionBar().hide();

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        instructionSlider = findViewById(R.id.instructionSlider);
        spacing = findViewById(R.id.spacing);

        contentTitle = findViewById(R.id.contentTitle);

        aboutContent1 = findViewById(R.id.aboutContent1);
        aboutContent2 = findViewById(R.id.aboutContent2);
        startButton = findViewById(R.id.startButton);

        initSlider();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InstructionSlider.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void initSlider() {
        sliderInstructionAdapter = new SliderInstructionAdapter(this,getImages());
        instructionSlider.setAdapter(sliderInstructionAdapter);

        initButton();

        instructionSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                currentImage = position;

                invalidateButtons();
            }
        });
    }

    private void invalidateButtons() {

        if(currentImage == sliderInstructionAdapter.getItemCount()-1){
            nextButton.setEnabled(false);
            previousButton.setEnabled(true);
            aboutContent1.setVisibility(View.GONE);
            aboutContent2.setVisibility(View.VISIBLE);

            spacing.setVisibility(View.GONE);
            contentTitle.setText("TEAM");

            startButton.setVisibility(View.VISIBLE);
        }else if (currentImage == 0){
            nextButton.setEnabled(true);
            previousButton.setEnabled(false);
            aboutContent1.setVisibility(View.VISIBLE);
            aboutContent2.setVisibility(View.GONE);
//            aboutContent1.setText(getResources().getString(R.string.slide2));
            contentTitle.setText("PURPOSE");
            spacing.setVisibility(View.VISIBLE);

            startButton.setVisibility(View.GONE);
        }else
            nextButton.setEnabled(true);
            previousButton.setEnabled(false);
    }

    private void initButton() {
        currentImage = 0;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous();
            }
        });
    }

    private void previous(){
        currentImage--;
        instructionSlider.setCurrentItem(currentImage);
    }

    private void next(){
        currentImage++;
        instructionSlider.setCurrentItem(currentImage);
    }


    private List<Integer> getImages(){
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.phonegif);
        list.add(R.drawable.teamworkgif);
        return list;
    }
}