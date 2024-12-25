/*
 * Copyright (C) 2010 The Android Open Source Project
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

import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.StringCkUtils;

import static com.test.testing12345.custom.common.ConstantsCk.CODE_OUTPUT_TEXT;
import static com.test.testing12345.custom.common.ConstantsCk.CODE_UNSPECIFIED;


public final class KeySpecCkParser {

    private static final char BACKSLASH = ConstantsCk.CODE_BACKSLASH;
    private static final char VERTICAL_BAR = ConstantsCk.CODE_VERTICAL_BAR;
    private static final String PREFIX_HEX = "0x";

    private KeySpecCkParser() {

    }

    private static boolean hasIcon(final String keySpec) {
        return keySpec.startsWith(KeyboardCkIconsSet.PREFIX_ICON);
    }

    private static boolean hasCode(final String keySpec, final int labelEnd) {
        if (labelEnd <= 0 || labelEnd + 1 >= keySpec.length()) {
            return false;
        }
        if (keySpec.startsWith(KeyboardCkCodesSet.PREFIX_CODE, labelEnd + 1)) {
            return true;
        }

        return keySpec.startsWith(PREFIX_HEX, labelEnd + 1);
    }

    private static String parseEscape(final String text) {
        if (text.indexOf(BACKSLASH) < 0) {
            return text;
        }
        final int length = text.length();
        final StringBuilder sb = new StringBuilder();
        for (int pos = 0; pos < length; pos++) {
            final char c = text.charAt(pos);
            if (c == BACKSLASH && pos + 1 < length) {

                pos++;
                sb.append(text.charAt(pos));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int indexOfLabelEnd(final String keySpec) {
        final int length = keySpec.length();
        if (keySpec.indexOf(BACKSLASH) < 0) {
            final int labelEnd = keySpec.indexOf(VERTICAL_BAR);
            if (labelEnd == 0) {
                if (length == 1) {

                    return -1;
                }
                throw new KeySpecParserError("Empty label");
            }
            return labelEnd;
        }
        for (int pos = 0; pos < length; pos++) {
            final char c = keySpec.charAt(pos);
            if (c == BACKSLASH && pos + 1 < length) {

                pos++;
            } else if (c == VERTICAL_BAR) {
                return pos;
            }
        }
        return -1;
    }

    private static String getBeforeLabelEnd(final String keySpec, final int labelEnd) {
        return (labelEnd < 0) ? keySpec : keySpec.substring(0, labelEnd);
    }

    private static String getAfterLabelEnd(final String keySpec, final int labelEnd) {
        return keySpec.substring(labelEnd + /* VERTICAL_BAR */1);
    }

    private static void checkDoubleLabelEnd(final String keySpec, final int labelEnd) {
        if (indexOfLabelEnd(getAfterLabelEnd(keySpec, labelEnd)) < 0) {
            return;
        }
        throw new KeySpecParserError("Multiple " + VERTICAL_BAR + ": " + keySpec);
    }

    public static String getLabel(final String keySpec) {
        if (keySpec == null) {
            // TODO: Throw {@link KeySpecParserError} once Key.keyLabel attribute becomes mandatory.
            return null;
        }
        if (hasIcon(keySpec)) {
            return null;
        }
        final int labelEnd = indexOfLabelEnd(keySpec);
        final String label = parseEscape(getBeforeLabelEnd(keySpec, labelEnd));
        if (label.isEmpty()) {
            throw new KeySpecParserError("Empty label: " + keySpec);
        }
        return label;
    }

    private static String getOutputTextInternal(final String keySpec, final int labelEnd) {
        if (labelEnd <= 0) {
            return null;
        }
        checkDoubleLabelEnd(keySpec, labelEnd);
        return parseEscape(getAfterLabelEnd(keySpec, labelEnd));
    }

    public static String getOutputText(final String keySpec) {
        if (keySpec == null) {
            // TODO: Throw {@link KeySpecParserError} once Key.keyLabel attribute becomes mandatory.
            return null;
        }
        final int labelEnd = indexOfLabelEnd(keySpec);
        if (hasCode(keySpec, labelEnd)) {
            return null;
        }
        final String outputText = getOutputTextInternal(keySpec, labelEnd);
        if (outputText != null) {
            if (StringCkUtils.codePointCount(outputText) == 1) {

                return null;
            }
            if (outputText.isEmpty()) {
                throw new KeySpecParserError("Empty outputText: " + keySpec);
            }
            return outputText;
        }
        final String label = getLabel(keySpec);
        if (label == null) {
            throw new KeySpecParserError("Empty label: " + keySpec);
        }

        return (StringCkUtils.codePointCount(label) == 1) ? null : label;
    }

    public static int getCode(final String keySpec) {
        if (keySpec == null) {
            // TODO: Throw {@link KeySpecParserError} once Key.keyLabel attribute becomes mandatory.
            return CODE_UNSPECIFIED;
        }
        final int labelEnd = indexOfLabelEnd(keySpec);
        if (hasCode(keySpec, labelEnd)) {
            checkDoubleLabelEnd(keySpec, labelEnd);
            return parseCode(getAfterLabelEnd(keySpec, labelEnd), CODE_UNSPECIFIED);
        }
        final String outputText = getOutputTextInternal(keySpec, labelEnd);
        if (outputText != null) {

            if (StringCkUtils.codePointCount(outputText) == 1) {
                return outputText.codePointAt(0);
            }
            return CODE_OUTPUT_TEXT;
        }
        final String label = getLabel(keySpec);
        if (label == null) {
            throw new KeySpecParserError("Empty label: " + keySpec);
        }
         return (StringCkUtils.codePointCount(label) == 1) ? label.codePointAt(0) : CODE_OUTPUT_TEXT;
    }

    public static int parseCode(final String text, final int defaultCode) {
        if (text == null) {
            return defaultCode;
        }
        if (text.startsWith(KeyboardCkCodesSet.PREFIX_CODE)) {
            return KeyboardCkCodesSet.getCode(text.substring(KeyboardCkCodesSet.PREFIX_CODE.length()));
        }

        if (text.startsWith(PREFIX_HEX)) {
            return Integer.parseInt(text.substring(PREFIX_HEX.length()), 16);
        }
        return defaultCode;
    }

    public static int getIconId(final String keySpec) {
        if (keySpec == null) {

            return KeyboardCkIconsSet.ICON_UNDEFINED;
        }
        if (!hasIcon(keySpec)) {
            return KeyboardCkIconsSet.ICON_UNDEFINED;
        }
        final int labelEnd = indexOfLabelEnd(keySpec);
        final String iconName = getBeforeLabelEnd(keySpec, labelEnd)
                .substring(KeyboardCkIconsSet.PREFIX_ICON.length());
        return KeyboardCkIconsSet.getIconId(iconName);
    }

    @SuppressWarnings("serial")
    public static final class KeySpecParserError extends RuntimeException {
        public KeySpecParserError(final String message) {
            super(message);
        }
    }
}
