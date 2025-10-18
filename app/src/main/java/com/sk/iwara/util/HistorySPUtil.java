package com.sk.iwara.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk.iwara.App;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.payload.VideoDetailPayload;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 25140 on 2025/10/15 .
 */
public class HistorySPUtil {
    private static final String NAME="history";



    /* 构造函数：传入用户名（表名） */
    public static void add(HomeVideoPayload.Results item, Context context){
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(item.getId(),new Gson().toJson(item)).apply();
    }
    public static List<HomeVideoPayload.Results> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sp.getAll(); // 所有 key-value

        List<HomeVideoPayload.Results> list = new ArrayList<>();
        Gson gson = new Gson();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue(); // 一定是 String
            HomeVideoPayload.Results item = gson.fromJson(json, HomeVideoPayload.Results.class);
            list.add(item);
        }
        return list;
    }
    public static void clearAll(Context context){
        context.getSharedPreferences(NAME,Context.MODE_PRIVATE).edit().clear().apply();
    }
    public static void clear(String id,Context context){
        context.getSharedPreferences(NAME,Context.MODE_PRIVATE).edit().remove(id).apply();
    }

}
