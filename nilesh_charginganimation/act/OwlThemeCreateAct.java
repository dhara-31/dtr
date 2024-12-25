package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.FontCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActOwlThemeBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

public class OwlThemeCreateAct extends Activity {
    ActOwlThemeBinding b;

    static Handler handler;
    static Runnable r;
    static float aa;
    private int cn = 0;
    private boolean update = true;
    private ShCAPreference shCAPreference;
    private int themeColor;

    static String[] fonts = {"font/font1.ttf", "font/font2.otf", "font/font3.otf", "font/font4.ttf", "font/font5.ttf", "font/font6.ttf", "font/font7.ttf", "font/font8.otf", "font/font9.ttf", "font/font10.ttf", "font/font11.ttf", "font/font12.ttf", "font/font13.ttf", "font/font14.ttf", "font/font15.ttf", "font/font16.ttf", "font/font17.ttf", "font/font19.otf", "font/font21.ttf", "font/font22.ttf"};
    String fontStyle = "font/font1.ttf";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActOwlThemeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        FrameLayout banner = findViewById(R.id.banner);
        NatBetsAll.getInstance().natVolBetsl(OwlThemeCreateAct.this, banner, findViewById(R.id.cardBAnner),admobNativeLarge2,cardView);


        shCAPreference = new ShCAPreference(this);
        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class,OwlThemeCreateAct.this)) {
                    saveData();
                }else {
                    openServiceDialog();
                }
            }
        });
        b.btPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPerview();
            }

        });
        b.tvDate.setText(ManyCAUSed.getDate4());
        setThemeApp();
        setFontAdapter();
        setOldData();

        setTimer(100);
    }

    private void saveData() {
        shCAPreference.setType_ca("owl_theme");
        b.tvApply.setText("Applied");
        b.btApply.setAlpha(0.4f);
        shCAPreference.setOwlFont_ca(fontStyle);
    }

    private void setOldData() {

        if(shCAPreference.getType_ca().equals("owl_theme")){
            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        }

        fontStyle = shCAPreference.getowlFont();
        setFontStyle();
    }


    private void openPerview() {

                Intent intent = new Intent(OwlThemeCreateAct.this, OwlThemePerview.class);
                intent.putExtra("fontStyle", fontStyle);
                startActivity(intent);


    }

    private void setFontAdapter() {

        b.rvFont.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FontCAAdapter font_styleAdapter = new FontCAAdapter(this, fonts, themeColor);
        b.rvFont.setAdapter(font_styleAdapter);
        font_styleAdapter.setOnItemClickListener(new FontCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(String pos) {

                b.tvApply.setText("Apply");
                b.btApply.setAlpha(1f);
                fontStyle = pos;
                setFontStyle();

            }
        });

    }

    private void setFontStyle() {
        b.tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    private void setThemeApp() {


        if (shCAPreference.getThemeType() == 1) {
            themeColor = getResources().getColor(R.color.th_1);

        } else if (shCAPreference.getThemeType() == 2) {
            themeColor = getResources().getColor(R.color.th_2);


        } else if (shCAPreference.getThemeType() == 3) {
            themeColor = getResources().getColor(R.color.th_3);


        } else if (shCAPreference.getThemeType() == 4) {
            themeColor = getResources().getColor(R.color.th_4);

        }

        b.ivPreview.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
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
                GoChBetryNils.getInstance().showChBetryNilster(OwlThemeCreateAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent=new Intent(OwlThemeCreateAct.this,SettingAct.class);
                        startActivity(intent);
                    }
                });



            }
        });
        create.show();


    }
    public void setTimer(int ll) {

        aa = 0;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                float progress = 1.0f - (((float) aa * 10 / 1000.0f));
                b.waveView.setWaveXAxisPositionMultiplier(progress);
                b.waveView2.setWaveXAxisPositionMultiplier(progress);
                b.waveView3.setWaveXAxisPositionMultiplier(progress);
                b.waveView4.setWaveXAxisPositionMultiplier(progress);
                b.waveView5.setWaveXAxisPositionMultiplier(progress);

                if (update) {
                    aa = aa + 0.2f;
                } else {
                    aa = aa - 0.2f;
                }

                if (aa > ll) {
                    update = false;

                }

                if (aa < 1f) {
                    update = true;
                    cn = cn + 1;
                    if (cn == 10) {
                        cn = 0;
                    }


                }
                if (update) {
                    handler.postDelayed(r, 50);
                } else {
                    setAnim();
                }

            }
        }, 50);


    }
    private void setAnim() {
        Animation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(1500);
        anim.setStartOffset(10);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        b.cvWave.startAnimation(anim);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(r);
    }
}
