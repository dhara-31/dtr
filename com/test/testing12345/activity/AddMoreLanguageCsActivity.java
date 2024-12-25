package com.test.testing12345.activity;

import android.app.Activity;
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
import com.test.testing12345.adapter.AdapterAddLanguage;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;
import com.test.testing12345.custom.RichInputMethodCkManager;
import com.test.testing12345.custom.SubtypeCk;
import com.test.testing12345.custom.common.LocaleCkUtils;
import com.test.testing12345.custom.utils.LocaleResourceCkUtils;
import com.test.testing12345.custom.utils.SubtypeLocaleCkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class AddMoreLanguageCsActivity extends Activity {
    private RichInputMethodCkManager mRichImm;
    private CharSequence[] mUnusedLocaleNames;
    private String[] mUnusedLocaleValues;
    RecyclerView recyclerUnLan;
    private String[] mUsedLocaleValues;
    SortedSet<Locale> unusedLocales;
    Comparator<Locale> comparator;
    Set<SubtypeCk> enabledSubtypeCks;
    SortedSet<Locale> usedLocales;
    AdapterAddLanguage adapterAddLanguage;
    TextView tvNoLan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_language);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, AddMoreLanguageCsActivity.this , c);

        recyclerUnLan = findViewById(R.id.recyclerUnLan);
        tvNoLan = findViewById(R.id.tvNoLan);
        RichInputMethodCkManager.init(this);
        mRichImm = RichInputMethodCkManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerUnLan.setLayoutManager(linearLayoutManager);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerUnLan.setLayoutAnimation(animation);

        setDATA();
    }

    private void setDATA() {

        ArrayList<String> arrayListLanguage = getL();

        if(arrayListLanguage.isEmpty()){
            tvNoLan.setVisibility(View.VISIBLE);
            recyclerUnLan.setVisibility(View.GONE);
        }else {

            tvNoLan.setVisibility(View.GONE);
            recyclerUnLan.setVisibility(View.VISIBLE);

                if(adapterAddLanguage != null){
                    adapterAddLanguage.setNewData(getL());
                }else {
                    adapterAddLanguage = new AdapterAddLanguage(this, arrayListLanguage);
                    recyclerUnLan.setAdapter(adapterAddLanguage);
                }
            adapterAddLanguage.setOnItemClickListener(new AdapterAddLanguage.OnItemClickListener1() {
                @Override
                public void onItemClick1(String pos, View v) throws IOException {
                    addLanOnKey(pos);


                }
            });
        }
    }

    private void addLanOnKey(String pos) {

            comparator = new LocaleCkUtils.LocaleComparator();
            enabledSubtypeCks = mRichImm.getEnabledSubtypes(false);

            usedLocales = getUsedLocales(enabledSubtypeCks, comparator);
            unusedLocales = getUnusedLocales(usedLocales, comparator);
            mUnusedLocaleNames = new CharSequence[unusedLocales.size()];
            mUnusedLocaleValues = new String[unusedLocales.size()];


            int i = 0;
            for (Locale locale : unusedLocales) {
                final String localeString = LocaleCkUtils.getLocaleString(locale);
                mUnusedLocaleValues[i] = localeString;
                mUnusedLocaleNames[i] =
                        LocaleResourceCkUtils.getLocaleDisplayNameInSystemLocale(localeString);

                final SubtypeCk subtypeCk = SubtypeLocaleCkUtils.getDefaultSubtype(
                        mUnusedLocaleValues[i],
                        getResources());
                if (subtypeCk.getName().contains(pos)) {

                    mRichImm.addSubtype(subtypeCk);


                }
                i++;
            }



        setDATA();

    }

    private ArrayList<String> getL() {
        ArrayList<String> lListLanguage =new ArrayList<>();
        comparator = new LocaleCkUtils.LocaleComparator();
        enabledSubtypeCks = mRichImm.getEnabledSubtypes(false);
        usedLocales = getUsedLocales(enabledSubtypeCks, comparator);

        unusedLocales = getUnusedLocales(usedLocales, comparator);

        for (Locale locale : unusedLocales) {
            {
                lListLanguage.add(locale.getDisplayName());

            }
        }


        return lListLanguage;

    }
    private SortedSet<Locale> getUsedLocales(final Set<SubtypeCk> subtypeCks,
                                             final Comparator<Locale> comparator) {
        final SortedSet<Locale> locales = new TreeSet<>(comparator);

        for (final SubtypeCk subtypeCk : subtypeCks) {
            locales.add(subtypeCk.getLocaleObject());

        }
        return locales;
    }

    private SortedSet<Locale> getUnusedLocales(final Set<Locale> usedLocales,
                                               final Comparator<Locale> comparator) {
        final SortedSet<Locale> locales = new TreeSet<>(comparator);
        for (String localeString : SubtypeLocaleCkUtils.getSupportedLocales()) {
            final Locale locale = LocaleCkUtils.constructLocaleFromString(localeString);
            if (usedLocales.contains(locale)) {
                continue;
            }
            locales.add(locale);
        }
        return locales;
    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(AddMoreLanguageCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }

}
