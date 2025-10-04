package com.sk.iwara.ui.Home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.sk.iwara.MainActivity;
import com.sk.iwara.adapter.VideoAdapter;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentNewBinding;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.util.GridUtil;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.ToastUtil;

public class NewFragment extends BaseFragment<FragmentNewBinding> {
    private int page = 1;                 // 当前页
    private boolean isLoading = false;    // 正在加载更多时不再重复触发
    VideoAdapter adapter;
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
        adapter= new VideoAdapter();
        binding.newRecyle.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.newRecyle.setAdapter(adapter);
        binding.newRecyle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                if (dy <= 0) return;                 // 向上滑不管

                GridLayoutManager lm = (GridLayoutManager) rv.getLayoutManager();
                int totalItemCount = lm.getItemCount();
                int lastVisible  = lm.findLastVisibleItemPosition();

                // 还剩 3 个 item 时提前加载，可自己调
                if (!isLoading && lastVisible >= totalItemCount - 3) {
                    loadMore();
                }
            }
        });
    }
    private void getData(){
        binding.getRoot().setRefreshing(true);
        HttpUtil.get().getAsync(IWARA_API.getHomeVideos("date", 15,1), null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                getActivity().runOnUiThread(()->{

                    HomeVideoPayload homeVideoPayload=new Gson().fromJson(respBody, HomeVideoPayload.class);
                    adapter.refresh(homeVideoPayload.getResults());
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
    private void loadMore() {
        if (isLoading) return;           // 防止重复
        isLoading = true;

        HttpUtil.get().getAsync(IWARA_API.getHomeVideos("trending", 20, ++page), null,
                new HttpUtil.NetCallback() {
                    @Override
                    public void onSuccess(String respBody) {
                        getActivity().runOnUiThread(() -> {
                            HomeVideoPayload payload = new Gson().fromJson(respBody, HomeVideoPayload.class);
                            adapter.addData(payload.getResults());
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        getActivity().runOnUiThread(() -> {
                            ToastUtil.ToastUtil(e.getMessage(), getActivity());
                            isLoading = false;
                            page--;          // 失败回退页码
                        });
                    }
                });
    }
}
