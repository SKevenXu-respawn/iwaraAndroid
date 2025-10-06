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
import com.sk.iwara.ui.Home.HotFragment;
import com.sk.iwara.ui.Home.NewFragment;
import com.sk.iwara.ui.Home.PopularFragment;
import com.sk.iwara.ui.Login.LoginActivity;
import com.sk.iwara.util.DateUtil;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.LoginSPUtil;
import com.sk.iwara.util.ToastUtil;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final String[] titles = {"热门视频", "人气视频", "最新视频"};
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void init() {
        sharedPreferences=getSharedPreferences("DEBUG_STAUTS", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        /* 2. ViewPager2 适配器 */
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




        binding.homeToolbar.setNavigationOnClickListener(v -> {
           binding.getRoot().openDrawer(GravityCompat.START);

        });


    }

    @Override
    protected void updateUI() {
        super.updateUI();
        binding.navView.setCheckedItem(R.id.menu_video);
        if (LoginSPUtil.getInstance(this).get("access_token","null").equals("null")){
            binding.navView.getHeaderView(0).findViewById(R.id.nav_header_login_data).setVisibility(View.GONE);
            binding.navView.getHeaderView(0).findViewById(R.id.nav_header_login_thumb).setVisibility(View.GONE);
            binding.navView.getHeaderView(0).findViewById(R.id.nav_header_no_login).setVisibility(View.VISIBLE);
            binding.navView.getHeaderView(0).findViewById(R.id.nav_header_no_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginSPUtil.getInstance(MainActivity.this).clear();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }else{
            binding.navView.getHeaderView(0).findViewById(R.id.nav_header_login_data).setVisibility(View.VISIBLE);
            binding.navView.getHeaderView(0).findViewById(R.id.nav_header_login_thumb).setVisibility(View.VISIBLE);
            binding.navView.getHeaderView(0).findViewById(R.id.nav_header_no_login).setVisibility(View.GONE);
            TextView name= binding.navView.getHeaderView(0).findViewById(R.id.nav_header_user_name);
            TextView username=binding.navView.getHeaderView(0).findViewById(R.id.nav_header_user_username);
           ImageView imageView= binding.navView.getHeaderView(0).findViewById(R.id.nav_header_user_image);
            TextView join= binding.navView.getHeaderView(0).findViewById(R.id.nav_header_user_join);
            TextView lostLogin= binding.navView.getHeaderView(0).findViewById(R.id.nav_header_user_last_login);
            TextView status= binding.navView.getHeaderView(0).findViewById(R.id.nav_header_user_status);

            status.setText(LoginSPUtil.getInstance(this).get("status","null"));
            lostLogin.setText(DateUtil.FormatDate(LoginSPUtil.getInstance(this).get("lastLogin","null")));
            join.setText(DateUtil.FormatDate(LoginSPUtil.getInstance(this).get("join","null")));
            username.setText(LoginSPUtil.getInstance(this).get("username","null"));
            name.setText(LoginSPUtil.getInstance(this).get("name","null"));
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
                Glide.with(imageView.getContext())
                        .load(glideUrl)
                        .circleCrop()
                        .error(R.mipmap.no_icon)
                        .into(imageView);
            }else{
                Glide.with(imageView.getContext())
                        .load(R.mipmap.no_icon)
                        .circleCrop()
                        .error(R.mipmap.no_icon)
                        .into(imageView);
            }

        }
    }

    @Override
    protected void initUI() {

    }
    @Override
    protected void initData() {

    }





}