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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.DKeboaApplication;
import com.test.testing12345.compat.PreferenceCkManagerCompat;
import com.test.testing12345.keyboard.internal.KeyDrawCkParams;
import com.test.testing12345.keyboard.internal.KeyVisualCkAttributes;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.settings.SettingsCk;
import com.test.testing12345.custom.utils.TypefaceCkUtils;
import com.test.testing12345.adsclass.StylishFontCkModel;
import com.test.testing12345.other.PrefCk;
import com.test.testing12345.other.StorageCkUtils;


public class KeyboardCkView extends View {
        private final KeyVisualCkAttributes mKeyVisualCkAttributes;

    private final int mDefaultKeyLabelFlags;
    private final float mKeyHintLetterPadding;
    private final float mKeyShiftedLetterHintPadding;
    private final float mKeyTextShadowRadius;
    private final float mVerticalCorrection;
    private final Drawable mKeyBackground;
    private final Drawable mFunctionalKeyBackground;
    private final Drawable mSpacebarBackground;
    private final float mSpacebarIconWidthRatio;
    private final Rect mKeyBackgroundPadding = new Rect();
    private static final float KET_TEXT_SHADOW_RADIUS_DISABLED = -1.0f;
    public int mCustomColor = 0;


    private static final float MAX_LABEL_RATIO = 0.90f;



    private KeyboardCk mKeyboardCk;
    private final KeyDrawCkParams mKeyDrawCkParams = new KeyDrawCkParams();


     private boolean mInvalidateAllKeys;
     private final HashSet<KeyCk> mInvalidatedKeyCks = new HashSet<>();
     private final Rect mClipRect = new Rect();
     private Bitmap mOffscreenBuffer;
     private final Canvas mOffscreenCanvas = new Canvas();
    private final Paint mPaint = new Paint();
    private final Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();

    public KeyboardCkView(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.keyboardViewStyle);
    }

    public KeyboardCkView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray keyboardViewAttr = context.obtainStyledAttributes(attrs,
                R.styleable.KeyboardView, defStyle, R.style.KeyboardView);
        mKeyBackground = keyboardViewAttr.getDrawable(R.styleable.KeyboardView_keyBackground);
        mKeyBackground.getPadding(mKeyBackgroundPadding);
        final Drawable functionalKeyBackground = keyboardViewAttr.getDrawable(
                R.styleable.KeyboardView_functionalKeyBackground);
        mFunctionalKeyBackground = (functionalKeyBackground != null) ? functionalKeyBackground
                : mKeyBackground;
        final Drawable spacebarBackground = keyboardViewAttr.getDrawable(
                R.styleable.KeyboardView_spacebarBackground);
        mSpacebarBackground = (spacebarBackground != null) ? spacebarBackground : mKeyBackground;
        mSpacebarIconWidthRatio = keyboardViewAttr.getFloat(
                R.styleable.KeyboardView_spacebarIconWidthRatio, 1.0f);
        mKeyHintLetterPadding = keyboardViewAttr.getDimension(
                R.styleable.KeyboardView_keyHintLetterPadding, 0.0f);
        mKeyShiftedLetterHintPadding = keyboardViewAttr.getDimension(
                R.styleable.KeyboardView_keyShiftedLetterHintPadding, 0.0f);
        mKeyTextShadowRadius = keyboardViewAttr.getFloat(
                R.styleable.KeyboardView_keyTextShadowRadius, KET_TEXT_SHADOW_RADIUS_DISABLED);
        mVerticalCorrection = keyboardViewAttr.getDimension(
                R.styleable.KeyboardView_verticalCorrection, 0.0f);
        keyboardViewAttr.recycle();

        final TypedArray keyAttr = context.obtainStyledAttributes(attrs,
                R.styleable.Keyboard_Key, defStyle, R.style.KeyboardView);
        mDefaultKeyLabelFlags = keyAttr.getInt(R.styleable.Keyboard_Key_keyLabelFlags, 0);
        mKeyVisualCkAttributes = KeyVisualCkAttributes.newInstance(keyAttr);
        keyAttr.recycle();

        mPaint.setAntiAlias(true);
    }

    private static void blendAlpha(final Paint paint, final int alpha) {
        final int color = paint.getColor();
        paint.setARGB((paint.getAlpha() * alpha) / ConstantsCk.Color.ALPHA_OPAQUE,
                Color.red(color), Color.green(color), Color.blue(color));
    }


    public void setKeyboard(final KeyboardCk keyboardCk) {
        mKeyboardCk = keyboardCk;
        final int keyHeight = keyboardCk.mMostCommonKeyHeight;
        mKeyDrawCkParams.updateParams(keyHeight, mKeyVisualCkAttributes);
        mKeyDrawCkParams.updateParams(keyHeight, keyboardCk.mKeyVisualCkAttributes);
        final SharedPreferences prefs = PreferenceCkManagerCompat.getDeviceSharedPreferences(getContext());
        mCustomColor = SettingsCk.readKeyboardColor(prefs, getContext());
        invalidateAllKeys();
        requestLayout();
    }


    public KeyboardCk getKeyboard() {
        return mKeyboardCk;
    }

    protected float getVerticalCorrection() {
        return mVerticalCorrection;
    }

    protected KeyDrawCkParams getKeyDrawParams() {
        return mKeyDrawCkParams;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final KeyboardCk keyboardCk = getKeyboard();
        if (keyboardCk == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        // The main keyboard expands to the entire this {@link KeyboardView}.
        final int width = keyboardCk.mOccupiedWidth + getPaddingLeft() + getPaddingRight();
        final int height = keyboardCk.mOccupiedHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (canvas.isHardwareAccelerated()) {
            onDrawKeyboard(canvas);
            return;
        }

        final boolean bufferNeedsUpdates = mInvalidateAllKeys || !mInvalidatedKeyCks.isEmpty();
        if (bufferNeedsUpdates || mOffscreenBuffer == null) {
            if (maybeAllocateOffscreenBuffer()) {
                mInvalidateAllKeys = true;
                 mOffscreenCanvas.setBitmap(mOffscreenBuffer);
            }
            onDrawKeyboard(mOffscreenCanvas);
        }
        canvas.drawBitmap(mOffscreenBuffer, 0.0f, 0.0f, null);
    }

    private boolean maybeAllocateOffscreenBuffer() {
        final int width = getWidth();
        final int height = getHeight();
        if (width == 0 || height == 0) {
            return false;
        }
        if (mOffscreenBuffer != null && mOffscreenBuffer.getWidth() == width
                && mOffscreenBuffer.getHeight() == height) {
            return false;
        }
        freeOffscreenBuffer();
        mOffscreenBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        return true;
    }

    private void freeOffscreenBuffer() {
        mOffscreenCanvas.setBitmap(null);
        mOffscreenCanvas.setMatrix(null);
        if (mOffscreenBuffer != null) {
            mOffscreenBuffer.recycle();
            mOffscreenBuffer = null;
        }
    }

    private void onDrawKeyboard(final Canvas canvas) {
        final KeyboardCk keyboardCk = getKeyboard();
        if (keyboardCk == null) {
            return;
        }

        final Paint paint = mPaint;
        final Drawable background = getBackground();
        if (Color.alpha(mCustomColor) > 0 && keyboardCk.getKey(ConstantsCk.CODE_SPACE) != null) {
            setBackgroundColor(mCustomColor);
        }

        final boolean drawAllKeys = mInvalidateAllKeys || mInvalidatedKeyCks.isEmpty();
        final boolean isHardwareAccelerated = canvas.isHardwareAccelerated();
         if (drawAllKeys || isHardwareAccelerated) {
            if (!isHardwareAccelerated && background != null) {
                 canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
                background.draw(canvas);
            }
             for (final KeyCk keyCk : keyboardCk.getSortedKeys()) {
                onDrawKey(keyCk, canvas, paint);
            }
        } else {
            for (final KeyCk keyCk : mInvalidatedKeyCks) {
                if (!keyboardCk.hasKey(keyCk)) {
                    continue;
                }
                if (background != null) {

                    final int x = keyCk.getX() + getPaddingLeft();
                    final int y = keyCk.getY() + getPaddingTop();
                    mClipRect.set(x, y, x + keyCk.getWidth(), y + keyCk.getHeight());
                    canvas.save();
                    canvas.clipRect(mClipRect);
                    canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
                    background.draw(canvas);
                    canvas.restore();
                }
                onDrawKey(keyCk, canvas, paint);
            }
        }

        mInvalidatedKeyCks.clear();
        mInvalidateAllKeys = false;
    }

    private void onDrawKey(final KeyCk keyCk, final Canvas canvas,
                           final Paint paint) {
        final int keyDrawX = keyCk.getX() + getPaddingLeft();
        final int keyDrawY = keyCk.getY() + getPaddingTop();
        canvas.translate(keyDrawX, keyDrawY);

        final KeyVisualCkAttributes attr = keyCk.getVisualAttributes();
        final KeyDrawCkParams params = mKeyDrawCkParams.mayCloneAndUpdateParams(keyCk.getHeight(), attr);
        params.mAnimAlpha = ConstantsCk.Color.ALPHA_OPAQUE;

        if (!keyCk.isSpacer()) {
            final Drawable background = keyCk.selectBackgroundDrawable(
                    mKeyBackground, mFunctionalKeyBackground, mSpacebarBackground);
            if (background != null) {
                onDrawKeyBackground(keyCk, canvas, background);
            }
        }
        onDrawKeyTopVisuals(keyCk, canvas, paint, params);

        canvas.translate(-keyDrawX, -keyDrawY);
    }


    protected void onDrawKeyBackground(final KeyCk keyCk, final Canvas canvas,
                                       final Drawable background) {
        final int keyWidth = keyCk.getWidth();
        final int keyHeight = keyCk.getHeight();
        final Rect padding = mKeyBackgroundPadding;
        final int bgWidth = keyWidth + padding.left + padding.right;
        final int bgHeight = keyHeight + padding.top + padding.bottom;
        final int bgX = -padding.left;
        final int bgY = -padding.top;
        final Rect bounds = background.getBounds();
        if (bgWidth != bounds.right || bgHeight != bounds.bottom) {
            background.setBounds(0, 0, bgWidth, bgHeight);
        }
        canvas.translate(bgX, bgY);
        background.draw(canvas);
        canvas.translate(-bgX, -bgY);
    }


    public void onDrawKeyTopVisuals(KeyCk keyCk, Canvas canvas, Paint paint, KeyDrawCkParams keyDrawCkParams) {
        Drawable drawable;
        int i;
        float f;
        String str;
        int i2;
        int i3;
        int i4;
        float f2;
        int drawWidth = keyCk.getWidth();
        int height = keyCk.getHeight();
        float f3 = (float) drawWidth;
        float f4 = f3 * 0.5f;
        float f5 = ((float) height) * 0.5f;
        KeyboardCk keyboardCk = getKeyboard();
        if (keyboardCk == null) {
            drawable = null;
        } else {
            drawable = keyCk.getIcon(keyboardCk.mIconsSet, keyDrawCkParams.mAnimAlpha);
        }
        String label = keyCk.getLabel();
        if (label != null) {
            paint.setTypeface(keyCk.selectTypeface(keyDrawCkParams));
            paint.setTextSize((float) keyCk.selectTextSize(keyDrawCkParams));
            float referenceCharHeight = TypefaceCkUtils.getReferenceCharHeight(paint);
            float referenceCharWidth = TypefaceCkUtils.getReferenceCharWidth(paint);
            f = f5 + (referenceCharHeight / 2.0f);
            if (keyCk.isAlignLabelOffCenter()) {
                f4 += keyDrawCkParams.mLabelOffCenterRatio * referenceCharWidth;
                paint.setTextAlign(Paint.Align.LEFT);
            } else {
                paint.setTextAlign(Paint.Align.CENTER);
            }
            if (keyCk.needsAutoXScale()) {
                float min = Math.min(1.0f, (MAX_LABEL_RATIO * f3) / TypefaceCkUtils.getStringWidth(label, paint));
                if (keyCk.needsAutoScale()) {
                    paint.setTextSize(paint.getTextSize() * min);
                } else {
                    paint.setTextScaleX(min);
                }
            }
            if (keyCk.isEnabled()) {
                paint.setColor(keyCk.selectTextColor(keyDrawCkParams));
                float f6 = this.mKeyTextShadowRadius;
                if (f6 > 0.0f) {
                    paint.setShadowLayer(f6, 0.0f, 0.0f, keyDrawCkParams.mTextShadowColor);
                } else {
                    paint.clearShadowLayer();
                }
            } else {
                paint.setColor(0);
                paint.clearShadowLayer();
            }
             PrefCk.getSharedPreferences(getContext());

            blendAlpha(paint, keyDrawCkParams.mAnimAlpha);
             if (label.length() > 1) {
                i = height;
                str = label;
                canvas.drawText(label, 0, label.length(), f4, f, paint);
            } else {
                i = height;
                str = label;
                canvas.drawText(ConstantsCk.isNumericKeyboard ? str : changeKeyboardFonts(str), 0, ConstantsCk.isNumericKeyboard ? str.length() : changeKeyboardFonts(str).length(), f4, f, paint);
            }
            paint.clearShadowLayer();
            paint.setTextScaleX(1.0f);
            f4 = f4;
        } else {
            i = height;
            str = label;
            f = f5;
        }
        String hintLabel = keyCk.getHintLabel();
        if (hintLabel != null) {
            paint.setTextSize((float) keyCk.selectHintTextSize(keyDrawCkParams));
            paint.setColor(keyCk.selectHintTextColor(keyDrawCkParams));
            paint.setTypeface(Typeface.DEFAULT);
            blendAlpha(paint, keyDrawCkParams.mAnimAlpha);
            float referenceCharHeight2 = TypefaceCkUtils.getReferenceCharHeight(paint);
            float referenceCharWidth2 = TypefaceCkUtils.getReferenceCharWidth(paint);
            if (keyCk.hasHintLabel()) {
                float f7 = f4 + (keyDrawCkParams.mHintLabelOffCenterRatio * referenceCharWidth2);
                if (!keyCk.isAlignHintLabelToBottom(this.mDefaultKeyLabelFlags)) {
                    f = f5 + (referenceCharHeight2 / 2.0f);
                }
                paint.setTextAlign(Paint.Align.LEFT);
                f2 = f7;
            } else if (keyCk.hasShiftedLetterHint()) {
                float f8 = (f3 - this.mKeyShiftedLetterHintPadding) - (referenceCharWidth2 / 2.0f);
                paint.getFontMetrics(this.mFontMetrics);
                paint.setTextAlign(Paint.Align.CENTER);
                f2 = f8;
                f = -this.mFontMetrics.top;
            } else {
                f2 = (f3 - this.mKeyHintLetterPadding) - (Math.max(TypefaceCkUtils.getReferenceDigitWidth(paint), TypefaceCkUtils.getStringWidth(hintLabel, paint)) / 2.0f);
                paint.setTextAlign(Paint.Align.CENTER);
                f = -paint.ascent();
            }
            float f9 = keyDrawCkParams.mHintLabelVerticalAdjustment * referenceCharHeight2;
            String changeKeyboardFonts = (!ConstantsCk.isNumericKeyboard && hintLabel.length() < 2) ? changeKeyboardFonts(hintLabel) : hintLabel;
            if (!ConstantsCk.isNumericKeyboard && hintLabel.length() < 2) {
                hintLabel = changeKeyboardFonts(hintLabel);
            }
            i2 = 2;
            canvas.drawText(changeKeyboardFonts, 0, hintLabel.length(), f2, f + f9, paint);
        } else {
            i2 = 2;
        }
        if (str == null && drawable != null) {
            if (keyCk.getCode() != 32 || !(drawable instanceof NinePatchDrawable)) {
                i3 = Math.min(drawable.getIntrinsicWidth(), drawWidth);
            } else {
                i3 = (int) (f3 * this.mSpacebarIconWidthRatio);
            }
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (keyCk.isAlignIconToBottom()) {
                i4 = i - intrinsicHeight;
            } else {
                i4 = (i - intrinsicHeight) / i2;
            }
            drawIcon(canvas, drawable, (drawWidth - i3) / 2, i4, i3, intrinsicHeight);
        }

    }

    protected static void drawIcon(final Canvas canvas, final Drawable icon,
            final int x, final int y, final int width, final int height) {
        canvas.translate(x, y);
        icon.setBounds(0, 0, width, height);
        icon.draw(canvas);
        canvas.translate(-x, -y);
    }

    public Paint newLabelPaint(final KeyCk keyCk) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (keyCk == null) {
            paint.setTypeface(mKeyDrawCkParams.mTypeface);
            paint.setTextSize(mKeyDrawCkParams.mLabelSize);
        } else {
            paint.setColor(keyCk.selectTextColor(mKeyDrawCkParams));
            paint.setTypeface(keyCk.selectTypeface(mKeyDrawCkParams));
            paint.setTextSize(keyCk.selectTextSize(mKeyDrawCkParams));
        }
        return paint;
    }
    private String changeKeyboardFonts(String str) {
        PrefCk.getSharedPreferences(DKeboaApplication.getInstance().getApplicationContext());
        StylishFontCkModel stylishFontCkModel = (StylishFontCkModel) StorageCkUtils.serializeObject(PrefCk.getSelectedFontObject(), StylishFontCkModel.class);
        char charAt = str.charAt(0);
        if (stylishFontCkModel != null) {
            try {
                str = stylishFontCkModel.getStyledCharacter(charAt);
            } catch (Exception unused) {
            }
        }
         return str;
    }


    public void invalidateAllKeys() {
        mInvalidatedKeyCks.clear();
        mInvalidateAllKeys = true;
        invalidate();
    }


    public void invalidateKey(final KeyCk keyCk) {
        if (mInvalidateAllKeys || keyCk == null) {
            return;
        }
        mInvalidatedKeyCks.add(keyCk);
        final int x = keyCk.getX() + getPaddingLeft();
        final int y = keyCk.getY() + getPaddingTop();
        invalidate(x, y, x + keyCk.getWidth(), y + keyCk.getHeight());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        freeOffscreenBuffer();
    }

    public void deallocateMemory() {
        freeOffscreenBuffer();
    }
}
