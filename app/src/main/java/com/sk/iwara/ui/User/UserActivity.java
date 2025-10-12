package com.sk.iwara.ui.User;

import android.view.View;

import com.bumptech.glide.Glide;
import com.sk.iwara.R;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityUserBinding;
import com.sk.iwara.util.LoginSPUtil;
import com.sk.iwara.util.ToastUtil;

public class UserActivity extends BaseActivity<ActivityUserBinding> {

    @Override
    protected void init() {

        binding.titlebar.rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginSPUtil.getInstance(UserActivity.this).clear();
                ToastUtil.ToastUtil("已登出",UserActivity.this);
                onBackPressed();
            }
        });
        binding.titlebar.leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void updateUI() {
        super.updateUI();

    }

    @Override
    protected void initUI() {
        binding.titlebar.headTitle.setText(LoginSPUtil.getInstance(this).get("username","用户界面"));
        Glide.with(this).load(R.mipmap.logout).into(binding.titlebar.rightIcon);
    }
}
