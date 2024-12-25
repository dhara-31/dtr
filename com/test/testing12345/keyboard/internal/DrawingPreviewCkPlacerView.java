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
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.test.testing12345.custom.common.CoordinateCkUtils;

public final class DrawingPreviewCkPlacerView extends RelativeLayout {
    private final int[] mKeyboardViewOrigin = CoordinateCkUtils.newInstance();

    public DrawingPreviewCkPlacerView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setKeyboardViewGeometry(final int[] originCoords) {
        CoordinateCkUtils.copy(mKeyboardViewOrigin, originCoords);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final int originX = CoordinateCkUtils.x(mKeyboardViewOrigin);
        final int originY = CoordinateCkUtils.y(mKeyboardViewOrigin);
        canvas.translate(originX, originY);
        canvas.translate(-originX, -originY);
    }
}
