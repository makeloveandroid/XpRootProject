package com.ks.hookdemo.demo;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.ks.hookdemo.App;

public class DimenUtil {

    private static final Context CONTEXT = App.Companion.getApp();

    private DimenUtil() {

    }

    public static String px2dip(float pxValue) {
        return px2dip(pxValue, false);
    }

    public static String px2dip(float pxValue, boolean withUnit) {
        float scale = CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F) + (withUnit ? "dp" : "");
    }

    public static int dip2px(float dpValue) {
        float scale = CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static int sp2px(float sp) {
        return (int) TypedValue.applyDimension(2, sp, CONTEXT.getResources().getDisplayMetrics());
    }

    public static String px2sp(float pxValue) {
        final float fontScale = CONTEXT.getResources().getDisplayMetrics().scaledDensity;
        return String.valueOf((int) (pxValue / fontScale + 0.5f));
    }

    public static int getScreenWidth() {
        return CONTEXT.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return CONTEXT.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight() {
        Resources resources = CONTEXT.getResources();
        int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resId > 0 ? resources.getDimensionPixelSize(resId) : 0;
    }
}
