package com.test.testing12345.frgment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.activity.ThemeCreateCsActivity;
import com.test.testing12345.adsclass.StoreageCkPref;
import com.test.testing12345.keyboard.KeyboardCkTheme;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KeyboardThemeFragmentCk extends Fragment {

    ImageView ivDisplay;
    RecyclerView recyclerViewTheme;
    public static ThemeAdapter themeAdapter;
    public static     int borderPos2 =0;
    StoreageCkPref storeageCkPref;

    public static void newTheme() {
        themeAdapter.newTheme();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frgment_keyboard_theme,
                container, false);
        storeageCkPref = new StoreageCkPref(getContext());
        recyclerViewTheme = rootView.findViewById(R.id.recyclerThemeList);
        ivDisplay = rootView.findViewById(R.id.ivDisplay);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewTheme.setLayoutManager(gridLayoutManager);

        setDisplayTheme();
        setDataInRV();
        return rootView;
    }

    private void setDisplayTheme() {

        storeageCkPref.setTempTHEME_ID(0);
        ivDisplay.setImageResource(getTheme(0));
    }


    private void setDataInRV() {
        ArrayList<Integer> stringArrayList = new ArrayList<>();
        stringArrayList.add(R.drawable.t1);
        stringArrayList.add(R.drawable.t2);
        stringArrayList.add(R.drawable.t3);
        stringArrayList.add(R.drawable.t4);
        stringArrayList.add(R.drawable.t5);
        stringArrayList.add(R.drawable.t6);


        themeAdapter = new ThemeAdapter(getContext(), stringArrayList);
        recyclerViewTheme.setAdapter(themeAdapter);
    }


    private class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

        Context context;
        int tempPos= storeageCkPref.getTHEME_ID();

        ArrayList<Integer> adArrayList;

        public ThemeAdapter(Context context, ArrayList<Integer> stringArrayList) {

            this.context = context;
            this.adArrayList = stringArrayList;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item_layout2, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ThemeAdapter.ViewHolder holder, int position) {
            holder.ivTheme.setImageResource(adArrayList.get(position));


            holder.ivTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   ivDisplay.setImageResource(getTheme(position));
                       storeageCkPref.setTempTHEME_ID(position);

                    borderPos2 =position;
                    ThemeCreateCsActivity.setB();
                    notifyDataSetChanged();
                }
            });
            final int themeId = KeyboardCkTheme.getKeyboardTheme(getContext()).mThemeId;

            if(themeId<6) {
                if (tempPos == position) {
                    holder.ivSelect.setVisibility(View.VISIBLE);
                } else {
                    holder.ivSelect.setVisibility(View.GONE);
                }
            }else {
                holder.ivSelect.setVisibility(View.GONE);

            }

            if(borderPos2 ==position) {
                holder.cvBg.setBackground(getResources().getDrawable(R.drawable.dr_bg2_border));
            }else {
                holder.cvBg.setBackground(null);
            }


        }


        @Override
        public int getItemCount() {
            return adArrayList.size();
        }

        public void newTheme() {
            tempPos= storeageCkPref.getTempTHEME_ID();
            notifyDataSetChanged();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivTheme;
            ImageView ivSelect;
            ConstraintLayout cvBg;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                ivTheme = itemView.findViewById(R.id.ivTheme);
                ivSelect = itemView.findViewById(R.id.ivSelect);
                cvBg = itemView.findViewById(R.id.cvBg);

            }
        }
    }
    private int getTheme(int position) {
        int drawable= R.drawable.keyboard_1;
        if(position==0){
            drawable = R.drawable.keyboard_1;
        }else if(position==1){
            drawable = R.drawable.keyboard_2;
        }else if(position==2){
            drawable = R.drawable.keyboard_3;
        }else if(position==3){
            drawable = R.drawable.keyboard_4;
        }else if(position==4){
            drawable = R.drawable.keyboard_5;
        }else if(position==5){
            drawable = R.drawable.keyboard_6;
        }
        return drawable;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        storeageCkPref.setTempBitmapS(null);
    }
}
