package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.AuUsesAdapter;
import com.si_charginganimation.nilesh_charginganimation.app_uses.UsageDaily;
import com.si_charginganimation.nilesh_charginganimation.app_uses.UsageDailyTask;
import com.si_charginganimation.nilesh_charginganimation.app_uses.UsedTimeTask;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActAppUsesBinding;
import com.si_charginganimation.nilesh_charginganimation.model.TimeUsedAU;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.util.ArrayList;
import java.util.Collections;

public class AppUsesAct extends Activity {
    ActAppUsesBinding b;
    private ArrayList<TimeUsedAU> timeUsedArrayList = new ArrayList<>();

    AuUsesAdapter auUsesAdapter;
    private ShCAPreference shCAPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        b = ActAppUsesBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NatBetsAll.getInstance().natVolBetsl(f, AppUsesAct.this, c, findViewById(R.id.admobNative_Banner), findViewById(R.id.nativesmallcard));

        shCAPreference = new ShCAPreference(this);
        b.rvAuUses.setLayoutManager(new LinearLayoutManager(this));

        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setData();


    }

    private void setData() {

        Collections.sort(timeUsedArrayList, TimeUsedAU.ListLM);
        auUsesAdapter = new AuUsesAdapter(this, timeUsedArrayList);
        b.rvAuUses.setAdapter(auUsesAdapter);

    }

    public void getApp() {
        b.rvAuUses.setVisibility(View.GONE);

        this.timeUsedArrayList.clear();
        new UsageDailyTask(this, new UsageDailyTask.OnLoadUsageDaily() {

            @Override
            public final void onLoadFinish(UsageDaily usageDaily) {
                timeUsedApp(usageDaily);
            }
        }).execute(new Void[0]);
    }

    private void timeUsedApp(UsageDaily usageDaily) {
        new UsedTimeTask(usageDaily, this, new UsedTimeTask.OnLoad() {
            @Override
            public final void onLoadFinish(ArrayList arrayList) {
                updateLsit(arrayList);
            }


        }).execute(new Void[0]);
    }

    private void updateLsit(ArrayList arrayList) {

        this.timeUsedArrayList.addAll(arrayList);
        b.rvAuUses.setVisibility(View.VISIBLE);
        Collections.sort(timeUsedArrayList, TimeUsedAU.ListLM);
        auUsesAdapter.setNEwData(timeUsedArrayList);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getApp();
    }
}
