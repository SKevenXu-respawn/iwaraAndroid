package com.sk.iwara;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sk.iwara.util.ActivityStack;
import com.sk.iwara.util.ForegroundHelper;

/**
 * Created by 25140 on 2025/10/14 .
 */
public class LockQuitHelper extends BroadcastReceiver {

    private final Application app;
    private final ForegroundHelper fgHelper;

    public LockQuitHelper(Application app, ForegroundHelper fgHelper) {
        this.app = app;
        this.fgHelper = fgHelper;
    }

    public void register() {
        // 只有用户在前台才监听
        if (fgHelper.isForeground()) {
            IntentFilter f = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            app.registerReceiver(this, f);
        }
    }

    public void unregister() {
        try { app.unregisterReceiver(this); } catch (Exception ignore) {}
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()) && fgHelper.isForeground()) {
            quitOnLock();
        }
    }

    private void quitOnLock() {
        // 1. 立即取消监听，防止进程复活再次进入
        unregister();
        // 2. 关闭所有 Activity
        ActivityStack.finishAll();
        // 3. 回到桌面
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        app.startActivity(home);
        // 4. 自杀进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
