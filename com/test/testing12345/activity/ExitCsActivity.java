package com.test.testing12345.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;


public class ExitCsActivity extends Activity {
    ExitCsActivity activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.getInstance().nativeAKeboa(f, ExitCsActivity.this, c);


        findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {


        GogleAsKeboard.getInstance().showInterBackPressKeboa(ExitCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }


}
