package com.test.testing12345.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.adapter.AdapterAddFont;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;
import com.test.testing12345.adsclass.StylishFontCkModel;
import com.test.testing12345.other.SelectingCkFontStyle;
import com.test.testing12345.adsclass.StoreageCkPref;

import java.io.IOException;
import java.util.ArrayList;

public class FontAddCsActivity extends Activity {

    RecyclerView recyclerText;

    AdapterAddFont adapterAddFont;
    ArrayList<StylishFontCkModel> stylishFontCkModelArrayList;
    StoreageCkPref storeageCkPref;
    TextView tvNoText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_font);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, FontAddCsActivity.this, c);

        recyclerText = findViewById(R.id.recyclerText);
        tvNoText = findViewById(R.id.tvNoText);
        storeageCkPref = new StoreageCkPref(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerText.setLayoutManager(linearLayoutManager);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerText.setLayoutAnimation(animation);

    }

    private void setDATA() {
        stylishFontCkModelArrayList = getUnSelctedFont();
        if (!stylishFontCkModelArrayList.isEmpty()) {
            recyclerText.setVisibility(View.VISIBLE);
            tvNoText.setVisibility(View.GONE);

            if (adapterAddFont != null) {
                adapterAddFont.setNewData(stylishFontCkModelArrayList);
            } else {


                adapterAddFont = new AdapterAddFont(this, stylishFontCkModelArrayList);
                recyclerText.setAdapter(adapterAddFont);
                adapterAddFont.setOnItemClickListener(new AdapterAddFont.OnItemClickListener1() {
                    @Override
                    public void onItemClick1(StylishFontCkModel pos, View v) throws IOException {

                        storeageCkPref.addFavorite(pos);
                        setDATA();
                    }
                });
            }
        } else {
            recyclerText.setVisibility(View.GONE);
            tvNoText.setVisibility(View.VISIBLE);
        }


    }

    private ArrayList<StylishFontCkModel> getUnSelctedFont() {
        ArrayList<StylishFontCkModel> stylishFontCkModelArrayList = new ArrayList<>();

        if (storeageCkPref.getFavorites() != null && !storeageCkPref.getFavorites().isEmpty()) {
            ArrayList<StylishFontCkModel> allArrayList = SelectingCkFontStyle.fetchingJsonFromAssets(null, this, Build.VERSION.SDK_INT >= 24 ? "stylishfonts/stylish_fonts.json" : "stylishfonts/stylish_fonts_lower_versions.json");

            for (int i = 0; i < allArrayList.size(); i++) {

                if (!checkADD(allArrayList.get(i).getFontStyleName())) {
                    stylishFontCkModelArrayList.add(allArrayList.get(i));
                }


            }

        } else {
            stylishFontCkModelArrayList = SelectingCkFontStyle.fetchingJsonFromAssets(null, this, Build.VERSION.SDK_INT >= 24 ? "stylishfonts/stylish_fonts.json" : "stylishfonts/stylish_fonts_lower_versions.json");

        }


        return stylishFontCkModelArrayList;
    }

    private boolean checkADD(String stylishFontModel) {
        boolean add = false;
        ArrayList<StylishFontCkModel> selectedArrayList = storeageCkPref.getFavorites();
        for (int i = 0; i < selectedArrayList.size(); i++) {
            if (selectedArrayList.get(i).getFontStyleName().equals(stylishFontModel)) {
                add = true;
                break;
            }
        }
        return add;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setDATA();
    }


    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(FontAddCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }

}
