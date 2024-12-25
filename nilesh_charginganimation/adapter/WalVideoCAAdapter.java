package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.si_charginganimation.nilesh_charginganimation.databinding.AdapterWalListBinding;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi2.WallpaperCA;

import java.util.ArrayList;

public class WalVideoCAAdapter extends RecyclerView.Adapter<WalVideoCAAdapter.ViewHolder> {

    Context context;
    ArrayList<WallpaperCA> adImageList;
    OnItemClickListenerS onItemClickListenerS;

    public WalVideoCAAdapter(Context context, ArrayList<WallpaperCA> appList) {
        this.adImageList = appList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder(AdapterWalListBinding.inflate(LayoutInflater.from(context)));
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

    public void setNewData(ArrayList<WallpaperCA> imageList) {

        this.adImageList = imageList;
        notifyDataSetChanged();
    }

    public void OnItemClickListenerS(OnItemClickListenerS onItemClickListener) {
        this.onItemClickListenerS = (OnItemClickListenerS) onItemClickListener;
    }

    public interface OnItemClickListenerS {

        void onItemClick1(View view, WallpaperCA datum);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AdapterWalListBinding binding;

        public ViewHolder(@NonNull AdapterWalListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {

            Glide.with(context).load(adImageList.get(position).getThumbnail()).into(binding.image);

            binding.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListenerS.onItemClick1(v, adImageList.get(position));
                }
            });
        }

    }
}


