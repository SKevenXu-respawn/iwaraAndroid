package com.sk.iwara.ui.Video;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sk.iwara.R;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityVideoBinding;

/**
 * Created by 25140 on 2025/10/13 .
 */
public class VideoActivity extends BaseActivity<ActivityVideoBinding> {
    private FragmentManager fragmentManager;


    public void playVideo(String videoUrl) {
        VideoFragment newFragment = VideoFragment.newInstance(videoUrl);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null) // 添加到返回栈
                .commit();
    }

    @Override
    public void onBackPressed() {
        // 先尝试让Fragment处理返回事件
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment instanceof VideoFragment) {
            // 如果有多个fragment，先回到上一个
            if (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStack();
                return;
            }
        }
        super.onBackPressed();
    }
    @Override
    protected void init() {
        fragmentManager = getSupportFragmentManager();

        String videoUrl = getIntent().getStringExtra("video_url");

            playVideo(videoUrl);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }
}
