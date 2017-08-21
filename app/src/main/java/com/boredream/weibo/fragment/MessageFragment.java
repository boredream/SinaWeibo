package com.boredream.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdcodehelper.view.TitleBarView;
import com.boredream.weibo.BaseFragment;
import com.boredream.weibo.R;

public class MessageFragment extends BaseFragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_message, null);

		TitleBarView titlebar = (TitleBarView) view.findViewById(R.id.titlebar);
		titlebar.setTitleText("消息");

		return view;
	}
}
