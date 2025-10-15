package com.sk.iwara.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk.iwara.App;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 25140 on 2025/10/15 .
 */
public class HistorySPUtil {
    public class HistoryItem {
        public String id;      // 业务 id
        public String title;
        public int num;
        public long   clickTime; // 点击时间戳
    }

    private final SharedPreferences sp;

    /* 构造函数：传入用户名（表名） */
    public HistorySPUtil(String userName, Activity activity) {
        sp = activity.getSharedPreferences("history_" + userName, Context.MODE_PRIVATE);
    }

    /* 点击一条记录（相同 id 更新时间并置顶） */
    public void click(String id) {
        long now = System.currentTimeMillis();
        List<HistoryItem> list = getAll();

        // 找相同 id
        HistoryItem hit = null;
        for (HistoryItem it : list) {
            if (TextUtils.equals(it.id, id)) {
                hit = it;
                break;
            }
        }
        if (hit != null) {
            list.remove(hit);          // 先删掉旧数据
        }
        // 新数据插到队首
        HistoryItem newest = new HistoryItem();
        newest.id = id;
        newest.clickTime = now;
        list.add(0, newest);

        // 持久化（只存前 200 条，防膨胀）
        if (list.size() > 200) list = list.subList(0, 200);
        save(list);
    }

    /* 获取有序列表（时间倒序） */
    public List<HistoryItem> getAll() {
        String json = sp.getString("list", "[]");
        Type type = new TypeToken<List<HistoryItem>>(){}.getType();
        return new Gson().fromJson(json, type);
    }

    /* 清空当前用户历史 */
    public void clear() {
        sp.edit().remove("list").apply();
    }

    /* 私有：保存列表 */
    private void save(List<HistoryItem> list) {
        sp.edit().putString("list", new Gson().toJson(list)).apply();
    }
}
