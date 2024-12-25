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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.test.testing12345.R;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.CoordinateCkUtils;


public class MoreKeysCkKeyboardCkView extends KeyboardCkView implements MoreKeysCkPanel {
    private final int[] mCoordinates = CoordinateCkUtils.newInstance();

    protected final KeyDetectorCk mKeyDetectorCk;
    private Controller mController = EMPTY_CONTROLLER;
    protected KeyboardActionListener mListener;
    private int mOriginX;
    private int mOriginY;
    private KeyCk mCurrentKeyCk;

    private int mActivePointerId;

    public MoreKeysCkKeyboardCkView(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.moreKeysKeyboardViewStyle);
    }

    public MoreKeysCkKeyboardCkView(final Context context, final AttributeSet attrs,
                                    final int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray moreKeysKeyboardViewAttr = context.obtainStyledAttributes(attrs,
                R.styleable.MoreKeysKeyboardView, defStyle, R.style.MoreKeysKeyboardView);
        moreKeysKeyboardViewAttr.recycle();
        mKeyDetectorCk = new MoreKeysDetectorCk(getResources().getDimension(
                R.dimen.config_more_keys_keyboard_slide_allowance));
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final KeyboardCk keyboardCk = getKeyboard();
        if (keyboardCk != null) {
            final int width = keyboardCk.mOccupiedWidth + getPaddingLeft() + getPaddingRight();
            final int height = keyboardCk.mOccupiedHeight + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void setKeyboard(final KeyboardCk keyboardCk) {
        super.setKeyboard(keyboardCk);
        mKeyDetectorCk.setKeyboard(
                keyboardCk, -getPaddingLeft(), -getPaddingTop() + getVerticalCorrection());
    }

    @Override
    public void showMoreKeysPanel(final View parentView, final Controller controller,
            final int pointX, final int pointY, final KeyboardActionListener listener) {
        mController = controller;
        mListener = listener;
        final View container = getContainerView();

        final int x = pointX - getDefaultCoordX() - container.getPaddingLeft() - getPaddingLeft();
        final int y = pointY - container.getMeasuredHeight() + container.getPaddingBottom()
                + getPaddingBottom();

        parentView.getLocationInWindow(mCoordinates);
        // Ensure the horizontal position of the panel does not extend past the parentView edges.
        final int maxX = parentView.getMeasuredWidth() - container.getMeasuredWidth();
        final int panelX = Math.max(0, Math.min(maxX, x)) + CoordinateCkUtils.x(mCoordinates);
        final int panelY = y + CoordinateCkUtils.y(mCoordinates);
        container.setX(panelX);
        container.setY(panelY);

        mOriginX = x + container.getPaddingLeft();
        mOriginY = y + container.getPaddingTop();
        controller.onShowMoreKeysPanel(this);
    }


    protected int getDefaultCoordX() {
        return ((MoreKeysKeyboardCk)getKeyboard()).getDefaultCoordX();
    }

    @Override
    public void onDownEvent(final int x, final int y, final int pointerId) {
        mActivePointerId = pointerId;
        mCurrentKeyCk = detectKey(x, y);
    }

    @Override
    public void onMoveEvent(final int x, final int y, final int pointerId) {
        if (mActivePointerId != pointerId) {
            return;
        }
        final boolean hasOldKey = (mCurrentKeyCk != null);
        mCurrentKeyCk = detectKey(x, y);
        if (hasOldKey && mCurrentKeyCk == null) {
             mController.onCancelMoreKeysPanel();
        }
    }

    @Override
    public void onUpEvent(final int x, final int y, final int pointerId) {
        if (mActivePointerId != pointerId) {
            return;
        }

        mCurrentKeyCk = detectKey(x, y);
        if (mCurrentKeyCk != null) {
            updateReleaseKeyGraphics(mCurrentKeyCk);
            onKeyInput(mCurrentKeyCk);
            mCurrentKeyCk = null;
        }
    }

    protected void onKeyInput(final KeyCk keyCk) {
        final int code = keyCk.getCode();
        if (code == ConstantsCk.CODE_OUTPUT_TEXT) {
            mListener.onTextInput(mCurrentKeyCk.getOutputText());
        } else if (code != ConstantsCk.CODE_UNSPECIFIED) {
            mListener.onCodeInput(code, ConstantsCk.NOT_A_COORDINATE, ConstantsCk.NOT_A_COORDINATE, false /* isKeyRepeat */);
        }
    }

    private KeyCk detectKey(int x, int y) {
        final KeyCk oldKeyCk = mCurrentKeyCk;
        final KeyCk newKeyCk = mKeyDetectorCk.detectHitKey(x, y);
        if (newKeyCk == oldKeyCk) {
            return newKeyCk;
        }

        if (oldKeyCk != null) {
            updateReleaseKeyGraphics(oldKeyCk);
            invalidateKey(oldKeyCk);
        }
        if (newKeyCk != null) {
            updatePressKeyGraphics(newKeyCk);
            invalidateKey(newKeyCk);
        }
        return newKeyCk;
    }

    private void updateReleaseKeyGraphics(final KeyCk keyCk) {
        keyCk.onReleased();
        invalidateKey(keyCk);
    }

    private void updatePressKeyGraphics(final KeyCk keyCk) {
        keyCk.onPressed();
        invalidateKey(keyCk);
    }

    @Override
    public void dismissMoreKeysPanel() {
        if (!isShowingInParent()) {
            return;
        }
        mController.onDismissMoreKeysPanel();
    }

    @Override
    public int translateX(final int x) {
        return x - mOriginX;
    }

    @Override
    public int translateY(final int y) {
        return y - mOriginY;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent me) {
        final int action = me.getActionMasked();
        final int index = me.getActionIndex();
        final int x = (int)me.getX(index);
        final int y = (int)me.getY(index);
        final int pointerId = me.getPointerId(index);
        switch (action) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
            onDownEvent(x, y, pointerId);
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            onUpEvent(x, y, pointerId);
            break;
        case MotionEvent.ACTION_MOVE:
            onMoveEvent(x, y, pointerId);
            break;
        }
        return true;
    }

    private View getContainerView() {
        return (View)getParent();
    }

    @Override
    public void showInParent(final ViewGroup parentView) {
        removeFromParent();
        parentView.addView(getContainerView());
    }

    @Override
    public void removeFromParent() {
        final View containerView = getContainerView();
        final ViewGroup currentParent = (ViewGroup)containerView.getParent();
        if (currentParent != null) {
            currentParent.removeView(containerView);
        }
    }

    @Override
    public boolean isShowingInParent() {
        return (getContainerView().getParent() != null);
    }
}
