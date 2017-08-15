package com.boredream.weibo;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.boredream.weibo.activity.MainActivity;

public class BaseFragment extends Fragment {
	
	protected MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = (MainActivity) getActivity();
	}

}
