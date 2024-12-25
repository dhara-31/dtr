package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActCreatePopularThemeBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.io.File;
import java.io.FileOutputStream;

public class PopularCreateThemeAct extends Activity {
    ActCreatePopularThemeBinding b;

    ShCAPreference shCAPreference;
    int tColor;
    String filePath;
    String tag = "Hello";
    private int wColor;
    String fontStyle = "font/font1.ttf";
    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActCreatePopularThemeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);



        shCAPreference = new ShCAPreference(this);
        shCAPreference = new ShCAPreference(this);
        tColor = Color.WHITE;

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        NatBetsAll.banaernatBetse(admobNativeLarge2, PopularCreateThemeAct.this, cardView, false);

        b.tvDay.setText(ManyCAUSed.getDate3());

        b.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopularCreateThemeAct.this, PopularThemeEditAct.class);
                intent.putExtra("tColor", tColor);
                intent.putExtra("waverColor", wColor);
                intent.putExtra("tagText", tag);
                intent.putExtra("filePath", filePath);
                intent.putExtra("fontStyle", fontStyle);


                GoChBetryNils.getInstance().showChBetryNilster(PopularCreateThemeAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivityForResult(intent, 101);
                    }
                });

            }
        });

        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        b.btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.btReset.setAlpha(0.4f);
                b.btReset.setEnabled(false);
                b.tvApply.setText("Apply");
                b.btApply.setAlpha(1f);
                setReset();
            }
        });
        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class,PopularCreateThemeAct.this)) {
                    saveData();
                    shCAPreference.setType_ca("atTheme");
                     b.tvApply.setText("Applied");
                    b.btApply.setAlpha(0.4f);
                }else {
                    openServiceDialog();
                }
            }
        });
        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);

        b.tvTag.setSingleLine(true);
        setOldData();

        b.btReset.setEnabled(false);
        b.btReset.setAlpha(0.4f);
        setButton();
        setTheme();
    }


    private void setButton() {

        if (shCAPreference.getAtEdited_ca()) {
            b.btReset.setAlpha(1f);
            b.btReset.setEnabled(true);
        } else {
            b.btReset.setEnabled(false);
            b.btReset.setAlpha(0.4f);
        }
        if(shCAPreference.getType_ca().equals("atTheme")){
            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        }else {
            b.tvApply.setText("Apply");
            b.btApply.setAlpha(1f);
        }
    }

    private void saveData() {
        if (b.btReset.getAlpha() == 1.0f) {
            shCAPreference.setAtEdited_ca(true);
        } else {
            shCAPreference.setAtEdited_ca(false);

        }
        shCAPreference.setAtFilepath_ca(filePath);
        shCAPreference.setAtTextColor_ca(tColor);
        shCAPreference.setAtTagText_ca(b.tvTag.getText().toString());
        shCAPreference.setAtWaveColor_ca(wColor);
        shCAPreference.setAtFont(fontStyle);

    }

    private void setReset() {
        filePath = getSaveFilePath();
        tColor = Color.WHITE;
        tag = "HELLO";
        wColor = Color.WHITE;
        fontStyle = "font/font1.ttf";
        setImageBG(filePath);
        setTextColor(tColor);

        setText(tag);

        setWaveColor(wColor);
        setFontStyle();
    }

    private void setWaveColor(int color) {

        b.pPer.setColorBackground(color);
    }

    private void setOldData() {

        if (shCAPreference.getAtFilepath_ca().isEmpty()) {
            filePath = getSaveFilePath();
            shCAPreference.setAtFilepath_ca(filePath);
            shCAPreference.setAtTextColor_ca(Color.WHITE);
            shCAPreference.setAtWaveColor_ca(Color.WHITE);


            shCAPreference.setAtFont(fontStyle);


            shCAPreference.setAtTagText_ca(tag);

        }

        filePath = shCAPreference.getAtFilepath_ca();
        tColor = shCAPreference.getAtTextColor_ca();
        tag = shCAPreference.getAtTagText_ca();
        wColor = shCAPreference.getAtWaveColor_ca();
        fontStyle = shCAPreference.getAtFont();

        setImageBG(filePath);
        setTextColor(tColor);
        setWaveColor(wColor);
        setText(tag);

        setFontStyle();
    }

    private void setFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTag.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    private void setText(String tag) {

        b.tvTag.setText(tag);
    }

    private String getSaveFilePath() {
        Bitmap originalBm = BitmapFactory.decodeResource(getResources(), R.drawable.wal2);
        String sFilePath = null;
        File file = new File(getFilesDir(), "bca");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {

            File temp_path = new File(file, "wal2" + ".jpg");
            if (!temp_path.exists()) {


                FileOutputStream outputStream = new FileOutputStream(temp_path);
                int quality = 100;
                originalBm.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            }
            sFilePath = temp_path.getAbsolutePath();
            setImageBG(filePath);
        } catch (Throwable e) {

            e.printStackTrace();
        }
        return sFilePath;
    }


    private void setTextColor(int color) {

        b.tvDay.setTextColor(color);
        b.tvTag.setTextColor(color);

        tColor = color;

    }

    private void setImageBG(String path) {
        filePath = path;
        Glide.with(this).load(path).into(b.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (requestCode == 101) {
                if (resultCode == Activity.RESULT_OK) {
                    b.btReset.setAlpha(1f);
                    b.btReset.setEnabled(true);
                    b.tvApply.setText("Apply");
                    b.btApply.setAlpha(1f);
                    setOldData();
                    shCAPreference.setAtEdited_ca(true);

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void setTheme() {


        if (shCAPreference.getThemeType() == 1) {
            themeColor = getResources().getColor(R.color.th_1);

        } else if (shCAPreference.getThemeType() == 2) {
            themeColor = getResources().getColor(R.color.th_2);




        } else if (shCAPreference.getThemeType() == 3) {
            themeColor = getResources().getColor(R.color.th_3);


        } else if (shCAPreference.getThemeType() == 4) {
            themeColor = getResources().getColor(R.color.th_4);

        }

        b.ivEdit.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivReset.setColorFilter(themeColor , android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivApply.setColorFilter(themeColor , android.graphics.PorterDuff.Mode.MULTIPLY);


    }
    private void openServiceDialog() {

        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_open_over_setting
                , (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();

        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        TextView btShow = inflate.findViewById(R.id.btSetting);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_btn_bg2);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        btShow.setBackground(wrappedDrawable);
        TextView btCancel = inflate.findViewById(R.id.btCancel);
        btCancel.setBackground(wrappedDrawable);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();



            }
        });
        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();

                GoChBetryNils.getInstance().showChBetryNilster(PopularCreateThemeAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {

                        Intent intent=new Intent(PopularCreateThemeAct.this,SettingAct.class);
                        startActivity(intent);
                    }
                });


            }
        });
        create.show();


    }

}
