package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.si_charginganimation.nilesh_charginganimation.AnimCAApi1.CADatum;
import com.si_charginganimation.nilesh_charginganimation.databinding.AdapterAnimHorVideoListBinding;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


import java.io.File;
import java.util.ArrayList;

public class AnimHorVideoCAAdapter extends RecyclerView.Adapter<AnimHorVideoCAAdapter.ViewHolder> {

    Context context;
    ArrayList<CADatum> adImageList;
    OnItemClickListenerS onItemClickListenerS;
    private ShCAPreference shCAPreference;

    public AnimHorVideoCAAdapter(Context context, ArrayList<CADatum> appList) {
        this.adImageList = appList;
        this.context = context;
        shCAPreference =new ShCAPreference(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder(AdapterAnimHorVideoListBinding.inflate(LayoutInflater.from(context)));
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

    public void setNewData(ArrayList<CADatum> imageList) {

        this.adImageList = imageList;
        notifyDataSetChanged();
    }
    public void OnItemClickListenerS(OnItemClickListenerS onItemClickListener) {
        this.onItemClickListenerS = (OnItemClickListenerS) onItemClickListener;
    }

    public interface OnItemClickListenerS {

        void onItemClick1(View view, CADatum datum);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AdapterAnimHorVideoListBinding binding;

        public ViewHolder(@NonNull AdapterAnimHorVideoListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {

            Glide.with(context).load(adImageList.get(position).getThumbnailUrl()).into(binding.image);

            binding.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListenerS.onItemClick1(v,adImageList.get(position));
                }
            });
            if(adImageList.size()-1==position){
                binding.cvItem2.setVisibility(View.VISIBLE);
            }else {
                binding.cvItem2.setVisibility(View.GONE);
            }
            if(check(adImageList.get(position))){
                binding.ivSelcted.setVisibility(View.VISIBLE);
            }else {
                binding.ivSelcted.setVisibility(View.GONE);
            }



        }

    }

    private boolean check(CADatum CADatum) {
        boolean acac=false;
        if (shCAPreference.getType_ca().equals("anim_video")){
            String fileName = URLUtil.guessFileName(CADatum.getResizedUrl(), null, null);

            File file = new File(context.getFilesDir(), "bca");

            File videoPathCheck = new File(file, "low_resize_" + fileName);
            if(videoPathCheck.exists())
            {
                if (shCAPreference.getVideoUri_ca() != null) {
                    if (videoPathCheck.getAbsolutePath().equals(shCAPreference.getVideoUri_ca())) {

                        acac=true;
                    }
                }
            }
            if(!acac){
                String fileName2 = URLUtil.guessFileName(CADatum.getOriginalUrl(), null, null);

                File file2 = new File(context.getFilesDir(), "bca");

                File videoPathCheck2 = new File(file2, fileName2);
                if(videoPathCheck2.exists())
                {
                    if (shCAPreference.getVideoUri_ca() != null) {
                        if (videoPathCheck2.getAbsolutePath().equals(shCAPreference.getVideoUri_ca())) {

                            acac=true;
                        }
                    }
                }
            }
        }


        return acac;


    }
}


