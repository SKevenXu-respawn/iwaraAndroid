package com.sk.iwara.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.payload.UserPayload;
import com.sk.iwara.ui.Login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 25140 on 2025/10/15 .
 */
public class LoginUtil {
    public interface LoginCallBack{
        void status(boolean isLogin,String name);
    }
    public static void checkIsLogin(Activity activity,LoginCallBack cb){
        if ( LoginSPUtil.getInstance(activity).get("access_token",null)!=null) {
           cb.status(true,LoginSPUtil.getInstance(activity).get("username",null));
        }else{
            cb.status(false,"");
        }


    }

}
