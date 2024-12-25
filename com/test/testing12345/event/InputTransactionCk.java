/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.test.testing12345.event;

import com.test.testing12345.custom.settings.SettingsCkValues;


public class InputTransactionCk {

    public static final int SHIFT_NO_UPDATE = 0;
    public static final int SHIFT_UPDATE_NOW = 1;
    public static final int SHIFT_UPDATE_LATER = 2;


    public final SettingsCkValues mSettingsCkValues;


    private int mRequiredShiftUpdate = SHIFT_NO_UPDATE;

    public InputTransactionCk(final SettingsCkValues settingsCkValues) {
        mSettingsCkValues = settingsCkValues;
    }

     public void requireShiftUpdate(final int updateType) {
        mRequiredShiftUpdate = Math.max(mRequiredShiftUpdate, updateType);
    }

     public int getRequiredShiftUpdate() {
        return mRequiredShiftUpdate;
    }
}
