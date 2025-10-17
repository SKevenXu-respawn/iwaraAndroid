package com.sk.iwara.ui.Video;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sk.iwara.R;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityVideoBinding;

import java.util.ArrayList;

/**
 * Created by 25140 on 2025/10/13 .
 */
public class VideoActivity extends BaseActivity<ActivityVideoBinding> {
    private FragmentManager fragmentManager;


    public void playVideo(String videoUrl, ArrayList<String> list) {
        VideoFragment newFragment = VideoFragment.newInstance(videoUrl,list);

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
            Log.i("VideoActivity",fragmentManager.getBackStackEntryCount()+"");
            if (fragmentManager.getBackStackEntryCount() == 1) {
               finish();
            }
        }
        super.onBackPressed();
    }
    @Override
    protected void init() {
        fragmentManager = getSupportFragmentManager();
        String videoUrl = getIntent().getBundleExtra("data").getString("id");
        ArrayList<String> tags = getIntent().getBundleExtra("data").getStringArrayList("tags");
        playVideo(videoUrl,tags);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }
}
