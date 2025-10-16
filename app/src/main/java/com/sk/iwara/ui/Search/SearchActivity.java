package com.sk.iwara.ui.Search;

import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.sk.iwara.MainActivity;
import com.sk.iwara.adapter.VideoAdapter;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentNewBinding;
import com.sk.iwara.databinding.FragmentSearchBinding;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.util.GridUtil;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.LoginSPUtil;
import com.sk.iwara.util.ToastUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends BaseActivity<FragmentSearchBinding> {
    private int page = 1;                 // 当前页
    private boolean isLoading = false;    // 正在加载更多时不再重复触发
    VideoAdapter adapter;
    String query=null;
    String fromat=null;
    Map<String,String> map;
    @Override
    protected void init() {
        map=new HashMap<>();
        map.put("Authorization","Bearer "+ LoginSPUtil.getInstance(this).get("access_token",null));

        if (getIntent()!=null){
            query=getIntent().getStringExtra("query");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                fromat=   URLEncoder.encode(query, StandardCharsets.UTF_8);
            }
            binding.searchView.setQuery(query,false);
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        binding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    fromat=   URLEncoder.encode(query, StandardCharsets.UTF_8);
                }
                getData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
        binding.searchRecycle.setLayoutManager(new GridLayoutManager(this, 2));
        binding.searchRecycle.setAdapter(adapter);
        binding.searchRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        runOnUiThread(()->{
            binding.getRoot().setRefreshing(true);
        });

        HttpUtil.get().getAsync(IWARA_API.VIDEO+"/search?type=videos&page=0&query="+fromat, null,map, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                runOnUiThread(()->{

                    HomeVideoPayload homeVideoPayload=new Gson().fromJson(respBody, HomeVideoPayload.class);
                    adapter.refresh(homeVideoPayload.getResults());
                    binding.getRoot().setRefreshing(false);
                });
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(()->{
                    Log.e("SearchActivity",e.getMessage());
                    ToastUtil.ToastUtil(e.getMessage(),SearchActivity.this);
                    binding.getRoot().setRefreshing(false);
                });

            }
        });
    }
    private void loadMore() {
        if (isLoading) return;           // 防止重复
        isLoading = true;

        HttpUtil.get().getAsync(IWARA_API.VIDEO+"search?type=videos&page="+(page++)+"&query="+fromat, null,map,
                new HttpUtil.NetCallback() {
                    @Override
                    public void onSuccess(String respBody) {
                        runOnUiThread(() -> {
                            HomeVideoPayload payload = new Gson().fromJson(respBody, HomeVideoPayload.class);
                            adapter.addData(payload.getResults());
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        runOnUiThread(() -> {
                            ToastUtil.ToastUtil(e.getMessage(), SearchActivity.this);
                            isLoading = false;
                            page--;          // 失败回退页码
                        });
                    }
                });
    }
}
