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

package com.test.testing12345.custom;

import android.text.InputType;
import android.view.inputmethod.EditorInfo;

import com.test.testing12345.custom.common.StringCkUtils;
import com.test.testing12345.custom.utils.InputTypeCkUtils;


public final class InputCkAttributes {
    private final String TAG = InputCkAttributes.class.getSimpleName();

    final public String mTargetApplicationPackageName;
    final public boolean mInputTypeNoAutoCorrect;
    final public boolean mIsPasswordField;
    final public boolean mShouldShowSuggestions;
    final public boolean mApplicationSpecifiedCompletionOn;
    final public boolean mShouldInsertSpacesAutomatically;
     final private int mInputType;

    public InputCkAttributes(final EditorInfo editorInfo, final boolean isFullscreenMode) {
        mTargetApplicationPackageName = null != editorInfo ? editorInfo.packageName : null;
        final int inputType = null != editorInfo ? editorInfo.inputType : 0;
        final int inputClass = inputType & InputType.TYPE_MASK_CLASS;
        mInputType = inputType;
        mIsPasswordField = InputTypeCkUtils.isPasswordInputType(inputType)
                || InputTypeCkUtils.isVisiblePasswordInputType(inputType);
        if (inputClass != InputType.TYPE_CLASS_TEXT) {

            if (null == editorInfo) {

            } else if (InputType.TYPE_NULL == inputType) {


            } else if (inputClass == 0) {

            }
            mShouldShowSuggestions = false;
            mInputTypeNoAutoCorrect = false;
            mApplicationSpecifiedCompletionOn = false;
            mShouldInsertSpacesAutomatically = false;
            return;
        }

        final int variation = inputType & InputType.TYPE_MASK_VARIATION;
        final boolean flagNoSuggestions =
                0 != (inputType & InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        final boolean flagMultiLine =
                0 != (inputType & InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        final boolean flagAutoCorrect =
                0 != (inputType & InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        final boolean flagAutoComplete =
                0 != (inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);


        final boolean shouldSuppressSuggestions = mIsPasswordField
                || InputTypeCkUtils.isEmailVariation(variation)
                || InputType.TYPE_TEXT_VARIATION_URI == variation
                || InputType.TYPE_TEXT_VARIATION_FILTER == variation
                || flagNoSuggestions
                || flagAutoComplete;
        mShouldShowSuggestions = !shouldSuppressSuggestions;

        mShouldInsertSpacesAutomatically = InputTypeCkUtils.isAutoSpaceFriendlyType(inputType);


        mInputTypeNoAutoCorrect =
                (variation == InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT && !flagAutoCorrect)
                || flagNoSuggestions
                || (!flagAutoCorrect && !flagMultiLine);

        mApplicationSpecifiedCompletionOn = flagAutoComplete && isFullscreenMode;
    }

    public boolean isTypeNull() {
        return InputType.TYPE_NULL == mInputType;
    }

    public boolean isSameInputType(final EditorInfo editorInfo) {
        return editorInfo.inputType == mInputType;
    }


    @Override
    public String toString() {
        return String.format(
                "%s: inputType=0x%08x%s%s%s%s%s targetApp=%s\n", getClass().getSimpleName(),
                mInputType,
                (mInputTypeNoAutoCorrect ? " noAutoCorrect" : ""),
                (mIsPasswordField ? " password" : ""),
                (mShouldShowSuggestions ? " shouldShowSuggestions" : ""),
                (mApplicationSpecifiedCompletionOn ? " appSpecified" : ""),
                (mShouldInsertSpacesAutomatically ? " insertSpaces" : ""),
                mTargetApplicationPackageName);
    }

    public static boolean inPrivateImeOptions(final String packageName, final String key,
            final EditorInfo editorInfo) {
        if (editorInfo == null) return false;
        final String findingKey = (packageName != null) ? packageName + "." + key : key;
        return StringCkUtils.containsInCommaSplittableText(findingKey, editorInfo.privateImeOptions);
    }
}
