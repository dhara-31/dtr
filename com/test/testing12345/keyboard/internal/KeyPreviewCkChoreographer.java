/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.HashMap;

import com.test.testing12345.keyboard.KeyCk;
import com.test.testing12345.custom.common.CoordinateCkUtils;
import com.test.testing12345.custom.utils.ViewLayoutCkUtils;

public final class KeyPreviewCkChoreographer {

    private final ArrayDeque<KeyPreviewCkView> mFreeKeyPreviewCkViews = new ArrayDeque<>();

    private final HashMap<KeyCk, KeyPreviewCkView> mShowingKeyPreviewViews = new HashMap<>();

    private final KeyPreviewCkDrawParams mParams;

    public KeyPreviewCkChoreographer(final KeyPreviewCkDrawParams params) {
        mParams = params;
    }

    public KeyPreviewCkView getKeyPreviewView(final KeyCk keyCk, final ViewGroup placerView) {
        KeyPreviewCkView keyPreviewCkView = mShowingKeyPreviewViews.remove(keyCk);
        if (keyPreviewCkView != null) {
            keyPreviewCkView.setScaleX(1);
            keyPreviewCkView.setScaleY(1);
            return keyPreviewCkView;
        }
        keyPreviewCkView = mFreeKeyPreviewCkViews.poll();
        if (keyPreviewCkView != null) {
            keyPreviewCkView.setScaleX(1);
            keyPreviewCkView.setScaleY(1);
            return keyPreviewCkView;
        }
        final Context context = placerView.getContext();
        keyPreviewCkView = new KeyPreviewCkView(context, null /* attrs */);
        keyPreviewCkView.setBackgroundResource(mParams.mPreviewBackgroundResId);
        placerView.addView(keyPreviewCkView, ViewLayoutCkUtils.newLayoutParam(placerView, 0, 0));
        return keyPreviewCkView;
    }

    public void dismissKeyPreview(final KeyCk keyCk, final boolean withAnimation) {
        if (keyCk == null) {
            return;
        }
        final KeyPreviewCkView keyPreviewCkView = mShowingKeyPreviewViews.get(keyCk);
        if (keyPreviewCkView == null) {
            return;
        }
        final Object tag = keyPreviewCkView.getTag();
        if (withAnimation) {
            if (tag instanceof KeyPreviewAnimators) {
                final KeyPreviewAnimators animators = (KeyPreviewAnimators)tag;
                animators.startDismiss();
                return;
            }
        }
        // Dismiss preview without animation.
        mShowingKeyPreviewViews.remove(keyCk);
        if (tag instanceof Animator) {
            ((Animator)tag).cancel();
        }
        keyPreviewCkView.setTag(null);
        keyPreviewCkView.setVisibility(View.INVISIBLE);
        mFreeKeyPreviewCkViews.add(keyPreviewCkView);
    }

    public void placeAndShowKeyPreview(final KeyCk keyCk, final KeyboardCkIconsSet iconsSet,
                                       final KeyDrawCkParams drawParams, final int[] keyboardOrigin,
                                       final ViewGroup placerView, final boolean withAnimation) {
        final KeyPreviewCkView keyPreviewCkView = getKeyPreviewView(keyCk, placerView);
        placeKeyPreview(
                keyCk, keyPreviewCkView, iconsSet, drawParams, keyboardOrigin);
        showKeyPreview(keyCk, keyPreviewCkView, withAnimation);
    }

    private void placeKeyPreview(final KeyCk keyCk, final KeyPreviewCkView keyPreviewCkView,
                                 final KeyboardCkIconsSet iconsSet, final KeyDrawCkParams drawParams,
                                 final int[] originCoords) {
        keyPreviewCkView.setPreviewVisual(keyCk, iconsSet, drawParams);
        keyPreviewCkView.measure(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mParams.setGeometry(keyPreviewCkView);
        final int previewWidth = Math.max(keyPreviewCkView.getMeasuredWidth(), mParams.mMinPreviewWidth);
        final int previewHeight = mParams.mPreviewHeight;
        final int keyWidth = keyCk.getWidth();

        int previewX = keyCk.getX() - (previewWidth - keyWidth) / 2
                + CoordinateCkUtils.x(originCoords);

        final int previewY = keyCk.getY() - previewHeight + mParams.mPreviewOffset
                + CoordinateCkUtils.y(originCoords);

        ViewLayoutCkUtils.placeViewAt(
                keyPreviewCkView, previewX, previewY, previewWidth, previewHeight);

    }

    void showKeyPreview(final KeyCk keyCk, final KeyPreviewCkView keyPreviewCkView,
                        final boolean withAnimation) {
        if (!withAnimation) {
            keyPreviewCkView.setVisibility(View.VISIBLE);
            mShowingKeyPreviewViews.put(keyCk, keyPreviewCkView);
            return;
        }

         final Animator dismissAnimator = createDismissAnimator(keyCk, keyPreviewCkView);
        final KeyPreviewAnimators animators = new KeyPreviewAnimators(dismissAnimator);
        keyPreviewCkView.setTag(animators);
        showKeyPreview(keyCk, keyPreviewCkView, false /* withAnimation */);
    }

    private Animator createDismissAnimator(final KeyCk keyCk, final KeyPreviewCkView keyPreviewCkView) {
        final Animator dismissAnimator = mParams.createDismissAnimator(keyPreviewCkView);
        dismissAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animator) {
                dismissKeyPreview(keyCk, false /* withAnimation */);
            }
        });
        return dismissAnimator;
    }

    private static class KeyPreviewAnimators extends AnimatorListenerAdapter {
        private final Animator mDismissAnimator;

        public KeyPreviewAnimators(final Animator dismissAnimator) {
            mDismissAnimator = dismissAnimator;
        }

        public void startDismiss() {
            mDismissAnimator.start();
        }
    }
}
