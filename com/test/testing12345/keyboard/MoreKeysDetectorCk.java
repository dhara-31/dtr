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

public final class MoreKeysDetectorCk extends KeyDetectorCk {
    private final int mSlideAllowanceSquare;
    private final int mSlideAllowanceSquareTop;

    public MoreKeysDetectorCk(float slideAllowance) {
        super();
        mSlideAllowanceSquare = (int)(slideAllowance * slideAllowance);

        mSlideAllowanceSquareTop = mSlideAllowanceSquare * 2;
    }

    @Override
    public boolean alwaysAllowsKeySelectionByDraggingFinger() {
        return true;
    }

    @Override
    public KeyCk detectHitKey(final int x, final int y) {
        final KeyboardCk keyboardCk = getKeyboard();
        if (keyboardCk == null) {
            return null;
        }
        final int touchX = getTouchX(x);
        final int touchY = getTouchY(y);

        KeyCk nearestKeyCk = null;
        int nearestDist = (y < 0) ? mSlideAllowanceSquareTop : mSlideAllowanceSquare;
        for (final KeyCk keyCk : keyboardCk.getSortedKeys()) {
            final int dist = keyCk.squaredDistanceToHitboxEdge(touchX, touchY);
            if (dist < nearestDist) {
                nearestKeyCk = keyCk;
                nearestDist = dist;
            }
        }
        return nearestKeyCk;
    }
}
