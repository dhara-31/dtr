package com.test.testing12345.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.AppDetailKeboa;
import com.test.testing12345.adsclass.DKeboaApplication;
import com.test.testing12345.adsclass.FontSelectCsActivity;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.frgment.FirstPreFragmentCk;
import com.test.testing12345.frgment.FivePreFragmentCk;
import com.test.testing12345.frgment.FourPreFragmentCk;
import com.test.testing12345.frgment.SecondPreFragmentCk;
import com.test.testing12345.frgment.ThirdPreFragmentCk;
import com.test.testing12345.adsclass.StylishFontCkModel;
import com.test.testing12345.other.AutoScrollCkViewpager;
import com.test.testing12345.other.PrefCk;
import com.test.testing12345.other.SelectingCkFontStyle;
import com.test.testing12345.other.StorageCkUtils;
import com.test.testing12345.adsclass.StoreageCkPref;

import java.util.ArrayList;
import java.util.List;

public class MainCsActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;

    ImageView btnDrawer, btnSetting, btnTheme, btnFont, btnSticker, btnLanguage;
    ConstraintLayout cv1, cv2, cvRate, cvShareApp, cvPp, cvKeyboardSwitch;
    AutoScrollCkViewpager autoScrollCkViewpager;
    MyPagerAdapter myPagerAdapter;
    StoreageCkPref storeageCkPref;
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    ImageView iv1, iv2, iv3, iv4, iv5;
    private long lClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storeageCkPref = new StoreageCkPref(this);
        drawerLayout = findViewById(R.id.drawer_layout);


        FrameLayout banner = findViewById(R.id.banner);
        GogleAsKeboard.getInstance().ShowBanner(MainCsActivity.this, banner);

        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);

        cvRate = findViewById(R.id.cv3);
        cvShareApp = findViewById(R.id.cv4);
        cvPp = findViewById(R.id.cv5);

        cvPp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();

                if (appDetail != null) {
                    if (appDetail.getPrivacy().equals("")) {
                        Toast.makeText(MainCsActivity.this, "Url not found...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(appDetail.getPrivacy()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        try {
                            startActivity(i);
                        } catch (Exception e) {
                            Toast.makeText(MainCsActivity.this, "Unable to open chrome", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainCsActivity.this, "Url not found...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnDrawer = findViewById(R.id.btnDrawer);
        cvKeyboardSwitch = findViewById(R.id.constraintLayout11);
        btnSetting = findViewById(R.id.btnSetting);
        btnTheme = findViewById(R.id.btnTheme);

        btnFont = findViewById(R.id.btnFont);
        btnSticker = findViewById(R.id.btnSticker);

        btnLanguage = findViewById(R.id.btnLanguage);
        autoScrollCkViewpager = findViewById(R.id.view_pager);

        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView4);
        iv5 = findViewById(R.id.imageView5);


        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });




        cvKeyboardSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputEnabled()) {
                    ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showInputMethodPicker();

                } else {
                    Intent intent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(MainCsActivity.this, "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GogleAsKeboard.getInstance().showInterKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(MainCsActivity.this, ThemeCreateCsActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });
        btnFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GogleAsKeboard.getInstance().showInterKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(MainCsActivity.this, FontSelectCsActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GogleAsKeboard.getInstance().showInterKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(MainCsActivity.this, LanguageSelectCsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GogleAsKeboard.getInstance().showInterKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(MainCsActivity.this, SettingCsActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
        btnSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SystemClock.elapsedRealtime() - lClickTime < 1000) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();


                GogleAsKeboard.getInstance().showInterKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {

                        Intent intent = new Intent(MainCsActivity.this, AllStickerAddCsActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

        autoScrollCkViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setDot(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        cvShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                drawerLayout.closeDrawer(GravityCompat.START);

                GogleAsKeboard.getInstance().showInterKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(MainCsActivity.this, ThemeCreateCsActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                GogleAsKeboard.getInstance().showInterKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {

                        Intent intent = new Intent(MainCsActivity.this, LanguageSelectCsActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();

                try {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });


        fetchingFonts();
        setPager();
    }


    private void setPager() {
        myPagerAdapter = new MyPagerAdapter(this, getSupportFragmentManager(), 5);
        autoScrollCkViewpager.setAdapter(myPagerAdapter);

        autoScrollCkViewpager.startAutoScroll();
        autoScrollCkViewpager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        autoScrollCkViewpager.setCycle(true);


    }


    private void fetchingFonts() {
        PrefCk.getSharedPreferences(this);
        if (!PrefCk.getSelectedFirstTimeStatus()) {
            PrefCk.setSelectedFontObject(StorageCkUtils.deserialize(SelectingCkFontStyle.fetchingJsonFromAssets(this, this, Build.VERSION.SDK_INT >= 24 ? "stylishfonts/stylish_fonts.json" : "stylishfonts/stylish_fonts_lower_versions.json").get(0)));
            PrefCk.setSelectedFirstTimeFont(true);
        }


        ArrayList<StylishFontCkModel> allArrayList = SelectingCkFontStyle.fetchingJsonFromAssets(null, this, Build.VERSION.SDK_INT >= 24 ? "stylishfonts/stylish_fonts.json" : "stylishfonts/stylish_fonts_lower_versions.json");

        if (storeageCkPref.getFavorites() == null) {
            storeageCkPref.addFavorite(allArrayList.get(0));

            storeageCkPref.addFavorite(allArrayList.get(1));
        }

    }

    public boolean isInputEnabled() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();
        boolean isInputEnabled = false;

        for (int i = 0; i < N; i++) {

            InputMethodInfo imi = mInputMethodProperties.get(i);
            if (imi.getId().contains(getPackageName())) {
                isInputEnabled = true;
            }
        }

        if (isInputEnabled) {
            return true;
        } else {
            return false;
        }
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

                    FirstPreFragmentCk firstFragment = new FirstPreFragmentCk();
                    return firstFragment;

                case 1:

                    SecondPreFragmentCk secondFragment = new SecondPreFragmentCk();
                    return secondFragment;
                case 2:

                    ThirdPreFragmentCk thirdFragment = new ThirdPreFragmentCk();
                    return thirdFragment;
                case 3:

                    FourPreFragmentCk fourFragment = new FourPreFragmentCk();
                    return fourFragment;
                case 4:

                    FivePreFragmentCk fiveFragment = new FivePreFragmentCk();
                    return fiveFragment;
                default:

                    return null;
            }

        }

        @Override
        public int getCount() {
            return totalTabs;
        }
    }

    private void setDot(int position) {
        if (position == 0) {
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv4.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv5.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));

        } else if (position == 1) {
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv4.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv5.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
        } else if (position == 2) {
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));

            iv4.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv5.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
        } else if (position == 3) {
            iv4.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));

            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv5.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
        } else if (position == 4) {
            iv5.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));

            iv4.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            GogleAsKeboard.getInstance().showInterBackPressKeboa(MainCsActivity.this, new GogleAsKeboard.AdsInterface() {
                @Override
                public void adsCall() {
                    Intent intent = new Intent(MainCsActivity.this, ExitCsActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

}
