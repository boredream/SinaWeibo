package com.boredream.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.weibo.BaseFragment;
import com.boredream.weibo.R;

public class SearchFragment extends BaseFragment {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(activity, R.layout.frag_search, null);
		return view;
	}
	
}
