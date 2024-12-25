package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.ColorCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.FontCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActClassicEditThemeBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

public class ClassicThemeEditAct extends Activity {
    ActClassicEditThemeBinding b;


    int tColor;
    int barColor;
    String filePath;
    String tag;
    int wColor;
    ShCAPreference shCAPreference;
    String fontStyle = "font/font1.ttf";
    static String[] fonts = {"font/font1.ttf", "font/font2.otf", "font/font3.otf", "font/font4.ttf", "font/font5.ttf", "font/font6.ttf", "font/font7.ttf", "font/font8.otf", "font/font9.ttf", "font/font10.ttf", "font/font11.ttf", "font/font12.ttf", "font/font13.ttf", "font/font14.ttf", "font/font15.ttf", "font/font16.ttf", "font/font17.ttf", "font/font19.otf", "font/font21.ttf", "font/font22.ttf"};
    int iconColor;
    private ColorCAAdapter waveColorCAAdapter;

    private int aa;
    private Handler handler;
    private Runnable r;
    int current = 1;
    private int review_position = 0;
    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActClassicEditThemeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);



        FrameLayout banner = findViewById(R.id.banner);
        GoChBetryNils.getInstance().ShowBanner(ClassicThemeEditAct.this, banner,findViewById(R.id.cardBAnner));

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        tColor = Color.WHITE;

        Bundle bundle = getIntent().getExtras();
        tColor = bundle.getInt("tColor");
        barColor = bundle.getInt("barColor");
        filePath = bundle.getString("filePath");
        filePath = bundle.getString("filePath");
        tag = bundle.getString("tagText");
        wColor = bundle.getInt("waverColor");
        fontStyle = bundle.getString("fontStyle");
        iconColor = bundle.getInt("iconColor");


        b.tvDay.setText(ManyCAUSed.getDate2());

        b.btBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoChBetryNils.getInstance().showChBetryNilster(ClassicThemeEditAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(ClassicThemeEditAct.this, WallpaperListAct.class);
                        startActivityForResult(intent, 101);
                    }
                });

            }
        });
        b.btAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagEditDialog();
            }
        });
        b.btPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassicThemeEditAct.this, ClassicThemePreviewAct.class);
                intent.putExtra("tColor", tColor);
                intent.putExtra("barColor", barColor);
                intent.putExtra("waverColor", wColor);
                intent.putExtra("tagText", tag);
                intent.putExtra("filePath", filePath);
                intent.putExtra("fontStyle", fontStyle);
                intent.putExtra("iconColor", iconColor);


                startActivity(intent);

            }
        });

        b.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        b.btBarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 1;
                setButton(1);
            }
        });

        b.btTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 2;
                setButton(2);
            }
        });
        b.btWaveColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 3;
                setButton(3);

            }
        });


        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);
        setAppTheme();
        setCtOldData();
        setCtFontAdapter();
        setCtWaveColorAdapter();


    }

    private void setAppTheme() {

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
        b.ivAddText.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivBg.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.btSave.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        b.btBarColor.setBackground(wrappedDrawable);
    }


    private void setButton(int i) {

        if (i == 1) {

            b.btBarColor.setTextColor(getResources().getColor(R.color.hadar));

            b.btTextColor.setTextColor(getResources().getColor(R.color.white));
            b.btWaveColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btBarColor.setBackground(wrappedDrawable);
            b.btWaveColor.setBackground(null);
            b.btTextColor.setBackground(null);
        } else if (i == 2) {
            b.btTextColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btBarColor.setTextColor(getResources().getColor(R.color.white));
            b.btWaveColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btTextColor.setBackground(wrappedDrawable);
            b.btBarColor.setBackground(null);
            b.btWaveColor.setBackground(null);
        } else if (i == 3) {
            b.btWaveColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btBarColor.setTextColor(getResources().getColor(R.color.white));
            b.btTextColor.setTextColor(getResources().getColor(R.color.white));

            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btWaveColor.setBackground(wrappedDrawable);
            b.btBarColor.setBackground(null);
            b.btTextColor.setBackground(null);

        }
        waveColorCAAdapter.setPos();
    }

    private void setCtFontAdapter() {

        b.rvFont.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FontCAAdapter font_styleAdapter = new FontCAAdapter(this, fonts, themeColor);
        b.rvFont.setAdapter(font_styleAdapter);
        font_styleAdapter.setOnItemClickListener(new FontCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(String pos) {
                fontStyle = pos;
                setFontStyle();
            }
        });

    }


    private void saveData() {
        shCAPreference.setCtFilepath_ca(filePath);
        shCAPreference.setCtBarColor_ca(barColor);
        shCAPreference.setCtTextColor_ca(tColor);
        shCAPreference.setCtTagText_ca(b.tvTag.getText().toString());
        shCAPreference.setCtWaveColor_ca(wColor);
        shCAPreference.setCtFont(fontStyle);
        shCAPreference.setCtIconColor_ca(iconColor);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setCtOldData() {


        setImageBG(filePath);
        setTextColor(tColor);
        setBarColor(barColor, iconColor);
        setText(tag);
        setWaveColor(wColor);
        setFontStyle();
        setCtTimer();

    }


    private void openTagEditDialog() {
        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_edit_tag, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView btSet = inflate.findViewById(R.id.btSet);
        TextView btCancel = inflate.findViewById(R.id.btCancel);
        EditText edtTag = inflate.findViewById(R.id.edtTag);
        ConstraintLayout cvTag = inflate.findViewById(R.id.cvTag);

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.ca_dr_edt_bg);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        cvTag.setBackground(wrappedDrawable);
        btSet.setTextColor(themeColor);
        edtTag.setText(tag);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();
            }
        });
        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTag.getText().toString().trim().isEmpty()) {
                    edtTag.setError("Enter Tag");
                } else {
                    tag = edtTag.getText().toString();

                    setText(tag);
                    create.dismiss();
                }
            }
        });


        create.show();

    }

    private void setFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTag.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));

    }

    private void setBarColor(int barColor, int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) b.ivIcon.getBackground();
        gradientDrawable.setColor(color);
        iconColor = color;
        int c = ColorUtils.setAlphaComponent(barColor, 60);
        this.barColor = c;
        GradientDrawable gradientDrawable2 = (GradientDrawable) b.llLife.getBackground();
        gradientDrawable2.setColor(c);

    }

    private void setTextColor(int color) {

        b.tvDay.setTextColor(color);
        b.tvTime.setTextColor(color);
        b.tvTag.setTextColor(color);
        b.view.setBackgroundColor(color);
        tColor = color;

    }

    private void setWaveColor(int color) {
        wColor = color;

        b.progressPer.setReachBarColor(color);
        b.progressPer.setTextColor(color);
    }

    private void setImageBG(String path) {
        filePath = path;
        Glide.with(this).load(path).into(b.imageView);
    }

    private void setText(String tag) {

        b.tvTag.setText(tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (requestCode == 101) {
                if (resultCode == Activity.RESULT_OK) {
                    setImageBG(data.getStringExtra("path"));
                }
            }
        }
    }


    @Override
    public void onBackPressed() {

        openSaveDialog();
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
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);
        tvLabel.setText("This changes not saved. are you sure want to discard this changes");
        btCancel.setTextColor(themeColor);


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

    private void setCtWaveColorAdapter() {


        int[] colorNumberList = this.getResources().getIntArray(R.array.colorNumberList);


        b.recyclerColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        waveColorCAAdapter = new ColorCAAdapter(this, colorNumberList, themeColor);
        b.recyclerColor.setAdapter(waveColorCAAdapter);
        waveColorCAAdapter.OnItemClickListenerS(new ColorCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int color, int position) {
                if (current == 1) {
                    setBarColor(color, color);
                } else if (current == 2) {
                    setTextColor(color);
                } else if (current == 3) {
                    setWaveColor(color);
                }

            }
        });


    }


}
