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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;

import com.test.testing12345.R;
import com.test.testing12345.custom.settings.SettingsCk;
import com.test.testing12345.custom.settings.SettingsCkValues;

public final class ResourceCkUtils {
    private static final String TAG = ResourceCkUtils.class.getSimpleName();

    public static final float UNDEFINED_RATIO = -1.0f;
    public static final int UNDEFINED_DIMENSION = -1;

    private ResourceCkUtils() {

    }

    private static final HashMap<String, String> sDeviceOverrideValueMap = new HashMap<>();

    private static final String[] BUILD_KEYS_AND_VALUES = {
        "HARDWARE", Build.HARDWARE,
        "MODEL", Build.MODEL,
        "BRAND", Build.BRAND,
        "MANUFACTURER", Build.MANUFACTURER
    };
    private static final HashMap<String, String> sBuildKeyValues;
    private static final String sBuildKeyValuesDebugString;

    static {
        sBuildKeyValues = new HashMap<>();
        final ArrayList<String> keyValuePairs = new ArrayList<>();
        final int keyCount = BUILD_KEYS_AND_VALUES.length / 2;
        for (int i = 0; i < keyCount; i++) {
            final int index = i * 2;
            final String key = BUILD_KEYS_AND_VALUES[index];
            final String value = BUILD_KEYS_AND_VALUES[index + 1];
            sBuildKeyValues.put(key, value);
            keyValuePairs.add(key + '=' + value);
        }
        sBuildKeyValuesDebugString = "[" + TextUtils.join(" ", keyValuePairs) + "]";
    }

    public static String getDeviceOverrideValue(final Resources res, final int overrideResId,
            final String defaultValue) {
        final int orientation = res.getConfiguration().orientation;
        final String key = overrideResId + "-" + orientation;
        if (sDeviceOverrideValueMap.containsKey(key)) {
            return sDeviceOverrideValueMap.get(key);
        }

        final String[] overrideArray = res.getStringArray(overrideResId);
        final String overrideValue = findConstantForKeyValuePairs(sBuildKeyValues, overrideArray);
         if (overrideValue != null) {
             sDeviceOverrideValueMap.put(key, overrideValue);
            return overrideValue;
        }

        sDeviceOverrideValueMap.put(key, defaultValue);
        return defaultValue;
    }

    @SuppressWarnings("serial")
    static class DeviceOverridePatternSyntaxError extends Exception {
        public DeviceOverridePatternSyntaxError(final String message, final String expression) {
            this(message, expression, null);
        }

        public DeviceOverridePatternSyntaxError(final String message, final String expression,
                final Throwable throwable) {
            super(message + ": " + expression, throwable);
        }
    }
    public static int getKeyboardResizeLeftPadding(boolean z) {
        if (SettingsCk.getInstance().getCurrent() == null || z) {
            return 0;
        }
        return SettingsCk.getInstance().getCurrent().mKeyboardResizedLeftPadding;
    }
    public static int getKeyboardResizeRightPadding(boolean z) {
        if (SettingsCk.getInstance().getCurrent() == null || z) {
            return 0;
        }
        return SettingsCk.getInstance().getCurrent().mKeyboardResizedRightPadding;
    }

     private static String findConstantForKeyValuePairs(final HashMap<String, String> keyValuePairs,
            final String[] conditionConstantArray) {
        if (conditionConstantArray == null || keyValuePairs == null) {
            return null;
        }
        String foundValue = null;
        for (final String conditionConstant : conditionConstantArray) {
            final int posComma = conditionConstant.indexOf(',');
            if (posComma < 0) {
                Log.w(TAG, "Array element has no comma: " + conditionConstant);
                continue;
            }
            final String condition = conditionConstant.substring(0, posComma);
            if (condition.isEmpty()) {
                Log.w(TAG, "Array element has no condition: " + conditionConstant);
                continue;
            }
            try {
                if (fulfillsCondition(keyValuePairs, condition)) {

                    if (foundValue == null) {
                        foundValue = conditionConstant.substring(posComma + 1);
                    }

                }
            } catch (final DeviceOverridePatternSyntaxError e) {
                Log.w(TAG, "Syntax error, ignored", e);
            }
        }
        return foundValue;
    }

    private static boolean fulfillsCondition(final HashMap<String,String> keyValuePairs,
            final String condition) throws DeviceOverridePatternSyntaxError {
        final String[] patterns = condition.split(":");

        boolean matchedAll = true;
        for (final String pattern : patterns) {
            final int posEqual = pattern.indexOf('=');
            if (posEqual < 0) {
                throw new DeviceOverridePatternSyntaxError("Pattern has no '='", condition);
            }
            final String key = pattern.substring(0, posEqual);
            final String value = keyValuePairs.get(key);
            if (value == null) {
                throw new DeviceOverridePatternSyntaxError("Unknown key", condition);
            }
            final String patternRegexpValue = pattern.substring(posEqual + 1);
            try {
                if (!value.matches(patternRegexpValue)) {
                    matchedAll = false;
                    // And continue walking through all patterns.
                }
            } catch (final PatternSyntaxException e) {
                throw new DeviceOverridePatternSyntaxError("Syntax error", condition, e);
            }
        }
        return matchedAll;
    }

    public static int getKeyboardHeight(final Resources res, final SettingsCkValues settingsCkValues) {
        final int defaultKeyboardHeight = getDefaultKeyboardHeight(res);
        float scale = settingsCkValues.mKeyboardHeightScale;
        return (int)(defaultKeyboardHeight * scale);
    }
    public static int getDefaultKeyboardWidth(Resources resources) {
        return resources.getDisplayMetrics().widthPixels;
    }
    public static int getDefaultKeyboardHeight(final Resources res) {
        final DisplayMetrics dm = res.getDisplayMetrics();
        final float keyboardHeight = res.getDimension(R.dimen.config_default_keyboard_height);
        final float maxKeyboardHeight = res.getFraction(
                R.fraction.config_max_keyboard_height, dm.heightPixels, dm.heightPixels);
        float minKeyboardHeight = res.getFraction(
                R.fraction.config_min_keyboard_height, dm.heightPixels, dm.heightPixels);
        if (minKeyboardHeight < 0.0f) {

            minKeyboardHeight = -res.getFraction(
                    R.fraction.config_min_keyboard_height, dm.widthPixels, dm.widthPixels);
        }

        return (int)Math.max(Math.min(keyboardHeight, maxKeyboardHeight), minKeyboardHeight);
    }

    public static boolean isValidFraction(final float fraction) {
        return fraction >= 0.0f;
    }


    public static boolean isValidDimensionPixelSize(final int dimension) {
        return dimension > 0;
    }

    public static float getFraction(final TypedArray a, final int index, final float defValue) {
        final TypedValue value = a.peekValue(index);
        if (value == null || !isFractionValue(value)) {
            return defValue;
        }
        return a.getFraction(index, 1, 1, defValue);
    }

    public static float getFraction(final TypedArray a, final int index) {
        return getFraction(a, index, UNDEFINED_RATIO);
    }

    public static float getFraction(final TypedArray a, final int index, final float base,
                                    final float defValue) {
        final TypedValue value = a.peekValue(index);
        if (value == null || !isFractionValue(value)) {
            return defValue;
        }
        return value.getFraction(base, base);
    }

    public static int getDimensionPixelSize(final TypedArray a, final int index) {
        final TypedValue value = a.peekValue(index);
        if (value == null || !isDimensionValue(value)) {
            return ResourceCkUtils.UNDEFINED_DIMENSION;
        }
        return a.getDimensionPixelSize(index, ResourceCkUtils.UNDEFINED_DIMENSION);
    }

    public static float getDimensionOrFraction(final TypedArray a, final int index, final int base,
            final float defValue) {
        final TypedValue value = a.peekValue(index);
        if (value == null) {
            return defValue;
        }
        if (isFractionValue(value)) {
            return a.getFraction(index, base, base, defValue);
        } else if (isDimensionValue(value)) {
            return a.getDimension(index, defValue);
        }
        return defValue;
    }

    public static float getDimensionOrFraction(final TypedArray a, final int index,
                                               final float base, final float defValue) {
        final TypedValue value = a.peekValue(index);
        if (value == null) {
            return defValue;
        }
        if (isFractionValue(value)) {
            return value.getFraction(index, base);
        } else if (isDimensionValue(value)) {
            return a.getDimension(index, defValue);
        }
        return defValue;
    }

    public static int getEnumValue(final TypedArray a, final int index, final int defValue) {
        final TypedValue value = a.peekValue(index);
        if (value == null) {
            return defValue;
        }
        if (isIntegerValue(value)) {
            return a.getInt(index, defValue);
        }
        return defValue;
    }

    public static boolean isFractionValue(final TypedValue v) {
        return v.type == TypedValue.TYPE_FRACTION;
    }

    public static boolean isDimensionValue(final TypedValue v) {
        return v.type == TypedValue.TYPE_DIMENSION;
    }

    public static boolean isIntegerValue(final TypedValue v) {
        return v.type >= TypedValue.TYPE_FIRST_INT && v.type <= TypedValue.TYPE_LAST_INT;
    }

    public static boolean isStringValue(final TypedValue v) {
        return v.type == TypedValue.TYPE_STRING;
    }

    public static boolean isBrightColor(int color) {
        if (android.R.color.transparent == color) {
            return true;
        }
         boolean bright = false;
        int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
        int brightness = (int) Math.sqrt(rgb[0] * rgb[0] * .241 + rgb[1] * rgb[1] * .691 + rgb[2] * rgb[2] * .068);
        if (brightness >= 210) {
            bright = true;
        }
        return bright;
    }
}
