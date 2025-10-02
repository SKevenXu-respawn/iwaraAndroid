package com.sk.iwara.util;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtil {
    public static void ToastUtil(String text, Activity activity){
        activity.runOnUiThread(()->{
            Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
        });
    }
}
