package com.boredream.weibo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.boredream.bdcodehelper.base.BoreBaseActivity;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public abstract class BaseActivity extends BoreBaseActivity {

	protected Oauth2AccessToken accessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		accessToken = AccessTokenKeeper.readAccessToken(this);
	}

}
