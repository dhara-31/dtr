package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.si_charginganimation.nilesh_charginganimation.databinding.LevelItemLayoutBinding;


public class LavelCAAdapter extends RecyclerView.Adapter<LavelCAAdapter.ViewHolder> {

    Context context;
    private final String[] fontList;
    OnItemClickListenera onItemClickListener;
    private String font;

    int curPos = 0;

    public LavelCAAdapter(Context context, String[] appList, int lastPos) {
        this.fontList = appList;
        this.context = context;
        this.curPos = lastPos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder(LevelItemLayoutBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        holder.bind(position);


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
        void onItemClickS(int pos);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LevelItemLayoutBinding binding;

        public ViewHolder(@NonNull LevelItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {

            if (curPos == position) {
                binding.radio.setChecked(true);
            } else {
                binding.radio.setChecked(false);
            }

            binding.tvText.setText(fontList[position]);


            binding.cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!binding.radio.isChecked()) {
                        binding.radio.setChecked(true);
                        onItemClickListener.onItemClickS(position);

                        curPos = position;
                        notifyDataSetChanged();


                    }
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


