package com.sk.iwara.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sk.iwara.R;
import com.sk.iwara.payload.TagPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25140 on 2025/10/15 .
 */
public class ResultTagAdapter extends RecyclerView.Adapter<ResultTagAdapter.Holder> {
    private final List<TagPayload.ResultsBean> data;

    private int checkedId = -1;

    public ResultTagAdapter() {
        this.data = new ArrayList<>();
    }

    public interface setOnItemClickListener {
        void onItemClick(TagPayload.ResultsBean removedItem, int position);
    }

    private setOnItemClickListener listener;

    public void setOnItemClickListener(setOnItemClickListener l) {
        this.listener = l;
    }
    static class Holder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout layout;
        ImageView imageView;
        String url_name;
        Holder(View v) {
            super(v);
            layout=v.findViewById(R.id.tag_layout);
            title = v.findViewById(R.id.tag_text);
            imageView=v.findViewById(R.id.tag_cancel);
            imageView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int viewType) {
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_tag, p, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        TagPayload.ResultsBean item = data.get(position);
        h.title.setText(item.getId());
        h.url_name= item.getId();
        // 点击 → 移除 + 回调
        h.layout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item, position);
            }
        });
    }


    @Override public int getItemCount() { return data.size(); }
    /**
     * 按列表批量添加（可插入到任意位置）
     * @param position 插入位置（0-based）
     * @param newItems 新标签列表（可空）
     */
    public void addAll(int position, List<TagPayload.ResultsBean> newItems) {
        if (newItems == null || newItems.isEmpty()) return;
        if (position < 0 || position > data.size()) return;

        // 倒序插入，避免 notifyItemInserted 多次刷新
        for (int i = newItems.size() - 1; i >= 0; i--) {
            data.add(position, newItems.get(i));
            notifyItemInserted(position);
        }

    }
    public void removedAll(){
        data.clear();
        notifyDataSetChanged();
    }
    public TagPayload.ResultsBean getItem(int pos){
        return data.get(pos);
    }

    /**
     * 动态添加一个标签项（可插入到任意位置）
     * @param position 插入位置（0-based）
     * @param bean     新标签数据
     */
    public void addItem(int position, TagPayload.ResultsBean bean) {
        if (position < 0 || position > data.size()) return;

        // 1. 数据层插入
        data.add(position, bean);

        // 2. UI 带动画插入
        notifyItemInserted(position);
    }

//    public static class TagItemBean {
//        public String url_name;
//        public String url;   // title
//
//        public TagItemBean(String url,String url_name){
//            this.url=url;
//            this.url_name=url_name;
//        }
//        public TagItemBean(){}
//
//        public String getUrl_name() {
//            return url_name;
//        }
//
//        public void setUrl_name(String url_name) {
//            this.url_name = url_name;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//    }


}