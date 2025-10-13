package com.sk.iwara.ui.Login;

import android.app.Dialog;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sk.iwara.R;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityLoginBinding;
import com.sk.iwara.payload.TokenPayload;
import com.sk.iwara.payload.UserPayload;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.LoadingUtil;
import com.sk.iwara.util.LoginSPUtil;
import com.sk.iwara.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Override
    protected void init() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {
        binding.titlebar.leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.titlebar.headTitle.setText("登录");
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LoginActivity","Login button click");
                if (binding.loginPassword.getText().toString().isEmpty()||binding.loginEmail.getText().toString().isEmpty()){
                    ToastUtil.ToastUtil("请输入邮箱或密码",LoginActivity.this);
                }else{

                    showLoading();

                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("email",binding.loginEmail.getText().toString());
                    jsonObject.addProperty("password",binding.loginPassword.getText().toString());
                    HttpUtil.get().postJsonAsync(IWARA_API.VIDEO + "/user/login", new Gson().toJson(jsonObject),null, new HttpUtil.NetCallback() {
                        @Override
                        public void onSuccess(String respBody) {
                            TokenPayload tokenPayload=new Gson().fromJson(respBody,TokenPayload.class);
                            if (!tokenPayload.getToken().isEmpty()){
                                Map<String,String> map=new HashMap<>();
                                map.put("Authorization","Bearer "+tokenPayload.getToken());
                                String token=tokenPayload.getToken();
                                HttpUtil.get().postJsonAsync(IWARA_API.VIDEO + "/user/token",null,map, new HttpUtil.NetCallback() {
                                    @Override
                                    public void onSuccess(String respBody) {
                                        TokenPayload tokenPayload=new Gson().fromJson(respBody,TokenPayload.class);
                                        if (!tokenPayload.getAccess_token().isEmpty()) {
                                            Map<String,String> map=new HashMap<>();
                                            map.put("Authorization","Bearer "+tokenPayload.getAccess_token());
                                            HttpUtil.get().getAsync(IWARA_API.VIDEO + "/user", null,map, new HttpUtil.NetCallback() {
                                                @Override
                                                public void onSuccess(String respBody) {
                                                    Log.d("LoginActivity",respBody);
                                                    UserPayload userPayload=new Gson().fromJson(respBody, UserPayload.class);
                                                    if (userPayload.getUser()!=null){
                                                        runOnUiThread(()->{
                                                            LoginSPUtil.getInstance(LoginActivity.this).putLoginData(userPayload.getUser().getUsername(),
                                                                    userPayload.getUser().getName(),
                                                                    userPayload.getUser().getEmail(),
                                                                    userPayload.getUser().getCreatedAt(),
                                                                    userPayload.getUser().getUpdatedAt(),
                                                                    userPayload.getUser().getStatus(),
                                                                    token,
                                                                    tokenPayload.getAccess_token(),
                                                                    userPayload.getUser().getAvatar()==null?null: userPayload.getUser().getAvatar().getId()+"/"+userPayload.getUser().getAvatar().getName());
                                                            ToastUtil.ToastUtil(userPayload.getUser().getName()+" 欢迎回来!",LoginActivity.this);
                                                            dismissLoading();

                                                            onBackPressed();
                                                        });

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Exception e) {
                                                    dismissLoading();
                                                    Log.d("LoginActivity",e.getMessage());
                                                    ToastUtil.ToastUtil("登陆失败，请检查账号密码是否正确! 以下为错误信息:\n"+e.getMessage(),LoginActivity.this);
                                                }
                                            });
                                        }
                                        }

                                    @Override
                                    public void onFailure(Exception e) {
                                        dismissLoading();
                                        Log.d("LoginActivity",e.getMessage());
                                        ToastUtil.ToastUtil("登陆失败，请检查账号密码是否正确! 以下为错误信息:\n"+e.getMessage(),LoginActivity.this);
                                    }
                                });
                            }
                            }


                        @Override
                        public void onFailure(Exception e) {
                            dismissLoading();
                            Log.d("LoginActivity",e.getMessage());
                            ToastUtil.ToastUtil("登陆失败，请检查账号密码是否正确! 以下为错误信息:\n"+e.getMessage(),LoginActivity.this);
                        }
                    });
                }
            }
        });
    }
}
