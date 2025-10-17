package com.sk.iwara.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.iwara.R;
import com.sk.iwara.payload.TagPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25140 on 2025/10/15 .
 */
public class SelectedTagAdapter extends RecyclerView.Adapter<SelectedTagAdapter.Holder> {
    private final List<TagPayload.ResultsBean> data;

    private int checkedId = -1;

    public SelectedTagAdapter() {
        this.data = new ArrayList<>();
    }


    static class Holder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout layout;
        ImageView imageView;
        String url_name;

        Holder(View v) {
            super(v);
            layout = v.findViewById(R.id.tag_layout);
            title = v.findViewById(R.id.tag_text);
            imageView = v.findViewById(R.id.tag_cancel);
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
        h.url_name = item.getId();
        h.layout.setOnClickListener(v -> removeItemAt(position));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 添加一个标签项（带动画 + 回调）
     */
    public void addItem(int position, TagPayload.ResultsBean bean) {
        if (position < 0 || position > data.size()) return;

        data.add(position, bean);
        notifyItemInserted(position);

        if (listener != null) {
            listener.onItemAdded(bean, position);
        }
    }

    /**
     * 移除指定位置项（带动画 + 回调）
     */
    public void removeItemAt(int position) {
        if (position < 0 || position >= data.size()) return;

        TagPayload.ResultsBean removed = data.remove(position);
        notifyItemRemoved(position);

        if (listener != null) {
            listener.onItemRemoved(removed, position);
        }
    }

    /**
     * 清空所有项（带动画 + 回调）
     */
    public void removedAll() {
        int oldSize = data.size();
        if (oldSize == 0) return;

        data.clear();
        notifyItemRangeRemoved(0, oldSize);

        if (listener != null) {
            listener.onAllItemsCleared();
        }
    }

    public List<String> getAllItem() {
        List<String> data = new ArrayList<>();
        for (TagPayload.ResultsBean item : this.data) {
            data.add(item.getId());
        }
        return data;
    }
    public interface OnItemChangedListener {
        void onItemAdded(TagPayload.ResultsBean addedItem, int position);
        void onItemRemoved(TagPayload.ResultsBean removedItem, int position);
        void onAllItemsCleared(); // 可选：清空回调
    }
    private OnItemChangedListener listener;

    public void setOnItemChangedListener(OnItemChangedListener l) {
        this.listener = l;
    }
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


