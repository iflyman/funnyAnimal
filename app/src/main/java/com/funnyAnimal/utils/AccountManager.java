package com.funnyAnimal.utils;

import com.funnyAnimal.activity.MyApplication;

/**
 * Created by bhyan on 2018/10/19.
 */

public class AccountManager {

    public static final String GENDER = "gender";
    public static final String ACCESSTOKEN = "accessToken";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String NAME = "name";
    public static final String ICONURL = "iconurl";

    public static void setGender(String gender) {
        SharedPreferenceUtil.setPrefString(MyApplication.getInstance(), GENDER, gender);
    }

    public static String getGender() {
        return SharedPreferenceUtil.getPrefString(MyApplication.getInstance(), GENDER, "");
    }

    public static void setAccesstoken(String accesstoken) {
        SharedPreferenceUtil.setPrefString(MyApplication.getInstance(), ACCESSTOKEN, accesstoken);
    }

    public static String getAccesstoken() {
        return SharedPreferenceUtil.getPrefString(MyApplication.getInstance(), ACCESSTOKEN, "");
    }

    public static void setProvince(String province) {
        SharedPreferenceUtil.setPrefString(MyApplication.getInstance(), PROVINCE, province);
    }

    public static String getProvince() {
        return SharedPreferenceUtil.getPrefString(MyApplication.getInstance(), PROVINCE, "");
    }

    public static void setCity(String city) {
        SharedPreferenceUtil.setPrefString(MyApplication.getInstance(), CITY, city);
    }

    public static String getCity() {
        return SharedPreferenceUtil.getPrefString(MyApplication.getInstance(), CITY, "");
    }

    public static void setName(String name) {
        SharedPreferenceUtil.setPrefString(MyApplication.getInstance(), NAME, name);
    }

    public static String getName() {
        return SharedPreferenceUtil.getPrefString(MyApplication.getInstance(), NAME, "");
    }

    public static void setIconurl(String iconurl) {
        SharedPreferenceUtil.setPrefString(MyApplication.getInstance(), ICONURL, iconurl);
    }

    public static String getIconurl() {
        return SharedPreferenceUtil.getPrefString(MyApplication.getInstance(), ICONURL, "");
    }
}
