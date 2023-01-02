package com.example.libraryapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class Information extends AppCompatActivity {

    String url1 = "https://i.ibb.co/RzWQH9Q/ahs1.jpg";
    String url2 = "https://i.ibb.co/GkrSP98/rsz-1315109965-1199805623908368-8040107534441163868-n.jpg";
    String url3 = "https://i.ibb.co/0DXp0zz/rsz-315521408-480583133906397-8785665893426289583-n.jpg";
    String url4 = "https://i.ibb.co/zJKYQfv/rsz-2315522404-1125164121723707-8660020170710503495-n.jpg";

//    "https://i.ibb.co/g3ySY68/ahs2.jpg" "https://i.ibb.co/mT3sjCg/ahs3.jpg"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        getSupportActionBar().hide();

        ArrayList<SliderContent> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderContent(url1));
        sliderDataArrayList.add(new SliderContent(url2));
        sliderDataArrayList.add(new SliderContent(url3));
        sliderDataArrayList.add(new SliderContent(url4));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        MaterialButton backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}