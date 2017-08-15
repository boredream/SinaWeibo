package com.boredream.weibo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.boredream.bdcodehelper.base.BoreBaseActivity;

public abstract class BaseActivity extends BoreBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

}
