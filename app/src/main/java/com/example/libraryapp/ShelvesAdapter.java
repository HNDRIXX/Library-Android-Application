package com.example.libraryapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ShelvesAdapter extends FirebaseRecyclerAdapter<BookTransaction, ShelvesAdapter.myViewHolder> {

    private ProgressBar progressBar;

    public ShelvesAdapter(@NonNull FirebaseRecyclerOptions<BookTransaction> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull BookTransaction bookTransaction) {

        Context context = holder.itemView.getContext();

        holder.transactionFlagView.setText(bookTransaction.getTransactionFlag());
        String get = holder.transactionFlagView.getText().toString().trim();

//        if(get.equals("1")){
//            holder.rootView.setLayoutParams(holder.params);
//        } else {
            holder.transactionCodeView.setText(bookTransaction.getTransactionCode());
            holder.bookTitleView.setText(bookTransaction.getBookTitle());

            if (get.equals("1")){
                holder.statusView.setText("Request Sent");
            }else if (get.equals("2")){
                holder.statusView.setText("Borrowed");
            }else if (get.equals("3")){
                holder.statusView.setText("Returned");
            }else if (get.equals("4")){
                holder.statusView.setText("Denied Request");
            }


//        }

        Glide.with(holder.imageView.getContext())
                .load(bookTransaction.getImageUrl())
                .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getRef(position).getKey();
                Intent intent = new Intent(view.getContext(), ShelvesClick.class);

                intent.putExtra("key", key);
                intent.putExtra("bookImage",bookTransaction.getImageUrl());
                intent.putExtra("bookTitle",bookTransaction.getBookTitle());
                intent.putExtra("transactionCode",bookTransaction.getTransactionCode());
                intent.putExtra("date",bookTransaction.getDate());

                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shelves_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleView, transactionCodeView, transactionFlagView, statusView;
        Context context;
        ImageView imageView;
        CardView.LayoutParams params;
        CardView rootView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

            params = new CardView.LayoutParams(0, 0);
            rootView = itemView.findViewById(R.id.rootView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            bookTitleView = (TextView) itemView.findViewById(R.id.bookTitleContent);
            transactionCodeView = (TextView) itemView.findViewById(R.id.transactionCodeContent);
            transactionFlagView = (TextView) itemView.findViewById(R.id.transactionFlagContent);
            statusView = (TextView) itemView.findViewById(R.id.statusContent);
        }
    }

    @Override
    public void onDataChanged() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}


