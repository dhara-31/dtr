package com.test.testing12345.adsclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.test.testing12345.R;
import com.test.testing12345.activity.ExitCsActivity;

import java.util.List;

public class IntroScreen2CsActivityKeboa extends Activity {
    TextView tvBtn;
    ConstraintLayout cvBtn;
    private long lClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro_screen_2);
        tvBtn = findViewById(R.id.tvBtn);

        cvBtn = findViewById(R.id.constraintLayoutBtn);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, IntroScreen2CsActivityKeboa.this , c);

        cvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lClickTime < 500) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();

                if (isInputEnabled()) {
                    if (!checkIsDefault()) {

                        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .showInputMethodPicker();
                    } else {
                        GogleAsKeboard.getInstance().showInterKeboa(IntroScreen2CsActivityKeboa.this, new GogleAsKeboard.AdsInterface() {
                            @Override
                            public void adsCall() {
                                Intent intent = new Intent(IntroScreen2CsActivityKeboa.this, Intro3CsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                } else {
                    Toast.makeText(IntroScreen2CsActivityKeboa.this, "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    public boolean checkIsDefault() {
        String string = Settings.Secure.getString(getContentResolver(), "default_input_method");
        return string.contains(getPackageName());
    }


    public boolean isInputEnabled() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();
        boolean isInputEnabled = false;

        for (int i = 0; i < N; i++) {

            InputMethodInfo imi = mInputMethodProperties.get(i);
            if (imi.getId().contains(getPackageName())) {
                isInputEnabled = true;
            }
        }

        if (isInputEnabled) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        manageBtnStates();

    }

    private void manageBtnStates() {
        if (!checkIsDefault()) {
            tvBtn.setText("Set Keyboard");
        } else {
            tvBtn.setText("Continue");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(IntroScreen2CsActivityKeboa.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                Intent intent = new Intent(IntroScreen2CsActivityKeboa.this, ExitCsActivity.class);
                startActivity(intent);
            }
        });
    }
}
