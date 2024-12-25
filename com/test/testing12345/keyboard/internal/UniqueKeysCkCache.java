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

package com.test.testing12345.keyboard.internal;

import java.util.HashMap;

import com.test.testing12345.keyboard.KeyCk;

public abstract class UniqueKeysCkCache {
    public abstract void setEnabled(boolean enabled);
    public abstract void clear();
    public abstract KeyCk getUniqueKey(KeyCk keyCk);

    public static final UniqueKeysCkCache NO_CACHE = new UniqueKeysCkCache() {
        @Override
        public void setEnabled(boolean enabled) {}

        @Override
        public void clear() {}

        @Override
        public KeyCk getUniqueKey(KeyCk keyCk) { return keyCk; }
    };

    public static UniqueKeysCkCache newInstance() {
        return new UniqueKeysCkCacheImpl();
    }

    private static final class UniqueKeysCkCacheImpl extends UniqueKeysCkCache {
        private final HashMap<KeyCk, KeyCk> mCache;

        private boolean mEnabled;

        UniqueKeysCkCacheImpl() {
            mCache = new HashMap<>();
        }

        @Override
        public void setEnabled(final boolean enabled) {
            mEnabled = enabled;
        }

        @Override
        public void clear() {
            mCache.clear();
        }

        @Override
        public KeyCk getUniqueKey(final KeyCk keyCk) {
            if (!mEnabled) {
                return keyCk;
            }
            final KeyCk existingKeyCk = mCache.get(keyCk);
            if (existingKeyCk != null) {

                return existingKeyCk;
            }
            mCache.put(keyCk, keyCk);
            return keyCk;
        }
    }
}
