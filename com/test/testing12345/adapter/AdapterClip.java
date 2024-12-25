package com.test.testing12345.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.custom.CustomKeyBoard;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterClip extends RecyclerView.Adapter<AdapterClip.ViewHolder> {

    Context context;
    ArrayList<String> listOfClip;
    OnItemClickListener1 onItemClickListener1;
    int textColor;
    public AdapterClip(CustomKeyBoard customKeyBoard, ArrayList<String> List, int textForThem) {
        this.context = customKeyBoard;
        this.listOfClip =  List;
        this.textColor = textForThem;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.clip_item_layout, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterClip.ViewHolder holder, int position) {
        holder.tvClip.setText(listOfClip.get(position));

        holder.tvClip.setTextColor(textColor);

        holder.tvClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemClickListener1.onItemClick1(listOfClip.get(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfClip.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvClip;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvClip = itemView.findViewById(R.id.tv);
        }
    }
    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener1) {
        this.onItemClickListener1 = (OnItemClickListener1) onItemClickListener1;
    }
    public interface OnItemClickListener1 {
        void onItemClick1(String pos) throws IOException;
    }
}
