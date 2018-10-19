package com.funnyAnimal.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.funnyAnimal.R;
import com.funnyAnimal.activity.MyApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * @author blm-sunpeng  on 16/9/23.
 */

public final class ImageLoadFactory {

    public ImageLoadFactory() {
    }

    public static void display(Context context, String uri, ImageView imageView) {
        if (isUri(uri)) {
            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.mipmap.ic_default_load)
                    .error(R.mipmap.ic_default_load)
                    .tag(context)
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(R.mipmap.ic_default_load)
                    .into(imageView);
        }
    }

    public static void display2(Context context, String uri, ImageView imageView) {
        if (isUri(uri)) {
            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.mipmap.empty_img)
                    .error(R.mipmap.empty_img)
                    .tag(context)
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(R.mipmap.empty_img)
                    .into(imageView);
        }
    }

    public static void displayHome(Context context, String uri, ImageView imageView) {
        if (isUri(uri)) {
            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.mipmap.img_user_login)
                    .error(R.mipmap.img_user_login)
                    .tag(context)
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(R.mipmap.empty_img)
                    .into(imageView);
        }
    }

    public static void displayRounder(Context context, String uri, ImageView imageView) {
        if (isUri(uri)) {
            Picasso.with(context)
                    .load(uri)
                    .transform(new RoundTransform(20))
                    .placeholder(R.mipmap.empty_img)
                    .error(R.mipmap.empty_img)
                    .tag(context)
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(R.mipmap.empty_img)
                    .into(imageView);
        }
    }

    public static void displayForHome(Context context, String uri, ImageView imageView) {
        if (isUri(uri)) {
            Picasso.with(context)
                    .load(uri)
                    .resize((ViewUtils.getScreenWidth(MyApplication.getInstance()) - UiHelper.dp2px(MyApplication.getInstance(), 36)), UiHelper.dp2px(MyApplication.getInstance(), 300))
                    .placeholder(R.mipmap.empty_img)
                    .error(R.mipmap.empty_img)
                    .tag(context)
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(R.mipmap.empty_img)
                    .into(imageView);
        }
    }

    public static void display3(Context context, String uri, ImageView imageView) {
        if (!isUri(uri))
            return;
        Picasso.with(context)
                .load(uri)
                .tag(context)
                .into(imageView);
    }

    public static void display4(Context context, File uri, ImageView imageView) {
        Picasso.with(context)
                .load(uri)
                .placeholder(R.mipmap.empty_img)
                .error(R.mipmap.empty_img)
                .tag(context)
                .into(imageView);
    }

    public static void display(Context context, String uri, @DrawableRes int defResId, ImageView imageView) {
        if (isUri(uri)) {
            Picasso.with(context)
                    .load(uri)
                    .placeholder(defResId)
                    .error(defResId)
                    .tag(context)
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(defResId)
                    .into(imageView);
        }
    }

    public static void display(Context context, @DrawableRes int resId, ImageView imageView) {
        Picasso.with(context)
                .load(resId)
                .tag(context)
                .into(imageView);
    }

    public static boolean isUri(String uri) {
        if (TextUtils.isEmpty(uri))
            return false;
        if (uri.startsWith("http"))
            return true;
        if (uri.startsWith("file:///"))
            return true;
        if (uri.startsWith("content://"))
            return true;
        if (uri.startsWith("https"))
            return true;
        return false;
    }
}
