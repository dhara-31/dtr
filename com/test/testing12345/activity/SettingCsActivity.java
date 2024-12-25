package com.test.testing12345.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.GogleAsKeboard;
import com.test.testing12345.adsclass.NativeAdsAllKeboa;
import com.test.testing12345.compat.PreferenceCkManagerCompat;
import com.test.testing12345.keyboard.KeyboardLayoutCkSet;
import com.test.testing12345.custom.settings.SettingsCk;

public class SettingCsActivity extends Activity {
        SharedPreferences sharedPreferences;
    SwitchCompat scNumRow, scLanKey,scSpaceSwipe,scDeleteSwipe,scPopUp,scVibrate,scSound;
    SharedPreferences.Editor edit;
    ConstraintLayout cvVibrateDuration,cvSound,cvPopup,cvHeight;
    TextView tvVbDuration,tvSoundVol,tvPopupTime,tvHeight;
    float PERCENTAGE_FLOAT = 100.0f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        scNumRow = findViewById(R.id.switchNumRow);
        scLanKey = findViewById(R.id.switchLanKey);
        scSpaceSwipe = findViewById(R.id.switchSpaceSwipe);
        scDeleteSwipe = findViewById(R.id.switchDeleteSwipe);
        scPopUp = findViewById(R.id.switchPopUp);
        scVibrate = findViewById(R.id.switchVibration);
        cvVibrateDuration = findViewById(R.id.cvVibrateDuration);
        tvVbDuration = findViewById(R.id.tvDurationValue);
        tvSoundVol = findViewById(R.id.tvSoundVol);
        cvSound = findViewById(R.id.cvSoundVol);
        scSound = findViewById(R.id.switchSound);
        cvPopup = findViewById(R.id.cvPopup);
        tvPopupTime = findViewById(R.id.tvPopupTime);
        cvHeight = findViewById(R.id.cvHight);
        tvHeight = findViewById(R.id.tvHeight);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView c = findViewById(R.id.c);
        NativeAdsAllKeboa.banerAllShowKeboa(admobNativeLarge2, SettingCsActivity.this , c);


        sharedPreferences =  PreferenceCkManagerCompat.getDeviceSharedPreferences(this);
        edit = sharedPreferences.edit();



       scNumRow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(scNumRow.isChecked()){
                   edit.putBoolean(SettingsCk.PREF_SHOW_NUMBER_ROW,true);
                   edit.commit();
                   KeyboardLayoutCkSet.onKeyboardThemeChanged();
               }else {
                   edit.putBoolean(SettingsCk.PREF_SHOW_NUMBER_ROW,false);
                   edit.commit();
                   KeyboardLayoutCkSet.onKeyboardThemeChanged();
               }
           }
       });
        scLanKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scLanKey.isChecked()){
                    edit.putBoolean(SettingsCk.PREF_HIDE_LANGUAGE_SWITCH_KEY,true);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }else {
                    edit.putBoolean(SettingsCk.PREF_HIDE_LANGUAGE_SWITCH_KEY,false);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }
            }
        });
        scSpaceSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scSpaceSwipe.isChecked()){
                    edit.putBoolean(SettingsCk.PREF_SPACE_SWIPE,true);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }else {
                    edit.putBoolean(SettingsCk.PREF_SPACE_SWIPE,false);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }
            }
        });
        scDeleteSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scDeleteSwipe.isChecked()){
                    edit.putBoolean(SettingsCk.PREF_DELETE_SWIPE,true);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }else {
                    edit.putBoolean(SettingsCk.PREF_DELETE_SWIPE,false);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }
//
            }
        });
        scPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scPopUp.isChecked()){
                    edit.putBoolean(SettingsCk.PREF_POPUP_ON,true);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }else {
                    edit.putBoolean(SettingsCk.PREF_POPUP_ON,false);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }

            }
        });
        scVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scVibrate.isChecked()){
                    edit.putBoolean(SettingsCk.PREF_VIBRATE_ON,true);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }else {
                    edit.putBoolean(SettingsCk.PREF_VIBRATE_ON,false);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }
            }
        });
        scSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scSound.isChecked()){
                    edit.putBoolean(SettingsCk.PREF_SOUND_ON,true);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }else {
                    edit.putBoolean(SettingsCk.PREF_SOUND_ON,false);
                    edit.commit();
                    KeyboardLayoutCkSet.onKeyboardThemeChanged();
                }
            }
        });
        cvVibrateDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVibrationDialog();
            }
        });
         cvSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSoundDialog();
            }
        });
        cvPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpdDialog();
            }
        });
        cvHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHeightDialog();
            }
        });
        setSwitch();

    }




    private void setSwitch() {

        if(SettingsCk.readShowNumberRow(sharedPreferences)){
            scNumRow.setChecked(true);
        }else {
            scNumRow.setChecked(false);
        }

        if(SettingsCk.readShowLanguageSwitchKey(sharedPreferences)){
            scLanKey.setChecked(false);
        }else {
            scLanKey.setChecked(true);
        }
        if(SettingsCk.readSpaceSwipeEnabled(sharedPreferences)){
            scSpaceSwipe.setChecked(true);
        }else {
            scSpaceSwipe.setChecked(false);
        }
        if(SettingsCk.readDeleteSwipeEnabled(sharedPreferences)){
            scDeleteSwipe.setChecked(true);
        }else {
            scDeleteSwipe.setChecked(false);
        }
        if(SettingsCk.readKeyPreviewPopupEnabled(sharedPreferences,getResources())){
            scPopUp.setChecked(true);
        }else {
            scPopUp.setChecked(false);
        }
        if(SettingsCk.readVibrationEnabled(sharedPreferences,getResources())){
            scVibrate.setChecked(true);
        }else {
            scVibrate.setChecked(false);
        } if(SettingsCk.readKeypressSoundEnabled(sharedPreferences,getResources())){
            scSound.setChecked(true);
        }else {
            scSound.setChecked(false);
        }

        tvVbDuration.setText(SettingsCk.readKeypressVibrationDuration(sharedPreferences,getResources())+"ms");
        tvSoundVol.setText(getPercentageFromValue(SettingsCk.readKeypressSoundVolume(sharedPreferences,getResources()))+"");
        tvPopupTime.setText(SettingsCk.readKeyLongpressTimeout(sharedPreferences,getResources())+"ms");
        tvHeight.setText(getPercentageFromValue(SettingsCk.readKeyboardHeight(sharedPreferences, 1))+"%");

    }



    private void openVibrationDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_vibraate
                , (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(true);

        TextView tvOk = inflate.findViewById(R.id.tvOk);
        TextView tvCancel = inflate.findViewById(R.id.tvCancel);
        TextView tvValue = inflate.findViewById(R.id.tvDurationValue);
        SeekBar seekBarVb = inflate.findViewById(R.id.seekBarVb);
        int oldValue = SettingsCk.readKeypressVibrationDuration(sharedPreferences,getResources());

        tvValue.setText(oldValue+"ms");
        seekBarVb.setProgress(oldValue);

        AlertDialog vbCreate1 = builder1.create();
        vbCreate1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putInt(SettingsCk.PREF_VIBRATION_DURATION_SETTINGS,seekBarVb.getProgress());
                edit.commit();
                vbCreate1.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vbCreate1.dismiss();
            }
        });
        seekBarVb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvValue.setText(progress+"ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        vbCreate1.show();


    }

    private void openSoundDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_vibraate
                , (ViewGroup) null);
        builder2.setView(inflate);
        builder2.setCancelable(true);

        TextView tvOk = inflate.findViewById(R.id.tvOk);
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);
        TextView tvCancel = inflate.findViewById(R.id.tvCancel);
        TextView tvValue = inflate.findViewById(R.id.tvDurationValue);
        SeekBar seekBarSound = inflate.findViewById(R.id.seekBarVb);
        tvLabel.setText("Keypress sound volume");
        int oldValue = getPercentageFromValue(SettingsCk.readKeypressSoundVolume(sharedPreferences,getResources()));

        tvValue.setText(oldValue+"");
        seekBarSound.setProgress(oldValue);

        AlertDialog soundCreate1 = builder2.create();
        soundCreate1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit.putFloat(SettingsCk.PREF_KEYPRESS_SOUND_VOLUME,getValueFromPercentage(seekBarSound.getProgress()));
                edit.commit();
                soundCreate1.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundCreate1.dismiss();
            }
        });





        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvValue.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        soundCreate1.show();

    }

    private void openPopUpdDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_pop_up
                , (ViewGroup) null);
        builder3.setView(inflate);
        builder3.setCancelable(true);

        TextView tvOk = inflate.findViewById(R.id.tvOk);
        TextView tvCancel = inflate.findViewById(R.id.tvCancel);
        TextView tvValue = inflate.findViewById(R.id.tvDurationValue);
        SeekBar seekBarVb = inflate.findViewById(R.id.seekBarVb);
        int oldValue = SettingsCk.readKeyLongpressTimeout(sharedPreferences,getResources());

        tvValue.setText(oldValue+"ms");
        seekBarVb.setProgress(oldValue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {




        }
        AlertDialog popCreate1 = builder3.create();
        popCreate1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putInt(SettingsCk.PREF_KEY_LONGPRESS_TIMEOUT,seekBarVb.getProgress());
                edit.commit();
                popCreate1.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popCreate1.dismiss();
            }
        });
        seekBarVb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvValue.setText(progress+"ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        popCreate1.show();
    }
    private void openHeightDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_height
                , (ViewGroup) null);
        builder2.setView(inflate);
        builder2.setCancelable(true);
        TextView tvOk = inflate.findViewById(R.id.tvOk);
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);
        TextView tvCancel = inflate.findViewById(R.id.tvCancel);
        TextView tvValue = inflate.findViewById(R.id.tvDurationValue);
        SeekBar seekBarHeight = inflate.findViewById(R.id.seekBarVb);

        int oldValue = getPercentageFromValue(SettingsCk.readKeyboardHeight(sharedPreferences,1));

        tvValue.setText(oldValue+"%");
        int old = (45+oldValue)-120;
        seekBarHeight.setProgress(old);

        AlertDialog hCreate1 = builder2.create();
        hCreate1.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit.putFloat(SettingsCk.PREF_KEYBOARD_HEIGHT,getValueFromPercentage(getP(seekBarHeight.getProgress())));
                edit.commit();
                KeyboardLayoutCkSet.onKeyboardThemeChanged();

                hCreate1.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hCreate1.dismiss();
            }
        });

        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvValue.setText(getP(progress)+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        hCreate1.show();

    }

    private int getP(int progress) {
        int a=75;
        if(progress==0){
            a=75;
        }else if(progress==1){
            a=76;
        }else if(progress==2){
            a=77;
        }else if(progress==3){
            a=78;
        }else if(progress==4){
            a=79;
        }else if(progress==5){
            a=80;
        }else if(progress==6){
            a=81;
        }else if(progress==7){
            a=82;
        }else if(progress==8){
            a=83;
        }else if(progress==9){
            a=84;
        }else if(progress==10){
            a=85;
        }else if(progress==11){
            a=86;
        }else if(progress==12){
            a=87;
        }else if(progress==13){
            a=88;
        }else if(progress==14){
            a=89;
        }else if(progress==15){
            a=90;
        }else if(progress==16){
            a=91;
        }else if(progress==17){
            a=92;
        }else if(progress==18){
            a=93;
        }else if(progress==19){
            a=94;
        }else if(progress==20){
            a=95;
        }else if(progress==21){
            a=96;
        }else if(progress==22){
            a=97;
        }else if(progress==23){
            a=98;
        }else if(progress==24){
            a=99;
        }else if(progress==25){
            a=100;
        }else if(progress==26){
            a=101;
        }else if(progress==27){
            a=102;
        }else if(progress==28){
            a=103;
        }else if(progress==29){
            a=104;
        }else if(progress==30){
            a=105;
        }else if(progress==31){
            a=106;
        }else if(progress==32){
            a=107;
        }else if(progress==33){
            a=108;
        }else if(progress==34){
            a=109;
        }else if(progress==35){
            a=110;
        }else if(progress==36){
            a=111;
        }else if(progress==37){
            a=112;
        }else if(progress==38){
            a=113;
        }else if(progress==39){
            a=114;
        }else if(progress==40){
            a=115;
        }else if(progress==41){
            a=116;
        }else if(progress==42){
            a=117;
        }else if(progress==43){
            a=118;
        }else if(progress==44){
            a=119;
        }else if(progress==45){
            a=120;
        }
return  a;

    }

    private float getValueFromPercentage(final int percentage) {
        return percentage / PERCENTAGE_FLOAT;
    }

    private int getPercentageFromValue(final float floatValue) {
        return (int)(floatValue * PERCENTAGE_FLOAT);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        tvVbDuration.setText(SettingsCk.readKeypressVibrationDuration(sharedPreferences,getResources())+"ms");
        tvPopupTime.setText(SettingsCk.readKeyLongpressTimeout(sharedPreferences,getResources())+"ms");
        tvSoundVol.setText(getPercentageFromValue(SettingsCk.readKeypressSoundVolume(sharedPreferences,getResources()))+"");
        tvHeight.setText(getPercentageFromValue(SettingsCk.readKeyboardHeight(sharedPreferences, 1))+"%");

    }


    @Override
    public void onBackPressed() {
        GogleAsKeboard.getInstance().showInterBackPressKeboa(SettingCsActivity.this, new GogleAsKeboard.AdsInterface() {
            @Override
            public void adsCall() {
                finish();
            }
        });
    }
}
