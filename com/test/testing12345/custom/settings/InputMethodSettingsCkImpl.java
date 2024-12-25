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

package com.test.testing12345.custom.settings;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.TextUtils;

import java.util.Set;

import com.test.testing12345.R;
import com.test.testing12345.custom.RichInputMethodCkManager;
import com.test.testing12345.custom.SubtypeCk;

 class InputMethodSettingsCkImpl {
    private Preference mSubtypeEnablerPreference;
    private RichInputMethodCkManager mRichImm;


    public boolean init(final Context context, final PreferenceScreen prefScreen) {
        RichInputMethodCkManager.init(context);
        mRichImm = RichInputMethodCkManager.getInstance();

        mSubtypeEnablerPreference = new Preference(context);
        mSubtypeEnablerPreference.setTitle(R.string.select_language);
        mSubtypeEnablerPreference.setFragment(LanguagesSettingsCkFragment.class.getName());
        prefScreen.addPreference(mSubtypeEnablerPreference);
        updateEnabledSubtypeList();
        return true;
    }

    private static String getEnabledSubtypesLabel(final RichInputMethodCkManager richImm) {
        if (richImm == null) {
            return null;
        }

        final Set<SubtypeCk> subtypeCks = richImm.getEnabledSubtypes(true);

        final StringBuilder sb = new StringBuilder();
        for (final SubtypeCk subtypeCk : subtypeCks) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(subtypeCk.getName());
        }
         return sb.toString();
    }

    public void updateEnabledSubtypeList() {
        if (mSubtypeEnablerPreference != null) {
            final String summary = getEnabledSubtypesLabel(mRichImm);
            if (!TextUtils.isEmpty(summary)) {
                mSubtypeEnablerPreference.setSummary(summary);
            }

         }
    }
}
