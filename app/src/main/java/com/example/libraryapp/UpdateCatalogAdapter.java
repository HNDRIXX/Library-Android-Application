package com.example.libraryapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class UpdateCatalogAdapter extends FirebaseRecyclerAdapter<SearchModel, UpdateCatalogAdapter.myViewHolder> {

    private  ProgressBar progressBar;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public UpdateCatalogAdapter(@NonNull FirebaseRecyclerOptions<SearchModel> options) {
        super(options);
    }

    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder,  int position, @NonNull SearchModel model) {
        holder.bookTitle.setText(model.getBookTitle());
        holder.bookAuthor.setText(model.getBookAuthor());
        holder.bookColNumber.setText(model.getBookColNumber());
        holder.bookPublisher.setText(model.getBookPublisher());
        holder.bookCopies.setText(model.getBookCopies());
        holder.bookCategory.setText(model.getBookCategory());

        Glide.with(holder.imageView.getContext())
                .load(model.getImageUrl())
                .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = holder.itemView.getContext();

                String bookURL = model.getImageUrl();
                String value = "activated";

                Intent intent = new Intent("custom-message");
                intent.putExtra("bookURL", bookURL);
                intent.putExtra("value", value);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

//                Intent intent = new Intent(view.getContext(), UpdateCatalog.class);
//
//                String value = "1";
//                String key = getRef(position).getKey();
//                String imageURL = model.getImageUrl();
//
//                intent.putExtra("value", value);
//                intent.putExtra("key", key);
//                intent.putExtra("bookImageView", imageURL);
//
//                view.getContext().startActivity(intent);
//
//
//                Glide.with(holder.imageView.getContext())
//                        .load(imageURL)
//                        .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
//                        .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
//                        .into(holder.imageViewBookZoom);
//
//                holder.imageViewBookZoom.setVisibility(View.VISIBLE);

            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1200)
                        .create();
                //dialogPlus.show();

                View view = dialogPlus.getHolderView();

                AppCompatButton exitButton = view.findViewById(R.id.exitButton);
                EditText bookTitle = view.findViewById(R.id.TxtbookTitle);
                EditText bookAuthor = view.findViewById(R.id.TxtbookAuthor);
                EditText bookPublisher = view.findViewById(R.id.TxtPublisher);
                EditText bookColNumber = view.findViewById(R.id.TxtColNum);
                EditText bookCategory = view.findViewById(R.id.TxtCategory);
                EditText bookCopies = view.findViewById(R.id.TxtCopies);
                EditText bookImage = view.findViewById(R.id.TxtImgUrl);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                bookTitle.setText(model.getBookTitle());
                bookAuthor.setText(model.getBookAuthor());
                bookPublisher.setText(model.getBookPublisher());
                bookColNumber.setText(model.getBookColNumber());
                bookCategory.setText(model.getBookCategory());
                bookCopies.setText(model.getBookCopies());
                bookImage.setText(model.getImageUrl());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("bookTitle",bookTitle.getText().toString());
                        map.put("bookAuthor",bookAuthor.getText().toString());
                        map.put("bookPublisher",bookPublisher.getText().toString());
                        map.put("bookColNumber",bookColNumber.getText().toString());
                        map.put("bookCategory",bookCategory.getText().toString());
                        map.put("bookCopies",bookCopies.getText().toString());
                        map.put("imageUrl",bookImage.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Book Info")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.bookTitle.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.bookTitle.getContext(), "Error While Updating", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.bookTitle.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can't be Undo.");

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.bookTitle.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1200)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText bookImage = view.findViewById(R.id.TxtImgUrl);
                bookImage.setText(model.getImageUrl());
                String storageUrl = bookImage.getText().toString();

                StorageReference storageReference = FirebaseStorage.getInstance("gs://libraryapp-a10b1.appspot.com/").getReferenceFromUrl(storageUrl);
                String imageUrl = storageReference.getName();
                StorageReference storageReferenceDelete = FirebaseStorage.getInstance("gs://libraryapp-a10b1.appspot.com/").getReference().child(imageUrl);

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storageReferenceDelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });

                        FirebaseDatabase.getInstance().getReference().child("Book Info")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.bookTitle.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_catalog_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageViewBookZoom;
        TextView bookTitle, bookAuthor, bookColNumber, bookPublisher, bookCategory, bookCopies;
        Button btnEdit, btnDelete;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            bookTitle = (TextView) itemView.findViewById(R.id.bookTitle);
            bookAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);
            bookColNumber = (TextView) itemView.findViewById(R.id.bookColNumber);
            bookPublisher = (TextView) itemView.findViewById(R.id.bookPublisher);
            bookCopies = (TextView) itemView.findViewById(R.id.bookCopies);
            bookCategory = (TextView) itemView.findViewById(R.id.bookCategory);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

            imageViewBookZoom = (ImageView) itemView.findViewById(R.id.imageViewBookZoom);

        }
    }

    @Override
    public void onDataChanged() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}


