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
public class VideoTagAdapter extends RecyclerView.Adapter<VideoTagAdapter.Holder> {
    private final List<String> data;

    private int checkedId = -1;

    public VideoTagAdapter(ArrayList<String> arrayList) {
        this.data = arrayList;
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
        String item = data.get(position);
        h.title.setText(item);
        h.url_name= item;
        h.imageView.setVisibility(View.GONE);
        h.layout.setOnClickListener(v ->{});
    }


    @Override public int getItemCount() { return data.size(); }
    /**
     * 点击后移除指定位置项（带动画）
     */
    private void removeItemAt(int position) {
        if (position < 0 || position >= data.size()) return;

        // 1. 数据层删除
        data.remove(position);

        // 2. UI 带动画删除
        notifyItemRemoved(position);

        // 4. 可选：立即触发外部回调（留空即可）
        // if (listener != null) listener.onItemRemoved(position);
    }
    /**
     * 动态添加一个标签项（可插入到任意位置）
     * @param position 插入位置（0-based）
     * @param bean     新标签数据
     */
    public void addItem(int position,String bean) {
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
//           this.url=url;
//           this.url_name=url_name;
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