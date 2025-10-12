package com.sk.iwara.ui.Video;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.sk.iwara.adapter.CommentsAdapter;
import com.sk.iwara.adapter.SuggestVideoAdapter;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentTalkBinding;
import com.sk.iwara.payload.CommentsPayload;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.payload.VideoDetailPayload;
import com.sk.iwara.util.HttpUtil;

public class TalkFragment extends BaseFragment<FragmentTalkBinding> {
    VideoDetailPayload videoDetailPayload;
    CommentsPayload commentsPayload;

    CommentsAdapter adapter;
    @Override
    protected void init() {

    }

    @Override
    protected void initData() {
        getActivity().runOnUiThread(()->{
            getParentFragmentManager().setFragmentResultListener("videoData",this,(k, b) -> {
                String data = b.getString("data");
                videoDetailPayload=new Gson().fromJson(data, VideoDetailPayload.class);
                Log.d("TalkFragment",data);
                String id=videoDetailPayload.getId();
                HttpUtil.get().getAsync("https://api.iwara.tv/video/" + id + "/comments", null, null,new HttpUtil.NetCallback() {
                    @Override
                    public void onSuccess(String respBody) {
                        getActivity().runOnUiThread(()->{
                            commentsPayload=new Gson().fromJson(respBody, CommentsPayload.class);
                            Log.d("Talk", "收到评论=" + commentsPayload.getResults().size());
                            adapter.refresh(commentsPayload.getResults());
                        });


                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("SuggestFragment",e.getMessage());
                    }
                });
            });
        });
    }

    @Override
    protected void initUI() {
        adapter = new CommentsAdapter();
        binding.commentRecyle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.commentRecyle.setAdapter(adapter);
    }
}
