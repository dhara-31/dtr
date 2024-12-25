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

package com.test.testing12345.keyboard.internal;

import android.os.Build;
import android.text.TextUtils;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import com.test.testing12345.keyboard.KeyCk;
import com.test.testing12345.custom.common.CollectionCkUtils;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.StringCkUtils;


public final class MoreKeyCkSpec {
    public final int mCode;
    public final String mLabel;
    public final String mOutputText;
    public final int mIconId;

    public MoreKeyCkSpec(final String moreKeySpec, boolean needsToUpperCase,
                         final Locale locale) {
        if (moreKeySpec.isEmpty()) {
            throw new KeySpecCkParser.KeySpecParserError("Empty more key spec");
        }
        final String label = KeySpecCkParser.getLabel(moreKeySpec);
        mLabel = needsToUpperCase ? StringCkUtils.toTitleCaseOfKeyLabel(label, locale) : label;
        final int codeInSpec = KeySpecCkParser.getCode(moreKeySpec);
        final int code = needsToUpperCase ? StringCkUtils.toTitleCaseOfKeyCode(codeInSpec, locale)
                : codeInSpec;
        if (code == ConstantsCk.CODE_UNSPECIFIED) {

            mCode = ConstantsCk.CODE_OUTPUT_TEXT;
            mOutputText = mLabel;
        } else {
            mCode = code;
            final String outputText = KeySpecCkParser.getOutputText(moreKeySpec);
            mOutputText = needsToUpperCase
                    ? StringCkUtils.toTitleCaseOfKeyLabel(outputText, locale) : outputText;
        }
        mIconId = KeySpecCkParser.getIconId(moreKeySpec);
    }

    public KeyCk buildKey(final float x, final float y, final float width, final float height,
                          final float leftPadding, final float rightPadding, final float topPadding,
                          final float bottomPadding, final int labelFlags) {
        return new KeyCk(mLabel, mIconId, mCode, mOutputText, null /* hintLabel */, labelFlags,
                KeyCk.BACKGROUND_TYPE_NORMAL, x, y, width, height, leftPadding, rightPadding,
                topPadding, bottomPadding);
    }

    @Override
    public int hashCode() {
        int hashCode = 31 + mCode;
        hashCode = hashCode * 31 + mIconId;
        final String label = mLabel;
        hashCode = hashCode * 31 + (label == null ? 0 : label.hashCode());
        final String outputText = mOutputText;
        hashCode = hashCode * 31 + (outputText == null ? 0 : outputText.hashCode());
        return hashCode;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof MoreKeyCkSpec) {
            final MoreKeyCkSpec other = (MoreKeyCkSpec)o;
            return mCode == other.mCode
                    && mIconId == other.mIconId
                    && TextUtils.equals(mLabel, other.mLabel)
                    && TextUtils.equals(mOutputText, other.mOutputText);
        }
        return false;
    }

    @Override
    public String toString() {
        final String label = (mIconId == KeyboardCkIconsSet.ICON_UNDEFINED ? mLabel
                : KeyboardCkIconsSet.PREFIX_ICON + KeyboardCkIconsSet.getIconName(mIconId));
        final String output = (mCode == ConstantsCk.CODE_OUTPUT_TEXT ? mOutputText
                : ConstantsCk.printableCode(mCode));
        if (StringCkUtils.codePointCount(label) == 1 && label.codePointAt(0) == mCode) {
            return output;
        }
        return label + "|" + output;
    }

    public static class LettersOnBaseLayout {
        private final SparseIntArray mCodes = new SparseIntArray();
        private final HashSet<String> mTexts = new HashSet<>();

        public void addLetter(final KeyCk keyCk) {
            final int code = keyCk.getCode();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Character.isAlphabetic(code)) {
                    mCodes.put(code, 0);
                } else if (code == ConstantsCk.CODE_OUTPUT_TEXT) {
                    mTexts.add(keyCk.getOutputText());
                }
            }
        }

        public boolean contains(final MoreKeyCkSpec moreKey) {
            final int code = moreKey.mCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Character.isAlphabetic(code) && mCodes.indexOfKey(code) >= 0) {
                    return true;
                } else if (code == ConstantsCk.CODE_OUTPUT_TEXT && mTexts.contains(moreKey.mOutputText)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static MoreKeyCkSpec[] removeRedundantMoreKeys(final MoreKeyCkSpec[] moreKeys,
                                                          final LettersOnBaseLayout lettersOnBaseLayout) {
        if (moreKeys == null) {
            return null;
        }
        final ArrayList<MoreKeyCkSpec> filteredMoreKeys = new ArrayList<>();
        for (final MoreKeyCkSpec moreKey : moreKeys) {
            if (!lettersOnBaseLayout.contains(moreKey)) {
                filteredMoreKeys.add(moreKey);
            }
        }
        final int size = filteredMoreKeys.size();
        if (size == moreKeys.length) {
            return moreKeys;
        }
        if (size == 0) {
            return null;
        }
        return filteredMoreKeys.toArray(new MoreKeyCkSpec[size]);
    }


    private static final char COMMA = ConstantsCk.CODE_COMMA;
    private static final char BACKSLASH = ConstantsCk.CODE_BACKSLASH;
    private static final String ADDITIONAL_MORE_KEY_MARKER =
            StringCkUtils.newSingleCodePointString(ConstantsCk.CODE_PERCENT);

     public static String[] splitKeySpecs(final String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        final int size = text.length();
        // Optimization for one-letter key specification.
        if (size == 1) {
            return text.charAt(0) == COMMA ? null : new String[] { text };
        }

        ArrayList<String> list = null;
        int start = 0;

        for (int pos = 0; pos < size; pos++) {
            final char c = text.charAt(pos);
            if (c == COMMA) {
                 if (pos - start > 0) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(text.substring(start, pos));
                }
                 start = pos + 1;
            } else if (c == BACKSLASH) {
                 pos++;
            }
        }
        final String remain = (size - start > 0) ? text.substring(start) : null;
        if (list == null) {
            return remain != null ? new String[] { remain } : null;
        }
        if (remain != null) {
            list.add(remain);
        }
        return list.toArray(new String[list.size()]);
    }

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static String[] filterOutEmptyString(final String[] array) {
        if (array == null) {
            return EMPTY_STRING_ARRAY;
        }
        ArrayList<String> out = null;
        for (int i = 0; i < array.length; i++) {
            final String entry = array[i];
            if (TextUtils.isEmpty(entry)) {
                if (out == null) {
                    out = CollectionCkUtils.arrayAsList(array, 0, i);
                }
            } else if (out != null) {
                out.add(entry);
            }
        }
        if (out == null) {
            return array;
        }
        return out.toArray(new String[out.size()]);
    }

    public static String[] insertAdditionalMoreKeys(final String[] moreKeySpecs,
            final String[] additionalMoreKeySpecs) {
        final String[] moreKeys = filterOutEmptyString(moreKeySpecs);
        final String[] additionalMoreKeys = filterOutEmptyString(additionalMoreKeySpecs);
        final int moreKeysCount = moreKeys.length;
        final int additionalCount = additionalMoreKeys.length;
        ArrayList<String> out = null;
        int additionalIndex = 0;
        for (int moreKeyIndex = 0; moreKeyIndex < moreKeysCount; moreKeyIndex++) {
            final String moreKeySpec = moreKeys[moreKeyIndex];
            if (moreKeySpec.equals(ADDITIONAL_MORE_KEY_MARKER)) {
                if (additionalIndex < additionalCount) {
                    // Replace '%' marker with additional more key specification.
                    final String additionalMoreKey = additionalMoreKeys[additionalIndex];
                    if (out != null) {
                        out.add(additionalMoreKey);
                    } else {
                        moreKeys[moreKeyIndex] = additionalMoreKey;
                    }
                    additionalIndex++;
                } else {
                     if (out == null) {
                        out = CollectionCkUtils.arrayAsList(moreKeys, 0, moreKeyIndex);
                    }
                }
            } else {
                if (out != null) {
                    out.add(moreKeySpec);
                }
            }
        }
        if (additionalCount > 0 && additionalIndex == 0) {

            out = CollectionCkUtils.arrayAsList(additionalMoreKeys, additionalIndex, additionalCount);
            for (int i = 0; i < moreKeysCount; i++) {
                out.add(moreKeys[i]);
            }
        } else if (additionalIndex < additionalCount) {

            out = CollectionCkUtils.arrayAsList(moreKeys, 0, moreKeysCount);
            for (int i = additionalIndex; i < additionalCount; i++) {
                out.add(additionalMoreKeys[additionalIndex]);
            }
        }
        if (out == null && moreKeysCount > 0) {
            return moreKeys;
        } else if (out != null && out.size() > 0) {
            return out.toArray(new String[out.size()]);
        } else {
            return null;
        }
    }

    public static int getIntValue(final String[] moreKeys, final String key,
            final int defaultValue) {
        if (moreKeys == null) {
            return defaultValue;
        }
        final int keyLen = key.length();
        boolean foundValue = false;
        int value = defaultValue;
        for (int i = 0; i < moreKeys.length; i++) {
            final String moreKeySpec = moreKeys[i];
            if (moreKeySpec == null || !moreKeySpec.startsWith(key)) {
                continue;
            }
            moreKeys[i] = null;
            try {
                if (!foundValue) {
                    value = Integer.parseInt(moreKeySpec.substring(keyLen));
                    foundValue = true;
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException(
                        "integer should follow after " + key + ": " + moreKeySpec);
            }
        }
        return value;
    }

    public static boolean getBooleanValue(final String[] moreKeys, final String key) {
        if (moreKeys == null) {
            return false;
        }
        boolean value = false;
        for (int i = 0; i < moreKeys.length; i++) {
            final String moreKeySpec = moreKeys[i];
            if (moreKeySpec == null || !moreKeySpec.equals(key)) {
                continue;
            }
            moreKeys[i] = null;
            value = true;
        }
        return value;
    }
}
