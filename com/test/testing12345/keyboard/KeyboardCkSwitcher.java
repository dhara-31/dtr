/*
 * Copyright (C) 2008 The Android Open Source Project
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testing12345.R;
import com.test.testing12345.adsclass.FontSelectCsActivity;
import com.test.testing12345.adapter.AdapterKeyFontStyle;
import com.test.testing12345.compat.PreferenceCkManagerCompat;
import com.test.testing12345.event.EventCk;
import com.test.testing12345.keyboard.KeyboardLayoutCkSet.KeyboardLayoutSetException;
import com.test.testing12345.keyboard.internal.KeyboardCkState;
import com.test.testing12345.keyboard.internal.KeyboardCkTextsSet;
import com.test.testing12345.custom.InputViewCk;
import com.test.testing12345.custom.CustomKeyBoard;
import com.test.testing12345.custom.RichInputMethodCkManager;
import com.test.testing12345.custom.settings.SettingsCk;
import com.test.testing12345.custom.settings.SettingsCkValues;
import com.test.testing12345.custom.utils.LanguageOnSpacebarCkUtils;
import com.test.testing12345.custom.utils.ResourceCkUtils;
import com.test.testing12345.adsclass.StylishFontCkModel;
import com.test.testing12345.adsclass.StoreageCkPref;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import github.ankushsachdeva.emojicon.EmojiconsPopup;

public final class KeyboardCkSwitcher implements KeyboardCkState.SwitchActions {
    private static final String TAG = KeyboardCkSwitcher.class.getSimpleName();
    private EmojiconsPopup popupWindow = null;

    private InputViewCk mCurrentInputViewCk;
    private int mCurrentUiMode;
    private int mCurrentTextColor = 0x0;
    private View mMainKeyboardFrame;
    LinearLayout ll, llTool,llFont;
    private MainKeyboardCkView mKeyboardView;
    ImageView btnEmoji, btnVoice, btnWidgets,btnSetting, btnSticker,btnFont,btnMore;
    private CustomKeyBoard mCustomKeyBoard;
    private RichInputMethodCkManager mRichImm;

    private KeyboardCkState mState;
    StoreageCkPref storeageCkPref;
    SharedPreferences sharedPreferences;


    private KeyboardLayoutCkSet mKeyboardLayoutCkSet;

    private final KeyboardCkTextsSet mKeyboardCkTextsSet = new KeyboardCkTextsSet();

    private KeyboardCkTheme mKeyboardCkTheme;
    private Context mThemeContext;

    private static final KeyboardCkSwitcher sInstance = new KeyboardCkSwitcher();
    private boolean sdfsdfasdf = true;
    private int i=0;
    private RecyclerView recyclerViewFontList;
    private Animation animation1;
    private Animation animation2;

    public static KeyboardCkSwitcher getInstance() {
        return sInstance;
    }

    private KeyboardCkSwitcher() {

    }

    public static void init(final CustomKeyBoard customKeyBoard) {
        sInstance.initInternal(customKeyBoard);
    }

    private void initInternal(final CustomKeyBoard customKeyBoard) {
        mCustomKeyBoard = customKeyBoard;
        mRichImm = RichInputMethodCkManager.getInstance();
        mState = new KeyboardCkState(this);
    }

    public void updateKeyboardTheme(final int uiMode) {
        final boolean themeUpdated = updateKeyboardThemeAndContextThemeWrapper(
                mCustomKeyBoard, KeyboardCkTheme.getKeyboardTheme(mCustomKeyBoard), uiMode);
        if (themeUpdated && mKeyboardView != null) {
            mCustomKeyBoard.setInputView(onCreateInputView(uiMode));
        }
    }

    private boolean updateKeyboardThemeAndContextThemeWrapper(final Context context,
                                                              final KeyboardCkTheme keyboardCkTheme, final int uiMode) {
        int newTextColor = 0x0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            newTextColor = context.getResources().getColor(R.color.key_text_color_lxx_system);
        }

        if (mThemeContext == null
                || !keyboardCkTheme.equals(mKeyboardCkTheme)
                || mCurrentUiMode != uiMode
                || newTextColor != mCurrentTextColor) {
            mKeyboardCkTheme = keyboardCkTheme;
            mCurrentUiMode = uiMode;
            mCurrentTextColor = newTextColor;
            mThemeContext = new ContextThemeWrapper(context, keyboardCkTheme.mStyleId);
            KeyboardLayoutCkSet.onKeyboardThemeChanged();
            return true;
        }
        return false;
    }

    public void loadKeyboard(final EditorInfo editorInfo, final SettingsCkValues settingsCkValues,
                             final int currentAutoCapsState, final int currentRecapitalizeState) {
        final KeyboardLayoutCkSet.Builder builder = new KeyboardLayoutCkSet.Builder(
                mThemeContext, editorInfo);
         final Resources res = mThemeContext.getResources();
        final int keyboardWidth = mCustomKeyBoard.getMaxWidth();
        final int keyboardHeight = ResourceCkUtils.getKeyboardHeight(res, settingsCkValues);
        builder.setKeyboardTheme(mKeyboardCkTheme.mThemeId);
        builder.setKeyboardGeometry(keyboardWidth, keyboardHeight);
        builder.setSubtype(mRichImm.getCurrentSubtype());


        builder.setLanguageSwitchKeyEnabled(mCustomKeyBoard.shouldShowLanguageSwitchKey());
        builder.setShowSpecialChars(!settingsCkValues.mHideSpecialChars);
        builder.setShowNumberRow(settingsCkValues.mShowNumberRow);
        mKeyboardLayoutCkSet = builder.build();

        try {
            mState.onLoadKeyboard(currentAutoCapsState, currentRecapitalizeState);
            mKeyboardCkTextsSet.setLocale(mRichImm.getCurrentSubtype().getLocaleObject(),
                    mThemeContext);
        } catch (KeyboardLayoutSetException e) {
         }
    }

    public void saveKeyboardState() {
        if (getKeyboard() != null) {
            mState.onSaveKeyboardState();
        }
    }

    public void onHideWindow() {
        if (mKeyboardView != null) {
            mKeyboardView.onHideWindow();
        }
    }

    private void setKeyboard(
            final int keyboardId,
            final KeyboardSwitchState toggleState) {
        final SettingsCkValues currentSettingsCkValues = SettingsCk.getInstance().getCurrent();
        setMainKeyboardFrame(currentSettingsCkValues, toggleState);
         final MainKeyboardCkView keyboardView = mKeyboardView;
        final KeyboardCk oldKeyboardCk = keyboardView.getKeyboard();
        final KeyboardCk newKeyboardCk = mKeyboardLayoutCkSet.getKeyboard(keyboardId);
        keyboardView.setKeyboard(newKeyboardCk);
        keyboardView.setKeyPreviewPopupEnabled(
                currentSettingsCkValues.mKeyPreviewPopupOn,
                currentSettingsCkValues.mKeyPreviewPopupDismissDelay);
        final boolean subtypeChanged = (oldKeyboardCk == null)
                || !newKeyboardCk.mId.mSubtypeCk.equals(oldKeyboardCk.mId.mSubtypeCk);
        final int languageOnSpacebarFormatType = LanguageOnSpacebarCkUtils
                .getLanguageOnSpacebarFormatType(newKeyboardCk.mId.mSubtypeCk);
        keyboardView.startDisplayLanguageOnSpacebar(subtypeChanged, languageOnSpacebarFormatType);
    }

    public KeyboardCk getKeyboard() {
        if (mKeyboardView != null) {
            return mKeyboardView.getKeyboard();
        }
        return null;
    }

    public void resetKeyboardStateToAlphabet(final int currentAutoCapsState,
                                             final int currentRecapitalizeState) {
        mState.onResetKeyboardStateToAlphabet(currentAutoCapsState, currentRecapitalizeState);
    }

    public void onPressKey(final int code, final boolean isSinglePointer,
                           final int currentAutoCapsState, final int currentRecapitalizeState) {
        mState.onPressKey(code, isSinglePointer, currentAutoCapsState, currentRecapitalizeState);
    }

    public void onReleaseKey(final int code, final boolean withSliding,
                             final int currentAutoCapsState, final int currentRecapitalizeState) {
        mState.onReleaseKey(code, withSliding, currentAutoCapsState, currentRecapitalizeState);
    }

    public void onFinishSlidingInput(final int currentAutoCapsState,
                                     final int currentRecapitalizeState) {
        mState.onFinishSlidingInput(currentAutoCapsState, currentRecapitalizeState);
    }

    @Override
    public void setAlphabetKeyboard() {
         setKeyboard(KeyboardCkId.ELEMENT_ALPHABET, KeyboardSwitchState.OTHER);
    }


    @Override
    public void setAlphabetManualShiftedKeyboard() {
        if (DEBUG_ACTION) {

        }
        setKeyboard(KeyboardCkId.ELEMENT_ALPHABET_MANUAL_SHIFTED, KeyboardSwitchState.OTHER);
    }

    @Override
    public void setAlphabetAutomaticShiftedKeyboard() {
        if (DEBUG_ACTION) {

        }
        setKeyboard(KeyboardCkId.ELEMENT_ALPHABET_AUTOMATIC_SHIFTED, KeyboardSwitchState.OTHER);
    }

    @Override
    public void setAlphabetShiftLockedKeyboard() {

        setKeyboard(KeyboardCkId.ELEMENT_ALPHABET_SHIFT_LOCKED, KeyboardSwitchState.OTHER);
    }

     @Override
    public void setSymbolsKeyboard() {
         setKeyboard(KeyboardCkId.ELEMENT_SYMBOLS, KeyboardSwitchState.OTHER);
    }

     @Override
    public void setSymbolsShiftedKeyboard() {
         setKeyboard(KeyboardCkId.ELEMENT_SYMBOLS_SHIFTED, KeyboardSwitchState.SYMBOLS_SHIFTED);
    }

    public boolean isImeSuppressedByHardwareKeyboard(
            final SettingsCkValues settingsCkValues,
            final KeyboardSwitchState toggleState) {
        return settingsCkValues.mHasHardwareKeyboard && toggleState == KeyboardSwitchState.HIDDEN;
    }

    private void setMainKeyboardFrame(
            final SettingsCkValues settingsCkValues,
            final KeyboardSwitchState toggleState) {
        final int visibility = isImeSuppressedByHardwareKeyboard(settingsCkValues, toggleState)
                ? View.GONE : View.VISIBLE;
        mKeyboardView.setVisibility(visibility);

        mMainKeyboardFrame.setVisibility(visibility);
    }

    public enum KeyboardSwitchState {
        HIDDEN(-1),
        SYMBOLS_SHIFTED(KeyboardCkId.ELEMENT_SYMBOLS_SHIFTED),
        OTHER(-1);

        final int mKeyboardId;

        KeyboardSwitchState(int keyboardId) {
            mKeyboardId = keyboardId;
        }
    }

    public KeyboardSwitchState getKeyboardSwitchState() {
        boolean hidden = mKeyboardLayoutCkSet == null
                || mKeyboardView == null
                || !mKeyboardView.isShown();
        if (hidden) {
            return KeyboardSwitchState.HIDDEN;
        } else if (isShowingKeyboardId(KeyboardCkId.ELEMENT_SYMBOLS_SHIFTED)) {
            return KeyboardSwitchState.SYMBOLS_SHIFTED;
        }
        return KeyboardSwitchState.OTHER;
    }

     @Override
    public void requestUpdatingShiftState(final int autoCapsFlags, final int recapitalizeMode) {
         mState.onUpdateShiftState(autoCapsFlags, recapitalizeMode);
    }


    @Override
    public void startDoubleTapShiftKeyTimer() {
         final MainKeyboardCkView keyboardView = getMainKeyboardView();
        if (keyboardView != null) {
            keyboardView.startDoubleTapShiftKeyTimer();
        }
    }
     @Override
    public void cancelDoubleTapShiftKeyTimer() {
         final MainKeyboardCkView keyboardView = getMainKeyboardView();
        if (keyboardView != null) {
            keyboardView.cancelDoubleTapShiftKeyTimer();
        }
    }

    @Override
    public void setBigNumKeyboard() {
        setKeyboard(KeyboardCkId.ELEMENT_BIG_NUMBER, KeyboardSwitchState.OTHER);

    }

     @Override
    public boolean isInDoubleTapShiftKeyTimeout() {
         final MainKeyboardCkView keyboardView = getMainKeyboardView();
        return keyboardView != null && keyboardView.isInDoubleTapShiftKeyTimeout();
    }


    public void onEvent(final EventCk eventCk, final int currentAutoCapsState,
                        final int currentRecapitalizeState) {
        mState.onEvent(eventCk, currentAutoCapsState, currentRecapitalizeState);
    }

    public boolean isShowingKeyboardId(int... keyboardIds) {
        if (mKeyboardView == null || !mKeyboardView.isShown()) {
            return false;
        }
        int activeKeyboardId = mKeyboardView.getKeyboard().mId.mElementId;
        for (int keyboardId : keyboardIds) {
            if (activeKeyboardId == keyboardId) {
                return true;
            }
        }
        return false;
    }

    public boolean isShowingMoreKeysPanel() {
        return mKeyboardView.isShowingMoreKeysPanel();
    }

    public View getVisibleKeyboardView() {
        return mKeyboardView;
    }

    public MainKeyboardCkView getMainKeyboardView() {
        return mKeyboardView;
    }

    public void deallocateMemory() {
        if (mKeyboardView != null) {
            mKeyboardView.cancelAllOngoingEvents();
            mKeyboardView.deallocateMemory();
        }
    }

    public View onCreateInputView(final int uiMode) {
        if (mKeyboardView != null) {
            mKeyboardView.closing();
        }

        storeageCkPref = new StoreageCkPref(mCustomKeyBoard.getApplicationContext());

        updateKeyboardThemeAndContextThemeWrapper(
                mCustomKeyBoard, KeyboardCkTheme.getKeyboardTheme(mCustomKeyBoard /* context */), uiMode);
        mCurrentInputViewCk = (InputViewCk) LayoutInflater.from(mThemeContext).inflate(
                R.layout.input_view, null);
        mMainKeyboardFrame = mCurrentInputViewCk.findViewById(R.id.main_keyboard_frame);

        ll = mCurrentInputViewCk.findViewById(R.id.ll);
        llTool = mCurrentInputViewCk.findViewById(R.id.llTool);
        btnEmoji = mCurrentInputViewCk.findViewById(R.id.btnEmoji);
        btnWidgets = mCurrentInputViewCk.findViewById(R.id.btnWidgets);

        btnVoice = mCurrentInputViewCk.findViewById(R.id.btnVoice);

        btnSetting = mCurrentInputViewCk.findViewById(R.id.btnSetting);
        btnSticker = mCurrentInputViewCk.findViewById(R.id.btnSticker);
        btnFont = mCurrentInputViewCk.findViewById(R.id.btnFont);
        llFont = mCurrentInputViewCk.findViewById(R.id.llFont);
        btnMore = mCurrentInputViewCk.findViewById(R.id.btnMore);
        recyclerViewFontList = mCurrentInputViewCk.findViewById(R.id.recyclerFontList);


        mKeyboardView = (MainKeyboardCkView) mCurrentInputViewCk.findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboardActionListener(mCustomKeyBoard);

        sharedPreferences =  PreferenceCkManagerCompat.getDeviceSharedPreferences(mThemeContext.getApplicationContext());

        animation2 = AnimationUtils.loadAnimation(ll.getContext(), R.anim.right);


        btnWidgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llTool.getVisibility() == View.VISIBLE) {
                    animation2 = AnimationUtils.loadAnimation(ll.getContext(), R.anim.left);
                    llTool.setAnimation(animation2);
                    llTool.setVisibility(View.GONE);
                    llFont.setVisibility(View.GONE);
                    mCustomKeyBoard.closedPopTool();

                } else if (
                    llFont.getVisibility() == View.VISIBLE) {
                    llFont.setVisibility(View.GONE);
                    llTool.setVisibility(View.VISIBLE);
                    animation1 = AnimationUtils.loadAnimation(ll.getContext(), R.anim.right);
                    llTool.setAnimation(animation1);
                }else {
                    llFont.setVisibility(View.GONE);
                    llTool.setVisibility(View.VISIBLE);
                    animation1 = AnimationUtils.loadAnimation(ll.getContext(), R.anim.right);
                    llTool.setAnimation(animation1);

                }


            }
        });

        btnFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storeageCkPref.getFavorites() != null && !storeageCkPref.getFavorites().isEmpty()) {
                    setAdapter();

                    llTool.setVisibility(View.GONE);
                    llFont.setVisibility(View.VISIBLE);
                }else {
                    Intent intent = new Intent(mThemeContext, FontSelectCsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mThemeContext.startActivity(intent);
                }
            }
        });



        final int themeId = KeyboardCkTheme.getKeyboardTheme(mThemeContext).mThemeId;

        if (themeId == 0) {
            ll.setBackgroundColor(mThemeContext.getResources().getColor(R.color.theme_0_bg));
        } else if (themeId == 1) {
            ll.setBackgroundColor(mThemeContext.getResources().getColor(R.color.theme_1_bg));

        } else if (themeId == 2) {
            ll.setBackgroundColor(mThemeContext.getResources().getColor(R.color.theme_2_bg));
            btnWidgets.setImageResource(R.drawable.th_2_menu);
            btnEmoji.setImageResource(R.drawable.th_2_emoji);
             btnSetting.setImageResource(R.drawable.th_2_setting);
            btnSticker.setImageResource(R.drawable.th_2_sticker);
            btnVoice.setImageResource(R.drawable.th_2_voice);
            btnMore.setImageResource(R.drawable.th_2_more);

            btnFont.setImageResource(R.drawable.th_2_fancy_text);


        } else if (themeId == 3) {


            ll.setBackgroundColor(mThemeContext.getResources().getColor(R.color.theme_3_bg));
            btnWidgets.setImageResource(R.drawable.th_3_menu);
            btnEmoji.setImageResource(R.drawable.th_3_emoji);

            btnSetting.setImageResource(R.drawable.th_3_setting);
            btnSticker.setImageResource(R.drawable.th_3_sticker);
            btnVoice.setImageResource(R.drawable.th_3_voice);
             btnMore.setImageResource(R.drawable.th_3_more);
            btnFont.setImageResource(R.drawable.th_3_fancy_text);


        } else if (themeId == 5) {
            ll.setBackgroundColor(mThemeContext.getResources().getColor(R.color.theme_5_bg));
            btnWidgets.setImageResource(R.drawable.th_5_menu);
            btnEmoji.setImageResource(R.drawable.th_5_emoji);

            btnSetting.setImageResource(R.drawable.th_5_setting);
            btnSticker.setImageResource(R.drawable.th_5_sticker);
            btnVoice.setImageResource(R.drawable.th_5_voice);
        btnMore.setImageResource(R.drawable.th_5_more);
            btnFont.setImageResource(R.drawable.th_5_f);

        } else if (themeId == 4) {
             ll.setBackground(mThemeContext.getResources().getDrawable(R.drawable.th4_bg2));
            btnWidgets.setImageResource(R.drawable.th_4_menu);
            btnEmoji.setImageResource(R.drawable.th_4_emoji);

            btnSetting.setImageResource(R.drawable.th_4_setting);
            btnSticker.setImageResource(R.drawable.th_4_sticker);
            btnVoice.setImageResource(R.drawable.th_4_voice);
             btnMore.setImageResource(R.drawable.th_4_more);
            btnFont.setImageResource(R.drawable.th_4_fancy_text);

        } else if (themeId == 6 || themeId == 7) {
            Bitmap originalBm = null;
            String fileS = storeageCkPref.getFILE_PATH();
            if (fileS != null) {
                Uri uri = FileProvider.getUriForFile(mThemeContext, mThemeContext.getPackageName() + ".fileprovider", new File(fileS));
                try {

                    originalBm = MediaStore.Images.Media.getBitmap(mThemeContext.getContentResolver(), uri);
                    int fromHere = (int) (originalBm.getHeight() * 0.8);
                    Bitmap bitmap2 = Bitmap.createBitmap(originalBm, 0, (int) (originalBm.getHeight() * 0.2), originalBm.getWidth(), fromHere);


                    Bitmap bm1 = Bitmap.createBitmap(originalBm, 0, 0, originalBm.getWidth(), (originalBm.getHeight() / 5));


                    Drawable dr2 = new BitmapDrawable(mThemeContext.getResources(), bitmap2);
                    Drawable dr1 = new BitmapDrawable(mThemeContext.getResources(), bm1);

                    mKeyboardView.setBackground(dr2);
                    ll.setBackground(dr1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

           }

                btnWidgets.setImageResource(R.drawable.th_2_menu);
            btnEmoji.setImageResource(R.drawable.th_2_emoji);

            btnSetting.setImageResource(R.drawable.th_2_setting);
            btnSticker.setImageResource(R.drawable.th_2_sticker);
            btnVoice.setImageResource(R.drawable.th_2_voice);
            btnFont.setImageResource(R.drawable.th_2_fancy_text);
            btnMore.setImageResource(R.drawable.th_2_more);

        }




        return mCurrentInputViewCk;
    }

    private void setAdapter() {

        recyclerViewFontList.setLayoutManager(new LinearLayoutManager(mThemeContext, RecyclerView.HORIZONTAL, false));

        ArrayList<StylishFontCkModel> stylishFontCkModelArrayList = new ArrayList<>();
        if (storeageCkPref.getFavorites() != null && !storeageCkPref.getFavorites().isEmpty()) {
            stylishFontCkModelArrayList = storeageCkPref.getFavorites();
        }
        AdapterKeyFontStyle adapterFontStyle = new AdapterKeyFontStyle(mThemeContext, stylishFontCkModelArrayList);
        this.recyclerViewFontList.setAdapter(adapterFontStyle);
        adapterFontStyle.setOnItemClickListener(new AdapterKeyFontStyle.OnItemClickListener1() {
            @Override
            public void onItemClick1(View v) throws IOException {
              mCustomKeyBoard.changeMyTheme();
            }
        });
    }
    private int getPercentageFromValue(final float floatValue) {
        return (int)(floatValue *  100.0f);
    }

}

