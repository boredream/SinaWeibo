package com.boredream.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.weibo.BaseFragment;
import com.boredream.weibo.R;
import com.boredream.weibo.entity.Goods;

public class StatusTransmitListFragment extends BaseFragment {

    private View view;
    private Goods status;

    public static StatusTransmitListFragment newInstance(Goods status) {
        StatusTransmitListFragment fragment = new StatusTransmitListFragment();
        Bundle args = new Bundle();
        args.putSerializable("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_status_retweetlist, null);
        initExtras();
        initView(view);
        initData();
        return view;
    }

    private void initExtras() {
        status = (Goods) getArguments().getSerializable("status");
    }

    private void initView(View view) {
    }

    private void initData() {

    }

}
