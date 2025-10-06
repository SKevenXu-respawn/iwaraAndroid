package com.sk.iwara.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.Nullable;

public class LoginSPUtil {
    private static volatile LoginSPUtil INSTANCE;
    private final SharedPreferences sp;

    private LoginSPUtil(Context appCtx) {
        sp = appCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    public static LoginSPUtil getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LoginSPUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoginSPUtil(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }
    public void putLoginData(String username, String name, String email, String join, String lastLogin, String status, String token, String access_token, @Nullable String thumb){
            put("username",username);
            put("name",name);
            put("email",email);
            put("join",join);
            put("lastLogin",lastLogin);
            put("status",status);
            put("token",token);
            put("access_token",access_token);
            if (thumb.isEmpty()){
                put("thumb","null");
            }else{
                put("thumb",thumb);
            }


    }
    public void put(String k, String v) {
        sp.edit().putString(k, v).apply();
    }
    public String get(String k, String def) {
        return sp.getString(k, def);
    }
    public void clear() {
        sp.edit().clear().apply();
    }
}
