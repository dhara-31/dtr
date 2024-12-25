/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.test.testing12345.custom.common;

import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.test.testing12345.custom.utils.LocaleResourceCkUtils;


public final class LocaleCkUtils {
    private LocaleCkUtils() {
     }

    private static final HashMap<String, Locale> sLocaleCache = new HashMap<>();

     public static Locale constructLocaleFromString(final String localeString) {
        synchronized (sLocaleCache) {
            if (sLocaleCache.containsKey(localeString)) {
                return sLocaleCache.get(localeString);
            }
            final String[] elements = localeString.split("_", 3);
            final Locale locale;
            if (elements.length == 1) {
                locale = new Locale(elements[0] /* language */);
            } else if (elements.length == 2) {
                locale = new Locale(elements[0] /* language */, elements[1] /* country */);
            } else { // localeParams.length == 3
                locale = new Locale(elements[0] /* language */, elements[1] /* country */,
                        elements[2] /* variant */);
            }
            sLocaleCache.put(localeString, locale);
            return locale;
        }
    }

     public static String getLocaleString(final Locale locale) {
        if (!TextUtils.isEmpty(locale.getVariant())) {
            return locale.getLanguage() + "_" + locale.getCountry() + "_" + locale.getVariant();
        }
        if (!TextUtils.isEmpty(locale.getCountry())) {
            return locale.getLanguage() + "_" + locale.getCountry();
        }
        return locale.getLanguage();
    }

     public static Locale findBestLocale(final Locale localeToMatch,
                                        final Collection<Locale> options) {

        for (final Locale locale : options) {
            if (locale.equals(localeToMatch)) {
                return locale;
            }
        }
        for (final Locale locale : options) {
            if (locale.getLanguage().equals(localeToMatch.getLanguage()) &&
                    locale.getCountry().equals(localeToMatch.getCountry()) &&
                    locale.getVariant().equals(localeToMatch.getVariant())) {
                return locale;
            }
        }
        for (final Locale locale : options) {
            if (locale.getLanguage().equals(localeToMatch.getLanguage()) &&
                    locale.getCountry().equals(localeToMatch.getCountry())) {
                return locale;
            }
        }
        for (final Locale locale : options) {
            if (locale.getLanguage().equals(localeToMatch.getLanguage())) {
                return locale;
            }
        }
        return null;
    }

     public static List<Locale> getSystemLocales() {
        ArrayList<Locale> locales = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = Resources.getSystem().getConfiguration().getLocales();
            for (int i = 0; i < localeList.size(); i++) {
                locales.add(localeList.get(i));
            }
        } else {
            locales.add(Resources.getSystem().getConfiguration().locale);
        }
        return locales;
    }

     public static class LocaleComparator implements Comparator<Locale> {
        @Override
        public int compare(Locale a, Locale b) {
            if (a.equals(b)) {

                return 0;
            }
            final String aDisplay =
                    LocaleResourceCkUtils.getLocaleDisplayNameInSystemLocale(getLocaleString(a));
            final String bDisplay =
                    LocaleResourceCkUtils.getLocaleDisplayNameInSystemLocale(getLocaleString(b));
            final int result = aDisplay.compareToIgnoreCase(bDisplay);
            if (result != 0) {
                return result;
            }

            return a.hashCode() > b.hashCode() ? 1 : -1;
        }
    }
}
