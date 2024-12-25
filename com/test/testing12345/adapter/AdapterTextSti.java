package com.test.testing12345.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.custom.CustomKeyBoard;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterTextSti extends RecyclerView.Adapter<AdapterTextSti.ViewHolder> {

    Context context;
    ArrayList<Integer> listOfGif;
    OnItemClickListener1 onItemClickListener1;
    public AdapterTextSti(CustomKeyBoard customKeyBoard, ArrayList<Integer> gifList) {
        this.context = customKeyBoard;
        this.listOfGif = gifList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_sti_key_item_layout, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterTextSti.ViewHolder holder, int position) {

        holder.tvTextSti.setImageResource(listOfGif.get(position));
        holder.tvTextSti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemClickListener1.onItemClick1(listOfGif.get(position),holder.tvTextSti);
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
    ImageView tvTextSti;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTextSti = itemView.findViewById(R.id.tvTextSti);
        }
    }
    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener1) {
        this.onItemClickListener1 = (OnItemClickListener1) onItemClickListener1;
    }
    public interface OnItemClickListener1 {
        void onItemClick1(Integer pos, ImageView v) throws IOException;
    }
}
