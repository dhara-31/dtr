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
import com.si_charginganimation.nilesh_charginganimation.databinding.ActPermiumEditThemeBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

public class PremiumThemeEditAct extends Activity {
    ActPermiumEditThemeBinding b;


    int tColor;
    int barColor;
    String filePath;
    String tag;
    String origanlTag;
    int wColor;
    ShCAPreference shCAPreference;
    String fontStyle = "font/font1.ttf";
    static String[] fonts = {"font/font1.ttf", "font/font2.otf", "font/font3.otf", "font/font4.ttf", "font/font5.ttf", "font/font6.ttf", "font/font7.ttf", "font/font8.otf", "font/font9.ttf", "font/font10.ttf", "font/font11.ttf", "font/font12.ttf", "font/font13.ttf", "font/font14.ttf", "font/font15.ttf", "font/font16.ttf", "font/font17.ttf", "font/font19.otf", "font/font21.ttf", "font/font22.ttf"};

    private ColorCAAdapter bgColorCAAdapter;
    private int current = 1;
    private int aa;
    private Handler handler;
    private Runnable r;
    private int review_position = 0;
    public static int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActPermiumEditThemeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        FrameLayout banner = findViewById(R.id.banner);
        GoChBetryNils.getInstance().ShowBanner(PremiumThemeEditAct.this, banner ,findViewById(R.id.cardBAnner));

        tColor = Color.WHITE;
        setBarColor(Color.BLACK);
        Bundle bundle = getIntent().getExtras();
        tColor = bundle.getInt("tColor");
        barColor = bundle.getInt("barColor");
        filePath = bundle.getString("filePath");
        filePath = bundle.getString("filePath");
        tag = bundle.getString("tagText");
        origanlTag = bundle.getString("origanalTag");
        wColor = bundle.getInt("waverColor");
        fontStyle = bundle.getString("fontStyle");

        float progress = 1.0f - (((float) 500 / 1000.0f));
        b.waveView.setWaveXAxisPositionMultiplier(progress);
        b.tvDay.setText(ManyCAUSed.getDate());

        b.btBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoChBetryNils.getInstance().showChBetryNilster(PremiumThemeEditAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(PremiumThemeEditAct.this, WallpaperListAct.class);
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
                Intent intent = new Intent(PremiumThemeEditAct.this, PremiumThemePreviewAct.class);
                intent.putExtra("tColor", tColor);
                intent.putExtra("barColor", barColor);
                intent.putExtra("waverColor", wColor);
                intent.putExtra("tagText", tag);
                intent.putExtra("filePath", filePath);
                intent.putExtra("fontStyle", fontStyle);
                GoChBetryNils.getInstance().showChBetryNilster(PremiumThemeEditAct.this, new GoChBetryNils.AChBetryNilInterface() {
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

        setAppTheme();
        setPtOldData();
        setPtFontAdapter();
        setPtBgColorAdapter();
        setPtTimer();

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

    private void setPtFontAdapter() {

        b.rvFont.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FontCAAdapter font_styleAdapter = new FontCAAdapter(this, fonts, themeColor);
        b.rvFont.setAdapter(font_styleAdapter);
        font_styleAdapter.setOnItemClickListener(new FontCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(String pos) {
                fontStyle = pos;
                setPtFontStyle();
            }
        });

    }


    private void saveData() {
        shCAPreference.setPtFilepath_ca(filePath);
        shCAPreference.setPtBarColor_ca(barColor);
        shCAPreference.setPtTextColor_ca(tColor);
        shCAPreference.setPtTagText_ca(tag);
        shCAPreference.setPtOrTagText_ca(origanlTag);
        shCAPreference.setPtWaveColor_ca(wColor);
        shCAPreference.setPtFont(fontStyle);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setPtOldData() {




        setImageBG(filePath);
        setTextColor(tColor);

        setText(tag);
        b.waveView.setWaveColor(wColor);
        b.llShowdate.setBackgroundColor(barColor);
        setPtFontStyle();

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
        edtTag.setText(origanlTag);

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
                } else if (edtTag.getText().toString().length() > 25) {
                    edtTag.setError("Max 25 Char");
                } else {
                    tag = edtTag.getText().toString();
                    origanlTag = edtTag.getText().toString();
//
                    StringBuffer finalString = new StringBuffer();


                    boolean result = tag.matches(".*[a-zA-Z]+.*");
                    if (!result) {

                        String[] stringArray = tag.split("\\n.join(regex.findall(r'\\X', text, regex.U))");
                        String tmpString = "";
                        for (String singleWord : stringArray) {

                            if ((tmpString + singleWord + " ").length() > 0) {
                                finalString.append(tmpString + "\n");

                                tmpString = singleWord + " ";
                            } else {
                                tmpString = tmpString + singleWord + " ";
                            }

                        }

                        if (tmpString.length() > 0) {
                            finalString.append(tmpString);
                        }

                    } else {
                        int index = 0;
                        while (index < tag.length()) {
                            finalString.append(tag.substring(index, Math.min(index + 1, tag.length())) + "\n");
                            index += 1;
                        }
                    }









                    setText(finalString.toString());
                    create.dismiss();
                }
            }
        });


        create.show();

    }

    private void setPtFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvContent2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    private void setBarColor(int color) {
        int c = ColorUtils.setAlphaComponent(color, 100);
        barColor = c;
        b.llShowdate.setBackgroundColor(c);
    }

    private void setTextColor(int color) {

        b.tvDay.setTextColor(color);
        b.tvTime.setTextColor(color);
        b.tvPer.setTextColor(color);
        b.tvContent2.setTextColor(color);
        tColor = color;

    }

    private void setWaveColor(int color) {
        wColor = color;
        b.waveView.setWaveColor(color);
    }

    private void setImageBG(String path) {
        filePath = path;
        Glide.with(this).load(path).into(b.imageView);
    }

    private void setText(String tag) {


        this.tag = tag;

        b.tvContent2.setText(tag);
        b.tvContent2.scroller.forceFinished(true);
        b.tvContent2.scroll();
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


    private void setPtBgColorAdapter() {
        int[] colorNumberList = this.getResources().getIntArray(R.array.colorNumberList);


        b.recyclerColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        bgColorCAAdapter = new ColorCAAdapter(this, colorNumberList, themeColor);
        b.recyclerColor.setAdapter(bgColorCAAdapter);
        bgColorCAAdapter.OnItemClickListenerS(new ColorCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int color, int position) {
                if (current == 1) {
                    setBarColor(color);
                } else if (current == 2) {
                    setTextColor(color);
                } else if (current == 3) {
                    setWaveColor(color);
                }
            }
        });


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
        bgColorCAAdapter.setPos();
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

}
