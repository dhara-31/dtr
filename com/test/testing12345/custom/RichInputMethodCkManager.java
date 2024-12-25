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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;

import com.test.testing12345.R;
import com.test.testing12345.compat.PreferenceCkManagerCompat;
import com.test.testing12345.custom.common.LocaleCkUtils;
import com.test.testing12345.custom.settings.SettingsCk;
import com.test.testing12345.custom.utils.DialogCkUtils;
import com.test.testing12345.custom.utils.LocaleResourceCkUtils;
import com.test.testing12345.custom.utils.SubtypeLocaleCkUtils;
import com.test.testing12345.custom.utils.SubtypePreferenceCkUtils;


public class RichInputMethodCkManager {
    private static final String TAG = RichInputMethodCkManager.class.getSimpleName();

    private RichInputMethodCkManager() {

    }

    private static final RichInputMethodCkManager sInstance = new RichInputMethodCkManager();

    public static InputMethodManager mImmService;

    public static SubtypeList mSubtypeList;

    public static RichInputMethodCkManager getInstance() {
        sInstance.checkInitialized();
        return sInstance;
    }

    public static void init(final Context context) {
        sInstance.initInternal(context);
    }

    private boolean isInitialized() {
        return mImmService != null;
    }

    private void checkInitialized() {
        if (!isInitialized()) {
            throw new RuntimeException(TAG + " is used before initialization");
        }
    }

    private void initInternal(final Context context) {
        if (isInitialized()) {
            return;
        }
        mImmService = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        LocaleResourceCkUtils.init(context);

         mSubtypeList = new SubtypeList(context);
    }

     public void setSubtypeChangeHandler(final SubtypeChangedListener listener) {
        mSubtypeList.setSubtypeChangeHandler(listener);
    }
     public interface SubtypeChangedListener {
        void onCurrentSubtypeChanged();
    }

     private static class SubtypeList {
         private final List<SubtypeCk> mSubtypeCks;
         private int mCurrentSubtypeIndex;

        private final SharedPreferences mPrefs;
        private SubtypeChangedListener mSubtypeChangedListener;

         public SubtypeList(final Context context) {
            mPrefs = PreferenceCkManagerCompat.getDeviceSharedPreferences(context);

            final String prefSubtypes = SettingsCk.readPrefSubtypes(mPrefs);
            final List<SubtypeCk> subtypeCks = SubtypePreferenceCkUtils.createSubtypesFromPref(
                    prefSubtypes, context.getResources());
            if (subtypeCks == null || subtypeCks.size() < 1) {
                mSubtypeCks = SubtypeLocaleCkUtils.getDefaultSubtypes(context.getResources());
            } else {
                mSubtypeCks = subtypeCks;
            }
            mCurrentSubtypeIndex = 0;
        }

        /**
         * Add a listener to be called when the virtual subtype changes.
         * @param listener the listener to call when the subtype changes.
         */
        public void setSubtypeChangeHandler(final SubtypeChangedListener listener) {
            mSubtypeChangedListener = listener;
        }

        /**
         * Call the subtype changed handler to indicate that the virtual subtype has changed.
         */
        public void notifySubtypeChanged() {
            if (mSubtypeChangedListener != null) {
                mSubtypeChangedListener.onCurrentSubtypeChanged();
            }
        }

        /**
         * Get all of the enabled languages.
         * @return the enabled languages.
         */
        public synchronized Set<Locale> getAllLocales() {
            final Set<Locale> locales = new HashSet<>();
            for (final SubtypeCk subtypeCk : mSubtypeCks) {
                locales.add(subtypeCk.getLocaleObject());
            }
            return locales;
        }

        /**
         * Get all of the enabled subtypes for language.
         * @param locale filter by Locale.
         * @return the enabled subtypes.
         */
        public synchronized Set<SubtypeCk> getAllForLocale(final String locale) {
            final Set<SubtypeCk> subtypeCks = new HashSet<>();
            for (final SubtypeCk subtypeCk : mSubtypeCks) {
                if (subtypeCk.getLocale().equals(locale))
                    subtypeCks.add(subtypeCk);
            }
            return subtypeCks;
        }

        /**
         * Get all of the enabled subtypes.
         * @param sortForDisplay whether the subtypes should be sorted alphabetically by the display
         *                      name as opposed to having no particular order.
         * @return the enabled subtypes.
         */
        public synchronized Set<SubtypeCk> getAll(final boolean sortForDisplay) {
            final Set<SubtypeCk> subtypeCks;
            if (sortForDisplay) {
                subtypeCks = new TreeSet<>(new Comparator<SubtypeCk>() {
                    @Override
                    public int compare(SubtypeCk a, SubtypeCk b) {
                        if (a.equals(b)) {
                            // ensure that this is consistent with equals
                            return 0;
                        }
                        final int result = a.getName().compareToIgnoreCase(b.getName());
                        if (result != 0) {
                            return result;
                        }
                        // ensure that non-equal objects are distinguished to be consistent with
                        // equals
                        return a.hashCode() > b.hashCode() ? 1 : -1;
                    }
                });
            } else {
                subtypeCks = new HashSet<>();
            }
            subtypeCks.addAll(mSubtypeCks);
            return subtypeCks;
        }

        /**
         * Get the number of enabled subtypes.
         * @return the number of enabled subtypes.
         */
        public synchronized int size() {
            return mSubtypeCks.size();
        }

        /**
         * Update the preference for the list of enabled subtypes.
         */
        private void saveSubtypeListPref() {
            final String prefSubtypes = SubtypePreferenceCkUtils.createPrefSubtypes(mSubtypeCks);
            SettingsCk.writePrefSubtypes(mPrefs, prefSubtypes);
        }

        /**
         * Add a subtype to the list.
         * @param subtypeCk the subtype to add.
         * @return whether the subtype was added to the list (or already existed in the list).
         */
        public synchronized boolean addSubtype(final SubtypeCk subtypeCk) {
            if (mSubtypeCks.contains(subtypeCk)) {
                // don't allow duplicates, but since it's already in the list this can be considered
                // successful
                return true;
            }
            if (!mSubtypeCks.add(subtypeCk)) {
                return false;
            }
            saveSubtypeListPref();
            return true;
        }

        /**
         * Remove a subtype from the list.
         * @param subtypeCk the subtype to remove.
         * @return whether the subtype was removed (or wasn't even in the list).
         */
        public synchronized boolean removeSubtype(final SubtypeCk subtypeCk) {
            if (mSubtypeCks.size() == 1) {
                // there needs to be at least one subtype
                return false;
            }

            final int index = mSubtypeCks.indexOf(subtypeCk);
            if (index < 0) {
                // nothing to remove
                return true;
            }

            final boolean subtypeChanged;
            if (mCurrentSubtypeIndex == index) {
                mCurrentSubtypeIndex = 0;
                subtypeChanged = true;
            } else {
                if (mCurrentSubtypeIndex > index) {
                    // make sure the current subtype is still pointed to when the other subtype is
                    // removed
                    mCurrentSubtypeIndex--;
                }
                subtypeChanged = false;
            }

            mSubtypeCks.remove(index);
            saveSubtypeListPref();
            if (subtypeChanged) {
                notifySubtypeChanged();
            }
            return true;
        }

        /**
         * Move the current subtype to the beginning of the list to allow the rest of the subtypes
         * to be cycled through before possibly switching to a separate input method. This should be
         * called whenever the user is done cycling through subtypes (eg: when a subtype is actually
         * used or the keyboard is closed).
         */
        public synchronized void resetSubtypeCycleOrder() {
            if (mCurrentSubtypeIndex == 0) {
                return;
            }

            // move the current subtype to the top of the list and shift everything above it down
            Collections.rotate(mSubtypeCks.subList(0, mCurrentSubtypeIndex + 1), 1);
            mCurrentSubtypeIndex = 0;
            saveSubtypeListPref();
        }

        /**
         * Set the current subtype to a specific subtype.
         * @param subtypeCk the subtype to set as current.
         * @return whether the current subtype was set to the requested subtype.
         */
        public synchronized boolean setCurrentSubtype(final SubtypeCk subtypeCk) {
            if (getCurrentSubtype().equals(subtypeCk)) {
                // nothing to do
                return true;
            }
            for (int i = 0; i < mSubtypeCks.size(); i++) {
                if (mSubtypeCks.get(i).equals(subtypeCk)) {
                    setCurrentSubtype(i);
                    return true;
                }
            }
            return false;
        }

        /**
         * Set the current subtype to match a specified locale.
         * @param locale the locale to use.
         * @return whether the current subtype was set to the requested locale.
         */
        public synchronized boolean setCurrentSubtype(final Locale locale) {
            final ArrayList<Locale> enabledLocales = new ArrayList<>(mSubtypeCks.size());
            for (final SubtypeCk subtypeCk : mSubtypeCks) {
                enabledLocales.add(subtypeCk.getLocaleObject());
            }
            final Locale bestLocale = LocaleCkUtils.findBestLocale(locale, enabledLocales);
            if (bestLocale != null) {
                // get the first subtype (most recently used) with a matching locale
                for (int i = 0; i < mSubtypeCks.size(); i++) {
                    final SubtypeCk subtypeCk = mSubtypeCks.get(i);
                    if (bestLocale.equals(subtypeCk.getLocaleObject())) {
                        setCurrentSubtype(i);
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Set the current subtype to a specified index. This should only be used when setting the
         * subtype to something specific (not when just iterating through the subtypes).
         * @param index the index of the subtype to set as current.
         */
        private void setCurrentSubtype(final int index) {
            if (mCurrentSubtypeIndex == index)
            {
                // nothing to do
                return;
            }
            mCurrentSubtypeIndex = index;
            if (index != 0) {
                // since the subtype was selected directly, the cycle should be reset so switching
                // to the next subtype can iterate through all of the rest of the subtypes
                resetSubtypeCycleOrder();
            }
            notifySubtypeChanged();
        }

         public synchronized boolean switchToNextSubtype(final boolean notifyChangeOnCycle) {
            final int nextIndex = mCurrentSubtypeIndex + 1;
            if (nextIndex >= mSubtypeCks.size()) {
                mCurrentSubtypeIndex = 0;
                if (!notifyChangeOnCycle) {
                    return false;
                }
            } else {
                mCurrentSubtypeIndex = nextIndex;
            }
            notifySubtypeChanged();
            return true;
        }

         public synchronized SubtypeCk getCurrentSubtype() {
            return mSubtypeCks.get(mCurrentSubtypeIndex);
        }
    }

     public static Set<SubtypeCk> getEnabledSubtypes(final boolean sortForDisplay) {
        return mSubtypeList.getAll(sortForDisplay);
    }

     public Set<Locale> getEnabledLocales() {
        return mSubtypeList.getAllLocales();
    }

     public Set<SubtypeCk> getEnabledSubtypesForLocale(final String locale) {
        return mSubtypeList.getAllForLocale(locale);
    }

     public boolean hasMultipleEnabledSubtypes() {
        return mSubtypeList.size() > 1;
    }

     public boolean addSubtype(final SubtypeCk subtypeCk) {
        return mSubtypeList.addSubtype(subtypeCk);
    }

     public boolean removeSubtype(final SubtypeCk subtypeCk) {
        return mSubtypeList.removeSubtype(subtypeCk);
    }

     public void resetSubtypeCycleOrder() {
        mSubtypeList.resetSubtypeCycleOrder();
    }

     public static boolean setCurrentSubtype(final SubtypeCk subtypeCk) {
        return mSubtypeList.setCurrentSubtype(subtypeCk);
    }

     public static   boolean setCurrentSubtype(final Locale locale) {
        return mSubtypeList.setCurrentSubtype(locale);
    }

     public boolean switchToNextInputMethod(final IBinder token, final boolean onlyCurrentIme) {
        if (onlyCurrentIme) {
            if (!hasMultipleEnabledSubtypes()) {
                return false;
            }
            return mSubtypeList.switchToNextSubtype(true);
        }
        if (mSubtypeList.switchToNextSubtype(false)) {
            return true;
        }
         if (mImmService.switchToNextInputMethod(token, false)) {
            return true;
        }
        if (hasMultipleEnabledSubtypes()) {

            mSubtypeList.notifySubtypeChanged();
            return true;
        }
        return false;
    }

     public SubtypeCk getCurrentSubtype() {
        return mSubtypeList.getCurrentSubtype();
    }

     public boolean shouldOfferSwitchingToOtherInputMethods(final IBinder binder) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return false;
        }
        return mImmService.shouldOfferSwitchingToNextInputMethod(binder);
    }

     public AlertDialog showSubtypePicker(final Context context, final IBinder windowToken,
                                         final InputMethodService inputMethodService) {
        if (windowToken == null) {
            return null;
        }
        final CharSequence title = context.getString(R.string.change_keyboard);

        final List<SubtypeInfo> subtypeInfoList = getEnabledSubtypeInfoOfAllImes(context);
        if (subtypeInfoList.size() < 2) {
             return null;
        }

        final CharSequence[] items = new CharSequence[subtypeInfoList.size()];
        final SubtypeCk currentSubtypeCk = getCurrentSubtype();
        int currentSubtypeIndex = 0;
        int i = 0;
        for (final SubtypeInfo subtypeInfo : subtypeInfoList) {
            if (subtypeInfo.virtualSubtypeCk != null
                    && subtypeInfo.virtualSubtypeCk.equals(currentSubtypeCk)) {
                currentSubtypeIndex = i;
            }

            final SpannableString itemTitle;
            final SpannableString itemSubtitle;
            if (!TextUtils.isEmpty(subtypeInfo.subtypeName)) {
                itemTitle = new SpannableString(subtypeInfo.subtypeName);
                itemSubtitle = new SpannableString("\n" + subtypeInfo.imeName);
            } else {
                itemTitle = new SpannableString(subtypeInfo.imeName);
                itemSubtitle = new SpannableString("");
            }
            itemTitle.setSpan(new RelativeSizeSpan(0.9f), 0,itemTitle.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            itemSubtitle.setSpan(new RelativeSizeSpan(0.85f), 0,itemSubtitle.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            items[i++] = new SpannableStringBuilder().append(itemTitle).append(itemSubtitle);
        }
        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface di, int position) {
                di.dismiss();
                int i = 0;
                for (final SubtypeInfo subtypeInfo : subtypeInfoList) {
                    if (i == position) {
                        if (subtypeInfo.virtualSubtypeCk != null) {
                            setCurrentSubtype(subtypeInfo.virtualSubtypeCk);
                        } else {
                            switchToTargetIme(subtypeInfo.imiId, subtypeInfo.systemSubtype,
                                    inputMethodService);
                        }
                        break;
                    }
                    i++;
                }
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                DialogCkUtils.getPlatformDialogThemeContext(context));
        builder.setSingleChoiceItems(items, currentSubtypeIndex, listener).setTitle(title);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final Window window = dialog.getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.token = windowToken;
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        dialog.show();
        return dialog;
    }

     public static List<SubtypeInfo> getEnabledSubtypeInfoOfAllImes(final Context context) {
        final List<SubtypeInfo> subtypeInfoList = new ArrayList<>();
        final PackageManager packageManager = context.getPackageManager();

        final Set<InputMethodInfo> imiList = new TreeSet<>(new Comparator<InputMethodInfo>() {
            @Override
            public int compare(InputMethodInfo a, InputMethodInfo b) {
                if (a.equals(b)) {
                    // ensure that this is consistent with equals
                    return 0;
                }
                final String labelA = a.loadLabel(packageManager).toString();
                final String labelB = b.loadLabel(packageManager).toString();
                final int result = labelA.compareToIgnoreCase(labelB);
                if (result != 0) {
                    return result;
                }

                return a.hashCode() > b.hashCode() ? 1 : -1;
            }
        });
        imiList.addAll(mImmService.getEnabledInputMethodList());

        for (final InputMethodInfo imi : imiList) {
            final CharSequence imeName = imi.loadLabel(packageManager);
            final String imiId = imi.getId();
            final String packageName = imi.getPackageName();

            if (packageName.equals(context.getPackageName())) {
                for (final SubtypeCk subtypeCk : getEnabledSubtypes(true)) {
                    final SubtypeInfo subtypeInfo = new SubtypeInfo();
                    subtypeInfo.virtualSubtypeCk = subtypeCk;
                    subtypeInfo.subtypeName = subtypeCk.getName();
                    subtypeInfo.imeName = imeName;
                    subtypeInfo.imiId = imiId;
                    subtypeInfoList.add(subtypeInfo);
                }
                continue;
            }

            final List<InputMethodSubtype> subtypes =
                    mImmService.getEnabledInputMethodSubtypeList(imi, true);
             if (subtypes.isEmpty()) {
                final SubtypeInfo subtypeInfo = new SubtypeInfo();
                subtypeInfo.imeName = imeName;
                subtypeInfo.imiId = imiId;
                subtypeInfoList.add(subtypeInfo);
                continue;
            }

            final ApplicationInfo applicationInfo = imi.getServiceInfo().applicationInfo;
            for (final InputMethodSubtype subtype : subtypes) {
                if (subtype.isAuxiliary()) {
                    continue;
                }
                final SubtypeInfo subtypeInfo = new SubtypeInfo();
                subtypeInfo.systemSubtype = subtype;
                if (!subtype.overridesImplicitlyEnabledSubtype()) {
                    subtypeInfo.subtypeName = subtype.getDisplayName(context, packageName,
                            applicationInfo);
                }
                subtypeInfo.imeName = imeName;
                subtypeInfo.imiId = imiId;
                subtypeInfoList.add(subtypeInfo);
            }
        }

        return subtypeInfoList;
    }


    public static class SubtypeInfo {
        public InputMethodSubtype systemSubtype;
        public SubtypeCk virtualSubtypeCk;
        public CharSequence subtypeName;
        public CharSequence imeName;
        public String imiId;
    }

     private void switchToTargetIme(final String imiId, final InputMethodSubtype subtype,
                                   final InputMethodService context) {
        final IBinder token = context.getWindow().getWindow().getAttributes().token;
        if (token == null) {
            return;
        }
        final InputMethodManager imm = mImmService;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                imm.setInputMethodAndSubtype(token, imiId, subtype);
            }
        });
    }
}
