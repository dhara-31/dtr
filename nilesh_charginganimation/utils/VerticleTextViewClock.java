package com.si_charginganimation.nilesh_charginganimation.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.DigitalClock;

import java.lang.reflect.Method;
import java.util.Calendar;

public class VerticleTextViewClock extends androidx.appcompat.widget.AppCompatTextView {
    Calendar mCalendar;
    @SuppressWarnings("FieldCanBeLocal")
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;
    final boolean topDown;

    public VerticleTextViewClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
            topDown = false;
        } else
            topDown = true;

        initClock();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (topDown) {
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        } else {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        }


        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
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


                setText(DateFormat.format("hh:mm a", mCalendar));
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
        return DigitalClock.class.getName();
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
}
