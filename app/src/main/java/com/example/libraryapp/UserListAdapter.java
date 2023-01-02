package com.example.libraryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class UserListAdapter extends FirebaseRecyclerAdapter<UserList, UserListAdapter.myViewHolder> {
//    ArrayList<UserList> list;
//
//    public UserListAdapter(Context context, ArrayList<UserList> list) {
//        this.context = context;
//        this.list = list;
//    }

    public UserListAdapter(@NonNull FirebaseRecyclerOptions<UserList> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.myViewHolder holder, int position, @NonNull UserList userList) {
//        UserList user = list.get(position);

        Context context = holder.itemView.getContext();

        holder.memIDContent.setText(userList.getMemID());
        holder.fullnameContent.setText(userList.getFullname());
        holder.emailContent.setText(userList.getEmail());
        holder.roleContent.setText(userList.getRole());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "Nothing to display", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public UserListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem, parent, false);
        return new UserListAdapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView fullnameContent, memIDContent, emailContent, roleContent;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            memIDContent = itemView.findViewById(R.id.memIDContent);
            fullnameContent = itemView.findViewById(R.id.fullnameContent);
            emailContent = itemView.findViewById(R.id.emailnContent);
            roleContent = itemView.findViewById(R.id.rolenContent);
        }
    }
}
