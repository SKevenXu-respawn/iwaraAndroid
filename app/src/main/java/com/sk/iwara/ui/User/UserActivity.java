package com.sk.iwara.ui.User;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
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
        if (!LoginSPUtil.getInstance(this).get("thumb","null").equals("null")){
            GlideUrl glideUrl = new GlideUrl("https://i.iwara.tv/image/avatar/"+LoginSPUtil.getInstance(this).get("thumb","null"), new LazyHeaders.Builder()
                    .addHeader("User-Agent",
                            "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Mobile Safari/537.36 Edg/140.0.0.0")
                    .addHeader("Referer", "https://www.iwara.tv/")
                    .addHeader("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                    .addHeader("content-type", "image/jpeg")
                    // 如果浏览器带了 Cookie 也加进来
                    // .addHeader("Cookie", "session=xxx")
                    .build());
            Glide.with(this)
                    .load(glideUrl)
                    .circleCrop()
                    .error(R.mipmap.no_icon)
                    .into(binding.userThumb);
        }else{
            Glide.with(this)
                    .load(R.mipmap.no_icon)
                    .circleCrop()
                    .error(R.mipmap.no_icon)
                    .into(binding.userThumb);
        }

    }
}
