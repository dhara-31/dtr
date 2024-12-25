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

package com.test.testing12345.keyboard.internal;

import java.util.ArrayList;

public final class PointerTrackerCkQueue {
    private static final String TAG = PointerTrackerCkQueue.class.getSimpleName();
    private static final boolean DEBUG = false;

    public interface Element {
        boolean isModifier();
        boolean isInDraggingFinger();
        void onPhantomUpEvent(long eventTime);
        void cancelTrackingForAction();
    }

    private static final int INITIAL_CAPACITY = 10;
     private final ArrayList<Element> mExpandableArrayOfActivePointers =
            new ArrayList<>(INITIAL_CAPACITY);
    private int mArraySize = 0;

    public int size() {
        synchronized (mExpandableArrayOfActivePointers) {
            return mArraySize;
        }
    }

    public void add(final Element pointer) {
        synchronized (mExpandableArrayOfActivePointers) {

            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            if (arraySize < expandableArray.size()) {
                expandableArray.set(arraySize, pointer);
            } else {
                expandableArray.add(pointer);
            }
            mArraySize = arraySize + 1;
        }
    }

    public void remove(final Element pointer) {
        synchronized (mExpandableArrayOfActivePointers) {
            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            int newIndex = 0;
            for (int index = 0; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                if (element == pointer) {
                    if (newIndex != index) {
                    }
                    continue; // Remove this element from the expandableArray.
                }
                if (newIndex != index) {
                    // Shift this element toward the beginning of the expandableArray.
                    expandableArray.set(newIndex, element);
                }
                newIndex++;
            }
            mArraySize = newIndex;
        }
    }

    public void releaseAllPointersOlderThan(final Element pointer, final long eventTime) {
        synchronized (mExpandableArrayOfActivePointers) {
            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            int newIndex, index;
            for (newIndex = index = 0; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                if (element == pointer) {
                    break;
                }
                if (!element.isModifier()) {
                    element.onPhantomUpEvent(eventTime);
                    continue;
                }
                if (newIndex != index) {

                    expandableArray.set(newIndex, element);
                }
                newIndex++;
            }

            int count = 0;
            for (; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                if (element == pointer) {
                    count++;
                    if (count > 1) {
                     }
                }
                if (newIndex != index) {
                     expandableArray.set(newIndex, expandableArray.get(index));
                }
                newIndex++;
            }
            mArraySize = newIndex;
        }
    }

    public void releaseAllPointers(final long eventTime) {
        releaseAllPointersExcept(null, eventTime);
    }

    public void releaseAllPointersExcept(final Element pointer, final long eventTime) {
        synchronized (mExpandableArrayOfActivePointers) {

            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            int newIndex = 0, count = 0;
            for (int index = 0; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                if (element == pointer) {
                    count++;
                    if (count > 1) {
                     }
                } else {
                    element.onPhantomUpEvent(eventTime);
                    continue;
                }
                if (newIndex != index) {
                     expandableArray.set(newIndex, element);
                }
                newIndex++;
            }
            mArraySize = newIndex;
        }
    }

    public boolean hasModifierKeyOlderThan(final Element pointer) {
        synchronized (mExpandableArrayOfActivePointers) {
            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            for (int index = 0; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                if (element == pointer) {
                    return false; // Stop searching modifier key.
                }
                if (element.isModifier()) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isAnyInDraggingFinger() {
        synchronized (mExpandableArrayOfActivePointers) {
            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            for (int index = 0; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                if (element.isInDraggingFinger()) {
                    return true;
                }
            }
            return false;
        }
    }

    public void cancelAllPointerTrackers() {
        synchronized (mExpandableArrayOfActivePointers) {

            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            for (int index = 0; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                element.cancelTrackingForAction();
            }
        }
    }

    @Override
    public String toString() {
        synchronized (mExpandableArrayOfActivePointers) {
            final StringBuilder sb = new StringBuilder();
            final ArrayList<Element> expandableArray = mExpandableArrayOfActivePointers;
            final int arraySize = mArraySize;
            for (int index = 0; index < arraySize; index++) {
                final Element element = expandableArray.get(index);
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(element.toString());
            }
            return "[" + sb.toString() + "]";
        }
    }
}
