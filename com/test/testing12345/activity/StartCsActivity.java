package com.test.testing12345.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.test.testing12345.R;
import com.test.testing12345.adsclass.AppDetailKeboa;
import com.test.testing12345.adsclass.DKeboaApplication;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;
import com.test.testing12345.adsclass.StoreageCkPref;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class StartCsActivity extends Activity {
    private long lClickTime = 0;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO

    };
    ImageView btnRateApp, btnShareApp,  btnStart;
    public static final int MULTIPLE_PERMISSIONS = 10001;

    StoreageCkPref storeageCkPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_start);

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.getInstance().nativeAKeboa(f, StartCsActivity.this, c);


        storeageCkPref = new StoreageCkPref(this);
        btnStart = findViewById(R.id.btnStart);
        btnRateApp = findViewById(R.id.btnRateUs);
        btnShareApp = findViewById(R.id.btnShareApp);


        ImageView btnPp = findViewById(R.id.btnPp);

        btnPp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();

                if (appDetail != null) {
                    if (appDetail.getPrivacy().equals("")) {
                        Toast.makeText(StartCsActivity.this, "Url not found...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(appDetail.getPrivacy()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        try {
                            startActivity(i);
                        } catch (Exception e) {
                            Toast.makeText(StartCsActivity.this, "Unable to open chrome", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(StartCsActivity.this, "Url not found...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lClickTime < 500) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();
                if (checkAllPermissions()) {

                    openNxt();
                }
            }
        });


        btnShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openShareDialog();

            }
        });
        btnRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = getPackageName();

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

    }

    private void openNxt() {

        if (storeageCkPref.getAPP_FIRST()) {

            GogleAsKeboard.getInstance().showInterKeboa(StartCsActivity.this, new GogleAsKeboard.AdsInterface() {
                @Override
                public void adsCall() {

                    Intent intent = new Intent(StartCsActivity.this, IntroScreen1CsActivity.class);
                    startActivity(intent);
                }
            });

        } else {

            GogleAsKeboard.getInstance().showInterKeboa(StartCsActivity.this, new GogleAsKeboard.AdsInterface() {
                @Override
                public void adsCall() {
                    Intent intent = new Intent(StartCsActivity.this, MainCsActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

    private boolean checkAllPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(StartCsActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                int grant = 0;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PERMISSION_GRANTED) {
                        grant++;
                    }
                }

                if (grant == grantResults.length) {
                    openNxt();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                        permissionDialog();
                    }
                    Toast.makeText(StartCsActivity.this, "Please Allow", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void permissionDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_permission, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        TextView tvOk = inflate.findViewById(R.id.tvAllow);
        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();


                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });


        create1.show();

    }


    private void openShareDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_share_app, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        ImageView ivPreview = inflate.findViewById(R.id.ivSticker);
        ImageView btnWaShare = inflate.findViewById(R.id.btnWaShare);
        ImageView btnCopy = inflate.findViewById(R.id.btnCopy);
        ImageView btnMore = inflate.findViewById(R.id.btnMore);
        ConstraintLayout ivFrame = inflate.findViewById(R.id.constraintLayout33);

        ivPreview.setImageResource(R.drawable.ck_logo_plain);

        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();
            }
        });

        btnWaShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = setImage(ivFrame);
                Uri uri = FileProvider.getUriForFile(StartCsActivity.this, getPackageName() + ".fileprovider", file);

                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(StartCsActivity.this, "App have not been installed.", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = setImage(ivFrame);

                Uri uri = FileProvider.getUriForFile(StartCsActivity.this, getPackageName() + ".fileprovider", file);

                Intent shareIntent = new Intent("android.intent.action.SEND");
                //  intent.putExtra("android.intent.extra.SUBJECT", string);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                startActivity(Intent.createChooser(shareIntent, "Share Image"));

            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence charSequence;

                String text = "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getPackageName();
                @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");

                clipboardManager.setPrimaryClip(ClipData.newPlainText("newtext", text));
                Toast.makeText(StartCsActivity.this, "Copy to ClipBord", Toast.LENGTH_LONG).show();


            }
        });
        create1.show();


    }

    private File setImage(ConstraintLayout clFrame) {
        File file;
        File file2 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getFilesDir(), "custom_case");
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "custom_case");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        try {


            file2 = new File(file, "IMG_Share" + ".jpg");


            clFrame.setDrawingCacheEnabled(true);
            Bitmap bitmapStamp = Bitmap.createBitmap(clFrame.getDrawingCache());

            FileOutputStream outputStream = new FileOutputStream(file2);
            bitmapStamp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            clFrame.setDrawingCacheEnabled(false);


        } catch (Throwable e) {

            e.printStackTrace();
        }

        return file2;
    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(StartCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                Intent intent = new Intent(StartCsActivity.this, ExitCsActivity.class);
                startActivity(intent);
            }
        });
    }
}
