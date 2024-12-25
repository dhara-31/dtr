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

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.test.testing12345.keyboard.internal.KeyVisualCkAttributes;
import com.test.testing12345.keyboard.internal.KeyboardCkIconsSet;
import com.test.testing12345.keyboard.internal.KeyboardCkParams;
import com.test.testing12345.custom.common.ConstantsCk;


public class KeyboardCk {
    public final KeyboardCkId mId;

     public final int mOccupiedHeight;
     public final int mOccupiedWidth;

     public final float mBottomPadding;
     public final float mVerticalGap;
     public final float mHorizontalGap;

     public final KeyVisualCkAttributes mKeyVisualCkAttributes;

    public final int mMostCommonKeyHeight;
    public final int mMostCommonKeyWidth;

     public final int mMoreKeysTemplate;

     private final List<KeyCk> mSortedKeyCks;
    public final List<KeyCk> mShiftKeyCks;
    public final List<KeyCk> mAltCodeKeysWhileTyping;
    public final KeyboardCkIconsSet mIconsSet;

    private final SparseArray<KeyCk> mKeyCache = new SparseArray<>();

    private final ProximityCkInfo mProximityCkInfo;

    public KeyboardCk(final KeyboardCkParams params) {
        mId = params.mId;
        mOccupiedHeight = params.mOccupiedHeight;
        mOccupiedWidth = params.mOccupiedWidth;
        mMostCommonKeyHeight = params.mMostCommonKeyHeight;
        mMostCommonKeyWidth = params.mMostCommonKeyWidth;
        mMoreKeysTemplate = params.mMoreKeysTemplate;
        mKeyVisualCkAttributes = params.mKeyVisualCkAttributes;
        mBottomPadding = params.mBottomPadding;
        mVerticalGap = params.mVerticalGap;
        mHorizontalGap = params.mHorizontalGap;

        mSortedKeyCks = Collections.unmodifiableList(new ArrayList<>(params.mSortedKeyCks));
        mShiftKeyCks = Collections.unmodifiableList(params.mShiftKeyCks);
        mAltCodeKeysWhileTyping = Collections.unmodifiableList(params.mAltCodeKeysWhileTyping);
        mIconsSet = params.mIconsSet;

        mProximityCkInfo = new ProximityCkInfo(params.mGridWidth, params.mGridHeight,
                mOccupiedWidth, mOccupiedHeight, mSortedKeyCks);
    }

     public List<KeyCk> getSortedKeys() {
        return mSortedKeyCks;
    }

    public KeyCk getKey(final int code) {
        if (code == ConstantsCk.CODE_UNSPECIFIED) {
            return null;
        }
        synchronized (mKeyCache) {
            final int index = mKeyCache.indexOfKey(code);
            if (index >= 0) {
                return mKeyCache.valueAt(index);
            }

            for (final KeyCk keyCk : getSortedKeys()) {
                if (keyCk.getCode() == code) {
                    mKeyCache.put(code, keyCk);
                    return keyCk;
                }
            }
            mKeyCache.put(code, null);
            return null;
        }
    }

    public boolean hasKey(final KeyCk aKeyCk) {
        if (mKeyCache.indexOfValue(aKeyCk) >= 0) {
            return true;
        }

        for (final KeyCk keyCk : getSortedKeys()) {
            if (keyCk == aKeyCk) {
                mKeyCache.put(keyCk.getCode(), keyCk);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return mId.toString();
    }

     public List<KeyCk> getNearestKeys(final int x, final int y) {
         final int adjustedX = Math.max(0, Math.min(x, mOccupiedWidth - 1));
        final int adjustedY = Math.max(0, Math.min(y, mOccupiedHeight - 1));
        return mProximityCkInfo.getNearestKeys(adjustedX, adjustedY);
    }
}
