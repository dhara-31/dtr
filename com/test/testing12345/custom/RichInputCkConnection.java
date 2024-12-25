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

package com.test.testing12345.custom;

import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.test.testing12345.event.EventCk;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.StringCkUtils;
import com.test.testing12345.custom.common.UnicodeCkSurrogate;
import com.test.testing12345.custom.settings.SpacingAndCkPunctuations;
import com.test.testing12345.custom.utils.CapsModeCkUtils;
import com.test.testing12345.other.SelectingCkFontStyle;


public final class RichInputCkConnection {
    private static final String TAG = "RichInputConnection";
    private static final boolean DBG = false;
    private static final boolean DEBUG_PREVIOUS_TEXT = false;
    private static final boolean DEBUG_BATCH_NESTING = false;
    private static final int INVALID_CURSOR_POSITION = -1;
    boolean isDeleteOperation = false;

    private static final long SLOW_INPUT_CONNECTION_ON_FULL_RELOAD_MS = 1000;
     private static final long SLOW_INPUT_CONNECTION_ON_PARTIAL_RELOAD_MS = 200;

    private static final int OPERATION_GET_TEXT_BEFORE_CURSOR = 0;
    private static final int OPERATION_GET_TEXT_AFTER_CURSOR = 1;
    private static final int OPERATION_RELOAD_TEXT_CACHE = 3;
    private static final String[] OPERATION_NAMES = new String[] {
            "GET_TEXT_BEFORE_CURSOR",
            "GET_TEXT_AFTER_CURSOR",
            "GET_WORD_RANGE_AT_CURSOR",
            "RELOAD_TEXT_CACHE"};

     private int mExpectedSelStart = INVALID_CURSOR_POSITION; // in chars, not code points
     private int mExpectedSelEnd = INVALID_CURSOR_POSITION; // in chars, not code points
     private final StringBuilder mCommittedTextBeforeComposingText = new StringBuilder();
     private final StringBuilder mComposingText = new StringBuilder();



    private SpannableStringBuilder mTempObjectForCommitText = new SpannableStringBuilder();

    private final InputMethodService mParent;
    private InputConnection mIC;
    private int mNestLevel;

    public RichInputCkConnection(final InputMethodService parent) {
        mParent = parent;
        mIC = null;
        mNestLevel = 0;
    }

    public boolean isConnected() {
        return mIC != null;
    }

    private void checkConsistencyForDebug() {
        final ExtractedTextRequest r = new ExtractedTextRequest();
        r.hintMaxChars = 0;
        r.hintMaxLines = 0;
        r.token = 1;
        r.flags = 0;
        final ExtractedText et = mIC.getExtractedText(r, 0);
        final CharSequence beforeCursor = getTextBeforeCursor(ConstantsCk.EDITOR_CONTENTS_CACHE_SIZE,
                0);
        final StringBuilder internal = new StringBuilder(mCommittedTextBeforeComposingText)
                .append(mComposingText);
        if (null == et || null == beforeCursor) return;
        final int actualLength = Math.min(beforeCursor.length(), internal.length());
        if (internal.length() > actualLength) {
            internal.delete(0, internal.length() - actualLength);
        }
        final String reference = (beforeCursor.length() <= actualLength) ? beforeCursor.toString()
                : beforeCursor.subSequence(beforeCursor.length() - actualLength,
                        beforeCursor.length()).toString();
        if (et.selectionStart != mExpectedSelStart
                || !(reference.equals(internal.toString()))) {
            final String context = "Expected selection start = " + mExpectedSelStart
                    + "\nActual selection start = " + et.selectionStart
                    + "\nExpected text = " + internal.length() + " " + internal
                    + "\nActual text = " + reference.length() + " " + reference;
            ((CustomKeyBoard)mParent).debugDumpStateAndCrashWithException(context);
        } else {

        }
    }

    public void beginBatchEdit() {
        if (++mNestLevel == 1) {
            mIC = mParent.getCurrentInputConnection();
            if (isConnected()) {
                mIC.beginBatchEdit();
            }
        } else {
            if (DBG) {
                throw new RuntimeException("Nest level too deep");
            }
         }
        if (DEBUG_BATCH_NESTING) checkBatchEdit();
        if (DEBUG_PREVIOUS_TEXT) checkConsistencyForDebug();
    }

    public void endBatchEdit() {
         if (--mNestLevel == 0 && isConnected()) {
            mIC.endBatchEdit();
        }
        if (DEBUG_PREVIOUS_TEXT) checkConsistencyForDebug();
    }

     public boolean resetCachesUponCursorMoveAndReturnSuccess(final int newSelStart,
            final int newSelEnd) {
        mExpectedSelStart = newSelStart;
        mExpectedSelEnd = newSelEnd;
        mComposingText.setLength(0);
        final boolean didReloadTextSuccessfully = reloadTextCache();
        if (!didReloadTextSuccessfully) {
             return false;
        }
        return true;
    }

     private boolean reloadTextCache() {
        mCommittedTextBeforeComposingText.setLength(0);
        mIC = mParent.getCurrentInputConnection();

        final CharSequence textBeforeCursor = getTextBeforeCursorAndDetectLaggyConnection(
                OPERATION_RELOAD_TEXT_CACHE,
                SLOW_INPUT_CONNECTION_ON_FULL_RELOAD_MS,
                ConstantsCk.EDITOR_CONTENTS_CACHE_SIZE,
                0  );
        if (null == textBeforeCursor) {

            mExpectedSelStart = INVALID_CURSOR_POSITION;
            mExpectedSelEnd = INVALID_CURSOR_POSITION;
             return false;
        }
        mCommittedTextBeforeComposingText.append(textBeforeCursor);
        return true;
    }

    private void checkBatchEdit() {
        if (mNestLevel != 1) {

        }
    }

    public void finishComposingText() {
        if (DEBUG_BATCH_NESTING) checkBatchEdit();
        if (DEBUG_PREVIOUS_TEXT) checkConsistencyForDebug();

        mCommittedTextBeforeComposingText.append(mComposingText);
        mComposingText.setLength(0);
        if (isConnected()) {
            mIC.finishComposingText();
        }
    }

     public void commitText(final CharSequence text, final int newCursorPosition) {
        RichInputMethodCkManager.getInstance().resetSubtypeCycleOrder();
        if (DEBUG_BATCH_NESTING) checkBatchEdit();
        if (DEBUG_PREVIOUS_TEXT) checkConsistencyForDebug();
        mCommittedTextBeforeComposingText.append(text);

        if (hasCursorPosition()) {
            mExpectedSelStart += text.length() - mComposingText.length();
            mExpectedSelEnd = mExpectedSelStart;
        }
        mComposingText.setLength(0);
        if (isConnected()) {
            mTempObjectForCommitText.clear();
            mTempObjectForCommitText.append(text);
            final CharacterStyle[] spans = mTempObjectForCommitText.getSpans(
                    0, text.length(), CharacterStyle.class);
            for (final CharacterStyle span : spans) {
                final int spanStart = mTempObjectForCommitText.getSpanStart(span);
                final int spanEnd = mTempObjectForCommitText.getSpanEnd(span);
                final int spanFlags = mTempObjectForCommitText.getSpanFlags(span);

                if (0 < spanEnd && spanEnd < mTempObjectForCommitText.length()) {
                    final char spanEndChar = mTempObjectForCommitText.charAt(spanEnd - 1);
                    final char nextChar = mTempObjectForCommitText.charAt(spanEnd);
                    if (UnicodeCkSurrogate.isLowSurrogate(spanEndChar)
                            && UnicodeCkSurrogate.isHighSurrogate(nextChar)) {
                        mTempObjectForCommitText.setSpan(span, spanStart, spanEnd + 1, spanFlags);
                    }
                }
            }
            mIC.commitText(mTempObjectForCommitText, newCursorPosition);
        }
    }

    public CharSequence getSelectedText(final int flags) {
        return isConnected() ?  mIC.getSelectedText(flags) : null;
    }

    public boolean canDeleteCharacters() {
        return mExpectedSelStart > 0;
    }

     public int getCursorCapsMode(final int inputType, final SpacingAndCkPunctuations spacingAndCkPunctuations) {
        mIC = mParent.getCurrentInputConnection();
        if (!isConnected()) {
            return ConstantsCk.TextUtils.CAP_MODE_OFF;
        }
        if (!TextUtils.isEmpty(mComposingText)) {

            return TextUtils.CAP_MODE_CHARACTERS & inputType;
        }

        if (TextUtils.isEmpty(mCommittedTextBeforeComposingText) && 0 != mExpectedSelStart) {
            if (!reloadTextCache()) {

            }
        }

        return CapsModeCkUtils.getCapsMode(mCommittedTextBeforeComposingText.toString(), inputType,
                spacingAndCkPunctuations);
    }

    public int getCodePointBeforeCursor() {
        final int length = mCommittedTextBeforeComposingText.length();
        if (length < 1) return ConstantsCk.NOT_A_CODE;
        return Character.codePointBefore(mCommittedTextBeforeComposingText, length);
    }

    public CharSequence getTextBeforeCursor(final int n, final int flags) {
        final int cachedLength =
                mCommittedTextBeforeComposingText.length() + mComposingText.length();

        if (INVALID_CURSOR_POSITION != mExpectedSelStart
                && (cachedLength >= n || cachedLength >= mExpectedSelStart)) {
            final StringBuilder s = new StringBuilder(mCommittedTextBeforeComposingText);

            s.append(mComposingText.toString());
            if (s.length() > n) {
                s.delete(0, s.length() - n);
            }
            return s;
        }
        return getTextBeforeCursorAndDetectLaggyConnection(
                OPERATION_GET_TEXT_BEFORE_CURSOR,
                SLOW_INPUT_CONNECTION_ON_PARTIAL_RELOAD_MS,
                n, flags);
    }

    public CharSequence getTextAfterCursor(final int n, final int flags) {
        return getTextAfterCursorAndDetectLaggyConnection(
                OPERATION_GET_TEXT_AFTER_CURSOR,
                SLOW_INPUT_CONNECTION_ON_PARTIAL_RELOAD_MS,
                n, flags);
    }

    private CharSequence getTextBeforeCursorAndDetectLaggyConnection(
            final int operation, final long timeout, final int n, final int flags) {
        mIC = mParent.getCurrentInputConnection();
        if (!isConnected()) {
            return null;
        }
        final long startTime = SystemClock.uptimeMillis();
        final CharSequence result = mIC.getTextBeforeCursor(n, flags);
        detectLaggyConnection(operation, timeout, startTime);
        return result;
    }

    private CharSequence getTextAfterCursorAndDetectLaggyConnection(
            final int operation, final long timeout, final int n, final int flags) {
        mIC = mParent.getCurrentInputConnection();
        if (!isConnected()) {
            return null;
        }
        final long startTime = SystemClock.uptimeMillis();
        final CharSequence result = mIC.getTextAfterCursor(n, flags);
        detectLaggyConnection(operation, timeout, startTime);
        return result;
    }

    private void detectLaggyConnection(final int operation, final long timeout, final long startTime) {
        final long duration = SystemClock.uptimeMillis() - startTime;
        if (duration >= timeout) {
            final String operationName = OPERATION_NAMES[operation];
            Log.w(TAG, "Slow InputConnection: " + operationName + " took " + duration + " ms.");
        }
    }

    public void deleteTextBeforeCursor(final int beforeLength) {
        if (DEBUG_BATCH_NESTING) checkBatchEdit();

        final int remainingChars = mComposingText.length() - beforeLength;
        if (remainingChars >= 0) {
            mComposingText.setLength(remainingChars);
        } else {
            mComposingText.setLength(0);

            final int len = Math.max(mCommittedTextBeforeComposingText.length()
                    + remainingChars, 0);
            mCommittedTextBeforeComposingText.setLength(len);
        }
        if (mExpectedSelStart > beforeLength) {
            mExpectedSelStart -= beforeLength;
            mExpectedSelEnd -= beforeLength;
        } else {

            mExpectedSelEnd -= mExpectedSelStart;
            mExpectedSelStart = 0;
        }
        if (isConnected()) {
            mIC.deleteSurroundingText(beforeLength, 0);
        }
        if (DEBUG_PREVIOUS_TEXT) checkConsistencyForDebug();
    }

    public void performEditorAction(final int actionId) {
        mIC = mParent.getCurrentInputConnection();
        if (isConnected()) {
            mIC.performEditorAction(actionId);
        }
    }

    public void sendKeyEvent(final KeyEvent keyEvent, EventCk eventCk) {

        String str;
         if (keyEvent.getAction() == 0) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode != 0) {
                if (keyCode == 66) {
                    this.mCommittedTextBeforeComposingText.append("\n");
                    int i = this.mExpectedSelStart + 1;
                    this.mExpectedSelStart = i;
                    this.mExpectedSelEnd = i;
                } else if (keyCode != 67) {
                    StringCkUtils.newSingleCodePointString(keyEvent.getUnicodeChar());
                    if (eventCk == null) {
                        str = StringCkUtils.newSingleCodePointString(keyEvent.getUnicodeChar());
                    } else {
                        str = String.valueOf(SelectingCkFontStyle.changingFont(eventCk));
                    }
                     this.mCommittedTextBeforeComposingText.append(str);
                    int length = this.mExpectedSelStart + str.length();
                    this.mExpectedSelStart = length;
                    this.mExpectedSelEnd = length;
                    if (!ConstantsCk.isNumericKeyboard) {
                        commitText(str, 0);
                    }
                } else {
                    this.isDeleteOperation = true;
                    if (this.mComposingText.length() != 0) {

                        StringBuilder sb = this.mComposingText;
                        sb.delete(sb.length() - 1, this.mComposingText.length());
                    } else if (this.mCommittedTextBeforeComposingText.length() > 0) {

                        StringBuilder sb2 = this.mCommittedTextBeforeComposingText;
                        sb2.delete(sb2.length() - 1, this.mCommittedTextBeforeComposingText.length());
                    }
                    int i2 = this.mExpectedSelStart;
                    if (i2 > 0 && i2 == this.mExpectedSelEnd) {
                        this.mExpectedSelStart = i2 - 1;
                    }
                    this.mExpectedSelEnd = this.mExpectedSelStart;
                }
            } else if (keyEvent.getCharacters() != null) {
                this.mCommittedTextBeforeComposingText.append(keyEvent.getCharacters());
                int length2 = this.mExpectedSelStart + keyEvent.getCharacters().length();
                this.mExpectedSelStart = length2;
                this.mExpectedSelEnd = length2;
            }

        }
        if (!isConnected()) {
            return;
        }
        if (ConstantsCk.isNumericKeyboard || this.isDeleteOperation) {
            this.isDeleteOperation = false;
            this.mIC.sendKeyEvent(keyEvent);
        }
    }

     public void setSelection(int start, int end) {
        if (DEBUG_BATCH_NESTING) checkBatchEdit();
        if (DEBUG_PREVIOUS_TEXT) checkConsistencyForDebug();
        if (start < 0 || end < 0) {
            return;
        }
        if (mExpectedSelStart == start && mExpectedSelEnd == end) {
            return;
        }

        mExpectedSelStart = start;
        mExpectedSelEnd = end;
        if (isConnected()) {
            final boolean isIcValid = mIC.setSelection(start, end);
            if (!isIcValid) {
                return;
            }
        }
        reloadTextCache();
    }

    public int getExpectedSelectionStart() {
        return mExpectedSelStart;
    }

    public int getExpectedSelectionEnd() {
        return mExpectedSelEnd;
    }

     public boolean hasSelection() {
        return mExpectedSelEnd != mExpectedSelStart;
    }

    public boolean hasCursorPosition() {
        return mExpectedSelStart != INVALID_CURSOR_POSITION && mExpectedSelEnd != INVALID_CURSOR_POSITION;
    }

     public int getUnicodeSteps(int chars, boolean rightSidePointer) {
        int steps = 0;
        if (chars < 0) {
            CharSequence charsBeforeCursor = rightSidePointer && hasSelection() ?
                    getSelectedText(0) :
                    getTextBeforeCursor(-chars * 2, 0);
            if (charsBeforeCursor != null) {
                for (int i = charsBeforeCursor.length() - 1; i >= 0 && chars < 0; i--, chars++, steps--) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Character.isSurrogate(charsBeforeCursor.charAt(i))) {
                            steps--;
                            i--;
                        }
                    }
                }
            }
        } else if (chars > 0) {
            CharSequence charsAfterCursor = !rightSidePointer && hasSelection() ?
                    getSelectedText(0) :
                    getTextAfterCursor(chars * 2, 0);
            if (charsAfterCursor != null) {
                for (int i = 0; i < charsAfterCursor.length() && chars > 0; i++, chars--, steps++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Character.isSurrogate(charsAfterCursor.charAt(i))) {
                            steps++;
                            i++;
                        }
                    }
                }
            }
        }
        return steps;
    }
}
