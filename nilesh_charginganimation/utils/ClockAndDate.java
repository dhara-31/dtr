package com.si_charginganimation.nilesh_charginganimation.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClockAndDate extends androidx.appcompat.widget.AppCompatTextView {

    Calendar mCalendar;
    @SuppressWarnings("FieldCanBeLocal")
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;

    public ClockAndDate(Context context) {
        super(context);
        initClock();
    }

    public ClockAndDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock();
    }

    private void initClock() {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        setFormat();

        mHandler = new Handler();


        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped) return;
                mCalendar.setTimeInMillis(System.currentTimeMillis());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE & \nLLL dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
                String time = simpleDateFormat2.format(calendar.getTime()).toString();


                setText(dateTime + " at " + time);
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
        getContext().getContentResolver().unregisterContentObserver(
                mFormatChangeObserver);
    }

    private void setFormat() {
        mFormat = DateFormat_getTimeFormatString(getContext());

    }

    private static String DateFormat_getTimeFormatString(Context context) {
        try {
            final Method method = DateFormat.class.getDeclaredMethod("getTimeFormatString");
            method.setAccessible(true);
            return (String) method.invoke(context);
        } catch (Exception ignored) {
            return "hh:mm a";
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return android.widget.DigitalClock.class.getName();
    }
}
