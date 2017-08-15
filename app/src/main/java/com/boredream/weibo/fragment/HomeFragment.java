package com.boredream.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.weibo.BaseFragment;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.StatusAdapter;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.net.RxComposer;
import com.boredream.weibo.net.WbHttpRequest;
import com.boredream.weibo.utils.TitleBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class HomeFragment extends BaseFragment {

	private View view;

	private SmartRefreshLayout refresh;
	private ListView lv_home;

	private StatusAdapter adapter;
	private List<Goods> statuses = new ArrayList<>();
	private int curPage = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initView();
		loadData(1);
		return view;
	}

	private void initView() {
		view = View.inflate(activity, R.layout.frag_home, null);

		new TitleBuilder(view).setTitleText("首页");

		refresh = (SmartRefreshLayout) view.findViewById(R.id.refresh);
		lv_home = (ListView) view.findViewById(R.id.lv_home);
		adapter = new StatusAdapter(activity, statuses);
		lv_home.setAdapter(adapter);
		refresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				loadData(1);
			}
		});
		refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				loadData(curPage + 1);
			}
		});
	}

	public void loadData(final int page) {
		WbHttpRequest.getInstance()
				.getApiService()
				.statusesHome(page)
				.compose(LcRxCompose.<Goods>handleListResponse())
				.compose(RxComposer.<ArrayList<Goods>>commonRefresh(activity, page>1, refresh))
				.subscribe(new SimpleDisObserver<ArrayList<Goods>>() {

					@Override
					public void onNext(@NonNull ArrayList<Goods> response) {
						if(page == 1) {
							statuses.clear();
						}
						curPage = page;

						addData(response);
					}
				});
	}
	
	private void addData(ArrayList<Goods> response) {
		for(Goods status : response) {
			if(!statuses.contains(status)) {
				statuses.add(status);
			}
		}
		adapter.notifyDataSetChanged();

		// FIXME: 2017/8/15
//		if(curPage < resBean.getTotal_number()) {
//			addFootView(footView);
//		} else {
//			removeFootView(footView);
//		}
	}
}
