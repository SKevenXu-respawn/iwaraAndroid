package com.sk.iwara.util;

import androidx.recyclerview.widget.GridLayoutManager;

import com.sk.iwara.adapter.ResultTagAdapter;

/**
 * Created by 25140 on 2025/10/16 .
 */
public class DynamicSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private final int maxSpan;      // 最大列数（短内容）
    private final int minSpan;      // 最小列数（长内容）
    ResultTagAdapter adapter;

    public DynamicSpanSizeLookup(int maxSpan, int minSpan, ResultTagAdapter adapter) {
        this.maxSpan = maxSpan;
        this.minSpan = minSpan;
        this.adapter=adapter;
    }

    @Override
    public int getSpanSize(int position) {
        // 这里用「内容长度」决定列数
        String content = adapter.getItem(position).getId(); // 你的内容字段
        int len = content.length();

        // 短 → 占 1 格（3 列），长 → 占 2 格（2 列）
        return len <= 7 ? 1 : 2;   // 20 字以内 3 列，以上 2 列
    }
}