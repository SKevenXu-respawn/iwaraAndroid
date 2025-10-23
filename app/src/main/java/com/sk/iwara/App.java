package com.sk.iwara;

import android.app.Application;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.database.StandaloneDatabaseProvider;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;


import com.sk.iwara.util.SPUtil;
import com.sk.iwara.util.VideoTask;

import java.io.File;
import java.util.ArrayList;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeCompat;
import me.jessyan.autosize.AutoSizeConfig;

@UnstableApi public class App extends Application {
    private static SimpleCache sCache;          // 必须单例
    public static Cache getCache() { return sCache; }

    @OptIn(markerClass = UnstableApi.class) @Override public void onCreate() {
        super.onCreate();
        File cacheDir = new File(getExternalCacheDir(), "exo_video");
        // 50 MB LRU 驱逐
        sCache = new SimpleCache(cacheDir,
                new LeastRecentlyUsedCacheEvictor(50 * 1024 * 1024),
                new StandaloneDatabaseProvider(this));
        VideoTask.video=new ArrayList<>();

        SPUtil.init(this,"Config");
        AutoSizeCompat.autoConvertDensityOfGlobal(getResources());
        AutoSizeConfig.getInstance().setExcludeFontScale(true);
        AutoSize.initCompatMultiProcess(this);
    }
}