package com.si_charginganimation.nilesh_charginganimation.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import android.util.AttributeSet;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.si_charginganimation.nilesh_charginganimation.R;

/**
 * Created by jhernandez on 04/12/2015.
 *
 */
public class CustomColorSwitchCompat extends SwitchCompat {

    protected int toggleOnColor = Color.parseColor("#009284");
    protected int toggleOffColor = Color.parseColor("#ececec");
    protected int bgOnColor = Color.parseColor("#97d9d7");
    protected int bgOffColor = Color.parseColor("#a6a6a6");

    public CustomColorSwitchCompat(Context context) {
        super(context);
    }

    public CustomColorSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(context, attrs);
    }

    public CustomColorSwitchCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(context, attrs);
    }

    private void applyAttributes(Context context, AttributeSet attrs) {

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomColorSwitchCompat);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.CustomColorSwitchCompat_toggleOnColor) {
                toggleOnColor = a.getColor(attr, Color.parseColor("#009284"));
            } else if (attr == R.styleable.CustomColorSwitchCompat_toggleOffColor) {
                toggleOffColor = a.getColor(attr, Color.parseColor("#ececec"));
            } else if (attr == R.styleable.CustomColorSwitchCompat_bgOnColor) {
                bgOnColor = a.getColor(attr, Color.parseColor("#97d9d7"));
            } else if (attr == R.styleable.CustomColorSwitchCompat_bgOffColor) {
                bgOffColor = a.getColor(attr, Color.parseColor("#a6a6a6"));
            }
        }
        a.recycle();
    }

    public int getToggleOnColor() {
        return toggleOnColor;
    }

    public void setToggleOnColor(int toggleOnColor) {
        this.toggleOnColor = toggleOnColor;
    }

    public int getToggleOffColor() {
        return toggleOffColor;
    }

    public void setToggleOffColor(int toggleOffColor) {
        this.toggleOffColor = toggleOffColor;
    }

    public int getBgOnColor() {
        return bgOnColor;
    }

    public void setBgOnColor(int bgOnColor) {
        this.bgOnColor = bgOnColor;
    }

    public int getBgOffColor() {
        return bgOffColor;
    }

    public void setBgOffColor(int bgOffColor) {
        this.bgOffColor = bgOffColor;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (this.isChecked()) {

            DrawableCompat.setTint(this.getThumbDrawable(), toggleOnColor);
            DrawableCompat.setTint(this.getTrackDrawable(), bgOnColor);
        } else {

            DrawableCompat.setTint(this.getThumbDrawable(), toggleOffColor);
            DrawableCompat.setTint(this.getTrackDrawable(), bgOffColor);
        }
        requestLayout();
        invalidate();
    }

}