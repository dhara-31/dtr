package com.test.testing12345.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.test.testing12345.R;
import com.test.testing12345.custom.CustomKeyBoard;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterGif extends RecyclerView.Adapter<AdapterGif.ViewHolder> {

    Context context;
    ArrayList<Integer> listOfGif;
    OnItemClickListener1 onItemClickListener1;
    public AdapterGif(CustomKeyBoard customKeyBoard, ArrayList<Integer> gifList) {
        this.context = customKeyBoard;
        this.listOfGif = gifList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gif_item_layout, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterGif.ViewHolder holder, int position) {
       //  holder.ivGif.setImageResource(listOfGif.get(position));
//        ResourceStreamLoader resourceLoader = new ResourceStreamLoader(context, R.raw.sti1);
//        WebPDrawable webpDrawable = new WebPDrawable(resourceLoader);
//        holder.ivGif.setImageDrawable(webpDrawable);
        Glide.with(context).asGif().load(listOfGif.get(position)).addListener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
//                float width = resource.getIntrinsicWidth();
//                float height = resource.getIntrinsicHeight();
//
//                img.setLayoutParams(new LinearLayout.LayoutParams((int) ((width / height) * 300), 300));

                return false;
            }
        }).into( holder.ivGif);

        holder.ivGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemClickListener1.onItemClick1(listOfGif.get(position),v);
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
    ImageView ivGif;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ivGif = itemView.findViewById(R.id.ivGif);
        }
    }
    public void setOnItemClickListener(OnItemClickListener1 onItemClickListener1) {
        this.onItemClickListener1 = (OnItemClickListener1) onItemClickListener1;
    }
    public interface OnItemClickListener1 {
        void onItemClick1(Integer pos, View v) throws IOException;
    }
}
