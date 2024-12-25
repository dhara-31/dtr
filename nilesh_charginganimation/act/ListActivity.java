package com.si_charginganimation.nilesh_charginganimation.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.magicfluids.MainActivity;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

public class ListActivity extends AppCompatActivity {

    ImageView a1, a2, a3, a4, a5, a6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity);

        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        a5 = findViewById(R.id.a5);
        a6 = findViewById(R.id.a6);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        ImageView btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NatBetsAll.getInstance().natVolBetsl(f, ListActivity.this, c, findViewById(R.id.admobNative_Banner), findViewById(R.id.nativesmallcard));


        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoChBetryNils.getInstance().showChBetryNilster(ListActivity.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(ListActivity.this, MainActivity.class).putExtra("v", 0));

                    }
                });
             }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoChBetryNils.getInstance().showChBetryNilster(ListActivity.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(ListActivity.this, MainActivity.class).putExtra("v", 9));

                    }
                });


            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoChBetryNils.getInstance().showChBetryNilster(ListActivity.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(ListActivity.this, MainActivity.class).putExtra("v", 11));

                    }
                });

             }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoChBetryNils.getInstance().showChBetryNilster(ListActivity.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(ListActivity.this, MainActivity.class).putExtra("v", 9));

                    }
                });
              }
        });
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoChBetryNils.getInstance().showChBetryNilster(ListActivity.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(ListActivity.this, MainActivity.class).putExtra("v", 16));
                    }
                });
             }
        });
        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoChBetryNils.getInstance().showChBetryNilster(ListActivity.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(ListActivity.this, MainActivity.class).putExtra("v", 4));

                    }
                });
                 }
        });

    }
}
