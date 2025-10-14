package com.sk.iwara.util;

import android.app.Activity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by 25140 on 2025/10/14 .
 */
public class ActivityStack {
    private static final List<Activity> activities = new CopyOnWriteArrayList<>();

    public static void add(Activity act) {
        activities.add(act);
    }
    public static void remove(Activity act) {
        activities.remove(act);
    }
    public static void finishAll() {
        for (Activity act : activities) {
            if (!act.isFinishing()) act.finish();
        }
        activities.clear();
    }
}
