package com.sk.iwara.ui.User;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sk.iwara.R;
import com.sk.iwara.adapter.UserViewPagerAdapter;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityUserBinding;
import com.sk.iwara.util.DateUtil;
import com.sk.iwara.util.LoginSPUtil;
import com.sk.iwara.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

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
        List<String> titles = Arrays.asList("关注中", "发布视频");


        binding.userViewPager.setAdapter(new UserViewPagerAdapter(this, titles,LoginSPUtil.getInstance(this).get("userId","null")));

        // 核心：TabLayoutMediator 自动双向联动
        new TabLayoutMediator(binding.userTabLayout, binding.userViewPager,
                (tab, position) -> tab.setText(titles.get(position))
        ).attach();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void updateUI() {
        super.updateUI();
        binding.userEmail.setText("邮箱: "+LoginSPUtil.getInstance(this).get("email",""));
        binding.userJoin.setText("加入于: "+ DateUtil.formatAgo(LoginSPUtil.getInstance(this).get("join","")));
        binding.userLastLogin.setText("最后一次登录在: "+DateUtil.formatAgo(LoginSPUtil.getInstance(this).get("lastLogin","")));
        binding.userStatus.setText(LoginSPUtil.getInstance(this).get("status","").equals("active")?"活跃中":"不活跃");
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
