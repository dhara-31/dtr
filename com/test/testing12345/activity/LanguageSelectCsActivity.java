package com.test.testing12345.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.adapter.AdapterSelLanguage;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.custom.RichInputMethodCkManager;
import com.test.testing12345.custom.SubtypeCk;
import com.test.testing12345.custom.common.LocaleCkUtils;
import com.test.testing12345.custom.utils.LocaleResourceCkUtils;
import com.test.testing12345.custom.utils.SubtypeLocaleCkUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class LanguageSelectCsActivity extends Activity {
    ImageView btnRemoveLanguage;
    ConstraintLayout btnMoreLanguage;
    RecyclerView recyclerLan;
    public LanAdapter lanAdapter;
    private RichInputMethodCkManager mRichImm;
    private CharSequence[] mUnusedLocaleNames;
    private String[] mUnusedLocaleValues;
    private CharSequence[] mUsedLocaleNames;
    SortedSet<Locale> usedLocales;
    private String[] mUsedLocaleValues;
    SortedSet<Locale> unusedLocales;
    Comparator<Locale> comparator;
    Set<SubtypeCk> enabledSubtypeCks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RichInputMethodCkManager.init(this);

        mRichImm = RichInputMethodCkManager.getInstance();

        setContentView(R.layout.activity_language_select);

        FrameLayout banner = findViewById(R.id.banner);
        GogleAsKeboard.getInstance().ShowBanner(LanguageSelectCsActivity.this, banner);

        btnMoreLanguage = findViewById(R.id.constraintLayoutBtn);
        btnRemoveLanguage = findViewById(R.id.btnRemoveLanguage);

        recyclerLan = findViewById(R.id.recyclerLan);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerLan.setLayoutManager(gridLayoutManager);

        btnRemoveLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLanguageDialog();
            }
        });

        btnMoreLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GogleAsKeboard.getInstance().showInterKeboa(LanguageSelectCsActivity.this, new GogleAsKeboard.AdsInterface() {
                    @Override
                    public void adsCall() {
                        Intent intent = new Intent(LanguageSelectCsActivity.this, AddMoreLanguageCsActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
        getLanguageList();


        setFixLan();
    }

    private void openLanguageDialog() {
        ArrayList<String> arrayListLanguage = new ArrayList<>();
        arrayListLanguage = getLList();


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_language_list, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        RecyclerView rectanglesSelectedList = inflate.findViewById(R.id.rectanglesSelectedList);
        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rectanglesSelectedList.setLayoutManager(linearLayoutManager);
        AdapterSelLanguage adapterSelLanguage = new AdapterSelLanguage(this, arrayListLanguage);
        rectanglesSelectedList.setAdapter(adapterSelLanguage);

        adapterSelLanguage.setOnItemClickListener(new AdapterSelLanguage.OnItemClickListener1() {
            @Override
            public void onItemClick1(String pos, View v) throws IOException {

                removeLanuge(pos);


                adapterSelLanguage.setNewData(getLList());

            }
        });


        create1.show();


    }

    private ArrayList<String> getLList() {
        comparator = new LocaleCkUtils.LocaleComparator();
        enabledSubtypeCks = mRichImm.getEnabledSubtypes(false);
        usedLocales = getUsedLocales(enabledSubtypeCks, comparator);
        unusedLocales = getUnusedLocales(usedLocales, comparator);
        ArrayList<String> arrayListLanguage = new ArrayList<>();

        for (Locale locale : usedLocales) {
            {
                arrayListLanguage.add(locale.getDisplayName());

            }
        }
        return arrayListLanguage;
    }

    private void removeLanuge(String pos) {


        mUsedLocaleNames = new CharSequence[usedLocales.size()];
        mUsedLocaleValues = new String[usedLocales.size()];
        int i = 0;
        for (Locale locale : usedLocales) {
            final String localeString = LocaleCkUtils.getLocaleString(locale);
            mUsedLocaleValues[i] = localeString;
            mUsedLocaleNames[i] =
                    LocaleResourceCkUtils.getLocaleDisplayNameInSystemLocale(localeString);

            final SubtypeCk subtypeCk = SubtypeLocaleCkUtils.getDefaultSubtype(
                    mUsedLocaleValues[i],
                    getResources());

            if (subtypeCk.getName().contains(pos)) {

                mRichImm.removeSubtype(subtypeCk);


            }
            i++;
        }


    }

    private void getLanguageList() {
        comparator = new LocaleCkUtils.LocaleComparator();
        enabledSubtypeCks = mRichImm.getEnabledSubtypes(false);
        usedLocales = getUsedLocales(enabledSubtypeCks, comparator);
        unusedLocales = getUnusedLocales(usedLocales, comparator);


    }

    String[] sLanList = new String[]{"English (India)", "Hindi", "Marathi (India)", "Tamil (India)", "Telugu (India)", "Kannada (India)", "Malayalam (India)", "Urdu"};

    private void setFixLan() {

        ArrayList<Integer> stringArrayList = new ArrayList<>();
        stringArrayList.add(R.drawable.ck_lan_english);
        stringArrayList.add(R.drawable.ck_lan_hindi);
        stringArrayList.add(R.drawable.ck_lan_marathi);
        stringArrayList.add(R.drawable.tamil);
        stringArrayList.add(R.drawable.telugu);
        stringArrayList.add(R.drawable.kannada);
        stringArrayList.add(R.drawable.ck_malayalam);
        stringArrayList.add(R.drawable.ck_muslim);


        lanAdapter = new LanAdapter(this, stringArrayList);

        recyclerLan.setAdapter(lanAdapter);

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


    private void addLanguageToKeyboard(String s) {
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
            if (subtypeCk.getName().contains(s)) {

                mRichImm.addSubtype(subtypeCk);


            }
            i++;
        }

    }

    public boolean checkAdd(String s) {
        boolean chcek = false;
        for (Locale locale : usedLocales) {
            {
                if (locale.getDisplayName().contains(s)) {
                    chcek = true;
                    break;
                }
            }
        }
        return chcek;
    }


    private class LanAdapter extends RecyclerView.Adapter<LanAdapter.ViewHolder> {

        Context context;

        ArrayList<Integer> adArrayList;

        public LanAdapter(Context context, ArrayList<Integer> stringArrayList) {

            this.context = context;
            this.adArrayList = stringArrayList;
        }

        String[] sLanList = new String[]{"English (India)", "Hindi", "Marathi (India)", "Tamil (India)", "Telugu (India)", "Kannada (India)", "Malayalam (India)", "Urdu"};

        @NonNull
        @NotNull
        @Override
        public LanAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lan_fix_item_layout, parent, false);
            return new LanAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull LanAdapter.ViewHolder holder, int position) {
            holder.ivLanImage.setImageResource(adArrayList.get(position));


            holder.ivLanImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checkAdd(sLanList[position])) {
                        addLanguageToKeyboard(sLanList[position]);
                        holder.btnAdd.setVisibility(View.VISIBLE);
                    }


                }
            });

            if (checkAdd(sLanList[position])) {
                holder.btnAdd.setVisibility(View.VISIBLE);
            } else {
                holder.btnAdd.setVisibility(View.GONE);
            }


        }

        @Override
        public int getItemCount() {
            return adArrayList.size();
        }

        public void setNewData() {
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivLanImage, btnAdd;
            ConstraintLayout cvBg;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);

                ivLanImage = itemView.findViewById(R.id.ivLanImage);
                cvBg = itemView.findViewById(R.id.cvBg);
                btnAdd = itemView.findViewById(R.id.btnAdd);

            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        getLanguageList();
        lanAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(LanguageSelectCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }
}



