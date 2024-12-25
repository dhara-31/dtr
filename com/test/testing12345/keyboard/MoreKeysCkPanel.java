
package com.test.testing12345.keyboard;

import android.view.View;
import android.view.ViewGroup;

public interface MoreKeysCkPanel {
    interface Controller {

        void onShowMoreKeysPanel(final MoreKeysCkPanel panel);


        void onDismissMoreKeysPanel();


        void onCancelMoreKeysPanel();
    }

    Controller EMPTY_CONTROLLER = new Controller() {
        @Override
        public void onShowMoreKeysPanel(final MoreKeysCkPanel panel) {}
        @Override
        public void onDismissMoreKeysPanel() {}
        @Override
        public void onCancelMoreKeysPanel() {}
    };

     void showMoreKeysPanel(View parentView, Controller controller, int pointX,
                           int pointY, KeyboardActionListener listener);

     void dismissMoreKeysPanel();

     void onMoveEvent(final int x, final int y, final int pointerId);

     void onDownEvent(final int x, final int y, final int pointerId);



    void onUpEvent(final int x, final int y, final int pointerId);

     int translateX(int x);

     int translateY(int y);

     void showInParent(ViewGroup parentView);

     void removeFromParent();

     boolean isShowingInParent();
}
