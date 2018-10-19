package com.funnyAnimal.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.funnyAnimal.activity.MyApplication;


public final class PreferenceUtil {

    public static void setUserUid(String uuid) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.UID, uuid);
        editor.apply();
    }

    public static String getUserUid() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.UID, null);
    }

    public static void setUserUuid(String uuid) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.UUID, uuid);
        editor.apply();
    }

    public static String getUserUuid() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.UUID, null);
    }

    public static void setUserName(String userName) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.USERNAME, userName);
        editor.apply();
    }

    public static String getUserName() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.USERNAME, null);
    }

    public static void setUserGender(String userGender) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.USERGENDER, userGender);
        editor.apply();
    }

    public static String getUserGender() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.USERGENDER, null);
    }

    public static void setUserCity(String userCity) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.CITY, userCity);
        editor.apply();
    }

    public static String getUserCity() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.CITY, null);
    }

    public static void setUserProvince(String userProvince) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.PROVINCE, userProvince);
        editor.apply();
    }

    public static String getUserProvince() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.PROVINCE, null);
    }

    public static void setUserPhone(String userPhone) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.USERPHONE, userPhone);
        editor.apply();
    }

    public static String getUserPhone() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.USERPHONE, null);
    }

    public static void setUserMoto(String userMoto) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.MOTO, userMoto);
        editor.apply();
    }

    public static String getUserMoto() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.MOTO, null);
    }

    public static void setUserBimapUrl(String bitmap) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.ICONURL, bitmap);
        editor.apply();
    }

    public static String getUserBitmapUrl() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.ICONURL, null);
    }

    public static void setWrapperResource(String resourceId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit();
        editor.putString(Constant.WRAPPER, resourceId);
        editor.apply();
    }

    public static String getWrapperResource() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getString(Constant.WRAPPER, null);
    }
}
