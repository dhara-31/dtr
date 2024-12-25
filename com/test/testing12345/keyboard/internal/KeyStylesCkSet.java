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

import android.content.res.TypedArray;
import android.util.SparseArray;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.util.Arrays;
import java.util.HashMap;

import com.test.testing12345.R;
import com.test.testing12345.custom.utils.XmlParseCkUtils;

public final class KeyStylesCkSet {
    private static final String TAG = KeyStylesCkSet.class.getSimpleName();
    private static final boolean DEBUG = false;

    private final HashMap<String, KeyCkStyle> mStyles = new HashMap<>();

    private final KeyboardCkTextsSet mTextsSet;
    private final KeyCkStyle mEmptyKeyCkStyle;
    private static final String EMPTY_STYLE_NAME = "<empty>";

    public KeyStylesCkSet(final KeyboardCkTextsSet textsSet) {
        mTextsSet = textsSet;
        mEmptyKeyCkStyle = new EmptyKeyCkStyle(textsSet);
        mStyles.put(EMPTY_STYLE_NAME, mEmptyKeyCkStyle);
    }

    private static final class EmptyKeyCkStyle extends KeyCkStyle {
        EmptyKeyCkStyle(final KeyboardCkTextsSet textsSet) {
            super(textsSet);
        }

        @Override
        public String[] getStringArray(final TypedArray a, final int index) {
            return parseStringArray(a, index);
        }

        @Override
        public String getString(final TypedArray a, final int index) {
            return parseString(a, index);
        }

        @Override
        public int getInt(final TypedArray a, final int index, final int defaultValue) {
            return a.getInt(index, defaultValue);
        }

        @Override
        public int getFlags(final TypedArray a, final int index) {
            return a.getInt(index, 0);
        }
    }

    private static final class DeclaredKeyCkStyle extends KeyCkStyle {
        private final HashMap<String, KeyCkStyle> mStyles;
        private final String mParentStyleName;
        private final SparseArray<Object> mStyleAttributes = new SparseArray<>();

        public DeclaredKeyCkStyle(final String parentStyleName,
                                  final KeyboardCkTextsSet textsSet,
                                  final HashMap<String, KeyCkStyle> styles) {
            super(textsSet);
            mParentStyleName = parentStyleName;
            mStyles = styles;
        }

        @Override
        public String[] getStringArray(final TypedArray a, final int index) {
            if (a.hasValue(index)) {
                return parseStringArray(a, index);
            }
            final Object value = mStyleAttributes.get(index);
            if (value != null) {
                final String[] array = (String[])value;
                return Arrays.copyOf(array, array.length);
            }
            final KeyCkStyle parentStyle = mStyles.get(mParentStyleName);
            return parentStyle.getStringArray(a, index);
        }

        @Override
        public String getString(final TypedArray a, final int index) {
            if (a.hasValue(index)) {
                return parseString(a, index);
            }
            final Object value = mStyleAttributes.get(index);
            if (value != null) {
                return (String)value;
            }
            final KeyCkStyle parentStyle = mStyles.get(mParentStyleName);
            return parentStyle.getString(a, index);
        }

        @Override
        public int getInt(final TypedArray a, final int index, final int defaultValue) {
            if (a.hasValue(index)) {
                return a.getInt(index, defaultValue);
            }
            final Object value = mStyleAttributes.get(index);
            if (value != null) {
                return (Integer)value;
            }
            final KeyCkStyle parentStyle = mStyles.get(mParentStyleName);
            return parentStyle.getInt(a, index, defaultValue);
        }

        @Override
        public int getFlags(final TypedArray a, final int index) {
            final int parentFlags = mStyles.get(mParentStyleName).getFlags(a, index);
            final Integer value = (Integer)mStyleAttributes.get(index);
            final int styleFlags = (value != null) ? value : 0;
            final int flags = a.getInt(index, 0);
            return flags | styleFlags | parentFlags;
        }

        public void readKeyAttributes(final TypedArray keyAttr) {

            readString(keyAttr, R.styleable.Keyboard_Key_altCode);
            readString(keyAttr, R.styleable.Keyboard_Key_keySpec);
            readString(keyAttr, R.styleable.Keyboard_Key_keyHintLabel);
            readStringArray(keyAttr, R.styleable.Keyboard_Key_moreKeys);
            readStringArray(keyAttr, R.styleable.Keyboard_Key_additionalMoreKeys);
            readFlags(keyAttr, R.styleable.Keyboard_Key_keyLabelFlags);
            readInt(keyAttr, R.styleable.Keyboard_Key_maxMoreKeysColumn);
            readInt(keyAttr, R.styleable.Keyboard_Key_backgroundType);
            readFlags(keyAttr, R.styleable.Keyboard_Key_keyActionFlags);
        }

        private void readString(final TypedArray a, final int index) {
            if (a.hasValue(index)) {
                mStyleAttributes.put(index, parseString(a, index));
            }
        }

        private void readInt(final TypedArray a, final int index) {
            if (a.hasValue(index)) {
                mStyleAttributes.put(index, a.getInt(index, 0));
            }
        }

        private void readFlags(final TypedArray a, final int index) {
            if (a.hasValue(index)) {
                final Integer value = (Integer)mStyleAttributes.get(index);
                final int styleFlags = value != null ? value : 0;
                mStyleAttributes.put(index, a.getInt(index, 0) | styleFlags);
            }
        }

        private void readStringArray(final TypedArray a, final int index) {
            if (a.hasValue(index)) {
                mStyleAttributes.put(index, parseStringArray(a, index));
            }
        }
    }

    public void parseKeyStyleAttributes(final TypedArray keyStyleAttr, final TypedArray keyAttrs,
            final XmlPullParser parser) throws XmlPullParserException {
        final String styleName = keyStyleAttr.getString(R.styleable.Keyboard_KeyStyle_styleName);
        if (styleName == null) {
            throw new XmlParseCkUtils.ParseException(
                    KeyboardCkBuilder.TAG_KEY_STYLE + " has no styleName attribute", parser);
        }

        final String parentStyleInAttr = keyStyleAttr.getString(
                R.styleable.Keyboard_KeyStyle_parentStyle);
        if (parentStyleInAttr != null && !mStyles.containsKey(parentStyleInAttr)) {
            throw new XmlParseCkUtils.ParseException(
                    "Unknown parentStyle " + parentStyleInAttr, parser);
        }
        final String parentStyleName = (parentStyleInAttr == null) ? EMPTY_STYLE_NAME
                : parentStyleInAttr;
        final DeclaredKeyCkStyle style = new DeclaredKeyCkStyle(parentStyleName, mTextsSet, mStyles);
        style.readKeyAttributes(keyAttrs);
        mStyles.put(styleName, style);
    }

    public KeyCkStyle getKeyStyle(final TypedArray keyAttr, final XmlPullParser parser)
            throws XmlParseCkUtils.ParseException {
        final String styleName = keyAttr.getString(R.styleable.Keyboard_Key_keyStyle);
        if (styleName == null) {
            return mEmptyKeyCkStyle;
        }
        final KeyCkStyle style = mStyles.get(styleName);
        if (style == null) {
            throw new XmlParseCkUtils.ParseException("Unknown key style: " + styleName, parser);
        }
        return style;
    }
}
