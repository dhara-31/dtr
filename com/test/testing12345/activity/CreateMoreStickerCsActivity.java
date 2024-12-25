package com.test.testing12345.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.adapter.AdapterSet1Sticker;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;
import com.test.testing12345.adsclass.StoreageCkPref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateMoreStickerCsActivity extends Activity {

    ImageView ivSti1, ivSti2, ivPreview, btnAdd;
    ConstraintLayout btnShare, cvPreview, btnCreate;


    ArrayList<Integer> stiList;
    ArrayList<Integer> stiList2;
    int st1 = 0;
    int st2 = 0;
    private long lClickTime = 0;
    StoreageCkPref storeageCkPref;
    AnimationDrawable animation;
    private int resourceId;
    private boolean changed = true;


    Animation shake1, shake2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_more_sticker);


        shake1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        shake2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, CreateMoreStickerCsActivity.this, c);


        storeageCkPref = new StoreageCkPref(this);
        ivSti1 = findViewById(R.id.ivSti1);
        ivSti2 = findViewById(R.id.ivSti2);
        ivPreview = findViewById(R.id.ivPreview);
        btnCreate = findViewById(R.id.constraintLayout37);
        btnAdd = findViewById(R.id.btnAdd);
        btnShare = findViewById(R.id.btnShare);
        cvPreview = findViewById(R.id.cvPreview);
        ivSti1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogStikerSet1(stiList);
            }
        });
        ivSti2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogStikerSet2(stiList2);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lClickTime < 2500) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();
                ivPreview.setImageResource(0);
                btnAdd.setVisibility(View.GONE);
                btnShare.setVisibility(View.GONE);

                setStiPreview();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateMoreStickerCsActivity.this, "Sticker add to keyboard", Toast.LENGTH_LONG).show();
                storeageCkPref.addCs(resourceId);
                btnAdd.setVisibility(View.GONE);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShareDialog();
            }
        });
        setLsit();
    }

    private void setStiPreview() {

        if (st1 == 0) {
            ivSti1.startAnimation(shake1);
            Toast.makeText(this, "Please select first emoji", Toast.LENGTH_LONG).show();


        } else if (st2 == 0) {
            Toast.makeText(this, "Please select second emoji", Toast.LENGTH_LONG).show();
            ivSti2.startAnimation(shake2);

        } else {
            cvPreview.setVisibility(View.VISIBLE);
            String name = getResources().getResourceEntryName(st1);
            String name2 = getResources().getResourceEntryName(st2);
            animation = new AnimationDrawable();
            animation.addFrame(getResources().getDrawable(R.drawable.csa1csb1), 20);
            animation.addFrame(getResources().getDrawable(R.drawable.csa4csb13), 40);
            animation.addFrame(getResources().getDrawable(R.drawable.csa5csb12), 60);
            animation.addFrame(getResources().getDrawable(R.drawable.csa4csb1), 80);
            animation.addFrame(getResources().getDrawable(R.drawable.csa5csb5), 100);
            animation.addFrame(getResources().getDrawable(R.drawable.csa1csb3), 120);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb10), 140);
            animation.addFrame(getResources().getDrawable(R.drawable.csa4csb5), 160);
            animation.addFrame(getResources().getDrawable(R.drawable.csa5csb15), 180);
            animation.addFrame(getResources().getDrawable(R.drawable.csa1csb8), 200);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb13), 220);
            animation.addFrame(getResources().getDrawable(R.drawable.csa3csb11), 240);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb14), 260);
            animation.addFrame(getResources().getDrawable(R.drawable.csa3csb12), 280);
            animation.addFrame(getResources().getDrawable(R.drawable.csa3csb4), 300);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb6), 320);
            animation.addFrame(getResources().getDrawable(R.drawable.csa4csb13), 340);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb8), 360);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb3), 380);
            animation.addFrame(getResources().getDrawable(R.drawable.csa4csb11), 400);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb14), 420);
            animation.addFrame(getResources().getDrawable(R.drawable.csa2csb11), 440);
            animation.addFrame(getResources().getDrawable(R.drawable.csa1csb9), 460);
            animation.addFrame(getResources().getDrawable(R.drawable.csa1csb12), 480);
            animation.addFrame(getResources().getDrawable(R.drawable.csa3csb3), 500);

            ivPreview.setBackgroundDrawable(animation);

            animation.start();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    ((AnimationDrawable) (ivPreview.getBackground())).stop();
                    ivPreview.setBackgroundDrawable(null);
                    ivPreview.setImageDrawable(getRID(name + name2));
                    btnShare.setVisibility(View.VISIBLE);
                    if (storeageCkPref.getCsList() != null && !storeageCkPref.getCsList().isEmpty()) {
                        if (checkAdd(resourceId)) {
                            btnAdd.setVisibility(View.GONE);
                        } else {
                            btnAdd.setVisibility(View.VISIBLE);
                        }

                    } else {
                        btnAdd.setVisibility(View.VISIBLE);
                    }

                }
            }, 2000);

        }
    }

    private boolean checkAdd(int resourceId) {
        boolean check = false;
        for (int i = 0; i < storeageCkPref.getCsList().size(); i++) {
            if (storeageCkPref.getCsList().get(i).equals(resourceId)) {
                check = true;
                break;
            }
        }

        return check;
    }

    private void setLsit() {
        stiList = new ArrayList<>();
        stiList.add(R.drawable.csa1);
        stiList.add(R.drawable.csa2);
        stiList.add(R.drawable.csa3);
        stiList.add(R.drawable.csa4);
        stiList.add(R.drawable.csa5);

        stiList2 = new ArrayList<>();
        stiList2.add(R.drawable.csb1);
        stiList2.add(R.drawable.csb2);
        stiList2.add(R.drawable.csb3);
        stiList2.add(R.drawable.csb4);
        stiList2.add(R.drawable.csb5);
        stiList2.add(R.drawable.csb6);
        stiList2.add(R.drawable.csb7);
        stiList2.add(R.drawable.csb8);
        stiList2.add(R.drawable.csb9);
        stiList2.add(R.drawable.csb10);
        stiList2.add(R.drawable.csb11);
        stiList2.add(R.drawable.csb12);
        stiList2.add(R.drawable.csb13);
        stiList2.add(R.drawable.csb14);
        stiList2.add(R.drawable.csb15);
    }

    private void openDialogStikerSet1(ArrayList<Integer> sti1) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_stiker_pick, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);
        RecyclerView recyclerView = inflate.findViewById(R.id.stickerList);

        AdapterSet1Sticker adapterSet1Sticker;


        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapterSet1Sticker = new AdapterSet1Sticker(this, sti1);
        recyclerView.setAdapter(adapterSet1Sticker);
        adapterSet1Sticker.setOnItemClickListener(new AdapterSet1Sticker.OnItemClickListener1() {
            @Override
            public void onItemClick1(Integer pos, ImageView v) throws IOException {
                ivSti1.setImageResource(pos);
                st1 = pos;
                create1.dismiss();

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
        ImageView ivBg = inflate.findViewById(R.id.ivBg);
        ImageView btnWaShare = inflate.findViewById(R.id.btnWaShare);
        ImageView btnCopy = inflate.findViewById(R.id.btnCopy);
        ImageView btnMore = inflate.findViewById(R.id.btnMore);
        ConstraintLayout ivFrame = inflate.findViewById(R.id.constraintLayout33);
        if (changed) {
            ivBg.setImageResource(R.drawable.ck_send_bg2);
            changed = false;
        } else {
            changed = true;
            ivBg.setImageResource(R.drawable.ck_send_bg);
        }
        ivPreview.setImageResource(resourceId);

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
                Uri uri = FileProvider.getUriForFile(CreateMoreStickerCsActivity.this, getPackageName() + ".fileprovider", file);

                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(CreateMoreStickerCsActivity.this, "App have not been installed.", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = setImage(ivFrame);

                Uri uri = FileProvider.getUriForFile(CreateMoreStickerCsActivity.this, getPackageName() + ".fileprovider", file);

                Intent shareIntent = new Intent("android.intent.action.SEND");
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
                Toast.makeText(CreateMoreStickerCsActivity.this, "Copy to ClipBord", Toast.LENGTH_LONG).show();


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

    private void openDialogStikerSet2(ArrayList<Integer> sti2) {


        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_stiker_pick, (ViewGroup) null);
        builder3.setView(inflate);
        builder3.setCancelable(true);
        RecyclerView recyclerView = inflate.findViewById(R.id.stickerList);

        AdapterSet1Sticker adapterSet1Sticker;


        AlertDialog create2 = builder3.create();
        create2.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapterSet1Sticker = new AdapterSet1Sticker(this, sti2);
        recyclerView.setAdapter(adapterSet1Sticker);
        adapterSet1Sticker.setOnItemClickListener(new AdapterSet1Sticker.OnItemClickListener1() {
            @Override
            public void onItemClick1(Integer pos, ImageView v) throws IOException {
                ivSti2.setImageResource(pos);
                st2 = pos;
                create2.dismiss();

            }
        });


        create2.show();
    }

    private Drawable getRID(String name) {
        Resources resources = getResources();
        resourceId = resources.getIdentifier(name, "drawable",
                getPackageName());
        return resources.getDrawable(resourceId);
    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(CreateMoreStickerCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }

}
