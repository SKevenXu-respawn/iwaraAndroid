package com.sk.iwara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Context;
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
import com.sk.iwara.util.HttpUtil;
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
        TextView textView= binding.navView.getHeaderView(0).findViewById(R.id.debug_button);
        if (sharedPreferences.getBoolean("isDebugIng",true)){
            textView.setText("DEBUGING");
            textView.setTextColor(Color.RED);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().equals("DEBUG")){
                    textView.setText("DEBUGING");
                    textView.setTextColor(Color.RED);
                    editor.putBoolean("isDebugIng",true);
                }else{
                    textView.setText("DEBUG");
                    textView.setTextColor(Color.BLACK);
                    editor.putBoolean("isDebugIng",false);
                }
                editor.commit();
            }
        });

        binding.navView.setCheckedItem(R.id.menu_video);
        binding.homeToolbar.setNavigationOnClickListener(v -> {
           binding.getRoot().openDrawer(GravityCompat.START);

        });


    }

    @Override
    protected void initUI() {

    }
    @Override
    protected void initData() {

    }





}