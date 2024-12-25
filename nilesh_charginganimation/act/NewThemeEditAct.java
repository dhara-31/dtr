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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.BgCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.ColorCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.FontCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.MultiColorCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActNewThemeEditBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.util.ArrayList;

public class NewThemeEditAct extends Activity {
    ActNewThemeEditBinding b;

    int waveColor;
    ShCAPreference shCAPreference;
    int walDr = R.drawable.new_wal1;
    private int themeColor, colorType;

    static String[] fonts = {"font/font1.ttf", "font/font2.otf", "font/font3.otf", "font/font4.ttf", "font/font5.ttf", "font/font6.ttf", "font/font7.ttf", "font/font8.otf", "font/font9.ttf", "font/font10.ttf", "font/font11.ttf", "font/font12.ttf", "font/font13.ttf", "font/font14.ttf", "font/font15.ttf", "font/font16.ttf", "font/font17.ttf", "font/font19.otf", "font/font21.ttf", "font/font22.ttf"};
    String fontStyle = "font/font1.ttf";
    private ColorCAAdapter bgColorCAAdapter;
    private MultiColorCAAdapter multiColorCAAdapter;
    BgCAAdapter bgCAAdapter;
    public static ArrayList<String> colorMultiList = new ArrayList<>();
    public ArrayList<Integer> bgList = new ArrayList<>();

    static Handler handler;
    static Runnable r;
    static float aa;
    private int cn = 0;
    private boolean update = true;
    private int currentType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActNewThemeEditBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        FrameLayout banner = findViewById(R.id.banner);
        GoChBetryNils.getInstance().ShowBanner(NewThemeEditAct.this, banner, findViewById(R.id.cardBAnner));

        Bundle bundle = getIntent().getExtras();
        waveColor = bundle.getInt("waveColor");
        walDr = bundle.getInt("wal");
        fontStyle = bundle.getString("fontStyle");
        colorType = bundle.getInt("colorType");


        b.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        b.btSingleColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorType = 1;
                setButton(1);
            }
        });

        b.btMultiColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorType = 2;
                setButton(2);

            }
        });
        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        b.btPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPer();
            }
        });


        b.tvDate.setText(ManyCAUSed.getDate4());
        b.tvDate2.setText(ManyCAUSed.getDate4());
        setThemeApp();
        setOldData();
        setNtFontAdapter();
        setNtWColorAdapter();
        setNtMultiColorAdapter();
        setBgAdapter();
        setTimer(50);
        setButton(colorType);
    }


    private void openPer() {

        if (colorType == 2) {
            if (colorMultiList == null || colorMultiList.size() < 5) {
                Toast.makeText(NewThemeEditAct.this, "Select Multiple 5 Color", Toast.LENGTH_LONG).show();
                return;
            }

        }


        Intent intent = new Intent(NewThemeEditAct.this, NewThemeEditPerview.class);
        intent.putExtra("wal", walDr);
        intent.putExtra("waveColor", waveColor);
        intent.putExtra("fontStyle", fontStyle);
        intent.putExtra("colorType", colorType);
        startActivity(intent);


    }


    private void setOldData() {

        b.waveView.setWaveColor(waveColor);
        b.imageView.setImageDrawable(getResources().getDrawable(walDr));
        colorMultiList = shCAPreference.getColorList();
        setNtFontStyle();
        setClockPos();

    }

    private void setClockPos() {
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


    }

    private void saveData() {
        if (colorType == 2) {
            if (colorMultiList == null || colorMultiList.size() < 5) {
                Toast.makeText(NewThemeEditAct.this, "Select Multiple 5 Color", Toast.LENGTH_LONG).show();
                return;
            }

        }
        shCAPreference.setNtTheme_ca(walDr);
        shCAPreference.setNtFont_ca(fontStyle);
        shCAPreference.setNtWColor(waveColor);
        shCAPreference.setNtColorType_ca(colorType);
        shCAPreference.saveColorList(colorMultiList);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setNtFontAdapter() {
        b.rvFont.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FontCAAdapter font_styleAdapter = new FontCAAdapter(this, fonts, themeColor);
        b.rvFont.setAdapter(font_styleAdapter);
        font_styleAdapter.setOnItemClickListener(new FontCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(String pos) {
                fontStyle = pos;
                setNtFontStyle();
            }
        });

    }

    private void setWColor(int color) {
        waveColor = color;
        b.waveView.setWaveColor(waveColor);
    }

    private void setNtFontStyle() {
        b.tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvDate2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
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

        b.ivPerview.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.btSave.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivPerview.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        b.btSingleColor.setBackground(wrappedDrawable);

    }

    private void setBgAdapter() {
        int cBgPos = 0;
        b.btMultiColor.setText("Multi Color(" + colorMultiList.size() + "/5)");
        bgList.add(R.drawable.new_wal1);
        bgList.add(R.drawable.new_wal2);
        bgList.add(R.drawable.new_wal3);

        if (walDr == R.drawable.new_wal1) {
            cBgPos = 0;
        } else if (walDr == R.drawable.new_wal2) {
            cBgPos = 1;
        } else if (walDr == R.drawable.new_wal3) {
            cBgPos = 2;
        }


        b.recyclerBg.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        bgCAAdapter = new BgCAAdapter(this, bgList, themeColor, cBgPos);
        b.recyclerBg.setAdapter(bgCAAdapter);
        bgCAAdapter.OnItemClickListenerS(new BgCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int bg, int position) {
                setBg(bg);
                setClockPos();
            }
        });
    }

    private void setBg(int bg) {
        walDr = bg;
        b.imageView.setImageDrawable(getResources().getDrawable(bg));

    }

    private void setNtWColorAdapter() {
        int[] colorNumberList = this.getResources().getIntArray(R.array.colorNumberList);


        b.recyclerColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        bgColorCAAdapter = new ColorCAAdapter(this, colorNumberList, themeColor);
        b.recyclerColor.setAdapter(bgColorCAAdapter);
        bgColorCAAdapter.OnItemClickListenerS(new ColorCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int color, int position) {
                setWColor(color);
            }
        });


    }

    private void setNtMultiColorAdapter() {
        int[] colorNumberList = this.getResources().getIntArray(R.array.colorNumberList);


        b.recyclerMultiColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        if (colorMultiList == null) {
            colorMultiList = new ArrayList<>();
        }
        multiColorCAAdapter = new MultiColorCAAdapter(this, colorNumberList, themeColor, colorMultiList);
        b.recyclerMultiColor.setAdapter(multiColorCAAdapter);
        multiColorCAAdapter.OnItemClickListenerS(new MultiColorCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int color, int position) {

                if (colorMultiList.contains(String.valueOf(color))) {
                    for (int i = 0; i < colorMultiList.size(); i++) {
                        if (Integer.parseInt(colorMultiList.get(i)) == color) {
                            colorMultiList.remove(i);
                        }
                    }

                    multiColorCAAdapter.setNewMulColor(colorMultiList);
                } else {
                    if (colorMultiList.size() < 5) {
                        colorMultiList.add(String.valueOf(color));
                        multiColorCAAdapter.setNewMulColor(colorMultiList);

                    } else {
                        Toast.makeText(NewThemeEditAct.this, "Max 5 Color", Toast.LENGTH_LONG).show();
                    }
                }

                b.btMultiColor.setText("Multi Color(" + colorMultiList.size() + "/5)");

            }
        });
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

    private void setButton(int i) {

        if (i == 1) {

            b.btSingleColor.setTextColor(getResources().getColor(R.color.hadar));

            b.btMultiColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btSingleColor.setBackground(wrappedDrawable);
            b.btMultiColor.setBackground(null);
            b.waveView.setWaveColor(waveColor);
            b.recyclerColor.setVisibility(View.VISIBLE);
            b.recyclerMultiColor.setVisibility(View.GONE);
        } else if (i == 2) {
            b.btMultiColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btSingleColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btMultiColor.setBackground(wrappedDrawable);
            b.btSingleColor.setBackground(null);
            b.recyclerColor.setVisibility(View.GONE);
            b.recyclerMultiColor.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(r);
    }

    @Override
    public void onBackPressed() {

        openSaveDialog();
    }

    private void openSaveDialog() {
        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_after_eidt, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView btCancel = inflate.findViewById(R.id.btBack);
        TextView btShow = inflate.findViewById(R.id.btSave);
        btCancel.setTextColor(themeColor);
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);
        tvLabel.setText("This changes not saved. are you sure want to discard this changes");

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        btShow.setBackground(wrappedDrawable);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();
                finish();
            }
        });
        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();
                saveData();

            }
        });

        create.show();

    }
}
