package com.sk.iwara.util;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.sk.iwara.App;

public class PlayerUtil {
    public static ExoPlayer createCachedPlayer(Context ctx) {
        /* 1. 浏览器级 UA & 常用校验头 */
        DefaultHttpDataSource.Factory httpFactory =
                new DefaultHttpDataSource.Factory()
                        .setUserAgent(
                                "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 " +
                                        "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");

        /* 2. 缓存层包装 */
        CacheDataSource.Factory cacheFactory =
                new CacheDataSource.Factory()
                        .setCache(App.getCache())
                        .setUpstreamDataSourceFactory(httpFactory)
                        .setFlags(CacheDataSource.FLAG_BLOCK_ON_CACHE);

        /* 3. 注入 MediaSource */
        DefaultMediaSourceFactory mediaSourceFactory =
                new DefaultMediaSourceFactory(ctx)
                        .setDataSourceFactory(cacheFactory);

        /* 4. 构建播放器 */
        return new ExoPlayer.Builder(ctx)
                .setMediaSourceFactory(mediaSourceFactory)
                .build();
    }
}