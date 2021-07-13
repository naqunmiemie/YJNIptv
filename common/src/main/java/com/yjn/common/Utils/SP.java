package com.yjn.common.Utils;

import android.app.Application;
import android.content.SharedPreferences;

public class SP {
    public static final String SP_NAME = "config";
    private static SharedPreferences sp;
    static Application application;

    public static void init(Application application){
        SP.application = application;
    }

    /**
     * 保存字符串
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putString(key, value).apply();
    }

    /**
     * 返回字符串
     *
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static String getString(String key, String defValue) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 保存布尔
     *
     * @param key
     * @param value
     */
    public static void saveBoolean(String key, boolean value) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 返回布尔
     *
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }

        return sp.getBoolean(key, defValue);
    }

    /**
     * 保存int
     *
     * @param key
     * @param value
     */
    public static void saveInt(String key, int value) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }

        sp.edit().putInt(key, value).apply();
    }
    /**
     * 返回int
     *
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static int getInt(String key, int defValue) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }

        return sp.getInt(key, defValue);
    }
    /**
     * 保存float
     *
     * @param key
     * @param value
     */
    public static void saveFloat(String key, float value) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }

        sp.edit().putFloat(key, value).apply();
    }
    /**
     * 返回float
     *
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static float getFloat(String key, float defValue) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }

        return sp.getFloat(key, defValue);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        if (sp == null){
            sp = application.getSharedPreferences(SP_NAME, 0);
        }
        return sp.contains(key);
    }
}
