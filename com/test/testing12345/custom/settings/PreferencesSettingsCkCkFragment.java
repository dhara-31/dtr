/*
 * Copyright (C) 2014 The Android Open Source Project
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
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.KeyboardLayoutCkSet;


public final class PreferencesSettingsCkCkFragment extends SubScreenCkFragment {
    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.prefs_screen_preferences);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            removePreference(SettingsCk.PREF_ENABLE_IME_SWITCH);
        } else {
            updateImeSwitchEnabledPref();
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences prefs, final String key) {
        if (key.equals(SettingsCk.PREF_HIDE_SPECIAL_CHARS) ||
                key.equals(SettingsCk.PREF_SHOW_NUMBER_ROW)) {
            KeyboardLayoutCkSet.onKeyboardThemeChanged();
        } else if (key.equals(SettingsCk.PREF_HIDE_LANGUAGE_SWITCH_KEY)) {
            updateImeSwitchEnabledPref();
        }
    }


    private void updateImeSwitchEnabledPref() {
        final Preference enableImeSwitch = findPreference(SettingsCk.PREF_ENABLE_IME_SWITCH);
        final Preference hideLanguageSwitchKey =
                findPreference(SettingsCk.PREF_HIDE_LANGUAGE_SWITCH_KEY);
        if (enableImeSwitch == null || hideLanguageSwitchKey == null) {
            return;
        }
        final boolean hideLanguageSwitchKeyIsChecked;
        // depending on the version of Android, the preferences could be different types
        if (hideLanguageSwitchKey instanceof CheckBoxPreference) {
            hideLanguageSwitchKeyIsChecked =
                    ((CheckBoxPreference)hideLanguageSwitchKey).isChecked();
        } else if (hideLanguageSwitchKey instanceof SwitchPreference) {
            hideLanguageSwitchKeyIsChecked =
                    ((SwitchPreference)hideLanguageSwitchKey).isChecked();
        } else {
            // in case it can be something else, don't bother doing anything
            return;
        }
        enableImeSwitch.setEnabled(!hideLanguageSwitchKeyIsChecked);
    }
}
