package com.sk.iwara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sk.iwara.ViewModel.LoginViewModel;
import com.sk.iwara.adapter.MenuAdapter;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityMainBinding;
import com.sk.iwara.payload.HomeVideoPayload;

import com.sk.iwara.payload.TokenPayload;
import com.sk.iwara.payload.UserPayload;
import com.sk.iwara.ui.Bbs.BbsFragment;

import com.sk.iwara.ui.Collect.CollectFragment;
import com.sk.iwara.ui.History.HistoryFragment;
import com.sk.iwara.ui.Home.HomeFragment;
import com.sk.iwara.ui.Home.HotFragment;
import com.sk.iwara.ui.Home.NewFragment;
import com.sk.iwara.ui.Home.PopularFragment;
import com.sk.iwara.ui.Login.LoginActivity;

import com.sk.iwara.ui.Search.SearchActivity;
import com.sk.iwara.ui.Settings.SettingFragment;

import com.sk.iwara.ui.SubscribedFragment.SubScribedFragment;
import com.sk.iwara.ui.Update.UpdateFragment;
import com.sk.iwara.ui.User.UserActivity;
import com.sk.iwara.util.DateUtil;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.LockScreenHelper;
import com.sk.iwara.util.LoginSPUtil;
import com.sk.iwara.util.LoginUtil;
import com.sk.iwara.util.SPUtil;
import com.sk.iwara.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private static final int HOME_FRAGMENT = R.id.menu_video;
    private MenuAdapter adapter;
    // ① 登录状态 LiveData（生命周期感知）
    private LoginViewModel loginViewModel;
    @Override
    protected void init() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
          // onCreate
// helper.stop(); // onDestroy 记得注销
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            // 事务落盘后再拿当前 Fragment 对应的菜单 ID
            int currentId = getCurrentFragmentId();
            adapter.setCheckedItem(currentId);
        });


        // 初始化悬浮搜索栏
        MaterialToolbar searchToolbar = findViewById(R.id.base_toolbar);


        // 设置悬浮搜索栏的导航按钮（返回键）点击事件
        searchToolbar.setNavigationOnClickListener(v -> {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        binding.baseSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void updateUI() {
        super.updateUI();
        if (LoginSPUtil.getInstance(this).get("access_token", "null").equals("null")) {
            binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_login_data).setVisibility(View.GONE);
            binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_login_thumb).setVisibility(View.GONE);
            binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_no_login).setVisibility(View.VISIBLE);
            binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_no_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginSPUtil.getInstance(MainActivity.this).clear();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        } else {

            binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_login_data).setVisibility(View.VISIBLE);
            binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_login_thumb).setVisibility(View.VISIBLE);
            binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_no_login).setVisibility(View.GONE);
            TextView name = binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_user_name);
            TextView username = binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_user_username);
            ImageView imageView = binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_user_image);
            TextView join = binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_user_join);
            TextView lostLogin = binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_user_last_login);
            TextView status = binding.navView.findViewById(R.id.nav_header).findViewById(R.id.nav_header_user_status);

            status.setText(LoginSPUtil.getInstance(this).get("status", "null"));
            lostLogin.setText("最近登录为 "+DateUtil.formatAgo(LoginSPUtil.getInstance(this).get("lastLogin", "null")));
            join.setText("首次加入为 "+DateUtil.formatAgo(LoginSPUtil.getInstance(this).get("join", "null")));
            name.setText(LoginSPUtil.getInstance(this).get("username", "null"));
            username.setText(LoginSPUtil.getInstance(this).get("name", "null"));
            if (!LoginSPUtil.getInstance(this).get("thumb", "null").equals("null")) {
                GlideUrl glideUrl = new GlideUrl("https://i.iwara.tv/image/avatar/" + LoginSPUtil.getInstance(this).get("thumb", "null"), new LazyHeaders.Builder()
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
            } else {
                Glide.with(imageView.getContext())
                        .load(R.mipmap.no_icon)
                        .circleCrop()
                        .error(R.mipmap.no_icon)
                        .into(imageView);
            }
            binding.navView.findViewById(R.id.nav_header).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                }
            });

        }
    }


    @Override
    protected void updateData() {
        super.updateData();
        Log.d("MainActivity","onUpdate");
        LoginUtil.checkIsLogin(this, new LoginUtil.LoginCallBack() {
            @Override
            public void status(boolean isLogin,String name) {
                if (isLogin){
                    loginViewModel.setLogin(isLogin);
//                    runOnUiThread(()->ToastUtil.ToastUtil("欢迎回来，"+name,MainActivity.this));
                    if (!adapter.checkItem(10001)){
                        Resources res = getResources();
                        adapter.insertItem(2,new MenuAdapter.MenuItemBean(10001,"订阅",new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.mipmap.back))));
                        adapter.insertItem(3,new MenuAdapter.MenuItemBean(10002,"收藏",new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.mipmap.collect))));
                        adapter.insertItem(4,new MenuAdapter.MenuItemBean(10003,"论坛",new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.mipmap.bbs))));
                    }
                    Log.d("MainActivity","login");
                }else{
                    loginViewModel.setLogin(isLogin);
                    adapter.removeItemForId(10001,10002,10003);
                    Log.d("MainActivity","logout");

                }

            }
        });

    }

    @Override
    protected void initUI() {
        if ( !SPUtil.getBoolean("office",false)){
            ToastUtil.ToastUtil("您已打开办公模式",this);
        }
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 清除半透明状态
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FFFFFF")); // 设置状态栏颜色
        }
        adapter=new MenuAdapter(this, item -> {
            Fragment fragment = null;

            if (item.getId() == HOME_FRAGMENT) {
                fragment = new HomeFragment();
            } else if (item.getId() == R.id.menu_setting) {
                fragment = new SettingFragment();
            } else if (item.getId() == 10002) {
                fragment = new CollectFragment();
            } else if (item.getId() == 10003) {
                fragment = new BbsFragment();
            } else if (item.getId() == R.id.menu_update) {
                fragment = new UpdateFragment();
            }else if (item.getId() == R.id.menu_history) {
                fragment = new HistoryFragment();
            } else if (item.getId() == 10001) {
                fragment = new SubScribedFragment();   // ← 跳转到订阅 Fragment
            }
// 添加更多菜单项处理逻辑

            // 获取当前显示的 Fragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

            // 检查当前 Fragment 是否已经是目标 Fragment
            if (fragment != null && (currentFragment == null || !fragment.getClass().equals(currentFragment.getClass()))) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        binding.navRecycle.setLayoutManager(new LinearLayoutManager(this));
        binding.navRecycle.setAdapter(adapter);
        adapter.setCheckedItem(R.id.menu_video);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new HomeFragment())
                    .commit();

    }
    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        // 如果有 Fragment 在回退栈中，返回到上一个 Fragment
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {
            // 如果回退栈为空，退出应用
            super.onBackPressed();
        }
    }
    private int getCurrentFragmentId() {
        // 获取当前 Fragment 的 ID
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment instanceof HomeFragment) {
            return R.id.menu_video;
        } else if (currentFragment instanceof SettingFragment) {
            return R.id.menu_setting;
        } else if (currentFragment instanceof CollectFragment) {
            return 10002;
        } else if (currentFragment instanceof BbsFragment) {
            return 10003;
        } else if (currentFragment instanceof UpdateFragment) {
            return R.id.menu_update;
        } else if (currentFragment instanceof HistoryFragment) {
            return R.id.menu_history;
        }else if (currentFragment instanceof SubScribedFragment) {
            return 10001;
        }
        return R.id.menu_video; // 默认返回首页
    }


}