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

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.internal.BogusMoveEventCkDetector;
import com.test.testing12345.keyboard.internal.DrawingProxy;
import com.test.testing12345.keyboard.internal.PointerTrackerCkQueue;
import com.test.testing12345.keyboard.internal.TimerCkProxy;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.CoordinateCkUtils;
import com.test.testing12345.custom.define.DebugCkFlags;
import com.test.testing12345.custom.settings.SettingsCk;

public final class PointerCkTracker implements PointerTrackerCkQueue.Element {
    private static final String TAG = PointerCkTracker.class.getSimpleName();
    private static final boolean DEBUG_EVENT = false;
    private static final boolean DEBUG_MOVE_EVENT = false;
    private static final boolean DEBUG_LISTENER = false;
    private static boolean DEBUG_MODE = DebugCkFlags.DEBUG_ENABLED || DEBUG_EVENT;

    static final class PointerTrackerParams {
        public final boolean mKeySelectionByDraggingFinger;
        public final int mTouchNoiseThresholdTime;
        public final int mTouchNoiseThresholdDistance;
        public final int mKeyRepeatStartTimeout;
        public final int mKeyRepeatInterval;
        public final int mLongPressShiftLockTimeout;

        public PointerTrackerParams(final TypedArray mainKeyboardViewAttr) {
            mKeySelectionByDraggingFinger = mainKeyboardViewAttr.getBoolean(
                    R.styleable.MainKeyboardView_keySelectionByDraggingFinger, false);
            mTouchNoiseThresholdTime = mainKeyboardViewAttr.getInt(
                    R.styleable.MainKeyboardView_touchNoiseThresholdTime, 0);
            mTouchNoiseThresholdDistance = mainKeyboardViewAttr.getDimensionPixelSize(
                    R.styleable.MainKeyboardView_touchNoiseThresholdDistance, 0);
            mKeyRepeatStartTimeout = mainKeyboardViewAttr.getInt(
                    R.styleable.MainKeyboardView_keyRepeatStartTimeout, 0);
            mKeyRepeatInterval = mainKeyboardViewAttr.getInt(
                    R.styleable.MainKeyboardView_keyRepeatInterval, 0);
            mLongPressShiftLockTimeout = mainKeyboardViewAttr.getInt(
                    R.styleable.MainKeyboardView_longPressShiftLockTimeout, 0);
        }
    }

    // Parameters for pointer handling.
    private static PointerTrackerParams sParams;
    private static int sPointerStep = (int)(10.0 * Resources.getSystem().getDisplayMetrics().density);

    private static final ArrayList<PointerCkTracker> sTrackers = new ArrayList<>();
    private static final PointerTrackerCkQueue S_POINTER_TRACKER_CK_QUEUE = new PointerTrackerCkQueue();

    public final int mPointerId;

    private static DrawingProxy sDrawingProxy;
    private static TimerCkProxy sTimerCkProxy;
    private static KeyboardActionListener sListener = KeyboardActionListener.EMPTY_LISTENER;

    private KeyDetectorCk mKeyDetectorCk = new KeyDetectorCk();
    private KeyboardCk mKeyboardCk;
    private final BogusMoveEventCkDetector mBogusMoveEventCkDetector = new BogusMoveEventCkDetector();

     private int[] mDownCoordinates = CoordinateCkUtils.newInstance();

     private KeyCk mCurrentKeyCk = null;
     private int mKeyX;
    private int mKeyY;

     private int mLastX;
    private int mLastY;
    private int mStartX;

    private long mStartTime;
    private boolean mCursorMoved = false;

     private boolean mKeyboardLayoutHasBeenChanged;

     private boolean mIsTrackingForActionDisabled;

     private MoreKeysCkPanel mMoreKeysCkPanel;

    private static final int MULTIPLIER_FOR_LONG_PRESS_TIMEOUT_IN_SLIDING_INPUT = 3;
     boolean mIsInDraggingFinger;
     boolean mIsInSlidingKeyInput;
     private int mCurrentRepeatingKeyCode = ConstantsCk.NOT_A_CODE;

     private boolean mIsAllowedDraggingFinger;

    // TODO: Add PointerTrackerFactory singleton and move some class static methods into it.
    public static void init(final TypedArray mainKeyboardViewAttr, final TimerCkProxy timerCkProxy,
            final DrawingProxy drawingProxy) {
        sParams = new PointerTrackerParams(mainKeyboardViewAttr);

        final Resources res = mainKeyboardViewAttr.getResources();
        BogusMoveEventCkDetector.init(res);

        sTimerCkProxy = timerCkProxy;
        sDrawingProxy = drawingProxy;
    }

    public static PointerCkTracker getPointerTracker(final int id) {
        final ArrayList<PointerCkTracker> trackers = sTrackers;

         for (int i = trackers.size(); i <= id; i++) {
            final PointerCkTracker tracker = new PointerCkTracker(i);
            trackers.add(tracker);
        }

        return trackers.get(id);
    }

    public static boolean isAnyInDraggingFinger() {
        return S_POINTER_TRACKER_CK_QUEUE.isAnyInDraggingFinger();
    }

    public static void cancelAllPointerTrackers() {
        S_POINTER_TRACKER_CK_QUEUE.cancelAllPointerTrackers();
    }

    public static void setKeyboardActionListener(final KeyboardActionListener listener) {
        sListener = listener;
    }

    public static void setKeyDetector(final KeyDetectorCk keyDetectorCk) {
        final KeyboardCk keyboardCk = keyDetectorCk.getKeyboard();
        if (keyboardCk == null) {
            return;
        }
        final int trackersSize = sTrackers.size();
        for (int i = 0; i < trackersSize; ++i) {
            final PointerCkTracker tracker = sTrackers.get(i);
            tracker.setKeyDetectorInner(keyDetectorCk);
        }
    }

    public static void setReleasedKeyGraphicsToAllKeys() {
        final int trackersSize = sTrackers.size();
        for (int i = 0; i < trackersSize; ++i) {
            final PointerCkTracker tracker = sTrackers.get(i);
            tracker.setReleasedKeyGraphics(tracker.getKey(), true /* withAnimation */);
        }
    }

    public static void dismissAllMoreKeysPanels() {
        final int trackersSize = sTrackers.size();
        for (int i = 0; i < trackersSize; ++i) {
            final PointerCkTracker tracker = sTrackers.get(i);
            tracker.dismissMoreKeysPanel();
        }
    }

    private PointerCkTracker(final int id) {
        mPointerId = id;
    }

     private boolean callListenerOnPressAndCheckKeyboardLayoutChange(final KeyCk keyCk,
            final int repeatCount) {

        final boolean ignoreModifierKey = mIsInDraggingFinger && keyCk.isModifier();
         if (ignoreModifierKey) {
            return false;
        }
        sListener.onPressKey(keyCk.getCode(), repeatCount, getActivePointerTrackerCount() == 1);
        final boolean keyboardLayoutHasBeenChanged = mKeyboardLayoutHasBeenChanged;
        mKeyboardLayoutHasBeenChanged = false;
        sTimerCkProxy.startTypingStateTimer(keyCk);
        return keyboardLayoutHasBeenChanged;
    }

     private void callListenerOnCodeInput(final KeyCk keyCk, final int primaryCode, final int x,
                                          final int y, final boolean isKeyRepeat) {
        final boolean ignoreModifierKey = mIsInDraggingFinger && keyCk.isModifier();
        final boolean altersCode = keyCk.altCodeWhileTyping() && sTimerCkProxy.isTypingState();
        final int code = altersCode ? keyCk.getAltCode() : primaryCode;
        if (DEBUG_LISTENER) {
            final String output = code == ConstantsCk.CODE_OUTPUT_TEXT
                    ? keyCk.getOutputText() : ConstantsCk.printableCode(code);
         }
        if (ignoreModifierKey) {
            return;
        }

        if (code == ConstantsCk.CODE_OUTPUT_TEXT) {
            sListener.onTextInput(keyCk.getOutputText());
        } else if (code != ConstantsCk.CODE_UNSPECIFIED) {
            sListener.onCodeInput(code,
                ConstantsCk.NOT_A_COORDINATE, ConstantsCk.NOT_A_COORDINATE, isKeyRepeat);
        }
    }



    private void callListenerOnRelease(final KeyCk keyCk, final int primaryCode,
                                       final boolean withSliding) {
         final boolean ignoreModifierKey = mIsInDraggingFinger && keyCk.isModifier();
         if (ignoreModifierKey) {
            return;
        }
        sListener.onReleaseKey(primaryCode, withSliding);
    }

    private void callListenerOnFinishSlidingInput() {
         sListener.onFinishSlidingInput();
    }

    private void setKeyDetectorInner(final KeyDetectorCk keyDetectorCk) {
        final KeyboardCk keyboardCk = keyDetectorCk.getKeyboard();
        if (keyboardCk == null) {
            return;
        }
        if (keyDetectorCk == mKeyDetectorCk && keyboardCk == mKeyboardCk) {
            return;
        }
        mKeyDetectorCk = keyDetectorCk;
        mKeyboardCk = keyboardCk;

        mKeyboardLayoutHasBeenChanged = true;
        final int keyPaddedWidth = mKeyboardCk.mMostCommonKeyWidth
                + Math.round(mKeyboardCk.mHorizontalGap);
        final int keyPaddedHeight = mKeyboardCk.mMostCommonKeyHeight
                + Math.round(mKeyboardCk.mVerticalGap);

        mBogusMoveEventCkDetector.setKeyboardGeometry(keyPaddedWidth, keyPaddedHeight);
    }

    @Override
    public boolean isInDraggingFinger() {
        return mIsInDraggingFinger;
    }

    public KeyCk getKey() {
        return mCurrentKeyCk;
    }

    @Override
    public boolean isModifier() {
        return mCurrentKeyCk != null && mCurrentKeyCk.isModifier();
    }

    public KeyCk getKeyOn(final int x, final int y) {
        return mKeyDetectorCk.detectHitKey(x, y);
    }

    private void setReleasedKeyGraphics(final KeyCk keyCk, final boolean withAnimation) {
        if (keyCk == null) {
            return;
        }

        sDrawingProxy.onKeyReleased(keyCk, withAnimation);

        if (keyCk.isShift()) {
            for (final KeyCk shiftKeyCk : mKeyboardCk.mShiftKeyCks) {
                if (shiftKeyCk != keyCk) {
                    sDrawingProxy.onKeyReleased(shiftKeyCk, false /* withAnimation */);
                }
            }
        }

        if (keyCk.altCodeWhileTyping()) {
            final int altCode = keyCk.getAltCode();
            final KeyCk altKeyCk = mKeyboardCk.getKey(altCode);
            if (altKeyCk != null) {
                sDrawingProxy.onKeyReleased(altKeyCk, false /* withAnimation */);
            }
            for (final KeyCk k : mKeyboardCk.mAltCodeKeysWhileTyping) {
                if (k != keyCk && k.getAltCode() == altCode) {
                    sDrawingProxy.onKeyReleased(k, false /* withAnimation */);
                }
            }
        }
    }

    private void setPressedKeyGraphics(final KeyCk keyCk) {
        if (keyCk == null) {
            return;
        }


        final boolean altersCode = keyCk.altCodeWhileTyping() && sTimerCkProxy.isTypingState();

        sDrawingProxy.onKeyPressed(keyCk, true);

        if (keyCk.isShift()) {
            for (final KeyCk shiftKeyCk : mKeyboardCk.mShiftKeyCks) {
                if (shiftKeyCk != keyCk) {
                    sDrawingProxy.onKeyPressed(shiftKeyCk, false /* withPreview */);
                }
            }
        }

        if (altersCode) {
            final int altCode = keyCk.getAltCode();
            final KeyCk altKeyCk = mKeyboardCk.getKey(altCode);
            if (altKeyCk != null) {
                sDrawingProxy.onKeyPressed(altKeyCk, false /* withPreview */);
            }
            for (final KeyCk k : mKeyboardCk.mAltCodeKeysWhileTyping) {
                if (k != keyCk && k.getAltCode() == altCode) {
                    sDrawingProxy.onKeyPressed(k, false /* withPreview */);
                }
            }
        }
    }

    public void getLastCoordinates(final int[] outCoords) {
        CoordinateCkUtils.set(outCoords, mLastX, mLastY);
    }

    private KeyCk onDownKey(final int x, final int y) {
        CoordinateCkUtils.set(mDownCoordinates, x, y);
        mBogusMoveEventCkDetector.onDownKey();
        return onMoveToNewKey(onMoveKeyInternal(x, y), x, y);
    }

    private static int getDistance(final int x1, final int y1, final int x2, final int y2) {
        return (int)Math.hypot(x1 - x2, y1 - y2);
    }

    private KeyCk onMoveKeyInternal(final int x, final int y) {
        mBogusMoveEventCkDetector.onMoveKey(getDistance(x, y, mLastX, mLastY));
        mLastX = x;
        mLastY = y;
        return mKeyDetectorCk.detectHitKey(x, y);
    }

    private KeyCk onMoveKey(final int x, final int y) {
        return onMoveKeyInternal(x, y);
    }

    private KeyCk onMoveToNewKey(final KeyCk newKeyCk, final int x, final int y) {
        mCurrentKeyCk = newKeyCk;
        mKeyX = x;
        mKeyY = y;
        return newKeyCk;
    }

  static int getActivePointerTrackerCount() {
        return S_POINTER_TRACKER_CK_QUEUE.size();
    }

    public void processMotionEvent(final MotionEvent me, final KeyDetectorCk keyDetectorCk) {
        final int action = me.getActionMasked();
        final long eventTime = me.getEventTime();
        if (action == MotionEvent.ACTION_MOVE) {

            final boolean shouldIgnoreOtherPointers =
                    isShowingMoreKeysPanel() && getActivePointerTrackerCount() == 1;
            final int pointerCount = me.getPointerCount();
            for (int index = 0; index < pointerCount; index++) {
                final int id = me.getPointerId(index);
                if (shouldIgnoreOtherPointers && id != mPointerId) {
                    continue;
                }
                final int x = (int)me.getX(index);
                final int y = (int)me.getY(index);
                final PointerCkTracker tracker = getPointerTracker(id);
                tracker.onMoveEvent(x, y, eventTime);
            }
            return;
        }
        final int index = me.getActionIndex();
        final int x = (int)me.getX(index);
        final int y = (int)me.getY(index);
        switch (action) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
            onDownEvent(x, y, eventTime, keyDetectorCk);
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            onUpEvent(x, y, eventTime);
            break;
        case MotionEvent.ACTION_CANCEL:
            onCancelEvent(x, y, eventTime);
            break;
        }
    }

    private void onDownEvent(final int x, final int y, final long eventTime,
            final KeyDetectorCk keyDetectorCk) {
        setKeyDetectorInner(keyDetectorCk);
        if (DEBUG_EVENT) {
            printTouchEvent("onDownEvent:", x, y, eventTime);
        }
        // Naive up-to-down noise filter.
        final long deltaT = eventTime;
        if (deltaT < sParams.mTouchNoiseThresholdTime) {
            final int distance = getDistance(x, y, mLastX, mLastY);
            if (distance < sParams.mTouchNoiseThresholdDistance) {
                if (DEBUG_MODE)
                    Log.w(TAG, String.format("[%d] onDownEvent:"
                            + " ignore potential noise: time=%d distance=%d",
                            mPointerId, deltaT, distance));
                cancelTrackingForAction();
                return;
            }
        }

        final KeyCk keyCk = getKeyOn(x, y);
        mBogusMoveEventCkDetector.onActualDownEvent(x, y);
        if (keyCk != null && keyCk.isModifier()) {

            S_POINTER_TRACKER_CK_QUEUE.releaseAllPointers(eventTime);
        }
        S_POINTER_TRACKER_CK_QUEUE.add(this);
        onDownEventInternal(x, y);
    }

  boolean isShowingMoreKeysPanel() {
        return (mMoreKeysCkPanel != null);
    }

    private void dismissMoreKeysPanel() {
        if (isShowingMoreKeysPanel()) {
            mMoreKeysCkPanel.dismissMoreKeysPanel();
            mMoreKeysCkPanel = null;
        }
    }

    private void onDownEventInternal(final int x, final int y) {
        KeyCk keyCk = onDownKey(x, y);

        mIsAllowedDraggingFinger = sParams.mKeySelectionByDraggingFinger
                || (keyCk != null && keyCk.isModifier())
                || mKeyDetectorCk.alwaysAllowsKeySelectionByDraggingFinger();
        mKeyboardLayoutHasBeenChanged = false;
        mIsTrackingForActionDisabled = false;
        resetKeySelectionByDraggingFinger();
        if (keyCk != null) {

            if (callListenerOnPressAndCheckKeyboardLayoutChange(keyCk, 0 /* repeatCount */)) {
                keyCk = onDownKey(x, y);
            }

            startRepeatKey(keyCk);
            startLongPressTimer(keyCk);
            setPressedKeyGraphics(keyCk);
            mStartX = x;
             mStartTime = System.currentTimeMillis();
        }
    }

    private void startKeySelectionByDraggingFinger(final KeyCk keyCk) {
        if (!mIsInDraggingFinger) {
            mIsInSlidingKeyInput = keyCk.isModifier();
        }
        mIsInDraggingFinger = true;
    }

    private void resetKeySelectionByDraggingFinger() {
        mIsInDraggingFinger = false;
        mIsInSlidingKeyInput = false;
    }

    private void onMoveEvent(final int x, final int y, final long eventTime) {
        if (DEBUG_MOVE_EVENT) {
            printTouchEvent("onMoveEvent:", x, y, eventTime);
        }
        if (mIsTrackingForActionDisabled) {
            return;
        }

        if (isShowingMoreKeysPanel()) {
            final int translatedX = mMoreKeysCkPanel.translateX(x);
            final int translatedY = mMoreKeysCkPanel.translateY(y);
            mMoreKeysCkPanel.onMoveEvent(translatedX, translatedY, mPointerId);
            onMoveKey(x, y);
            return;
        }
        onMoveEventInternal(x, y, eventTime);
    }

    private void processDraggingFingerInToNewKey(final KeyCk newKeyCk, final int x, final int y) {

        KeyCk keyCk = newKeyCk;
        if (callListenerOnPressAndCheckKeyboardLayoutChange(keyCk, 0 /* repeatCount */)) {
            keyCk = onMoveKey(x, y);
        }
        onMoveToNewKey(keyCk, x, y);
        if (mIsTrackingForActionDisabled) {
            return;
        }
        startLongPressTimer(keyCk);
        setPressedKeyGraphics(keyCk);
    }

    private void processDraggingFingerOutFromOldKey(final KeyCk oldKeyCk) {
        setReleasedKeyGraphics(oldKeyCk, true /* withAnimation */);
        callListenerOnRelease(oldKeyCk, oldKeyCk.getCode(), true /* withSliding */);
        startKeySelectionByDraggingFinger(oldKeyCk);
        sTimerCkProxy.cancelKeyTimersOf(this);
    }

    private void dragFingerFromOldKeyToNewKey(final KeyCk keyCk, final int x, final int y,
                                              final long eventTime, final KeyCk oldKeyCk) {

        processDraggingFingerOutFromOldKey(oldKeyCk);
        startRepeatKey(keyCk);
        if (mIsAllowedDraggingFinger) {
            processDraggingFingerInToNewKey(keyCk, x, y);
        }

        else if (getActivePointerTrackerCount() > 1
                && !S_POINTER_TRACKER_CK_QUEUE.hasModifierKeyOlderThan(this)) {
             onUpEvent(x, y, eventTime);
            cancelTrackingForAction();
            setReleasedKeyGraphics(oldKeyCk, true /* withAnimation */);
        } else {
            cancelTrackingForAction();
            setReleasedKeyGraphics(oldKeyCk, true /* withAnimation */);
        }
    }

    private void dragFingerOutFromOldKey(final KeyCk oldKeyCk, final int x, final int y) {

        processDraggingFingerOutFromOldKey(oldKeyCk);
        if (mIsAllowedDraggingFinger) {
            onMoveToNewKey(null, x, y);
        } else {
            cancelTrackingForAction();
        }
    }

        private void onMoveEventInternal(final int x, final int y, final long eventTime) {
        final KeyCk oldKeyCk = mCurrentKeyCk;

        if (oldKeyCk != null && oldKeyCk.getCode() == ConstantsCk.CODE_SPACE && SettingsCk.getInstance().getCurrent().mSpaceSwipeEnabled) {


            int steps = (x - mStartX) / sPointerStep;
            final int longpressTimeout = SettingsCk.getInstance().getCurrent().mKeyLongpressTimeout / MULTIPLIER_FOR_LONG_PRESS_TIMEOUT_IN_SLIDING_INPUT;
            if (steps != 0 && mStartTime + longpressTimeout < System.currentTimeMillis()) {
                mCursorMoved = true;
                mStartX += steps * sPointerStep;
                sListener.onMovePointer(steps);
            }
            return;
        }

        if (oldKeyCk != null && oldKeyCk.getCode() == ConstantsCk.CODE_DELETE && SettingsCk.getInstance().getCurrent().mDeleteSwipeEnabled) {


            int steps = (x - mStartX) / sPointerStep;
            if (steps != 0) {
                sTimerCkProxy.cancelKeyTimersOf(this);
                mCursorMoved = true;
                mStartX += steps * sPointerStep;
                sListener.onMoveDeletePointer(steps);
            }
            return;
        }

        final KeyCk newKeyCk = onMoveKey(x, y);
        if (newKeyCk != null) {
            if (oldKeyCk != null && isMajorEnoughMoveToBeOnNewKey(x, y, newKeyCk)) {
                dragFingerFromOldKeyToNewKey(newKeyCk, x, y, eventTime, oldKeyCk);
            } else if (oldKeyCk == null) {


                processDraggingFingerInToNewKey(newKeyCk, x, y);
            }
        } else {

            if (oldKeyCk != null && isMajorEnoughMoveToBeOnNewKey(x, y, newKeyCk)) {
                dragFingerOutFromOldKey(oldKeyCk, x, y);
            }
        }
    }

    private void onUpEvent(final int x, final int y, final long eventTime) {
        if (DEBUG_EVENT) {
            printTouchEvent("onUpEvent  :", x, y, eventTime);
        }

        sTimerCkProxy.cancelUpdateBatchInputTimer(this);
        if (mCurrentKeyCk != null && mCurrentKeyCk.isModifier()) {


            S_POINTER_TRACKER_CK_QUEUE.releaseAllPointersExcept(this, eventTime);
        } else {
            S_POINTER_TRACKER_CK_QUEUE.releaseAllPointersOlderThan(this, eventTime);
        }
        onUpEventInternal(x, y);
        S_POINTER_TRACKER_CK_QUEUE.remove(this);
    }



    @Override
    public void onPhantomUpEvent(final long eventTime) {

        onUpEventInternal(mLastX, mLastY);
        cancelTrackingForAction();
    }

    private void onUpEventInternal(final int x, final int y) {
        sTimerCkProxy.cancelKeyTimersOf(this);
        final boolean isInDraggingFinger = mIsInDraggingFinger;
        final boolean isInSlidingKeyInput = mIsInSlidingKeyInput;
        resetKeySelectionByDraggingFinger();
        final KeyCk currentKeyCk = mCurrentKeyCk;
        mCurrentKeyCk = null;
        final int currentRepeatingKeyCode = mCurrentRepeatingKeyCode;
        mCurrentRepeatingKeyCode = ConstantsCk.NOT_A_CODE;


        setReleasedKeyGraphics(currentKeyCk, true /* withAnimation */);

        if(mCursorMoved && currentKeyCk.getCode() == ConstantsCk.CODE_DELETE) {
            sListener.onUpWithDeletePointerActive();
        }

        if (isShowingMoreKeysPanel()) {
            if (!mIsTrackingForActionDisabled) {
                final int translatedX = mMoreKeysCkPanel.translateX(x);
                final int translatedY = mMoreKeysCkPanel.translateY(y);
                mMoreKeysCkPanel.onUpEvent(translatedX, translatedY, mPointerId);
            }
            dismissMoreKeysPanel();
            return;
        }

        if (mCursorMoved) {
            mCursorMoved = false;
            return;
        }
        if (mIsTrackingForActionDisabled) {
            return;
        }
        if (currentKeyCk != null && currentKeyCk.isRepeatable()
                && (currentKeyCk.getCode() == currentRepeatingKeyCode) && !isInDraggingFinger) {
            return;
        }
        detectAndSendKey(currentKeyCk, mKeyX, mKeyY);
        if (isInSlidingKeyInput) {
            callListenerOnFinishSlidingInput();
        }
    }

    @Override
    public void cancelTrackingForAction() {
        if (isShowingMoreKeysPanel()) {
            return;
        }
        mIsTrackingForActionDisabled = true;
    }

    public void onLongPressed() {
        sTimerCkProxy.cancelLongPressTimersOf(this);
        if (isShowingMoreKeysPanel()) {
            return;
        }
        if (mCursorMoved) {
            return;
        }
        final KeyCk keyCk = getKey();
        if (keyCk == null) {
            return;
        }
        if (keyCk.hasNoPanelAutoMoreKey()) {
            cancelKeyTracking();
            final int moreKeyCode = keyCk.getMoreKeys()[0].mCode;
            sListener.onPressKey(moreKeyCode, 0
                    , true
            );
            sListener.onCodeInput(moreKeyCode, ConstantsCk.NOT_A_COORDINATE,
                    ConstantsCk.NOT_A_COORDINATE, false
            );
            sListener.onReleaseKey(moreKeyCode, false
            );
            return;
        }
        final int code = keyCk.getCode();
        if (code == ConstantsCk.CODE_SPACE || code == ConstantsCk.CODE_LANGUAGE_SWITCH) {
             if (sListener.onCustomRequest(ConstantsCk.CUSTOM_CODE_SHOW_INPUT_METHOD_PICKER)) {
                cancelKeyTracking();
                sListener.onReleaseKey(code, false  );
                return;
            }
        }

        setReleasedKeyGraphics(keyCk, false  );
        final MoreKeysCkPanel moreKeysCkPanel = sDrawingProxy.showMoreKeysKeyboard(keyCk, this);
        if (moreKeysCkPanel == null) {
            return;
        }
        final int translatedX = moreKeysCkPanel.translateX(mLastX);
        final int translatedY = moreKeysCkPanel.translateY(mLastY);
        moreKeysCkPanel.onDownEvent(translatedX, translatedY, mPointerId);
        mMoreKeysCkPanel = moreKeysCkPanel;
    }

    private void cancelKeyTracking() {
        resetKeySelectionByDraggingFinger();
        cancelTrackingForAction();
        setReleasedKeyGraphics(mCurrentKeyCk, true  );
        S_POINTER_TRACKER_CK_QUEUE.remove(this);
    }

    private void onCancelEvent(final int x, final int y, final long eventTime) {
        if (DEBUG_EVENT) {
            printTouchEvent("onCancelEvt:", x, y, eventTime);
        }

        cancelAllPointerTrackers();
        S_POINTER_TRACKER_CK_QUEUE.releaseAllPointers(eventTime);
        onCancelEventInternal();
    }

    private void onCancelEventInternal() {
        sTimerCkProxy.cancelKeyTimersOf(this);
        setReleasedKeyGraphics(mCurrentKeyCk, true );
        resetKeySelectionByDraggingFinger();
        dismissMoreKeysPanel();
    }

    private boolean isMajorEnoughMoveToBeOnNewKey(final int x, final int y, final KeyCk newKeyCk) {
        final KeyCk curKeyCk = mCurrentKeyCk;
        if (newKeyCk == curKeyCk) {
            return false;
        }
        if (curKeyCk == null  ) {
            return true;
        }
         final int keyHysteresisDistanceSquared = mKeyDetectorCk.getKeyHysteresisDistanceSquared(
                mIsInSlidingKeyInput);
        final int distanceFromKeyEdgeSquared = curKeyCk.squaredDistanceToHitboxEdge(x, y);
        if (distanceFromKeyEdgeSquared >= keyHysteresisDistanceSquared) {
            if (DEBUG_MODE) {
                final float distanceToEdgeRatio = (float)Math.sqrt(distanceFromKeyEdgeSquared)
                        / (mKeyboardCk.mMostCommonKeyWidth + mKeyboardCk.mHorizontalGap);
             }
            return true;
        }
        if (!mIsAllowedDraggingFinger && mBogusMoveEventCkDetector.hasTraveledLongDistance(x, y)) {
            if (DEBUG_MODE) {
                final float keyDiagonal = (float)Math.hypot(
                        mKeyboardCk.mMostCommonKeyWidth + mKeyboardCk.mHorizontalGap,
                        mKeyboardCk.mMostCommonKeyHeight + mKeyboardCk.mVerticalGap);
                final float lengthFromDownRatio =
                        mBogusMoveEventCkDetector.getAccumulatedDistanceFromDownKey() / keyDiagonal;
             }
            return true;
        }
        return false;
    }

    private void startLongPressTimer(final KeyCk keyCk) {
         sTimerCkProxy.cancelLongPressShiftKeyTimer();
        if (keyCk == null) return;
        if (!keyCk.isLongPressEnabled()) return;
         if (mIsInDraggingFinger && keyCk.getMoreKeys() == null) return;

        final int delay = getLongPressTimeout(keyCk.getCode());
        if (delay <= 0) return;
        sTimerCkProxy.startLongPressTimerOf(this, delay);
    }

    private int getLongPressTimeout(final int code) {
        if (code == ConstantsCk.CODE_SHIFT) {
            return sParams.mLongPressShiftLockTimeout;
        }
        final int longpressTimeout = SettingsCk.getInstance().getCurrent().mKeyLongpressTimeout;
        if (mIsInSlidingKeyInput) {

            return longpressTimeout * MULTIPLIER_FOR_LONG_PRESS_TIMEOUT_IN_SLIDING_INPUT;
        }
        if (code == ConstantsCk.CODE_SPACE) {

            return longpressTimeout * MULTIPLIER_FOR_LONG_PRESS_TIMEOUT_IN_SLIDING_INPUT;
        }
        return longpressTimeout;
    }

    private void detectAndSendKey(final KeyCk keyCk, final int x, final int y) {
        if (keyCk == null) return;

        final int code = keyCk.getCode();
        callListenerOnCodeInput(keyCk, code, x, y, false /* isKeyRepeat */);
        callListenerOnRelease(keyCk, code, false /* withSliding */);
    }

    private void startRepeatKey(final KeyCk keyCk) {
        if (keyCk == null) return;
        if (!keyCk.isRepeatable()) return;
         if (mIsInDraggingFinger) return;
        final int startRepeatCount = 1;
        startKeyRepeatTimer(startRepeatCount);
    }

    public void onKeyRepeat(final int code, final int repeatCount) {
        final KeyCk keyCk = getKey();
        if (keyCk == null || keyCk.getCode() != code) {
            mCurrentRepeatingKeyCode = ConstantsCk.NOT_A_CODE;
            return;
        }
        mCurrentRepeatingKeyCode = code;
        final int nextRepeatCount = repeatCount + 1;
        startKeyRepeatTimer(nextRepeatCount);
        callListenerOnPressAndCheckKeyboardLayoutChange(keyCk, repeatCount);
        callListenerOnCodeInput(keyCk, code, mKeyX, mKeyY, true /* isKeyRepeat */);
    }

    private void startKeyRepeatTimer(final int repeatCount) {
        final int delay =
                (repeatCount == 1) ? sParams.mKeyRepeatStartTimeout : sParams.mKeyRepeatInterval;
        sTimerCkProxy.startKeyRepeatTimerOf(this, repeatCount, delay);
    }

    private void printTouchEvent(final String title, final int x, final int y,
            final long eventTime) {
        final KeyCk keyCk = mKeyDetectorCk.detectHitKey(x, y);
        final String code = (keyCk == null ? "none" : ConstantsCk.printableCode(keyCk.getCode()));
     }
}
