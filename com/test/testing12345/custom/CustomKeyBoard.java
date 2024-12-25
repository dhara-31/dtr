
package com.test.testing12345.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.airbnb.lottie.LottieAnimationView;
import com.test.testing12345.R;
import com.test.testing12345.activity.AllStickerAddCsActivity;
import com.test.testing12345.activity.MainCsActivity;
import com.test.testing12345.adsclass.FontSelectCsActivity;
import com.test.testing12345.activity.SettingCsActivity;
import com.test.testing12345.activity.ThemeCreateCsActivity;
import com.test.testing12345.adapter.AdapterCSti;
import com.test.testing12345.adapter.AdapterClip;
import com.test.testing12345.adapter.AdapterGif;
import com.test.testing12345.adapter.AdapterSti;
import com.test.testing12345.adapter.AdapterTextSti;
import com.test.testing12345.compat.EditorInfoCompatCkUtils;
import com.test.testing12345.compat.PreferenceCkManagerCompat;
import com.test.testing12345.compat.ViewOutlineProviderCompatCkUtils;
import com.test.testing12345.compat.ViewOutlineProviderCompatCkUtils.InsetsUpdater;
import com.test.testing12345.event.EventCk;
import com.test.testing12345.event.InputTransactionCk;
import com.test.testing12345.keyboard.KeyboardCk;
import com.test.testing12345.keyboard.KeyboardActionListener;
import com.test.testing12345.keyboard.KeyboardCkId;
import com.test.testing12345.keyboard.KeyboardCkSwitcher;
import com.test.testing12345.keyboard.KeyboardCkTheme;
import com.test.testing12345.keyboard.MainKeyboardCkView;
import com.test.testing12345.custom.common.ConstantsCk;
import com.test.testing12345.custom.define.DebugCkFlags;
import com.test.testing12345.custom.inputlogic.InputLogicCk;
import com.test.testing12345.custom.settings.SettingsCk;
import com.test.testing12345.custom.settings.SettingsCkValues;
import com.test.testing12345.custom.utils.ApplicationCkUtils;
import com.test.testing12345.custom.utils.LeakGuardHandlerCkWrapper;
import com.test.testing12345.custom.utils.ResourceCkUtils;
import com.test.testing12345.custom.utils.ViewLayoutCkUtils;
import com.test.testing12345.adsclass.StoreageCkPref;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;


public class CustomKeyBoard extends InputMethodService implements KeyboardActionListener,
        RichInputMethodCkManager.SubtypeChangedListener {
    private long lClickTime = 0;
    public static CustomKeyBoard Instance;
    static final String TAG = CustomKeyBoard.class.getSimpleName();
    private static final boolean TRACE = false;
    private static final int EXTENDED_TOUCHABLE_REGION_HEIGHT = 100;
    private static final int PERIOD_FOR_AUDIO_AND_HAPTIC_FEEDBACK_IN_KEY_REPEAT = 2;
    private static final int PENDING_IMS_CALLBACK_DURATION_MILLIS = 800;
    static final long DELAY_DEALLOCATE_MEMORY_MILLIS = TimeUnit.SECONDS.toMillis(10);
    private SpeechRecognizer speechRecognizer;
    final SettingsCk mSettingsCk;
    private Locale mLocale;
    private int mOriginalNavBarColor = 0;
    private int mOriginalNavBarFlags = 0;
    final InputLogicCk mInputLogicCk = new InputLogicCk(this);
      private View mInputView;
    private InsetsUpdater mInsetsUpdater;
    private EmojiconsPopup popupWindow = null;
    private RichInputMethodCkManager mRichImm;
    final KeyboardCkSwitcher mKeyboardCkSwitcher;
    private AlertDialog mOptionsDialog;
    public final UIHandlerCk mHandler = new UIHandlerCk(this);
    private ImageView btnEmoji, btnVoice, btnSticker, btnMore, btnSetting;

    public final StringBuilder mComposing = new StringBuilder();
    private InputMethodManager mInputMethodManager;
    private PopupWindow popGif;
    private PopupWindow popSticker;
    private PopupWindow popClip;
    private PopupWindow popTool;
    private boolean openGif = false;
    private Animator currentAnimator;
    MediaPlayer mediaPlayer;
    private int shortAnimationDuration;
    private ArrayList<String> clipboard = new ArrayList<>();
    private StoreageCkPref storeageCkPref;
    private PopupWindow popMic;
    Intent speechRecognizerIntent;

    public static final class UIHandlerCk extends LeakGuardHandlerCkWrapper<CustomKeyBoard> {
        private static final int MSG_UPDATE_SHIFT_STATE = 0;
        private static final int MSG_PENDING_IMS_CALLBACK = 1;
        private static final int MSG_RESET_CACHES = 7;
        private static final int MSG_WAIT_FOR_DICTIONARY_LOAD = 8;
        private static final int MSG_DEALLOCATE_MEMORY = 9;

        private static final int ARG1_TRUE = 1;

        private int mDelayInMillisecondsToUpdateShiftState;


        public UIHandlerCk(final CustomKeyBoard ownerInstance) {
            super(ownerInstance);
        }

        public void onCreate() {
            final CustomKeyBoard customKeyBoard = getOwnerInstance();
            if (customKeyBoard == null) {
                return;
            }
            final Resources res = customKeyBoard.getResources();
            mDelayInMillisecondsToUpdateShiftState = res.getInteger(
                    R.integer.config_delay_in_milliseconds_to_update_shift_state);
        }

        @Override
        public void handleMessage(final Message msg) {
            final CustomKeyBoard customKeyBoard = getOwnerInstance();
            if (customKeyBoard == null) {
                return;
            }
            final KeyboardCkSwitcher switcher = customKeyBoard.mKeyboardCkSwitcher;
            switch (msg.what) {
                case MSG_UPDATE_SHIFT_STATE:
                    switcher.requestUpdatingShiftState(customKeyBoard.getCurrentAutoCapsState(),
                            customKeyBoard.getCurrentRecapitalizeState());
                    break;
                case MSG_RESET_CACHES:
                    final SettingsCkValues settingsCkValues = customKeyBoard.mSettingsCk.getCurrent();
                    if (customKeyBoard.mInputLogicCk.retryResetCachesAndReturnSuccess(
                            msg.arg1 == ARG1_TRUE /* tryResumeSuggestions */,
                            msg.arg2, this)) {

                        customKeyBoard.mKeyboardCkSwitcher.loadKeyboard(customKeyBoard.getCurrentInputEditorInfo(),
                                settingsCkValues, customKeyBoard.getCurrentAutoCapsState(),
                                customKeyBoard.getCurrentRecapitalizeState());
                    }
                    break;
                case MSG_WAIT_FOR_DICTIONARY_LOAD:
                    break;
                case MSG_DEALLOCATE_MEMORY:
                    customKeyBoard.deallocateMemory();
                    break;
            }
        }

        public void postResetCaches(final boolean tryResumeSuggestions, final int remainingTries) {
            removeMessages(MSG_RESET_CACHES);
            sendMessage(obtainMessage(MSG_RESET_CACHES, tryResumeSuggestions ? 1 : 0,
                    remainingTries, null));
        }

        public void postUpdateShiftState() {
            removeMessages(MSG_UPDATE_SHIFT_STATE);
            sendMessageDelayed(obtainMessage(MSG_UPDATE_SHIFT_STATE),
                    mDelayInMillisecondsToUpdateShiftState);
        }

        public void postDeallocateMemory() {
            sendMessageDelayed(obtainMessage(MSG_DEALLOCATE_MEMORY),
                    DELAY_DEALLOCATE_MEMORY_MILLIS);
        }

        public void cancelDeallocateMemory() {
            removeMessages(MSG_DEALLOCATE_MEMORY);
        }

        public boolean hasPendingDeallocateMemory() {
            return hasMessages(MSG_DEALLOCATE_MEMORY);
        }

        // Working variables for the following methods.
        private boolean mIsOrientationChanging;
        private boolean mPendingSuccessiveImsCallback;
        private boolean mHasPendingStartInput;
        private boolean mHasPendingFinishInputView;
        private boolean mHasPendingFinishInput;
        private EditorInfo mAppliedEditorInfo;

        private void resetPendingImsCallback() {
            mHasPendingFinishInputView = false;
            mHasPendingFinishInput = false;
            mHasPendingStartInput = false;
        }

        private void executePendingImsCallback(final CustomKeyBoard customKeyBoard, final EditorInfo editorInfo,
                                               boolean restarting) {
            if (mHasPendingFinishInputView) {
                customKeyBoard.onFinishInputViewInternal(mHasPendingFinishInput);
            }
            if (mHasPendingFinishInput) {
                customKeyBoard.onFinishInputInternal();
            }
            if (mHasPendingStartInput) {
                customKeyBoard.onStartInputInternal(editorInfo, restarting);
            }
            resetPendingImsCallback();
        }

        public void onStartInput(final EditorInfo editorInfo, final boolean restarting) {
            if (hasMessages(MSG_PENDING_IMS_CALLBACK)) {

                mHasPendingStartInput = true;
            } else {
                if (mIsOrientationChanging && restarting) {

                    mIsOrientationChanging = false;
                    mPendingSuccessiveImsCallback = true;
                }
                final CustomKeyBoard customKeyBoard = getOwnerInstance();
                if (customKeyBoard != null) {
                    executePendingImsCallback(customKeyBoard, editorInfo, restarting);
                    customKeyBoard.onStartInputInternal(editorInfo, restarting);
                }
            }
        }

        public void onStartInputView(final EditorInfo editorInfo, final boolean restarting) {
            if (hasMessages(MSG_PENDING_IMS_CALLBACK)
                    && KeyboardCkId.equivalentEditorInfoForKeyboard(editorInfo, mAppliedEditorInfo)) {

                resetPendingImsCallback();
            } else {
                if (mPendingSuccessiveImsCallback) {

                    mPendingSuccessiveImsCallback = false;
                    resetPendingImsCallback();
                    sendMessageDelayed(obtainMessage(MSG_PENDING_IMS_CALLBACK),
                            PENDING_IMS_CALLBACK_DURATION_MILLIS);
                }
                final CustomKeyBoard customKeyBoard = getOwnerInstance();
                if (customKeyBoard != null) {
                    executePendingImsCallback(customKeyBoard, editorInfo, restarting);
                    customKeyBoard.onStartInputViewInternal(editorInfo, restarting);
                    mAppliedEditorInfo = editorInfo;
                }
                cancelDeallocateMemory();
            }
        }

        public void onFinishInputView(final boolean finishingInput) {
            if (hasMessages(MSG_PENDING_IMS_CALLBACK)) {
                // Typically this is the first onFinishInputView after orientation changed.
                mHasPendingFinishInputView = true;
            } else {
                final CustomKeyBoard customKeyBoard = getOwnerInstance();
                if (customKeyBoard != null) {
                    customKeyBoard.onFinishInputViewInternal(finishingInput);
                    mAppliedEditorInfo = null;
                }
                if (!hasPendingDeallocateMemory()) {
                    postDeallocateMemory();
                }

            }


        }

        public void onFinishInput() {
            if (hasMessages(MSG_PENDING_IMS_CALLBACK)) {

                mHasPendingFinishInput = true;
            } else {
                final CustomKeyBoard customKeyBoard = getOwnerInstance();
                if (customKeyBoard != null) {
                    executePendingImsCallback(customKeyBoard, null, false);
                    customKeyBoard.onFinishInputInternal();
                }
            }

        }

    }

    public void closedPopTool() {
        if (popTool != null && popTool.isShowing()) {
            popTool.dismiss();
        }
    }

    public void changeMyTheme() {
        hideMyKeyboard();
        startShowingInputView(true);
    }

    public void hideMyKeyboard() {
        KeyboardCkSwitcher.getInstance().onHideWindow();

        if (isShowingOptionDialog()) {
            this.mOptionsDialog.dismiss();
            this.mOptionsDialog = null;
        }
        super.hideWindow();
    }

    public void startShowingInputView(boolean z) {

        showWindow(true);

        if (z) {
            loadKeyboard();
        }
    }

    public CustomKeyBoard() {
        super();
        mSettingsCk = SettingsCk.getInstance();
        mKeyboardCkSwitcher = KeyboardCkSwitcher.getInstance();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        this.mInputMethodManager = (InputMethodManager) getSystemService("input_method");

        Instance = this;
        SettingsCk.init(this);
        DebugCkFlags.init(PreferenceCkManagerCompat.getDeviceSharedPreferences(this));
        RichInputMethodCkManager.init(this);
        mRichImm = RichInputMethodCkManager.getInstance();


        mRichImm.setSubtypeChangeHandler(this);
        KeyboardCkSwitcher.init(this);
        AudioAndHapticFeedbackCkManager.init(this);
        super.onCreate();

        mHandler.onCreate();

        // TODO: Resolve mutual dependencies of {@link #loadSettings()} and
        // {@link #resetDictionaryFacilitatorIfNecessary()}.
        loadSettings();

        // Register to receive ringer mode change.
        final IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(mRingerModeChangeReceiver, filter);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

    }

    private String voiceExists(InputMethodManager imeManager) {
        List<InputMethodInfo> list = imeManager.getInputMethodList();
        for (InputMethodInfo el : list) {
            String id = el.getId();
            if (id.contains("com.google.android.voicesearch")) {
                return id;
            }
        }
        return null;
    }


    private void loadSettings() {
        mLocale = mRichImm.getCurrentSubtype().getLocaleObject();
        final EditorInfo editorInfo = getCurrentInputEditorInfo();
        final InputCkAttributes inputCkAttributes = new InputCkAttributes(editorInfo, isFullscreenMode());
        mSettingsCk.loadSettings(inputCkAttributes);
        final SettingsCkValues currentSettingsCkValues = mSettingsCk.getCurrent();
        AudioAndHapticFeedbackCkManager.getInstance().onSettingsChanged(currentSettingsCkValues);
    }

    @Override
    public void onDestroy() {
        mSettingsCk.onDestroy();
        unregisterReceiver(mRingerModeChangeReceiver);


        super.onDestroy();
    }

    private boolean isImeSuppressedByHardwareKeyboard() {
        final KeyboardCkSwitcher switcher = KeyboardCkSwitcher.getInstance();
        return !onEvaluateInputViewShown() && switcher.isImeSuppressedByHardwareKeyboard(
                mSettingsCk.getCurrent(), switcher.getKeyboardSwitchState());
    }

    @Override
    public void onConfigurationChanged(final Configuration conf) {
        SettingsCkValues settingsCkValues = mSettingsCk.getCurrent();
        if (settingsCkValues.mHasHardwareKeyboard != SettingsCk.readHasHardwareKeyboard(conf)) {

            loadSettings();
        }

        mKeyboardCkSwitcher.updateKeyboardTheme(conf.uiMode);

        super.onConfigurationChanged(conf);
    }

    @Override
    public View onCreateInputView() {

        return mKeyboardCkSwitcher.onCreateInputView(getResources().getConfiguration().uiMode);
    }

    @Override
    public void setInputView(final View view) {
        super.setInputView(view);


        mInputView = view;
        storeageCkPref = new StoreageCkPref(this);
        mInsetsUpdater = ViewOutlineProviderCompatCkUtils.setInsetsOutlineProvider(view);

        //this.mEmojiPopup = new SWRX_EmojiPopup(this.mInputView, getApplicationContext());

        btnEmoji = mInputView.findViewById(R.id.btnEmoji);
        btnVoice = mInputView.findViewById(R.id.btnVoice);

        btnSetting = mInputView.findViewById(R.id.btnSetting);
        btnSticker = mInputView.findViewById(R.id.btnSticker);
        btnMore = mInputView.findViewById(R.id.btnMore);


        final View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView().getParent());


        btnEmoji.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                setEmoticons();


            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomKeyBoard.this, MainCsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String voiceExists = voiceExists(mInputMethodManager);
//                if (voiceExists != null) {
//                    final IBinder token = getWindow().getWindow().getAttributes().token;
//                    mInputMethodManager.setInputMethod(token, voiceExists);
//                }
                if (popMic != null && popMic.isShowing()) {
                    popMic.dismiss();
                } else {
                    showMicPop();

                }


            }
        });


        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popTool != null && popTool.isShowing()) {
                    popTool.dismiss();
                } else {
                    showToolPop();

                }
            }
        });
        btnSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStickerPop();
            }
        });

        mediaPlayer = MediaPlayer.create(CustomKeyBoard.this, github.ankushsachdeva.emojicon.R.raw.emoji_sound);
        updateSoftInputWindowLayoutParameters();
    }


    public Intent createIntent(Context context, InputBinding inputBinding, Uri uri) {
        String[] packageNames = context.getPackageManager().getPackagesForUid(inputBinding.getUid());
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.setType("image/*");
        sharingIntent.setPackage(packageNames[0]);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return sharingIntent;
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    @Override
    public void setCandidatesView(final View view) {
        // To ensure that CandidatesView will never be set.
    }

    @Override
    public void onStartInput(final EditorInfo editorInfo, final boolean restarting) {
        mHandler.onStartInput(editorInfo, restarting);
//        SWRX_VoiceRecognitionTrigger sWRX_VoiceRecognitionTrigger = this.mVoiceRecognitionTrigger;
//        if (sWRX_VoiceRecognitionTrigger != null) {
//            sWRX_VoiceRecognitionTrigger.onStartInputView();
//        }
    }

    @Override
    public void onStartInputView(final EditorInfo editorInfo, final boolean restarting) {
        mHandler.onStartInputView(editorInfo, restarting);
    }

    @Override
    public void onFinishInputView(final boolean finishingInput) {
        mRichImm.resetSubtypeCycleOrder();
        mHandler.onFinishInputView(finishingInput);
    }

    @Override
    public void onFinishInput() {
        mHandler.onFinishInput();
    }

    @Override
    public void onCurrentSubtypeChanged() {
        mInputLogicCk.onSubtypeChanged();
        loadKeyboard();
    }

    void onStartInputInternal(final EditorInfo editorInfo, final boolean restarting) {
        super.onStartInput(editorInfo, restarting);

        final Locale primaryHintLocale = EditorInfoCompatCkUtils.getPrimaryHintLocale(editorInfo);
        if (primaryHintLocale == null) {
            return;
        }

        mRichImm.setCurrentSubtype(primaryHintLocale);
    }

    void onStartInputViewInternal(final EditorInfo editorInfo, final boolean restarting) {
        super.onStartInputView(editorInfo, restarting);


        final KeyboardCkSwitcher switcher = mKeyboardCkSwitcher;
        switcher.updateKeyboardTheme(getResources().getConfiguration().uiMode);
        final MainKeyboardCkView mainKeyboardView = switcher.getMainKeyboardView();

        SettingsCkValues currentSettingsCkValues = mSettingsCk.getCurrent();

        if (editorInfo == null) {
            Log.e(TAG, "Null EditorInfo in onStartInputView()");
            if (DebugCkFlags.DEBUG_ENABLED) {
                throw new NullPointerException("Null EditorInfo in onStartInputView()");
            }
            return;
        }


        if (mainKeyboardView == null) {
            return;
        }

        final boolean inputTypeChanged = !currentSettingsCkValues.isSameInputType(editorInfo);
        final boolean isDifferentTextField = !restarting || inputTypeChanged;


        updateFullscreenMode();


        final boolean needToCallLoadKeyboardLater;
        if (!isImeSuppressedByHardwareKeyboard()) {

            mInputLogicCk.startInput();


            if (!mInputLogicCk.mConnection.resetCachesUponCursorMoveAndReturnSuccess(
                    editorInfo.initialSelStart, editorInfo.initialSelEnd)) {

                mHandler.postResetCaches(isDifferentTextField, 5);
                needToCallLoadKeyboardLater = true;
            } else {
                needToCallLoadKeyboardLater = false;
            }
        } else {
            needToCallLoadKeyboardLater = false;
        }

        if (isDifferentTextField ||
                !currentSettingsCkValues.hasSameOrientation(getResources().getConfiguration())) {
            loadSettings();
        }
        if (isDifferentTextField) {
            mainKeyboardView.closing();
            currentSettingsCkValues = mSettingsCk.getCurrent();


            switcher.loadKeyboard(editorInfo, currentSettingsCkValues, getCurrentAutoCapsState(),
                    getCurrentRecapitalizeState());
            if (needToCallLoadKeyboardLater) {
                switcher.saveKeyboardState();
            }

        } else if (restarting) {
            switcher.resetKeyboardStateToAlphabet(getCurrentAutoCapsState(),
                    getCurrentRecapitalizeState());
            switcher.requestUpdatingShiftState(getCurrentAutoCapsState(),
                    getCurrentRecapitalizeState());
        }

        if (popClip != null && popClip.isShowing()) {
            popClip.dismiss();
        }
        if (popGif != null && popGif.isShowing()) {
            popGif.dismiss();
        }
        if (popMic != null && popMic.isShowing()) {
            popMic.dismiss();
        }
        if (popSticker != null && popSticker.isShowing()) {
            popSticker.dismiss();
        }
        if (popTool != null && popTool.isShowing()) {
            popTool.dismiss();
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }


        if (TRACE) Debug.startMethodTracing("/data/trace/latinime");
    }

    @Override
    public void onWindowShown() {
        super.onWindowShown();
        if (isInputViewShown())
            setNavigationBarColor();

    }

    @Override
    public void onWindowHidden() {
        super.onWindowHidden();
        final MainKeyboardCkView mainKeyboardView = mKeyboardCkSwitcher.getMainKeyboardView();
        if (mainKeyboardView != null) {
            mainKeyboardView.closing();
        }
        clearNavigationBarColor();


    }

    void onFinishInputInternal() {
        super.onFinishInput();

        final MainKeyboardCkView mainKeyboardView = mKeyboardCkSwitcher.getMainKeyboardView();
        if (mainKeyboardView != null) {
            mainKeyboardView.closing();
        }
    }

    void onFinishInputViewInternal(final boolean finishingInput) {
        super.onFinishInputView(finishingInput);
    }

    protected void deallocateMemory() {
        mKeyboardCkSwitcher.deallocateMemory();
    }

    @Override
    public void onUpdateSelection(final int oldSelStart, final int oldSelEnd,
                                  final int newSelStart, final int newSelEnd,
                                  final int composingSpanStart, final int composingSpanEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                composingSpanStart, composingSpanEnd);
        if (DebugCkFlags.DEBUG_ENABLED) {
            Log.i(TAG, "onUpdateSelection: oss=" + oldSelStart + ", ose=" + oldSelEnd
                    + ", nss=" + newSelStart + ", nse=" + newSelEnd
                    + ", cs=" + composingSpanStart + ", ce=" + composingSpanEnd);
        }

        if (isInputViewShown()
                && mInputLogicCk.onUpdateSelection(newSelStart, newSelEnd)) {
            mKeyboardCkSwitcher.requestUpdatingShiftState(getCurrentAutoCapsState(),
                    getCurrentRecapitalizeState());
        }
    }

    @Override
    public void hideWindow() {
        mKeyboardCkSwitcher.onHideWindow();


        if (TRACE) Debug.stopMethodTracing();
        if (isShowingOptionDialog()) {
            mOptionsDialog.dismiss();
            mOptionsDialog = null;
        }


        super.hideWindow();
    }

    @Override
    public void onComputeInsets(final Insets outInsets) {
        super.onComputeInsets(outInsets);

        if (mInputView == null) {
            return;
        }

        final View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView().getParent());


        if (visibleKeyboardView == null) {
            return;
        }
        final int inputHeight = mInputView.getHeight();
        if (isImeSuppressedByHardwareKeyboard() && !visibleKeyboardView.isShown()) {


            outInsets.contentTopInsets = inputHeight;
            outInsets.visibleTopInsets = inputHeight;
            mInsetsUpdater.setInsets(outInsets);
            return;
        }
        final int visibleTopY = inputHeight - visibleKeyboardView.getHeight();

        if (visibleKeyboardView.isShown()) {
            final int touchLeft = 0;
            final int touchTop = mKeyboardCkSwitcher.isShowingMoreKeysPanel() ? 0 : visibleTopY;
            final int touchRight = visibleKeyboardView.getWidth();
            final int touchBottom = inputHeight
                    // Extend touchable region below the keyboard.
                    + EXTENDED_TOUCHABLE_REGION_HEIGHT;
            outInsets.touchableInsets = Insets.TOUCHABLE_INSETS_REGION;
            outInsets.touchableRegion.set(touchLeft, touchTop, touchRight, touchBottom);
        }
        outInsets.contentTopInsets = visibleTopY;
        outInsets.visibleTopInsets = visibleTopY;
        mInsetsUpdater.setInsets(outInsets);
    }

    @Override
    public boolean onShowInputRequested(final int flags, final boolean configChange) {
        if (isImeSuppressedByHardwareKeyboard()) {

            return true;
        }

        return super.onShowInputRequested(flags, configChange);
    }

    @Override
    public boolean onEvaluateFullscreenMode() {
        if (isImeSuppressedByHardwareKeyboard()) {
            // If there is a hardware keyboard, disable full screen mode.
            return false;
        }
        // Reread resource value here, because this method is called by the framework as needed.
        final boolean isFullscreenModeAllowed = SettingsCk.readUseFullscreenMode(getResources());
        if (super.onEvaluateFullscreenMode() && isFullscreenModeAllowed) {
            // TODO: Remove this hack. Actually we should not really assume NO_EXTRACT_UI
            // implies NO_FULLSCREEN. However, the framework mistakenly does.  i.e. NO_EXTRACT_UI
            // without NO_FULLSCREEN doesn't work as expected. Because of this we need this
            // hack for now.  Let's get rid of this once the framework gets fixed.
            final EditorInfo ei = getCurrentInputEditorInfo();
            return !(ei != null && ((ei.imeOptions & EditorInfo.IME_FLAG_NO_EXTRACT_UI) != 0));
        }
        return false;
    }

    @Override
    public void updateFullscreenMode() {
        super.updateFullscreenMode();
        updateSoftInputWindowLayoutParameters();
    }

    private void updateSoftInputWindowLayoutParameters() {

        final Window window = getWindow().getWindow();
        ViewLayoutCkUtils.updateLayoutHeightOf(window, LayoutParams.MATCH_PARENT);

        if (mInputView != null) {

            final int layoutHeight = isFullscreenMode()
                    ? LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT;
            final View inputArea = window.findViewById(android.R.id.inputArea);
            ViewLayoutCkUtils.updateLayoutHeightOf(inputArea, layoutHeight);
            ViewLayoutCkUtils.updateLayoutGravityOf(inputArea, Gravity.BOTTOM);
            ViewLayoutCkUtils.updateLayoutHeightOf(mInputView, layoutHeight);
        }
    }

    int getCurrentAutoCapsState() {
        return mInputLogicCk.getCurrentAutoCapsState(mSettingsCk.getCurrent(),
                mRichImm.getCurrentSubtype().getKeyboardLayoutSet());
    }

    int getCurrentRecapitalizeState() {
        return mInputLogicCk.getCurrentRecapitalizeState();
    }

    @Override
    public boolean onCustomRequest(final int requestCode) {
        switch (requestCode) {
            case ConstantsCk.CUSTOM_CODE_SHOW_INPUT_METHOD_PICKER:
                return showInputMethodPicker();
        }
        return false;
    }

    private boolean showInputMethodPicker() {
        if (isShowingOptionDialog()) {
            return false;
        }
        mOptionsDialog = mRichImm.showSubtypePicker(this,
                mKeyboardCkSwitcher.getMainKeyboardView().getWindowToken(), this);
        return mOptionsDialog != null;
    }

    public Locale getCurrentLayoutLocale() {
        return mLocale;
    }

    @Override
    public void onMovePointer(int steps) {
        if (mInputLogicCk.mConnection.hasCursorPosition()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (TextUtils.getLayoutDirectionFromLocale(getCurrentLayoutLocale()) == View.LAYOUT_DIRECTION_RTL)
                    steps = -steps;
            }

            steps = mInputLogicCk.mConnection.getUnicodeSteps(steps, true);
            final int end = mInputLogicCk.mConnection.getExpectedSelectionEnd() + steps;
            final int start = mInputLogicCk.mConnection.hasSelection() ? mInputLogicCk.mConnection.getExpectedSelectionStart() : end;
            mInputLogicCk.mConnection.setSelection(start, end);
        } else {
            for (; steps < 0; steps++)
                mInputLogicCk.sendDownUpKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT, null);
            for (; steps > 0; steps--)
                mInputLogicCk.sendDownUpKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }
    }

    @Override
    public void onMoveDeletePointer(int steps) {
        if (mInputLogicCk.mConnection.hasCursorPosition()) {
            steps = mInputLogicCk.mConnection.getUnicodeSteps(steps, false);
            final int end = mInputLogicCk.mConnection.getExpectedSelectionEnd();
            final int start = mInputLogicCk.mConnection.getExpectedSelectionStart() + steps;
            if (start > end)
                return;
            mInputLogicCk.mConnection.setSelection(start, end);
        } else {
            for (; steps < 0; steps++)
                mInputLogicCk.sendDownUpKeyEvent(KeyEvent.KEYCODE_DEL, null);
        }
    }

    @Override
    public void onUpWithDeletePointerActive() {
        if (mInputLogicCk.mConnection.hasSelection())
            mInputLogicCk.sendDownUpKeyEvent(KeyEvent.KEYCODE_DEL, null);
    }

    private boolean isShowingOptionDialog() {
        return mOptionsDialog != null && mOptionsDialog.isShowing();
    }

    public void switchToNextSubtype() {
        final IBinder token = getWindow().getWindow().getAttributes().token;
        mRichImm.switchToNextInputMethod(token, !shouldSwitchToOtherInputMethods(token));
    }


    private int getCodePointForKeyboard(final int codePoint) {
        if (ConstantsCk.CODE_SHIFT == codePoint) {
            final KeyboardCk currentKeyboardCk = mKeyboardCkSwitcher.getKeyboard();
            if (null != currentKeyboardCk && currentKeyboardCk.mId.isAlphabetKeyboard()) {
                return codePoint;
            }
            return ConstantsCk.CODE_SYMBOL_SHIFT;
        }
        return codePoint;
    }

    // Implementation of {@link KeyboardActionListener}.
    @Override
    public void onCodeInput(final int codePoint, final int x, final int y,
                            final boolean isKeyRepeat) {
        final MainKeyboardCkView mainKeyboardView = mKeyboardCkSwitcher.getMainKeyboardView();

        final int keyX = mainKeyboardView.getKeyX(x);
        final int keyY = mainKeyboardView.getKeyY(y);
        final EventCk eventCk = createSoftwareKeypressEvent(getCodePointForKeyboard(codePoint),
                keyX, keyY, isKeyRepeat);
        onEvent(eventCk);
    }


    public void onEvent(final EventCk eventCk) {
        final InputTransactionCk completeInputTransactionCk =
                mInputLogicCk.onCodeInput(mSettingsCk.getCurrent(), eventCk);
        updateStateAfterInputTransaction(completeInputTransactionCk);
        mKeyboardCkSwitcher.onEvent(eventCk, getCurrentAutoCapsState(), getCurrentRecapitalizeState());
    }

    // A helper method to split the code point and the key code. Ultimately, they should not be
    // squashed into the same variable, and this method should be removed.
    // public for testing, as we don't want to copy the same logic into test code
    public static EventCk createSoftwareKeypressEvent(final int keyCodeOrCodePoint, final int keyX,
                                                      final int keyY, final boolean isKeyRepeat) {
        final int keyCode;
        final int codePoint;
        if (keyCodeOrCodePoint <= 0) {
            keyCode = keyCodeOrCodePoint;
            codePoint = EventCk.NOT_A_CODE_POINT;
        } else {
            keyCode = EventCk.NOT_A_KEY_CODE;
            codePoint = keyCodeOrCodePoint;
        }
        return EventCk.createSoftwareKeypressEvent(codePoint, keyCode, keyX, keyY, isKeyRepeat);
    }

    // Called from PointerTracker through the KeyboardActionListener interface
    @Override
    public void onTextInput(final String rawText) {

        // TODO: have the keyboard pass the correct key code when we need it.
        final EventCk eventCk = EventCk.createSoftwareTextEvent(rawText, ConstantsCk.CODE_OUTPUT_TEXT);
        final InputTransactionCk completeInputTransactionCk =
                mInputLogicCk.onTextInput(mSettingsCk.getCurrent(), eventCk);
        updateStateAfterInputTransaction(completeInputTransactionCk);
        mKeyboardCkSwitcher.onEvent(eventCk, getCurrentAutoCapsState(), getCurrentRecapitalizeState());
    }

    // Called from PointerTracker through the KeyboardActionListener interface
    @Override
    public void onFinishSlidingInput() {
        // User finished sliding input.
        mKeyboardCkSwitcher.onFinishSlidingInput(getCurrentAutoCapsState(),
                getCurrentRecapitalizeState());
    }

    private void loadKeyboard() {

        loadSettings();
        if (mKeyboardCkSwitcher.getMainKeyboardView() != null) {

            mKeyboardCkSwitcher.loadKeyboard(getCurrentInputEditorInfo(), mSettingsCk.getCurrent(),
                    getCurrentAutoCapsState(), getCurrentRecapitalizeState());

        }
    }


    private void updateStateAfterInputTransaction(final InputTransactionCk inputTransactionCk) {
        switch (inputTransactionCk.getRequiredShiftUpdate()) {
            case InputTransactionCk.SHIFT_UPDATE_LATER:
                mHandler.postUpdateShiftState();
                break;
            case InputTransactionCk.SHIFT_UPDATE_NOW:
                mKeyboardCkSwitcher.requestUpdatingShiftState(getCurrentAutoCapsState(),
                        getCurrentRecapitalizeState());
                break;
            default:
        }
    }

    private void hapticAndAudioFeedback(final int code, final int repeatCount) {
        final MainKeyboardCkView keyboardView = mKeyboardCkSwitcher.getMainKeyboardView();
        if (keyboardView != null && keyboardView.isInDraggingFinger()) {

            return;
        }
        if (repeatCount > 0) {
            if (code == ConstantsCk.CODE_DELETE && !mInputLogicCk.mConnection.canDeleteCharacters()) {

                return;
            }


            if (repeatCount % PERIOD_FOR_AUDIO_AND_HAPTIC_FEEDBACK_IN_KEY_REPEAT == 0) {
                return;
            }
        }
        final AudioAndHapticFeedbackCkManager feedbackManager =
                AudioAndHapticFeedbackCkManager.getInstance();
        if (repeatCount == 0) {
            feedbackManager.performHapticFeedback(keyboardView);
        }
        feedbackManager.performAudioFeedback(code);
    }


    @Override
    public void onPressKey(final int primaryCode, final int repeatCount,
                           final boolean isSinglePointer) {


        mKeyboardCkSwitcher.onPressKey(primaryCode, isSinglePointer, getCurrentAutoCapsState(),
                getCurrentRecapitalizeState());
        hapticAndAudioFeedback(primaryCode, repeatCount);
    }

    @Override
    public void onReleaseKey(final int primaryCode, final boolean withSliding) {
        mKeyboardCkSwitcher.onReleaseKey(primaryCode, withSliding, getCurrentAutoCapsState(),
                getCurrentRecapitalizeState());
    }


    private final BroadcastReceiver mRingerModeChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (action.equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                AudioAndHapticFeedbackCkManager.getInstance().onRingerModeChanged();
            }
        }
    };

    public void launchSettings() {
        requestHideSelf(0);
        final MainKeyboardCkView mainKeyboardView = mKeyboardCkSwitcher.getMainKeyboardView();
        if (mainKeyboardView != null) {
            mainKeyboardView.closing();
        }
        final Intent intent = new Intent();
        intent.setClass(CustomKeyBoard.this, SettingCsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void debugDumpStateAndCrashWithException(final String context) {
        final SettingsCkValues settingsCkValues = mSettingsCk.getCurrent();
        final StringBuilder s = new StringBuilder(settingsCkValues.toString());
        s.append("\nAttributes : ").append(settingsCkValues.mInputCkAttributes)
                .append("\nContext : ").append(context);
        throw new RuntimeException(s.toString());
    }

    @Override
    protected void dump(final FileDescriptor fd, final PrintWriter fout, final String[] args) {
        super.dump(fd, fout, args);

        final Printer p = new PrintWriterPrinter(fout);
        p.println("LatinIME state :");
        p.println("  VersionCode = " + ApplicationCkUtils.getVersionCode(this));
        p.println("  VersionName = " + ApplicationCkUtils.getVersionName(this));
        final KeyboardCk keyboardCk = mKeyboardCkSwitcher.getKeyboard();
        final int keyboardMode = keyboardCk != null ? keyboardCk.mId.mMode : -1;
        p.println("  Keyboard mode = " + keyboardMode);
    }

    public boolean shouldSwitchToOtherInputMethods(final IBinder token) {

        if (!mSettingsCk.getCurrent().mImeSwitchEnabled) {
            return false;
        }
        return mRichImm.shouldOfferSwitchingToOtherInputMethods(token);
    }

    public boolean shouldShowLanguageSwitchKey() {
        if (mSettingsCk.getCurrent().isLanguageSwitchKeyDisabled()) {
            return false;
        }
        if (mRichImm.hasMultipleEnabledSubtypes()) {
            return true;
        }

        final IBinder token = getWindow().getWindow().getAttributes().token;
        if (token == null) {
            return false;
        }
        return shouldSwitchToOtherInputMethods(token);
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && mSettingsCk.getCurrent().mUseMatchingNavbarColor) {
            final SharedPreferences prefs = PreferenceCkManagerCompat.getDeviceSharedPreferences(this);
            final int keyboardColor = SettingsCk.readKeyboardColor(prefs, this);
            final Window window = getWindow().getWindow();
            if (window == null) {
                return;
            }
            mOriginalNavBarColor = window.getNavigationBarColor();
            window.setNavigationBarColor(keyboardColor);

            final View view = window.getDecorView();
            mOriginalNavBarFlags = view.getSystemUiVisibility();
            if (ResourceCkUtils.isBrightColor(keyboardColor)) {
                view.setSystemUiVisibility(mOriginalNavBarFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            } else {
                view.setSystemUiVisibility(mOriginalNavBarFlags & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }
    }

    private void clearNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && mSettingsCk.getCurrent().mUseMatchingNavbarColor) {
            final Window window = getWindow().getWindow();
            if (window == null) {
                return;
            }
            window.setNavigationBarColor(mOriginalNavBarColor);
            final View view = window.getDecorView();
            view.setSystemUiVisibility(mOriginalNavBarFlags);
        }
    }

    public void setEmoticons() {
        final int themeIdEmoticons = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;
        final View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView().getParent());
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            View popupView = layoutInflater.inflate(R.layout.emojicons, null);
            popupWindow = new EmojiconsPopup(popupView, this, themeIdEmoticons);
            popupWindow.setSizeForSoftKeyboard();
            // popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            popupWindow.setBackgroundDrawable(getBgForPop(false));

            int height = visibleKeyboardView.getHeight();
            int total = height + getResources().getDimensionPixelSize(R.dimen._40sdp);
            popupWindow.setSize(LayoutParams.MATCH_PARENT, height);


            popupWindow.showAtLocation(visibleKeyboardView.getRootView(), Gravity.BOTTOM, 0, 0);


            popupWindow.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
                @Override
                public void onKeyboardOpen(int keyBoardHeight) {
                }

                @Override
                public void onKeyboardClose() {
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
            });
            popupWindow.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
                @Override
                public void onEmojiconClicked(Emojicon emojicon, TextView icon) {


                    mComposing.append(emojicon.getEmoji());
                    commitTyped(getCurrentInputConnection());
                }
            });
            popupWindow.setOnEmojiconLongClickedListener(new EmojiconGridView.OnEmojiconLongClickedListener() {
                @Override
                public void onEmojiconLongClicked(File file) throws IOException {

//                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider",  CopyRAWtoSDCard(0));
//                        commitGifImage(uri);
                }

            });
            popupWindow.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {
                @Override
                public void onEmojiconBackspaceClicked(View v) {
                    handleBackspace();
                }
            });
        }
    }


    private void commitTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {


            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);
            //   inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));

            /// updateCandidates();
        }
    }

    public void closeEmoticons() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    private void handleBackspace() {
        if (CustomKeyBoard.Instance != null) {

            InputConnection currentInputConnection = CustomKeyBoard.Instance.getCurrentInputConnection();
            if (CustomKeyBoard.Instance.mComposing.length() > 1) {
                CustomKeyBoard.Instance.mComposing.delete(CustomKeyBoard.Instance.mComposing.length() - 1, CustomKeyBoard.Instance.mComposing.length());
                currentInputConnection.setComposingText(CustomKeyBoard.Instance.mComposing, 1);

            } else if (CustomKeyBoard.Instance.mComposing.length() > 0) {
                CustomKeyBoard.Instance.mComposing.setLength(0);
                currentInputConnection.commitText("", 0);

            } else {
                currentInputConnection.sendKeyEvent(new KeyEvent(0, 67));
                currentInputConnection.sendKeyEvent(new KeyEvent(1, 67));
            }

        }
    }

    private File CopyRAWtoStorage(int raw_id) throws IOException {

        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getFilesDir(), "keybg");
            // file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "keybg");
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "keybg");
        }

        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(file, "sti.webp");

        file1.setWritable(true, false);
        InputStream in = getResources().openRawResource(raw_id);
        FileOutputStream out = new FileOutputStream(file1);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }


        return file1;
    }

    private File CopyRAWtoSDCard(int raw_id) throws IOException {
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getFilesDir(), "keybg");
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "keybg");
        }

        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(file, "gifff.gif");

        file1.setWritable(true, false);
        InputStream in = getResources().openRawResource(raw_id);
        FileOutputStream out = new FileOutputStream(file1);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
        return file1;
    }

    private void commitSticker(Uri contentUri) {
        InputContentInfoCompat inputContentInfo;
        EditorInfo editorInfo = getCurrentInputEditorInfo();


        if (isCommitContentSupported(editorInfo, "image/webp.wasticker")) {
            inputContentInfo = new InputContentInfoCompat(contentUri, new ClipDescription("", new String[]{"image/webp.wasticker"}), null);

            InputConnection inputConnection = getCurrentInputConnection();
            int flags = 0;
            if (android.os.Build.VERSION.SDK_INT >= 25) {
                flags |= InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
            }


            InputConnectionCompat.commitContent(inputConnection, editorInfo, inputContentInfo, flags, null);
        } else {
            Toast.makeText(this, "Sticker Not Support", Toast.LENGTH_LONG).show();

        }
    }

    private void commitGifImage(@NonNull Uri contentUri) {

        try {
            InputContentInfoCompat inputContentInfo = new InputContentInfoCompat(contentUri, new ClipDescription("", new String[]{"image/gif"}), null);


            InputConnection inputConnection = getCurrentInputConnection();

            EditorInfo editorInfo = getCurrentInputEditorInfo();

            if (isCommitContentSupported(editorInfo, "image/gif")) {

                int flags = 0;
                if (android.os.Build.VERSION.SDK_INT >= 25) {
                    flags |= InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
                }
                InputConnectionCompat.commitContent(inputConnection, editorInfo, inputContentInfo, flags, null);
            } else {
                Toast.makeText(this, "GIF Not Support", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showPop() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_gif, null);

        ImageView btnKeyboard = (ImageView) customView.findViewById(R.id.btnKeyboard);
        RecyclerView rvGif = (RecyclerView) customView.findViewById(R.id.recyclerGif);
        ConstraintLayout cv = (ConstraintLayout) customView.findViewById(R.id.cv);
        TextView tvLabel = (TextView) customView.findViewById(R.id.tvLabel);

        popGif = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        final View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView().getParent());
        popGif.setWidth(LayoutParams.MATCH_PARENT);
        popGif.setHeight(visibleKeyboardView.getHeight());
        popGif.setBackgroundDrawable(getBgForPop(false));
        popGif.showAtLocation(visibleKeyboardView.getRootView(), Gravity.BOTTOM, 0, 0);
        btnKeyboard.setBackground(getIconForTheme());
        tvLabel.setTextColor(getTextForThem());

        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popGif.dismiss();
            }
        });
        ArrayList<Integer> gifList = new ArrayList<>();
        gifList.add(R.raw.abc);
        gifList.add(R.raw.giff1);
        gifList.add(R.raw.giff2);
        gifList.add(R.raw.giff3);
        gifList.add(R.raw.giff4);
        gifList.add(R.raw.giff5);
        gifList.add(R.raw.giff6);
        gifList.add(R.raw.giff7);
        gifList.add(R.raw.giff8);
        AdapterGif adapterDown = new AdapterGif(this, gifList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvGif.setLayoutManager(gridLayoutManager);
        rvGif.setAdapter(adapterDown);

        adapterDown.setOnItemClickListener(new AdapterGif.OnItemClickListener1() {
            @Override
            public void onItemClick1(Integer pos, View v) throws IOException {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                    try {
                        MediaScannerConnection.scanFile(CustomKeyBoard.this, new String[]{CopyRAWtoSDCard(pos).toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                commitGifImage(uri);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", CopyRAWtoSDCard(pos));
                    //passImage(uri);
                    commitGifImage(uri);
                }
            }
        });

    }

    private void showStickerPop() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_sti, null);

        ImageView btnKeyboard = (ImageView) customView.findViewById(R.id.btnKeyboard);
        RecyclerView rvSticker = (RecyclerView) customView.findViewById(R.id.recyclerSti);
        RecyclerView rvCSticker = (RecyclerView) customView.findViewById(R.id.recyclerSt2);
        RecyclerView rvTextSticker = (RecyclerView) customView.findViewById(R.id.recyclerSt3);

        TextView tv1 = (TextView) customView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) customView.findViewById(R.id.tv2);
        TextView tv3 = (TextView) customView.findViewById(R.id.tv3);
        TextView tvLabel1 = (TextView) customView.findViewById(R.id.tvLabel1);
        TextView tvLabel2 = (TextView) customView.findViewById(R.id.tvLabel2);
        TextView tvLabel3 = (TextView) customView.findViewById(R.id.tvLabel3);
        ConstraintLayout cv1 = (ConstraintLayout) customView.findViewById(R.id.cv1);
        ConstraintLayout cv2 = (ConstraintLayout) customView.findViewById(R.id.cv2);
        ConstraintLayout cv3 = (ConstraintLayout) customView.findViewById(R.id.cv3);
        ConstraintLayout cv = (ConstraintLayout) customView.findViewById(R.id.container);
        ConstraintLayout cv1Main = (ConstraintLayout) customView.findViewById(R.id.cv1Main);
        ConstraintLayout cv1Empty = (ConstraintLayout) customView.findViewById(R.id.cv1Empty);
        ConstraintLayout cv2Main = (ConstraintLayout) customView.findViewById(R.id.cv2Main);
        ConstraintLayout cv2Empty = (ConstraintLayout) customView.findViewById(R.id.cv2Empty);
        ConstraintLayout cv3Main = (ConstraintLayout) customView.findViewById(R.id.cv3Main);
        ConstraintLayout cv3Empty = (ConstraintLayout) customView.findViewById(R.id.cv3Empty);

        ImageView expanded_image = (ImageView) customView.findViewById(R.id.expanded_image);
        tv1.setTextColor(getTextForThem());
        tv2.setTextColor(getTextForThem());
        tv3.setTextColor(getTextForThem());
        tvLabel1.setTextColor(getTextForThem());
        tvLabel2.setTextColor(getTextForThem());
        tvLabel3.setTextColor(getTextForThem());

        btnKeyboard.setBackground(getIconForTheme());

        tv1.setBackground(getTVBgForTheme());

        popSticker = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView().getParent());
        popSticker.setWidth(LayoutParams.MATCH_PARENT);
        popSticker.setHeight(visibleKeyboardView.getHeight());
        popSticker.setBackgroundDrawable(getBgForPop(false));
        popSticker.showAtLocation(visibleKeyboardView.getRootView(), Gravity.BOTTOM, 0, 0);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setBackground(getTVBgForTheme());


                tv2.setBackground(null);
                tv3.setBackground(null);
                cv1.setVisibility(View.VISIBLE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setBackground(getTVBgForTheme());
                tv1.setBackground(null);
                tv3.setBackground(null);

                cv2.setVisibility(View.VISIBLE);
                cv1.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv3.setBackground(getTVBgForTheme());
                tv2.setBackground(null);
                tv1.setBackground(null);

                cv3.setVisibility(View.VISIBLE);
                cv2.setVisibility(View.GONE);
                cv1.setVisibility(View.GONE);

            }
        });

        tvLabel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openS(0);
            }
        });
        tvLabel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openS(1);
            }
        });
        tvLabel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openS(3);
            }
        });
        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSticker.dismiss();
            }
        });
        if (!storeageCkPref.getSHOW_AS_LST()) {
            cv1Empty.setVisibility(View.VISIBLE);
            cv1Main.setVisibility(View.GONE);
        } else {
            cv1Empty.setVisibility(View.GONE);
            cv1Main.setVisibility(View.VISIBLE);
        }
        if (!storeageCkPref.getSHOW_TS_LST()) {
            cv3Empty.setVisibility(View.VISIBLE);
            cv3Main.setVisibility(View.GONE);
        } else {
            cv3Empty.setVisibility(View.GONE);
            cv3Main.setVisibility(View.VISIBLE);
        }

        if (storeageCkPref.getCsList() != null && !storeageCkPref.getCsList().isEmpty()) {

            cv2Empty.setVisibility(View.GONE);
            cv2Main.setVisibility(View.VISIBLE);
        } else {
            cv2Empty.setVisibility(View.VISIBLE);
            cv2Main.setVisibility(View.GONE);
        }
        ArrayList<Integer> stiList = new ArrayList<>();
        stiList.add(R.raw.as1);
        stiList.add(R.raw.as2);
        stiList.add(R.raw.as3);
        stiList.add(R.raw.as4);
        stiList.add(R.raw.as5);
        stiList.add(R.raw.as6);
        stiList.add(R.raw.as7);
        stiList.add(R.raw.as8);
        stiList.add(R.raw.as9);
        stiList.add(R.raw.as10);
        stiList.add(R.raw.as12);
        stiList.add(R.raw.as15);


        AdapterSti adapterSti = new AdapterSti(this, stiList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvSticker.setLayoutManager(gridLayoutManager);
        rvSticker.setAdapter(adapterSti);

        adapterSti.setOnItemClickListener(new AdapterSti.OnItemClickListener1() {
            @Override
            public void onItemClick1(Integer pos, ImageView v) throws IOException {

                if (SystemClock.elapsedRealtime() - lClickTime < 1900) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();
                zoomImageFromThumb(v, pos, expanded_image, cv);
            }
        });
        ArrayList<Integer> textStiList = new ArrayList<>();
        textStiList.add(R.raw.s1);
        textStiList.add(R.raw.s2);
        textStiList.add(R.raw.s3);
        textStiList.add(R.raw.s4);
        textStiList.add(R.raw.s5);
        textStiList.add(R.raw.s6);
        textStiList.add(R.raw.s7);
        textStiList.add(R.raw.s8);
        textStiList.add(R.raw.s9);
        textStiList.add(R.raw.s10);
        textStiList.add(R.raw.s11);
        textStiList.add(R.raw.s12);
        textStiList.add(R.raw.s13);
        textStiList.add(R.raw.s14);
        textStiList.add(R.raw.s15);
        textStiList.add(R.raw.s16);


        AdapterTextSti adapterTextSti = new AdapterTextSti(this, textStiList);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvTextSticker.setLayoutManager(gridLayoutManager1);
        rvTextSticker.setAdapter(adapterTextSti);

        adapterTextSti.setOnItemClickListener(new AdapterTextSti.OnItemClickListener1() {
            @Override
            public void onItemClick1(Integer pos, ImageView v) throws IOException {

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                    try {
                        MediaScannerConnection.scanFile(CustomKeyBoard.this, new String[]{CopyRAWtoStorage(pos).toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {


                            public void onScanCompleted(String path, Uri uri) {
                                commitSticker(uri);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Uri uri = null;
                    try {
                        uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", CopyRAWtoStorage(pos));
                        // getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        commitSticker(uri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        ArrayList<Integer> csList = new ArrayList<>();
        if (storeageCkPref.getCsList() != null && !storeageCkPref.getCsList().isEmpty()) {
            csList = storeageCkPref.getCsList();
        }

        AdapterCSti adapterCSti = new AdapterCSti(this, csList);

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvCSticker.setLayoutManager(gridLayoutManager2);
        rvCSticker.setAdapter(adapterCSti);

        adapterCSti.setOnItemClickListener(new AdapterCSti.OnItemClickListener1() {
            @Override
            public void onItemClick1(Integer pos, ImageView v) throws IOException {
                if (SystemClock.elapsedRealtime() - lClickTime < 1900) {
                    return;
                }
                lClickTime = SystemClock.elapsedRealtime();
                zoomImageFromThumb(v, pos, expanded_image, cv);
            }
        });
        shortAnimationDuration = getResources().getInteger(R.integer.config_longAnimTime1);

    }

    private void openS(int i) {
        Intent intent = new Intent(CustomKeyBoard.this, AllStickerAddCsActivity.class);
        intent.putExtra("set", i);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void showClipPop() {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_clip, null);

        ImageView btnKeyboard = (ImageView) customView.findViewById(R.id.btnKeyboard);
        RecyclerView rvClip = (RecyclerView) customView.findViewById(R.id.recyclerClip);
        TextView tvLabel = (TextView) customView.findViewById(R.id.tvLabel);

        popClip = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        final View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView().getParent());
        popClip.setWidth(LayoutParams.MATCH_PARENT);
        popClip.setHeight(visibleKeyboardView.getHeight());
        popClip.setBackgroundDrawable(getBgForPop(false));
        popClip.showAtLocation(visibleKeyboardView.getRootView(), Gravity.BOTTOM, 0, 0);
        btnKeyboard.setBackground(getIconForTheme());
        tvLabel.setTextColor(getTextForThem());

        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popClip.dismiss();
            }
        });
        @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");
        if (clipboardManager.getPrimaryClip() != null) {
            for (int i = 0; i < clipboardManager.getPrimaryClip().getItemCount(); i++) {
                try {
                    if (!(clipboardManager.getPrimaryClip() == null || clipboardManager.getPrimaryClip().getItemAt(i) == null || clipboardManager.getPrimaryClip().getItemAt(i).getText().length() == 0)) {
                        ArrayList<String> arrayList = clipboard;
                        if (!arrayList.contains("" + ((Object) clipboardManager.getPrimaryClip().getItemAt(i).getText()))) {
                            ArrayList<String> arrayList2 = clipboard;
                            arrayList2.add("" + ((Object) clipboardManager.getPrimaryClip().getItemAt(i).getText()));
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }

        Collections.reverse(clipboard);

        AdapterClip adapterClip = new AdapterClip(this, clipboard, getTextForThem());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvClip.setLayoutManager(linearLayoutManager);
        rvClip.setAdapter(adapterClip);

        adapterClip.setOnItemClickListener(new AdapterClip.OnItemClickListener1() {
            @Override
            public void onItemClick1(String pos) throws IOException {


                getCurrentInputConnection().commitText(pos, 1);
            }
        });
    }

    private void showMicPop() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_mic, null);
        ImageView btnClear = customView.findViewById(R.id.btnClear);
        ImageView btnKeyboard = customView.findViewById(R.id.btnKeyboard);
        TextView tvLabel = customView.findViewById(R.id.tvLabel);
        ImageView btnMic = customView.findViewById(R.id.btnMic);
        LottieAnimationView animView = customView.findViewById(R.id.lottieAnimationView1);
        popMic = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView());
        popMic.setWidth(LayoutParams.MATCH_PARENT);
        popMic.setHeight(visibleKeyboardView.getHeight());
        popMic.setBackgroundDrawable(getBgForPop(true));
        popMic.showAtLocation(visibleKeyboardView.getRootView(), Gravity.BOTTOM, 0, 0);


        tvLabel.setTextColor(getTextForThem());
        final int themeIdPopTool = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;
        if (themeIdPopTool == 0 || themeIdPopTool == 1 || themeIdPopTool == 3 || themeIdPopTool == 4 || themeIdPopTool == 5) {
            btnClear.setImageResource(R.drawable.btn_key_th_0_delete);
            btnKeyboard.setImageResource(R.drawable.th_0_keyboard);
            btnMic.setImageResource(R.drawable.ck_mic_w);
            animView.setAnimation("mic_lottie2.json");
        } else if (themeIdPopTool == 2 || themeIdPopTool == 6 || themeIdPopTool == 7) {

            btnClear.setImageResource(R.drawable.btn_key_th_2_delete);
            btnKeyboard.setImageResource(R.drawable.th_2_keyboard);
            btnMic.setImageResource(R.drawable.ck_mic_b);
            animView.setAnimation("mic_lottie3.json");
        }

        animView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (animView.isAnimating()) {
                    speechRecognizer.stopListening();
                    animView.pauseAnimation();
                    tvLabel.setText(getResources().getString(R.string.mic));
                } else {
                    tvLabel.setText("Listening...");
                    speechRecognizer.startListening(speechRecognizerIntent);

                    animView.playAnimation();
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputConnection currentInputConnection = Instance.getCurrentInputConnection();
                currentInputConnection.deleteSurroundingText(1, 0);
            }
        });
        btnClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                InputConnection inputConnection = getCurrentInputConnection();

                CharSequence currentText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
                CharSequence beforCursorText = inputConnection.getTextBeforeCursor(currentText.length(), 0);
                CharSequence afterCursorText = inputConnection.getTextAfterCursor(currentText.length(), 0);
                inputConnection.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
                return false;
            }
        });

        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMic.dismiss();
            }
        });
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                tvLabel.setText(getResources().getString(R.string.mic));
                animView.pauseAnimation();
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Instance.getCurrentInputConnection().commitText(data.get(0), 1);
                tvLabel.setText(getResources().getString(R.string.mic));
                animView.pauseAnimation();

            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Instance.getCurrentInputConnection().commitText(data.get(0), 1);
                tvLabel.setText(getResources().getString(R.string.mic));
                animView.pauseAnimation();
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        popMic.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                speechRecognizer.stopListening();

            }
        });

    }

    private void showToolPop() {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_tool, null);

        popTool = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ImageView btnCopy = customView.findViewById(R.id.btnCopy);
        ImageView btnClipBoard = customView.findViewById(R.id.btnClipboard);
        ImageView btnGif = customView.findViewById(R.id.btnGif);
        ImageView btnTheme = customView.findViewById(R.id.btnTheme);
        ImageView btnFont = customView.findViewById(R.id.btnFont);
        ImageView btnSticker = customView.findViewById(R.id.btnSticker);

        ConstraintLayout cv1 = customView.findViewById(R.id.cv1);
        ConstraintLayout cv2 = customView.findViewById(R.id.cv2);
        ConstraintLayout cv3 = customView.findViewById(R.id.cv3);
        ConstraintLayout cv4 = customView.findViewById(R.id.cv4);
        ConstraintLayout cv5 = customView.findViewById(R.id.cv5);
        ConstraintLayout cv6 = customView.findViewById(R.id.cv6);

        final int themeIdPopTool = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;

        if (themeIdPopTool == 2 || themeIdPopTool == 6 || themeIdPopTool == 7) {
            cv1.setBackground(getResources().getDrawable(R.drawable.dr_bg_b));
            cv2.setBackground(getResources().getDrawable(R.drawable.dr_bg_b));
            cv3.setBackground(getResources().getDrawable(R.drawable.dr_bg_b));
            cv4.setBackground(getResources().getDrawable(R.drawable.dr_bg_b));
            cv5.setBackground(getResources().getDrawable(R.drawable.dr_bg_b));
            cv6.setBackground(getResources().getDrawable(R.drawable.dr_bg_b));

            btnCopy.setImageResource(R.drawable.th_2_copy);
            btnClipBoard.setImageResource(R.drawable.th_2_clipboard);
            btnGif.setImageResource(R.drawable.th_2_gif);
            btnTheme.setImageResource(R.drawable.th_2_keyboard);
            btnFont.setImageResource(R.drawable.th_2_fancy_text);
            btnSticker.setImageResource(R.drawable.emoji_w);
        } else if (themeIdPopTool == 1) {
            btnCopy.setImageResource(R.drawable.th_0_copy);
            btnClipBoard.setImageResource(R.drawable.th_0_clipboard);
            btnGif.setImageResource(R.drawable.th_0_gif);
            btnTheme.setImageResource(R.drawable.th_0_keyboard);
            btnFont.setImageResource(R.drawable.th_0_fancy_text);
            btnSticker.setImageResource(R.drawable.emoji_black);
            cv1.setBackground(getResources().getDrawable(R.drawable.dr_bg_w));
            cv2.setBackground(getResources().getDrawable(R.drawable.dr_bg_w));
            cv3.setBackground(getResources().getDrawable(R.drawable.dr_bg_w));
            cv4.setBackground(getResources().getDrawable(R.drawable.dr_bg_w));
            cv5.setBackground(getResources().getDrawable(R.drawable.dr_bg_w));
            cv6.setBackground(getResources().getDrawable(R.drawable.dr_bg_w));
        } else if (themeIdPopTool == 5) {
            cv1.setBackground(getResources().getDrawable(R.drawable.dr_bg5_b));
            cv2.setBackground(getResources().getDrawable(R.drawable.dr_bg5_b));
            cv3.setBackground(getResources().getDrawable(R.drawable.dr_bg5_b));
            cv4.setBackground(getResources().getDrawable(R.drawable.dr_bg5_b));
            cv5.setBackground(getResources().getDrawable(R.drawable.dr_bg5_b));
            cv6.setBackground(getResources().getDrawable(R.drawable.dr_bg5_b));

            btnCopy.setImageResource(R.drawable.th_2_copy);
            btnClipBoard.setImageResource(R.drawable.th_2_clipboard);
            btnGif.setImageResource(R.drawable.th_2_gif);
            btnTheme.setImageResource(R.drawable.th_2_keyboard);
            btnFont.setImageResource(R.drawable.th_2_fancy_text);
            btnSticker.setImageResource(R.drawable.emoji_w);
        } else if (themeIdPopTool == 4) {
            cv1.setBackground(getResources().getDrawable(R.drawable.dr_bg4_b));
            cv2.setBackground(getResources().getDrawable(R.drawable.dr_bg4_b));
            cv3.setBackground(getResources().getDrawable(R.drawable.dr_bg4_b));
            cv4.setBackground(getResources().getDrawable(R.drawable.dr_bg4_b));
            cv5.setBackground(getResources().getDrawable(R.drawable.dr_bg4_b));
            cv6.setBackground(getResources().getDrawable(R.drawable.dr_bg4_b));

            btnCopy.setImageResource(R.drawable.th_2_copy);
            btnClipBoard.setImageResource(R.drawable.th_2_clipboard);
            btnGif.setImageResource(R.drawable.th_2_gif);
            btnTheme.setImageResource(R.drawable.th_2_keyboard);
            btnFont.setImageResource(R.drawable.th_2_fancy_text);
            btnSticker.setImageResource(R.drawable.emoji_w);
        } else if (themeIdPopTool == 3) {
            cv1.setBackground(getResources().getDrawable(R.drawable.dr_bg3_b));
            cv2.setBackground(getResources().getDrawable(R.drawable.dr_bg3_b));
            cv3.setBackground(getResources().getDrawable(R.drawable.dr_bg3_b));
            cv4.setBackground(getResources().getDrawable(R.drawable.dr_bg3_b));
            cv5.setBackground(getResources().getDrawable(R.drawable.dr_bg3_b));
            cv6.setBackground(getResources().getDrawable(R.drawable.dr_bg3_b));

            btnCopy.setImageResource(R.drawable.th_0_copy);
            btnClipBoard.setImageResource(R.drawable.th_0_clipboard);
            btnGif.setImageResource(R.drawable.th_0_gif);
            btnTheme.setImageResource(R.drawable.th_0_keyboard);
            btnFont.setImageResource(R.drawable.th_0_fancy_text);
            btnSticker.setImageResource(R.drawable.emoji_black);
        } else if (themeIdPopTool == 0) {
            cv1.setBackground(getResources().getDrawable(R.drawable.dr_bg0_b));
            cv2.setBackground(getResources().getDrawable(R.drawable.dr_bg0_b));
            cv3.setBackground(getResources().getDrawable(R.drawable.dr_bg0_b));
            cv4.setBackground(getResources().getDrawable(R.drawable.dr_bg0_b));
            cv5.setBackground(getResources().getDrawable(R.drawable.dr_bg0_b));
            cv6.setBackground(getResources().getDrawable(R.drawable.dr_bg0_b));

            btnCopy.setImageResource(R.drawable.th_0_copy);
            btnClipBoard.setImageResource(R.drawable.th_0_clipboard);
            btnGif.setImageResource(R.drawable.th_0_gif);
            btnTheme.setImageResource(R.drawable.th_0_keyboard);
            btnFont.setImageResource(R.drawable.th_0_fancy_text);
            btnSticker.setImageResource(R.drawable.emoji_black);
        }


        View visibleKeyboardView = ((View) mKeyboardCkSwitcher.getVisibleKeyboardView());
        popTool.setWidth(LayoutParams.MATCH_PARENT);
        popTool.setHeight(visibleKeyboardView.getHeight());
        popTool.setBackgroundDrawable(getBgForPop(true));
        popTool.showAtLocation(visibleKeyboardView.getRootView(), Gravity.BOTTOM, 0, 0);


        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence charSequence;
                CustomKeyBoard mainSftKeybrd = CustomKeyBoard.this;

                ExtractedText extractedText = mainSftKeybrd.getCurrentInputConnection().getExtractedText(new ExtractedTextRequest(), 0);
                if (extractedText != null && (charSequence = extractedText.text) != null) {
                    mainSftKeybrd.getCurrentInputConnection().setSelection(0, charSequence.length());
                    CharSequence selectedText = mainSftKeybrd.getCurrentInputConnection().getSelectedText(0);
                    @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) mainSftKeybrd.getSystemService("clipboard");
                    if (selectedText != null) {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("newtext", selectedText.toString()));
                        Toast.makeText(getApplicationContext(), "copy--" + selectedText, Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Nothing to copy", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnClipBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClipPop();
            }
        });
        btnGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomKeyBoard.this, ThemeCreateCsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomKeyBoard.this, FontSelectCsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomKeyBoard.this, AllStickerAddCsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void zoomImageFromThumb(final View thumbView, int imageResId, ImageView expandedImageView, ConstraintLayout frameLayout) {

        if (currentAnimator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                currentAnimator.cancel();
            }
        }


        expandedImageView.setImageResource(imageResId);


        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        thumbView.getGlobalVisibleRect(startBounds);
        frameLayout.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        expandedImageView.setVisibility(View.VISIBLE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            expandedImageView.setPivotX(0f);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            expandedImageView.setPivotY(0f);
        }

        AnimatorSet set = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                set.play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.centerX(), startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                                startBounds.top, finalBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                                startScale, 1f))
                        .with(ObjectAnimator.ofFloat(expandedImageView,
                                View.SCALE_Y, startScale, 1f));
            }
            set.setDuration(shortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentAnimator = null;
                    expandedImageView.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                        try {
                            MediaScannerConnection.scanFile(CustomKeyBoard.this, new String[]{CopyRAWtoStorage(imageResId).toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {


                                public void onScanCompleted(String path, Uri uri) {
                                    commitSticker(uri);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Uri uri = null;
                        try {
                            uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", CopyRAWtoStorage(imageResId));

                            commitSticker(uri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    currentAnimator = null;
                    expandedImageView.setVisibility(View.GONE);
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            expandedImageView.startAnimation(AnimationUtils.loadAnimation(CustomKeyBoard.this, github.ankushsachdeva.emojicon.R.anim.shack));
            set.start();
            mediaPlayer.start();
            mediaPlayer.seekTo(0);
            //reExtra.setVisibility(View.VISIBLE);
            currentAnimator = set;
        }


    }

    private Drawable getBgForPop(boolean b) {

        Drawable drawable = null;
        final int themeId = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;
        if (themeId == 0) {
            drawable = new ColorDrawable(getResources().getColor(R.color.theme_0_bg));

        } else if (themeId == 1) {
            drawable = getResources().getDrawable(R.drawable.th1_bg);


        } else if (themeId == 2) {
            drawable = new ColorDrawable(getResources().getColor(R.color.theme_2_bg));
        } else if (themeId == 3) {
            drawable = new ColorDrawable(getResources().getColor(R.color.theme_3_bg));

        } else if (themeId == 4) {


            drawable = getResources().getDrawable(R.drawable.th4_bg1_1);


        } else if (themeId == 5) {
            drawable = new ColorDrawable(getResources().getColor(R.color.theme_5_bg));


        } else if (themeId == 6 || themeId == 7) {
            String fileS = storeageCkPref.getFILE_PATH();
            if (fileS != null) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(fileS));

                try {
                    Bitmap originalBm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    if (b) {
                        int fromHere = (int) (originalBm.getHeight() * 0.8);
                        Bitmap bitmap2 = Bitmap.createBitmap(originalBm, 0, (int) (originalBm.getHeight() * 0.2), originalBm.getWidth(), fromHere);

                        drawable = new BitmapDrawable(getResources(), bitmap2);
                    } else {


                        drawable = new BitmapDrawable(getResources(), originalBm);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {


                drawable = new ColorDrawable(getResources().getColor(R.color.black));
            }
        }
        return drawable;
    }

    private int getTextForThem() {
        int drawable = getResources().getColor(R.color.white);
        ;
        final int themeId = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;
        if (themeId == 0) {
            drawable = getResources().getColor(R.color.black);

        } else if (themeId == 1) {
            drawable = getResources().getColor(R.color.black);

        } else if (themeId == 2) {
            drawable = getResources().getColor(R.color.white);
        } else if (themeId == 3) {
            drawable = getResources().getColor(R.color.black);

        } else if (themeId == 4) {
            drawable = getResources().getColor(R.color.black);

        } else if (themeId == 5) {
            drawable = getResources().getColor(R.color.black);


        } else if (themeId == 6 || themeId == 7) {
            drawable = getResources().getColor(R.color.white);

        }
        return drawable;
    }

    private Drawable getTVBgForTheme() {
        Drawable drawable = getResources().getDrawable(R.drawable.dr_tv_bg1);
        final int themeId = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;
        if (themeId == 0) {
            drawable = getResources().getDrawable(R.drawable.dr_tv_bg2);

        } else if (themeId == 1) {
            drawable = getResources().getDrawable(R.drawable.dr_tv_bg2);


        } else if (themeId == 2) {
            drawable = getResources().getDrawable(R.drawable.dr_tv_bg1);

        } else if (themeId == 3) {
            drawable = getResources().getDrawable(R.drawable.dr_tv_bg2);


        } else if (themeId == 4) {
            drawable = getResources().getDrawable(R.drawable.dr_tv_bg2);

        } else if (themeId == 5) {
            drawable = getResources().getDrawable(R.drawable.dr_tv_bg1);


        } else if (themeId == 6 || themeId == 7) {
            drawable = getResources().getDrawable(R.drawable.dr_tv_bg1);

        }
        return drawable;
    }

    private Drawable getIconForTheme() {
        Drawable drawable = getResources().getDrawable(R.drawable.th_0_keyboard);
        final int themeId = KeyboardCkTheme.getKeyboardTheme(this).mThemeId;
        if (themeId == 0) {
            drawable = getResources().getDrawable(R.drawable.th_0_keyboard);

        } else if (themeId == 1) {
            drawable = getResources().getDrawable(R.drawable.th_0_keyboard);


        } else if (themeId == 2) {
            drawable = getResources().getDrawable(R.drawable.th_2_keyboard);

        } else if (themeId == 3) {
            drawable = getResources().getDrawable(R.drawable.th_3_keyboard);


        } else if (themeId == 4) {
            drawable = getResources().getDrawable(R.drawable.th_4_keyboard);

        } else if (themeId == 5) {
            drawable = getResources().getDrawable(R.drawable.th_5_keyboard);


        } else if (themeId == 6 || themeId == 7) {
            drawable = getResources().getDrawable(R.drawable.th_2_keyboard);

        }
        return drawable;
    }

    private boolean isCommitContentSupported(
            @Nullable EditorInfo editorInfo, @NonNull String mimeType) {
        if (editorInfo == null) {
            return false;
        }

        final InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
            return false;
        }

        if (!validatePackageName(editorInfo)) {
            return false;
        }

        final String[] supportedMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        for (String supportedMimeType : supportedMimeTypes) {
            if (ClipDescription.compareMimeTypes(mimeType, supportedMimeType)) {
                return true;
            }
        }
        return false;
    }

    private boolean validatePackageName(@Nullable EditorInfo editorInfo) {
        if (editorInfo == null) {
            return false;
        }
        final String packageName = editorInfo.packageName;
        if (packageName == null) {
            return false;
        }

        // In Android L MR-1 and prior devices, EditorInfo.packageName is not a reliable identifier
        // of the target application because:
        //   1. the system does not verify it [1]
        //   2. InputMethodManager.startInputInner() had filled EditorInfo.packageName with
        //      view.getContext().getPackageName() [2]
        // [1]: https://android.googlesource.com/platform/frameworks/base/+/a0f3ad1b5aabe04d9eb1df8bad34124b826ab641
        // [2]: https://android.googlesource.com/platform/frameworks/base/+/02df328f0cd12f2af87ca96ecf5819c8a3470dc8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }

        final InputBinding inputBinding = getCurrentInputBinding();
        if (inputBinding == null) {
            // Due to b.android.com/225029, it is possible that getCurrentInputBinding() returns
            // null even after onStartInputView() is called.
            // TODO: Come up with a way to work around this bug....
            Log.e(TAG, "inputBinding should not be null here. "
                    + "You are likely to be hitting b.android.com/225029");
            return false;
        }
        final int packageUid = inputBinding.getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final AppOpsManager appOpsManager =
                    (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            try {
                appOpsManager.checkPackage(packageUid, packageName);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        final PackageManager packageManager = getPackageManager();
        final String possiblePackageNames[] = packageManager.getPackagesForUid(packageUid);
        for (final String possiblePackageName : possiblePackageNames) {
            if (packageName.equals(possiblePackageName)) {
                return true;
            }
        }
        return false;
    }

}

//"image/webp.wasticker"