package com.example.libraryapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ReturnBooksAdapter extends FirebaseRecyclerAdapter<BookTransaction, ReturnBooksAdapter.myViewHolder> {

    private ProgressBar progressBar;

    public ReturnBooksAdapter(@NonNull FirebaseRecyclerOptions<BookTransaction> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull BookTransaction bookTransaction) {

        holder.transactionCodeView.setText(bookTransaction.getTransactionCode());
        holder.bookTitleView.setText(bookTransaction.getBookTitle());
        holder.fullNameView.setText(bookTransaction.getFullname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getRef(position).getKey();
                Intent intent = new Intent(view.getContext(), ReturnBooksClick.class);

                intent.putExtra("key", key);
                intent.putExtra("bookImage",bookTransaction.getImageUrl());
                intent.putExtra("bookTitle",bookTransaction.getBookTitle());
                intent.putExtra("fullname",bookTransaction.getFullname());
                intent.putExtra("transactionCode",bookTransaction.getTransactionCode());
                intent.putExtra("date",bookTransaction.getDate());

                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_and_return_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleView, fullNameView, transactionCodeView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

            bookTitleView = (TextView) itemView.findViewById(R.id.bookTitleContent);
            fullNameView = (TextView) itemView.findViewById(R.id.fullNameContent);
            transactionCodeView = (TextView) itemView.findViewById(R.id.transactionCodeContent);
        }
    }

    @Override
    public void onDataChanged() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}


