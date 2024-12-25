package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.model.TimeUsedAU;
import com.si_charginganimation.nilesh_charginganimation.model.UtilsAU;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AuUsesAdapter extends RecyclerView.Adapter<AuUsesAdapter.ViewHolder> {
    private Context contex;
    private ArrayList<TimeUsedAU> timeUsedArrayList = new ArrayList<>();
    ShCAPreference shCAPreference;

    public AuUsesAdapter(Context cc, ArrayList<TimeUsedAU> btUsesAsdapter) {
        this.contex = cc;
        this.timeUsedArrayList = btUsesAsdapter;
        shCAPreference = new ShCAPreference(contex);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contex).inflate(R.layout.au_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        TimeUsedAU timeUsed = timeUsedArrayList.get(position);

        holder.imageView.setImageDrawable(timeUsed.getIconApp());
        holder.tvAppName.setText(timeUsed.getAppName());
        holder.tvTime.setText(UtilsAU.convertTimes((int) timeUsed.getTotalTime()));
        holder.tvTime.setTextColor(getColorTheme());


    }


    @Override
    public int getItemCount() {
        return timeUsedArrayList.size();
    }

    public void setNEwData(ArrayList<TimeUsedAU> timeUsedArrayList) {
        this.timeUsedArrayList = timeUsedArrayList;
        notifyDataSetChanged();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvAppName, tvTime;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivLogo);
            tvAppName = itemView.findViewById(R.id.tvAppName);
            tvTime = itemView.findViewById(R.id.tvTime);

            tvTime.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvTime.setSelected(true);
            tvTime.setSingleLine(true);
        }
    }

    private int getColorTheme() {
        int cc = contex.getResources().getColor(R.color.th_4);
        if (shCAPreference.getThemeType() == 1) {
            cc = contex.getResources().getColor(R.color.th_1);
        } else if (shCAPreference.getThemeType() == 2) {
            cc = contex.getResources().getColor(R.color.th_2);
        } else if (shCAPreference.getThemeType() == 3) {
            cc = contex.getResources().getColor(R.color.th_3);
        } else if (shCAPreference.getThemeType() == 4) {
            cc = contex.getResources().getColor(R.color.th_4);
        }
        return cc;
    }

    private void setThem() {


    }
}
