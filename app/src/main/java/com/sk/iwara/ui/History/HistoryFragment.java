package com.sk.iwara.ui.History;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.sk.iwara.adapter.HistoryAdapter;
import com.sk.iwara.base.BaseFragment;
import com.sk.iwara.databinding.FragmentHistoryBinding;

/**
 * Created by 25140 on 2025/10/15 .
 */
public class HistoryFragment extends BaseFragment<FragmentHistoryBinding> {
    private HistoryAdapter adapter;
    @Override
    protected void init() {
        adapter=new HistoryAdapter(getContext());
        adapter.initData();
        binding.historyRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.historyRecycle.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }
}
