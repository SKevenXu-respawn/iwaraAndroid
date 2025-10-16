package com.sk.iwara.ui.Home;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.sk.iwara.R;
import com.sk.iwara.ViewModel.LoginViewModel;
import com.sk.iwara.adapter.ResultTagAdapter;
import com.sk.iwara.adapter.SelectedTagAdapter;
import com.sk.iwara.adapter.VideoAdapter;
import com.sk.iwara.adapter.VideoTagAdapter;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentHomeBinding;
import com.sk.iwara.payload.HomeVideoPayload;
import com.sk.iwara.payload.TagPayload;
import com.sk.iwara.ui.SubscribedFragment.SubScribedFragment;
import com.sk.iwara.util.DynamicSpanSizeLookup;
import com.sk.iwara.util.HttpUtil;
import com.sk.iwara.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private int page = 1;                 // 当前页
    private boolean isLoading = false;    // 正在加载更多时不再重复触发
    VideoAdapter adapter;
    SelectedTagAdapter selectedTagAdapter;
    ResultTagAdapter resultTagAdapter;
    VideoTagAdapter videoTagAdapter;

    private String type="trending";
    private void changeType(String type){
        this.type=type;

        getData();
    }
    @Override
    protected void init() {

        binding.rbHot.setChecked(true);
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbHot ){
                    changeType("trending");   // 热门
                    binding.homeTagList.setText("当前选择的分类为："+"热门视频");
                }else if (i==R.id.rbPopular){
                    changeType("popularity"); // 人气
                    binding.homeTagList.setText("当前选择的分类为："+"人气视频");
                }else if (i==R.id.rbNew){
                    changeType("date");     // 最新
                    binding.homeTagList.setText("当前选择的分类为："+"最新视频");
                }

            }
        });
        binding.homeNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.homeDrawer.isOpen()) {
                    binding.homeDrawer.openDrawer(GravityCompat.END);
                } else {
                    binding.homeDrawer.closeDrawer(GravityCompat.END);
                }
                ;

            }
        });
        binding.homeTagList.setText("当前选择的分类为：热门视频");
    }


    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected void initUI() {


        resultTagAdapter=new ResultTagAdapter();
        binding.homeTagSearchResultList.setLayoutManager(new GridLayoutManager(getContext(), 3) {{
            setSpanSizeLookup(new DynamicSpanSizeLookup(3, 2,resultTagAdapter));
        }});
       // binding.homeTagSearchResultList.setLayoutManager(new GridLayoutManager(getContext(),3));
        binding.homeTagSearchResultList.setAdapter(resultTagAdapter);


         selectedTagAdapter = new SelectedTagAdapter();
         binding.homeTagSelectedList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.homeTagSelectedList.setAdapter(selectedTagAdapter);
        binding.homeTagRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.homeTagRecycle.setAdapter(selectedTagAdapter);

        adapter= new VideoAdapter();
        binding.homeRecyle.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.homeRecyle.setAdapter(adapter);
        binding.homeRecyle.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        resultTagAdapter.setOnItemClickListener(new ResultTagAdapter.setOnItemClickListener() {
            @Override
            public void onItemClick(TagPayload.ResultsBean removedItem, int position) {

                selectedTagAdapter.addItem(selectedTagAdapter.getItemCount(),removedItem);
                binding.homeTagSelectedList.smoothScrollToPosition(selectedTagAdapter.getItemCount());
                binding.homeTagRecycle.smoothScrollToPosition(selectedTagAdapter.getItemCount());
            }
        });
        binding.homeTagSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                HttpUtil.get().getAsync(IWARA_API.VIDEO + "/autocomplete/tags?query=" + s, null, null, new HttpUtil.NetCallback() {
                    @Override
                    public void onSuccess(String respBody) {
                        TagPayload tags=new Gson().fromJson(respBody,TagPayload.class);
                        if (tags.getResults()!=null){
                            getActivity().runOnUiThread(()->{
                                resultTagAdapter.removedAll();
                                resultTagAdapter.addAll(0,tags.getResults());
                            });



                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                return false;
            }
        });
        HttpUtil.get().getAsync(IWARA_API.VIDEO+"/tags?filter=A&page=0", null, null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {

                TagPayload tags=new Gson().fromJson(respBody,TagPayload.class);
                if (tags.getResults()!=null){
                    getActivity().runOnUiThread(()->{
                        resultTagAdapter.removedAll();
                        resultTagAdapter.addAll(0,tags.getResults());
                    });



                }

            }

            @Override
            public void onFailure(Exception e) {
                Log.d("HomeFragment", "onFailure: "+e.getMessage());
            }
        });

    }
    private void getData(){
        getActivity().runOnUiThread(()->{
            showLoading();

        });

        page=1;
        /* 网络请求 */
        HttpUtil.get().getAsync(IWARA_API.getHomeVideos(type, 20,1), null,null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                getActivity().runOnUiThread(()->{

                    HomeVideoPayload homeVideoPayload=new Gson().fromJson(respBody, HomeVideoPayload.class);
                    Log.d("IWARAAdapter", "loadMore 返回 size = " + homeVideoPayload.getResults().size());
                    adapter.refresh(homeVideoPayload.getResults());
                    dismissLoading();

                });
            }

            @Override
            public void onFailure(Exception e) {

                if (binding!=null){
                    getActivity().runOnUiThread(()->{
                        ToastUtil.ToastUtil(e.getMessage(), getActivity());
                        dismissLoading();
                    });

                }

            }
        });
    }
    private void loadMore() {
        if (isLoading) return;           // 防止重复
        isLoading = true;

        HttpUtil.get().getAsync(IWARA_API.getHomeVideos(type, 20, ++page), null,null,
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
