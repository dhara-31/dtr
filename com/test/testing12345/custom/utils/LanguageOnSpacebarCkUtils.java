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

package com.test.testing12345.custom.utils;

import java.util.Locale;
import java.util.Set;

import com.test.testing12345.custom.RichInputMethodCkManager;
import com.test.testing12345.custom.SubtypeCk;

/**
 * This class determines that the language name on the spacebar should be displayed in what format.
 */
public final class LanguageOnSpacebarCkUtils {
    public static final int FORMAT_TYPE_NONE = 0;
    public static final int FORMAT_TYPE_LANGUAGE_ONLY = 1;
    public static final int FORMAT_TYPE_FULL_LOCALE = 2;

    private LanguageOnSpacebarCkUtils() {
     }

    public static int getLanguageOnSpacebarFormatType(final SubtypeCk subtypeCk) {
        final Locale locale = subtypeCk.getLocaleObject();
        if (locale == null) {
            return FORMAT_TYPE_NONE;
        }
        final String keyboardLanguage = locale.getLanguage();
        final String keyboardLayout = subtypeCk.getKeyboardLayoutSet();
        int sameLanguageAndLayoutCount = 0;
        final Set<SubtypeCk> enabledSubtypeCks =
                RichInputMethodCkManager.getInstance().getEnabledSubtypes(false);
        for (final SubtypeCk enabledSubtypeCk : enabledSubtypeCks) {
            final String language = enabledSubtypeCk.getLocaleObject().getLanguage();
            if (keyboardLanguage.equals(language)
                    && keyboardLayout.equals(enabledSubtypeCk.getKeyboardLayoutSet())) {
                sameLanguageAndLayoutCount++;
            }
        }

        return sameLanguageAndLayoutCount > 1 ? FORMAT_TYPE_FULL_LOCALE
                : FORMAT_TYPE_LANGUAGE_ONLY;
    }
}
