package com.si_charginganimation.nilesh_charginganimation.act;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActGalleryBinding;
import com.si_charginganimation.nilesh_charginganimation.fragment.ImageFragmet;
import com.si_charginganimation.nilesh_charginganimation.fragment.VideoFragmet;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

public class GalleryAct extends AppCompatActivity {
    ActGalleryBinding b;
    private int themeColor;
    private ShCAPreference shCAPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActGalleryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        FrameLayout banner = findViewById(R.id.banner);
        NatBetsAll.getInstance().natVolBetsl(GalleryAct.this, banner, findViewById(R.id.cardBAnner), admobNativeLarge2, cardView);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        shCAPreference = new ShCAPreference(this);
        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        b.btIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.viewPager.setCurrentItem(0);
            }
        });

        b.btVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.viewPager.setCurrentItem(1);
            }
        });

        setThemeApp();

        setTabLayout();

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
    }

    private void setTabLayout() {

        final PagerAdapter adapter = new PagerAdapter(this, getSupportFragmentManager(), 2);
        b.viewPager.setAdapter(adapter);

        b.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setButton(int position) {
        if (position == 0) {
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btIamge.setBackground(wrappedDrawable);
            b.btVideo.setBackground(null);
            b.btIamge.setTextColor(getResources().getColor(R.color.hadar));
            b.btVideo.setTextColor(getResources().getColor(R.color.white));
        } else if (position == 1) {
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, themeColor);
            b.btIamge.setBackground(null);
            b.btVideo.setBackground(wrappedDrawable);
            b.btIamge.setTextColor(getResources().getColor(R.color.white));
            b.btVideo.setTextColor(getResources().getColor(R.color.hadar));
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public PagerAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        public PagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    ImageFragmet imageFragmet = new ImageFragmet();
                    return imageFragmet;

                case 1:

                    VideoFragmet videoFragmet = new VideoFragmet();
                    return videoFragmet;

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
    protected void onPause() {
        super.onPause();
    }
}