package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.si_charginganimation.nilesh_charginganimation.act.MediaPreviewAct;
import com.si_charginganimation.nilesh_charginganimation.databinding.AdapterImageListBinding;
import com.si_charginganimation.nilesh_charginganimation.fragment.DataModel;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


import java.util.ArrayList;

public class VideoCAAdapter extends RecyclerView.Adapter<VideoCAAdapter.ViewHolder> {

    Context context;
    ArrayList<DataModel> adImageList;
    private ShCAPreference shCAPreference;

    public VideoCAAdapter(Context context, ArrayList<DataModel> appList) {
        this.adImageList = appList;
        this.context = context;
        shCAPreference = new ShCAPreference(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterImageListBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return adImageList.size();
    }

    public void setNewData(ArrayList<DataModel> imageList) {
        this.adImageList = imageList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AdapterImageListBinding binding;

        public ViewHolder(@NonNull AdapterImageListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {

            Glide.with(context).load(adImageList.get(position).getFilePath()).into(binding.image);
            binding.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MediaPreviewAct.class);
                    intent.putExtra("type", "video");
                    intent.putExtra("path", adImageList.get(position).getFilePath());

                    GoChBetryNils.getInstance().showChBetryNilster((Activity) context, new GoChBetryNils.AChBetryNilInterface() {
                        @Override
                        public void aChBetryNilsCall() {
                            context.startActivity(intent);
                        }
                    });
                }
            });
            if (check(adImageList.get(position).getFilePath())) {
                binding.ivSelcted.setVisibility(View.VISIBLE);
            } else {
                binding.ivSelcted.setVisibility(View.GONE);
            }
        }

    }

    private boolean check(String filePath) {
        boolean acbc = false;
        if (shCAPreference.getType_ca() != null) {
            if (shCAPreference.getType_ca().equals("video")) {
                if (shCAPreference.getVideoUri_ca() != null) {
                    if (filePath.equals(shCAPreference.getVideoUri_ca())) {
                        acbc = true;
                    }
                }
            }
        }
        return acbc;
    }
}


