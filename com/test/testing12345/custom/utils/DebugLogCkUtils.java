/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.test.testing12345.custom.utils;

import com.test.testing12345.custom.define.DebugCkFlags;

public final class DebugLogCkUtils {
    private final static String TAG = DebugLogCkUtils.class.getSimpleName();
    private final static boolean sDBG = DebugCkFlags.DEBUG_ENABLED;

     public static String s(final Object o) {
        return null == o ? "null" : o.toString();
    }

     public static String getStackTrace() {
        return getStackTrace(Integer.MAX_VALUE - 1);
    }

     public static String getStackTrace(final int limit) {
        final StringBuilder sb = new StringBuilder();
        try {
            throw new RuntimeException();
        } catch (final RuntimeException e) {
            final StackTraceElement[] frames = e.getStackTrace();
            // Start at 1 because the first frame is here and we don't care about it
            for (int j = 1; j < frames.length && j < limit + 1; ++j) {
                sb.append(frames[j].toString() + "\n");
            }
        }
        return sb.toString();
    }

     public static void l(final Object... args) {
        if (!sDBG) return;
        final StringBuilder sb = new StringBuilder();
        for (final Object o : args) {
            sb.append(s(o).toString());
            sb.append(" ");
        }
     }

     public static void r(final Object... args) {
        if (!sDBG) return;
        final StringBuilder sb = new StringBuilder("\u001B[31m");
        for (final Object o : args) {
            sb.append(s(o).toString());
            sb.append(" ");
        }
        sb.append("\u001B[0m");
     }
}
