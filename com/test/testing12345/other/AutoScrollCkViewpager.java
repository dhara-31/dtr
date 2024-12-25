package com.test.testing12345.other;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class AutoScrollCkViewpager extends ViewPager {
        public static final int DEFAULT_INTERVAL = 1500;
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        private static final String TAG = "AutoScrollViewPager";
        public static final int SLIDE_BORDER_MODE_NONE = 0;
        public static final int SLIDE_BORDER_MODE_CYCLE = 1;
        public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;
        private long interval = DEFAULT_INTERVAL;
        private int direction = RIGHT;
        private boolean isCycle = true;
        private boolean stopScrollWhenTouch = true;
        private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
        private boolean isBorderAnimation = true;
        private double autoScrollFactor = 1.0;
        private double swipeScrollFactor = 1.0;
        private Handler handler;
        @Nullable
        private DurationCkScroller scroller;
        public static final int SCROLL_WHAT = 0;
        public AutoScrollCkViewpager(Context paramContext) {
            super(paramContext);
            init();
        }
        public AutoScrollCkViewpager(Context paramContext, AttributeSet paramAttributeSet) {
            super(paramContext, paramAttributeSet);
            init();
        }
        private void init() {
            handler = new MyHandler(this);
            setViewPagerScroller();
        }
         public void startAutoScroll() {
            if (scroller != null) {
                sendScrollMessage(
                        (long) (interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
            }
        }
         public void startAutoScroll(int delayTimeInMills) {
            sendScrollMessage(delayTimeInMills);
        }
         public void stopAutoScroll() {
            handler.removeMessages(SCROLL_WHAT);
        }
         public void setSwipeScrollDurationFactor(double scrollFactor) {
            swipeScrollFactor = scrollFactor;
        }


        public void setAutoScrollDurationFactor(double scrollFactor) {
            autoScrollFactor = scrollFactor;
        }
        private void sendScrollMessage(long delayTimeInMills) {
             handler.removeMessages(SCROLL_WHAT);
            handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
        }
         private void setViewPagerScroller() {
            try {
                Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
                scrollerField.setAccessible(true);
                Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
                interpolatorField.setAccessible(true);
                scroller =
                        new DurationCkScroller(getContext(), (Interpolator) interpolatorField.get(null));
                scrollerField.set(this, scroller);
            } catch (IllegalAccessException e) {
              } catch (NoSuchFieldException e) {

            }
        }
         public void scrollOnce() {
            PagerAdapter adapter = getAdapter();
            int currentItem = getCurrentItem();
            int totalCount = adapter != null ? adapter.getCount() : -100;
            if (adapter == null || totalCount <= 1) {
                return;
            }
            int nextItem = (direction == LEFT) ?- currentItem : ++currentItem;
            if (nextItem < 0) {
                if (isCycle) {
                    setCurrentItem(totalCount - 1, isBorderAnimation);
                }
            } else if (nextItem == totalCount) {
                if (isCycle) {
                    setCurrentItem(0, false);
                }
            } else {
                setCurrentItem(nextItem, true);
            }
        }
        private static class MyHandler extends Handler {
            private final WeakReference<AutoScrollCkViewpager> autoScrollViewPager;
            public MyHandler(AutoScrollCkViewpager autoScrollCkViewPager) {
                this.autoScrollViewPager = new WeakReference<AutoScrollCkViewpager>(autoScrollCkViewPager);
            }
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SCROLL_WHAT) {
                    AutoScrollCkViewpager pager = this.autoScrollViewPager.get();
                    if (pager != null && pager.scroller != null) {
                        pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);
                        pager.scrollOnce();
                        pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
                        pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                    }
                }
            }
        }
         public long getInterval() {
            return interval;
        }
         public void setInterval(long interval) {
            this.interval = interval;
        }
         public int getDirection() {
            return (direction == LEFT) ? LEFT : RIGHT;
        }
         public void setDirection(int direction) {
            this.direction = direction;
        }
         public boolean isCycleScroll() {
            return isCycle;
        }

        public void setCycle(boolean isCycle) {
            this.isCycle = isCycle;
        }
         public boolean isStopScrollWhenTouch() {
            return stopScrollWhenTouch;
        }
         public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
            this.stopScrollWhenTouch = stopScrollWhenTouch;
        }
         public int getSlideBorderMode() {
            return slideBorderMode;
        }
         public void setSlideBorderMode(int slideBorderMode) {
            this.slideBorderMode = slideBorderMode;
        }

        public boolean isBorderAnimationEnabled() {
            return isBorderAnimation;
        }
         public void setBorderAnimation(boolean isBorderAnimation) {
            this.isBorderAnimation = isBorderAnimation;
        }
    }

