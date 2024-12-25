package com.test.testing12345.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.test.testing12345.R;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.StoreageCkPref;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class PhotoPreviewCsActivity extends Activity {

    PhotoView cropImageView;
    ConstraintLayout btnApply;
    public String stringFile;
    StoreageCkPref storeageCkPref;
    private Bitmap rBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crop1);


        FrameLayout banner = findViewById(R.id.banner);
        GogleAsKeboard.getInstance().ShowBanner(PhotoPreviewCsActivity.this, banner);

        cropImageView = findViewById(R.id.photo_view);
        btnApply = findViewById(R.id.constraintLayoutBtn);


        storeageCkPref = new StoreageCkPref(this);
        Bundle bundle = getIntent().getExtras();
        stringFile = bundle.getString("path");


        File path = saveGetPaath();


        try {
            setImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    crop();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void crop() throws FileNotFoundException {
        try {
            File file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                file = new File(getExternalFilesDir("") + "/", "custom_keyboard");
            } else {
                file = new File(Environment.getExternalStorageDirectory(), "custom_keyboard");
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            File temp_path;


            Drawable dr2 = cropImageView.getDrawable();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) dr2;
            Bitmap waterMark = bitmapDrawable.getBitmap();

            temp_path = new File(file, "IMG_" + Calendar.getInstance().getTimeInMillis() + ".jpg");

            FileOutputStream outputStream = new FileOutputStream(temp_path);
            int quality = 100;
            waterMark.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);


            Intent intent = new Intent(this, PhotoCropCsActivity.class);
            intent.putExtra("path", temp_path.getAbsolutePath());

            GogleAsKeboard.getInstance().showInterKeboa(PhotoPreviewCsActivity.this, new GogleAsKeboard.AdsInterface() {
                @Override
                public void adsCall() {
                    startActivity(intent);
                }
            });


        } catch (Exception e) {
            Toast.makeText(this, "File not found", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private void setImage() throws IOException {


        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(stringFile));
        Glide.with(this).load(uri).into(cropImageView);


    }


    private File saveGetPaath() {

        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getExternalFilesDir("") + "/", "custom_keyboard");
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "custom_keyboard");
        }
        if (!file.exists()) {
            file.mkdirs();
        }

        File temp_path;
        temp_path = new File(file, "IMG_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
        return temp_path;
    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(PhotoPreviewCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }

}
