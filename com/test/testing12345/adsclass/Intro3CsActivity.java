package com.test.testing12345.adsclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.test.testing12345.R;
import com.test.testing12345.activity.ExitCsActivity;
import com.test.testing12345.activity.MainCsActivity;
import com.test.testing12345.custom.RichInputMethodCkManager;
import com.test.testing12345.custom.SubtypeCk;
import com.test.testing12345.custom.common.LocaleCkUtils;
import com.test.testing12345.custom.utils.LocaleResourceCkUtils;
import com.test.testing12345.custom.utils.SubtypeLocaleCkUtils;
import com.test.testing12345.frgment.FirstFragmentCk;
import com.test.testing12345.frgment.SecondFragmentCk;
import com.test.testing12345.frgment.ThirdFragmentCk;

import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Intro3CsActivity extends AppCompatActivity {

    private RichInputMethodCkManager mRichImm;
    private CharSequence[] mUsedLocaleNames;
    private String[] mUsedLocaleValues;
    private CharSequence[] mUnusedLocaleNames;
    private String[] mUnusedLocaleValues;
    ViewPager viewPager;
    ImageView iv1, iv2, iv3;
    ConstraintLayout btnConfirm;
    private MyPagerAdapter myPagerAdapter;
    public static String selectedLan = "English";
    private long lClickTime=0;
    StoreageCkPref storeageCkPref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeageCkPref = new StoreageCkPref(this);
        RichInputMethodCkManager.init(this);
        mRichImm = RichInputMethodCkManager.getInstance();
        setContentView(R.layout.activity_language_set);


        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.getInstance().nativeAKeboa(f, Intro3CsActivity.this, c);

        viewPager = findViewById(R.id.viewPager);
        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        btnConfirm = findViewById(R.id.constraintLayout8);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lClickTime < 500){
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();
                getLanguage();
                storeageCkPref.setAPP_FIRST(false);

                GogleAsKeboard.getInstance().showInterKeboa(Intro3CsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(Intro3CsActivity.this, MainCsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPager();
            }
        },200);

    }

    private void getLanguage() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Comparator<Locale> comparator = new LocaleCkUtils.LocaleComparator();
                final Set<SubtypeCk> enabledSubtypeCks = mRichImm.getEnabledSubtypes(false);
                final SortedSet<Locale> usedLocales = getUsedLocales(enabledSubtypeCks, comparator);
                mUsedLocaleNames = new CharSequence[usedLocales.size()];
                mUsedLocaleValues = new String[usedLocales.size()];

                int i = 0;
                for (Locale locale : usedLocales) {
                    final String localeString = LocaleCkUtils.getLocaleString(locale);
                    mUsedLocaleValues[i] = localeString;
                    mUsedLocaleNames[i] =   LocaleResourceCkUtils.getLocaleDisplayNameInSystemLocale(localeString);

                    final SubtypeCk subtypeCk = SubtypeLocaleCkUtils.getDefaultSubtype(
                            mUsedLocaleValues[i],
                            getResources());
                    if (subtypeCk.getName().contains("Spanish")||subtypeCk.getName().contains("Russian")) {

                        mRichImm.removeSubtype(subtypeCk);


                    }
                    if (subtypeCk.getName().contains(selectedLan)) {
                         mRichImm.addSubtype(subtypeCk);

                        RichInputMethodCkManager.setCurrentSubtype(subtypeCk);

                    }
                    i++;
                }
            }
        });
    }

    private SortedSet<Locale> getUsedLocales(final Set<SubtypeCk> subtypeCks,
                                             final Comparator<Locale> comparator) {
        final SortedSet<Locale> locales = new TreeSet<>(comparator);

        for (final SubtypeCk subtypeCk : subtypeCks) {
            locales.add(subtypeCk.getLocaleObject());
        }
        for (String localeString : SubtypeLocaleCkUtils.getSupportedLocales()) {
            final Locale locale = LocaleCkUtils.constructLocaleFromString(localeString);
            locales.add(locale);
        }
         return locales;
    }

    private void setDot(int position) {
        if (position == 0) {
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
             FirstFragmentCk.setData();

        } else if (position == 1) {
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            SecondFragmentCk.setData();

        } else if (position == 2) {
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_s1));
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.dr_dot_u1));
            ThirdFragmentCk.setData();

        }
    }

    private void setPager() {
        myPagerAdapter = new MyPagerAdapter(this, getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);


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

                    FirstFragmentCk firstFragment = new FirstFragmentCk();
                    return firstFragment;

                case 1:

                    SecondFragmentCk secondFragment = new SecondFragmentCk();
                    return secondFragment;
                case 2:

                    ThirdFragmentCk thirdFragmentCk = new ThirdFragmentCk();
                    return thirdFragmentCk;
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
        GogleAsKeboard.getInstance().showInterBackPressKeboa(Intro3CsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                Intent intent = new Intent(Intro3CsActivity.this, ExitCsActivity.class);
                startActivity(intent);
            }
        });
    }


}
