package com.test.testing12345.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.AppDetailKeboa;
import com.test.testing12345.adsclass.DKeboaApplication;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.IntroScreen2CsActivityKeboa;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class IntroScreen1CsActivity extends Activity {
    TextView tvPp, tvBtn, textView1, textView2;
    ConstraintLayout cvBtn;
    private long lClickTime = 0;
    public static final int MULTIPLE_PERMISSIONS = 10001;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen_1);

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, IntroScreen1CsActivity.this, c);

        tvBtn = findViewById(R.id.tvBtn);

        tvPp = findViewById(R.id.tvPp);
        cvBtn = findViewById(R.id.constraintLayoutBtn);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);


        cvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lClickTime < 500) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();


                if (checkAllPermissions()) {
                    goNext();
                }
            }
        });

        setText();

    }

    private void goNext() {

        if (!isInputEnabled()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
            startActivityForResult(intent, 0);
        } else {
            GogleAsKeboard.getInstance().showInterKeboa(IntroScreen1CsActivity.this, new GogleAsKeboard.AdsInterface() {
                @Override
                public void adsCall() {
                    Intent intent = new Intent(IntroScreen1CsActivity.this, IntroScreen2CsActivityKeboa.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }

    private boolean checkAllPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(IntroScreen1CsActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                int grant = 0;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PERMISSION_GRANTED) {
                        grant++;
                    }
                }

                if (grant == grantResults.length) {
                    goNext();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                        permissionDialog();
                    }
                    Toast.makeText(IntroScreen1CsActivity.this, "Please Allow", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void permissionDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_permission, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        TextView tvOk = inflate.findViewById(R.id.tvAllow);
        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();


                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });


        create1.show();

    }


    private void setText() {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String black = getResources().getString(R.string.pp_text);
        SpannableString redSpannable = new SpannableString(black);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, black.length(), 0);
        builder.append(redSpannable);
        String blue = getResources().getString(R.string.pp_link);
        SpannableString whiteSpannable = new SpannableString(blue);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, blue.length(), 0);
        whiteSpannable.setSpan(new UnderlineSpan(), 1, blue.length(), 0);
        builder.append(whiteSpannable);
        tvPp.setText(builder, TextView.BufferType.SPANNABLE);


        tvPp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();

                if (appDetail != null) {
                    if (appDetail.getPrivacy().equals("")) {
                        Toast.makeText(IntroScreen1CsActivity.this, "Url not found...", Toast.LENGTH_SHORT).show();
                    } else {
                        String url = appDetail.getPrivacy();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(IntroScreen1CsActivity.this, "Url not found...", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    protected void onResume() {
        super.onResume();
        if (!isInputEnabled()) {
            tvBtn.setText("Enable");
        } else {
            tvBtn.setText("Continue");
        }
    }


    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(IntroScreen1CsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                Intent intent = new Intent(IntroScreen1CsActivity.this, ExitCsActivity.class);
                startActivity(intent);
            }
        });

    }
}
