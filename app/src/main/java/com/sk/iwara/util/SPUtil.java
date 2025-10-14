package com.sk.iwara.util;

/**
 * Created by 25140 on 2025/10/14 .
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

/**
 * 极简 SharedPreferences 工具类
 * 需在 Application.onCreate() 中初始化：SPUtil.init(this);
 */
public class SPUtil {

    private static SPUtil INSTANCE;
    private final SharedPreferences sp;

    /* ===================== 初始化 ===================== */
    public static void init(Context appContext,String fileName) {
        if (INSTANCE == null) {
            synchronized (SPUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SPUtil(appContext,fileName);
                }
            }
        }
    }

    private SPUtil(Context app,String fileName) {
        sp = app.getSharedPreferences(fileName, Context.MODE_PRIVATE); // 文件名可改
    }

    private static SPUtil get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("请先调用 SPUtil.init(Application)");
        }
        return INSTANCE;
    }

    /* ===================== 写 ===================== */
    public static void putString(String key, String value) {
        get().sp.edit().putString(key, value).apply();
    }

    public static void putInt(String key, int value) {
        get().sp.edit().putInt(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        get().sp.edit().putBoolean(key, value).apply();
    }

    public static void putFloat(String key, float value) {
        get().sp.edit().putFloat(key, value).apply();
    }

    public static void putLong(String key, long value) {
        get().sp.edit().putLong(key, value).apply();
    }

    public static void putStringSet(String key, Set<String> value) {
        get().sp.edit().putStringSet(key, value).apply();
    }

    /* ===================== 读 ===================== */
    public static String getString(String key) {
        return get().sp.getString(key, "");
    }

    public static String getString(String key, String defaultVal) {
        return get().sp.getString(key, defaultVal);
    }

    public static int getInt(String key) {
        return get().sp.getInt(key, 0);
    }

    public static int getInt(String key, int defaultVal) {
        return get().sp.getInt(key, defaultVal);
    }

    public static boolean getBoolean(String key) {
        return get().sp.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultVal) {
        return get().sp.getBoolean(key, defaultVal);
    }

    public static float getFloat(String key) {
        return get().sp.getFloat(key, 0f);
    }

    public static float getFloat(String key, float defaultVal) {
        return get().sp.getFloat(key, defaultVal);
    }

    public static long getLong(String key) {
        return get().sp.getLong(key, 0L);
    }

    public static long getLong(String key, long defaultVal) {
        return get().sp.getLong(key, defaultVal);
    }

    public static Set<String> getStringSet(String key) {
        return get().sp.getStringSet(key, null);
    }

    /* ===================== 删 ===================== */
    public static void remove(String key) {
        get().sp.edit().remove(key).apply();
    }

    public static void clearAll() {
        get().sp.edit().clear().apply();
    }

    /* ===================== 是否存在 ===================== */
    public static boolean contains(String key) {
        return get().sp.contains(key);
    }
}
