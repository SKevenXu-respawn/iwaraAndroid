package com.sk.iwara.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by 25140 on 2025/10/14 .
 */
public class LockScreenHelper {

    private final Context ctx;
    private final BroadcastReceiver receiver;

    public LockScreenHelper(Context ctx, Callback cb) {
        this.ctx = ctx;
        this.receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                String a = intent.getAction();
                if (Intent.ACTION_SCREEN_OFF.equals(a)) {
                    cb.onScreenOff();          // 即将锁屏
                } else if (Intent.ACTION_SCREEN_ON.equals(a)) {
                    cb.onScreenOn();           // 开始解锁
                } else if (Intent.ACTION_USER_PRESENT.equals(a)) {
                    cb.onUserPresent();        // 解锁完成
                }
            }
        };
    }

    public void start() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        ctx.registerReceiver(receiver, filter);
    }

    public void stop() {
        ctx.unregisterReceiver(receiver);
    }

    public interface Callback {
        void onScreenOff();
        void onScreenOn();
        void onUserPresent();
    }
}
