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

package com.test.testing12345.keyboard;

import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Locale;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.internal.KeyDrawCkParams;
import com.test.testing12345.keyboard.internal.KeySpecCkParser;
import com.test.testing12345.keyboard.internal.KeyCkStyle;
import com.test.testing12345.keyboard.internal.KeyVisualCkAttributes;
import com.test.testing12345.keyboard.internal.KeyboardCkIconsSet;
import com.test.testing12345.keyboard.internal.KeyboardCkParams;
import com.test.testing12345.keyboard.internal.KeyboardCkRow;
import com.test.testing12345.keyboard.internal.MoreKeyCkSpec;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.StringCkUtils;

import static com.test.testing12345.custom.common.ConstantsCk.CODE_OUTPUT_TEXT;
import static com.test.testing12345.custom.common.ConstantsCk.CODE_SHIFT;
import static com.test.testing12345.custom.common.ConstantsCk.CODE_SWITCH_ALPHA_SYMBOL;
import static com.test.testing12345.custom.common.ConstantsCk.CODE_UNSPECIFIED;


public class KeyCk implements Comparable<KeyCk> {
     private final int mCode;

     private final String mLabel;
     private final String mHintLabel;
     private final int mLabelFlags;
    private static final int LABEL_FLAGS_ALIGN_HINT_LABEL_TO_BOTTOM = 0x02;
    private static final int LABEL_FLAGS_ALIGN_ICON_TO_BOTTOM = 0x04;
    private static final int LABEL_FLAGS_ALIGN_LABEL_OFF_CENTER = 0x08;
     private static final int LABEL_FLAGS_FONT_MASK = 0x30;
    private static final int LABEL_FLAGS_FONT_NORMAL = 0x10;
    private static final int LABEL_FLAGS_FONT_MONO_SPACE = 0x20;
    private static final int LABEL_FLAGS_FONT_DEFAULT = 0x30;
    // Start of key text ratio enum values
    private static final int LABEL_FLAGS_FOLLOW_KEY_TEXT_RATIO_MASK = 0x1C0;
    private static final int LABEL_FLAGS_FOLLOW_KEY_LARGE_LETTER_RATIO = 0x40;
    private static final int LABEL_FLAGS_FOLLOW_KEY_LETTER_RATIO = 0x80;
    private static final int LABEL_FLAGS_FOLLOW_KEY_LABEL_RATIO = 0xC0;
    private static final int LABEL_FLAGS_FOLLOW_KEY_HINT_LABEL_RATIO = 0x140;
     private static final int LABEL_FLAGS_HAS_SHIFTED_LETTER_HINT = 0x400;
    private static final int LABEL_FLAGS_HAS_HINT_LABEL = 0x800;

    private static final int LABEL_FLAGS_AUTO_X_SCALE = 0x4000;
    private static final int LABEL_FLAGS_AUTO_Y_SCALE = 0x8000;
    private static final int LABEL_FLAGS_AUTO_SCALE = LABEL_FLAGS_AUTO_X_SCALE
            | LABEL_FLAGS_AUTO_Y_SCALE;
    private static final int LABEL_FLAGS_PRESERVE_CASE = 0x10000;
    private static final int LABEL_FLAGS_SHIFTED_LETTER_ACTIVATED = 0x20000;
    private static final int LABEL_FLAGS_FROM_CUSTOM_ACTION_LABEL = 0x40000;
    private static final int LABEL_FLAGS_FOLLOW_FUNCTIONAL_TEXT_COLOR = 0x80000;
    private static final int LABEL_FLAGS_DISABLE_HINT_LABEL = 0x40000000;
    private static final int LABEL_FLAGS_DISABLE_ADDITIONAL_MORE_KEYS = 0x80000000;

     private final int mIconId;

     private final int mWidth;
     private final int mHeight;
     private final float mDefinedWidth;
     private final float mDefinedHeight;

    private final int mX;

    private final int mY;
     private final Rect mHitbox = new Rect();

     private final MoreKeyCkSpec[] mMoreKeys;
     private final int mMoreKeysColumnAndFlags;
    private static final int MORE_KEYS_COLUMN_NUMBER_MASK = 0x000000ff;

    private static final int MORE_KEYS_FLAGS_FIXED_COLUMN = 0x00000100;

    private static final int MORE_KEYS_FLAGS_FIXED_ORDER = 0x00000200;
    private static final int MORE_KEYS_MODE_MAX_COLUMN_WITH_AUTO_ORDER = 0;
    private static final int MORE_KEYS_MODE_FIXED_COLUMN_WITH_AUTO_ORDER =
            MORE_KEYS_FLAGS_FIXED_COLUMN;
    private static final int MORE_KEYS_MODE_FIXED_COLUMN_WITH_FIXED_ORDER =
            (MORE_KEYS_FLAGS_FIXED_COLUMN | MORE_KEYS_FLAGS_FIXED_ORDER);
    private static final int MORE_KEYS_FLAGS_HAS_LABELS = 0x40000000;
    private static final int MORE_KEYS_FLAGS_NO_PANEL_AUTO_MORE_KEY = 0x10000000;
     private static final String MORE_KEYS_AUTO_COLUMN_ORDER = "!autoColumnOrder!";
    private static final String MORE_KEYS_FIXED_COLUMN_ORDER = "!fixedColumnOrder!";
    private static final String MORE_KEYS_HAS_LABELS = "!hasLabels!";
    private static final String MORE_KEYS_NO_PANEL_AUTO_MORE_KEY = "!noPanelAutoMoreKey!";
    private boolean mEnabled;
     private final int mBackgroundType;
    public static final int BACKGROUND_TYPE_EMPTY = 0;
    public static final int BACKGROUND_TYPE_NORMAL = 1;
    public static final int BACKGROUND_TYPE_FUNCTIONAL = 2;
    public static final int BACKGROUND_TYPE_ACTION = 5;
    public static final int BACKGROUND_TYPE_SPACEBAR = 6;

    private final int mActionFlags;
    private static final int ACTION_FLAGS_IS_REPEATABLE = 0x01;
    private static final int ACTION_FLAGS_NO_KEY_PREVIEW = 0x02;
    private static final int ACTION_FLAGS_ALT_CODE_WHILE_TYPING = 0x04;
    private static final int ACTION_FLAGS_ENABLE_LONG_PRESS = 0x08;

    private final KeyVisualCkAttributes mKeyVisualCkAttributes;
    private final OptionalAttributes mOptionalAttributes;

    private static final class OptionalAttributes {
         public final String mOutputText;
        public final int mAltCode;

        private OptionalAttributes(final String outputText, final int altCode) {
            mOutputText = outputText;
            mAltCode = altCode;
        }

        public static OptionalAttributes newInstance(final String outputText, final int altCode) {
            if (outputText == null && altCode == CODE_UNSPECIFIED) {
                return null;
            }
            return new OptionalAttributes(outputText, altCode);
        }
    }

    private final int mHashCode;

     private boolean mPressed;

    public KeyCk(final String label, final int iconId, final int code, final String outputText,
                 final String hintLabel, final int labelFlags, final int backgroundType,
                 final float x, final float y, final float width, final float height,
                 final float leftPadding, final float rightPadding, final float topPadding,
                 final float bottomPadding) {
        this.mEnabled = true;

        mHitbox.set(Math.round(x - leftPadding), Math.round(y - topPadding),
                Math.round(x + width + rightPadding), Math.round(y + height + bottomPadding));
        mX = Math.round(x);
        mY = Math.round(y);
        mWidth = Math.round(x + width) - mX;
        mHeight = Math.round(y + height) - mY;
        mDefinedWidth = width;
        mDefinedHeight = height;
        mHintLabel = hintLabel;
        mLabelFlags = labelFlags;
        boolean z = false;
        mBackgroundType = backgroundType;
        // TODO: Pass keyActionFlags as an argument.
        mActionFlags = ACTION_FLAGS_NO_KEY_PREVIEW;
        mMoreKeys = null;
        mMoreKeysColumnAndFlags = 0;
        mLabel = label;
        mOptionalAttributes = OptionalAttributes.newInstance(outputText, CODE_UNSPECIFIED);
        mCode = code;
        this.mEnabled = code != -15 ? true : z;
        mIconId = iconId;
        mKeyVisualCkAttributes = null;

        mHashCode = computeHashCode(this);
    }

    public KeyCk(final String keySpec, final TypedArray keyAttr,
                 final KeyCkStyle style, final KeyboardCkParams params,
                 final KeyboardCkRow row) {
         row.setCurrentKey(keyAttr, isSpacer());
        this.mEnabled = true;
        mDefinedWidth = row.getKeyWidth();
        mDefinedHeight = row.getKeyHeight();

        final float keyLeft = row.getKeyX();
        final float keyTop = row.getKeyY();
        final float keyRight = keyLeft + mDefinedWidth;
        final float keyBottom = keyTop + mDefinedHeight;

        final float leftPadding = row.getKeyLeftPadding();
        final float topPadding = row.getKeyTopPadding();
        final float rightPadding = row.getKeyRightPadding();
        final float bottomPadding = row.getKeyBottomPadding();

        mHitbox.set(Math.round(keyLeft - leftPadding), Math.round(keyTop - topPadding),
                Math.round(keyRight + rightPadding), Math.round(keyBottom + bottomPadding));
        mX = Math.round(keyLeft);
        mY = Math.round(keyTop);
        mWidth = Math.round(keyRight) - mX;
        mHeight = Math.round(keyBottom) - mY;

        mBackgroundType = style.getInt(keyAttr,
                R.styleable.Keyboard_Key_backgroundType, row.getDefaultBackgroundType());

        mLabelFlags = style.getFlags(keyAttr, R.styleable.Keyboard_Key_keyLabelFlags)
                | row.getDefaultKeyLabelFlags();
        final boolean needsToUpcase = needsToUpcase(mLabelFlags, params.mId.mElementId);
        final Locale localeForUpcasing = params.mId.getLocale();
        int actionFlags = style.getFlags(keyAttr, R.styleable.Keyboard_Key_keyActionFlags);
        String[] moreKeys = style.getStringArray(keyAttr, R.styleable.Keyboard_Key_moreKeys);

         int moreKeysColumnAndFlags = MORE_KEYS_MODE_MAX_COLUMN_WITH_AUTO_ORDER
                | style.getInt(keyAttr, R.styleable.Keyboard_Key_maxMoreKeysColumn,
                        params.mMaxMoreKeysKeyboardColumn);
        int value;
        if ((value = MoreKeyCkSpec.getIntValue(moreKeys, MORE_KEYS_AUTO_COLUMN_ORDER, -1)) > 0) {
             moreKeysColumnAndFlags = MORE_KEYS_MODE_FIXED_COLUMN_WITH_AUTO_ORDER
                    | (value & MORE_KEYS_COLUMN_NUMBER_MASK);
        }
        if ((value = MoreKeyCkSpec.getIntValue(moreKeys, MORE_KEYS_FIXED_COLUMN_ORDER, -1)) > 0) {
             moreKeysColumnAndFlags = MORE_KEYS_MODE_FIXED_COLUMN_WITH_FIXED_ORDER
                    | (value & MORE_KEYS_COLUMN_NUMBER_MASK);
        }
        if (MoreKeyCkSpec.getBooleanValue(moreKeys, MORE_KEYS_HAS_LABELS)) {
            moreKeysColumnAndFlags |= MORE_KEYS_FLAGS_HAS_LABELS;
        }
        if (MoreKeyCkSpec.getBooleanValue(moreKeys, MORE_KEYS_NO_PANEL_AUTO_MORE_KEY)) {
            moreKeysColumnAndFlags |= MORE_KEYS_FLAGS_NO_PANEL_AUTO_MORE_KEY;
        }
        mMoreKeysColumnAndFlags = moreKeysColumnAndFlags;

        final String[] additionalMoreKeys;
        if ((mLabelFlags & LABEL_FLAGS_DISABLE_ADDITIONAL_MORE_KEYS) != 0) {
            additionalMoreKeys = null;
        } else {
            additionalMoreKeys = style.getStringArray(keyAttr,
                    R.styleable.Keyboard_Key_additionalMoreKeys);
        }
        moreKeys = MoreKeyCkSpec.insertAdditionalMoreKeys(moreKeys, additionalMoreKeys);
        if (moreKeys != null) {
            actionFlags |= ACTION_FLAGS_ENABLE_LONG_PRESS;
            mMoreKeys = new MoreKeyCkSpec[moreKeys.length];
            for (int i = 0; i < moreKeys.length; i++) {
                mMoreKeys[i] = new MoreKeyCkSpec(moreKeys[i], needsToUpcase, localeForUpcasing);
            }
        } else {
            mMoreKeys = null;
        }
        mActionFlags = actionFlags;

        mIconId = KeySpecCkParser.getIconId(keySpec);

        final int code = KeySpecCkParser.getCode(keySpec);
        if ((mLabelFlags & LABEL_FLAGS_FROM_CUSTOM_ACTION_LABEL) != 0) {
            mLabel = params.mId.mCustomActionLabel;
        } else if (code >= Character.MIN_SUPPLEMENTARY_CODE_POINT) {

            mLabel = new StringBuilder().appendCodePoint(code).toString();
        } else {
            final String label = KeySpecCkParser.getLabel(keySpec);
            mLabel = needsToUpcase
                    ? StringCkUtils.toTitleCaseOfKeyLabel(label, localeForUpcasing)
                    : label;
        }
        if ((mLabelFlags & LABEL_FLAGS_DISABLE_HINT_LABEL) != 0) {
            mHintLabel = null;
        } else {
            final String hintLabel = style.getString(
                    keyAttr, R.styleable.Keyboard_Key_keyHintLabel);
            mHintLabel = needsToUpcase
                    ? StringCkUtils.toTitleCaseOfKeyLabel(hintLabel, localeForUpcasing)
                    : hintLabel;
        }
        String outputText = KeySpecCkParser.getOutputText(keySpec);
        if (needsToUpcase) {
            outputText = StringCkUtils.toTitleCaseOfKeyLabel(outputText, localeForUpcasing);
        }
         if (code == CODE_UNSPECIFIED && TextUtils.isEmpty(outputText)
                && !TextUtils.isEmpty(mLabel)) {
            if (StringCkUtils.codePointCount(mLabel) == 1) {

                if (hasShiftedLetterHint() && isShiftedLetterActivated()) {
                    mCode = mHintLabel.codePointAt(0);
                } else {
                    mCode = mLabel.codePointAt(0);
                }
            } else {

                outputText = mLabel;
                mCode = CODE_OUTPUT_TEXT;
            }
        } else if (code == CODE_UNSPECIFIED && outputText != null) {
            if (StringCkUtils.codePointCount(outputText) == 1) {
                mCode = outputText.codePointAt(0);
                outputText = null;
            } else {
                mCode = CODE_OUTPUT_TEXT;
            }
        } else {
            mCode = needsToUpcase ? StringCkUtils.toTitleCaseOfKeyCode(code, localeForUpcasing)
                    : code;
        }
        final int altCodeInAttr = KeySpecCkParser.parseCode(
                style.getString(keyAttr, R.styleable.Keyboard_Key_altCode), CODE_UNSPECIFIED);
        final int altCode = needsToUpcase
                ? StringCkUtils.toTitleCaseOfKeyCode(altCodeInAttr, localeForUpcasing)
                : altCodeInAttr;
        mOptionalAttributes = OptionalAttributes.newInstance(outputText, altCode);
        mKeyVisualCkAttributes = KeyVisualCkAttributes.newInstance(keyAttr);
        mHashCode = computeHashCode(this);
    }


    protected KeyCk(final KeyCk keyCk) {
        this(keyCk, keyCk.mMoreKeys);
    }

    private KeyCk(final KeyCk keyCk, final MoreKeyCkSpec[] moreKeys) {
         this.mEnabled = true;
        mCode = keyCk.mCode;
        mLabel = keyCk.mLabel;
        mHintLabel = keyCk.mHintLabel;
        mLabelFlags = keyCk.mLabelFlags;
        mIconId = keyCk.mIconId;
        mWidth = keyCk.mWidth;
        mHeight = keyCk.mHeight;
        mDefinedWidth = keyCk.mDefinedWidth;
        mDefinedHeight = keyCk.mDefinedHeight;
        mX = keyCk.mX;
        mY = keyCk.mY;
        mHitbox.set(keyCk.mHitbox);
        mMoreKeys = moreKeys;
        mMoreKeysColumnAndFlags = keyCk.mMoreKeysColumnAndFlags;
        mBackgroundType = keyCk.mBackgroundType;
        mActionFlags = keyCk.mActionFlags;
        mKeyVisualCkAttributes = keyCk.mKeyVisualCkAttributes;
        mOptionalAttributes = keyCk.mOptionalAttributes;
        mHashCode = keyCk.mHashCode;
         this.mEnabled = keyCk.mEnabled;
        mPressed = keyCk.mPressed;
    }

    public static KeyCk removeRedundantMoreKeys(final KeyCk keyCk,
                                                final MoreKeyCkSpec.LettersOnBaseLayout lettersOnBaseLayout) {
        final MoreKeyCkSpec[] moreKeys = keyCk.getMoreKeys();
        final MoreKeyCkSpec[] filteredMoreKeys = MoreKeyCkSpec.removeRedundantMoreKeys(
                moreKeys, lettersOnBaseLayout);
        return (filteredMoreKeys == moreKeys) ? keyCk : new KeyCk(keyCk, filteredMoreKeys);
    }

    private static boolean needsToUpcase(final int labelFlags, final int keyboardElementId) {
        if ((labelFlags & LABEL_FLAGS_PRESERVE_CASE) != 0) return false;
        switch (keyboardElementId) {
        case KeyboardCkId.ELEMENT_ALPHABET_MANUAL_SHIFTED:
        case KeyboardCkId.ELEMENT_ALPHABET_AUTOMATIC_SHIFTED:
        case KeyboardCkId.ELEMENT_ALPHABET_SHIFT_LOCKED:
            return true;
        default:
            return false;
        }
    }
    public final boolean isEnabled() {
        return this.mEnabled;
    }
    private static int computeHashCode(final KeyCk keyCk) {
        return Arrays.hashCode(new Object[] {
                keyCk.mX,
                keyCk.mY,
                keyCk.mWidth,
                keyCk.mHeight,
                keyCk.mCode,
                keyCk.mLabel,
                keyCk.mHintLabel,
                keyCk.mIconId,
                keyCk.mBackgroundType,
                Arrays.hashCode(keyCk.mMoreKeys),
                keyCk.getOutputText(),
                keyCk.mActionFlags,
                keyCk.mLabelFlags,

        });
    }

    private boolean equalsInternal(final KeyCk o) {
        if (this == o) return true;
        return o.mX == mX
                && o.mY == mY
                && o.mWidth == mWidth
                && o.mHeight == mHeight
                && o.mCode == mCode
                && TextUtils.equals(o.mLabel, mLabel)
                && TextUtils.equals(o.mHintLabel, mHintLabel)
                && o.mIconId == mIconId
                && o.mBackgroundType == mBackgroundType
                && Arrays.equals(o.mMoreKeys, mMoreKeys)
                && TextUtils.equals(o.getOutputText(), getOutputText())
                && o.mActionFlags == mActionFlags
                && o.mLabelFlags == mLabelFlags;
    }

    @Override
    public int compareTo(KeyCk o) {
        if (equalsInternal(o)) return 0;
        if (mHashCode > o.mHashCode) return 1;
        return -1;
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof KeyCk && equalsInternal((KeyCk)o);
    }

    @Override
    public String toString() {
        return toShortString() + " " + getX() + "," + getY() + " " + getWidth() + "x" + getHeight();
    }

    public String toShortString() {
        final int code = getCode();
        if (code == ConstantsCk.CODE_OUTPUT_TEXT) {
            return getOutputText();
        }
        return ConstantsCk.printableCode(code);
    }

    public int getCode() {
        return mCode;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getHintLabel() {
        return mHintLabel;
    }

    public MoreKeyCkSpec[] getMoreKeys() {
        return mMoreKeys;
    }

    public void setHitboxRightEdge(final int right) {
        mHitbox.right = right;
    }

    public final boolean isSpacer() {
        return this instanceof Spacer;
    }

    public final boolean isActionKey() {
        return mBackgroundType == BACKGROUND_TYPE_ACTION;
    }

    public final boolean isShift() {
        return mCode == CODE_SHIFT;
    }

    public final boolean isModifier() {
        return mCode == CODE_SHIFT || mCode == CODE_SWITCH_ALPHA_SYMBOL;
    }

    public final boolean isRepeatable() {
        return (mActionFlags & ACTION_FLAGS_IS_REPEATABLE) != 0;
    }

    public final boolean noKeyPreview() {
        return (mActionFlags & ACTION_FLAGS_NO_KEY_PREVIEW) != 0;
    }

    public final boolean altCodeWhileTyping() {
        return (mActionFlags & ACTION_FLAGS_ALT_CODE_WHILE_TYPING) != 0;
    }

    public final boolean isLongPressEnabled() {
        return (mActionFlags & ACTION_FLAGS_ENABLE_LONG_PRESS) != 0
                && (mLabelFlags & LABEL_FLAGS_SHIFTED_LETTER_ACTIVATED) == 0;
    }

    public KeyVisualCkAttributes getVisualAttributes() {
        return mKeyVisualCkAttributes;
    }

    public final Typeface selectTypeface(final KeyDrawCkParams params) {
        switch (mLabelFlags & LABEL_FLAGS_FONT_MASK) {
        case LABEL_FLAGS_FONT_NORMAL:
            return Typeface.DEFAULT;
        case LABEL_FLAGS_FONT_MONO_SPACE:
            return Typeface.MONOSPACE;
        case LABEL_FLAGS_FONT_DEFAULT:
        default:
             return params.mTypeface;
        }
    }

    public final int selectTextSize(final KeyDrawCkParams params) {
        switch (mLabelFlags & LABEL_FLAGS_FOLLOW_KEY_TEXT_RATIO_MASK) {
        case LABEL_FLAGS_FOLLOW_KEY_LETTER_RATIO:
            return params.mLetterSize;
        case LABEL_FLAGS_FOLLOW_KEY_LARGE_LETTER_RATIO:
            return params.mLargeLetterSize;
        case LABEL_FLAGS_FOLLOW_KEY_LABEL_RATIO:
            return params.mLabelSize;
        case LABEL_FLAGS_FOLLOW_KEY_HINT_LABEL_RATIO:
            return params.mHintLabelSize;
        default:
            return StringCkUtils.codePointCount(mLabel) == 1 ? params.mLetterSize : params.mLabelSize;
        }
    }

    public final int selectTextColor(final KeyDrawCkParams params) {
        if ((mLabelFlags & LABEL_FLAGS_FOLLOW_FUNCTIONAL_TEXT_COLOR) != 0) {
            return params.mFunctionalTextColor;
        }
        return isShiftedLetterActivated() ? params.mTextInactivatedColor : params.mTextColor;
    }

    public final int selectHintTextSize(final KeyDrawCkParams params) {
        if (hasHintLabel()) {
            return params.mHintLabelSize;
        }
        if (hasShiftedLetterHint()) {
            return params.mShiftedLetterHintSize;
        }
        return params.mHintLetterSize;
    }

    public final int selectHintTextColor(final KeyDrawCkParams params) {
        if (hasHintLabel()) {
            return params.mHintLabelColor;
        }
        if (hasShiftedLetterHint()) {
            return isShiftedLetterActivated() ? params.mShiftedLetterHintActivatedColor
                    : params.mShiftedLetterHintInactivatedColor;
        }
        return params.mHintLetterColor;
    }

    public final String getPreviewLabel() {
        return isShiftedLetterActivated() ? mHintLabel : mLabel;
    }

    private boolean previewHasLetterSize() {
        return (mLabelFlags & LABEL_FLAGS_FOLLOW_KEY_LETTER_RATIO) != 0
                || StringCkUtils.codePointCount(getPreviewLabel()) == 1;
    }

    public final int selectPreviewTextSize(final KeyDrawCkParams params) {
        if (previewHasLetterSize()) {
            return params.mPreviewTextSize;
        }
        return params.mLetterSize;
    }

    public Typeface selectPreviewTypeface(final KeyDrawCkParams params) {
        if (previewHasLetterSize()) {
            return selectTypeface(params);
        }
        return Typeface.DEFAULT_BOLD;
    }

    public final boolean isAlignHintLabelToBottom(final int defaultFlags) {
        return ((mLabelFlags | defaultFlags) & LABEL_FLAGS_ALIGN_HINT_LABEL_TO_BOTTOM) != 0;
    }

    public final boolean isAlignIconToBottom() {
        return (mLabelFlags & LABEL_FLAGS_ALIGN_ICON_TO_BOTTOM) != 0;
    }

    public final boolean isAlignLabelOffCenter() {
        return (mLabelFlags & LABEL_FLAGS_ALIGN_LABEL_OFF_CENTER) != 0;
    }

    public final boolean hasShiftedLetterHint() {
        return (mLabelFlags & LABEL_FLAGS_HAS_SHIFTED_LETTER_HINT) != 0
                && !TextUtils.isEmpty(mHintLabel);
    }

    public final boolean hasHintLabel() {
        return (mLabelFlags & LABEL_FLAGS_HAS_HINT_LABEL) != 0;
    }

    public final boolean needsAutoXScale() {
        return (mLabelFlags & LABEL_FLAGS_AUTO_X_SCALE) != 0;
    }

    public final boolean needsAutoScale() {
        return (mLabelFlags & LABEL_FLAGS_AUTO_SCALE) == LABEL_FLAGS_AUTO_SCALE;
    }

    private final boolean isShiftedLetterActivated() {
        return (mLabelFlags & LABEL_FLAGS_SHIFTED_LETTER_ACTIVATED) != 0
                && !TextUtils.isEmpty(mHintLabel);
    }

    public final int getMoreKeysColumnNumber() {
        return mMoreKeysColumnAndFlags & MORE_KEYS_COLUMN_NUMBER_MASK;
    }

    public final boolean isMoreKeysFixedColumn() {
        return (mMoreKeysColumnAndFlags & MORE_KEYS_FLAGS_FIXED_COLUMN) != 0;
    }

    public final boolean isMoreKeysFixedOrder() {
        return (mMoreKeysColumnAndFlags & MORE_KEYS_FLAGS_FIXED_ORDER) != 0;
    }

    public final boolean hasLabelsInMoreKeys() {
        return (mMoreKeysColumnAndFlags & MORE_KEYS_FLAGS_HAS_LABELS) != 0;
    }

    public final int getMoreKeyLabelFlags() {
        final int labelSizeFlag = hasLabelsInMoreKeys()
                ? LABEL_FLAGS_FOLLOW_KEY_LABEL_RATIO
                : LABEL_FLAGS_FOLLOW_KEY_LETTER_RATIO;
        return labelSizeFlag | LABEL_FLAGS_AUTO_X_SCALE;
    }

    public final boolean hasPopupHint() {
        return (this.mLabelFlags & 512) != 0;
    }

    public final boolean hasNoPanelAutoMoreKey() {
        return (mMoreKeysColumnAndFlags & MORE_KEYS_FLAGS_NO_PANEL_AUTO_MORE_KEY) != 0;
    }

    public final String getOutputText() {
        final OptionalAttributes attrs = mOptionalAttributes;
        return (attrs != null) ? attrs.mOutputText : null;
    }

    public final int getAltCode() {
        final OptionalAttributes attrs = mOptionalAttributes;
        return (attrs != null) ? attrs.mAltCode : CODE_UNSPECIFIED;
    }
    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    public int getIconId() {
        return mIconId;
    }

    public Drawable getIcon(final KeyboardCkIconsSet iconSet, final int alpha) {
        final Drawable icon = iconSet.getIconDrawable(getIconId());
        if (icon != null) {
            icon.setAlpha(alpha);
        }

        return icon;
    }

    public Drawable getPreviewIcon(final KeyboardCkIconsSet iconSet) {
        return iconSet.getIconDrawable(getIconId());
    }



    public int getWidth() {
        return mWidth;
    }

     public int getHeight() {
        return mHeight;
    }

     public float getDefinedWidth() {
        return mDefinedWidth;
    }

     public float getDefinedHeight() {
        return mDefinedHeight;
    }



    public int getX() {
        return mX;
    }

     public int getY() {
        return mY;
    }

     public int getTopPadding() {
        return mY - mHitbox.top;
    }

     public int getBottomPadding() {
        return mHitbox.bottom - mY - mHeight;
    }

     public int getLeftPadding() {
        return mX - mHitbox.left;
    }

     public int getRightPadding() {
        return mHitbox.right - mX - mWidth;
    }

     public void onPressed() {
        mPressed = true;
    }

     public void onReleased() {
        mPressed = false;
    }

     public boolean isOnKey(final int x, final int y) {
        return mHitbox.contains(x, y);
    }

     public int squaredDistanceToHitboxEdge(final int x, final int y) {
        final int left = mHitbox.left;

        final int right = mHitbox.right - 1;
        final int top = mHitbox.top;

        final int bottom = mHitbox.bottom - 1;
        final int edgeX = x < left ? left : Math.min(x, right);
        final int edgeY = y < top ? top : Math.min(y, bottom);
        final int dx = x - edgeX;
        final int dy = y - edgeY;
        return dx * dx + dy * dy;
    }

    static class KeyBackgroundState {
        private final int[] mReleasedState;
        private final int[] mPressedState;

        private KeyBackgroundState(final int ... attrs) {
            mReleasedState = attrs;
            mPressedState = Arrays.copyOf(attrs, attrs.length + 1);
            mPressedState[attrs.length] = android.R.attr.state_pressed;
        }

        public int[] getState(final boolean pressed) {
            return pressed ? mPressedState : mReleasedState;
        }

        public static final KeyBackgroundState[] STATES = {

            new KeyBackgroundState(android.R.attr.state_empty),

            new KeyBackgroundState(),

            new KeyBackgroundState(),

            new KeyBackgroundState(android.R.attr.state_checkable),

            new KeyBackgroundState(android.R.attr.state_checkable, android.R.attr.state_checked),
             new KeyBackgroundState(android.R.attr.state_active),

            new KeyBackgroundState(),
        };
    }

     public final Drawable selectBackgroundDrawable(final Drawable keyBackground,
            final Drawable functionalKeyBackground,
            final Drawable spacebarBackground) {
        final Drawable background;
        if (mBackgroundType == BACKGROUND_TYPE_FUNCTIONAL) {
            background = functionalKeyBackground;
        } else if (mBackgroundType == BACKGROUND_TYPE_SPACEBAR) {
            background = spacebarBackground;
        } else {
            background = keyBackground;
        }
        final int[] state = KeyBackgroundState.STATES[mBackgroundType].getState(mPressed);
        background.setState(state);
        return background;
    }

    public static class Spacer extends KeyCk {
        public Spacer(final TypedArray keyAttr, final KeyCkStyle keyCkStyle,
                      final KeyboardCkParams params, final KeyboardCkRow row) {
            super(null /* keySpec */, keyAttr, keyCkStyle, params, row);
        }
    }
}
