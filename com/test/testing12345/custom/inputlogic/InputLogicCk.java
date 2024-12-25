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

package com.test.testing12345.custom.inputlogic;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import java.util.TreeSet;

import com.test.testing12345.event.EventCk;
import com.test.testing12345.event.InputTransactionCk;
import com.test.testing12345.custom.CustomKeyBoard;
import com.test.testing12345.custom.RichInputCkConnection;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.common.StringCkUtils;
import com.test.testing12345.custom.settings.SettingsCkValues;
import com.test.testing12345.custom.utils.InputTypeCkUtils;
import com.test.testing12345.custom.utils.RecapitalizeCkStatus;
import com.test.testing12345.custom.utils.SubtypeLocaleCkUtils;
import com.test.testing12345.other.SelectingCkFontStyle;

public final class InputLogicCk {
     final CustomKeyBoard mCustomKeyBoard;

     public final RichInputCkConnection mConnection;
    private final RecapitalizeCkStatus mRecapitalizeCkStatus = new RecapitalizeCkStatus();

    public final TreeSet<Long> mCurrentlyPressedHardwareKeys = new TreeSet<>();


    public InputLogicCk(final CustomKeyBoard customKeyBoard) {
        mCustomKeyBoard = customKeyBoard;
        mConnection = new RichInputCkConnection(customKeyBoard);
    }

     public void startInput() {
        mRecapitalizeCkStatus.disable(); // Do not perform recapitalize until the cursor is moved once
        mCurrentlyPressedHardwareKeys.clear();
    }

     public void onSubtypeChanged() {
        startInput();
    }

     public InputTransactionCk onTextInput(final SettingsCkValues settingsCkValues, final EventCk eventCk) {
        final String rawText = eventCk.getTextToCommit().toString();
        final InputTransactionCk inputTransactionCk = new InputTransactionCk(settingsCkValues);
        mConnection.beginBatchEdit();
        final String text = performSpecificTldProcessingOnTextInput(rawText);
        mConnection.commitText(text, 1);
        mConnection.endBatchEdit();
        // Space state must be updated before calling updateShiftState
        inputTransactionCk.requireShiftUpdate(InputTransactionCk.SHIFT_UPDATE_NOW);
        return inputTransactionCk;
    }

     public boolean onUpdateSelection(final int newSelStart, final int newSelEnd) {
        resetEntireInputState(newSelStart, newSelEnd);

         mRecapitalizeCkStatus.enable();
         mRecapitalizeCkStatus.stop();
        return true;
    }

     public InputTransactionCk onCodeInput(final SettingsCkValues settingsCkValues, final EventCk eventCk) {
        final InputTransactionCk inputTransactionCk = new InputTransactionCk(settingsCkValues);
        mConnection.beginBatchEdit();

        EventCk currentEventCk = eventCk;
        while (null != currentEventCk) {
            if (currentEventCk.isConsumed()) {
                handleConsumedEvent(currentEventCk);
            } else if (currentEventCk.isFunctionalKeyEvent()) {
                handleFunctionalEvent(currentEventCk, inputTransactionCk);
            } else {
                handleNonFunctionalEvent(currentEventCk, inputTransactionCk);
            }
            currentEventCk = currentEventCk.mNextEventCk;
        }
        mConnection.endBatchEdit();
        return inputTransactionCk;
    }

     private void handleConsumedEvent(final EventCk eventCk) {



        final CharSequence textToCommit = eventCk.getTextToCommit();
        if (!TextUtils.isEmpty(textToCommit)) {
            mConnection.commitText(textToCommit, 1);
        }
    }

     private void handleFunctionalEvent(final EventCk eventCk, final InputTransactionCk inputTransactionCk) {
        switch (eventCk.mKeyCode) {
            case ConstantsCk.CODE_DELETE:
                handleBackspaceEvent(eventCk, inputTransactionCk);
                 break;
            case ConstantsCk.CODE_SHIFT:
                performRecapitalization(inputTransactionCk.mSettingsCkValues);
                inputTransactionCk.requireShiftUpdate(InputTransactionCk.SHIFT_UPDATE_NOW);
                break;
            case ConstantsCk.CODE_CAPSLOCK:

                break;
            case ConstantsCk.CODE_SYMBOL_SHIFT:

                break;
            case ConstantsCk.CODE_SWITCH_ALPHA_SYMBOL:

                break;
            case ConstantsCk.CODE_SETTINGS:
                onSettingsKeyPressed();
                break;
            case ConstantsCk.CODE_ACTION_NEXT:
                performEditorAction(EditorInfo.IME_ACTION_NEXT);
                break;
            case ConstantsCk.CODE_ACTION_PREVIOUS:
                performEditorAction(EditorInfo.IME_ACTION_PREVIOUS);
                break;
            case ConstantsCk.CODE_LANGUAGE_SWITCH:
                handleLanguageSwitchKey();
                break;
            case ConstantsCk.CODE_SHIFT_ENTER:
                final EventCk tmpEventCk = EventCk.createSoftwareKeypressEvent(ConstantsCk.CODE_ENTER,
                        eventCk.mKeyCode, eventCk.mX, eventCk.mY, eventCk.isKeyRepeat());
                handleNonSpecialCharacterEvent(tmpEventCk, inputTransactionCk);

                break;
            case ConstantsCk.CODE_NUMBER:

                break;
            case ConstantsCk.CODE_SYMBOL:


                break;
            default:
                throw new RuntimeException("Unknown key code : " + eventCk.mKeyCode);
        }
    }

     private void handleNonFunctionalEvent(final EventCk eventCk,
            final InputTransactionCk inputTransactionCk) {
        switch (eventCk.mCodePoint) {
            case ConstantsCk.CODE_ENTER:
                final EditorInfo editorInfo = getCurrentInputEditorInfo();
                final int imeOptionsActionId =
                        InputTypeCkUtils.getImeOptionsActionIdFromEditorInfo(editorInfo);
                if (InputTypeCkUtils.IME_ACTION_CUSTOM_LABEL == imeOptionsActionId) {

                    performEditorAction(editorInfo.actionId);
                } else if (EditorInfo.IME_ACTION_NONE != imeOptionsActionId) {

                    performEditorAction(imeOptionsActionId);
                } else {
                     handleNonSpecialCharacterEvent(eventCk, inputTransactionCk);
                }
                break;
            default:
                handleNonSpecialCharacterEvent(eventCk, inputTransactionCk);
                break;
        }
    }

     private void handleNonSpecialCharacterEvent(final EventCk eventCk,
            final InputTransactionCk inputTransactionCk) {
        final int codePoint = eventCk.mCodePoint;
        if (inputTransactionCk.mSettingsCkValues.isWordSeparator(codePoint)
                || Character.getType(codePoint) == Character.OTHER_SYMBOL) {
            handleSeparatorEvent(eventCk, inputTransactionCk);
        } else {
            handleNonSeparatorEvent(eventCk);
        }
    }

     private void handleNonSeparatorEvent(final EventCk eventCk) {
        sendKeyCodePoint(eventCk.mCodePoint, eventCk);
    }

     private void handleSeparatorEvent(final EventCk eventCk, final InputTransactionCk inputTransactionCk) {
        sendKeyCodePoint(eventCk.mCodePoint, eventCk);

        inputTransactionCk.requireShiftUpdate(InputTransactionCk.SHIFT_UPDATE_NOW);
    }



    private void handleBackspaceEvent(final EventCk eventCk, final InputTransactionCk inputTransactionCk) {

        final int shiftUpdateKind =
                eventCk.isKeyRepeat() && mConnection.getExpectedSelectionStart() > 0
                ? InputTransactionCk.SHIFT_UPDATE_LATER : InputTransactionCk.SHIFT_UPDATE_NOW;
        inputTransactionCk.requireShiftUpdate(shiftUpdateKind);


        if (mConnection.hasSelection()) {

            final int numCharsDeleted = mConnection.getExpectedSelectionEnd()
                    - mConnection.getExpectedSelectionStart();
            mConnection.setSelection(mConnection.getExpectedSelectionEnd(),
                    mConnection.getExpectedSelectionEnd());
            mConnection.deleteTextBeforeCursor(numCharsDeleted);
        } else {
             if (inputTransactionCk.mSettingsCkValues.mInputCkAttributes.isTypeNull()
                    || ConstantsCk.NOT_A_CURSOR_POSITION
                            == mConnection.getExpectedSelectionEnd()) {

                sendDownUpKeyEvent(KeyEvent.KEYCODE_DEL, eventCk);
            } else {
                final int codePointBeforeCursor = mConnection.getCodePointBeforeCursor();
                if (codePointBeforeCursor == ConstantsCk.NOT_A_CODE) {

                    mConnection.deleteTextBeforeCursor(1);
                     return;
                }
                final int lengthToDelete =
                        Character.isSupplementaryCodePoint(codePointBeforeCursor) ? 2 : 1;
                mConnection.deleteTextBeforeCursor(lengthToDelete);
            }
        }
    }


    private void handleLanguageSwitchKey() {
        mCustomKeyBoard.switchToNextSubtype();
    }


    private void performRecapitalization(final SettingsCkValues settingsCkValues) {
        if (!mConnection.hasSelection() || !mRecapitalizeCkStatus.mIsEnabled()) {
            return;
        }
        final int selectionStart = mConnection.getExpectedSelectionStart();
        final int selectionEnd = mConnection.getExpectedSelectionEnd();
        final int numCharsSelected = selectionEnd - selectionStart;
        if (numCharsSelected > ConstantsCk.MAX_CHARACTERS_FOR_RECAPITALIZATION) {

            return;
        }

        if (!mRecapitalizeCkStatus.isStarted()
                || !mRecapitalizeCkStatus.isSetAt(selectionStart, selectionEnd)) {
            final CharSequence selectedText =
                    mConnection.getSelectedText(0  );
            if (TextUtils.isEmpty(selectedText)) return;
            mRecapitalizeCkStatus.start(selectionStart, selectionEnd, selectedText.toString(), mCustomKeyBoard.getCurrentLayoutLocale());
             mRecapitalizeCkStatus.trim();
        }
        mConnection.finishComposingText();
        mRecapitalizeCkStatus.rotate();
        mConnection.setSelection(selectionEnd, selectionEnd);
        mConnection.deleteTextBeforeCursor(numCharsSelected);
        mConnection.commitText(mRecapitalizeCkStatus.getRecapitalizedString(), 0);
        mConnection.setSelection(mRecapitalizeCkStatus.getNewCursorStart(),
                mRecapitalizeCkStatus.getNewCursorEnd());
    }

     public int getCurrentAutoCapsState(final SettingsCkValues settingsCkValues,
                                       final String layoutSetName) {
        if (!settingsCkValues.mAutoCap || !layoutUsesAutoCaps(layoutSetName)) {
            return ConstantsCk.TextUtils.CAP_MODE_OFF;
        }

        final EditorInfo ei = getCurrentInputEditorInfo();
        if (ei == null) return ConstantsCk.TextUtils.CAP_MODE_OFF;
        final int inputType = ei.inputType;

        return mConnection.getCursorCapsMode(inputType, settingsCkValues.mSpacingAndCkPunctuations);
    }

    private boolean layoutUsesAutoCaps(final String layoutSetName) {
        switch (layoutSetName) {
            case SubtypeLocaleCkUtils.LAYOUT_ARABIC:
            case SubtypeLocaleCkUtils.LAYOUT_BENGALI:
            case SubtypeLocaleCkUtils.LAYOUT_BENGALI_AKKHOR:
            case SubtypeLocaleCkUtils.LAYOUT_FARSI:
            case SubtypeLocaleCkUtils.LAYOUT_GEORGIAN:
            case SubtypeLocaleCkUtils.LAYOUT_HEBREW:
            case SubtypeLocaleCkUtils.LAYOUT_HINDI:
            case SubtypeLocaleCkUtils.LAYOUT_HINDI_COMPACT:
            case SubtypeLocaleCkUtils.LAYOUT_KANNADA:
            case SubtypeLocaleCkUtils.LAYOUT_KHMER:
            case SubtypeLocaleCkUtils.LAYOUT_LAO:
            case SubtypeLocaleCkUtils.LAYOUT_MALAYALAM:
            case SubtypeLocaleCkUtils.LAYOUT_MARATHI:
            case SubtypeLocaleCkUtils.LAYOUT_NEPALI_ROMANIZED:
            case SubtypeLocaleCkUtils.LAYOUT_NEPALI_TRADITIONAL:
            case SubtypeLocaleCkUtils.LAYOUT_TAMIL:
            case SubtypeLocaleCkUtils.LAYOUT_TELUGU:
            case SubtypeLocaleCkUtils.LAYOUT_THAI:
            case SubtypeLocaleCkUtils.LAYOUT_URDU:
                return false;
            default:
                return true;
        }
    }

    public int getCurrentRecapitalizeState() {
        if (!mRecapitalizeCkStatus.isStarted()
                || !mRecapitalizeCkStatus.isSetAt(mConnection.getExpectedSelectionStart(),
                        mConnection.getExpectedSelectionEnd())) {

            return RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE;
        }
        return mRecapitalizeCkStatus.getCurrentMode();
    }

     private EditorInfo getCurrentInputEditorInfo() {
        return mCustomKeyBoard.getCurrentInputEditorInfo();
    }

     private void performEditorAction(final int actionId) {
        mConnection.performEditorAction(actionId);
    }

     private String performSpecificTldProcessingOnTextInput(final String text) {
        if (text.length() <= 1 || text.charAt(0) != ConstantsCk.CODE_PERIOD
                || !Character.isLetter(text.charAt(1))) {
            // Not a tld: do nothing.
            return text;
        }
        final int codePointBeforeCursor = mConnection.getCodePointBeforeCursor();
        // If no code point, #getCodePointBeforeCursor returns NOT_A_CODE_POINT.
        if (ConstantsCk.CODE_PERIOD == codePointBeforeCursor) {
            return text.substring(1);
        }
        return text;
    }

     private void onSettingsKeyPressed() {
        mCustomKeyBoard.launchSettings();
    }


    private void resetEntireInputState(final int newSelStart, final int newSelEnd) {
        mConnection.resetCachesUponCursorMoveAndReturnSuccess(newSelStart, newSelEnd);
    }

     public void sendDownUpKeyEvent(final int keyCode, EventCk eventCk) {
        final long eventTime = SystemClock.uptimeMillis();
        mConnection.sendKeyEvent(new KeyEvent(eventTime, eventTime,
                KeyEvent.ACTION_DOWN, keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE), eventCk);
        mConnection.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), eventTime,
                KeyEvent.ACTION_UP, keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE), eventCk);
    }

     private void sendKeyCodePoint(final int codePoint, EventCk eventCk) {
         if (codePoint >= '0' && codePoint <= '9') {
            sendDownUpKeyEvent(codePoint - '0' + KeyEvent.KEYCODE_0, eventCk);
            return;
        }

         else if (Character.charCount(codePoint) == 1) {
            this.mConnection.commitText(    SelectingCkFontStyle.changingFont(eventCk), 1);
        } else {
            this.mConnection.commitText(StringCkUtils.newSingleCodePointString(codePoint), 1);
        }
    }

     public boolean retryResetCachesAndReturnSuccess(final boolean tryResumeSuggestions,
            final int remainingTries, final CustomKeyBoard.UIHandlerCk handler) {
        if (!mConnection.resetCachesUponCursorMoveAndReturnSuccess(
                mConnection.getExpectedSelectionStart(), mConnection.getExpectedSelectionEnd())) {
            if (0 < remainingTries) {
                handler.postResetCaches(tryResumeSuggestions, remainingTries - 1);
                return false;
            }
         }
        return true;
    }
}
