package com.sk.iwara;

import android.app.Application;
import android.util.Log;

import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.sk.iwara.util.ForegroundHelper;
import com.sk.iwara.util.LockScreenHelper;
import com.sk.iwara.util.SPUtil;
import com.sk.iwara.util.ToastUtil;
import com.sk.iwara.util.VideoTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static SimpleCache sCache;          // 必须单例
    public static SimpleCache getCache() { return sCache; }

    @Override public void onCreate() {
        super.onCreate();
        File cacheDir = new File(getExternalCacheDir(), "exo_video");
        // 50 MB LRU 驱逐
        sCache = new SimpleCache(cacheDir,
                new LeastRecentlyUsedCacheEvictor(50 * 1024 * 1024),
                new StandaloneDatabaseProvider(this));
        VideoTask.video=new ArrayList<>();


    }
}