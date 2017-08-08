package com.boredream.weibo;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.boredream.weibo.activity.MainActivity;
import com.boredream.weibo.constants.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class BaseFragment extends Fragment {
	
	protected MainActivity activity;
	protected Oauth2AccessToken accessToken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = (MainActivity) getActivity();
		accessToken = AccessTokenKeeper.readAccessToken(activity);
	}
}
