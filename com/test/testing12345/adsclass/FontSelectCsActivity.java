package com.test.testing12345.adsclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.activity.FontAddCsActivity;

import java.io.IOException;
import java.util.ArrayList;

public class FontSelectCsActivity extends Activity {

    RecyclerView recyclerText;

    AdapterSelectedFont adapterSelectedFont;
    ArrayList<StylishFontCkModel> stylishFontCkModelArrayList;
    StoreageCkPref storeageCkPref;
    TextView tvNoText, btnMoreFont;
    ConstraintLayout constraintLayoutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selcted_font);

        recyclerText = findViewById(R.id.recyclerText);

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.getInstance().nativeAKeboa(f, FontSelectCsActivity.this, c);


        tvNoText = findViewById(R.id.tvNoText);
        btnMoreFont = findViewById(R.id.tvBtn);
        constraintLayoutBtn = findViewById(R.id.constraintLayoutBtn);
        storeageCkPref = new StoreageCkPref(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerText.setLayoutManager(linearLayoutManager);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerText.setLayoutAnimation(animation);
        constraintLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GogleAsKeboard.getInstance().showInterKeboa(FontSelectCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(FontSelectCsActivity.this, FontAddCsActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }

    private void setDATA() {

        if (storeageCkPref.getFavorites() != null && !storeageCkPref.getFavorites().isEmpty()) {
            stylishFontCkModelArrayList = storeageCkPref.getFavorites();
            tvNoText.setVisibility(View.GONE);
            recyclerText.setVisibility(View.VISIBLE);

            adapterSelectedFont = new AdapterSelectedFont(this, stylishFontCkModelArrayList);
            recyclerText.setAdapter(adapterSelectedFont);
            adapterSelectedFont.setOnItemClickListener(new AdapterSelectedFont.OnItemClickListener1() {
                @Override
                public void onItemClick1(int pos, View v) throws IOException {

                    storeageCkPref.removeFavorite(pos);
                    setDATA();
                }
            });

        } else {
            tvNoText.setVisibility(View.VISIBLE);
            recyclerText.setVisibility(View.GONE);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        setDATA();
    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(FontSelectCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }

}
