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

package com.test.testing12345.keyboard;

import android.content.Context;
import android.content.SharedPreferences;

import com.test.testing12345.R;
import com.test.testing12345.compat.PreferenceCkManagerCompat;
import com.test.testing12345.custom.settings.SettingsCk;

public final class KeyboardCkTheme {
    private static final String TAG = KeyboardCkTheme.class.getSimpleName();

    static final String KEYBOARD_THEME_KEY = "pref_keyboard_theme_12341234";


     public static final int THEME_ID_ZERO = 0;
    public static final int THEME_ID_ONE = 1;
    public static final int THEME_ID_TWO = 2;
    public static final int THEME_ID_THREE = 3;



    public static final int THEME_ID_FOUR = 4;
    public static final int THEME_ID_FIVE = 5;
    public static final int THEME_ID_CUSTOM = 6;
    public static final int THEME_ID_CUSTOM2 = 7;




    static final KeyboardCkTheme[] KEYBOARD_THEMES = {
            new KeyboardCkTheme(THEME_ID_ZERO, "THEME_ID_ZERO", R.style.KeyboardTheme_THEME_ID_ZERO),
            new KeyboardCkTheme(THEME_ID_ONE, "THEME_ID_ONE", R.style.KeyboardTheme_THEME_ID_ONE),
          new KeyboardCkTheme(THEME_ID_TWO, "THEME_ID_TWO", R.style.KeyboardTheme_THEME_ID_TWO),
          new KeyboardCkTheme(THEME_ID_THREE, "THEME_ID_THREE", R.style.KeyboardTheme_THEME_ID_THREE),
          new KeyboardCkTheme(THEME_ID_FOUR, "THEME_ID_FOUR", R.style.KeyboardTheme_THEME_ID_FOUR),
         new KeyboardCkTheme(THEME_ID_FIVE, "THEME_ID_FIVE", R.style.KeyboardTheme_THEME_ID_FIVE),
        new KeyboardCkTheme(THEME_ID_CUSTOM, "THEME_ID_CUSTOM", R.style.KeyboardTheme_THEME_ID_CUSTOM),
        new KeyboardCkTheme(THEME_ID_CUSTOM2, "THEME_ID_CUSTOM", R.style.KeyboardTheme_THEME_ID_CUSTOM),
     };

    public final int mThemeId;
    public final int mStyleId;
    public final String mThemeName;

    private KeyboardCkTheme(final int themeId, final String themeName, final int styleId) {
        mThemeId = themeId;
        mThemeName = themeName;
        mStyleId = styleId;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        return (o instanceof KeyboardCkTheme) && ((KeyboardCkTheme)o).mThemeId == mThemeId;
    }

    @Override
    public int hashCode() {
        return mThemeId;
    }

     static KeyboardCkTheme searchKeyboardThemeById(final int themeId) {
         for (final KeyboardCkTheme theme : KEYBOARD_THEMES) {
            if (theme.mThemeId == themeId) {
                return theme;
            }
        }
        return null;
    }

     static KeyboardCkTheme getDefaultKeyboardTheme() {
        return searchKeyboardThemeById(THEME_ID_ZERO);
    }

    public static String getKeyboardThemeName(final int themeId) {
        final KeyboardCkTheme theme = searchKeyboardThemeById(themeId);
         return theme.mThemeName;
    }

    public static void saveKeyboardThemeId(final int themeId, final SharedPreferences prefs) {
        prefs.edit().putString(KEYBOARD_THEME_KEY, Integer.toString(themeId)).apply();
    }

    public static KeyboardCkTheme getKeyboardTheme(final Context context) {
        final SharedPreferences prefs = PreferenceCkManagerCompat.getDeviceSharedPreferences(context);
        return getKeyboardTheme(prefs);
    }

    public static KeyboardCkTheme getKeyboardTheme(final SharedPreferences prefs) {
        final String themeIdString = prefs.getString(KEYBOARD_THEME_KEY, null);
        if (themeIdString == null) {
            return searchKeyboardThemeById(THEME_ID_ZERO);
        }
        try {
            final int themeId = Integer.parseInt(themeIdString);
            final KeyboardCkTheme theme = searchKeyboardThemeById(themeId);
            if (theme != null) {
                return theme;
            }
         } catch (final NumberFormatException e) {
         }
         prefs.edit().remove(KEYBOARD_THEME_KEY).remove(SettingsCk.PREF_KEYBOARD_COLOR).apply();
        return getDefaultKeyboardTheme();
    }
}
