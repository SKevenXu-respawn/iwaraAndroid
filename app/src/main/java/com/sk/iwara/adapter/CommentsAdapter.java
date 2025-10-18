package com.sk.iwara.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.sk.iwara.R;
import com.sk.iwara.payload.CommentsPayload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.Holder> {
    private List<CommentsPayload.Results> list = new ArrayList<>();

    public void addData(List<CommentsPayload.Results> more){
        Log.d("IWARAAdapter", "loadMore 返回 size = " + more.size());
        list.addAll(more);
        notifyDataSetChanged();
    }
    public void refresh(List<CommentsPayload.Results> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int viewType){
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.comment_card, p, false);
        return new Holder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder h, int position){
        CommentsPayload.Results bean = list.get(position);
        // 绑定数据

        TextView tv = h.itemView.findViewById(R.id.comment_name);
        tv.setText(bean.getUser().getUsername());


        ImageView thumb=h.itemView.findViewById(R.id.comment_thumb);
        TextView date=h.itemView.findViewById(R.id.comment_date);

        TextView detail=h.itemView.findViewById(R.id.comment_detail);
        detail.setText(String.valueOf(bean.getBody()));

        if (bean.getUser().getAvatar()!=null){
            CommentsPayload.Results.User.avatar avatar=bean.getUser().getAvatar();
            GlideUrl glideUrl = new GlideUrl("https://i.iwara.tv/image/avatar/"+avatar.getId()+"/"+avatar.getName(), new LazyHeaders.Builder()
                    .addHeader("User-Agent",
                            "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Mobile Safari/537.36 Edg/140.0.0.0")
                    .addHeader("Referer", "https://www.iwara.tv/")
                    .addHeader("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                    .addHeader("content-type", "image/jpeg")
                    // 如果浏览器带了 Cookie 也加进来
                    // .addHeader("Cookie", "session=xxx")
                    .build());
            Glide.with(thumb.getContext())
                    .load(glideUrl)
                    .circleCrop()
                    .error(R.mipmap.logo)
                    .into(thumb);
        }else{
            Glide.with(thumb.getContext())
                    .load(R.mipmap.logo)
                    .circleCrop()
                    .error(R.mipmap.logo)
                    .into(thumb);
        }

        date.setText(FormatDate(bean.getCreatedAt()));


    }
    static class Holder extends RecyclerView.ViewHolder{
        Holder(View item){ super(item); }
    }
    @Override public int getItemCount(){ return list.size(); }
    private String FormatDate(String iso){
        LocalDateTime utc = LocalDateTime.parse(iso, DateTimeFormatter.ISO_DATE_TIME);
        // 如需东八区可自行加 8 小时： utc = utc.plusHours(8);
        // 格式化
        try{
            return utc.format(DateTimeFormatter.ofPattern("yy-MM-dd-HH时mm分"));
        }catch (Exception e){
            return iso;
        }
    }
}