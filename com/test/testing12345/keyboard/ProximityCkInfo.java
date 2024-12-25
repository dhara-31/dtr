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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProximityCkInfo {
    private static final List<KeyCk> EMPTY_KEY_CK_LIST = Collections.emptyList();

    private final int mGridWidth;
    private final int mGridHeight;
    private final int mGridSize;
    private final int mCellWidth;
    private final int mCellHeight;

    private final int mKeyboardMinWidth;
    private final int mKeyboardHeight;
    private final List<KeyCk> mSortedKeyCks;
    private final List<KeyCk>[] mGridNeighbors;

    @SuppressWarnings("unchecked")
    ProximityCkInfo(final int gridWidth, final int gridHeight, final int minWidth, final int height,
                    final List<KeyCk> sortedKeyCks) {
        mGridWidth = gridWidth;
        mGridHeight = gridHeight;
        mGridSize = mGridWidth * mGridHeight;
        mCellWidth = (minWidth + mGridWidth - 1) / mGridWidth;
        mCellHeight = (height + mGridHeight - 1) / mGridHeight;
        mKeyboardMinWidth = minWidth;
        mKeyboardHeight = height;
        mSortedKeyCks = sortedKeyCks;
        mGridNeighbors = new List[mGridSize];
        if (minWidth == 0 || height == 0) {
             return;
        }
        computeNearestNeighbors();
    }

    private void computeNearestNeighbors() {
        final int keyCount = mSortedKeyCks.size();
        final int gridSize = mGridNeighbors.length;
        final int maxKeyRight = mGridWidth * mCellWidth;
        final int maxKeyBottom = mGridHeight * mCellHeight;


        final KeyCk[] neighborsFlatBuffer = new KeyCk[gridSize * keyCount];
        final int[] neighborCountPerCell = new int[gridSize];
        for (final KeyCk keyCk : mSortedKeyCks) {
            if (keyCk.isSpacer()) continue;


            final int keyX = keyCk.getX();
            final int keyY = keyCk.getY();
            final int keyTop = keyY - keyCk.getTopPadding();
            final int keyBottom = Math.min(keyY + keyCk.getHeight() + keyCk.getBottomPadding(),
                    maxKeyBottom);
            final int keyLeft = keyX - keyCk.getLeftPadding();
            final int keyRight = Math.min(keyX + keyCk.getWidth() + keyCk.getRightPadding(),
                    maxKeyRight);
            final int yDeltaToGrid = keyTop % mCellHeight;
            final int xDeltaToGrid = keyLeft % mCellWidth;
            final int yStart = keyTop - yDeltaToGrid;
            final int xStart = keyLeft - xDeltaToGrid;
            int baseIndexOfCurrentRow = (yStart / mCellHeight) * mGridWidth + (xStart / mCellWidth);
            for (int cellTop = yStart; cellTop < keyBottom; cellTop += mCellHeight) {
                int index = baseIndexOfCurrentRow;
                for (int cellLeft = xStart; cellLeft < keyRight; cellLeft += mCellWidth) {
                    neighborsFlatBuffer[index * keyCount + neighborCountPerCell[index]] = keyCk;
                    ++neighborCountPerCell[index];
                    ++index;
                }
                baseIndexOfCurrentRow += mGridWidth;
            }
        }

        for (int i = 0; i < gridSize; ++i) {
            final int indexStart = i * keyCount;
            final int indexEnd = indexStart + neighborCountPerCell[i];
            final ArrayList<KeyCk> neighbors = new ArrayList<>(indexEnd - indexStart);
            for (int index = indexStart; index < indexEnd; index++) {
                neighbors.add(neighborsFlatBuffer[index]);
            }
            mGridNeighbors[i] = Collections.unmodifiableList(neighbors);
        }
    }

    public List<KeyCk> getNearestKeys(final int x, final int y) {
        if (x >= 0 && x < mKeyboardMinWidth && y >= 0 && y < mKeyboardHeight) {
            int index = (y / mCellHeight) * mGridWidth + (x / mCellWidth);
            if (index < mGridSize) {
                return mGridNeighbors[index];
            }
        }
        return EMPTY_KEY_CK_LIST;
    }
}
