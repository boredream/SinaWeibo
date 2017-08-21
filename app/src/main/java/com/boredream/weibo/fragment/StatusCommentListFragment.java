package com.boredream.weibo.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.net.CommonRxComposer;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.weibo.BaseFragment;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.StatusCommentRvAdapter;
import com.boredream.weibo.entity.Comment;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.net.WbHttpRequest;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class StatusCommentListFragment extends BaseFragment {

    private View view;
    private RecyclerView rv;
    private Goods status;
    private StatusCommentRvAdapter adapter;
    private List<Comment> comments = new ArrayList<>();

    public static StatusCommentListFragment newInstance(Goods status) {
        StatusCommentListFragment fragment = new StatusCommentListFragment();
        Bundle args = new Bundle();
        args.putSerializable("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_status_commentlist, null);
        initExtras();
        initView(view);
        initData();
        return view;
    }

    private void initExtras() {
        status = (Goods) getArguments().getSerializable("status");
    }

    private void initView(View view) {
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new StatusCommentRvAdapter(getActivity(), comments);
        rv.setAdapter(adapter);
    }

    private void initData() {
        // TODO: 2017/8/18 所屬
        WbHttpRequest.getInstance()
                .getApiService()
                .commentsShow(1)
                .compose(LcRxCompose.<Comment>handleListResponse())
                .compose(CommonRxComposer.<ArrayList<Comment>>schedulers())
                .compose(CommonRxComposer.<ArrayList<Comment>>lifecycle(this))
                .compose(LcRxCompose.<ArrayList<Comment>>defaultFailed(this))
                .subscribe(new SimpleDisObserver<ArrayList<Comment>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<Comment> comments) {
                        setResponse(comments);
                    }
                });
    }

    private void setResponse(ArrayList<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);

        adapter.notifyDataSetChanged();
    }
}
