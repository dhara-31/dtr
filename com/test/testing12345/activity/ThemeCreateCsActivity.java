package com.test.testing12345.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.test.testing12345.R;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.StoreageCkPref;
import com.test.testing12345.compat.PreferenceCkManagerCompat;
import com.test.testing12345.custom.settings.SettingsCk;
import com.test.testing12345.frgment.CustomThemeCkFragment;
import com.test.testing12345.frgment.KeyboardThemeFragmentCk;
import com.test.testing12345.keyboard.KeyboardCkTheme;
import com.test.testing12345.other.NonSwipeableCkViewPager;

import java.io.File;
import java.io.FileOutputStream;

public class ThemeCreateCsActivity extends AppCompatActivity {
    NonSwipeableCkViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    public static ConstraintLayout btnApply;
    public static TextView tvBtn;
    public static RadioButton radioButton1, radioButton2;
    public static StoreageCkPref storeageCkPref;
    private int set = 0;
    public static Drawable bg;
    public static Activity activity;

    public static void setB() {


        if (radioButton1.isChecked()) {
            setB1();
        }else if(radioButton2.isChecked()) {
            setB2();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_theme_create);

        FrameLayout banner = findViewById(R.id.banner);
        GogleAsKeboard.getInstance().ShowBanner(ThemeCreateCsActivity.this, banner);

        bg = getResources().getDrawable(R.drawable.dr_btn);
        btnApply = findViewById(R.id.constraintLayoutBtn);
        radioButton1 = findViewById(R.id.radio1);
        radioButton2 = findViewById(R.id.radio2);
        viewPager = findViewById(R.id.viewPagerTheme);
        tvBtn = findViewById(R.id.tvBtn);
        storeageCkPref = new StoreageCkPref(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            set = bundle.getInt("set", 0);
        } else {
            if (KeyboardCkTheme.getKeyboardTheme(this).mThemeId == 6 || KeyboardCkTheme.getKeyboardTheme(this).mThemeId == 7) {
                set = 0;

            } else {
                set = 1;

            }


        }
        if (set == 0) {
            setB1();
        } else {
            setB2();
        }
        setPager();


        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton1.isChecked()) {
                    viewPager.setCurrentItem(0);
                    setB1();
                }
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton2.isChecked()) {
                    viewPager.setCurrentItem(1);
                    setB2();
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTheme();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    radioButton1.setChecked(true);
                    radioButton2.setChecked(false);
                } else if (position == 1) {
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(true);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public static void setB1() {
        if (KeyboardCkTheme.getKeyboardTheme(activity).mThemeId == 6 || KeyboardCkTheme.getKeyboardTheme(activity).mThemeId == 7) {

            if (storeageCkPref.getCusTempTHEME_ID() == CustomThemeCkFragment.borderPos) {

                setBtn();
            } else {
                set();
            }
        } else {
            set();
        }

    }

    public static void setB2() {
        if (KeyboardCkTheme.getKeyboardTheme(activity).mThemeId == 6 || KeyboardCkTheme.getKeyboardTheme(activity).mThemeId == 7) {


            set();
        } else {
            if (storeageCkPref.getTHEME_ID() == KeyboardThemeFragmentCk.borderPos2) {

                setBtn();
            } else {
                set();
            }
        }

    }
    public static void set() {
        btnApply.setVisibility(View.VISIBLE);
        btnApply.setBackground(bg);
        tvBtn.setTextColor(Color.WHITE);
        tvBtn.setText("Apply");
        btnApply.setEnabled(true);

    }

    public static void setBtn() {
        btnApply.setVisibility(View.VISIBLE);

        btnApply.setBackground(activity.getResources().getDrawable(R.drawable.dr_btn2));
        tvBtn.setTextColor(Color.BLACK);
        tvBtn.setText("Applied");
        btnApply.setEnabled(false);
    }

    private void saveTheme() {
        if (radioButton1.isChecked()) {

            String encoded = storeageCkPref.getFILE_PATH();
            String tempEncoded = storeageCkPref.getTempBitmapS();

            if (tempEncoded != null) {
                if (new File(tempEncoded).getName().contains("CUS_")) {
                    storeageCkPref.setCusTempTHEME_ID(0);
                }

                storeageCkPref.setFilePath(new File(tempEncoded));
            } else {
                if (encoded == null) {
                    Bitmap originalBm = BitmapFactory.decodeResource(getResources(), R.drawable.bg_1);
                    File file;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        file = new File(getExternalFilesDir("") + "/", "keyboard_theme");
                    } else {
                        file = new File(Environment.getExternalStorageDirectory(), "keyboard_theme");
                    }
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    try {


                        File temp_path = new File(file, "IMG_" + 0 + ".jpg");


                        FileOutputStream outputStream = new FileOutputStream(temp_path);
                        int quality = 100;
                        originalBm.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

                        storeageCkPref.setFilePath(temp_path);

                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {

                    storeageCkPref.setFilePath(new File(encoded));
                }
            }

            storeageCkPref.setCusTempTHEME_ID(CustomThemeCkFragment.borderPos);

            final int themeId = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;
            if (themeId == 6) {
                KeyboardCkTheme.saveKeyboardThemeId(7, PreferenceCkManagerCompat.getDeviceSharedPreferences(this));
                SettingsCk.removeKeyboardColor(PreferenceCkManagerCompat.getDeviceSharedPreferences(this));

            } else {
                KeyboardCkTheme.saveKeyboardThemeId(6, PreferenceCkManagerCompat.getDeviceSharedPreferences(this));
                SettingsCk.removeKeyboardColor(PreferenceCkManagerCompat.getDeviceSharedPreferences(this));
            }

            CustomThemeCkFragment.newTheme();
            KeyboardThemeFragmentCk.newTheme();
        } else {


            KeyboardCkTheme.saveKeyboardThemeId(storeageCkPref.getTempTHEME_ID(), PreferenceCkManagerCompat.getDeviceSharedPreferences(this));
            SettingsCk.removeKeyboardColor(PreferenceCkManagerCompat.getDeviceSharedPreferences(this));
            storeageCkPref.setTHEME_ID(storeageCkPref.getTempTHEME_ID());
            KeyboardThemeFragmentCk.newTheme();
            CustomThemeCkFragment.newTheme();
        }
        setBtn();

    }


    private void setPager() {
        myPagerAdapter = new MyPagerAdapter(this, getSupportFragmentManager(), 2);
        viewPager.setAdapter(myPagerAdapter);

        viewPager.setCurrentItem(set);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    CustomThemeCkFragment firstFragment = new CustomThemeCkFragment();
                    return firstFragment;

                case 1:

                    KeyboardThemeFragmentCk secondFragment = new KeyboardThemeFragmentCk();
                    return secondFragment;

                default:

                    return null;
            }

        }

        @Override
        public int getCount() {
            return totalTabs;
        }
    }

    @Override
    public void onBackPressed() {
        CustomThemeCkFragment.borderPos = 1;
        KeyboardThemeFragmentCk.borderPos2 = 0;

        GogleAsKeboard.getInstance().showInterBackPressKeboa(ThemeCreateCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });

    }
}
