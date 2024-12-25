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

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.test.testing12345.R;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.define.DebugCkFlags;

 public final class BogusMoveEventCkDetector {
    private static final String TAG = BogusMoveEventCkDetector.class.getSimpleName();
    private static final boolean DEBUG_MODE = DebugCkFlags.DEBUG_ENABLED;


    private static final float BOGUS_MOVE_ACCUMULATED_DISTANCE_THRESHOLD = 0.53f;

    private static boolean sNeedsProximateBogusDownMoveUpEventHack;

    public static void init(final Resources res) {

        final int screenMetrics = res.getInteger(R.integer.config_screen_metrics);
        final boolean isLargeTablet = (screenMetrics == ConstantsCk.SCREEN_METRICS_LARGE_TABLET);
        final boolean isSmallTablet = (screenMetrics == ConstantsCk.SCREEN_METRICS_SMALL_TABLET);
        final int densityDpi = res.getDisplayMetrics().densityDpi;
        final boolean hasLowDensityScreen = (densityDpi < DisplayMetrics.DENSITY_HIGH);
        final boolean needsTheHack = isLargeTablet || (isSmallTablet && hasLowDensityScreen);
         sNeedsProximateBogusDownMoveUpEventHack = needsTheHack;
    }

    private int mAccumulatedDistanceThreshold;

   int mAccumulatedDistanceFromDownKey;
    private int mActualDownX;
    private int mActualDownY;

    public void setKeyboardGeometry(final int keyPaddedWidth, final int keyPaddedHeight) {
        final float keyDiagonal = (float)Math.hypot(keyPaddedWidth, keyPaddedHeight);
        mAccumulatedDistanceThreshold = (int)(
                keyDiagonal * BOGUS_MOVE_ACCUMULATED_DISTANCE_THRESHOLD);
    }

    public void onActualDownEvent(final int x, final int y) {
        mActualDownX = x;
        mActualDownY = y;
    }

    public void onDownKey() {
        mAccumulatedDistanceFromDownKey = 0;
    }

    public void onMoveKey(final int distance) {
        mAccumulatedDistanceFromDownKey += distance;
    }

    public boolean hasTraveledLongDistance(final int x, final int y) {
        if (!sNeedsProximateBogusDownMoveUpEventHack) {
            return false;
        }
        final int dx = Math.abs(x - mActualDownX);
        final int dy = Math.abs(y - mActualDownY);

        return dx >= dy && mAccumulatedDistanceFromDownKey >= mAccumulatedDistanceThreshold;
    }

    public int getAccumulatedDistanceFromDownKey() {
        return mAccumulatedDistanceFromDownKey;
    }
}
