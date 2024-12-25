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

import com.test.testing12345.keyboard.KeyCk;
import com.test.testing12345.keyboard.MoreKeysCkPanel;
import com.test.testing12345.keyboard.PointerCkTracker;

public interface DrawingProxy {
     void onKeyPressed(KeyCk keyCk, boolean withPreview);

     void onKeyReleased(KeyCk keyCk, boolean withAnimation);

     MoreKeysCkPanel showMoreKeysKeyboard(KeyCk keyCk, PointerCkTracker tracker);

     void startWhileTypingAnimation(int fadeInOrOut);
    int FADE_IN = 0;
    int FADE_OUT = 1;
}
