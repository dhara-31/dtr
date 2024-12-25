/*
 * Copyright (C) 2013 The Android Open Source Project
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

import android.text.TextUtils;

import java.util.ArrayList;

import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.settings.SpacingAndCkPunctuations;

public final class CapsModeCkUtils {
    private CapsModeCkUtils() {
     }

     private static boolean isStartPunctuation(final int codePoint) {
        return (codePoint == ConstantsCk.CODE_DOUBLE_QUOTE || codePoint == ConstantsCk.CODE_SINGLE_QUOTE
                || codePoint == ConstantsCk.CODE_INVERTED_QUESTION_MARK
                || codePoint == ConstantsCk.CODE_INVERTED_EXCLAMATION_MARK
                || Character.getType(codePoint) == Character.START_PUNCTUATION);
    }

     public static int getCapsMode(final CharSequence cs, final int reqModes,
            final SpacingAndCkPunctuations spacingAndCkPunctuations) {

        if ((reqModes & (TextUtils.CAP_MODE_WORDS | TextUtils.CAP_MODE_SENTENCES)) == 0) {

            return TextUtils.CAP_MODE_CHARACTERS & reqModes;
        }


        int i;
        for (i = cs.length(); i > 0; i--) {
            final char c = cs.charAt(i - 1);
            if (!isStartPunctuation(c)) {
                break;
            }
        }
        final int newCapIndex = i;


        char prevChar = ConstantsCk.CODE_SPACE;
        while (i > 0) {
            prevChar = cs.charAt(i - 1);
            if (!Character.isSpaceChar(prevChar) && prevChar != ConstantsCk.CODE_TAB) {
                break;
            }
            i--;
        }
        if (i <= 0 || Character.isWhitespace(prevChar)) {
            if (spacingAndCkPunctuations.mUsesGermanRules) {

                boolean hasNewLine = false;
                while (--i >= 0 && Character.isWhitespace(prevChar)) {
                    if (ConstantsCk.CODE_ENTER == prevChar) {
                        hasNewLine = true;
                    }
                    prevChar = cs.charAt(i);
                }
                if (ConstantsCk.CODE_COMMA == prevChar && hasNewLine) {
                    return (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS) & reqModes;
                }
            }

            return (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS
                    | TextUtils.CAP_MODE_SENTENCES) & reqModes;
        }
        if (newCapIndex == i) {

            if (spacingAndCkPunctuations.isWordSeparator(cs.charAt(cs.length() - 1))) {
                return (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS) & reqModes;
            }

            return TextUtils.CAP_MODE_CHARACTERS & reqModes;
        }
        if ((reqModes & TextUtils.CAP_MODE_SENTENCES) == 0) {

            return (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS) & reqModes;
        }

        if (spacingAndCkPunctuations.mUsesAmericanTypography) {
            for (; i > 0; i--) {

                final char c = cs.charAt(i - 1);
                if (c != ConstantsCk.CODE_DOUBLE_QUOTE && c != ConstantsCk.CODE_SINGLE_QUOTE
                        && Character.getType(c) != Character.END_PUNCTUATION) {
                    break;
                }
            }
        }

        if (i <= 0) {
            return TextUtils.CAP_MODE_CHARACTERS & reqModes;
        }
        char c = cs.charAt(--i);


        if (spacingAndCkPunctuations.isSentenceTerminator(c)
                && !spacingAndCkPunctuations.isAbbreviationMarker(c)) {
            return (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS
                    | TextUtils.CAP_MODE_SENTENCES) & reqModes;
        }

        if (!spacingAndCkPunctuations.isSentenceSeparator(c) || i <= 0) {
            return (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS) & reqModes;
        }


        final int START = 0;
        final int WORD = 1;
        final int PERIOD = 2;
        final int LETTER = 3;
        final int NUMBER = 4;
        final int caps = (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS
                | TextUtils.CAP_MODE_SENTENCES) & reqModes;
        final int noCaps = (TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS) & reqModes;
        int state = START;
        while (i > 0) {
            c = cs.charAt(--i);
            switch (state) {
            case START:
                if (Character.isLetter(c)) {
                    state = WORD;
                } else if (Character.isWhitespace(c)) {
                    return noCaps;
                } else if (Character.isDigit(c) && spacingAndCkPunctuations.mUsesGermanRules) {
                    state = NUMBER;
                } else {
                    return caps;
                }
                break;
            case WORD:
                if (Character.isLetter(c)) {
                    state = WORD;
                } else if (spacingAndCkPunctuations.isSentenceSeparator(c)) {
                    state = PERIOD;
                } else {
                    return caps;
                }
                break;
            case PERIOD:
                if (Character.isLetter(c)) {
                    state = LETTER;
                } else {
                    return caps;
                }
                break;
            case LETTER:
                if (Character.isLetter(c)) {
                    state = LETTER;
                } else if (spacingAndCkPunctuations.isSentenceSeparator(c)) {
                    state = PERIOD;
                } else {
                    return noCaps;
                }
                break;
            case NUMBER:
                if (Character.isLetter(c)) {
                    state = WORD;
                } else if (Character.isDigit(c)) {
                    state = NUMBER;
                } else {
                    return noCaps;
                }
            }
        }
         return (START == state || LETTER == state) ? noCaps : caps;
    }

     public static String flagsToString(final int capsFlags) {
        final int capsFlagsMask = TextUtils.CAP_MODE_CHARACTERS | TextUtils.CAP_MODE_WORDS
                | TextUtils.CAP_MODE_SENTENCES;
        if ((capsFlags & ~capsFlagsMask) != 0) {
            return "unknown<0x" + Integer.toHexString(capsFlags) + ">";
        }
        final ArrayList<String> builder = new ArrayList<>();
        if ((capsFlags & TextUtils.CAP_MODE_CHARACTERS) != 0) {
            builder.add("characters");
        }
        if ((capsFlags & TextUtils.CAP_MODE_WORDS) != 0) {
            builder.add("words");
        }
        if ((capsFlags & TextUtils.CAP_MODE_SENTENCES) != 0) {
            builder.add("sentences");
        }
        return builder.isEmpty() ? "none" : TextUtils.join("|", builder);
    }
}
