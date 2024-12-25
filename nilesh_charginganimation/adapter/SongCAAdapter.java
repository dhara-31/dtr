package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.SongItemLayoutBinding;
import com.si_charginganimation.nilesh_charginganimation.model.SongModel;

import java.io.File;
import java.util.ArrayList;


public class SongCAAdapter extends RecyclerView.Adapter<SongCAAdapter.ViewHolder> {

    private final int themeColor;
    Context context;
    ArrayList<SongModel> songList;
    OnItemClickListenera onItemClickListener;
    OnItemClickListeneraMusic onItemClickListeneraMusic;

    int curPos = 0;
    int songPos = 1010101;
    private boolean isPlaying = false;

    public SongCAAdapter(Context context, ArrayList<SongModel> appList, int lastPos, int thColor) {
        this.songList = appList;
        this.context = context;
        this.themeColor=thColor;
        this.curPos = lastPos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


      return new ViewHolder(SongItemLayoutBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void
    setOnItemClickListener(OnItemClickListenera onItemClickListener) {
        this.onItemClickListener = (OnItemClickListenera) onItemClickListener;
    }

    public void setPl(boolean b) {
        this.isPlaying = b;
        notifyDataSetChanged();
    }

    public void setNewS(ArrayList<SongModel> songList) {
        this.songList=songList;
        notifyDataSetChanged();
    }


    public interface OnItemClickListenera {
        void onItemClickS(int pos);

    }

    public void
    setOnItemClickListeneraMusic(OnItemClickListeneraMusic OnItemClickListeneraMusic) {
        this.onItemClickListeneraMusic = (OnItemClickListeneraMusic) OnItemClickListeneraMusic;
    }

    public interface OnItemClickListeneraMusic {
        void onItemClickS(int pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        SongItemLayoutBinding binding;

        public ViewHolder(@NonNull SongItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {
            File file = new File(songList.get(position).getSongPath());
            if (curPos == position) {
                binding.radio.setChecked(true);
            } else {
                binding.radio.setChecked(false);
            }
            if (position == 0) {
                binding.btPlayPause.setVisibility(View.GONE);
            } else {
                binding.btPlayPause.setVisibility(View.VISIBLE);
                if (songPos == position) {
                    if (isPlaying) {
                        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_pause_circle_outline_24);
                        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                        DrawableCompat.setTint(wrappedDrawable, themeColor);
                        binding.btPlayPause.setImageDrawable(wrappedDrawable);

                    } else {
                        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_play_circle_outline_24);
                        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                        DrawableCompat.setTint(wrappedDrawable, themeColor);
                        binding.btPlayPause.setImageDrawable(wrappedDrawable);
                    }

                } else {
                    Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_play_circle_outline_24);
                    Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                    DrawableCompat.setTint(wrappedDrawable, themeColor);
                    binding.btPlayPause.setImageDrawable(wrappedDrawable);
                }
            }


            binding.tvText.setText(file.getName());

            binding.btPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListeneraMusic.onItemClickS(position);
                    songPos = position;

                }
            });


            binding.radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.radio.isChecked()) {
                        onItemClickListener.onItemClickS(position);
                        curPos = position;
                        notifyDataSetChanged();

                    } else {
                        binding.radio.setChecked(true);
                    }
                }
            });
      }

    }
}


