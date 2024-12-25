/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.testing12345.custom;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.View;

import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.settings.SettingsCkValues;


public final class AudioAndHapticFeedbackCkManager {
    private AudioManager mAudioManager;
    private Vibrator mVibrator;

    private SettingsCkValues mSettingsCkValues;
    private boolean mSoundOn;

    private static final AudioAndHapticFeedbackCkManager sInstance =
            new AudioAndHapticFeedbackCkManager();

    public static AudioAndHapticFeedbackCkManager getInstance() {
        return sInstance;
    }

    private AudioAndHapticFeedbackCkManager() {

    }

    public static void init(final Context context) {
        sInstance.initInternal(context);
    }

    private void initInternal(final Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public boolean hasVibrator() {
        return mVibrator != null && mVibrator.hasVibrator();
    }

    public void vibrate(final long milliseconds) {
        if (mVibrator == null) {
            return;
        }
        mVibrator.vibrate(milliseconds);
    }

    private boolean reevaluateIfSoundIsOn() {
        if (mSettingsCkValues == null || !mSettingsCkValues.mSoundOn || mAudioManager == null) {
            return false;
        }
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public void performAudioFeedback(final int code) {
         if (mAudioManager == null) {
            return;
        }
        if (!mSoundOn) {
            return;
        }
        final int sound;
        switch (code) {
        case ConstantsCk.CODE_DELETE:
            sound = AudioManager.FX_KEYPRESS_DELETE;
            break;
        case ConstantsCk.CODE_ENTER:
            sound = AudioManager.FX_KEYPRESS_RETURN;
            break;
        case ConstantsCk.CODE_SPACE:
            sound = AudioManager.FX_KEYPRESS_SPACEBAR;
            break;
        default:
            sound = AudioManager.FX_KEYPRESS_STANDARD;
            break;
        }
        mAudioManager.playSoundEffect(sound, mSettingsCkValues.mKeypressSoundVolume);
    }

    public void performHapticFeedback(final View viewToPerformHapticFeedbackOn) {
        if (!mSettingsCkValues.mVibrateOn) {
            return;
        }
        if (mSettingsCkValues.mKeypressVibrationDuration >= 0) {
            vibrate(mSettingsCkValues.mKeypressVibrationDuration);
            return;
        }
         if (viewToPerformHapticFeedbackOn != null) {
            viewToPerformHapticFeedbackOn.performHapticFeedback(
                    HapticFeedbackConstants.KEYBOARD_TAP,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }
    }

    public void onSettingsChanged(final SettingsCkValues settingsCkValues) {
        mSettingsCkValues = settingsCkValues;
        mSoundOn = reevaluateIfSoundIsOn();
    }

    public void onRingerModeChanged() {
        mSoundOn = reevaluateIfSoundIsOn();
    }
}
