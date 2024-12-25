/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.test.testing12345.custom.settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.inputmethod.EditorInfo;

import com.test.testing12345.R;
import com.test.testing12345.custom.InputCkAttributes;

 public class SettingsCkValues {
    public static final float DEFAULT_SIZE_SCALE = 1.0f; // 100%
    public   int mKeyboardResizedLeftPadding;
    public   int mKeyboardResizedRightPadding;

     public final SpacingAndCkPunctuations mSpacingAndCkPunctuations;
     public final boolean mHasHardwareKeyboard;
    public final int mDisplayOrientation;
     public final boolean mAutoCap;
    public final boolean mVibrateOn;
    public final boolean mSoundOn;
    public final boolean mKeyPreviewPopupOn;
    public final boolean mShowsLanguageSwitchKey;
    public final boolean mImeSwitchEnabled;
    public final int mKeyLongpressTimeout;
    public final boolean mHideSpecialChars;
    public final boolean mShowNumberRow;
    public final boolean mSpaceSwipeEnabled;
    public final boolean mDeleteSwipeEnabled;
    public final boolean mUseMatchingNavbarColor;

     public final InputCkAttributes mInputCkAttributes;

     public final int mKeypressVibrationDuration;
    public final float mKeypressSoundVolume;
    public final int mKeyPreviewPopupDismissDelay;

     public final float mKeyboardHeightScale;

    public SettingsCkValues(final SharedPreferences prefs, final Resources res,
                            final InputCkAttributes inputCkAttributes) {
         mSpacingAndCkPunctuations = new SpacingAndCkPunctuations(res);

         mInputCkAttributes = inputCkAttributes;
        this.mKeyboardResizedLeftPadding = prefs.getInt( "pref_resized_keyboard_padding_left", 0);
        this.mKeyboardResizedRightPadding = prefs.getInt("pref_resized_keyboard_padding_right", 0);

         mAutoCap = prefs.getBoolean(SettingsCk.PREF_AUTO_CAP, true);
        mVibrateOn = SettingsCk.readVibrationEnabled(prefs, res);
        mSoundOn = SettingsCk.readKeypressSoundEnabled(prefs, res);
        mKeyPreviewPopupOn = SettingsCk.readKeyPreviewPopupEnabled(prefs, res);
        mShowsLanguageSwitchKey = SettingsCk.readShowLanguageSwitchKey(prefs);
        mImeSwitchEnabled = SettingsCk.readEnableImeSwitch(prefs);
        mHasHardwareKeyboard = SettingsCk.readHasHardwareKeyboard(res.getConfiguration());

         mKeyLongpressTimeout = SettingsCk.readKeyLongpressTimeout(prefs, res);
        mKeypressVibrationDuration = SettingsCk.readKeypressVibrationDuration(prefs, res);
        mKeypressSoundVolume = SettingsCk.readKeypressSoundVolume(prefs, res);
        mKeyPreviewPopupDismissDelay = res.getInteger(R.integer.config_key_preview_linger_timeout);
        mKeyboardHeightScale = SettingsCk.readKeyboardHeight(prefs, DEFAULT_SIZE_SCALE);
        mDisplayOrientation = res.getConfiguration().orientation;
        mHideSpecialChars = SettingsCk.readHideSpecialChars(prefs);
        mShowNumberRow = SettingsCk.readShowNumberRow(prefs);
        mSpaceSwipeEnabled = SettingsCk.readSpaceSwipeEnabled(prefs);
        mDeleteSwipeEnabled = SettingsCk.readDeleteSwipeEnabled(prefs);
        mUseMatchingNavbarColor = SettingsCk.readUseMatchingNavbarColor(prefs);
    }

    public boolean isWordSeparator(final int code) {
        return mSpacingAndCkPunctuations.isWordSeparator(code);
    }

    public boolean isLanguageSwitchKeyDisabled() {
        return !mShowsLanguageSwitchKey;
    }

    public boolean isSameInputType(final EditorInfo editorInfo) {
        return mInputCkAttributes.isSameInputType(editorInfo);
    }

    public boolean hasSameOrientation(final Configuration configuration) {
        return mDisplayOrientation == configuration.orientation;
    }
}
