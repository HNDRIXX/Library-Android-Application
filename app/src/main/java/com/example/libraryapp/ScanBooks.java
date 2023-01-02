package com.example.libraryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.controls.templates.ThumbnailTemplate;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryapp.ml.Modelbook;
import com.example.libraryapp.ml.Modelbook;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class ScanBooks extends AppCompatActivity {

    TextView bookPercentageResult;
    String scanBookSearch;
    TextView bookResult;
    ImageView imageView;
    AppCompatButton picturebutton;
    int imageSize = 224;
    private static final int REQUEST_CAMERA_CODE = 100;

    ScanBooksAdapter scanBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_books);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);

        bookResult = findViewById(R.id.bookResult);
//        bookPercentageResult = findViewById(R.id.bookPercentageResult);
        imageView = findViewById(R.id.imageView);

        picturebutton = findViewById(R.id.picturebutton);

        picturebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                    }
                }
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ScanBooks.this, UserHome.class);
                startActivity(intent);
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            Modelbook model = Modelbook.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0 ,0, image.getWidth(), image.getHeight());
            int pixel = 0;

            for (int i = 0; i < imageSize; i++){
                for (int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f/ 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f/ 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f/ 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Modelbook.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] resultBook = outputFeature0.getFloatArray();

            int position = 0;
            float percentage = 0;

            for(int i = 0; i < resultBook.length; i++){
                if(resultBook[i] > percentage){
                    percentage = resultBook[i];
                    position = i;
                }
            }

//            String[] classes = {"Atomic Habits", "The Subtle Are Of Not Giving a F*ck", "Power of the Plus Factor"};

            String[] classes = {"Algebra Integrated Mathematics",
                    "Achievements Through The Ages",
                    "Advance Mathematical Concept",
                    "Adventures For Readers",
                    "Atomic Habit",
                    "America's Story",
                    "Building English Skills",
                    "Computer Fundamentals",
                    "Computer Programming Basic",
                    "Conceptual Physics",
                    "Diversity",
                    "Elements of Literature",
                    "English 2600",
                    "From Darkness To Light",
                    "Guidance Services",
                    "HBJ Language",
                    "In The Middle",
                    "Informal Geometry",
                    "Investigating Mathematics",
                    "Laidlaw Algebra",
                    "Literature Language",
                    "Our Natural Resources",
                    "People And Nations",
                    "Psychology Principles",
                    "Power of the Plus Factor",
                    "Prentice Hall Literature - The American Experience",
                    "Rise Of American Nation",
                    "Science Explorer",
                    "Stay Alive All Your Life",
                    "Succeeding In The World of Work",
                    "The Original Telepathy",
                    "The Power Of Positive Living",
                    "The World Past and Present",
                    "War and Society In Renaissance Europe",
                    "Word Perfect 6.0",
                    "World Geography",
                    "Write Source 2000",
                    "20th Century",
                    "A Proud Nation",
                    "Accent On Science",
                    "Addison-Wesley Mathematics",
                    "Advance Algebra",
                    "Adventures In American Literature",
                    "Adventures In Appreciation",
                    "Adventures In Reading",
                    "Agriscience",
                    "Algebra 2 With Trigonometry",
                    "Algebra Two With Trigonometry",
                    "Algebra Structure And Method",
                    "Accounting Fourth Edition",
                    "America In Space And Time",
                    "America Its People And Values",
                    "America The People And The Dream",
                    "American Civics",
                    "American Civics",
                    "American Government",
                    "Analytics Chemistry",
                    "Appreciating Literature",
                    "Area Handbook For Peru",
                    "Art In Your World",
                    "Asia 20th Century",
                    "Asia Contemporary Art",
                    "Asian 20th Century ",
                    "Appreciating Literature",
                    "Automotive Body Repair",
                    "Basic Crafts",
                    "Basic Skills In English",
                    "A Picture Story Of The Nations Capital",
                    "Big Media Little Media",
                    "Biology The Web of Life",
                    "Bridled Ambition",
                    "Business Filings And Records Control",
                    "Business Mathematics Tenth Edition",
                    "Caring For Children",
                    "Central Banking For Every Juan And Maria",
                    "Chemistry A Modern Course ",
                    "Christian Philosophy",
                    "Civics",
                    "Civil Code Of The Philippines",
                    "Civil Code Of The Philippines: Third Edition",
                    "Close Up",
                    "Clothes, Clues And Careers",
                    "Communication Arts And Skills",
                    "Complexity, Global Politics And National Security",
                    "Computer Fundamentals With Basic Programming",
                    "Conjugal Dictatorship",
                    "Connecting To Algebra",
                    "Consumer Mathematics",
                    "Contemporary Literature",
                    "Co-ordinated Science: The Earth",
                    "Co-ordinated Science: Chemistry",
                    "Co-ordinated Science: Physics",
                    "Correct Writing 6th Edition",
                    "Desired Learning Competencies",
                    "Discovering Food",
                    "Easter Chimes",
                    "Economics And Free Enterprise",
                    "Edukasyong Pangkatawan,",
                    "Effective School Desegregation",
                    "Effective Speech Communication",
                    "Electric Arc Welding",
                    "Elements Of Literature (Third Course)",
                    "Elements Of Radio",
                    "Elements Of Writing",
                    "Encounters",
                    "Encyclopedia Of Presidents: Franklin D. Roosevelt",
                    "Encyclopedia Of Presidents: Millard Fillmore",
                    "Energy And Physics",
                    "English Communication Skills In The New Millenium",
                    "English Writing and Skills",
                    "English Communication and Skills (4th Year)",
                    "English Communication Skills In The New Millennium",
                    "English For High School",
                    "English Grammar And Composition",
                    "English Skills With Readings",
                    "Environmental Science",
                    "Explorations in Literature",
                    "Exploring A Changing World",
                    "Exploring Construction Industry",
                    "Exploring Professional Cooking Revised",
                    "Fact Finder: Hunting for Treasures",
                    "Fact Finder: Looking For Fossil",
                    "Fact Finder: Space Diary",
                    "Fast As The Wind",
                    "Finding My Way",
                    "Focus On Earth Science",
                    "Focus On Physical Science",
                    "Footing the Bill for Cleanups",
                    "For Men Only",
                    "Forms In English Literature",
                    "Fundamentals Of Carpentry",
                    "Fundamentals Of Selling",
                    "Gamiting Filipino Pagbasa At Komposisyon",
                    "Gathering Sunbeans",
                    "Gems In Philippine Literature",
                    "General Chemistry",
                    "General Industrial Education",
                    "General Recordkeeping 9th Edition",
                    "Geometry",
                    "Geometry: Application And Connections",
                    "Geometry: Tools For A Changing World",
                    "Glencoe English",
                    "Good Health For You",
                    "Grammar And Composition",
                    "Grammar And Composition Teacher's Edition",
                    "Greff Shorthand For The Electronic Office",
                    "Gregg Dictation",
                    "Grob Basic Electronics",
                    "Growing Up Caring",
                    "Guide To Economics For Filipinos",
                    "Health Social Studies Texas Past To Present",
                    "Health Science",
                    "Health: A Guide To Wellness",
                    "Health: A Way Of life",
                    "Health English Teacher's Edition",
                    "Hiyas Ng Lahi II",
                    "Hiyas Ng Lahi IV",
                    "Holt School Mathematics",
                    "Holt Science",
                    "Home Economics And Industrial Technology I",
                    "Home Economics IV",
                    "Houghton Mifflin: English",
                    "Houghton Mifflin: Spelling",
                    "Houghton Mifflin: Mathematics California Edition",
                    "I Touched The Sun",
                    "Ibong Adarna",
                    "Ikatlong Sangwikaan",
                    "Industrial Arts: Wood Working",
                    "Integrated Mathematics",
                    "Introduction To Business",
                    "Introduction To Psychology",
                    "Introductory Analysis",
                    "Introductory Statistics",
                    "Keystone",
                    "Invitation to Economics",
                    "Land and People: A World Geography",
                    "Language In Literature: Afro-Asian",
                    "Language In Literature: Philippine Literature",
                    "Language: Skills And Use",
                    "Larson Hostetler Calculus Third Edition",
                    "Leaving Footprints",
                    "Literature (Purple Level)",
                    "Literature Green Level",
                    "Living Economics (Filipino Edisyon)",
                    "Living Literature",
                    "Living Literature: Our World Today",
                    "Macmillan English",
                    "Make The Connection",
                    "Makers Of American History",
                    "Manual For Youth Development And Citizen Army",
                    "MAPEH II",
                    "MAPEH III",
                    "MAPEH In Action I",
                    "Mathematics",
                    "Mathematics Applications And Connections Course 3",
                    "Maturity: Growing Up Strong",
                    "Microsoft Office 2010",
                    "Modern English In Action",
                    "Modern Health",
                    "Modern Radio Production",
                    "Modern Radio Physiology",
                    "Monetary Anarchy",
                    "Moral And Educational Orientation For Filipinos",
                    "Music Centinnial Edition",
                    "Music, Arts, Physical Education And Health III",
                    "New Science Library: Electricity, Magnetism",
                    "Of, By And For The People",
                    "On Line T.L.E. II",
                    "On the Horizon",
                    "PASCAL",
                    "Patterns In Literature",
                    "Philippines Folk Dance Volume One",
                    "Philippines Folk Dance Volume Two",
                    "Philippines Folk Dance Volume Three",
                    "Philippines Folk Dance Volume Four",
                    "Philippines Folk Dance Volume Five",
                    "Philippines Folk Dance Volume Six",
                    "Phillipine Folk Fiction And Tales",
                    "Physical Edukasyon, Health And Music III",
                    "Pluma: Wika At Panitikan",
                    "Prentice Hall: Writing And Grammar",
                    "Prentice Hall: Geometry",
                    "Prentice Hall: Literature",
                    "Prentice Hall: Mathematics California Pre-Algebra",
                    "Not Found", "Not Found 2", "Not Found 3"};

            bookResult.setText(classes[position]);

//            String string = "";
//            for(int i = 0; i < classes.length; i++){
//                string += String.format("%s: %.1f%%\n", classes[i], resultBook[i] * 100);
//            }
//
//            bookPercentageResult.setText(string);
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK){
            Bitmap image = (Bitmap)  data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension , dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);

            scanBookSearch = bookResult.getText().toString().trim();

            Intent i = new Intent(this, ScanBooksSuccess.class);
            i.putExtra("scanBookSearch", scanBookSearch);
            startActivity(i);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//                Bitmap image = result.getBitmap();
////                Bitmap image = (Bitmap)  data.getExtras().get("data");
//                int dimension = Math.min(image.getWidth(), image.getHeight());
//                image = ThumbnailUtils.extractThumbnail(image, dimension , dimension);
//                imageView.setImageBitmap(image);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
//
//                scanBookSearch = bookResult.getText().toString().trim();
//
//                Intent i = new Intent(this, ScanBooksSuccess.class);
//                i.putExtra("scanBookSearch", scanBookSearch);
//                startActivity(i);
//
////                imageUri = result.getUri();
////                Picasso.with(this).load(imageUri).into(imageView);
////                imageView.setImageURI(imageUri);
//            }
//        }
//    }
}