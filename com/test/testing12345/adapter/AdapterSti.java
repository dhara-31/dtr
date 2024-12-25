package com.test.testing12345.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.penfeizhou.animation.loader.ResourceStreamLoader;
import com.github.penfeizhou.animation.webp.WebPDrawable;
import com.test.testing12345.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterSti extends RecyclerView.Adapter<AdapterSti.ViewHolder> {

    Context context;
    ArrayList<Integer> listOfGif;
    OnItemClickListener1 onItemClickListener1;
    public AdapterSti(Context customKeyBoard, ArrayList<Integer> gifList) {
        this.context = customKeyBoard;
        this.listOfGif = gifList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sti_item_layout, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterSti.ViewHolder holder, int position) {
        ResourceStreamLoader resourceLoader = new ResourceStreamLoader(context, listOfGif.get(position));
        WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);

        holder.ivSti.setImageDrawable(webpDrawable);


        holder.ivSti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemClickListener1.onItemClick1(listOfGif.get(position),holder.ivSti);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfGif.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView ivSti;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ivSti = itemView.findViewById(R.id.ivGif);
        }
    }
    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener1) {
        this.onItemClickListener1 = (OnItemClickListener1) onItemClickListener1;
    }
    public interface OnItemClickListener1 {
        void onItemClick1(Integer pos, ImageView v) throws IOException;
    }
}
