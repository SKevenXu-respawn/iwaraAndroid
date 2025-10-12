package com.sk.iwara.ui.Home;

import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.sk.iwara.MainActivity;
import com.sk.iwara.R;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentHomeBinding;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    private final String[] titles = {"热门视频", "人气视频", "最新视频"};

    @Override
    protected void init() {
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
            TextView tv = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.item_tab, binding.homeTabLayout, false);
            tv.setText(t);
            binding.homeTabLayout.addTab(binding.homeTabLayout.newTab().setCustomView(tv));
        }
        new TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager2,
                (tab, position) -> {
                    TextView tv = (TextView) LayoutInflater.from(getContext())
                            .inflate(R.layout.item_tab, binding.homeTabLayout, false);
                    tv.setText(titles[position]);
                    tab.setCustomView(tv);
                }).attach();






    }

    @Override
    protected void initUI() {

    }
    @Override
    protected void initData() {

    }
}
