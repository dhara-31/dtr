
package com.test.testing12345.keyboard.internal;

import android.text.TextUtils;

import com.test.testing12345.event.EventCk;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.utils.CapsModeCkUtils;
import com.test.testing12345.custom.utils.RecapitalizeCkStatus;


public final class KeyboardCkState {
    private static final String TAG = KeyboardCkState.class.getSimpleName();
    private static final boolean DEBUG_EVENT = false;
    private static final boolean DEBUG_INTERNAL_ACTION = false;

    public interface SwitchActions {
        boolean DEBUG_ACTION = false;

        void setAlphabetKeyboard();
        void setAlphabetManualShiftedKeyboard();
        void setAlphabetAutomaticShiftedKeyboard();
        void setAlphabetShiftLockedKeyboard();
        void setSymbolsKeyboard();
        void setSymbolsShiftedKeyboard();

        void requestUpdatingShiftState(final int autoCapsFlags, final int recapitalizeMode);

        boolean DEBUG_TIMER_ACTION = false;

        void startDoubleTapShiftKeyTimer();
        boolean isInDoubleTapShiftKeyTimeout();
        void cancelDoubleTapShiftKeyTimer();

        void setBigNumKeyboard();
    }

    private final SwitchActions mSwitchActions;

    private ShiftKeyCkState mShiftKeyState = new ShiftKeyCkState("Shift");
    private ModifierKeyCkState mSymbolKeyState = new ModifierKeyCkState("Symbol");

    // TODO: Merge {@link #mSwitchState}, {@link #mIsAlphabetMode}, {@link #mAlphabetShiftState},

    private static final int SWITCH_STATE_ALPHA = 0;
    private static final int SWITCH_STATE_SYMBOL_BEGIN = 1;
    private static final int SWITCH_STATE_SYMBOL = 2;
    private static final int SWITCH_STATE_MOMENTARY_ALPHA_AND_SYMBOL = 3;
    private static final int SWITCH_STATE_MOMENTARY_SYMBOL_AND_MORE = 4;
    private int mSwitchState = SWITCH_STATE_ALPHA;

    private boolean mIsAlphabetMode;
    private AlphabetShiftStateCk mAlphabetShiftStateCk = new AlphabetShiftStateCk();
    private boolean mIsSymbolShifted;
    private boolean mPrevMainKeyboardWasShiftLocked;
    private boolean mPrevSymbolsKeyboardWasShifted;
    private int mRecapitalizeMode;

     private boolean mIsInAlphabetUnshiftedFromShifted;
    private boolean mIsInDoubleTapShiftKey;

    private final SavedKeyboardState mSavedKeyboardState = new SavedKeyboardState();

    static final class SavedKeyboardState {
        public boolean mIsValid;
        public boolean mIsAlphabetMode;
        public boolean mIsAlphabetShiftLocked;
        public int mShiftMode;

        @Override
        public String toString() {
            if (!mIsValid) {
                return "INVALID";
            }
            if (mIsAlphabetMode) {
                return mIsAlphabetShiftLocked ? "ALPHABET_SHIFT_LOCKED"
                        : "ALPHABET_" + shiftModeToString(mShiftMode);
            }
            return "SYMBOLS_" + shiftModeToString(mShiftMode);
        }
    }

    public KeyboardCkState(final SwitchActions switchActions) {
        mSwitchActions = switchActions;
        mRecapitalizeMode = RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE;
    }

    public void onLoadKeyboard(final int autoCapsFlags, final int recapitalizeMode) {

        mAlphabetShiftStateCk.setShiftLocked(false);
        mPrevMainKeyboardWasShiftLocked = false;
        mPrevSymbolsKeyboardWasShifted = false;
        mShiftKeyState.onRelease();
        mSymbolKeyState.onRelease();
        if (mSavedKeyboardState.mIsValid) {
            onRestoreKeyboardState(autoCapsFlags, recapitalizeMode);
            mSavedKeyboardState.mIsValid = false;
        } else {
             setAlphabetKeyboard(autoCapsFlags, recapitalizeMode);
        }
    }

     private static final int UNSHIFT = 0;
    private static final int MANUAL_SHIFT = 1;
    private static final int AUTOMATIC_SHIFT = 2;
    private static final int SHIFT_LOCK_SHIFTED = 3;

    public void onSaveKeyboardState() {
        final SavedKeyboardState state = mSavedKeyboardState;
        state.mIsAlphabetMode = mIsAlphabetMode;
        if (mIsAlphabetMode) {
            state.mIsAlphabetShiftLocked = mAlphabetShiftStateCk.isShiftLocked();
            state.mShiftMode = mAlphabetShiftStateCk.isAutomaticShifted() ? AUTOMATIC_SHIFT
                    : (mAlphabetShiftStateCk.isShiftedOrShiftLocked() ? MANUAL_SHIFT : UNSHIFT);
        } else {
            state.mIsAlphabetShiftLocked = mPrevMainKeyboardWasShiftLocked;
            state.mShiftMode = mIsSymbolShifted ? MANUAL_SHIFT : UNSHIFT;
        }
        state.mIsValid = true;

    }

    private void onRestoreKeyboardState(final int autoCapsFlags, final int recapitalizeMode) {
        final SavedKeyboardState state = mSavedKeyboardState;

        mPrevMainKeyboardWasShiftLocked = state.mIsAlphabetShiftLocked;
        if (state.mIsAlphabetMode) {
            setAlphabetKeyboard(autoCapsFlags, recapitalizeMode);
            setShiftLocked(state.mIsAlphabetShiftLocked);
            if (!state.mIsAlphabetShiftLocked) {
                setShifted(state.mShiftMode);
            }
            return;
        }
         if (state.mShiftMode == MANUAL_SHIFT) {
            setSymbolsShiftedKeyboard();
        } else {
            setSymbolsKeyboard();
        }
    }

    private void setShifted(final int shiftMode) {
         if (!mIsAlphabetMode) return;
        final int prevShiftMode;
        if (mAlphabetShiftStateCk.isAutomaticShifted()) {
            prevShiftMode = AUTOMATIC_SHIFT;
        } else if (mAlphabetShiftStateCk.isManualShifted()) {
            prevShiftMode = MANUAL_SHIFT;
        } else {
            prevShiftMode = UNSHIFT;
        }
        switch (shiftMode) {
            case AUTOMATIC_SHIFT:
                mAlphabetShiftStateCk.setAutomaticShifted();
                if (shiftMode != prevShiftMode) {
                    mSwitchActions.setAlphabetAutomaticShiftedKeyboard();
                }
                break;
            case MANUAL_SHIFT:
                mAlphabetShiftStateCk.setShifted(true);
                if (shiftMode != prevShiftMode) {
                    mSwitchActions.setAlphabetManualShiftedKeyboard();
                }
                break;
            case UNSHIFT:
                mAlphabetShiftStateCk.setShifted(false);
                if (shiftMode != prevShiftMode) {
                    mSwitchActions.setAlphabetKeyboard();
                }
                break;
            case SHIFT_LOCK_SHIFTED:
                mAlphabetShiftStateCk.setShifted(true);
                break;
        }
    }

    private void setShiftLocked(final boolean shiftLocked) {
         if (!mIsAlphabetMode) return;
        if (shiftLocked && (!mAlphabetShiftStateCk.isShiftLocked()
                || mAlphabetShiftStateCk.isShiftLockShifted())) {
            mSwitchActions.setAlphabetShiftLockedKeyboard();
        }
        if (!shiftLocked && mAlphabetShiftStateCk.isShiftLocked()) {
            mSwitchActions.setAlphabetKeyboard();
        }
        mAlphabetShiftStateCk.setShiftLocked(shiftLocked);
    }

    private void toggleAlphabetAndSymbols(final int autoCapsFlags, final int recapitalizeMode) {
         if (mIsAlphabetMode) {
            mPrevMainKeyboardWasShiftLocked = mAlphabetShiftStateCk.isShiftLocked();
            if (mPrevSymbolsKeyboardWasShifted) {
                setSymbolsShiftedKeyboard();
            } else {
                setSymbolsKeyboard();
            }
            mPrevSymbolsKeyboardWasShifted = false;
        } else {
            mPrevSymbolsKeyboardWasShifted = mIsSymbolShifted;
            setAlphabetKeyboard(autoCapsFlags, recapitalizeMode);
            if (mPrevMainKeyboardWasShiftLocked) {
                setShiftLocked(true);
            }
            mPrevMainKeyboardWasShiftLocked = false;
        }
    }

      private void resetKeyboardStateToAlphabet(final int autoCapsFlags, final int recapitalizeMode) {
         if (mIsAlphabetMode) return;

        mPrevSymbolsKeyboardWasShifted = mIsSymbolShifted;
        setAlphabetKeyboard(autoCapsFlags, recapitalizeMode);
        if (mPrevMainKeyboardWasShiftLocked) {
            setShiftLocked(true);
        }
        mPrevMainKeyboardWasShiftLocked = false;
    }

    private void toggleShiftInSymbols() {
        if (mIsSymbolShifted) {
            setSymbolsKeyboard();
        } else {
            setSymbolsShiftedKeyboard();
        }
    }

    private void setAlphabetKeyboard(final int autoCapsFlags, final int recapitalizeMode) {

        mSwitchActions.setAlphabetKeyboard();
        mIsAlphabetMode = true;
        mIsSymbolShifted = false;
        mRecapitalizeMode = RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE;
        mSwitchState = SWITCH_STATE_ALPHA;
        mSwitchActions.requestUpdatingShiftState(autoCapsFlags, recapitalizeMode);
    }

    private void setSymbolsKeyboard() {
         mSwitchActions.setSymbolsKeyboard();
        mIsAlphabetMode = false;
        mIsSymbolShifted = false;
        mRecapitalizeMode = RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE;

        mAlphabetShiftStateCk.setShiftLocked(false);
        mSwitchState = SWITCH_STATE_SYMBOL_BEGIN;
    }

    private void setSymbolsShiftedKeyboard() {
         mSwitchActions.setSymbolsShiftedKeyboard();
        mIsAlphabetMode = false;
        mIsSymbolShifted = true;
        mRecapitalizeMode = RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE;

        mAlphabetShiftStateCk.setShiftLocked(false);
        mSwitchState = SWITCH_STATE_SYMBOL_BEGIN;
    }

    public void onPressKey(final int code, final boolean isSinglePointer, final int autoCapsFlags,
                           final int recapitalizeMode) {
         if (code != ConstantsCk.CODE_SHIFT) {

            mSwitchActions.cancelDoubleTapShiftKeyTimer();
        }
        if (code == ConstantsCk.CODE_SHIFT) {
            onPressShift();
        } else if (code == ConstantsCk.CODE_CAPSLOCK) {

        } else if (code == ConstantsCk.CODE_SWITCH_ALPHA_SYMBOL) {
            onPressSymbol(autoCapsFlags, recapitalizeMode);
        } else if (code == ConstantsCk.CODE_NUMBER)
        {
            mSwitchActions.setBigNumKeyboard();

        } else if (code == ConstantsCk.CODE_SYMBOL)
        {
            setSymbolsKeyboard();

        } else{
            mShiftKeyState.onOtherKeyPressed();
            mSymbolKeyState.onOtherKeyPressed();

            if (!isSinglePointer && mIsAlphabetMode
                    && autoCapsFlags != TextUtils.CAP_MODE_CHARACTERS) {
                final boolean needsToResetAutoCaps =
                        (mAlphabetShiftStateCk.isAutomaticShifted() && !mShiftKeyState.isChording())
                                || (mAlphabetShiftStateCk.isManualShifted() && mShiftKeyState.isReleasing());
                if (needsToResetAutoCaps) {
                    mSwitchActions.setAlphabetKeyboard();
                }
            }
        }
    }

    public void onReleaseKey(final int code, final boolean withSliding, final int autoCapsFlags,
                             final int recapitalizeMode) {
         if (code == ConstantsCk.CODE_SHIFT) {
            onReleaseShift(withSliding, autoCapsFlags, recapitalizeMode);
        } else if (code == ConstantsCk.CODE_CAPSLOCK) {
            setShiftLocked(!mAlphabetShiftStateCk.isShiftLocked());
        } else if (code == ConstantsCk.CODE_SWITCH_ALPHA_SYMBOL) {
            onReleaseSymbol(withSliding, autoCapsFlags, recapitalizeMode);
        }
    }

    private void onPressSymbol(final int autoCapsFlags,
                               final int recapitalizeMode) {
        toggleAlphabetAndSymbols(autoCapsFlags, recapitalizeMode);
        mSymbolKeyState.onPress();
        mSwitchState = SWITCH_STATE_MOMENTARY_ALPHA_AND_SYMBOL;
    }

    private void onReleaseSymbol(final boolean withSliding, final int autoCapsFlags,
                                 final int recapitalizeMode) {
        if (mSymbolKeyState.isChording()) {

            toggleAlphabetAndSymbols(autoCapsFlags, recapitalizeMode);
        } else if (!withSliding) {

            mPrevSymbolsKeyboardWasShifted = false;
        }
        mSymbolKeyState.onRelease();
    }

    public void onUpdateShiftState(final int autoCapsFlags, final int recapitalizeMode) {
         mRecapitalizeMode = recapitalizeMode;
        updateAlphabetShiftState(autoCapsFlags, recapitalizeMode);
    }


    public void onResetKeyboardStateToAlphabet(final int autoCapsFlags,
                                               final int recapitalizeMode) {
         resetKeyboardStateToAlphabet(autoCapsFlags, recapitalizeMode);
    }

    private void updateShiftStateForRecapitalize(final int recapitalizeMode) {
        switch (recapitalizeMode) {
            case RecapitalizeCkStatus.CAPS_MODE_ALL_UPPER:
                setShifted(SHIFT_LOCK_SHIFTED);
                break;
            case RecapitalizeCkStatus.CAPS_MODE_FIRST_WORD_UPPER:
                setShifted(AUTOMATIC_SHIFT);
                break;
            case RecapitalizeCkStatus.CAPS_MODE_ALL_LOWER:
            case RecapitalizeCkStatus.CAPS_MODE_ORIGINAL_MIXED_CASE:
            default:
                setShifted(UNSHIFT);
        }
    }

    private void updateAlphabetShiftState(final int autoCapsFlags, final int recapitalizeMode) {
        if (!mIsAlphabetMode) return;
        if (RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE != recapitalizeMode) {

            updateShiftStateForRecapitalize(recapitalizeMode);
            return;
        }
        if (!mShiftKeyState.isReleasing()) {

            return;
        }
        if (!mAlphabetShiftStateCk.isShiftLocked() && !mShiftKeyState.isIgnoring()) {
            if (mShiftKeyState.isReleasing() && autoCapsFlags != ConstantsCk.TextUtils.CAP_MODE_OFF) {
                 setShifted(AUTOMATIC_SHIFT);
            } else {
                setShifted(mShiftKeyState.isChording() ? MANUAL_SHIFT : UNSHIFT);
            }
        }
    }

    private void onPressShift() {

        if (RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE != mRecapitalizeMode) {
            return;
        }
        if (mIsAlphabetMode) {
            mIsInDoubleTapShiftKey = mSwitchActions.isInDoubleTapShiftKeyTimeout();
            if (!mIsInDoubleTapShiftKey) {

                mSwitchActions.startDoubleTapShiftKeyTimer();
            }
            if (mIsInDoubleTapShiftKey) {
                if (mAlphabetShiftStateCk.isManualShifted() || mIsInAlphabetUnshiftedFromShifted) {

                    setShiftLocked(true);
                } else {

                }
            } else {
                if (mAlphabetShiftStateCk.isShiftLocked()) {

                    setShifted(SHIFT_LOCK_SHIFTED);
                    mShiftKeyState.onPress();
                } else if (mAlphabetShiftStateCk.isAutomaticShifted()) {

                    mShiftKeyState.onPress();
                } else if (mAlphabetShiftStateCk.isShiftedOrShiftLocked()) {

                    mShiftKeyState.onPressOnShifted();
                } else {
                     setShifted(MANUAL_SHIFT);
                    mShiftKeyState.onPress();
                }
            }
        } else {
             toggleShiftInSymbols();
            mSwitchState = SWITCH_STATE_MOMENTARY_SYMBOL_AND_MORE;
            mShiftKeyState.onPress();
        }
    }

    private void onReleaseShift(final boolean withSliding, final int autoCapsFlags,
                                final int recapitalizeMode) {
        if (RecapitalizeCkStatus.NOT_A_RECAPITALIZE_MODE != mRecapitalizeMode) {

            updateShiftStateForRecapitalize(mRecapitalizeMode);
        } else if (mIsAlphabetMode) {
            final boolean isShiftLocked = mAlphabetShiftStateCk.isShiftLocked();
            mIsInAlphabetUnshiftedFromShifted = false;
            if (mIsInDoubleTapShiftKey) {
                 mIsInDoubleTapShiftKey = false;
            } else if (mShiftKeyState.isChording()) {
                if (mAlphabetShiftStateCk.isShiftLockShifted()) {
                     setShiftLocked(true);
                } else {
                     setShifted(UNSHIFT);
                }

                mShiftKeyState.onRelease();
                mSwitchActions.requestUpdatingShiftState(autoCapsFlags, recapitalizeMode);
                return;
            } else if (isShiftLocked && !mAlphabetShiftStateCk.isShiftLockShifted()
                    && (mShiftKeyState.isPressing() || mShiftKeyState.isPressingOnShifted())
                    && !withSliding) {
             } else if (isShiftLocked && !mShiftKeyState.isIgnoring() && !withSliding) {
                 setShiftLocked(false);
            } else if (mAlphabetShiftStateCk.isShiftedOrShiftLocked()
                    && mShiftKeyState.isPressingOnShifted() && !withSliding) {
                 setShifted(UNSHIFT);
                mIsInAlphabetUnshiftedFromShifted = true;
            } else if (mAlphabetShiftStateCk.isAutomaticShifted() && mShiftKeyState.isPressing()
                    && !withSliding) {
                 setShifted(UNSHIFT);
                mIsInAlphabetUnshiftedFromShifted = true;
            }
        } else {

            if (mShiftKeyState.isChording()) {
                toggleShiftInSymbols();
            }
        }
        mShiftKeyState.onRelease();
    }

    public void onFinishSlidingInput(final int autoCapsFlags, final int recapitalizeMode) {

        switch (mSwitchState) {
            case SWITCH_STATE_MOMENTARY_ALPHA_AND_SYMBOL:
                toggleAlphabetAndSymbols(autoCapsFlags, recapitalizeMode);
                break;
            case SWITCH_STATE_MOMENTARY_SYMBOL_AND_MORE:
                toggleShiftInSymbols();
                break;
        }
    }

    private static boolean isSpaceOrEnter(final int c) {
        return c == ConstantsCk.CODE_SPACE || c == ConstantsCk.CODE_ENTER;
    }

    public void onEvent(final EventCk eventCk, final int autoCapsFlags, final int recapitalizeMode) {
        final int code = eventCk.isFunctionalKeyEvent() ? eventCk.mKeyCode : eventCk.mCodePoint;

        switch (mSwitchState) {
            case SWITCH_STATE_MOMENTARY_ALPHA_AND_SYMBOL:
                if (code == ConstantsCk.CODE_SWITCH_ALPHA_SYMBOL) {

                    if (mIsAlphabetMode) {
                        mSwitchState = SWITCH_STATE_ALPHA;
                    } else {
                        mSwitchState = SWITCH_STATE_SYMBOL_BEGIN;
                    }
                }
                break;
            case SWITCH_STATE_MOMENTARY_SYMBOL_AND_MORE:
                if (code == ConstantsCk.CODE_SHIFT) {

                    mSwitchState = SWITCH_STATE_SYMBOL_BEGIN;
                }
                break;
            case SWITCH_STATE_SYMBOL_BEGIN:
                if (!isSpaceOrEnter(code) && (ConstantsCk.isLetterCode(code)
                        || code == ConstantsCk.CODE_OUTPUT_TEXT)) {
                    mSwitchState = SWITCH_STATE_SYMBOL;
                }
                break;
            case SWITCH_STATE_SYMBOL:

                if (isSpaceOrEnter(code)) {
                    toggleAlphabetAndSymbols(autoCapsFlags, recapitalizeMode);
                    mPrevSymbolsKeyboardWasShifted = false;
                }
                break;
        }


        if (ConstantsCk.isLetterCode(code)) {
            updateAlphabetShiftState(autoCapsFlags, recapitalizeMode);
        }
    }

    static String shiftModeToString(final int shiftMode) {
        switch (shiftMode) {
            case UNSHIFT: return "UNSHIFT";
            case MANUAL_SHIFT: return "MANUAL";
            case AUTOMATIC_SHIFT: return "AUTOMATIC";
            default: return null;
        }
    }

    private static String switchStateToString(final int switchState) {
        switch (switchState) {
            case SWITCH_STATE_ALPHA: return "ALPHA";
            case SWITCH_STATE_SYMBOL_BEGIN: return "SYMBOL-BEGIN";
            case SWITCH_STATE_SYMBOL: return "SYMBOL";
            case SWITCH_STATE_MOMENTARY_ALPHA_AND_SYMBOL: return "MOMENTARY-ALPHA-SYMBOL";
            case SWITCH_STATE_MOMENTARY_SYMBOL_AND_MORE: return "MOMENTARY-SYMBOL-MORE";
            default: return null;
        }
    }

    @Override
    public String toString() {
        return "[keyboard=" + (mIsAlphabetMode ? mAlphabetShiftStateCk.toString()
                : (mIsSymbolShifted ? "SYMBOLS_SHIFTED" : "SYMBOLS"))
                + " shift=" + mShiftKeyState
                + " symbol=" + mSymbolKeyState
                + " switch=" + switchStateToString(mSwitchState) + "]";
    }

    private String stateToString(final int autoCapsFlags, final int recapitalizeMode) {
        return this + " autoCapsFlags=" + CapsModeCkUtils.flagsToString(autoCapsFlags)
                + " recapitalizeMode=" + RecapitalizeCkStatus.modeToString(recapitalizeMode);
    }
}

