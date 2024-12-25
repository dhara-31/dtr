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

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.KeyboardCkTheme;
import com.test.testing12345.custom.settings.RadioButtonCkPreference.OnRadioButtonClickedListener;


public final class ThemeSettingsCkFragment extends SubScreenCkFragment
        implements OnRadioButtonClickedListener {
    private int mSelectedThemeId;

    static class KeyboardThemeCkPreference extends RadioButtonCkPreference {
        final int mThemeId;

        KeyboardThemeCkPreference(final Context context, final String name, final int id) {
            super(context);
            setTitle(name);
            mThemeId = id;
        }
    }

    static void updateKeyboardThemeSummary(final Preference pref) {
        final Context context = pref.getContext();
        final Resources res = context.getResources();
        final KeyboardCkTheme keyboardCkTheme = KeyboardCkTheme.getKeyboardTheme(context);
        final String[] keyboardThemeNames = res.getStringArray(R.array.keyboard_theme_names);
        final int[] keyboardThemeIds = res.getIntArray(R.array.keyboard_theme_ids);
        for (int index = 0; index < keyboardThemeIds.length; index++) {
            if (keyboardCkTheme.mThemeId == keyboardThemeIds[index]) {
                pref.setSummary(keyboardThemeNames[index]);
                return;
            }
        }
    }

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.prefs_screen_theme);
        final PreferenceScreen screen = getPreferenceScreen();
        final Context context = getActivity();
        final Resources res = getResources();
        final String[] keyboardThemeNames = res.getStringArray(R.array.keyboard_theme_names);
        final int[] keyboardThemeIds = res.getIntArray(R.array.keyboard_theme_ids);
        for (int index = 0; index < keyboardThemeIds.length; index++) {
            final KeyboardThemeCkPreference pref = new KeyboardThemeCkPreference(
                    context, keyboardThemeNames[index], keyboardThemeIds[index]);
            screen.addPreference(pref);
            pref.setOnRadioButtonClickedListener(this);
        }
        final KeyboardCkTheme keyboardCkTheme = KeyboardCkTheme.getKeyboardTheme(context);
        mSelectedThemeId = keyboardCkTheme.mThemeId;
    }

    @Override
    public void onRadioButtonClicked(final RadioButtonCkPreference preference) {
        if (preference instanceof KeyboardThemeCkPreference) {
            final KeyboardThemeCkPreference pref = (KeyboardThemeCkPreference)preference;
            mSelectedThemeId = pref.mThemeId;
            updateSelected();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSelected();
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardCkTheme.saveKeyboardThemeId(mSelectedThemeId, getSharedPreferences());
        SettingsCk.removeKeyboardColor(getSharedPreferences());
    }

    private void updateSelected() {
        final PreferenceScreen screen = getPreferenceScreen();
        final int count = screen.getPreferenceCount();
        for (int index = 0; index < count; index++) {
            final Preference preference = screen.getPreference(index);
            if (preference instanceof KeyboardThemeCkPreference) {
                final KeyboardThemeCkPreference pref = (KeyboardThemeCkPreference)preference;
                final boolean selected = (mSelectedThemeId == pref.mThemeId);
                pref.setSelected(selected);
            }
        }
    }
}
