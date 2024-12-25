package com.test.testing12345.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterAddLanguage extends RecyclerView.Adapter<AdapterAddLanguage.ViewHolder> {

    Context context;
    ArrayList<String> listOfLan;
    OnItemClickListener1 onItemClickListener1;
    public AdapterAddLanguage(Context languageSelectActivity, ArrayList<String> lanList) {
        this.context = languageSelectActivity;
        this.listOfLan = lanList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lan_remove_item_layout, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterAddLanguage.ViewHolder holder, int position) {

        holder.tvLan.setText(listOfLan.get(position));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemClickListener1.onItemClick1(listOfLan.get(position),v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfLan.size();
    }

    public void setNewData(ArrayList<String> arrayListLanguage) {
        this.listOfLan = arrayListLanguage;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView btnAdd;
    TextView tvLan;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvLan = itemView.findViewById(R.id.tvLan);
            btnAdd = itemView.findViewById(R.id.btnRemove);
            btnAdd.setImageResource(R.drawable.ck_lan_add);
        }
    }
    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener1) {
        this.onItemClickListener1 = (OnItemClickListener1) onItemClickListener1;
    }
    public interface OnItemClickListener1 {
        void onItemClick1(String pos, View v) throws IOException;
    }
}
