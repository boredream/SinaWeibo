package com.boredream.weibo.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.view.TitleBarView;
import com.boredream.weibo.BaseFragment;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.StatusAdapter;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.net.RxComposer;
import com.boredream.weibo.net.WbHttpRequest;
import com.boredream.weibo.packerng.PackerNg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		view = View.inflate(getActivity(), R.layout.frag_home, null);

		TitleBarView titlebar = (TitleBarView) view.findViewById(R.id.titlebar);
		titlebar.setTitleText("首页 channel:" + PackerNg.getMarket(getActivity()));

		refresh = (SmartRefreshLayout) view.findViewById(R.id.refresh);
		lv_home = (ListView) view.findViewById(R.id.lv_home);
		adapter = new StatusAdapter(getActivity(), statuses);
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
		Map<String, Object> request = new HashMap<>();
		request.put("page", page);
		request.put("include", "user");

		WbHttpRequest.getInstance()
				.getApiService()
				.statusesHome(request)
				.compose(LcRxCompose.<Goods>handleListResponse())
				.compose(RxComposer.<ArrayList<Goods>>commonRefresh(this, page>1, refresh))
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
	}
}
