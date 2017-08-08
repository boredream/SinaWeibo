package com.boredream.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.weibo.BaseFragment;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.StatusAdapter;
import com.boredream.weibo.api.WeiboApi;
import com.boredream.weibo.entity.Status;
import com.boredream.weibo.entity.response.StatusListResponse;
import com.boredream.weibo.utils.TitleBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

	private View view;

	private SmartRefreshLayout refresh;
	private ListView lv_home;
	private View footView;
	
	private StatusAdapter adapter;
	private List<Status> statuses = new ArrayList<>();
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
		
		footView = View.inflate(activity, R.layout.footview_loading, null);
	}

	public void loadData(final int page) {
		HttpRequest.getSingleton()
				.getApiService(WeiboApi.class)
				.statusesHome_timeline(accessToken.getToken(), page)
				.subscribe(new DefaultDisposableObserver<StatusListResponse>(activity) {
					@Override
					public void onNext(StatusListResponse response) {
						super.onNext(response);

						if(page == 1) {
							statuses.clear();
						}
						curPage = page;

						addData(response);
					}

					@Override
					public void onError(Throwable e) {
						super.onError(e);

						// TODO: 2017/8/8  
//						lv_home.onRefreshComplete();
					}
				});
	}
	
	private void addData(StatusListResponse resBean) {
		for(Status status : resBean.getStatuses()) {
			if(!statuses.contains(status)) {
				statuses.add(status);
			}
		}
		adapter.notifyDataSetChanged();
		
		if(curPage < resBean.getTotal_number()) {
			addFootView(footView);
		} else {
			removeFootView(footView);
		}
	}
	
	private void addFootView(View footView) {
		if(lv_home.getFooterViewsCount() == 1) {
			lv_home.addFooterView(footView);
		}
	}
	
	private void removeFootView(View footView) {
		if(lv_home.getFooterViewsCount() > 1) {
			lv_home.removeFooterView(footView);
		}
	}
}
