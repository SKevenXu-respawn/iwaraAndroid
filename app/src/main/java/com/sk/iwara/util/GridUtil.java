package com.sk.iwara.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sk.iwara.R;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.payload.HomeVideoPayload;

import java.util.List;

public class GridUtil {
    public static void addGridCard(List<HomeVideoPayload.Results> data, GridLayout gridLayout) {
        int columns = 3;
        LayoutInflater inflater = LayoutInflater.from(gridLayout.getContext());

        SharedPreferences  sharedPreferences=gridLayout.getContext().getSharedPreferences("DEBUG_STAUTS", Context.MODE_PRIVATE);
        boolean isDeBug=!sharedPreferences.getBoolean("isDebugIng",true);
        if (isDeBug){
            Toast.makeText(gridLayout.getContext(),"DEBUGING",Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < data.size(); i++) {
            HomeVideoPayload.Results f = data.get(i);

            // 1. inflate XML
            View itemView = inflater.inflate(R.layout.video_card_layout, gridLayout, false);

            // 2. 绑定数据
            ImageView ivIcon = itemView.findViewById(R.id.video_image);
            TextView tvName = itemView.findViewById(R.id.video_text);
            if (f.getId()!=null){
                Glide.with(gridLayout.getContext())
                        .load(IWARA_API.IMAGE+"thumbnail/"+f.getFile().getId()+"/thumbnail-"+String.format("%02d", f.getThumbnail())+".jpg")
                        .into(ivIcon);
            }else{
                Glide.with(gridLayout.getContext())
                        .load(R.mipmap.no_icon)
                        .into(ivIcon);
            }

            tvName.setText(f.getTitle());

            // 3. 计算行列
            int row = i / columns;
            int col = i % columns;

            // 4. LayoutParams：占 1 行 1 列，权重=1 均分
            GridLayout.Spec rowSpec = GridLayout.spec(row, 1, 1f);
            GridLayout.Spec colSpec = GridLayout.spec(col, 1, 1f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
            params.width = 0;   // 0dp + 权重
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;

            if (isDeBug){
                // 5. 加入容器
                gridLayout.addView(itemView, params);

            }

        }
    }
}
