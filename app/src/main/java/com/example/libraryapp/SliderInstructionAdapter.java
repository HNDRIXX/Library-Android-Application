package com.example.libraryapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SliderInstructionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context c;
    private List<Integer> list;

    public SliderInstructionAdapter(Context c, List<Integer> list){
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new ViewHolder(LayoutInflater.from(c).inflate(R.layout.instructionslider, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bindData(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView sliderInstruction;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sliderInstruction = itemView.findViewById(R.id.sliderInstruction);

        }

        void bindData(Integer image){
            Glide.with(c)
                .load(image).override(600, 600).into(sliderInstruction);


        }
    }
}
