package com.sk.iwara.ui.Home;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.sk.iwara.MainActivity;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentNewBinding;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.util.GridUtil;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.ToastUtil;

public class NewFragment extends BaseFragment<FragmentNewBinding> {
    @Override
    protected void init() {
        binding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void initData() {
        getData();

    }

    @Override
    protected void initUI() {

    }
    private void getData(){
        binding.getRoot().setRefreshing(true);
        HttpUtil.get().getAsync(IWARA_API.getHomeVideos("date", 15,1), null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                getActivity().runOnUiThread(()->{
                    binding.newGrid.removeAllViews();
                    HomeVideoPayload homeVideoPayload=new Gson().fromJson(respBody, HomeVideoPayload.class);
                    GridUtil.addGridCard(homeVideoPayload.getResults(),binding.newGrid);
                    binding.getRoot().setRefreshing(false);
                });
            }

            @Override
            public void onFailure(Exception e) {
                ToastUtil.ToastUtil(e.getMessage(), getActivity());
                binding.getRoot().setRefreshing(false);
            }
        });
    }
}
