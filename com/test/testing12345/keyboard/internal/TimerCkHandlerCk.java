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

import android.os.Message;
import android.view.ViewConfiguration;

import com.test.testing12345.keyboard.KeyCk;
import com.test.testing12345.keyboard.PointerCkTracker;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.utils.LeakGuardHandlerCkWrapper;

public final class TimerCkHandlerCk extends LeakGuardHandlerCkWrapper<DrawingProxy>
        implements TimerCkProxy {
    private static final int MSG_TYPING_STATE_EXPIRED = 0;
    private static final int MSG_REPEAT_KEY = 1;
    private static final int MSG_LONGPRESS_KEY = 2;
    private static final int MSG_LONGPRESS_SHIFT_KEY = 3;
    private static final int MSG_DOUBLE_TAP_SHIFT_KEY = 4;
    private static final int MSG_UPDATE_BATCH_INPUT = 5;
    private static final int MSG_DISMISS_KEY_PREVIEW = 6;
    private static final int MSG_DISMISS_GESTURE_FLOATING_PREVIEW_TEXT = 7;

    private final int mIgnoreAltCodeKeyTimeout;

    public TimerCkHandlerCk(final DrawingProxy ownerInstance, final int ignoreAltCodeKeyTimeout) {
        super(ownerInstance);
        mIgnoreAltCodeKeyTimeout = ignoreAltCodeKeyTimeout;
    }

    @Override
    public void handleMessage(final Message msg) {
        final DrawingProxy drawingProxy = getOwnerInstance();
        if (drawingProxy == null) {
            return;
        }
        switch (msg.what) {
        case MSG_REPEAT_KEY:
            final PointerCkTracker tracker1 = (PointerCkTracker) msg.obj;
            tracker1.onKeyRepeat(msg.arg1 /* code */, msg.arg2 /* repeatCount */);
            break;
        case MSG_LONGPRESS_KEY:
        case MSG_LONGPRESS_SHIFT_KEY:
            cancelLongPressTimers();
            final PointerCkTracker tracker2 = (PointerCkTracker) msg.obj;
            tracker2.onLongPressed();
            break;
        case MSG_DISMISS_KEY_PREVIEW:
            drawingProxy.onKeyReleased((KeyCk) msg.obj, false /* withAnimation */);
            break;
        }
    }

    @Override
    public void startKeyRepeatTimerOf(final PointerCkTracker tracker, final int repeatCount,
                                      final int delay) {
        final KeyCk keyCk = tracker.getKey();
        if (keyCk == null || delay == 0) {
            return;
        }
        sendMessageDelayed(
                obtainMessage(MSG_REPEAT_KEY, keyCk.getCode(), repeatCount, tracker), delay);
    }

    private void cancelKeyRepeatTimerOf(final PointerCkTracker tracker) {
        removeMessages(MSG_REPEAT_KEY, tracker);
    }

    public void cancelKeyRepeatTimers() {
        removeMessages(MSG_REPEAT_KEY);
    }

    // TODO: Suppress layout changes in key repeat mode
    public boolean isInKeyRepeat() {
        return hasMessages(MSG_REPEAT_KEY);
    }

    @Override
    public void startLongPressTimerOf(final PointerCkTracker tracker, final int delay) {
        final KeyCk keyCk = tracker.getKey();
        if (keyCk == null) {
            return;
        }

        final int messageId = (keyCk.getCode() == ConstantsCk.CODE_SHIFT)
                ? MSG_LONGPRESS_SHIFT_KEY : MSG_LONGPRESS_KEY;
        sendMessageDelayed(obtainMessage(messageId, tracker), delay);
    }

    @Override
    public void cancelLongPressTimersOf(final PointerCkTracker tracker) {
        removeMessages(MSG_LONGPRESS_KEY, tracker);
        removeMessages(MSG_LONGPRESS_SHIFT_KEY, tracker);
    }

    @Override
    public void cancelLongPressShiftKeyTimer() {
        removeMessages(MSG_LONGPRESS_SHIFT_KEY);
    }

    public void cancelLongPressTimers() {
        removeMessages(MSG_LONGPRESS_KEY);
        removeMessages(MSG_LONGPRESS_SHIFT_KEY);
    }

    @Override
    public void startTypingStateTimer(final KeyCk typedKeyCk) {
        if (typedKeyCk.isModifier() || typedKeyCk.altCodeWhileTyping()) {
            return;
        }

        final boolean isTyping = isTypingState();
        removeMessages(MSG_TYPING_STATE_EXPIRED);
        final DrawingProxy drawingProxy = getOwnerInstance();
        if (drawingProxy == null) {
            return;
        }

         final int typedCode = typedKeyCk.getCode();
        if (typedCode == ConstantsCk.CODE_SPACE || typedCode == ConstantsCk.CODE_ENTER) {
            if (isTyping) {
                drawingProxy.startWhileTypingAnimation(DrawingProxy.FADE_IN);
            }
            return;
        }

        sendMessageDelayed(
                obtainMessage(MSG_TYPING_STATE_EXPIRED), mIgnoreAltCodeKeyTimeout);
        if (isTyping) {
            return;
        }
        drawingProxy.startWhileTypingAnimation(DrawingProxy.FADE_OUT);
    }

    @Override
    public boolean isTypingState() {
        return hasMessages(MSG_TYPING_STATE_EXPIRED);
    }

    @Override
    public void startDoubleTapShiftKeyTimer() {
        sendMessageDelayed(obtainMessage(MSG_DOUBLE_TAP_SHIFT_KEY),
                ViewConfiguration.getDoubleTapTimeout());
    }

    @Override
    public void cancelDoubleTapShiftKeyTimer() {
        removeMessages(MSG_DOUBLE_TAP_SHIFT_KEY);
    }

    @Override
    public boolean isInDoubleTapShiftKeyTimeout() {
        return hasMessages(MSG_DOUBLE_TAP_SHIFT_KEY);
    }

    @Override
    public void cancelKeyTimersOf(final PointerCkTracker tracker) {
        cancelKeyRepeatTimerOf(tracker);
        cancelLongPressTimersOf(tracker);
    }

    public void cancelAllKeyTimers() {
        cancelKeyRepeatTimers();
        cancelLongPressTimers();
    }

    @Override
    public void cancelUpdateBatchInputTimer(final PointerCkTracker tracker) {
        removeMessages(MSG_UPDATE_BATCH_INPUT, tracker);
    }

    @Override
    public void cancelAllUpdateBatchInputTimers() {
        removeMessages(MSG_UPDATE_BATCH_INPUT);
    }

    public void postDismissKeyPreview(final KeyCk keyCk, final long delay) {
        sendMessageDelayed(obtainMessage(MSG_DISMISS_KEY_PREVIEW, keyCk), delay);
    }

    public void cancelAllMessages() {
        cancelAllKeyTimers();
        cancelAllUpdateBatchInputTimers();
        removeMessages(MSG_DISMISS_KEY_PREVIEW);
        removeMessages(MSG_DISMISS_GESTURE_FLOATING_PREVIEW_TEXT);
    }
}
