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
            Map<String,String> map=new HashMap<>();
            map.put("Authorization","Bearer "+ LoginSPUtil.getInstance(activity).get("access_token",null));
            HttpUtil.get().getAsync(IWARA_API.VIDEO + "/user", null,map, new HttpUtil.NetCallback() {
                @Override
                public void onSuccess(String respBody) {
                    Log.d("LoginUtil",respBody);
                    UserPayload userPayload=new Gson().fromJson(respBody, UserPayload.class);
                    if (userPayload.getUser()!=null) {
                        activity.runOnUiThread(() -> {
                            LoginSPUtil.getInstance(activity).putLoginData(userPayload.getUser().getUsername(),
                                    userPayload.getUser().getName(),
                                    userPayload.getUser().getEmail(),
                                    userPayload.getUser().getCreatedAt(),
                                    userPayload.getUser().getUpdatedAt(),
                                    userPayload.getUser().getStatus(),
                                    LoginSPUtil.getInstance(activity).get("token",null),
                                    LoginSPUtil.getInstance(activity).get("access_token",null),
                                    userPayload.getUser().getAvatar() == null ? null : userPayload.getUser().getAvatar().getId() + "/" + userPayload.getUser().getAvatar().getName());

                            cb.status(true,userPayload.getUser().getUsername());
                        });
                    }else{
                        cb.status(false,null);
                        ToastUtil.ToastUtil("登陆失败或失效，请重新登陆",activity);
                        LoginSPUtil.getInstance(activity).clear();
                    }

                }

                @Override
                public void onFailure(Exception e) {
                    activity.runOnUiThread(()->{
                        Log.d("LoginUtil",e.getMessage());
                        LoginSPUtil.getInstance(activity).clear();
                        ToastUtil.ToastUtil("登陆失败，请检查账号密码是否正确! 以下为错误信息:\n"+e.getMessage(),activity);
                    });

                }
            });
        }


    }

}
