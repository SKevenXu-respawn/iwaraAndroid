package com.sk.iwara.ui.Video;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.sk.iwara.adapter.SuggestVideoAdapter;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentSuggestBinding;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.payload.VideoDetailPayload;
import com.sk.iwara.payload.VideoPlayListPayload;
import com.sk.iwara.util.HttpUtil;

public class SuggestFragment extends BaseFragment<FragmentSuggestBinding> {
    VideoDetailPayload videoDetailPayload;
    private SuggestVideoAdapter adapter;
    @Override
    protected void init() {

    }

    @Override
    protected void initData() {
        getActivity().runOnUiThread(()->{
            getParentFragmentManager().setFragmentResultListener("videoData", this, (k, b) -> {
                String data = b.getString("data");
                videoDetailPayload=new Gson().fromJson(data,VideoDetailPayload.class);
                Log.d("SuggestFragment",data);
                String user=videoDetailPayload.getUser().getId();
                String id=videoDetailPayload.getId();
                HttpUtil.get().getAsync("https://api.iwara.tv/videos?rating=ecchi&user=" + user + "&exclude=" + id + "&limit=15", null,null, new HttpUtil.NetCallback() {
                    @Override
                    public void onSuccess(String respBody) {
                        getActivity().runOnUiThread(()->{
                            HomeVideoPayload homeVideoPayload=new Gson().fromJson(respBody,HomeVideoPayload.class);
                            adapter.addData(homeVideoPayload.getResults());
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
        adapter = new SuggestVideoAdapter();
        binding.suggestRecyle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.suggestRecyle.setAdapter(adapter);
    }
}
