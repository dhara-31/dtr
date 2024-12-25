package com.si_charginganimation.nilesh_charginganimation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.si_charginganimation.nilesh_charginganimation.R;

import java.util.ArrayList;


public class MultiColorCAAdapter extends RecyclerView.Adapter<MultiColorCAAdapter.ViewHolder> {
    Context context;
    int[] fList;
    private int fPos = 1000;
    OnItemClickListenerS onItemClickListenerS;
    int aThemeColor;
    ArrayList<String> colorMultiList;
    public MultiColorCAAdapter(Context context, int[] fontList, int themeColor, ArrayList<String> colorMultiList) {
        this.context = context;
        this.fList = fontList;
        this.aThemeColor = themeColor;
        this.colorMultiList=colorMultiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_font_color_layot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.clColor.setBackgroundColor(fList[position]);

        if (colorMultiList.contains(String.valueOf(fList[position]))) {
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_color2);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, aThemeColor);
            holder.clBg.setBackground(wrappedDrawable);
         } else {
            holder.clBg.setBackground(null);
         }
        holder.clColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fPos = position;
                onItemClickListenerS.onItemClick1(view, fList[position], position);


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

    public void setNewMulColor(ArrayList<String> colorMultiList) {
        this.colorMultiList=colorMultiList;
        notifyDataSetChanged();
    }


    public interface OnItemClickListenerS {

        void onItemClick1(View view, int i, int position);
    }

    @Override
    public int getItemCount() {
        return fList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clBg, clColor;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clColor = itemView.findViewById(R.id.clColor);
            clBg = itemView.findViewById(R.id.clColorBg);
            cardView = itemView.findViewById(R.id.cardView);

            if(Build.VERSION.SDK_INT<=27) {
                float  radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,  context.getResources().getDisplayMetrics());

                cardView.setRadius(radius);
            }else {
                float  radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120f,  context.getResources().getDisplayMetrics());
                cardView.setRadius(radius);
            }
        }
    }
}
