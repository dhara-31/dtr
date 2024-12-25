package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.FontItemLayoutBinding;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


public class FontCAAdapter extends RecyclerView.Adapter<FontCAAdapter.ViewHolder> {

    Context context;
    private final String[] fontList;
    OnItemClickListenera onItemClickListener;
    ShCAPreference shCAPreference;
    String font;
    int aThemeColor;

    public FontCAAdapter(Context context, String[] appList, int thc) {
        this.fontList = appList;
        this.context = context;
        this.aThemeColor = thc;
        shCAPreference = new ShCAPreference(context);
        this.font = "cbaShPreference.getFontStyle()";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder(FontItemLayoutBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        holder.bind(position);

        if (font.equals(fontList[position])) {
            holder.binding.tvText.setTextColor(aThemeColor);
        } else {
            holder.binding.tvText.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return fontList.length;
    }

    public void
    setOnItemClickListener(OnItemClickListenera onItemClickListener) {
        this.onItemClickListener = (OnItemClickListenera) onItemClickListener;
    }


    public interface OnItemClickListenera {
        void onItemClickS(String pos);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FontItemLayoutBinding binding;

        public ViewHolder(@NonNull FontItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {
            binding.tvText.setTypeface(Typeface.createFromAsset(context.getAssets(), fontList[position]));

            binding.tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickS(fontList[position]);
                    font = fontList[position];
                    notifyDataSetChanged();
                }
            });
            binding.cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickS(fontList[position]);
                    font = fontList[position];
                    notifyDataSetChanged();
                }
            });
        }

    }
}


