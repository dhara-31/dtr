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

import android.content.Context;
import android.graphics.Paint;

import com.test.testing12345.R;
import com.test.testing12345.keyboard.internal.KeyboardCkBuilder;
import com.test.testing12345.keyboard.internal.KeyboardCkParams;
import com.test.testing12345.keyboard.internal.MoreKeyCkSpec;
import com.test.testing12345.custom.common.StringCkUtils;
import com.test.testing12345.custom.utils.TypefaceCkUtils;

public final class MoreKeysKeyboardCk extends KeyboardCk {
    private static final String TAG = MoreKeysKeyboardCk.class.getSimpleName();
    private final int mDefaultKeyCoordX;
    private static final float FLOAT_THRESHOLD = 0.0001f;

    MoreKeysKeyboardCk(final MoreKeysKeyboardCkParams params) {
        super(params);
        mDefaultKeyCoordX = Math.round(params.getDefaultKeyCoordX() + params.mOffsetX
                + (params.mDefaultKeyPaddedWidth - params.mHorizontalGap) / 2);
    }

    public int getDefaultCoordX() {
        return mDefaultKeyCoordX;
    }

    static class MoreKeysKeyboardCkParams extends KeyboardCkParams {
        public boolean mIsMoreKeysFixedOrder;
         int mTopRowAdjustment;
        public int mNumRows;
        public int mNumColumns;
        public int mTopKeys;
        public int mLeftKeys;
        public int mRightKeys;
        public float mColumnWidth;
        public float mOffsetX;

        public MoreKeysKeyboardCkParams() {
            super();
        }

         public void setParameters(final int numKeys, final int numColumn,
                                  final float keyPaddedWidth, final float rowHeight,
                                  final float coordXInParent, final int parentKeyboardWidth,
                                  final boolean isMoreKeysFixedColumn,
                                  final boolean isMoreKeysFixedOrder) {

            final float availableWidth = parentKeyboardWidth - mLeftPadding - mRightPadding
                    + mHorizontalGap;
            if (availableWidth < keyPaddedWidth) {
                throw new IllegalArgumentException("Keyboard is too small to hold more keys: "
                        + availableWidth + " " + keyPaddedWidth);
            }
            mIsMoreKeysFixedOrder = isMoreKeysFixedOrder;
            mDefaultKeyPaddedWidth = keyPaddedWidth;
            mDefaultRowHeight = rowHeight;

            final int maxColumns = getMaxKeys(availableWidth, keyPaddedWidth);
            if (isMoreKeysFixedColumn) {
                int requestedNumColumns = Math.min(numKeys, numColumn);
                if (maxColumns < requestedNumColumns) {
                     mNumColumns = maxColumns;
                } else {
                    mNumColumns = requestedNumColumns;
                }
                mNumRows = getNumRows(numKeys, mNumColumns);
            } else {
                int defaultNumColumns = Math.min(maxColumns, numColumn);
                mNumRows = getNumRows(numKeys, defaultNumColumns);
                mNumColumns = getOptimizedColumns(numKeys, defaultNumColumns, mNumRows);
            }
            final int topKeys = numKeys % mNumColumns;
            mTopKeys = topKeys == 0 ? mNumColumns : topKeys;

            final int numLeftKeys = (mNumColumns - 1) / 2;
            final int numRightKeys = mNumColumns - numLeftKeys;

            final float leftWidth = Math.max(coordXInParent - mLeftPadding - keyPaddedWidth / 2
                    + mHorizontalGap / 2, 0);
            final float rightWidth = Math.max(parentKeyboardWidth - coordXInParent
                    + keyPaddedWidth / 2 - mRightPadding + mHorizontalGap / 2, 0);
            int maxLeftKeys = getMaxKeys(leftWidth, keyPaddedWidth);
            int maxRightKeys = getMaxKeys(rightWidth, keyPaddedWidth);

            if (numKeys >= mNumColumns && mNumColumns == maxColumns
                    && maxLeftKeys + maxRightKeys < maxColumns) {
                final float extraLeft = leftWidth - maxLeftKeys * keyPaddedWidth;
                final float extraRight = rightWidth - maxRightKeys * keyPaddedWidth;

                if (extraLeft > extraRight) {
                    maxLeftKeys++;
                } else {
                    maxRightKeys++;
                }
            }

            int leftKeys, rightKeys;
            if (numLeftKeys > maxLeftKeys) {
                leftKeys = maxLeftKeys;
                rightKeys = mNumColumns - leftKeys;
            } else if (numRightKeys > maxRightKeys) {

                rightKeys = Math.max(maxRightKeys, 1);
                leftKeys = mNumColumns - rightKeys;
            } else {
                leftKeys = numLeftKeys;
                rightKeys = numRightKeys;
            }
            mLeftKeys = leftKeys;
            mRightKeys = rightKeys;


            mTopRowAdjustment = getTopRowAdjustment();
            mColumnWidth = mDefaultKeyPaddedWidth;
            mBaseWidth = mNumColumns * mColumnWidth;

            mOccupiedWidth = Math.round(mBaseWidth + mLeftPadding + mRightPadding - mHorizontalGap);
            mBaseHeight = mNumRows * mDefaultRowHeight;

            mOccupiedHeight = Math.round(mBaseHeight + mTopPadding + mBottomPadding - mVerticalGap);



            mGridWidth = Math.min(mGridWidth, mNumColumns);
            mGridHeight = Math.min(mGridHeight, mNumRows);
        }

        private int getTopRowAdjustment() {
            final int numOffCenterKeys = Math.abs(mRightKeys - 1 - mLeftKeys);

            if (mTopKeys > mNumColumns - numOffCenterKeys || mTopKeys % 2 == 1) {
                return 0;
            }
            return -1;
        }

         int getColumnPos(final int n) {
            return mIsMoreKeysFixedOrder ? getFixedOrderColumnPos(n) : getAutomaticColumnPos(n);
        }

        private int getFixedOrderColumnPos(final int n) {
            final int col = n % mNumColumns;
            final int row = n / mNumColumns;
            if (!isTopRow(row)) {
                return col - mLeftKeys;
            }
            final int rightSideKeys = mTopKeys / 2;
            final int leftSideKeys = mTopKeys - (rightSideKeys + 1);
            final int pos = col - leftSideKeys;
            final int numLeftKeys = mLeftKeys + mTopRowAdjustment;
            final int numRightKeys = mRightKeys - 1;
            if (numRightKeys >= rightSideKeys && numLeftKeys >= leftSideKeys) {
                return pos;
            } else if (numRightKeys < rightSideKeys) {
                return pos - (rightSideKeys - numRightKeys);
            } else { // numLeftKeys < leftSideKeys
                return pos + (leftSideKeys - numLeftKeys);
            }
        }

        private int getAutomaticColumnPos(final int n) {
            final int col = n % mNumColumns;
            final int row = n / mNumColumns;
            int leftKeys = mLeftKeys;
            if (isTopRow(row)) {
                leftKeys += mTopRowAdjustment;
            }
            if (col == 0) {

                return 0;
            }

            int pos = 0;
            int right = 1;
            int left = 0;
            int i = 0;
            while (true) {

                if (right < mRightKeys) {
                    pos = right;
                    right++;
                    i++;
                }
                if (i >= col)
                    break;
                 if (left < leftKeys) {
                    left++;
                    pos = -left;
                    i++;
                }
                if (i >= col)
                    break;
            }
            return pos;
        }

        private static int getTopRowEmptySlots(final int numKeys, final int numColumns) {
            final int remainings = numKeys % numColumns;
            return remainings == 0 ? 0 : numColumns - remainings;
        }

        private static int getOptimizedColumns(final int numKeys, final int maxColumns,
                                               final int numRows) {
            int numColumns = Math.min(numKeys, maxColumns);
            while (getTopRowEmptySlots(numKeys, numColumns) >= numRows) {
                numColumns--;
            }
            return numColumns;
        }

        private static int getNumRows(final int numKeys, final int numColumn) {
            return (numKeys + numColumn - 1) / numColumn;
        }

        private static int getMaxKeys(final float keyboardWidth, final float keyPaddedWidth) {

            final int maxKeys = Math.round(keyboardWidth / keyPaddedWidth);
            if (maxKeys * keyPaddedWidth > keyboardWidth + FLOAT_THRESHOLD) {
                return maxKeys - 1;
            }
            return maxKeys;
        }

        public float getDefaultKeyCoordX() {
            return mLeftKeys * mColumnWidth + mLeftPadding;
        }

        public float getX(final int n, final int row) {
            final float x = getColumnPos(n) * mColumnWidth + getDefaultKeyCoordX();
            if (isTopRow(row)) {
                return x + mTopRowAdjustment * (mColumnWidth / 2);
            }
            return x;
        }

        public float getY(final int row) {
            return (mNumRows - 1 - row) * mDefaultRowHeight + mTopPadding;
        }

        private boolean isTopRow(final int rowCount) {
            return mNumRows > 1 && rowCount == mNumRows - 1;
        }
    }

    public static class CkBuilder extends KeyboardCkBuilder<MoreKeysKeyboardCkParams> {
        private final KeyCk mParentKeyCk;

        private static final float LABEL_PADDING_RATIO = 0.2f;

         public CkBuilder(final Context context, final KeyCk keyCk, final KeyboardCk keyboardCk,
                          final boolean isSingleMoreKeyWithPreview, final int keyPreviewVisibleWidth,
                          final int keyPreviewVisibleHeight, final Paint paintToMeasure) {
            super(context, new MoreKeysKeyboardCkParams());
            load(keyboardCk.mMoreKeysTemplate, keyboardCk.mId);



            mParams.mVerticalGap = keyboardCk.mVerticalGap / 2;

            mParentKeyCk = keyCk;

            final float keyPaddedWidth, rowHeight;
            if (isSingleMoreKeyWithPreview) {

                final float keyboardHorizontalPadding = mParams.mLeftPadding
                        + mParams.mRightPadding;
                final float baseKeyPaddedWidth = keyPreviewVisibleWidth + mParams.mHorizontalGap;
                if (keyboardHorizontalPadding > baseKeyPaddedWidth - FLOAT_THRESHOLD) {

                    keyPaddedWidth = baseKeyPaddedWidth;
                } else {
                    keyPaddedWidth = baseKeyPaddedWidth - keyboardHorizontalPadding;

                    mParams.mOffsetX = (mParams.mRightPadding - mParams.mLeftPadding) / 2;
                }
                final float baseKeyPaddedHeight = keyPreviewVisibleHeight + mParams.mVerticalGap;
                if (mParams.mTopPadding > baseKeyPaddedHeight - FLOAT_THRESHOLD) {
                     rowHeight = baseKeyPaddedHeight;
                } else {
                    rowHeight = baseKeyPaddedHeight - mParams.mTopPadding;
                }
            } else {
                final float defaultKeyWidth = mParams.mDefaultKeyPaddedWidth
                        - mParams.mHorizontalGap;
                final float padding = context.getResources().getDimension(
                        R.dimen.config_more_keys_keyboard_key_horizontal_padding)
                        + (keyCk.hasLabelsInMoreKeys()
                        ? defaultKeyWidth * LABEL_PADDING_RATIO : 0.0f);
                keyPaddedWidth = getMaxKeyWidth(keyCk, defaultKeyWidth, padding, paintToMeasure)
                        + mParams.mHorizontalGap;
                rowHeight = keyboardCk.mMostCommonKeyHeight + keyboardCk.mVerticalGap;
            }
            final MoreKeyCkSpec[] moreKeys = keyCk.getMoreKeys();
            mParams.setParameters(moreKeys.length, keyCk.getMoreKeysColumnNumber(), keyPaddedWidth,
                    rowHeight, keyCk.getX() + keyCk.getWidth() / 2f, keyboardCk.mId.mWidth,
                    keyCk.isMoreKeysFixedColumn(), keyCk.isMoreKeysFixedOrder());
        }

        private static float getMaxKeyWidth(final KeyCk parentKeyCk, final float minKeyWidth,
                                            final float padding, final Paint paint) {
            float maxWidth = minKeyWidth;
            for (final MoreKeyCkSpec spec : parentKeyCk.getMoreKeys()) {
                final String label = spec.mLabel;
                 if (label != null && StringCkUtils.codePointCount(label) > 1) {
                    maxWidth = Math.max(maxWidth,
                            TypefaceCkUtils.getStringWidth(label, paint) + padding);
                }
            }
            return maxWidth;
        }

        @Override
        public MoreKeysKeyboardCk build() {
            final MoreKeysKeyboardCkParams params = mParams;
            final int moreKeyFlags = mParentKeyCk.getMoreKeyLabelFlags();
            final MoreKeyCkSpec[] moreKeys = mParentKeyCk.getMoreKeys();
            for (int n = 0; n < moreKeys.length; n++) {
                final MoreKeyCkSpec moreKeyCkSpec = moreKeys[n];
                final int row = n / params.mNumColumns;
                final float width = params.mDefaultKeyPaddedWidth - params.mHorizontalGap;
                final float height = params.mDefaultRowHeight - params.mVerticalGap;
                final float keyLeftEdge = params.getX(n, row);
                final float keyTopEdge = params.getY(row);
                final float keyRightEdge = keyLeftEdge + width;
                final float keyBottomEdge = keyTopEdge + height;

                final float keyboardLeftEdge = params.mLeftPadding;
                final float keyboardRightEdge = params.mOccupiedWidth - params.mRightPadding;
                final float keyboardTopEdge = params.mTopPadding;
                final float keyboardBottomEdge = params.mOccupiedHeight - params.mBottomPadding;

                final float keyLeftPadding = keyLeftEdge < keyboardLeftEdge + FLOAT_THRESHOLD
                                ? params.mLeftPadding : params.mHorizontalGap / 2;
                final float keyRightPadding = keyRightEdge > keyboardRightEdge - FLOAT_THRESHOLD
                                ? params.mRightPadding : params.mHorizontalGap / 2;
                final float keyTopPadding = keyTopEdge < keyboardTopEdge + FLOAT_THRESHOLD
                                ? params.mTopPadding : params.mVerticalGap / 2;
                final float keyBottomPadding = keyBottomEdge > keyboardBottomEdge - FLOAT_THRESHOLD
                                ? params.mBottomPadding : params.mVerticalGap / 2;

                final KeyCk keyCk = moreKeyCkSpec.buildKey(keyLeftEdge, keyTopEdge, width, height,
                        keyLeftPadding, keyRightPadding, keyTopPadding, keyBottomPadding,
                        moreKeyFlags);
                params.onAddKey(keyCk);
            }
            return new MoreKeysKeyboardCk(params);
        }
    }
}
