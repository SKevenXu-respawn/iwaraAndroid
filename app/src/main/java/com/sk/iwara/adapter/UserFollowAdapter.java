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
import com.google.gson.Gson;
import com.sk.iwara.R;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.payload.CommentsPayload;
import com.sk.iwara.payload.FollowPayload;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.ToastUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserFollowAdapter extends RecyclerView.Adapter<UserFollowAdapter.Holder> {
    private String userId;
    private List<FollowPayload.ResultsBean> list = new ArrayList<>();
    public void initData(){
        HttpUtil.get().getAsync(IWARA_API.VIDEO + "/user/"+userId+"/following?limit=15", null, null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                refresh(new Gson().fromJson(respBody,FollowPayload.class).getResults());
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("UserFollowAdapter",e.getMessage());
            }
        });
    }

    public  UserFollowAdapter(String userId){
        this.userId=userId;
    }

    public void addData(List<FollowPayload.ResultsBean> more){
        Log.d("IWARAAdapter", "loadMore 返回 size = " + more.size());
        list.addAll(more);
        notifyDataSetChanged();
    }
    public void refresh(List<FollowPayload.ResultsBean> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int viewType){
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_follow_list, p, false);
        return new Holder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder h, int position){
        FollowPayload.ResultsBean bean = list.get(position);
        // 绑定数据

        TextView tv = h.itemView.findViewById(R.id.item_follow_name);
        tv.setText(bean.getUser().getName());

        TextView cancel = h.itemView.findViewById(R.id.item_follow_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        ImageView thumb=h.itemView.findViewById(R.id.item_follow_thumb);

        if (bean.getUser().getAvatar()!=null){
            FollowPayload.ResultsBean.UserBean.AvatarBean avatar=bean.getUser().getAvatar();
            GlideUrl glideUrl = new GlideUrl("https://i.iwara.tv/image/avatar/"+avatar.getId()+"/"+avatar.getName(), new LazyHeaders.Builder()
                    .addHeader("User-Agent",
                            "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Mobile Safari/537.36 Edg/140.0.0.0")
                    .addHeader("Referer", "https://www.iwara.tv/")
                    .addHeader("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                    .addHeader("content-type", "image/jpeg")
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




    }
    static class Holder extends RecyclerView.ViewHolder{
        Holder(View item){ super(item); }
    }
    @Override public int getItemCount(){ return list.size(); }
}