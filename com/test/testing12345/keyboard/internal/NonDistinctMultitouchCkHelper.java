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

package com.test.testing12345.keyboard.internal;

import android.view.MotionEvent;

import com.test.testing12345.keyboard.KeyCk;
import com.test.testing12345.keyboard.KeyDetectorCk;
import com.test.testing12345.keyboard.PointerCkTracker;
import com.test.testing12345.custom.common.CoordinateCkUtils;

public final class NonDistinctMultitouchCkHelper {
    private static final String TAG = NonDistinctMultitouchCkHelper.class.getSimpleName();

    private static final int MAIN_POINTER_TRACKER_ID = 0;
    private int mOldPointerCount = 1;
    private KeyCk mOldKeyCk;
    private int[] mLastCoords = CoordinateCkUtils.newInstance();

    public void processMotionEvent(final MotionEvent me, final KeyDetectorCk keyDetectorCk) {
        final int pointerCount = me.getPointerCount();
        final int oldPointerCount = mOldPointerCount;
        mOldPointerCount = pointerCount;

        if (pointerCount > 1 && oldPointerCount > 1) {
            return;
        }


        final PointerCkTracker mainTracker = PointerCkTracker.getPointerTracker(
                MAIN_POINTER_TRACKER_ID);
        final int action = me.getActionMasked();
        final int index = me.getActionIndex();
        final long eventTime = me.getEventTime();
        final long downTime = me.getDownTime();


        if (oldPointerCount == 1 && pointerCount == 1) {
            if (me.getPointerId(index) == mainTracker.mPointerId) {
                mainTracker.processMotionEvent(me, keyDetectorCk);
                return;
            }

            injectMotionEvent(action, me.getX(index), me.getY(index), downTime, eventTime,
                    mainTracker, keyDetectorCk);
            return;
        }

         if (oldPointerCount == 1 && pointerCount == 2) {

            mainTracker.getLastCoordinates(mLastCoords);
            final int x = CoordinateCkUtils.x(mLastCoords);
            final int y = CoordinateCkUtils.y(mLastCoords);
            mOldKeyCk = mainTracker.getKeyOn(x, y);
             injectMotionEvent(MotionEvent.ACTION_UP, x, y, downTime, eventTime,
                    mainTracker, keyDetectorCk);
            return;
        }

         if (oldPointerCount == 2 && pointerCount == 1) {

            final int x = (int)me.getX(index);
            final int y = (int)me.getY(index);
            final KeyCk newKeyCk = mainTracker.getKeyOn(x, y);
            if (mOldKeyCk != newKeyCk) {

                injectMotionEvent(MotionEvent.ACTION_DOWN, x, y, downTime, eventTime,
                        mainTracker, keyDetectorCk);
                if (action == MotionEvent.ACTION_UP) {
                     injectMotionEvent(MotionEvent.ACTION_UP, x, y, downTime, eventTime,
                            mainTracker, keyDetectorCk);
                }
            }
            return;
        }

     }

    private static void injectMotionEvent(final int action, final float x, final float y,
            final long downTime, final long eventTime, final PointerCkTracker tracker,
            final KeyDetectorCk keyDetectorCk) {
        final MotionEvent me = MotionEvent.obtain(
                downTime, eventTime, action, x, y, 0 );
        try {
            tracker.processMotionEvent(me, keyDetectorCk);
        } finally {
            me.recycle();
        }
    }
}
