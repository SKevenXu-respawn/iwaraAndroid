package com.sk.iwara.util;

import android.annotation.SuppressLint;
import android.view.MotionEvent;

import com.google.android.exoplayer2.ui.PlayerView;

/**
 * Created by 25140 on 2025/10/13 .
 */


import android.annotation.SuppressLint;
import android.view.MotionEvent;
import com.google.android.exoplayer2.ui.PlayerView;

public final class PlayerSwipeSeek {

    private static final int MIN_SWIPE_PIXEL = 50; // 滑动阈值
    private static final int MAX_CLICK_OFFSET = 50; // 点击阈值（可同值）

    private float downX, downY;
    private long currentPos, duration;
    private int widthPixels;

    /* 点击回调 */
    public interface OnClickListener {
        void onClick();
    }
    private OnClickListener clickListener;

    public PlayerSwipeSeek setOnClickListener(OnClickListener l) {
        this.clickListener = l;
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void attach(PlayerView playerView) {
        widthPixels = playerView.getResources().getDisplayMetrics().widthPixels;
        playerView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    currentPos = playerView.getPlayer().getCurrentPosition();
                    duration   = playerView.getPlayer().getDuration();
                    return true;

                case MotionEvent.ACTION_UP:
                    float dx = event.getRawX() - downX;
                    float dy = event.getRawY() - downY;

                    // 1. 先判断是否为点击
                    if (Math.abs(dx) < MAX_CLICK_OFFSET && Math.abs(dy) < MAX_CLICK_OFFSET) {
                        if (clickListener != null) clickListener.onClick();
                        return true;
                    }

                    // 2. 再判断滑动
                    if (Math.abs(dx) < MIN_SWIPE_PIXEL) return true;

                    long target = (long) (currentPos + dx * (duration / (float) widthPixels));
                    if (target < 0 || target > duration) return true;
                    playerView.getPlayer().seekTo(target);
                    return true;
            }
            return false;
        });
    }
}