package com.test.testing12345.frgment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.activity.PhotoSelectCsActivity;
import com.test.testing12345.activity.ThemeCreateCsActivity;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.StoreageCkPref;
import com.test.testing12345.keyboard.KeyboardCkTheme;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class CustomThemeCkFragment extends Fragment {
    RecyclerView recyclerWallpaper;
    public static ThemeAdapter themeAdapter;
    public static ImageView ivDisplay;
    public static StoreageCkPref storeageCkPref;
    public static int borderPos = 1;

    public static void newTheme() {
        themeAdapter.newTheme();
    }

    public static Activity activity;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.frgment_custom_theme,
                container, false);
        activity = getActivity();

        storeageCkPref = new StoreageCkPref(getContext());
        recyclerWallpaper = rootView.findViewById(R.id.recyclerWallpaper);
        ivDisplay = rootView.findViewById(R.id.ivDisplay);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerWallpaper.setLayoutManager(gridLayoutManager);


        setDataInRV();
        setFirst();
        return rootView;
    }

    private void setFirst() {
        Bitmap originalBm = BitmapFactory.decodeResource(getResources(), R.drawable.bg_1);
        Bitmap bmp1;
        ivDisplay.setImageBitmap(originalBm);

        Drawable d = getResources().getDrawable(getTheme(1));
        bmp1 = ((BitmapDrawable) d).getBitmap();


        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getActivity().getExternalFilesDir("") + "/", "keyboard_theme");
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "keyboard_theme");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        try {

            File temp_path = new File(file, getName(1) + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(temp_path);
            int quality = 100;
            bmp1.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            storeageCkPref.setTempBitmapS(temp_path.getAbsolutePath());

        } catch (Throwable e) {

            e.printStackTrace();
        }
    }

    public static void setOld() {
        Bitmap originalBm = null;
        String encoded = storeageCkPref.getFILE_PATH();

        String tempEncoded = storeageCkPref.getTempBitmapS();
        if (tempEncoded != null) {


            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", new File(tempEncoded));
            try {

                originalBm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);

            } catch (Exception e) {
                e.printStackTrace();
            }


            themeAdapter.setNEW();


        } else {

            if (encoded != null) {
                Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", new File(encoded));
                try {

                    originalBm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                originalBm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.bg_1);
            }


        }
        ivDisplay.setImageBitmap(originalBm);
    }


    private void setDataInRV() {
        ArrayList<Integer> stringArrayList = new ArrayList<>();
        stringArrayList.add(R.drawable.box);


        stringArrayList.add(R.drawable.bg_1);
        stringArrayList.add(R.drawable.bg_2);
        stringArrayList.add(R.drawable.bg_3);
        stringArrayList.add(R.drawable.bg_4);
        stringArrayList.add(R.drawable.bg_5);
        stringArrayList.add(R.drawable.color_bg_1);
        stringArrayList.add(R.drawable.color_bg_2);
        stringArrayList.add(R.drawable.color_bg_3);
        stringArrayList.add(R.drawable.color_bg_4);
        stringArrayList.add(R.drawable.color_bg_5);
        stringArrayList.add(R.drawable.color_bg_6);


        themeAdapter = new ThemeAdapter(getContext(), stringArrayList);
        recyclerWallpaper.setAdapter(themeAdapter);
    }


    private class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

        Context context;
        int tempPos = storeageCkPref.getCusTempTHEME_ID();
        ArrayList<Integer> adArrayList;

        public ThemeAdapter(Context context, ArrayList<Integer> stringArrayList) {

            this.context = context;
            this.adArrayList = stringArrayList;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaer_item_layout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ThemeAdapter.ViewHolder holder, int position) {
            holder.ivTheme.setImageResource(adArrayList.get(position));


            holder.ivTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        GogleAsKeboard.getInstance().showInterKeboa(getActivity(), new GogleAsKeboard.AdsInterface() {
                            @Override
                            public void adsCall() {
                                Intent intent = new Intent(getContext(), PhotoSelectCsActivity.class);
                                startActivity(intent);
                                notifyDataSetChanged();
                            }
                        });


                    } else {
                        setData(position);

                        borderPos = position;
                        ThemeCreateCsActivity.setB();

                        notifyDataSetChanged();


                    }

                }
            });

            final int themeId = KeyboardCkTheme.getKeyboardTheme(getContext()).mThemeId;
            if (themeId > 5) {
                if (tempPos == position) {
                    holder.ivSelect.setVisibility(View.VISIBLE);
                } else {
                    holder.ivSelect.setVisibility(View.GONE);

                }
            } else {
                holder.ivSelect.setVisibility(View.GONE);

            }
            if (0 == position) {
                holder.ivSelect.setVisibility(View.GONE);
                holder.ivADD.setVisibility(View.VISIBLE);
            } else {
                holder.ivADD.setVisibility(View.GONE);

            }
            if (borderPos == position) {
                holder.cvBg.setBackground(getResources().getDrawable(R.drawable.dr_bg2_border));
            } else {
                holder.cvBg.setBackground(null);
            }

            if (borderPos == 1) {
                //setData(position);
            }

        }


        @Override
        public int getItemCount() {
            return adArrayList.size();
        }

        public void setNEW() {
            notifyDataSetChanged();
        }

        public void newTheme() {
            tempPos = storeageCkPref.getCusTempTHEME_ID();
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivTheme;
            ImageView ivSelect, ivADD;
            ConstraintLayout cvBg;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                ivTheme = itemView.findViewById(R.id.ivTheme);
                ivSelect = itemView.findViewById(R.id.ivSelect);
                ivADD = itemView.findViewById(R.id.ivADD);
                cvBg = itemView.findViewById(R.id.cvBg);

            }
        }
    }

    private void setData(int position) {
        storeageCkPref.setTempBitmapS(null);
        ivDisplay.setImageDrawable(getActivity().getResources().getDrawable(getTheme(position)));
        Bitmap bmp1;


        Drawable d = getResources().getDrawable(getTheme(position));
        bmp1 = ((BitmapDrawable) d).getBitmap();


        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getActivity().getExternalFilesDir("") + "/", "keyboard_theme");
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "keyboard_theme");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        try {

            File temp_path = new File(file, getName(position) + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(temp_path);
            int quality = 100;
            bmp1.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            storeageCkPref.setTempBitmapS(temp_path.getAbsolutePath());

        } catch (Throwable e) {

            e.printStackTrace();
        }
    }

    private String getName(int position) {

        String name;

        if (position > 9) {
            name = "IMG__" + position;
        } else {
            name = "IMG_" + position;
        }

        return name;
    }

    private int getTheme(int position) {
        int drawable = 1;
        if (position == 1) {
            drawable = R.drawable.bg_1;
        } else if (position == 2) {
            drawable = R.drawable.bg_2;
        } else if (position == 3) {
            drawable = R.drawable.bg_3;
        } else if (position == 4) {
            drawable = R.drawable.bg_4;
        } else if (position == 5) {
            drawable = R.drawable.bg_5;
        } else if (position == 6) {
            drawable = R.drawable.color_bg_1;
        } else if (position == 7) {
            drawable = R.drawable.color_bg_2;
        } else if (position == 8) {
            drawable = R.drawable.color_bg_3;
        } else if (position == 9) {
            drawable = R.drawable.color_bg_4;
        } else if (position == 10) {
            drawable = R.drawable.color_bg_5;
        } else if (position == 11) {
            drawable = R.drawable.color_bg_6;
        }
        return drawable;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
