package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.ColorCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.FontCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActAnimatorBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import me.itangqi.waveloadingview.WaveLoadingView;

public class AnimatorAct extends Activity {
    ActAnimatorBinding b;


    ColorCAAdapter clockColorCAAdapter;
    ShCAPreference shCAPreference;
    int shapea = 1;
    String fontStylea = "font/font1.ttf";
    String clockPosa = "top";
    int clockColora = 0xFFFFFFFF;
    int amplitudea;
    float borderWidtha;
    int colorBordera;
    int waveColora;
    int bgcolora;
    private int current = 1;

    static String[] fonts = {"font/font1.ttf", "font/font2.otf", "font/font3.otf", "font/font4.ttf", "font/font5.ttf", "font/font6.ttf", "font/font7.ttf", "font/font8.otf", "font/font9.ttf", "font/font10.ttf", "font/font11.ttf", "font/font12.ttf", "font/font13.ttf", "font/font14.ttf", "font/font15.ttf", "font/font16.ttf", "font/font17.ttf", "font/font19.otf", "font/font21.ttf", "font/font22.ttf"};
    private int review_position = 0;
    private int themeColor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActAnimatorBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        shCAPreference = new ShCAPreference(this);

        b.waveLoadingView.setAnimDuration(2000);
        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        b.btShape1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shapea = 1;
                displayShapeTypea(1);
            }
        });

        FrameLayout banner = findViewById(R.id.banner);
        GoChBetryNils.getInstance().ShowBanner(AnimatorAct.this, banner,findViewById(R.id.cardBAnner));

        b.btShape2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayShapeTypea(2);
                shapea = 2;
            }
        });
        b.btShape3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayShapeTypea(3);

                shapea = 3;
            }
        });
        b.btShape4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayShapeTypea(4);

                shapea = 4;
            }
        });


        b.seekBarAmplitude.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                b.waveLoadingView.setAmplitudeRatio(value);
                amplitudea = value;

                setButtonAp();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        b.seekbarBorderWidth.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                b.waveLoadingView.setBorderWidth(value);
                borderWidtha = value;
                setButtonAp();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        b.tvPTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockPosa = "top";
                displayPostion("top");

            }
        });
        b.tvPCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockPosa = "center";
                displayPostion("center");

            }
        });

        b.tvPBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockPosa = "bottom";
                displayPostion("bottom");
            }
        });
        b.btPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimatorAct.this, AnimatorPreviewAct.class);
                intent.putExtra("shape", shapea);
                intent.putExtra("fontStyle", fontStylea);
                intent.putExtra("clockPos", clockPosa);
                intent.putExtra("clockColor", clockColora);
                intent.putExtra("amplitude", amplitudea);
                intent.putExtra("borderWidth", borderWidtha);
                intent.putExtra("colorBorder", colorBordera);
                intent.putExtra("waveColor", waveColora);
                intent.putExtra("bgcolor", bgcolora);
                intent.putExtra("set", "apply");

                GoChBetryNils.getInstance().showChBetryNilster(AnimatorAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(intent);
                    }
                });

            }
        });

        b.btWaveColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 1;
                setButton(1);

            }
        });

        b.btBgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 2;
                setButton(2);

            }
        });
        b.btClockColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 3;
                setButton(3);

            }
        });
        b.btBorderColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 4;
                setButton(4);

            }
        });
        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class,AnimatorAct.this)) {
                                  storeData();

                }else {
                    openServiceDialog();
                }
            }
        });

        setThem();
        displayOldValue();
        displayFontAdapter();

        setClockColorAdapter();


    }
  private void setThem() {


      if (shCAPreference.getThemeType() == 1) {
          themeColor = getResources().getColor(R.color.th_1);
      } else if (shCAPreference.getThemeType() == 2) {

          themeColor = getResources().getColor(R.color.th_2);
      } else if (shCAPreference.getThemeType() == 3) {

          themeColor = getResources().getColor(R.color.th_3);
      } else if (shCAPreference.getThemeType() == 4) {
          themeColor = getResources().getColor(R.color.th_4);
      }

      b.btApply.setColorFilter(themeColor,android.graphics.PorterDuff.Mode.MULTIPLY);
      b.ivPerview.setColorFilter(themeColor,android.graphics.PorterDuff.Mode.MULTIPLY);
      Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
      Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
      DrawableCompat.setTint(wrappedDrawable, themeColor);
      b.btWaveColor.setBackground(wrappedDrawable);

      b.seekBarAmplitude.setScrubberColor(themeColor);
      b.seekbarBorderWidth.setScrubberColor(themeColor);
      b.seekBarAmplitude.setThumbColor(themeColor,themeColor);
      b.seekbarBorderWidth.setThumbColor(themeColor,themeColor);
      if(shCAPreference.getType_ca().equals("cs_anim")){

          b.btApply.setAlpha(0.4f);
      }else {

          b.btApply.setAlpha(1f);
      }

  }
    private void setButtonAp() {
         b.btApply.setAlpha(1f);
    }


    private void displayOldValue() {
        if (shCAPreference.getWaveColor_ca() == 0) {
            shCAPreference.setBgColor_ca(b.waveLoadingView.getWaveBgColor());
            shCAPreference.setWaveColor_ca(b.waveLoadingView.getWaveColor());
        }
        displayPostion(shCAPreference.getClockPos_ca());
        displayShapeTypea(shCAPreference.getShapeType_ca());

        shapea = shCAPreference.getShapeType_ca();
        clockColora = shCAPreference.getClockColor_ca();
        clockPosa = shCAPreference.getClockPos_ca();
        amplitudea = shCAPreference.getAmplitude_ca();
        borderWidtha = shCAPreference.getBorderWidth_ca();
        bgcolora = shCAPreference.getBgColor_ca();
        colorBordera = shCAPreference.getcolorBorder();
        fontStylea = shCAPreference.getFontStyle_ca();
        waveColora = shCAPreference.getWaveColor_ca();



        b.waveLoadingView.setWaveColor(waveColora);
        b.waveLoadingView.setWaveBgColor(bgcolora);
        b.waveLoadingView.setBorderColor(colorBordera);
        b.viewLine.setBackgroundColor(waveColora);
        b.seekbarBorderWidth.setProgress(0);

    }

    private void displayShapeTypea(int shapeT) {
        if (shapeT == 1) {
            b.waveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
            b.cv2.setVisibility(View.GONE);
            b.llCp.setVisibility(View.GONE);

            b.btBorderColor.setVisibility(View.VISIBLE);
            b.llBorderWidth.setVisibility(View.VISIBLE);

            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_circle2);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);

            b.btShape1.setImageDrawable(wrappedDrawable);
            b.btShape2.setImageResource(R.drawable.ic_s);
            b.btShape3.setImageResource(R.drawable.ic_t);
            b.btShape4.setImageResource(R.drawable.ic_s_2);
            b.waveLoadingView.setBorderWidth(borderWidtha);
        } else if (shapeT == 2) {
            b.waveLoadingView.setShapeType(WaveLoadingView.ShapeType.SQUARE);
            b.cv2.setVisibility(View.GONE);
            b.llCp.setVisibility(View.GONE);

            b.btBorderColor.setVisibility(View.VISIBLE);
            b.llBorderWidth.setVisibility(View.VISIBLE);
            b.btShape1.setImageResource(R.drawable.ic_e);
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_square);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btShape2.setImageDrawable(wrappedDrawable);
            b.btShape3.setImageResource(R.drawable.ic_t);
            b.btShape4.setImageResource(R.drawable.ic_s_2);
            b.waveLoadingView.setBorderWidth(borderWidtha);
        } else if (shapeT == 3) {
            b.waveLoadingView.setShapeType(WaveLoadingView.ShapeType.TRIANGLE);
            b.cv2.setVisibility(View.GONE);
            b.llCp.setVisibility(View.GONE);
            b.llBorderWidth.setVisibility(View.GONE);
            b.btShape1.setImageResource(R.drawable.ic_e);
            b.btShape2.setImageResource(R.drawable.ic_s);

            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_triangle);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btShape3.setImageDrawable(wrappedDrawable);
            b.btShape4.setImageResource(R.drawable.ic_s_2);

            b.btBorderColor.setVisibility(View.GONE);
        } else if (shapeT == 4) {
            b.waveLoadingView.setShapeType(WaveLoadingView.ShapeType.RECTANGLE);
            b.cv2.setVisibility(View.VISIBLE);
            b.llCp.setVisibility(View.VISIBLE);

            b.btBorderColor.setVisibility(View.GONE);
            b.llBorderWidth.setVisibility(View.GONE);
            b.btShape1.setImageResource(R.drawable.ic_e);
            b.btShape2.setImageResource(R.drawable.ic_s);
            b.btShape3.setImageResource(R.drawable.ic_t);
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_square_2);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btShape4.setImageDrawable(wrappedDrawable);
            b.waveLoadingView.setBorderWidth(0);
        }
        setButtonAp();
    }

    private void storeData() {
        b.btApply.setAlpha(0.4f);
        shCAPreference.setShapeType_ca(shapea);
        shCAPreference.setFontStyle_ca(fontStylea);
        shCAPreference.setClockColor_ca(clockColora);
        shCAPreference.setAmplitude_ca(amplitudea);
        shCAPreference.setBorderWidth_ca(borderWidtha);
        shCAPreference.setColorBorder_ca(colorBordera);
        shCAPreference.setWaveColor_ca(waveColora);
        shCAPreference.setBgColor_ca(bgcolora);
        shCAPreference.setClockPos_ca(clockPosa);
        shCAPreference.setType_ca("cs_anim");

    }

    private void displayPostion(String tc) {
        if (tc.equals("top")) {
            b.tvPTop.setTextColor(themeColor);
            b.tvPCenter.setTextColor(getResources().getColor(R.color.text_lite));
            b.tvPBottom.setTextColor(getResources().getColor(R.color.text_lite));
        } else if (tc.equals("center")) {
            b.tvPTop.setTextColor(getResources().getColor(R.color.text_lite));
            b.tvPCenter.setTextColor(themeColor);
            b.tvPBottom.setTextColor(getResources().getColor(R.color.text_lite));

        } else if (tc.equals("bottom")) {
            b.tvPTop.setTextColor(getResources().getColor(R.color.text_lite));
            b.tvPCenter.setTextColor(getResources().getColor(R.color.text_lite));
            b.tvPBottom.setTextColor(themeColor);
        }
        setButtonAp();
    }

    private void displayFontAdapter() {

        b.rvFont.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FontCAAdapter font_styleAdapter = new FontCAAdapter(this, fonts, themeColor);
        b.rvFont.setAdapter(font_styleAdapter);
        font_styleAdapter.setOnItemClickListener(new FontCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(String pos) {
                fontStylea = pos;
                setButtonAp();
            }
        });

    }


    private void setClockColorAdapter() {

        int[] colorNumberList = this.getResources().getIntArray(R.array.colorNumberList);


        b.recyclerColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        clockColorCAAdapter = new ColorCAAdapter(this, colorNumberList, themeColor);
        b.recyclerColor.setAdapter(clockColorCAAdapter);
        clockColorCAAdapter.OnItemClickListenerS(new ColorCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int color, int position) {

                if (current == 1) {
                    b.waveLoadingView.setWaveColor(color);
                    b.viewLine.setBackgroundColor(color);
                    setButtonAp();
                    waveColora = color;
                } else if (current == 2) {
                    b.waveLoadingView.setWaveBgColor(color);
                    bgcolora = color;
                    setButtonAp();
                } else if (current == 3) {
                    clockColora = color;
                    setButtonAp();
                } else if (current == 4) {
                    b.waveLoadingView.setBorderColor(color);
                    colorBordera = color;
                    setButtonAp();
                }
            }
        });


    }

    private void setButton(int i) {

        if (i == 1) {
            b.btWaveColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btBgColor.setTextColor(getResources().getColor(R.color.white));
            b.btClockColor.setTextColor(getResources().getColor(R.color.white));
            b.btBorderColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btWaveColor.setBackground(wrappedDrawable);
            b.btBgColor.setBackground(null);
            b.btClockColor.setBackground(null);
            b.btBorderColor.setBackground(null);

        } else if (i == 2) {
            b.btWaveColor.setTextColor(getResources().getColor(R.color.white));
            b.btBgColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btClockColor.setTextColor(getResources().getColor(R.color.white));
            b.btBorderColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btBgColor.setBackground(wrappedDrawable);
            b.btWaveColor.setBackground(null);
            b.btClockColor.setBackground(null);
            b.btBorderColor.setBackground(null);
        } else if (i == 3) {
            b.btWaveColor.setTextColor(getResources().getColor(R.color.white));
            b.btBgColor.setTextColor(getResources().getColor(R.color.white));
            b.btClockColor.setTextColor(getResources().getColor(R.color.hadar));
            b.btBorderColor.setTextColor(getResources().getColor(R.color.white));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btClockColor.setBackground(wrappedDrawable);
            b.btWaveColor.setBackground(null);
            b.btBgColor.setBackground(null);
            b.btBorderColor.setBackground(null);

        } else if (i == 4) {
            b.btWaveColor.setTextColor(getResources().getColor(R.color.white));
            b.btBgColor.setTextColor(getResources().getColor(R.color.white));
            b.btClockColor.setTextColor(getResources().getColor(R.color.white));
            b.btBorderColor.setTextColor(getResources().getColor(R.color.hadar));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btBorderColor.setBackground(wrappedDrawable);
            b.btWaveColor.setBackground(null);
            b.btBgColor.setBackground(null);
            b.btClockColor.setBackground(null);

        }
        clockColorCAAdapter.setPos();
    }


    private WaveLoadingView.ShapeType getShape(int shape) {
        if (shape == 3) {
            return WaveLoadingView.ShapeType.TRIANGLE;
        } else if (shape == 1) {
            return WaveLoadingView.ShapeType.CIRCLE;
        } else if (shape == 2) {
            return WaveLoadingView.ShapeType.SQUARE;
        } else if (shape == 4) {
            return WaveLoadingView.ShapeType.RECTANGLE;
        } else {
            return WaveLoadingView.ShapeType.SQUARE;
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

                GoChBetryNils.getInstance().showChBetryNilster(AnimatorAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent=new Intent(AnimatorAct.this,SettingAct.class);
                        startActivity(intent);
                    }
                });


            }
        });
        create.show();


    }

}

