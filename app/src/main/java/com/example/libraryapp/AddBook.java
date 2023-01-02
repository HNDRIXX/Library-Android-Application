package com.example.libraryapp;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddBook extends AppCompatActivity {

    ProgressBar progressBarCircle;
    ImageView progressBarBackground;
    TextView progressBarText;
    RelativeLayout addNewBookLayout;

    //widgets
    Button btnSubmit;
    ImageView imageView;
    EditText bookTitleEdt, bookAuthorEdt,bookColNumberEdt,bookPublisherEdt, bookCopiesEdt;
    Spinner bookCategorySpn;
    MaterialButton backbutton;

    //vars
    DatabaseReference reference = FirebaseDatabase.getInstance("https://libraryapp-a10b1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Book Info");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Uri imageUri;
    String bookTitle;
    String bookAuthor;
    String bookColNumber;
    String bookPublisher;
    String bookCopies;
    String bookCategory;
    boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        getSupportActionBar().hide();

        MaterialButton backbutton = findViewById(R.id.backbutton);

        btnSubmit = findViewById(R.id.btnSubmit);
        imageView = findViewById(R.id.imageView);
        bookTitleEdt = findViewById(R.id.bookTitleEdt);
        bookAuthorEdt = findViewById(R.id.bookAuthorEdt);
        bookColNumberEdt = findViewById(R.id.bookColNumberEdt);
        bookPublisherEdt = findViewById(R.id.bookPublisherEdt);
        bookCategorySpn = findViewById(R.id.bookCategorySpn);
        bookCopiesEdt = findViewById(R.id.bookCopiesEdt);

        progressBarCircle = findViewById(R.id.progressBarCircle);
        progressBarBackground = findViewById(R.id.progressBarBackground);
        progressBarText = findViewById(R.id.progressBarText);

        addNewBookLayout = findViewById(R.id.addNewBookLayout);

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(AddBook.this);
//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, 2);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addNewBookLayout.getWindowToken(), 0);

                if (imageUri != null){
                    progressBarCircle.setVisibility(View.VISIBLE);
                    progressBarBackground.setVisibility(View.VISIBLE);
                    progressBarText.setVisibility(View.VISIBLE);

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    uploadToFirebase(imageUri);
                }else{
                    progressBarCircle.setVisibility(View.GONE);
                    progressBarBackground.setVisibility(View.GONE);
                    progressBarText.setVisibility(View.GONE);

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toast.makeText(AddBook.this,"Please select image.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadToFirebase(Uri imageUri) {
        Bitmap bmp = null;

        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] fileInBytes = baos.toByteArray();

        final StorageReference fileRef = storageReference.child(System.currentTimeMillis()+".jpg");

        fileRef.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String bookTitleToUpperCase = bookTitleEdt.getText().toString().trim();

                        String[] strArray = bookTitleToUpperCase.split(" ");
                        StringBuilder builderBookTitle = new StringBuilder();

                        for (String s : strArray) {
                            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                            builderBookTitle.append(cap + " ");
                        }

                        bookTitle = builderBookTitle.toString();

                        String bookAuthorToUpperCase = bookAuthorEdt.getText().toString().trim();

                        String[] strArrayBookAuthor = bookAuthorToUpperCase.split(" ");
                        StringBuilder builderBookAuthor = new StringBuilder();

                        for (String s : strArrayBookAuthor) {
                            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                            builderBookAuthor.append(cap + " ");
                        }

                        bookAuthor = builderBookAuthor.toString();

                        bookColNumber = bookColNumberEdt.getText().toString().trim();
                        bookCopies = bookCopiesEdt.getText().toString().trim();
                        bookPublisher = bookPublisherEdt.getText().toString().trim();
                        bookCategory = bookCategorySpn.getSelectedItem().toString();

                        BookInfo model = new BookInfo (uri.toString(), bookTitle, bookAuthor, bookColNumber, bookCopies, bookPublisher, bookCategory );
                        String modelId = reference.push().getKey();
                        reference.child(modelId).setValue(model);

                        progressBarCircle.setVisibility(View.GONE);
                        progressBarBackground.setVisibility(View.GONE);
                        progressBarText.setVisibility(View.GONE);

                        Toast.makeText(AddBook.this,"New Book Added!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddBook.this, ManageBooks.class);
                        startActivity(intent);

//                        Toast.makeText(AddBook.this,"Uploaded FALSE. " + status,Toast.LENGTH_SHORT).show();

                        //setting the image placeholder to default after image is uploaded instead of image uploaded
                        imageView.setImageResource(R.drawable.icon_bookimage);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddBook.this,"Uploaded Failed.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Picasso.with(this).load(imageUri).into(imageView);
                imageView.setImageURI(imageUri);
            }
        }
    }

}