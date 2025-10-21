package com.sk.iwara.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.payload.TokenPayload;
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
//    public static void refreshLogin(Activity activity,LoginCallBack cb){
//
//
//
//
//            JsonObject jsonObject=new JsonObject();
//            jsonObject.addProperty("email",LoginSPUtil.getInstance(activity).get("email",""));
//            jsonObject.addProperty("password",binding.loginPassword.getText().toString());
//            HttpUtil.get().postJsonAsync(IWARA_API.VIDEO + "/user/login", new Gson().toJson(jsonObject),null, new HttpUtil.NetCallback() {
//                @Override
//                public void onSuccess(String respBody) {
//                    TokenPayload tokenPayload=new Gson().fromJson(respBody,TokenPayload.class);
//                    if (!tokenPayload.getToken().isEmpty()){
//                        Map<String,String> map=new HashMap<>();
//                        map.put("Authorization","Bearer "+tokenPayload.getToken());
//                        String token=tokenPayload.getToken();
//                        HttpUtil.get().postJsonAsync(IWARA_API.VIDEO + "/user/token",null,map, new HttpUtil.NetCallback() {
//                            @Override
//                            public void onSuccess(String respBody) {
//                                TokenPayload tokenPayload=new Gson().fromJson(respBody,TokenPayload.class);
//                                if (!tokenPayload.getAccess_token().isEmpty()) {
//                                    Map<String,String> map=new HashMap<>();
//                                    map.put("Authorization","Bearer "+tokenPayload.getAccess_token());
//                                    HttpUtil.get().getAsync(IWARA_API.VIDEO + "/user", null,map, new HttpUtil.NetCallback() {
//                                        @Override
//                                        public void onSuccess(String respBody) {
//                                            Log.d("LoginActivity",respBody);
//                                            UserPayload userPayload=new Gson().fromJson(respBody, UserPayload.class);
//                                            if (userPayload.getUser()!=null){
//                                                activity.runOnUiThread(()->{
//                                                    LoginSPUtil.getInstance(activity).putLoginData(
//                                                            userPayload.getUser().getId(),
//                                                            userPayload.getUser().getUsername(),
//                                                            userPayload.getUser().getName(),
//                                                            userPayload.getUser().getEmail(),
//                                                            userPayload.getUser().getCreatedAt(),
//                                                            userPayload.getUser().getUpdatedAt(),
//                                                            userPayload.getUser().getStatus(),
//                                                            token,
//                                                            tokenPayload.getAccess_token(),
//                                                            userPayload.getUser().getAvatar()==null?null: userPayload.getUser().getAvatar().getId()+"/"+userPayload.getUser().getAvatar().getName());
//                                                    ToastUtil.ToastUtil(userPayload.getUser().getName()+" 欢迎回来!",activity);
//
//                                                    Log.d("access_token",tokenPayload.getAccess_token());
//                                                });
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Exception e) {
//
//                                            Log.d("LoginActivity",e.getMessage());
//                                            ToastUtil.ToastUtil("登陆失败，请检查账号密码是否正确! 以下为错误信息:\n"+e.getMessage(),activity);
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Exception e) {
//
//                                Log.d("LoginActivity",e.getMessage());
//                                ToastUtil.ToastUtil("登陆失败，请检查账号密码是否正确! 以下为错误信息:\n"+e.getMessage(),activity);
//                            }
//                        });
//                    }
//                }
//
//
//                @Override
//                public void onFailure(Exception e) {
//
//                    Log.d("LoginActivity",e.getMessage());
//                    ToastUtil.ToastUtil("登陆失败，请检查账号密码是否正确! 以下为错误信息:\n"+e.getMessage(),activity);
//                }
//            });
//
//        }
    

}
