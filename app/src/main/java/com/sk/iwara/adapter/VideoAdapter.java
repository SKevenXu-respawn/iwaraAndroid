package com.sk.iwara.adapter;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sk.iwara.R;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.payload.HomeVideoPayload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.Holder> {
    private List<HomeVideoPayload.Results> list = new ArrayList<>();

    public void addData(List<HomeVideoPayload.Results> more){
        Log.d("IWARAAdapter", "loadMore 返回 size = " + more.size());
        list.addAll(more);
        notifyDataSetChanged();
    }
    public void refresh(List<HomeVideoPayload.Results> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int viewType){
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.video_card_layout, p, false);
        return new Holder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder h, int position){
        HomeVideoPayload.Results bean = list.get(position);
        // 绑定数据
        TextView tv = h.itemView.findViewById(R.id.video_text);
        tv.setText(bean.getTitle());
        ImageView im=h.itemView.findViewById(R.id.video_image);
        ImageView thumb=h.itemView.findViewById(R.id.card_user_thumb);
        TextView name=h.itemView.findViewById(R.id.card_user_name);
        TextView date=h.itemView.findViewById(R.id.card_date);
        if (bean.getId()!=null){
            Glide.with(im.getContext())
                    .load(IWARA_API.IMAGE+"thumbnail/"+bean.getFile().getId()+"/thumbnail-"+String.format("%02d", bean.getThumbnail())+".jpg")
                    .into(im);
        }else{
            Glide.with(im.getContext())
                    .load(R.mipmap.no_icon)
                    .into(im);
        }
        if (bean.getUser().getAvatar()!=null){
            HomeVideoPayload.Results.User.avatar avatar=bean.getUser().getAvatar();
            Glide.with(thumb.getContext())
                    .load(IWARA_API.IMAGE+"avatar/"+avatar.getId()+"/"+avatar.getName())
                    .into(thumb);
            Log.d("IWARAAdapter", "thumb = " + IWARA_API.IMAGE+"avatar/"+avatar.getId()+"/"+avatar.getId()+".jpg");
        }else{
            Glide.with(thumb.getContext())
                    .load(R.mipmap.no_icon)
                    .into(thumb);
        }
        name.setText(bean.getUser().getUsername());
        date.setText(FormatDate(bean.getFile().getUpdatedAt()));

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
            return utc.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"));
        }catch (Exception e){
            return iso;
        }
    }
}