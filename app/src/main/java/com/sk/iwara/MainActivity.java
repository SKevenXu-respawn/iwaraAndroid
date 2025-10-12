package com.sk.iwara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityMainBinding;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.ui.Bbs.BbsActivity;
import com.sk.iwara.ui.Collect.CollectActivity;
import com.sk.iwara.ui.Home.HotFragment;
import com.sk.iwara.ui.Home.NewFragment;
import com.sk.iwara.ui.Home.PopularFragment;
import com.sk.iwara.ui.Login.LoginActivity;
import com.sk.iwara.ui.Settings.SettingActivity;
import com.sk.iwara.ui.Update.UpdateActivity;
import com.sk.iwara.ui.User.UserActivity;
import com.sk.iwara.util.DateUtil;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.LoginSPUtil;
import com.sk.iwara.util.ToastUtil;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final String[] titles = {"热门视频", "人气视频", "最新视频"};

    @Override
    protected void init() {
        /* 2. ViewPager2 适配器 */
        setItemSelect(R.id.menu_video);
        binding.homeViewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0: return new HotFragment();
                    case 1: return new PopularFragment();
                    default: return new NewFragment();
                }
            }
            @Override
            public int getItemCount() {
                return titles.length;
            }
        });
        for (String t : titles) {
            TextView tv = (TextView) LayoutInflater.from(this)
                    .inflate(R.layout.item_tab, binding.homeTabLayout, false);
            tv.setText(t);
            binding.homeTabLayout.addTab(binding.homeTabLayout.newTab().setCustomView(tv));
        }
        new TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager2,
                (tab, position) -> {
                    TextView tv = (TextView) LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.item_tab, binding.homeTabLayout, false);
                    tv.setText(titles[position]);
                    tab.setCustomView(tv);
                }).attach();






    }

    @Override
    protected void updateUI() {
        super.updateUI();


    }

    @Override
    protected void initUI() {

    }
    @Override
    protected void initData() {

    }





}