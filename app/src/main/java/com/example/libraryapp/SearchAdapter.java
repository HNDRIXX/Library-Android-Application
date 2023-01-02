package com.example.libraryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SearchAdapter extends FirebaseRecyclerAdapter<SearchModel,SearchAdapter.myViewHolder> {

    private ProgressBar progressBar;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public SearchAdapter(@NonNull FirebaseRecyclerOptions<SearchModel> options) {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchBooksClick.class);
                intent.putExtra("Book Image",model.getImageUrl());
                intent.putExtra("Title",model.getBookTitle());
                intent.putExtra("Author",model.getBookAuthor());
                intent.putExtra("Category",model.getBookCategory());
                intent.putExtra("Copies", model.getBookCopies());
                intent.putExtra("Publisher",model.getBookPublisher());
                intent.putExtra("Colnum",model.getBookColNumber());
                view.getContext().startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView bookTitle, bookAuthor, bookColNumber, bookPublisher, bookCopies, bookCategory;
        Button borrowbtn, locationbtn;


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


        }
    }

    @Override
    public void onDataChanged() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}


