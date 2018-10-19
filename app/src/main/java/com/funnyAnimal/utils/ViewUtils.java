package com.funnyAnimal.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.activity.MyApplication;

/**
 * View工具类
 *
 * @author brucewuu Created on 2014/9/9.
 */
public final class ViewUtils {

    private static int statusBarHeight = 0;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    public static float density = 1;

    private static long lastClickTime;

    static {
        density = MyApplication.getInstance().getResources().getDisplayMetrics().density;
    }

    private ViewUtils() {
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float pxToDp(float px) {
        return px / density;
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕高
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (0 == screenHeight)
            screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        return screenHeight;
    }

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (0 == screenWidth)
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight == 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusBarHeight;
    }
//
//    public static int getSystemBarHeight(Context context) {
//        int result = 0;
//        int resourceId = context.getResources().getIdentifier("system_bar_height", "dimen", "android");
//
//        if (resourceId > 0) {
//            result = context.getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0,
                context.getResources().getDimensionPixelSize(R.dimen.dimen_56dp));
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static boolean isFullScreen(final Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isTranslucentStatus(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (activity.getWindow().getAttributes().flags &
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static boolean isFitsSystemWindows(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0).
                    getFitsSystemWindows();
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setDrawable(View view, Drawable drawable) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void showSystemUi(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void hideSystemUi(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * 判断是否为连击
     *
     * @return boolean
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setTextSpanColor(@NonNull TextView textView, @NonNull String text, @NonNull String spanStr, @ColorInt int color) {
        if (TextUtils.isEmpty(text))
            return;
        int start = text.indexOf(spanStr);
        if (-1 == start) {
            textView.setText(text);
            return;
        }
        SpannableString spannableString = new SpannableString(text);
        //设置文字的前景色
        spannableString.setSpan(new ForegroundColorSpan(color), start, start + spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    public static void setTextSpanSize(@NonNull TextView textView, @NonNull String text, @NonNull String spanStr, int size) {
        if (TextUtils.isEmpty(text))
            return;
        int start = text.indexOf(spanStr);
        if (-1 == start) {
            textView.setText(text);
            return;
        }
        SpannableString spannableString = new SpannableString(text);
        //设置文字的前景色
        spannableString.setSpan(new AbsoluteSizeSpan(size, true), start, start + spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    public static Spanned fromHtml(String source) {
        if (TextUtils.isEmpty(source))
            return null;
        if (isN())
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        else
            //noinspection deprecation
            return Html.fromHtml(source);
    }

    public static boolean isN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
