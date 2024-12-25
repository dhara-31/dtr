package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActStartBinding;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

public class StartAct extends Activity {
    ActStartBinding b;

    ShCAPreference shCAPreference;
    private int themeColor;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 500;
    ConstraintLayout btShare;
    ConstraintLayout btRate;
    TextView tvPrivacyP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActStartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        shCAPreference = new ShCAPreference(this);

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NatBetsAll.getInstance().natVolBetsl(f, StartAct.this, c, findViewById(R.id.admobNative_Banner), findViewById(R.id.nativesmallcard));


        btShare = findViewById(R.id.btShare);
        btRate = findViewById(R.id.btRate);
        tvPrivacyP = findViewById(R.id.tvPrivacyP);
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (java.lang.Exception e) {

                }
            }
        });
        btRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StartAct.this, " unable to find app", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
        });
        tvPrivacyP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (DBettryplication.getInstance().getAppDetail() != null) {
                        if (DBettryplication.getInstance().getAppDetail().getPrivacy() != null && !DBettryplication.getInstance().getAppDetail().getPrivacy().trim().isEmpty()) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(DBettryplication.getInstance().getAppDetail().getPrivacy()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setPackage("com.android.chrome");
                            try {
                                startActivity(i);
                            } catch (ActivityNotFoundException e) {
                                i.setPackage(null);
                                startActivity(i);
                            }
                        } else {
                            Toast.makeText(StartAct.this, "Unable to open", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StartAct.this, "Unable to open", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(StartAct.this, "Unable to open", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b.btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;

                    openMYNextAct();

            }

        });

        setThemeApp();

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
        b.tvStart.setTextColor(themeColor);

    }

    private BroadcastReceiver cBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {

            int level = intent.getIntExtra("level", 0);

            if (level > 74) {
                shCAPreference.setThemeType(4);
            } else if (level > 49) {
                shCAPreference.setThemeType(3);
            } else if (level > 24) {
                shCAPreference.setThemeType(2);
            } else if (level > 0) {
                shCAPreference.setThemeType(1);
            }
            setThemeApp();
        }

    };


    private void openMYNextAct() {

            GoChBetryNils.getInstance().showChBetryNilster(StartAct.this, new GoChBetryNils.AChBetryNilInterface() {
                @Override
                public void aChBetryNilsCall() {
                    Intent intent = new Intent(StartAct.this, MainAct.class);
                    startActivity(intent);
                }
            });


    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(this.cBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    @Override
    protected void onPause() {
        try {
            if (this.cBatInfoReceiver != null) {
                unregisterReceiver(this.cBatInfoReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StartAct.this, ExitAct.class);
        startActivity(intent);
    }
}
