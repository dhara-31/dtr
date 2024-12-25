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


public class KeyDetectorCk {
    private final int mKeyHysteresisDistanceSquared;
    private final int mKeyHysteresisDistanceForSlidingModifierSquared;

    private KeyboardCk mKeyboardCk;
    private int mCorrectionX;
    private int mCorrectionY;

    public KeyDetectorCk() {
        this(0.0f /* keyHysteresisDistance */, 0.0f /* keyHysteresisDistanceForSlidingModifier */);
    }


    public KeyDetectorCk(final float keyHysteresisDistance,
                         final float keyHysteresisDistanceForSlidingModifier) {
        mKeyHysteresisDistanceSquared = (int)(keyHysteresisDistance * keyHysteresisDistance);
        mKeyHysteresisDistanceForSlidingModifierSquared = (int)(
                keyHysteresisDistanceForSlidingModifier * keyHysteresisDistanceForSlidingModifier);
    }

    public void setKeyboard(final KeyboardCk keyboardCk, final float correctionX,
                            final float correctionY) {
        if (keyboardCk == null) {
            throw new NullPointerException();
        }
        mCorrectionX = (int)correctionX;
        mCorrectionY = (int)correctionY;
        mKeyboardCk = keyboardCk;
    }

    public int getKeyHysteresisDistanceSquared(final boolean isSlidingFromModifier) {
        return isSlidingFromModifier
                ? mKeyHysteresisDistanceForSlidingModifierSquared : mKeyHysteresisDistanceSquared;
    }

    public int getTouchX(final int x) {
        return x + mCorrectionX;
    }


    public int getTouchY(final int y) {
        return y + mCorrectionY;
    }

    public KeyboardCk getKeyboard() {
        return mKeyboardCk;
    }

    public boolean alwaysAllowsKeySelectionByDraggingFinger() {
        return false;
    }


    public KeyCk detectHitKey(final int x, final int y) {
        if (mKeyboardCk == null) {
            return null;
        }
        final int touchX = getTouchX(x);
        final int touchY = getTouchY(y);

        for (final KeyCk keyCk : mKeyboardCk.getNearestKeys(touchX, touchY)) {
            if (keyCk.isOnKey(touchX, touchY)) {
                return keyCk;
            }
        }
        return null;
    }
}
