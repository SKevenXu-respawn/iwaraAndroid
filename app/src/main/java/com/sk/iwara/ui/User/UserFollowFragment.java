package com.sk.iwara.ui.User;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sk.iwara.adapter.UserFollowAdapter;
import com.sk.iwara.api.IWARA_API;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentFollowBinding;
import com.sk.iwara.payload.FollowPayload;
import com.sk.iwara.util.HttpUtil;

/**
 * Created by 25140 on 2025/10/20 .
 */
public class UserFollowFragment extends BaseFragment<FragmentFollowBinding> {
    private String userId;
    private UserFollowAdapter adapter;
    private int page=1;
    private boolean isLoading=false;
    @Override
    protected void init() {
        if (getArguments()!=null){
            userId=getArguments().getString("userId");
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {
        if (userId!=null&&!userId.equals("null")){
            binding.followRecycle.setVisibility(View.VISIBLE);
            binding.followNoMatch.setVisibility(View.GONE);

            adapter=new UserFollowAdapter(userId);
            binding.followRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.followRecycle.setAdapter(adapter);
            binding.followRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                    super.onScrolled(rv, dx, dy);
                    if (dy <= 0) return;                 // 向上滑不管

                    GridLayoutManager lm = (GridLayoutManager) rv.getLayoutManager();
                    int totalItemCount = lm.getItemCount();
                    int lastVisible  = lm.findLastVisibleItemPosition();

                    // 还剩 3 个 item 时提前加载，可自己调
                    if (!isLoading && lastVisible >= totalItemCount - 3) {
                        loadMoreData();
                    }
                }
            });



            getData();
        }else{
            binding.followRecycle.setVisibility(View.GONE);
            binding.followNoMatch.setVisibility(View.VISIBLE);
        }
        Log.i("UserFollowFragment",userId);
    }
    private void getData(){

        HttpUtil.get().getAsync(IWARA_API.VIDEO + "/user/"+userId+"/following?limit=15", null, null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                getActivity().runOnUiThread(() -> {
                    adapter.refresh(new Gson().fromJson(respBody, FollowPayload.class).getResults());

                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("UserFollowAdapter",e.getMessage());
            }
        });
    }
    private void loadMoreData(){
        if (isLoading){
            return;
        }
        isLoading=true;
        HttpUtil.get().getAsync(IWARA_API.VIDEO + "/user/"+userId+"/following?limit=15&page="+(++page), null, null, new HttpUtil.NetCallback() {
            @Override
            public void onSuccess(String respBody) {
                getActivity().runOnUiThread(() -> {
                    adapter.addData(new Gson().fromJson(respBody, FollowPayload.class).getResults());
                    isLoading=false;
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("UserFollowAdapter",e.getMessage());
            }
        });
    }
}
