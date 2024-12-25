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

package com.test.testing12345.keyboard.internal;

import java.util.HashMap;

import com.test.testing12345.custom.common.ConstantsCk;

public final class KeyboardCkCodesSet {
    public static final String PREFIX_CODE = "!code/";

    private static final HashMap<String, Integer> sNameToIdMap = new HashMap<>();

    private KeyboardCkCodesSet() {

    }

    public static int getCode(final String name) {
        Integer id = sNameToIdMap.get(name);
        if (id == null) throw new RuntimeException("Unknown key code: " + name);
        return DEFAULT[id];
    }

    private static final String[] ID_TO_NAME = {
        "key_tab",
        "key_enter",
        "key_space",
        "key_shift",
        "key_capslock",
        "key_switch_alpha_symbol",
        "key_output_text",
        "key_delete",
        "key_settings",
        "key_action_next",
        "key_action_previous",
        "key_shift_enter",
        "key_language_switch",
            "key_switch_to_alphabet_number",
            "switch_to_symbol",
            "key_left",
        "key_right",
        "key_unspecified",
    };

    private static final int[] DEFAULT = {
        ConstantsCk.CODE_TAB,
        ConstantsCk.CODE_ENTER,
        ConstantsCk.CODE_SPACE,
        ConstantsCk.CODE_SHIFT,
        ConstantsCk.CODE_CAPSLOCK,
        ConstantsCk.CODE_SWITCH_ALPHA_SYMBOL,
        ConstantsCk.CODE_OUTPUT_TEXT,
        ConstantsCk.CODE_DELETE,
        ConstantsCk.CODE_SETTINGS,
        ConstantsCk.CODE_ACTION_NEXT,
        ConstantsCk.CODE_ACTION_PREVIOUS,
        ConstantsCk.CODE_SHIFT_ENTER,
        ConstantsCk.CODE_LANGUAGE_SWITCH,
            ConstantsCk.CODE_NUMBER,
            ConstantsCk.CODE_SYMBOL,
        ConstantsCk.CODE_UNSPECIFIED,

    };

    static {
        for (int i = 0; i < ID_TO_NAME.length; i++) {
            sNameToIdMap.put(ID_TO_NAME[i], i);
        }
    }
}
