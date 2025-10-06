package com.sk.iwara.ui.Video;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk.iwara.R;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityPlayBinding;
import com.sk.iwara.payload.VideoDetailPayload;
import com.sk.iwara.payload.VideoPlayListPayload;
import com.sk.iwara.util.DateUtil;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.PlayerUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends BaseActivity<ActivityPlayBinding> {
    private ExoPlayer player;
    private String id;

    private Handler handler = new Handler(Looper.getMainLooper());


    @Override protected void onStop() {
        super.onStop();
        player.release();
        binding.videoToolsSeekBar.removeCallbacks(progressRunnable);
    }

    @Override
    protected void init() {
        binding.videoToolsSeekBar.post(progressRunnable);


    }

    @Override
    protected void initData() {
        Bundle bd= getIntent().getBundleExtra("data");
        id= bd.getString("id");
        Log.d("VideoActivity",id);
        HttpUtil.get().getAsync(IWARA_API.VIDEO+"/video/"+id, null,null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                Log.d("VideoActivity",respBody);

                VideoDetailPayload videoDetailPayload=new Gson().fromJson(respBody,VideoDetailPayload.class);

                updateUI(videoDetailPayload);
                HttpUtil.get().getAsync(videoDetailPayload.getFileUrl(), null, null,new HttpUtil.NetCallback() {
                    @Override
                    public void onSuccess(String respBody) {
                        runOnUiThread(()->{

                            Type listType = new TypeToken<List<VideoPlayListPayload>>(){}.getType();
                            List<VideoPlayListPayload> videoPlayListPayloads = new Gson().fromJson(respBody, listType);
                            String url = "https:"+videoPlayListPayloads.get(0).getSrc().getView();
                            Log.d("VideoActivity",url);
                            MediaItem item = MediaItem.fromUri(url);
                            player.setMediaItem(item);
                            player.prepare();
                            player.play();
                        });


                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("VideoActivity",e.toString());
                    }
                });



            }

            @Override
            public void onFailure(Exception e) {
                Log.d("VideoActivity",e.toString());
            }
        });

    }
    private void updateUI(VideoDetailPayload videoDetailPayload){


        runOnUiThread(()->{
            // 1. 把值抛出去
            binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    // 当用户滑到 0 或 1 时再发
                    Bundle b = new Bundle();
                    b.putString("data", new Gson().toJson(videoDetailPayload));
                    getSupportFragmentManager().setFragmentResult("videoData", b);
                }
            });
//            Bundle result = new Bundle();
//            result.putString("data", new Gson().toJson(videoDetailPayload));
//            getSupportFragmentManager().setFragmentResult("videoData", result);

            VideoPagerAdapter adapter = new VideoPagerAdapter(this);
            binding.viewPager.setAdapter(adapter);

            new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                    (tab, position) -> tab.setText(position == 0 ? "推荐视频" : "评论 "+videoDetailPayload.getNumComments())
            ).attach();
            binding.videoDetailTitle.setText(videoDetailPayload.getTitle());
            binding.videoDetailJianjie.setText(videoDetailPayload.getBody());
            binding.videoDetailUserName.setText(videoDetailPayload.getUser().getUsername());
            binding.videoDetailUserUpdate.setText(DateUtil.FormatDate( videoDetailPayload.getFile().getUpdatedAt()));
            binding.videoDetailLikesNum.setText(String.valueOf(videoDetailPayload.getNumLikes()) );
            binding.videoDetailViewsNum.setText(String.valueOf(videoDetailPayload.getNumViews()));

            GlideUrl glideUrl = new GlideUrl("https://i.iwara.tv/image/avatar/"+videoDetailPayload.getUser().getAvatar().getId()+"/"+videoDetailPayload.getUser().getAvatar().getName(), new LazyHeaders.Builder()
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
                    .error(R.mipmap.logo)
                    .into(binding.videoDetailUserThumb);
        });

    }
    @Override
    protected void initUI() {
        player = PlayerUtil.createCachedPlayer(VideoActivity.this);
        binding.playerView.setPlayer(player);
        binding.videoToolsTogglePlay.setOnClickListener(v -> {
            player.setPlayWhenReady(!player.getPlayWhenReady());

        });
        updatePlayBtn();
        binding.videoToolsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                // 播放器自己状态变了（包括缓冲完、缓冲中、用户点击、自动播放下一个）
                updatePlayBtn();
            }
        });
        binding.videoToolsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private long newPosition;   // 记住用户拖到的位置

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    long duration = player.getDuration();
                    if (duration > 0) {
                        newPosition = duration * progress / 100;
                        // 只更新文字，不 setProgress（否则自己改自己）
                        binding.videoToolsSeekTime.setText(
                                formatTime(newPosition) + "/" + formatTime(duration));
                    }
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(newPosition);   // 关键：真正跳转
            }
        });
        binding.playerView.setOnClickListener(v -> toggleControl());
        handler.post(progressRunnable);
    }
    /* 7. 进度条 1 秒刷新一次 */
    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (player == null) return;
            long current = player.getCurrentPosition();
            long duration = player.getDuration();
            int progress = duration > 0 ? (int)(current * 100 / duration) : 0;

            binding.videoToolsSeekBar.setProgress(progress);
            binding.videoToolsSeekTime.setText(
                    formatTime(current) + "/" + formatTime(duration));
            binding.videoToolsSeekBar.postDelayed(this, 1000);
        }
    };
    /* 8. 工具：更新图标 */
    private void updatePlayBtn() {
        binding.videoToolsTogglePlay.setImageResource(player.getPlayWhenReady()
                ? R.mipmap.play
                : R.mipmap.pause);
    }
    private void toggleControl() {
        binding.videoToolsTop.setVisibility(
                binding.videoToolsTop.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        binding.videoToolsBottom.setVisibility(
                binding.videoToolsBottom.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
    private String formatTime(long ms) {
        long s = ms / 1000;
        long h = s / 3600, m = (s % 3600) / 60, sec = s % 60;
        return h > 0 ? String.format("%02d:%02d:%02d", h, m, sec)
                : String.format("%02d:%02d", m, sec);
    }
    public class VideoPagerAdapter extends FragmentStateAdapter {
        public VideoPagerAdapter(@NonNull FragmentActivity fa) { super(fa); }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new SuggestFragment() : new TalkFragment();
        }

        @Override
        public int getItemCount() { return 2; }
    }
}