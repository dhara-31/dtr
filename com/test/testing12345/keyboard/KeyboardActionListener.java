/*
 * Copyright (C) 2010 The Android Open Source Project
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

public interface KeyboardActionListener {
     void onPressKey(int primaryCode, int repeatCount, boolean isSinglePointer);

     void onReleaseKey(int primaryCode, boolean withSliding);


     void onCodeInput(int primaryCode, int x, int y, boolean isKeyRepeat);


    void onTextInput(final String rawText);

     void onFinishSlidingInput();

     boolean onCustomRequest(int requestCode);
    void onMovePointer(int steps);
    void onMoveDeletePointer(int steps);
    void onUpWithDeletePointerActive();

    KeyboardActionListener EMPTY_LISTENER = new Adapter();

    class Adapter implements KeyboardActionListener {
        @Override
        public void onPressKey(int primaryCode, int repeatCount, boolean isSinglePointer) {}
        @Override
        public void onReleaseKey(int primaryCode, boolean withSliding) {}
        @Override
        public void onCodeInput(int primaryCode, int x, int y, boolean isKeyRepeat) {}
        @Override
        public void onTextInput(String text) {}
        @Override
        public void onFinishSlidingInput() {}
        @Override
        public boolean onCustomRequest(int requestCode) {
            return false;
        }
        @Override
        public void onMovePointer(int steps) {}
        @Override
        public void onMoveDeletePointer(int steps) {}
        @Override
        public void onUpWithDeletePointerActive() {}
    }
}
