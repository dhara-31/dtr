package com.test.testing12345.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.testing12345.R;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;
import com.test.testing12345.adsclass.StoreageCkPref;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class PhotoSelectCsActivity extends Activity {
    public static PhotoAdapter photoAdapter;
    RecyclerView recyclerView;
    ArrayList<String> pList = new ArrayList<>();
    StoreageCkPref storeageCkPref;
    TextView tvNoImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, PhotoSelectCsActivity.this , c);


        recyclerView = findViewById(R.id.recyclerPhoto);
        tvNoImage = findViewById(R.id.tvNoImage);
        storeageCkPref = new StoreageCkPref(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);


    }

    private void setDataInRV() {
        pList = getAllShownImagesPath(this);

        if (pList.isEmpty()) {
            tvNoImage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            photoAdapter = new PhotoAdapter(this, pList);
            recyclerView.setAdapter(photoAdapter);

        }

    }


    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        Context context;

        ArrayList<String> adArrayList;

        public PhotoAdapter(Context context, ArrayList<String> stringArrayList) {

            this.context = context;
            this.adArrayList = stringArrayList;
        }

        @NonNull
        @NotNull
        @Override
        public PhotoSelectCsActivity.PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_layout, parent, false);
            return new PhotoAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull PhotoSelectCsActivity.PhotoAdapter.ViewHolder holder, int position) {


            Glide.with(context).load(adArrayList.get(position)).into(holder.ivPhoto);


            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GogleAsKeboard.getInstance().showInterKeboa(PhotoSelectCsActivity.this, new GogleAsKeboard.AdsInterface() {
                        @Override
                        public void adsCall() {
                            Intent intent = new Intent(context, PhotoPreviewCsActivity.class);
                            intent.putExtra("path", adArrayList.get(position));
                            startActivity(intent);
                        }
                    });
                }
            });


        }


        @Override
        public int getItemCount() {
            return adArrayList.size();
        }

        public void setNewData(ArrayList<String> pList) {
            this.adArrayList = pList;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivPhoto;


            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                ivPhoto = itemView.findViewById(R.id.ivTheme);

            }
        }
    }


    @SuppressLint("Range")
    public ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            if (absolutePathOfImage.endsWith(".jpg") || absolutePathOfImage.endsWith(".png"))
                if (new File(absolutePathOfImage).exists()) {
                    listOfAllImages.add(absolutePathOfImage);
                }

        }


        return listOfAllImages;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setDataInRV();
    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(PhotoSelectCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }
}
