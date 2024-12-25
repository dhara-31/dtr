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

package com.test.testing12345.custom.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.test.testing12345.custom.SubtypeCk;

public final class SubtypePreferenceCkUtils {
    private static final String TAG = SubtypePreferenceCkUtils.class.getSimpleName();

    private SubtypePreferenceCkUtils() {

    }

    private static final String LOCALE_AND_LAYOUT_SEPARATOR = ":";
    private static final int INDEX_OF_LOCALE = 0;
    private static final int INDEX_OF_KEYBOARD_LAYOUT = 1;
    private static final int PREF_ELEMENTS_LENGTH = (INDEX_OF_KEYBOARD_LAYOUT + 1);
    private static final String PREF_SUBTYPE_SEPARATOR = ";";

    private static String getPrefString(final SubtypeCk subtypeCk) {
        final String localeString = subtypeCk.getLocale();
        final String keyboardLayoutSetName = subtypeCk.getKeyboardLayoutSet();
        return localeString + LOCALE_AND_LAYOUT_SEPARATOR + keyboardLayoutSetName;
    }

    public static List<SubtypeCk> createSubtypesFromPref(final String prefSubtypes,
                                                         final Resources resources) {
        if (TextUtils.isEmpty(prefSubtypes)) {
            return new ArrayList<>();
        }
        final String[] prefSubtypeArray = prefSubtypes.split(PREF_SUBTYPE_SEPARATOR);
        final ArrayList<SubtypeCk> subtypesList = new ArrayList<>(prefSubtypeArray.length);
        for (final String prefSubtype : prefSubtypeArray) {
            final String[] elements = prefSubtype.split(LOCALE_AND_LAYOUT_SEPARATOR);
            if (elements.length != PREF_ELEMENTS_LENGTH) {
                Log.w(TAG, "Unknown subtype specified: " + prefSubtype + " in "
                        + prefSubtypes);
                continue;
            }
            final String localeString = elements[INDEX_OF_LOCALE];
            final String keyboardLayoutSetName = elements[INDEX_OF_KEYBOARD_LAYOUT];
            final SubtypeCk subtypeCk =
                    SubtypeLocaleCkUtils.getSubtype(localeString, keyboardLayoutSetName, resources);
            if (subtypeCk == null) {
                continue;
            }
            subtypesList.add(subtypeCk);
        }
        return subtypesList;
    }

    public static String createPrefSubtypes(final List<SubtypeCk> subtypeCks) {
        if (subtypeCks == null || subtypeCks.size() == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final SubtypeCk subtypeCk : subtypeCks) {
            if (sb.length() > 0) {
                sb.append(PREF_SUBTYPE_SEPARATOR);
            }
            sb.append(getPrefString(subtypeCk));
        }
        return sb.toString();
    }
}
