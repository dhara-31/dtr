package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.ColorCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.FontCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActPopularEditThemeBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

public class PopularThemeEditAct extends Activity {
    ActPopularEditThemeBinding b;


    int tColor;
    String filePath;
    String tagText;
    int wColor;
    ShCAPreference shCAPreference;
    String fontStyle = "font/font1.ttf";
    static String[] fonts = {"font/font1.ttf", "font/font2.otf", "font/font3.otf", "font/font4.ttf", "font/font5.ttf", "font/font6.ttf", "font/font7.ttf", "font/font8.otf", "font/font9.ttf", "font/font10.ttf", "font/font11.ttf", "font/font12.ttf", "font/font13.ttf", "font/font14.ttf", "font/font15.ttf", "font/font16.ttf", "font/font17.ttf", "font/font19.otf", "font/font21.ttf", "font/font22.ttf"};
    private ColorCAAdapter waveColorCAAdapter;
    private int current = 1;
    private int review_position = 0;
    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActPopularEditThemeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);
        tColor = Color.WHITE;

        FrameLayout banner = findViewById(R.id.banner);
        GoChBetryNils.getInstance().ShowBanner(PopularThemeEditAct.this, banner,findViewById(R.id.cardBAnner));
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        Bundle bundle = getIntent().getExtras();
        tColor = bundle.getInt("tColor");
        filePath = bundle.getString("filePath");
        tagText = bundle.getString("tagText");
        wColor = bundle.getInt("waverColor");
        fontStyle = bundle.getString("fontStyle");
        b.tvDay.setText(ManyCAUSed.getDate3());

        b.btBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoChBetryNils.getInstance().showChBetryNilster(PopularThemeEditAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(PopularThemeEditAct.this, WallpaperListAct.class);
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
                Intent intent = new Intent(PopularThemeEditAct.this, PopularThemePreviewAct.class);
                intent.putExtra("tColor", tColor);
                intent.putExtra("waverColor", wColor);
                intent.putExtra("tagText", tagText);
                intent.putExtra("filePath", filePath);
                intent.putExtra("fontStyle", fontStyle);


                GoChBetryNils.getInstance().showChBetryNilster(PopularThemeEditAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(intent);
                    }
                });



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
        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);

        b.tvTag.setSingleLine(true);
        b.btTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 1;
                setButton(1);

            }
        });
        b.btWaveColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 2;
                setButton(2);
            }
        });

        setAppTheme();
        setOldData();
        setFontAdapter();
        setPtWaveColorAdapter();


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
        b.btTextColor.setBackground(wrappedDrawable);
    }


    private void setFontAdapter() {

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
        shCAPreference.setAtFilepath_ca(filePath);
        shCAPreference.setAtTextColor_ca(tColor);
        shCAPreference.setAtTagText_ca(b.tvTag.getText().toString());
        shCAPreference.setAtWaveColor_ca(wColor);
        shCAPreference.setAtFont(fontStyle);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setOldData() {


        setImageBG(filePath);
        setTextColor(tColor);

        setText(tagText);

        setWaveColor(wColor);
        setFontStyle();

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
        edtTag.setText(tagText);
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
                    tagText = edtTag.getText().toString();

                    setText(tagText);
                    create.dismiss();
                }
            }
        });


        create.show();

    }

    private void setFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTag.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));

    }


    private void setTextColor(int color) {

        b.tvDay.setTextColor(color);
        b.tvTag.setTextColor(color);

        tColor = color;

    }

    private void setWaveColor(int color) {
        wColor = color;
        b.pPer.setColorBackground(color);
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

    private void setPtWaveColorAdapter() {

        int[] colorNumberList = this.getResources().getIntArray(R.array.colorNumberList);


        b.recyclerColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        waveColorCAAdapter = new ColorCAAdapter(this, colorNumberList, themeColor);
        b.recyclerColor.setAdapter(waveColorCAAdapter);

        waveColorCAAdapter.OnItemClickListenerS(new ColorCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int color, int position) {
                if (current == 1) {
                    setTextColor(color);
                } else if (current == 2) {
                    setWaveColor(color);
                }
            }
        });


    }


    private void setButton(int i) {

        if (i == 1) {
            b.btTextColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btWaveColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btTextColor.setBackground(wrappedDrawable);
            b.btWaveColor.setBackground(null);
        } else if (i == 2) {
            b.btWaveColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btTextColor.setTextColor(getResources().getColor(R.color.white));

            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btWaveColor.setBackground(wrappedDrawable);
            b.btTextColor.setBackground(null);

        }
        waveColorCAAdapter.setPos();
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




















