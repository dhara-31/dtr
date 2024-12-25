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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActCreatePermiumThemeBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.io.File;
import java.io.FileOutputStream;

public class PremiumCreateThemeAct extends Activity {
    ActCreatePermiumThemeBinding b;
    ShCAPreference shCAPreference;
    int tColor;
    int barColor;
    String filePath;
    String tag = "Hello";
    String origanalTag = "Hello";
    private int wColor;
    String fontStyle = "font/font1.ttf";
    private int aa;
    private Handler handler;
    private Runnable r;
    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActCreatePermiumThemeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        FrameLayout banner = findViewById(R.id.banner);
        NatBetsAll.getInstance().natVolBetsl(PremiumCreateThemeAct.this, banner, findViewById(R.id.cardBAnner),admobNativeLarge2,cardView);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        shCAPreference = new ShCAPreference(this);
        tColor = Color.WHITE;

        float progress = 1.0f - (((float) 500 / 1000.0f));
        b.waveView.setWaveXAxisPositionMultiplier(progress);

        b.tvDay.setText(ManyCAUSed.getDate());
        b.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PremiumCreateThemeAct.this, PremiumThemeEditAct.class);
                intent.putExtra("tColor", tColor);
                intent.putExtra("barColor", barColor);
                intent.putExtra("waverColor", wColor);
                intent.putExtra("tagText", tag);
                intent.putExtra("origanalTag", origanalTag);
                intent.putExtra("filePath", filePath);
                intent.putExtra("fontStyle", fontStyle);

                GoChBetryNils.getInstance().showChBetryNilster(PremiumCreateThemeAct.this, new GoChBetryNils.AChBetryNilInterface() {
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
                setPtReset();
            }
        });

        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, PremiumCreateThemeAct.this)) {
                    saveData();
                    shCAPreference.setType_ca("ptTheme");
                    b.tvApply.setText("Applied");
                    b.btApply.setAlpha(0.4f);
                } else {
                    openServiceDialog();
                }
            }
        });
        b.btReset.setAlpha(0.4f);
        b.btReset.setEnabled(false);
        setTheme();
        setPtOldData();
        setButton();

    }

    private void setButton() {


        if (shCAPreference.getPtEdited_ca()) {
            b.btReset.setAlpha(1f);
            b.btReset.setEnabled(true);
        } else {
            b.btReset.setEnabled(false);
            b.btReset.setAlpha(0.4f);
        }
        if (shCAPreference.getType_ca().equals("ptTheme")) {
            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        } else {
            b.tvApply.setText("Apply");
            b.btApply.setAlpha(1f);
        }
    }

    private void saveData() {

        if (b.btReset.getAlpha() == 1.0f) {
            shCAPreference.setPtEdited_ca(true);
        } else {
            shCAPreference.setPtEdited_ca(false);

        }

        shCAPreference.setPtFilepath_ca(filePath);
        shCAPreference.setPtBarColor_ca(barColor);
        shCAPreference.setPtTextColor_ca(tColor);
        shCAPreference.setPtTagText_ca(tag);
        shCAPreference.setPtOrTagText_ca(origanalTag);

        shCAPreference.setPtWaveColor_ca(wColor);
        shCAPreference.setPtFont(fontStyle);

    }

    private void setPtReset() {
        String tempTag = "Hello";
        filePath = getSaveFilePath();
        tColor = Color.WHITE;
        barColor = ColorUtils.setAlphaComponent(Color.BLACK, 100);
        StringBuffer finalString = new StringBuffer();
        int index = 0;
        while (index < tempTag.length()) {
            finalString.append(tempTag.substring(index, Math.min(index + 1, tempTag.length())) + "\n");
            index += 1;
        }
        tag = finalString.toString();
        origanalTag = tempTag;
        wColor = Color.WHITE;
        fontStyle = "font/font1.ttf";
        setPtImageBG(filePath);
        setTextColor(tColor);
        setPtBarColor(barColor);
        setPtText(tag);
        b.waveView.setWaveColor(wColor);

        setPtFontStyle();
    }

    private void setPtOldData() {

        if (shCAPreference.getPtFilepath_ca().isEmpty()) {
            filePath = getSaveFilePath();
            int c = ColorUtils.setAlphaComponent(Color.BLACK, 100);
            shCAPreference.setPtFilepath_ca(filePath);
            shCAPreference.setPtBarColor_ca(c);
            shCAPreference.setPtTextColor_ca(Color.WHITE);
            shCAPreference.setPtWaveColor_ca(Color.WHITE);


            shCAPreference.setPtFont(fontStyle);
            StringBuffer finalString = new StringBuffer();
            int index = 0;
            while (index < tag.length()) {
                finalString.append(tag.substring(index, Math.min(index + 1, tag.length())) + "\n");
                index += 1;
            }
            shCAPreference.setPtTagText_ca(finalString.toString());
            shCAPreference.setPtOrTagText_ca(tag);

        }

        filePath = shCAPreference.getPtFilepath_ca();
        tColor = shCAPreference.getPtTextColor_ca();
        barColor = shCAPreference.getPtBarColor_ca();
        tag = shCAPreference.getPtTagText_ca();
        origanalTag = shCAPreference.getPtORTagText();
        wColor = shCAPreference.getPtWaveColor_ca();
        fontStyle = shCAPreference.getPtFont();

        setPtImageBG(filePath);
        setTextColor(tColor);
        setPtBarColor(barColor);
        setPtText(tag);
        b.waveView.setWaveColor(wColor);

        setPtFontStyle();
        setPtTimer();
    }

    private void setPtFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvContent2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    private void setPtText(String tag) {

        b.tvContent2.setText(tag);
        b.tvContent2.scroller.forceFinished(true);
        b.tvContent2.scroll();
    }

    private String getSaveFilePath() {
        Bitmap originalBm = BitmapFactory.decodeResource(getResources(), R.drawable.wal3);
        String sFilePath = null;
        File file = new File(getFilesDir(), "bca");
        if (!file.exists()) {
            file.mkdirs();

        }

        try {


            File temp_path = new File(file, "wal" + ".jpg");
            if (!temp_path.exists()) {


                FileOutputStream outputStream = new FileOutputStream(temp_path);
                int quality = 100;
                originalBm.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            }
            sFilePath = temp_path.getAbsolutePath();
            setPtImageBG(filePath);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return sFilePath;
    }


    private void setPtBarColor(int color) {


        b.llShowdate.setBackgroundColor(color);

    }

    private void setTextColor(int color) {

        b.tvDay.setTextColor(color);
        b.tvTime.setTextColor(color);
        b.tvPer.setTextColor(color);
        b.tvContent2.setTextColor(color);
        tColor = color;

    }

    private void setPtImageBG(String path) {
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
                    shCAPreference.setPtEdited_ca(true);
                    setPtOldData();
                }
            }
        }
    }

    public void setPtTimer() {

        aa = 10;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                float progress = 1.0f - (((float) aa * 10 / 1000.0f));
                b.waveView.setWaveXAxisPositionMultiplier(progress);
                b.tvPer.setText(aa + "%");
                aa = aa + 1;
                if (aa > 50) {

                } else {
                    handler.postDelayed(r, 100);
                }
            }
        }, 100);


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
        b.ivReset.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivApply.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);

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

                GoChBetryNils.getInstance().showChBetryNilster(PremiumCreateThemeAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(PremiumCreateThemeAct.this, SettingAct.class);
                        startActivity(intent);
                    }
                });


            }
        });
        create.show();


    }

}
