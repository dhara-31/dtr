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
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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

import com.si_charginganimation.nilesh_charginganimation.databinding.ActCreateClassicThemeBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.io.File;
import java.io.FileOutputStream;

public class ClassicCreateThemeAct extends Activity {
    ActCreateClassicThemeBinding b;
    ShCAPreference shCAPreference;
    int tColor;
    int barColor;
    String filePath;
    String tag = "Hello";
    private int wColor;
    String fontStyle = "font/font1.ttf";
    private int iconColor;
    private int aa;
    private Handler handler;
    private Runnable r;
    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActCreateClassicThemeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        FrameLayout banner = findViewById(R.id.banner);
        NatBetsAll.getInstance().natVolBetsl(ClassicCreateThemeAct.this, banner, findViewById(R.id.cardBAnner),admobNativeLarge2,cardView);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        shCAPreference = new ShCAPreference(this);
        tColor = Color.WHITE;


        b.tvDay.setText(ManyCAUSed.getDate2());

        b.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassicCreateThemeAct.this, ClassicThemeEditAct.class);
                intent.putExtra("tColor", tColor);
                intent.putExtra("barColor", barColor);
                intent.putExtra("waverColor", wColor);
                intent.putExtra("tagText", tag);
                intent.putExtra("filePath", filePath);
                intent.putExtra("fontStyle", fontStyle);
                intent.putExtra("iconColor", iconColor);

                GoChBetryNils.getInstance().showChBetryNilster(ClassicCreateThemeAct.this, new GoChBetryNils.AChBetryNilInterface() {
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
                b.btReset.setEnabled(true);
                b.tvApply.setText("Apply");
                b.btApply.setAlpha(1f);
                setReset();
            }
        });
        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, ClassicCreateThemeAct.this)) {
                    saveData();
                    shCAPreference.setType_ca("ctTheme");
                    b.btReset.setEnabled(true);
                    b.tvApply.setText("Applied");
                    b.btApply.setAlpha(0.4f);
                } else {
                    openServiceDialog();
                }


            }
        });

        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);

        b.tvTag.setSingleLine(true);

        b.btReset.setAlpha(0.4f);
        b.btReset.setEnabled(false);
        setTheme();
        setOldData();
        setButton();

        setTheme();
    }

    private void setButton() {

        if (shCAPreference.getCtEdited_ca()) {
            b.btReset.setAlpha(1f);
            b.btReset.setEnabled(true);
        } else {
            b.btReset.setEnabled(false);
            b.btReset.setAlpha(0.4f);
        }
        if (shCAPreference.getType_ca().equals("ctTheme")) {
            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        } else {
            b.tvApply.setText("Apply");
            b.btApply.setAlpha(1f);
        }

    }

    private void saveData() {
        if (b.btReset.getAlpha() == 1.0f) {
            shCAPreference.setCtEdited_ca(true);
        } else {
            shCAPreference.setCtEdited_ca(false);

        }
        shCAPreference.setCtFilepath_ca(filePath);
        shCAPreference.setCtBarColor_ca(barColor);
        shCAPreference.setCtTextColor_ca(tColor);
        shCAPreference.setCtTagText_ca(b.tvTag.getText().toString());
        shCAPreference.setCtWaveColor_ca(wColor);
        shCAPreference.setCtFont(fontStyle);
        shCAPreference.setCtIconColor_ca(iconColor);

    }

    private void setReset() {
        filePath = getSaveFilePath();
        tColor = Color.WHITE;
        tag = "HELLO";
        wColor = Color.WHITE;
        iconColor = Color.BLACK;
        fontStyle = "font/font1.ttf";
        setCtImageBG(filePath);
        setCtTextColor(tColor);
        setCtBarColor(Color.BLACK, iconColor);
        setCtProgressColor(wColor);
        b.tvTag.setText(tag);


        setCtFontStyle();
    }

    private void setOldData() {

        if (shCAPreference.getCtFilepath_ca().isEmpty()) {
            filePath = getSaveFilePath();
            int c = ColorUtils.setAlphaComponent(Color.BLACK, 100);
            shCAPreference.setCtFilepath_ca(filePath);
            shCAPreference.setCtBarColor_ca(c);
            shCAPreference.setCtTextColor_ca(Color.WHITE);
            shCAPreference.setCtIconColor_ca(Color.BLACK);
            shCAPreference.setCtWaveColor_ca(Color.WHITE);
            shCAPreference.setCtFont(fontStyle);

            shCAPreference.setCtTagText_ca(tag);

        }

        filePath = shCAPreference.getCtFilepath_ca();
        tColor = shCAPreference.getCtTextColor_ca();
        barColor = shCAPreference.getCtBarColor_ca();
        tag = shCAPreference.getCtTagText_ca();
        wColor = shCAPreference.getCtWaveColor_ca();
        fontStyle = shCAPreference.getCtFont();
        iconColor = shCAPreference.getCtIconColor_ca();

        setCtImageBG(filePath);
        setCtTextColor(tColor);
        setCtBarColor(barColor, iconColor);
        setCtFontStyle();
        setCtProgressColor(wColor);
        b.tvTag.setText(tag);
        setCtTimer();
    }

    private void setCtProgressColor(int wColor) {
        b.progressPer.setTextColor(wColor);
        b.progressPer.setReachBarColor(wColor);
    }

    private void setCtFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTag.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));

    }


    private String getSaveFilePath() {
        Bitmap originalBm = BitmapFactory.decodeResource(getResources(), R.drawable.wal);
        String sFilePath = null;
        File file = new File(getFilesDir(), "bca");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            File temp_path = new File(file, "wal3" + ".jpg");
            if (!temp_path.exists()) {


                FileOutputStream outputStream = new FileOutputStream(temp_path);
                int quality = 100;
                originalBm.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            }
            sFilePath = temp_path.getAbsolutePath();
            setCtImageBG(filePath);
        } catch (Throwable e) {

            e.printStackTrace();
        }
        return sFilePath;
    }


    private void setCtBarColor(int color, int color1) {

        GradientDrawable gradientDrawable = (GradientDrawable) b.ivIcon.getBackground();
        gradientDrawable.setColor(color1);
        iconColor = color;
        int c = ColorUtils.setAlphaComponent(color, 60);
        barColor = c;
        GradientDrawable gradientDrawable2 = (GradientDrawable) b.llLife.getBackground();
        gradientDrawable2.setColor(c);
    }

    private void setCtTextColor(int color) {

        b.tvDay.setTextColor(color);
        b.tvTime.setTextColor(color);
        b.tvTag.setTextColor(color);
        b.view.setBackgroundColor(color);
        tColor = color;

    }

    private void setCtImageBG(String path) {
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
                    shCAPreference.setCtEdited_ca(true);
                    setOldData();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setCtTimer() {
        aa = 10;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                b.progressPer.setProgress(aa);

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
                GoChBetryNils.getInstance().showChBetryNilster(ClassicCreateThemeAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(ClassicCreateThemeAct.this, SettingAct.class);
                        startActivity(intent);
                    }
                });


            }
        });
        create.show();


    }

}
