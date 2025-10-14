package com.sk.iwara.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25140 on 2025/10/14 .
 */
public class ForegroundHelper implements Application.ActivityLifecycleCallbacks {

    private int activityCount = 0;
    private final List<ForegroundListener> listeners = new ArrayList<>();
    private boolean isForeground = false;
    public interface ForegroundListener {
        void onForeground(boolean foreground);
    }
    public void addListener(ForegroundListener l) {
        listeners.add(l);
    }

    public void removeListener(ForegroundListener l) {
        listeners.remove(l);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override public void onActivityStarted(Activity activity) {
        if (++activityCount == 1) isForeground = true;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override public void onActivityStopped(Activity activity) {
        if (--activityCount == 0) isForeground = false;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public boolean isForeground() {
        return isForeground;
    }
}