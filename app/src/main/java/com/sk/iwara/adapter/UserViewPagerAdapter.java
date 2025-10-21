package com.sk.iwara.adapter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sk.iwara.ui.User.UserFollowFragment;
import com.sk.iwara.ui.User.UserVideoFragment;

import java.util.List;

/**
 * Created by 25140 on 2025/10/20 .
 */
public class UserViewPagerAdapter extends FragmentStateAdapter {
    private final List<String> titles;
    private String userId;

    public UserViewPagerAdapter(@NonNull FragmentActivity fa, List<String> titles,String userId) {
        super(fa);
        this.titles = titles;
        this.userId=userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment f;
        if (position == 0) {
            f = new UserFollowFragment();
        } else if (position == 1) {
            f = new UserVideoFragment();
        } else {
            f = new UserFollowFragment();
        }
        Log.e("UserViewPagerAdapter",position+" "+ userId);
        Bundle args = new Bundle();
        args.putString("userId", userId);   // 关键
        f.setArguments(args);
        return f;
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
}
