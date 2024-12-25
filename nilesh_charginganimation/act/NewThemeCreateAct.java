package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActNewThemeCreateBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewThemeCreateAct extends Activity {
    ActNewThemeCreateBinding b;
    static Handler handler;
    static Runnable r;
    static float aa;
    private int cn = 0;
    String fontStyle = "font/font1.ttf";
    int waveColor;
    boolean update = true;
    int[] colorNumberList;

    ShCAPreference shCAPreference;
    int walDr = R.drawable.new_wal1;
    private int themeColor;
    int colorType;
    private ArrayList<String> colorMultiList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActNewThemeCreateBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);



        shCAPreference = new ShCAPreference(this);

        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, NewThemeCreateAct.this)) {
                    saveData();

                } else {
                    openServiceDialog();
                }
            }
        });

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        NatBetsAll.banaernatBetse(admobNativeLarge2, NewThemeCreateAct.this, cardView, false);

        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        b.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewThemeCreateAct.this, NewThemeEditAct.class);
                intent.putExtra("wal", walDr);
                intent.putExtra("waveColor", waveColor);
                intent.putExtra("fontStyle", fontStyle);
                intent.putExtra("colorType", colorType);


                GoChBetryNils.getInstance().showChBetryNilster(NewThemeCreateAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivityForResult(intent, 101);
                    }
                });

            }

        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        b.tvDate.setText(dateTime);
        b.tvDate2.setText(dateTime);


        setThemeApp();
        setOldNewThemeData();
        setTimer(50);
    }

    private void saveData() {
        shCAPreference.setNtTheme_ca(walDr);
        shCAPreference.setNtFont_ca(fontStyle);
        shCAPreference.setNtWColor(waveColor);
        shCAPreference.setType_ca("newTheme");
        shCAPreference.saveColorList(colorMultiList);
        b.tvApply.setText("Applied");
        b.btApply.setAlpha(0.4f);


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

        b.ivEdit.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivReset.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivApply.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    private void setOldNewThemeData() {

        if (shCAPreference.getType_ca().equals("newTheme")) {
            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        }

        if (shCAPreference.getNtTheme_ca() == 0) {
            shCAPreference.setNtTheme_ca(R.drawable.new_wal1);
            shCAPreference.setNtFont_ca(fontStyle);
            shCAPreference.setNtWColor(Color.WHITE);
        }
        walDr = shCAPreference.getNtTheme_ca();
        fontStyle = shCAPreference.getNtFont_ca();
        waveColor = shCAPreference.getNtWColor();
        colorType = shCAPreference.getNtColorType_ca();

        if (walDr == R.drawable.new_wal3) {
            b.cvDate1.setVisibility(View.GONE);
            b.tvPer.setVisibility(View.GONE);
            b.cvDate2.setVisibility(View.VISIBLE);
            b.tvPer2.setVisibility(View.VISIBLE);
            b.cardView.setCardBackgroundColor(getResources().getColor(R.color.owl_theme));
        } else {
            b.cvDate2.setVisibility(View.GONE);
            b.tvPer2.setVisibility(View.GONE);
            b.cvDate1.setVisibility(View.VISIBLE);
            b.tvPer.setVisibility(View.VISIBLE);
            b.cardView.setCardBackgroundColor(getResources().getColor(R.color.black));
        }
        b.waveView.setWaveColor(waveColor);

        colorMultiList = shCAPreference.getColorList();
        b.imageView.setImageDrawable(getResources().getDrawable(walDr));
        setNtFontStyle();

    }

    private void setNtFontStyle() {
        b.tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvDate2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (requestCode == 101) {
                if (resultCode == Activity.RESULT_OK) {


                    b.tvApply.setText("Apply");
                    b.btApply.setAlpha(1f);

                    setOldNewThemeData();
                }
            }
        }
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

                GoChBetryNils.getInstance().showChBetryNilster(NewThemeCreateAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(NewThemeCreateAct.this, SettingAct.class);
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
                    if (!colorMultiList.isEmpty()) {
                        if (colorType == 2) {
                            cn = cn + 1;
                            if (cn >= colorMultiList.size()) {
                                cn = 0;
                            }


                            if (colorMultiList.size() > 0) {
                                b.waveView.setWaveColor(Integer.parseInt(colorMultiList.get(cn)));
                            }
                        }
                    }



                }
                handler.postDelayed(r, 50);

            }
        }, 50);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(r);
    }
}

