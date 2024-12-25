/*
 * Copyright (C) 2011 The Android Open Source Project
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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.text.InputType;
import android.util.SparseArray;
import android.util.Xml;
import android.view.inputmethod.EditorInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.internal.KeyboardCkBuilder;
import com.test.testing12345.keyboard.internal.KeyboardCkParams;
import com.test.testing12345.keyboard.internal.UniqueKeysCkCache;
import com.test.testing12345.custom.SubtypeCk;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.utils.InputTypeCkUtils;
import com.test.testing12345.custom.utils.XmlParseCkUtils;


public final class KeyboardLayoutCkSet {
    private static final String TAG = KeyboardLayoutCkSet.class.getSimpleName();
    private static final boolean DEBUG_CACHE = false;

    private static final String TAG_KEYBOARD_SET = "KeyboardLayoutSet";
    private static final String TAG_ELEMENT = "Element";

    private static final String KEYBOARD_LAYOUT_SET_RESOURCE_PREFIX = "keyboard_layout_set_";

    private final Context mContext;
    private final Params mParams;


    private static final int FORCIBLE_CACHE_SIZE = 4;

    private static final KeyboardCk[] S_FORCIBLE_KEYBOARD_CK_CACHE = new KeyboardCk[FORCIBLE_CACHE_SIZE];
    private static final HashMap<KeyboardCkId, SoftReference<KeyboardCk>> sKeyboardCache =
            new HashMap<>();
    private static final UniqueKeysCkCache S_UNIQUE_KEYS_CK_CACHE = UniqueKeysCkCache.newInstance();

    @SuppressWarnings("serial")
    public static final class KeyboardLayoutSetException extends RuntimeException {
        public final KeyboardCkId mKeyboardCkId;

        public KeyboardLayoutSetException(final Throwable cause, final KeyboardCkId keyboardCkId) {
            super(cause);
            mKeyboardCkId = keyboardCkId;
        }
    }

    private static final class ElementParams {
        int mKeyboardXmlId;
        boolean mAllowRedundantMoreKeys;
        public ElementParams() {}
    }

    public static final class Params {
        String mKeyboardLayoutSetName;
        int mMode;
         EditorInfo mEditorInfo;
        boolean mLanguageSwitchKeyEnabled;
        SubtypeCk mSubtypeCk;
        int mKeyboardThemeId;
        int mKeyboardWidth;
        int mKeyboardHeight;
        boolean mShowMoreKeys;
        boolean mShowNumberRow;

        final SparseArray<ElementParams> mKeyboardLayoutSetElementIdToParamsMap =
                new SparseArray<>();
    }

    public static void onSystemLocaleChanged() {
        clearKeyboardCache();
    }

    public static void onKeyboardThemeChanged() {
        clearKeyboardCache();
    }

    private static void clearKeyboardCache() {
        sKeyboardCache.clear();
        S_UNIQUE_KEYS_CK_CACHE.clear();
    }

    KeyboardLayoutCkSet(final Context context, final Params params) {
        mContext = context;
        mParams = params;
    }

    public KeyboardCk getKeyboard(final int baseKeyboardLayoutSetElementId) {
        final int keyboardLayoutSetElementId;
        switch (mParams.mMode) {
        case KeyboardCkId.MODE_PHONE:
            ConstantsCk.isNumericKeyboard = true;
            if (baseKeyboardLayoutSetElementId == KeyboardCkId.ELEMENT_SYMBOLS) {
                keyboardLayoutSetElementId = KeyboardCkId.ELEMENT_PHONE_SYMBOLS;
            } else {
                keyboardLayoutSetElementId = KeyboardCkId.ELEMENT_PHONE;
            }
            break;
        case KeyboardCkId.MODE_NUMBER:
        case KeyboardCkId.MODE_DATE:
        case KeyboardCkId.MODE_TIME:
        case KeyboardCkId.MODE_DATETIME:
            ConstantsCk.isNumericKeyboard = true;
            keyboardLayoutSetElementId = KeyboardCkId.ELEMENT_NUMBER;
            break;
        default:
            ConstantsCk.isNumericKeyboard = false;
            keyboardLayoutSetElementId = baseKeyboardLayoutSetElementId;
            break;
        }

        ElementParams elementParams = mParams.mKeyboardLayoutSetElementIdToParamsMap.get(
                keyboardLayoutSetElementId);
        if (elementParams == null) {
            elementParams = mParams.mKeyboardLayoutSetElementIdToParamsMap.get(
                    KeyboardCkId.ELEMENT_ALPHABET);
        }


        final KeyboardCkId id = new KeyboardCkId(keyboardLayoutSetElementId, mParams);
        return getKeyboard(elementParams, id);
    }

    private KeyboardCk getKeyboard(final ElementParams elementParams, final KeyboardCkId id) {
        final SoftReference<KeyboardCk> ref = sKeyboardCache.get(id);
        final KeyboardCk cachedKeyboardCk = (ref == null) ? null : ref.get();
        if (cachedKeyboardCk != null) {
             return cachedKeyboardCk;
        }

        final KeyboardCkBuilder<KeyboardCkParams> builder =
                new KeyboardCkBuilder<>(mContext, new KeyboardCkParams(S_UNIQUE_KEYS_CK_CACHE));
        S_UNIQUE_KEYS_CK_CACHE.setEnabled(id.isAlphabetKeyboard());
        builder.setAllowRedundantMoreKes(elementParams.mAllowRedundantMoreKeys);
        final int keyboardXmlId = elementParams.mKeyboardXmlId;
        builder.load(keyboardXmlId, id);
        final KeyboardCk keyboardCk = builder.build();
        sKeyboardCache.put(id, new SoftReference<>(keyboardCk));
        if ((id.mElementId == KeyboardCkId.ELEMENT_ALPHABET
                || id.mElementId == KeyboardCkId.ELEMENT_ALPHABET_AUTOMATIC_SHIFTED)) {

            for (int i = S_FORCIBLE_KEYBOARD_CK_CACHE.length - 1; i >= 1; --i) {
                S_FORCIBLE_KEYBOARD_CK_CACHE[i] = S_FORCIBLE_KEYBOARD_CK_CACHE[i - 1];
            }
            S_FORCIBLE_KEYBOARD_CK_CACHE[0] = keyboardCk;
         }
         return keyboardCk;
    }

    public static final class Builder {
        private final Context mContext;
        private final Resources mResources;

        private final Params mParams = new Params();

        private static final EditorInfo EMPTY_EDITOR_INFO = new EditorInfo();

        public Builder(final Context context, final EditorInfo ei) {
            mContext = context;
            mResources = context.getResources();
            final Params params = mParams;

            final EditorInfo editorInfo = (ei != null) ? ei : EMPTY_EDITOR_INFO;
            params.mMode = getKeyboardMode(editorInfo);
             params.mEditorInfo = editorInfo;
        }

        public Builder setKeyboardTheme(final int themeId) {
            mParams.mKeyboardThemeId = themeId;
            return this;
        }

        public Builder setKeyboardGeometry(final int keyboardWidth, final int keyboardHeight) {
            mParams.mKeyboardWidth = keyboardWidth;
            mParams.mKeyboardHeight = keyboardHeight;
            return this;
        }

        public Builder setSubtype(final SubtypeCk subtypeCk) {

            mParams.mSubtypeCk = subtypeCk;
            mParams.mKeyboardLayoutSetName = KEYBOARD_LAYOUT_SET_RESOURCE_PREFIX
                    + subtypeCk.getKeyboardLayoutSet();
            return this;
        }

        public Builder setLanguageSwitchKeyEnabled(final boolean enabled) {
            mParams.mLanguageSwitchKeyEnabled = enabled;
            return this;
        }

        public Builder setShowSpecialChars(final boolean enabled) {
            mParams.mShowMoreKeys = enabled;
            return this;
        }

        public Builder setShowNumberRow(final boolean enabled) {
            mParams.mShowNumberRow = enabled;
            return this;
        }

        public KeyboardLayoutCkSet build() {
            if (mParams.mSubtypeCk == null)
                throw new RuntimeException("KeyboardLayoutSet subtype is not specified");
            final int xmlId = getXmlId(mResources, mParams.mKeyboardLayoutSetName);
            try {
                parseKeyboardLayoutSet(mResources, xmlId);
            } catch (final IOException | XmlPullParserException e) {
                throw new RuntimeException(e.getMessage() + " in " + mParams.mKeyboardLayoutSetName,
                        e);
            }
            return new KeyboardLayoutCkSet(mContext, mParams);
        }

        private static int getXmlId(final Resources resources, final String keyboardLayoutSetName) {
            final String packageName = resources.getResourcePackageName(
                    R.xml.keyboard_layout_set_qwerty);
            return resources.getIdentifier(keyboardLayoutSetName, "xml", packageName);
        }

        private void parseKeyboardLayoutSet(final Resources res, final int resId)
                throws XmlPullParserException, IOException {
            final XmlResourceParser parser = res.getXml(resId);
            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    final int event = parser.next();
                    if (event == XmlPullParser.START_TAG) {
                        final String tag = parser.getName();
                        if (TAG_KEYBOARD_SET.equals(tag)) {
                            parseKeyboardLayoutSetContent(parser);
                        } else {
                            throw new XmlParseCkUtils.IllegalStartTag(parser, tag, TAG_KEYBOARD_SET);
                        }
                    }
                }
            } finally {
                parser.close();
            }
        }

        private void parseKeyboardLayoutSetContent(final XmlPullParser parser)
                throws XmlPullParserException, IOException {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                final int event = parser.next();
                if (event == XmlPullParser.START_TAG) {
                    final String tag = parser.getName();
                    if (TAG_ELEMENT.equals(tag)) {
                        parseKeyboardLayoutSetElement(parser);
                    } else {
                        throw new XmlParseCkUtils.IllegalStartTag(parser, tag, TAG_KEYBOARD_SET);
                    }
                } else if (event == XmlPullParser.END_TAG) {
                    final String tag = parser.getName();
                    if (TAG_KEYBOARD_SET.equals(tag)) {
                        break;
                    }
                    throw new XmlParseCkUtils.IllegalEndTag(parser, tag, TAG_KEYBOARD_SET);
                }
            }
        }

        private void parseKeyboardLayoutSetElement(final XmlPullParser parser)
                throws XmlPullParserException, IOException {
            final TypedArray a = mResources.obtainAttributes(Xml.asAttributeSet(parser),
                    R.styleable.KeyboardLayoutSet_Element);
            try {
                XmlParseCkUtils.checkAttributeExists(a,
                        R.styleable.KeyboardLayoutSet_Element_elementName, "elementName",
                        TAG_ELEMENT, parser);
                XmlParseCkUtils.checkAttributeExists(a,
                        R.styleable.KeyboardLayoutSet_Element_elementKeyboard, "elementKeyboard",
                        TAG_ELEMENT, parser);
                XmlParseCkUtils.checkEndTag(TAG_ELEMENT, parser);

                final ElementParams elementParams = new ElementParams();
                final int elementName = a.getInt(
                        R.styleable.KeyboardLayoutSet_Element_elementName, 0);
                elementParams.mKeyboardXmlId = a.getResourceId(
                        R.styleable.KeyboardLayoutSet_Element_elementKeyboard, 0);
                elementParams.mAllowRedundantMoreKeys = a.getBoolean(
                        R.styleable.KeyboardLayoutSet_Element_allowRedundantMoreKeys, true);
                mParams.mKeyboardLayoutSetElementIdToParamsMap.put(elementName, elementParams);
            } finally {
                a.recycle();
            }
        }

        private static int getKeyboardMode(final EditorInfo editorInfo) {
            final int inputType = editorInfo.inputType;
            final int variation = inputType & InputType.TYPE_MASK_VARIATION;

            switch (inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
                return KeyboardCkId.MODE_NUMBER;
            case InputType.TYPE_CLASS_DATETIME:
                switch (variation) {
                case InputType.TYPE_DATETIME_VARIATION_DATE:
                    return KeyboardCkId.MODE_DATE;
                case InputType.TYPE_DATETIME_VARIATION_TIME:
                    return KeyboardCkId.MODE_TIME;
                default:
                    return KeyboardCkId.MODE_DATETIME;
                }
            case InputType.TYPE_CLASS_PHONE:
                return KeyboardCkId.MODE_PHONE;
            case InputType.TYPE_CLASS_TEXT:
                if (InputTypeCkUtils.isEmailVariation(variation)) {
                    return KeyboardCkId.MODE_EMAIL;
                } else if (variation == InputType.TYPE_TEXT_VARIATION_URI) {
                    return KeyboardCkId.MODE_URL;
                } else if (variation == InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE) {
                    return KeyboardCkId.MODE_IM;
                } else if (variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
                    return KeyboardCkId.MODE_TEXT;
                } else {
                    return KeyboardCkId.MODE_TEXT;
                }
            default:
                return KeyboardCkId.MODE_TEXT;
            }
        }
    }
}
