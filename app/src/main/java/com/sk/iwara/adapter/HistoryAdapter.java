package com.sk.iwara.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.sk.iwara.R;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.payload.TagPayload;
import com.sk.iwara.payload.VideoDetailPayload;
import com.sk.iwara.ui.Video.VideoActivity;
import com.sk.iwara.ui.Video.VideoFragment;
import com.sk.iwara.util.HistorySPUtil;
import com.sk.iwara.util.SPUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {
    private List<HomeVideoPayload.Results> list = new ArrayList<>();
    private Context context;
    public HistoryAdapter(Context context){
        this.context=context;
    }

    public void addData(List<HomeVideoPayload.Results> more){
        Log.d("IWARAAdapter", "loadMore 返回 size = " + more.size());
        list.addAll(more);
        notifyDataSetChanged();
    }
    public void refresh(){
        list.clear();
        for(HomeVideoPayload.Results item:HistorySPUtil.getAll(context)){
            if (item.getId()!=null){
                list.add(item);
            }
            notifyItemRemoved(list.size());
        }
    }
    public void initData(){
        for(HomeVideoPayload.Results item:HistorySPUtil.getAll(context)){
            if (item.getId()!=null){
                list.add(item);
            }
            notifyItemInserted(list.size());
        }


    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int viewType){
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.history_card_layout, p, false);
        return new Holder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder h, int position){
        HomeVideoPayload.Results bean = list.get(position);
        // 绑定数据
        CardView cardView=h.itemView.findViewById(R.id.card_view);
        TextView tv = h.itemView.findViewById(R.id.video_text);
        tv.setText(bean.getTitle());
        Log.d("IWARAAdapter", "title = " + bean.getTitle());
        ImageView im=h.itemView.findViewById(R.id.video_image);
        ImageView thumb=h.itemView.findViewById(R.id.card_user_thumb);
        TextView name=h.itemView.findViewById(R.id.card_user_name);
        TextView date=h.itemView.findViewById(R.id.card_date);
        LinearLayout ll=h.itemView.findViewById(R.id.history_item_layout);
        CardView delete=h.itemView.findViewById(R.id.delete_card_view);

        if (bean.getId()!=null&& !SPUtil.getBoolean("office",false)){
            Glide.with(im.getContext())
                    .load(IWARA_API.IMAGE+"thumbnail/"+bean.getFile().getId()+"/thumbnail-"+String.format("%02d", bean.getThumbnail())+".jpg")
                    .error(R.mipmap.no_icon)
                    .into(im);
        }else{
            Glide.with(im.getContext())
                    .load(R.mipmap.no_icon)
                    .error(R.mipmap.no_icon)
                    .into(im);
        }
        if (bean.getUser()!=null){
            if (bean.getUser().getAvatar()!=null){
                HomeVideoPayload.Results.User.avatar avatar=bean.getUser().getAvatar();
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
                        .error(R.mipmap.no_icon)
                        .into(thumb);
            }else{
                Glide.with(thumb.getContext())
                        .load(R.mipmap.no_icon)
                        .circleCrop()
                        .error(R.mipmap.no_icon)
                        .into(thumb);
            }
            name.setText(bean.getUser().getUsername());
        }

        if (bean.getFile()!=null){
            date.setText(FormatDate(bean.getFile().getUpdatedAt()));
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d("VideoAdapter",bean.)
                //HistorySPUtil.add(new HistorySPUtil.HistoryItem(bean),view.getContext());

                Intent intent=new Intent(view.getContext(), VideoActivity.class);
                Bundle bd=new Bundle();
                bd.putString("id",bean.getId());
                bd.putStringArrayList("tags",getAllItem(bean.getTags()));
                intent.putExtra("data",bd);

                view.getContext().startActivity(intent);
            }
        });
        ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                animateBottomMargin(ll,40);
                return true;
            }
        });
        // 失去焦点自动隐藏
        ll.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                animateBottomMargin(ll, 0); // 恢复 0dp
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistorySPUtil.clear(bean.getId(), view.getContext());
                animateBottomMargin(ll,0);
                Log.d("HistoryAdapter", "delete");
                refresh();
            }
        });

    }
    public ArrayList<String> getAllItem(List<HomeVideoPayload.Results.Tags> tags) {
        ArrayList<String> data = new ArrayList<>();
        for (HomeVideoPayload.Results.Tags item : tags) {
            data.add(item.getId());
        }
        return data;
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
    private void animateBottomMargin(View target, int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, target.getContext().getResources().getDisplayMetrics());

        // 创建 LayoutParams 动画
        ValueAnimator animator = ValueAnimator.ofInt(getBottomMargin(target), px);
        animator.addUpdateListener(animation -> {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) target.getLayoutParams();
            params.bottomMargin = (int) animation.getAnimatedValue();
            target.setLayoutParams(params);
        });
        animator.setDuration(200); // 200ms 动画
        animator.start();
    }

    private int getBottomMargin(View target) {
        return ((FrameLayout.LayoutParams) target.getLayoutParams()).bottomMargin;
    }
}