package com.si_charginganimation.nilesh_charginganimation.other;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class HelperResize {
    public static final int NEXT_CLICK_TIME = 1500;
    public static int SCALE_HEIGHT = 1920;
    public static int SCALE_WIDTH = 1080;
    public static int height;
    public static int width;

    public static void FS(Activity mActivity) {
        mActivity.getWindow().addFlags(1024);
    }

    public static void FS2(Activity mActivity) {
        mActivity.getWindow().getDecorView().setSystemUiVisibility(4102);
    }

    public static void getheightandwidth(Context context) {
        getHeight(context);
        getwidth(context);
    }

    public static int getwidth(Context context) {
        int i = context.getResources().getDisplayMetrics().widthPixels;
        width = i;
        return i;
    }

    public static int getHeight(Context context) {
        int i = context.getResources().getDisplayMetrics().heightPixels;
        height = i;
        return i;
    }

    public static void setHeight(Context mContext, View view, int v_height) {
        view.getLayoutParams().height = (mContext.getResources().getDisplayMetrics().heightPixels * v_height) / SCALE_HEIGHT;
    }

    public static void setWidth(Context mContext, View view, int v_Width) {
        view.getLayoutParams().width = (mContext.getResources().getDisplayMetrics().widthPixels * v_Width) / SCALE_WIDTH;
    }

    public static int setHeight(int h) {
        return (height * h) / 1920;
    }

    public static int setWidth(int w) {
        return (width * w) / 1080;
    }

    public static void setSize(View view, int width2, int height2) {
        view.getLayoutParams().height = setHeight(height2);
        view.getLayoutParams().width = setWidth(width2);
    }


    public static void setHeightByWidth(Context mContext, View view, int v_height) {
        view.getLayoutParams().height = (mContext.getResources().getDisplayMetrics().widthPixels * v_height) / SCALE_WIDTH;
    }

    public static void setSize(View view, int width2, int height2, boolean sameheightandwidth) {
        if (sameheightandwidth) {
            view.getLayoutParams().height = setWidth(height2);
            view.getLayoutParams().width = setWidth(width2);
            return;
        }
        view.getLayoutParams().height = setHeight(height2);
        view.getLayoutParams().width = setHeight(width2);
    }

    public static void setMargin(View view, int left, int top, int right, int bottom) {
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(setWidth(left), setHeight(top), setWidth(right), setHeight(bottom));
    }

    public static void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
    }


    public static float convertDpToPixel(float dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().densityDpi / 160.0f);
    }
}
