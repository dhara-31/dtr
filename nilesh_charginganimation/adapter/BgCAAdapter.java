package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.si_charginganimation.nilesh_charginganimation.R;

import java.util.ArrayList;


public class BgCAAdapter extends RecyclerView.Adapter<BgCAAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> abgList;
    private int fPos = 0;
    OnItemClickListenerS onItemClickListenerS;
    int aThemeColor;

    public BgCAAdapter(Context context, ArrayList<Integer> bgList, int themeColor, int pos) {
        this.context = context;
        this.fPos=pos;
        this.abgList = bgList;
        this.aThemeColor = themeColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_bg_image_layot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(context.getResources().getDrawable(abgList.get(position))).into(holder.ivBg);


        if(fPos==position){
            holder.clBorder.setBackground(context.getResources().getDrawable(R.drawable.bg_item));
        }else {
         holder.clBorder.setBackground(null);
        }


        holder.ivBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fPos=position;
                onItemClickListenerS.onItemClick1(v,abgList.get(position),position);
                notifyDataSetChanged();
            }
        });
    }

    public void OnItemClickListenerS(OnItemClickListenerS onItemClickListener) {
        this.onItemClickListenerS = (OnItemClickListenerS) onItemClickListener;
    }

    public void setPos() {
        fPos = 1000;
        notifyDataSetChanged();
    }


    public interface OnItemClickListenerS {

        void onItemClick1(View view, int i, int position);
    }

    @Override
    public int getItemCount() {
        return abgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clBorder;
        CardView cardView;
        ImageView ivBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clBorder = itemView.findViewById(R.id.clBorder);
            cardView = itemView.findViewById(R.id.cardView);
            ivBg = itemView.findViewById(R.id.ivBg);


        }
    }
}
