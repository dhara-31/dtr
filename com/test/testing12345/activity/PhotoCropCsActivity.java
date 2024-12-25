package com.test.testing12345.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.test.testing12345.R;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.StoreageCkPref;
import com.test.testing12345.frgment.CustomThemeCkFragment;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class PhotoCropCsActivity extends Activity {

    CropImageView cropImageView;
    ConstraintLayout btnApply;
    public String stringFile;
    StoreageCkPref storeageCkPref;
    private Bitmap rBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crop);

        FrameLayout banner = findViewById(R.id.banner);
        GogleAsKeboard.getInstance().ShowBanner(PhotoCropCsActivity.this, banner);


        cropImageView = findViewById(R.id.cropImageView);
        btnApply = findViewById(R.id.constraintLayoutBtn);


        storeageCkPref = new StoreageCkPref(this);
        Bundle bundle = getIntent().getExtras();
        stringFile = bundle.getString("path");


        try {
            setImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        });
    }

    private void cropImage() {
        Bitmap rBitmap = cropImageView.getCroppedImage();

        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getExternalFilesDir("") + "/", "keyboard_theme");
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "keyboard_theme");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        try {


            File temp_path = new File(file, "CUS_" + Calendar.getInstance().getTimeInMillis() + ".jpg");


            FileOutputStream outputStream = new FileOutputStream(temp_path);
            int quality = 100;
            rBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            storeageCkPref.setTempBitmapS(temp_path.getAbsolutePath());


        } catch (Throwable e) {

            e.printStackTrace();
            Toast.makeText(PhotoCropCsActivity.this, "Image Not Save" + e, Toast.LENGTH_SHORT).show();
        }
        CustomThemeCkFragment.borderPos = 0;
        ThemeCreateCsActivity.set();
        CustomThemeCkFragment.setOld();


        GogleAsKeboard.getInstance().showInterKeboa(PhotoCropCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                Intent intent = new Intent(PhotoCropCsActivity.this, ThemeCreateCsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }


    private void setImage() throws IOException {

        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(stringFile));

        cropImageView.setImageUriAsync(uri);
        cropImageView.setAspectRatio(3, 2);


    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(PhotoCropCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }
}
