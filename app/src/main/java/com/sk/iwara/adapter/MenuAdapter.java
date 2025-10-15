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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25140 on 2025/10/15 .
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.Holder> {
    private final List<MenuItemBean> data;
    private final OnItemClick listener;
    private int checkedId = -1;

    public interface OnItemClick { void onClick(MenuItemBean item); }

    public MenuAdapter(Activity context, OnItemClick l) {
        this.data = loadMenuItems(context);
        this.listener = l;
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout layout;
        ImageView imageView;
        Holder(View v) {
            super(v);
            layout=v.findViewById(R.id.menu_bg);
            title = v.findViewById(R.id.menu_text);
            imageView=v.findViewById(R.id.menu_image);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int viewType) {
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_menu, p, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        MenuItemBean item = data.get(position);
        h.title.setText(item.title);

        Glide.with(h.imageView).load(item.image).into(h.imageView);
        boolean isChecked = item.id == checkedId;
        h.title.setTextColor(isChecked?Color.parseColor("#FF000000"):Color.parseColor("#FF777777"));
        h.title.setBackground(!isChecked?h.itemView.getContext().getDrawable(R.drawable.transport_bg): h.itemView.getContext().getDrawable(R.drawable.left_radio_bg));
        h.itemView.setOnClickListener(v -> {
            listener.onClick(item);
            setCheckedItem(item.id);
        });
    }

    @Override public int getItemCount() { return data.size(); }
    public class MenuItemBean {
        public int    id;      // item id
        public String title;   // title
        public Drawable image;

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    private List<MenuItemBean> loadMenuItems(Activity context) {
        List<MenuItemBean> list = new ArrayList<>();
        @SuppressLint("RestrictedApi")
        Menu menu = new MenuBuilder(context);   // androidx.appcompat.view.menu
        context.getMenuInflater().inflate(R.menu.nav_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            MenuItemBean bean = new MenuItemBean();
            bean.id   = item.getItemId();
            bean.title= item.getTitle().toString();
            bean.image=item.getIcon();
            list.add(bean);
        }
        return list;
    }
    /* 手动设置选中项 */
    public void setCheckedItem(int id) {
        if (checkedId == id) return;   // 没变不刷新
        int oldPos = positionById(checkedId);
        int newPos = positionById(id);
        checkedId = id;
        if (oldPos != -1) notifyItemChanged(oldPos);   // 旧项去掉高亮
        if (newPos != -1) notifyItemChanged(newPos);   // 新项加高亮
    }

    private int positionById(int id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id == id) return i;
        }
        return -1;
    }
}