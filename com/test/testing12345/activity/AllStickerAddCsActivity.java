package com.test.testing12345.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.frgment.AnimatedStickerCkFragment;
import com.test.testing12345.frgment.CustonmStickerCkFragment;
import com.test.testing12345.frgment.TextStickerFragmentCk;

public class AllStickerAddCsActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView tvAnimated, tvCustom, tvText;
    MyPagerAdapter myPagerAdapter;
    private int set = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_all_sticker);


        FrameLayout banner = findViewById(R.id.banner);
        GogleAsKeboard.getInstance().ShowBanner(AllStickerAddCsActivity.this, banner);

        tvAnimated = findViewById(R.id.tvAnimated);

        tvCustom = findViewById(R.id.tvCustom);
        tvText = findViewById(R.id.tvText);
        viewPager = findViewById(R.id.viewPagerSticker);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            set = bundle.getInt("set", 0);
        }
        tvAnimated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                viewPager.setCurrentItem(0);
            }
        });
        tvCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(1);
            }
        });
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                viewPager.setCurrentItem(2);
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setTab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setPager();


    }

    private void setTab(int i) {
        if (i == 0) {
            tvAnimated.setTextColor(getResources().getColor(R.color.white));
            tvCustom.setTextColor(getResources().getColor(R.color.black));
            tvText.setTextColor(getResources().getColor(R.color.black));
            tvAnimated.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg_s));
            tvCustom.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg));
            tvText.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg));
        } else if (i == 1) {

            tvAnimated.setTextColor(getResources().getColor(R.color.black));
            tvCustom.setTextColor(getResources().getColor(R.color.white));
            tvText.setTextColor(getResources().getColor(R.color.black));
            tvAnimated.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg));
            tvCustom.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg_s));
            tvText.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg));

        } else if (i == 2) {
            tvAnimated.setTextColor(getResources().getColor(R.color.black));
            tvCustom.setTextColor(getResources().getColor(R.color.black));
            tvText.setTextColor(getResources().getColor(R.color.white));
            tvAnimated.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg));
            tvCustom.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg));
            tvText.setBackground(getResources().getDrawable(R.drawable.dr_tab_bg_s));
        }
    }

    private void setPager() {
        myPagerAdapter = new MyPagerAdapter(this, getSupportFragmentManager(), 3);
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
                    AnimatedStickerCkFragment firstFragment = new AnimatedStickerCkFragment();
                    return firstFragment;

                case 1:

                    CustonmStickerCkFragment secondFragment = new CustonmStickerCkFragment();
                    return secondFragment;
                case 2:

                    TextStickerFragmentCk thirdFragment = new TextStickerFragmentCk();
                    return thirdFragment;
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
        GogleAsKeboard.getInstance().showInterBackPressKeboa(AllStickerAddCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }

}
