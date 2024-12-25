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

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.WeakHashMap;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.internal.DrawingPreviewCkPlacerView;
import com.test.testing12345.keyboard.internal.DrawingProxy;
import com.test.testing12345.keyboard.internal.KeyDrawCkParams;
import com.test.testing12345.keyboard.internal.KeyPreviewCkChoreographer;
import com.test.testing12345.keyboard.internal.KeyPreviewCkDrawParams;
import com.test.testing12345.keyboard.internal.KeyPreviewCkView;
import com.test.testing12345.keyboard.internal.MoreKeyCkSpec;
import com.test.testing12345.keyboard.internal.NonDistinctMultitouchCkHelper;
import com.test.testing12345.keyboard.internal.TimerCkHandlerCk;
import com.test.testing12345.custom.SubtypeCk;
import com.test.testing12345.custom.RichInputMethodCkManager;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.CoordinateCkUtils;
import com.test.testing12345.custom.utils.LanguageOnSpacebarCkUtils;
import com.test.testing12345.custom.utils.LocaleResourceCkUtils;
import com.test.testing12345.custom.utils.TypefaceCkUtils;


public final class MainKeyboardCkView extends KeyboardCkView implements MoreKeysCkPanel.Controller, DrawingProxy {
    private static final String TAG = MainKeyboardCkView.class.getSimpleName();

     private KeyboardActionListener mKeyboardActionListener;

     private KeyCk mSpaceKeyCk;
     private final int mLanguageOnSpacebarFinalAlpha;
    private ObjectAnimator mLanguageOnSpacebarFadeoutAnimator;
    private int mLanguageOnSpacebarFormatType;
    private int mLanguageOnSpacebarAnimAlpha = ConstantsCk.Color.ALPHA_OPAQUE;
    private final float mLanguageOnSpacebarTextRatio;
    private float mLanguageOnSpacebarTextSize;
    private final int mLanguageOnSpacebarTextColor;
     private static final float MINIMUM_XSCALE_OF_LANGUAGE_NAME = 0.8f;

     private final ObjectAnimator mAltCodeKeyWhileTypingFadeoutAnimator;
    private final ObjectAnimator mAltCodeKeyWhileTypingFadeinAnimator;
    private int mAltCodeKeyWhileTypingAnimAlpha = ConstantsCk.Color.ALPHA_OPAQUE;


    private final DrawingPreviewCkPlacerView mDrawingPreviewCkPlacerView;
    private final int[] mOriginCoords = CoordinateCkUtils.newInstance();


    private final KeyPreviewCkDrawParams mKeyPreviewCkDrawParams;
    private final KeyPreviewCkChoreographer mKeyPreviewCkChoreographer;

     private final Paint mBackgroundDimAlphaPaint = new Paint();
    private final View mMoreKeysKeyboardContainer;
    private final WeakHashMap<KeyCk, KeyboardCk> mMoreKeysKeyboardCache = new WeakHashMap<>();
    private final boolean mConfigShowMoreKeysKeyboardAtTouchedPoint;

    private MoreKeysCkPanel mMoreKeysCkPanel;

    private final KeyDetectorCk mKeyDetectorCk;
    private final NonDistinctMultitouchCkHelper mNonDistinctMultitouchCkHelper;

    private final TimerCkHandlerCk mTimerHandler;
    private final int mLanguageOnSpacebarHorizontalMargin;

    public MainKeyboardCkView(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.mainKeyboardViewStyle);
    }

    public MainKeyboardCkView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        final DrawingPreviewCkPlacerView drawingPreviewCkPlacerView =
                new DrawingPreviewCkPlacerView(context, attrs);

        final TypedArray mainKeyboardViewAttr = context.obtainStyledAttributes(
                attrs, R.styleable.MainKeyboardView, defStyle, R.style.MainKeyboardView);
        final int ignoreAltCodeKeyTimeout = mainKeyboardViewAttr.getInt(
                R.styleable.MainKeyboardView_ignoreAltCodeKeyTimeout, 0);
        mTimerHandler = new TimerCkHandlerCk(this, ignoreAltCodeKeyTimeout);

        final float keyHysteresisDistance = mainKeyboardViewAttr.getDimension(
                R.styleable.MainKeyboardView_keyHysteresisDistance, 0.0f);
        final float keyHysteresisDistanceForSlidingModifier = mainKeyboardViewAttr.getDimension(
                R.styleable.MainKeyboardView_keyHysteresisDistanceForSlidingModifier, 0.0f);
        mKeyDetectorCk = new KeyDetectorCk(
                keyHysteresisDistance, keyHysteresisDistanceForSlidingModifier);

        PointerCkTracker.init(mainKeyboardViewAttr, mTimerHandler, this /* DrawingProxy */);

        final boolean hasDistinctMultitouch = context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
        mNonDistinctMultitouchCkHelper = hasDistinctMultitouch ? null
                : new NonDistinctMultitouchCkHelper();

        final int backgroundDimAlpha = mainKeyboardViewAttr.getInt(
                R.styleable.MainKeyboardView_backgroundDimAlpha, 0);
        mBackgroundDimAlphaPaint.setColor(Color.BLACK);
        mBackgroundDimAlphaPaint.setAlpha(backgroundDimAlpha);
        mLanguageOnSpacebarTextRatio = mainKeyboardViewAttr.getFraction(
                R.styleable.MainKeyboardView_languageOnSpacebarTextRatio, 1, 1, 1.0f);
        mLanguageOnSpacebarTextColor = mainKeyboardViewAttr.getColor(
                R.styleable.MainKeyboardView_languageOnSpacebarTextColor, 0);
        mLanguageOnSpacebarFinalAlpha = mainKeyboardViewAttr.getInt(
                R.styleable.MainKeyboardView_languageOnSpacebarFinalAlpha,
                ConstantsCk.Color.ALPHA_OPAQUE);
        final int languageOnSpacebarFadeoutAnimatorResId = mainKeyboardViewAttr.getResourceId(
                R.styleable.MainKeyboardView_languageOnSpacebarFadeoutAnimator, 0);
        final int altCodeKeyWhileTypingFadeoutAnimatorResId = mainKeyboardViewAttr.getResourceId(
                R.styleable.MainKeyboardView_altCodeKeyWhileTypingFadeoutAnimator, 0);
        final int altCodeKeyWhileTypingFadeinAnimatorResId = mainKeyboardViewAttr.getResourceId(
                R.styleable.MainKeyboardView_altCodeKeyWhileTypingFadeinAnimator, 0);

        mKeyPreviewCkDrawParams = new KeyPreviewCkDrawParams(mainKeyboardViewAttr);
        mKeyPreviewCkChoreographer = new KeyPreviewCkChoreographer(mKeyPreviewCkDrawParams);

        final int moreKeysKeyboardLayoutId = mainKeyboardViewAttr.getResourceId(
                R.styleable.MainKeyboardView_moreKeysKeyboardLayout, 0);
        mConfigShowMoreKeysKeyboardAtTouchedPoint = mainKeyboardViewAttr.getBoolean(
                R.styleable.MainKeyboardView_showMoreKeysKeyboardAtTouchedPoint, false);

        mainKeyboardViewAttr.recycle();

        mDrawingPreviewCkPlacerView = drawingPreviewCkPlacerView;

        final LayoutInflater inflater = LayoutInflater.from(getContext());
        mMoreKeysKeyboardContainer = inflater.inflate(moreKeysKeyboardLayoutId, null);
        mLanguageOnSpacebarFadeoutAnimator = loadObjectAnimator(
                languageOnSpacebarFadeoutAnimatorResId, this);
        mAltCodeKeyWhileTypingFadeoutAnimator = loadObjectAnimator(
                altCodeKeyWhileTypingFadeoutAnimatorResId, this);
        mAltCodeKeyWhileTypingFadeinAnimator = loadObjectAnimator(
                altCodeKeyWhileTypingFadeinAnimatorResId, this);

        mKeyboardActionListener = KeyboardActionListener.EMPTY_LISTENER;

        mLanguageOnSpacebarHorizontalMargin = (int)getResources().getDimension(
                R.dimen.config_language_on_spacebar_horizontal_margin);
    }

    private ObjectAnimator loadObjectAnimator(final int resId, final Object target) {
        if (resId == 0) {
            // TODO: Stop returning null.
            return null;
        }
        final ObjectAnimator animator = (ObjectAnimator)AnimatorInflater.loadAnimator(
                getContext(), resId);
        if (animator != null) {
            animator.setTarget(target);
        }
        return animator;
    }

    private static void cancelAndStartAnimators(final ObjectAnimator animatorToCancel,
            final ObjectAnimator animatorToStart) {
        if (animatorToCancel == null || animatorToStart == null) {
            // TODO: Stop using null as a no-operation animator.
            return;
        }
        float startFraction = 0.0f;
        if (animatorToCancel.isStarted()) {
            animatorToCancel.cancel();
            startFraction = 1.0f - animatorToCancel.getAnimatedFraction();
        }
        final long startTime = (long)(animatorToStart.getDuration() * startFraction);
        animatorToStart.start();
        animatorToStart.setCurrentPlayTime(startTime);
    }


    @Override
    public void startWhileTypingAnimation(final int fadeInOrOut) {
        switch (fadeInOrOut) {
        case DrawingProxy.FADE_IN:
            cancelAndStartAnimators(
                    mAltCodeKeyWhileTypingFadeoutAnimator, mAltCodeKeyWhileTypingFadeinAnimator);
            break;
        case DrawingProxy.FADE_OUT:
            cancelAndStartAnimators(
                    mAltCodeKeyWhileTypingFadeinAnimator, mAltCodeKeyWhileTypingFadeoutAnimator);
            break;
        }
    }

    public void setLanguageOnSpacebarAnimAlpha(final int alpha) {
        mLanguageOnSpacebarAnimAlpha = alpha;
        invalidateKey(mSpaceKeyCk);
    }

    public void setKeyboardActionListener(final KeyboardActionListener listener) {
        mKeyboardActionListener = listener;
        PointerCkTracker.setKeyboardActionListener(listener);
    }
     public int getKeyX(final int x) {
        return ConstantsCk.isValidCoordinate(x) ? mKeyDetectorCk.getTouchX(x) : x;
    }



    public int getKeyY(final int y) {
        return ConstantsCk.isValidCoordinate(y) ? mKeyDetectorCk.getTouchY(y) : y;
    }

     @Override
    public void setKeyboard(final KeyboardCk keyboardCk) {
         mTimerHandler.cancelLongPressTimers();
        super.setKeyboard(keyboardCk);
        mKeyDetectorCk.setKeyboard(
                keyboardCk, -getPaddingLeft(), -getPaddingTop() + getVerticalCorrection());
        PointerCkTracker.setKeyDetector(mKeyDetectorCk);
        mMoreKeysKeyboardCache.clear();

        mSpaceKeyCk = keyboardCk.getKey(ConstantsCk.CODE_SPACE);
        final int keyHeight = keyboardCk.mMostCommonKeyHeight;
        mLanguageOnSpacebarTextSize = keyHeight * mLanguageOnSpacebarTextRatio;
    }

     public void setKeyPreviewPopupEnabled(final boolean previewEnabled, final int delay) {
        mKeyPreviewCkDrawParams.setPopupEnabled(previewEnabled, delay);
    }

    private void locatePreviewPlacerView() {
        getLocationInWindow(mOriginCoords);
        mDrawingPreviewCkPlacerView.setKeyboardViewGeometry(mOriginCoords);
    }

    private void installPreviewPlacerView() {
        final View rootView = getRootView();
        if (rootView == null) {
            Log.w(TAG, "Cannot find root view");
            return;
        }
        final ViewGroup windowContentView = (ViewGroup)rootView.findViewById(android.R.id.content);

        if (windowContentView == null) {
            Log.w(TAG, "Cannot find android.R.id.content view to add DrawingPreviewPlacerView");
            return;
        }
        windowContentView.addView(mDrawingPreviewCkPlacerView);
    }


    @Override
    public void onKeyPressed(final KeyCk keyCk, final boolean withPreview) {
        keyCk.onPressed();
        invalidateKey(keyCk);
        if (withPreview && !keyCk.noKeyPreview()) {
            showKeyPreview(keyCk);
        }
    }

    private void showKeyPreview(final KeyCk keyCk) {
        final KeyboardCk keyboardCk = getKeyboard();
        if (keyboardCk == null) {
            return;
        }
        final KeyPreviewCkDrawParams previewParams = mKeyPreviewCkDrawParams;
        if (!previewParams.isPopupEnabled()) {
            previewParams.setVisibleOffset(-Math.round(keyboardCk.mVerticalGap));
            return;
        }

        locatePreviewPlacerView();
        getLocationInWindow(mOriginCoords);
        mKeyPreviewCkChoreographer.placeAndShowKeyPreview(keyCk, keyboardCk.mIconsSet, getKeyDrawParams(),
                mOriginCoords, mDrawingPreviewCkPlacerView, isHardwareAccelerated());
    }

    private void dismissKeyPreviewWithoutDelay(final KeyCk keyCk) {
        mKeyPreviewCkChoreographer.dismissKeyPreview(keyCk, false /* withAnimation */);
        invalidateKey(keyCk);
    }


    @Override
    public void onKeyReleased(final KeyCk keyCk, final boolean withAnimation) {
        keyCk.onReleased();
        invalidateKey(keyCk);
        if (!keyCk.noKeyPreview()) {
            if (withAnimation) {
                dismissKeyPreview(keyCk);
            } else {
                dismissKeyPreviewWithoutDelay(keyCk);
            }
        }
    }

    private void dismissKeyPreview(final KeyCk keyCk) {
        if (isHardwareAccelerated()) {
            mKeyPreviewCkChoreographer.dismissKeyPreview(keyCk, true /* withAnimation */);
            return;
        }

        mTimerHandler.postDismissKeyPreview(keyCk, mKeyPreviewCkDrawParams.getLingerTimeout());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        installPreviewPlacerView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDrawingPreviewCkPlacerView.removeAllViews();
    }


    public MoreKeysCkPanel showMoreKeysKeyboard(final KeyCk keyCk,
                                                final PointerCkTracker tracker) {
        final MoreKeyCkSpec[] moreKeys = keyCk.getMoreKeys();
        if (moreKeys == null) {
            return null;
        }
        KeyboardCk moreKeysKeyboardCk = mMoreKeysKeyboardCache.get(keyCk);
        if (moreKeysKeyboardCk == null) {

            final boolean isSingleMoreKeyWithPreview = mKeyPreviewCkDrawParams.isPopupEnabled()
                    && !keyCk.noKeyPreview() && moreKeys.length == 1
                    && mKeyPreviewCkDrawParams.getVisibleWidth() > 0;
            final MoreKeysKeyboardCk.CkBuilder builder = new MoreKeysKeyboardCk.CkBuilder(
                    getContext(), keyCk, getKeyboard(), isSingleMoreKeyWithPreview,
                    mKeyPreviewCkDrawParams.getVisibleWidth(),
                    mKeyPreviewCkDrawParams.getVisibleHeight(), newLabelPaint(keyCk));
            moreKeysKeyboardCk = builder.build();
            mMoreKeysKeyboardCache.put(keyCk, moreKeysKeyboardCk);
        }

        final MoreKeysCkKeyboardCkView moreKeysKeyboardView =
                mMoreKeysKeyboardContainer.findViewById(R.id.more_keys_keyboard_view);
        moreKeysKeyboardView.setKeyboard(moreKeysKeyboardCk);
        mMoreKeysKeyboardContainer.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final int[] lastCoords = CoordinateCkUtils.newInstance();
        tracker.getLastCoordinates(lastCoords);
        final boolean keyPreviewEnabled = mKeyPreviewCkDrawParams.isPopupEnabled()
                && !keyCk.noKeyPreview();

        final int pointX = (mConfigShowMoreKeysKeyboardAtTouchedPoint && !keyPreviewEnabled)
                ? CoordinateCkUtils.x(lastCoords)
                : keyCk.getX() + keyCk.getWidth() / 2;

        final int pointY = keyCk.getY() + mKeyPreviewCkDrawParams.getVisibleOffset()
                + Math.round(moreKeysKeyboardCk.mBottomPadding);
        moreKeysKeyboardView.showMoreKeysPanel(this, this, pointX, pointY, mKeyboardActionListener);
        return moreKeysKeyboardView;
    }

    public boolean isInDraggingFinger() {
        if (isShowingMoreKeysPanel()) {
            return true;
        }
        return PointerCkTracker.isAnyInDraggingFinger();
    }

    @Override
    public void onShowMoreKeysPanel(final MoreKeysCkPanel panel) {
        locatePreviewPlacerView();

        onDismissMoreKeysPanel();

        PointerCkTracker.setReleasedKeyGraphicsToAllKeys();

        panel.showInParent(mDrawingPreviewCkPlacerView);
        mMoreKeysCkPanel = panel;
    }

    public boolean isShowingMoreKeysPanel() {
        return mMoreKeysCkPanel != null && mMoreKeysCkPanel.isShowingInParent();
    }

    @Override
    public void onCancelMoreKeysPanel() {
        PointerCkTracker.dismissAllMoreKeysPanels();
    }

    @Override
    public void onDismissMoreKeysPanel() {
        if (isShowingMoreKeysPanel()) {
            mMoreKeysCkPanel.removeFromParent();
            mMoreKeysCkPanel = null;
        }
    }

    public void startDoubleTapShiftKeyTimer() {
        mTimerHandler.startDoubleTapShiftKeyTimer();
    }

    public void cancelDoubleTapShiftKeyTimer() {
        mTimerHandler.cancelDoubleTapShiftKeyTimer();
    }

    public boolean isInDoubleTapShiftKeyTimeout() {
        return mTimerHandler.isInDoubleTapShiftKeyTimeout();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (getKeyboard() == null) {
            return false;
        }
        if (mNonDistinctMultitouchCkHelper != null) {
            if (event.getPointerCount() > 1 && mTimerHandler.isInKeyRepeat()) {

                mTimerHandler.cancelKeyRepeatTimers();
            }
             mNonDistinctMultitouchCkHelper.processMotionEvent(event, mKeyDetectorCk);
            return true;
        }
        return processMotionEvent(event);
    }

    public boolean processMotionEvent(final MotionEvent event) {
        final int index = event.getActionIndex();
        final int id = event.getPointerId(index);
        final PointerCkTracker tracker = PointerCkTracker.getPointerTracker(id);

        if (isShowingMoreKeysPanel() && !tracker.isShowingMoreKeysPanel()
                && PointerCkTracker.getActivePointerTrackerCount() == 1) {
            return true;
        }
        tracker.processMotionEvent(event, mKeyDetectorCk);
        return true;
    }

    public void cancelAllOngoingEvents() {
        mTimerHandler.cancelAllMessages();
        PointerCkTracker.setReleasedKeyGraphicsToAllKeys();
        PointerCkTracker.dismissAllMoreKeysPanels();
        PointerCkTracker.cancelAllPointerTrackers();
    }

    public void closing() {
        cancelAllOngoingEvents();
        mMoreKeysKeyboardCache.clear();
    }

    public void onHideWindow() {
        onDismissMoreKeysPanel();
    }

    public void startDisplayLanguageOnSpacebar(final boolean subtypeChanged,
            final int languageOnSpacebarFormatType) {
        if (subtypeChanged) {
            KeyPreviewCkView.clearTextCache();
        }
        mLanguageOnSpacebarFormatType = languageOnSpacebarFormatType;
        final ObjectAnimator animator = mLanguageOnSpacebarFadeoutAnimator;
        if (animator == null) {
            mLanguageOnSpacebarFormatType = LanguageOnSpacebarCkUtils.FORMAT_TYPE_NONE;
        } else {
            if (subtypeChanged
                    && languageOnSpacebarFormatType != LanguageOnSpacebarCkUtils.FORMAT_TYPE_NONE) {
                setLanguageOnSpacebarAnimAlpha(ConstantsCk.Color.ALPHA_OPAQUE);
                if (animator.isStarted()) {
                    animator.cancel();
                }
                animator.start();
            } else {
                if (!animator.isStarted()) {
                    mLanguageOnSpacebarAnimAlpha = mLanguageOnSpacebarFinalAlpha;
                }
            }
        }
        invalidateKey(mSpaceKeyCk);
    }

    @Override
    public void onDrawKeyTopVisuals(final KeyCk keyCk, final Canvas canvas, final Paint paint,
                                    final KeyDrawCkParams params) {
        if (keyCk.altCodeWhileTyping()) {
            params.mAnimAlpha = mAltCodeKeyWhileTypingAnimAlpha;
        }
        super.onDrawKeyTopVisuals(keyCk, canvas, paint, params);
        final int code = keyCk.getCode();
        if (code == ConstantsCk.CODE_SPACE) {
             final RichInputMethodCkManager imm = RichInputMethodCkManager.getInstance();
            if (imm.hasMultipleEnabledSubtypes()) {
                drawLanguageOnSpacebar(keyCk, canvas, paint);
            }
        }
    }

    private boolean fitsTextIntoWidth(final int width, final String text, final Paint paint) {
        final int maxTextWidth = width - mLanguageOnSpacebarHorizontalMargin * 2;
        paint.setTextScaleX(1.0f);
        final float textWidth = TypefaceCkUtils.getStringWidth(text, paint);
        if (textWidth < width) {
            return true;
        }

        final float scaleX = maxTextWidth / textWidth;
        if (scaleX < MINIMUM_XSCALE_OF_LANGUAGE_NAME) {
            return false;
        }

        paint.setTextScaleX(scaleX);
        return TypefaceCkUtils.getStringWidth(text, paint) < maxTextWidth;
    }

     private String layoutLanguageOnSpacebar(final Paint paint,
                                             final SubtypeCk subtypeCk, final int width) {
         if (mLanguageOnSpacebarFormatType == LanguageOnSpacebarCkUtils.FORMAT_TYPE_FULL_LOCALE) {
            final String fullText =
                    LocaleResourceCkUtils.getLocaleDisplayNameInLocale(subtypeCk.getLocale());
            if (fitsTextIntoWidth(width, fullText, paint)) {
                return fullText;
            }
        }

        final String middleText =
                LocaleResourceCkUtils.getLanguageDisplayNameInLocale(subtypeCk.getLocale());
        if (fitsTextIntoWidth(width, middleText, paint)) {
            return middleText;
        }

        return "";
    }

    private void drawLanguageOnSpacebar(final KeyCk keyCk, final Canvas canvas, final Paint paint) {
        final KeyboardCk keyboardCk = getKeyboard();
        if (keyboardCk == null) {
            return;
        }
        final int width = keyCk.getWidth();
        final int height = keyCk.getHeight();
        paint.setTextAlign(Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(mLanguageOnSpacebarTextSize);
        final String language = layoutLanguageOnSpacebar(paint, keyboardCk.mId.mSubtypeCk, width);
         final float descent = paint.descent();
        final float textHeight = -paint.ascent() + descent;
        final float baseline = height / 2 + textHeight / 2;
        paint.setColor(mLanguageOnSpacebarTextColor);
        paint.setAlpha(mLanguageOnSpacebarAnimAlpha);
        canvas.drawText(language, width / 2, baseline - descent, paint);
        paint.clearShadowLayer();
        paint.setTextScaleX(1.0f);
    }
}
