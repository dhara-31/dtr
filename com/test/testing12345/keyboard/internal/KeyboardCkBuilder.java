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

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.KeyCk;
import com.test.testing12345.keyboard.KeyboardCk;
import com.test.testing12345.keyboard.KeyboardCkId;
import com.test.testing12345.keyboard.KeyboardCkTheme;
import com.test.testing12345.custom.common.StringCkUtils;
import com.test.testing12345.custom.utils.ResourceCkUtils;
import com.test.testing12345.custom.utils.XmlParseCkUtils;
import com.test.testing12345.custom.utils.XmlParseCkUtils.ParseException;



 public class KeyboardCkBuilder<KP extends KeyboardCkParams> {
    private static final String BUILDER_TAG = "Keyboard.Builder";
    private static final boolean DEBUG = false;

     private static final String TAG_KEYBOARD = "Keyboard";
    private static final String TAG_ROW = "Row";
    private static final String TAG_KEY = "Key";
    private static final String TAG_SPACER = "Spacer";
    private static final String TAG_INCLUDE = "include";
    private static final String TAG_MERGE = "merge";
    private static final String TAG_SWITCH = "switch";
    private static final String TAG_CASE = "case";
    private static final String TAG_DEFAULT = "default";
    public static final String TAG_KEY_STYLE = "key-style";

    private static final int DEFAULT_KEYBOARD_COLUMNS = 10;
    private static final int DEFAULT_KEYBOARD_ROWS = 4;

    protected final KP mParams;
    protected final Context mContext;
    protected final Resources mResources;

    private float mCurrentY = 0;
    private KeyboardCkRow mCurrentRow = null;
    private KeyCk mPreviousKeyCkInRow = null;
    private boolean mKeyboardDefined = false;

    public KeyboardCkBuilder(final Context context, final KP params) {
        mContext = context;
        final Resources res = context.getResources();
        mResources = res;

        mParams = params;

        params.mGridWidth = res.getInteger(R.integer.config_keyboard_grid_width);
        params.mGridHeight = res.getInteger(R.integer.config_keyboard_grid_height);
    }

    public void setAllowRedundantMoreKes(final boolean enabled) {
        mParams.mAllowRedundantMoreKeys = enabled;
    }

    public KeyboardCkBuilder<KP> load(final int xmlId, final KeyboardCkId id) {
        mParams.mId = id;
        final XmlResourceParser parser = mResources.getXml(xmlId);
        try {
            parseKeyboard(parser, false);
            if (!mKeyboardDefined) {
                throw new XmlParseCkUtils.ParseException("No " + TAG_KEYBOARD + " tag was found");
            }
        } catch (XmlPullParserException e) {
            Log.w(BUILDER_TAG, "keyboard XML parse error", e);
            throw new IllegalArgumentException(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(BUILDER_TAG, "keyboard XML parse error", e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            parser.close();
        }
        return this;
    }

    public KeyboardCk build() {
        return new KeyboardCk(mParams);
    }

    private int mIndent;
    private static final String SPACES = "                                             ";

    private static String spaces(final int count) {
        return (count < SPACES.length()) ? SPACES.substring(0, count) : SPACES;
    }

    private void startTag(final String format, final Object ... args) {
     }

    private void endTag(final String format, final Object ... args) {
     }

    private void startEndTag(final String format, final Object ... args) {
         mIndent--;
    }

    private void parseKeyboard(final XmlPullParser parser, final boolean skip)
            throws XmlPullParserException, IOException {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            final int event = parser.next();
            if (event == XmlPullParser.START_TAG) {
                final String tag = parser.getName();
                if (TAG_KEYBOARD.equals(tag)) {
                    if (DEBUG) startTag("<%s> %s%s", TAG_KEYBOARD, mParams.mId,
                            skip ? " skipped" : "");
                    if (!skip) {
                        if (mKeyboardDefined) {
                            throw new XmlParseCkUtils.ParseException("Only one " + TAG_KEYBOARD
                                    + " tag can be defined", parser);
                        }
                        mKeyboardDefined = true;
                        parseKeyboardAttributes(parser);
                        startKeyboard();
                    }
                    parseKeyboardContent(parser, skip);
                } else if (TAG_SWITCH.equals(tag)) {
                    parseSwitchKeyboard(parser, skip);
                } else {
                    throw new XmlParseCkUtils.IllegalStartTag(parser, tag, TAG_KEYBOARD);
                }
            } else if (event == XmlPullParser.END_TAG) {
                final String tag = parser.getName();
                if (DEBUG) endTag("</%s>", tag);
                if (TAG_CASE.equals(tag) || TAG_DEFAULT.equals(tag)) {
                    return;
                }
                throw new XmlParseCkUtils.IllegalEndTag(parser, tag, TAG_ROW);
            }
        }
    }

    private void parseKeyboardAttributes(final XmlPullParser parser) {
        final AttributeSet attr = Xml.asAttributeSet(parser);
        final TypedArray keyboardAttr = mContext.obtainStyledAttributes(
                attr, R.styleable.Keyboard, R.attr.keyboardStyle, R.style.Keyboard);
        final TypedArray keyAttr = mResources.obtainAttributes(attr, R.styleable.Keyboard_Key);
        try {
            final KeyboardCkParams params = mParams;
            final int height = params.mId.mHeight;
            final int width = params.mId.mWidth;
            // The bonus height isn't used to determine the other dimensions (gap/padding) to allow
            // those to stay consistent between layouts with and without the bonus height added.
            final int bonusHeight = (int)keyboardAttr.getFraction(R.styleable.Keyboard_bonusHeight,
                    height, height, 0);
            params.mOccupiedHeight = height + bonusHeight;
            params.mOccupiedWidth = width;
            params.mTopPadding = ResourceCkUtils.getDimensionOrFraction(keyboardAttr,
                    R.styleable.Keyboard_keyboardTopPadding, height, 0);
            params.mBottomPadding = ResourceCkUtils.getDimensionOrFraction(keyboardAttr,
                    R.styleable.Keyboard_keyboardBottomPadding, height, 0);
            params.mLeftPadding = ResourceCkUtils.getDimensionOrFraction(keyboardAttr,
                    R.styleable.Keyboard_keyboardLeftPadding, width, 0);
            params.mRightPadding = ResourceCkUtils.getDimensionOrFraction(keyboardAttr,
                    R.styleable.Keyboard_keyboardRightPadding, width, 0);

            params.mHorizontalGap = keyboardAttr.getFraction(
                    R.styleable.Keyboard_horizontalGap, width, width, 0);
            final float baseWidth = params.mOccupiedWidth - params.mLeftPadding
                    - params.mRightPadding + params.mHorizontalGap;
            params.mBaseWidth = baseWidth;
            params.mDefaultKeyPaddedWidth = ResourceCkUtils.getFraction(keyAttr,
                    R.styleable.Keyboard_Key_keyWidth, baseWidth,
                    baseWidth / DEFAULT_KEYBOARD_COLUMNS);

            params.mVerticalGap = keyboardAttr.getFraction(
                    R.styleable.Keyboard_verticalGap, height, height, 0);
            final float baseHeight = params.mOccupiedHeight - params.mTopPadding
                    - params.mBottomPadding + params.mVerticalGap;
            params.mBaseHeight = baseHeight;
            params.mDefaultRowHeight = ResourceCkUtils.getDimensionOrFraction(keyboardAttr,
                    R.styleable.Keyboard_rowHeight, baseHeight, baseHeight / DEFAULT_KEYBOARD_ROWS);

            params.mKeyVisualCkAttributes = KeyVisualCkAttributes.newInstance(keyAttr);

            params.mMoreKeysTemplate = keyboardAttr.getResourceId(
                    R.styleable.Keyboard_moreKeysTemplate, 0);
            params.mMaxMoreKeysKeyboardColumn = keyAttr.getInt(
                    R.styleable.Keyboard_Key_maxMoreKeysColumn, 5);

            params.mIconsSet.loadIcons(keyboardAttr);
            params.mTextsSet.setLocale(params.mId.getLocale(), mContext);
        } finally {
            keyAttr.recycle();
            keyboardAttr.recycle();
        }
    }

    private void parseKeyboardContent(final XmlPullParser parser, final boolean skip)
            throws XmlPullParserException, IOException {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            final int event = parser.next();
            if (event == XmlPullParser.START_TAG) {
                final String tag = parser.getName();
                if (TAG_ROW.equals(tag)) {
                    final KeyboardCkRow row = parseRowAttributes(parser);
                    if (DEBUG) startTag("<%s>%s", TAG_ROW, skip ? " skipped" : "");
                    if (!skip) {
                        startRow(row);
                    }
                    parseRowContent(parser, row, skip);
                } else if (TAG_INCLUDE.equals(tag)) {
                    parseIncludeKeyboardContent(parser, skip);
                } else if (TAG_SWITCH.equals(tag)) {
                    parseSwitchKeyboardContent(parser, skip);
                } else if (TAG_KEY_STYLE.equals(tag)) {
                    parseKeyStyle(parser, skip);
                } else {
                    throw new XmlParseCkUtils.IllegalStartTag(parser, tag, TAG_ROW);
                }
            } else if (event == XmlPullParser.END_TAG) {
                final String tag = parser.getName();
                if (DEBUG) endTag("</%s>", tag);
                if (TAG_KEYBOARD.equals(tag)) {
                    endKeyboard();
                    return;
                }
                if (TAG_CASE.equals(tag) || TAG_DEFAULT.equals(tag) || TAG_MERGE.equals(tag)) {
                    return;
                }
                throw new XmlParseCkUtils.IllegalEndTag(parser, tag, TAG_ROW);
            }
        }
    }

    private KeyboardCkRow parseRowAttributes(final XmlPullParser parser)
            throws XmlPullParserException {
        final AttributeSet attr = Xml.asAttributeSet(parser);
        final TypedArray keyboardAttr = mResources.obtainAttributes(attr, R.styleable.Keyboard);
        try {
            if (keyboardAttr.hasValue(R.styleable.Keyboard_horizontalGap)) {
                throw new XmlParseCkUtils.IllegalAttribute(parser, TAG_ROW, "horizontalGap");
            }
            if (keyboardAttr.hasValue(R.styleable.Keyboard_verticalGap)) {
                throw new XmlParseCkUtils.IllegalAttribute(parser, TAG_ROW, "verticalGap");
            }
            return new KeyboardCkRow(mResources, mParams, parser, mCurrentY);
        } finally {
            keyboardAttr.recycle();
        }
    }

    private void parseRowContent(final XmlPullParser parser, final KeyboardCkRow row,
            final boolean skip) throws XmlPullParserException, IOException {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            final int event = parser.next();
            if (event == XmlPullParser.START_TAG) {
                final String tag = parser.getName();
                if (TAG_KEY.equals(tag)) {
                    parseKey(parser, row, skip);
                } else if (TAG_SPACER.equals(tag)) {
                    parseSpacer(parser, row, skip);
                } else if (TAG_INCLUDE.equals(tag)) {
                    parseIncludeRowContent(parser, row, skip);
                } else if (TAG_SWITCH.equals(tag)) {
                    parseSwitchRowContent(parser, row, skip);
                } else if (TAG_KEY_STYLE.equals(tag)) {
                    parseKeyStyle(parser, skip);
                } else {
                    throw new XmlParseCkUtils.IllegalStartTag(parser, tag, TAG_ROW);
                }
            } else if (event == XmlPullParser.END_TAG) {
                final String tag = parser.getName();
                if (DEBUG) endTag("</%s>", tag);
                if (TAG_ROW.equals(tag)) {
                    if (!skip) {
                        endRow(row);
                    }
                    return;
                }
                if (TAG_CASE.equals(tag) || TAG_DEFAULT.equals(tag) || TAG_MERGE.equals(tag)) {
                    return;
                }
                throw new XmlParseCkUtils.IllegalEndTag(parser, tag, TAG_ROW);
            }
        }
    }

    private void parseKey(final XmlPullParser parser, final KeyboardCkRow row, final boolean skip)
            throws XmlPullParserException, IOException {
        if (skip) {
            XmlParseCkUtils.checkEndTag(TAG_KEY, parser);
            if (DEBUG) startEndTag("<%s /> skipped", TAG_KEY);
            return;
        }
        final TypedArray keyAttr = mResources.obtainAttributes(
                Xml.asAttributeSet(parser), R.styleable.Keyboard_Key);
        final KeyCkStyle keyCkStyle = mParams.mKeyStyles.getKeyStyle(keyAttr, parser);
        final String keySpec = keyCkStyle.getString(keyAttr, R.styleable.Keyboard_Key_keySpec);
        if (TextUtils.isEmpty(keySpec)) {
            throw new ParseException("Empty keySpec", parser);
        }
        final KeyCk keyCk = new KeyCk(keySpec, keyAttr, keyCkStyle, mParams, row);
        keyAttr.recycle();
        if (DEBUG) {
            startEndTag("<%s %s moreKeys=%s />", TAG_KEY, keyCk, Arrays.toString(keyCk.getMoreKeys()));
        }
        XmlParseCkUtils.checkEndTag(TAG_KEY, parser);
        endKey(keyCk, row);
    }

    private void parseSpacer(final XmlPullParser parser, final KeyboardCkRow row, final boolean skip)
            throws XmlPullParserException, IOException {
        if (skip) {
            XmlParseCkUtils.checkEndTag(TAG_SPACER, parser);
            if (DEBUG) startEndTag("<%s /> skipped", TAG_SPACER);
            return;
        }
        final TypedArray keyAttr = mResources.obtainAttributes(
                Xml.asAttributeSet(parser), R.styleable.Keyboard_Key);
        final KeyCkStyle keyCkStyle = mParams.mKeyStyles.getKeyStyle(keyAttr, parser);
        final KeyCk spacer = new KeyCk.Spacer(keyAttr, keyCkStyle, mParams, row);
        keyAttr.recycle();
        if (DEBUG) startEndTag("<%s />", TAG_SPACER);
        XmlParseCkUtils.checkEndTag(TAG_SPACER, parser);
        endKey(spacer, row);
    }

    private void parseIncludeKeyboardContent(final XmlPullParser parser, final boolean skip)
            throws XmlPullParserException, IOException {
        parseIncludeInternal(parser, null, skip);
    }

    private void parseIncludeRowContent(final XmlPullParser parser, final KeyboardCkRow row,
            final boolean skip) throws XmlPullParserException, IOException {
        parseIncludeInternal(parser, row, skip);
    }

    private void parseIncludeInternal(final XmlPullParser parser, final KeyboardCkRow row,
            final boolean skip) throws XmlPullParserException, IOException {
        if (skip) {
            XmlParseCkUtils.checkEndTag(TAG_INCLUDE, parser);
            if (DEBUG) startEndTag("</%s> skipped", TAG_INCLUDE);
            return;
        }
        final AttributeSet attr = Xml.asAttributeSet(parser);
        final TypedArray keyboardAttr = mResources.obtainAttributes(
                attr, R.styleable.Keyboard_Include);
        final TypedArray includeAttr = mResources.obtainAttributes(
                attr, R.styleable.Keyboard);
        mParams.mDefaultRowHeight = ResourceCkUtils.getDimensionOrFraction(includeAttr,
                R.styleable.Keyboard_rowHeight, mParams.mBaseHeight, mParams.mDefaultRowHeight);

        final TypedArray keyAttr = mResources.obtainAttributes(attr, R.styleable.Keyboard_Key);
        int keyboardLayout = 0;
        try {
            XmlParseCkUtils.checkAttributeExists(
                    keyboardAttr, R.styleable.Keyboard_Include_keyboardLayout, "keyboardLayout",
                    TAG_INCLUDE, parser);
            keyboardLayout = keyboardAttr.getResourceId(
                    R.styleable.Keyboard_Include_keyboardLayout, 0);
            if (row != null) {
                // Override current x coordinate.
                row.updateXPos(keyAttr);
                // Push current Row attributes and update with new attributes.
                row.pushRowAttributes(keyAttr);
            }
        } finally {
            keyboardAttr.recycle();
            keyAttr.recycle();
            includeAttr.recycle();
        }

        XmlParseCkUtils.checkEndTag(TAG_INCLUDE, parser);
        if (DEBUG) {
            startEndTag("<%s keyboardLayout=%s />",TAG_INCLUDE,
                    mResources.getResourceEntryName(keyboardLayout));
        }
        final XmlResourceParser parserForInclude = mResources.getXml(keyboardLayout);
        try {
            parseMerge(parserForInclude, row, skip);
        } finally {
            if (row != null) {
                 row.popRowAttributes();
            }
            parserForInclude.close();
        }
    }

    private void parseMerge(final XmlPullParser parser, final KeyboardCkRow row, final boolean skip)
            throws XmlPullParserException, IOException {
        if (DEBUG) startTag("<%s>", TAG_MERGE);
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            final int event = parser.next();
            if (event == XmlPullParser.START_TAG) {
                final String tag = parser.getName();
                if (TAG_MERGE.equals(tag)) {
                    if (row == null) {
                        parseKeyboardContent(parser, skip);
                    } else {
                        parseRowContent(parser, row, skip);
                    }
                    return;
                }
                throw new XmlParseCkUtils.ParseException(
                        "Included keyboard layout must have <merge> root element", parser);
            }
        }
    }

    private void parseSwitchKeyboard(final XmlPullParser parser, final boolean skip)
            throws XmlPullParserException, IOException {
        parseSwitchInternal(parser, true, null, skip);
    }

    private void parseSwitchKeyboardContent(final XmlPullParser parser, final boolean skip)
            throws XmlPullParserException, IOException {
        parseSwitchInternal(parser, false, null, skip);
    }

    private void parseSwitchRowContent(final XmlPullParser parser, final KeyboardCkRow row,
            final boolean skip) throws XmlPullParserException, IOException {
        parseSwitchInternal(parser, false, row, skip);
    }

    private void parseSwitchInternal(final XmlPullParser parser, final boolean parseKeyboard,
                                     final KeyboardCkRow row, final boolean skip) throws XmlPullParserException, IOException {
        if (DEBUG) startTag("<%s> %s", TAG_SWITCH, mParams.mId);
        boolean selected = false;
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            final int event = parser.next();
            if (event == XmlPullParser.START_TAG) {
                final String tag = parser.getName();
                if (TAG_CASE.equals(tag)) {
                    selected |= parseCase(parser, parseKeyboard, row, selected || skip);
                } else if (TAG_DEFAULT.equals(tag)) {
                    selected |= parseDefault(parser, parseKeyboard, row, selected || skip);
                } else {
                    throw new XmlParseCkUtils.IllegalStartTag(parser, tag, TAG_SWITCH);
                }
            } else if (event == XmlPullParser.END_TAG) {
                final String tag = parser.getName();
                if (TAG_SWITCH.equals(tag)) {
                    if (DEBUG) endTag("</%s>", TAG_SWITCH);
                    return;
                }
                throw new XmlParseCkUtils.IllegalEndTag(parser, tag, TAG_SWITCH);
            }
        }
    }

    private boolean parseCase(final XmlPullParser parser, final boolean parseKeyboard,
                              final KeyboardCkRow row, final boolean skip) throws XmlPullParserException, IOException {
        final boolean selected = parseCaseCondition(parser);
        if (parseKeyboard) {

            parseKeyboard(parser, !selected || skip);
        } else if (row == null) {
             parseKeyboardContent(parser, !selected || skip);
        } else {

            parseRowContent(parser, row, !selected || skip);
        }
        return selected;
    }

    private boolean parseCaseCondition(final XmlPullParser parser) {
        final KeyboardCkId id = mParams.mId;
        if (id == null) {
            return true;
        }
        final AttributeSet attr = Xml.asAttributeSet(parser);
        final TypedArray caseAttr = mResources.obtainAttributes(attr, R.styleable.Keyboard_Case);
        if (DEBUG) startTag("<%s>", TAG_CASE);
        try {
            final boolean keyboardLayoutSetMatched = matchString(caseAttr,
                    R.styleable.Keyboard_Case_keyboardLayoutSet,
                    id.mSubtypeCk.getKeyboardLayoutSet());
            final boolean keyboardLayoutSetElementMatched = matchTypedValue(caseAttr,
                    R.styleable.Keyboard_Case_keyboardLayoutSetElement, id.mElementId,
                    KeyboardCkId.elementIdToName(id.mElementId));
            final boolean keyboardThemeMatched = matchTypedValue(caseAttr,
                    R.styleable.Keyboard_Case_keyboardTheme, id.mThemeId,
                    KeyboardCkTheme.getKeyboardThemeName(id.mThemeId));
            final boolean modeMatched = matchTypedValue(caseAttr,
                    R.styleable.Keyboard_Case_mode, id.mMode, KeyboardCkId.modeName(id.mMode));
            final boolean navigateNextMatched = matchBoolean(caseAttr,
                    R.styleable.Keyboard_Case_navigateNext, id.navigateNext());
            final boolean navigatePreviousMatched = matchBoolean(caseAttr,
                    R.styleable.Keyboard_Case_navigatePrevious, id.navigatePrevious());
            final boolean passwordInputMatched = matchBoolean(caseAttr,
                    R.styleable.Keyboard_Case_passwordInput, id.passwordInput());
            final boolean languageSwitchKeyEnabledMatched = matchBoolean(caseAttr,
                    R.styleable.Keyboard_Case_languageSwitchKeyEnabled,
                    id.mLanguageSwitchKeyEnabled);
            final boolean isMultiLineMatched = matchBoolean(caseAttr,
                    R.styleable.Keyboard_Case_isMultiLine, id.isMultiLine());
            final boolean imeActionMatched = matchInteger(caseAttr,
                    R.styleable.Keyboard_Case_imeAction, id.imeAction());
            final boolean isIconDefinedMatched = isIconDefined(caseAttr,
                    R.styleable.Keyboard_Case_isIconDefined, mParams.mIconsSet);
            final Locale locale = id.getLocale();
            final boolean localeCodeMatched = matchLocaleCodes(caseAttr, locale);
            final boolean languageCodeMatched = matchLanguageCodes(caseAttr, locale);
            final boolean countryCodeMatched = matchCountryCodes(caseAttr, locale);
            final boolean showMoreKeysMatched = matchBoolean(caseAttr,
                    R.styleable.Keyboard_Case_showExtraChars, id.mShowMoreKeys);
            final boolean showNumberRowMatched = matchBoolean(caseAttr,
                    R.styleable.Keyboard_Case_showNumberRow, id.mShowNumberRow);
            final boolean selected = keyboardLayoutSetMatched && keyboardLayoutSetElementMatched
                    && keyboardThemeMatched && modeMatched && navigateNextMatched
                    && navigatePreviousMatched && passwordInputMatched
                    && languageSwitchKeyEnabledMatched
                    && isMultiLineMatched && imeActionMatched && isIconDefinedMatched
                    && localeCodeMatched && languageCodeMatched && countryCodeMatched
                    && showMoreKeysMatched && showNumberRowMatched;

            return selected;
        } finally {
            caseAttr.recycle();
        }
    }

    private static boolean matchLocaleCodes(TypedArray caseAttr, final Locale locale) {
        return matchString(caseAttr, R.styleable.Keyboard_Case_localeCode, locale.toString());
    }

    private static boolean matchLanguageCodes(TypedArray caseAttr, Locale locale) {
        return matchString(caseAttr, R.styleable.Keyboard_Case_languageCode, locale.getLanguage());
    }

    private static boolean matchCountryCodes(TypedArray caseAttr, Locale locale) {
        return matchString(caseAttr, R.styleable.Keyboard_Case_countryCode, locale.getCountry());
    }

    private static boolean matchInteger(final TypedArray a, final int index, final int value) {

        return !a.hasValue(index) || a.getInt(index, 0) == value;
    }

    private static boolean matchBoolean(final TypedArray a, final int index, final boolean value) {

        return !a.hasValue(index) || a.getBoolean(index, false) == value;
    }

    private static boolean matchString(final TypedArray a, final int index, final String value) {

        return !a.hasValue(index)
                || StringCkUtils.containsInArray(value, a.getString(index).split("\\|"));
    }

    private static boolean matchTypedValue(final TypedArray a, final int index, final int intValue,
            final String strValue) {

        final TypedValue v = a.peekValue(index);
        if (v == null) {
            return true;
        }
        if (ResourceCkUtils.isIntegerValue(v)) {
            return intValue == a.getInt(index, 0);
        }
        if (ResourceCkUtils.isStringValue(v)) {
            return StringCkUtils.containsInArray(strValue, a.getString(index).split("\\|"));
        }
        return false;
    }

    private static boolean isIconDefined(final TypedArray a, final int index,
            final KeyboardCkIconsSet iconsSet) {
        if (!a.hasValue(index)) {
            return true;
        }
        final String iconName = a.getString(index);
        final int iconId = KeyboardCkIconsSet.getIconId(iconName);
        return iconsSet.getIconDrawable(iconId) != null;
    }

    private boolean parseDefault(final XmlPullParser parser, final boolean parseKeyboard,
                                 final KeyboardCkRow row, final boolean skip) throws XmlPullParserException, IOException {
        if (DEBUG) startTag("<%s>", TAG_DEFAULT);
        if (parseKeyboard) {
            parseKeyboard(parser, skip);
        } else if (row == null) {
            parseKeyboardContent(parser, skip);
        } else {
            parseRowContent(parser, row, skip);
        }
        return true;
    }

    private void parseKeyStyle(final XmlPullParser parser, final boolean skip)
            throws XmlPullParserException, IOException {
        final AttributeSet attr = Xml.asAttributeSet(parser);
        final TypedArray keyStyleAttr = mResources.obtainAttributes(
                attr, R.styleable.Keyboard_KeyStyle);
        final TypedArray keyAttrs = mResources.obtainAttributes(attr, R.styleable.Keyboard_Key);
        try {
            if (!keyStyleAttr.hasValue(R.styleable.Keyboard_KeyStyle_styleName)) {
                throw new XmlParseCkUtils.ParseException("<" + TAG_KEY_STYLE
                        + "/> needs styleName attribute", parser);
            }
            if (DEBUG) {
                startEndTag("<%s styleName=%s />%s", TAG_KEY_STYLE,
                        keyStyleAttr.getString(R.styleable.Keyboard_KeyStyle_styleName),
                        skip ? " skipped" : "");
            }
            if (!skip) {
                mParams.mKeyStyles.parseKeyStyleAttributes(keyStyleAttr, keyAttrs, parser);
            }
        } finally {
            keyStyleAttr.recycle();
            keyAttrs.recycle();
        }
        XmlParseCkUtils.checkEndTag(TAG_KEY_STYLE, parser);
    }

    private void startKeyboard() {

    }

    private void startRow(final KeyboardCkRow row) {
        mCurrentRow = row;
        mPreviousKeyCkInRow = null;
    }

    private void endRow(final KeyboardCkRow row) {
        if (mCurrentRow == null) {
            throw new RuntimeException("orphan end row tag");
        }
        if (mPreviousKeyCkInRow != null && !mPreviousKeyCkInRow.isSpacer()) {
            setKeyHitboxRightEdge(mPreviousKeyCkInRow, mParams.mOccupiedWidth);
            mPreviousKeyCkInRow = null;
        }
        mCurrentY += row.getRowHeight();
        mCurrentRow = null;
    }

    private void endKey(final KeyCk keyCk, final KeyboardCkRow row) {
        mParams.onAddKey(keyCk);
        if (mPreviousKeyCkInRow != null && !mPreviousKeyCkInRow.isSpacer()) {

            setKeyHitboxRightEdge(mPreviousKeyCkInRow, row.getKeyX() - row.getKeyLeftPadding());
        }
        mPreviousKeyCkInRow = keyCk;
    }

    private void setKeyHitboxRightEdge(final KeyCk keyCk, final float xPos) {
        final int keyRight = keyCk.getX() + keyCk.getWidth();
        final float padding = xPos - keyRight;
        keyCk.setHitboxRightEdge(Math.round(padding) + keyRight);
    }

    private void endKeyboard() {
        mParams.removeRedundantMoreKeys();
    }
}
