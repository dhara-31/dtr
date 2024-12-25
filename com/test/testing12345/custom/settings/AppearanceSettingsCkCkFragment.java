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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.KeyboardCkTheme;


public final class AppearanceSettingsCkCkFragment extends SubScreenCkFragment {
    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.prefs_screen_appearance);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            removePreference(SettingsCk.PREF_MATCHING_NAVBAR_COLOR);
        }

        setupKeyboardHeightSettings();
        setupKeyboardColorSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSettings();
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences prefs, final String key) {
        refreshSettings();
    }

    private void refreshSettings() {
        ThemeSettingsCkFragment.updateKeyboardThemeSummary(findPreference(SettingsCk.SCREEN_THEME));

        final SharedPreferences prefs = getSharedPreferences();
        final KeyboardCkTheme theme = KeyboardCkTheme.getKeyboardTheme(prefs);
        final boolean isSystemTheme = theme.mThemeId != KeyboardCkTheme.THEME_ID_ZERO
                && theme.mThemeId != KeyboardCkTheme.THEME_ID_ZERO;
        setPreferenceEnabled(SettingsCk.PREF_KEYBOARD_COLOR, isSystemTheme);
    }

    private void setupKeyboardHeightSettings() {
        final SeekBarDialogCkPreference pref = (SeekBarDialogCkPreference)findPreference(
                SettingsCk.PREF_KEYBOARD_HEIGHT);
        if (pref == null) {
            return;
        }
        final SharedPreferences prefs = getSharedPreferences();
        final Resources res = getResources();
        pref.setInterface(new SeekBarDialogCkPreference.ValueProxy() {
            private static final float PERCENTAGE_FLOAT = 100.0f;

            private float getValueFromPercentage(final int percentage) {
                return percentage / PERCENTAGE_FLOAT;
            }

            private int getPercentageFromValue(final float floatValue) {
                return Math.round(floatValue * PERCENTAGE_FLOAT);
            }

            @Override
            public void writeValue(final int value, final String key) {
                prefs.edit().putFloat(key, getValueFromPercentage(value)).apply();
            }

            @Override
            public void writeDefaultValue(final String key) {
                prefs.edit().remove(key).apply();
            }

            @Override
            public int readValue(final String key) {
                return getPercentageFromValue(SettingsCk.readKeyboardHeight(prefs, 1));
            }

            @Override
            public int readDefaultValue(final String key) {
                return getPercentageFromValue(1);
            }

            @SuppressLint("StringFormatMatches")
            @Override
            public String getValueText(final int value) {
                if (value < 0) {
                    return res.getString(R.string.settings_system_default);
                }
                return res.getString(R.string.abbreviation_unit_percent, value);
            }

            @Override
            public void feedbackValue(final int value) {}
        });
    }

    private void setupKeyboardColorSettings() {
        final ColorDialogCkPreference pref = (ColorDialogCkPreference)findPreference(
                SettingsCk.PREF_KEYBOARD_COLOR);
        if (pref == null) {
            return;
        }
        final SharedPreferences prefs = getSharedPreferences();
        final Context context = this.getActivity().getApplicationContext();
        pref.setInterface(new ColorDialogCkPreference.ValueProxy() {
            @Override
            public void writeValue(final int value, final String key) {
                prefs.edit().putInt(key, value).apply();
            }

            @Override
            public int readValue(final String key) {
                return SettingsCk.readKeyboardColor(prefs, context);
            }

            @Override
            public void writeDefaultValue(final String key) {
                SettingsCk.removeKeyboardColor(prefs);
            }
        });
    }
}
