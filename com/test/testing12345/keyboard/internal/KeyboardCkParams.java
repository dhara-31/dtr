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

import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.test.testing12345.keyboard.KeyCk;
import com.test.testing12345.keyboard.KeyboardCkId;
import com.test.testing12345.custom.common.ConstantsCk;

public class KeyboardCkParams {
    public KeyboardCkId mId;

     public int mOccupiedHeight;
    public int mOccupiedWidth;

     public float mBaseHeight;
    public float mBaseWidth;

    public float mTopPadding;
    public float mBottomPadding;
    public float mLeftPadding;
    public float mRightPadding;

    public KeyVisualCkAttributes mKeyVisualCkAttributes;

    public float mDefaultRowHeight;
    public float mDefaultKeyPaddedWidth;
    public float mHorizontalGap;
    public float mVerticalGap;

    public int mMoreKeysTemplate;
    public int mMaxMoreKeysKeyboardColumn;

    public int mGridWidth;
    public int mGridHeight;

    // Keys are sorted from top-left to bottom-right order.
    public final SortedSet<KeyCk> mSortedKeyCks = new TreeSet<>(ROW_COLUMN_COMPARATOR);
    public final ArrayList<KeyCk> mShiftKeyCks = new ArrayList<>();
    public final ArrayList<KeyCk> mAltCodeKeysWhileTyping = new ArrayList<>();
    public final KeyboardCkIconsSet mIconsSet = new KeyboardCkIconsSet();
    public final KeyboardCkTextsSet mTextsSet = new KeyboardCkTextsSet();
    public final KeyStylesCkSet mKeyStyles = new KeyStylesCkSet(mTextsSet);

    private final UniqueKeysCkCache mUniqueKeysCkCache;
    public boolean mAllowRedundantMoreKeys;

    public int mMostCommonKeyHeight = 0;
    public int mMostCommonKeyWidth = 0;

    // Comparator to sort {@link Key}s from top-left to bottom-right order.
    private static final Comparator<KeyCk> ROW_COLUMN_COMPARATOR = new Comparator<KeyCk>() {
        @Override
        public int compare(final KeyCk lhs, final KeyCk rhs) {
            if (lhs.getY() < rhs.getY()) return -1;
            if (lhs.getY() > rhs.getY()) return 1;
            if (lhs.getX() < rhs.getX()) return -1;
            if (lhs.getX() > rhs.getX()) return 1;
            return 0;
        }
    };

    public KeyboardCkParams() {
        this(UniqueKeysCkCache.NO_CACHE);
    }

    public KeyboardCkParams(final UniqueKeysCkCache keysCache) {
        mUniqueKeysCkCache = keysCache;
    }

    public void onAddKey(final KeyCk newKeyCk) {
        final KeyCk keyCk = mUniqueKeysCkCache.getUniqueKey(newKeyCk);
        final boolean isSpacer = keyCk.isSpacer();
        if (isSpacer && keyCk.getWidth() == 0) {
            // Ignore zero width {@link Spacer}.
            return;
        }
        mSortedKeyCks.add(keyCk);
        if (isSpacer) {
            return;
        }
        updateHistogram(keyCk);
        if (keyCk.getCode() == ConstantsCk.CODE_SHIFT) {
            mShiftKeyCks.add(keyCk);
        }
        if (keyCk.altCodeWhileTyping()) {
            mAltCodeKeysWhileTyping.add(keyCk);
        }
    }

    public void removeRedundantMoreKeys() {
        if (mAllowRedundantMoreKeys) {
            return;
        }
        final MoreKeyCkSpec.LettersOnBaseLayout lettersOnBaseLayout =
                new MoreKeyCkSpec.LettersOnBaseLayout();
        for (final KeyCk keyCk : mSortedKeyCks) {
            lettersOnBaseLayout.addLetter(keyCk);
        }
        final ArrayList<KeyCk> allKeyCks = new ArrayList<>(mSortedKeyCks);
        mSortedKeyCks.clear();
        for (final KeyCk keyCk : allKeyCks) {
            final KeyCk filteredKeyCk = KeyCk.removeRedundantMoreKeys(keyCk, lettersOnBaseLayout);
            mSortedKeyCks.add(mUniqueKeysCkCache.getUniqueKey(filteredKeyCk));
        }
    }

    private int mMaxHeightCount = 0;
    private int mMaxWidthCount = 0;
    private final SparseIntArray mHeightHistogram = new SparseIntArray();
    private final SparseIntArray mWidthHistogram = new SparseIntArray();

    private static int updateHistogramCounter(final SparseIntArray histogram, final int key) {
        final int index = histogram.indexOfKey(key);
        final int count = (index >= 0 ? histogram.get(key) : 0) + 1;
        histogram.put(key, count);
        return count;
    }

    private void updateHistogram(final KeyCk keyCk) {
        final int height = Math.round(keyCk.getDefinedHeight());
        final int heightCount = updateHistogramCounter(mHeightHistogram, height);
        if (heightCount > mMaxHeightCount) {
            mMaxHeightCount = heightCount;
            mMostCommonKeyHeight = height;
        }

        final int width = Math.round(keyCk.getDefinedWidth());
        final int widthCount = updateHistogramCounter(mWidthHistogram, width);
        if (widthCount > mMaxWidthCount) {
            mMaxWidthCount = widthCount;
            mMostCommonKeyWidth = width;
        }
    }
}
