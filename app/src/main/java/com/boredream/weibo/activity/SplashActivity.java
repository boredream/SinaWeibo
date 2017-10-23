package com.boredream.weibo.activity;

import android.content.Intent;
import android.os.Bundle;

import com.boredream.bdcodehelper.lean.net.LcTokenKeeper;
import com.boredream.bdcodehelper.utils.LogUtils;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.presenter.LoginContract;
import com.boredream.weibo.presenter.LoginPresenter;
import com.boredream.weibo.tinker.LoadTinkerManager;

public class SplashActivity extends BaseActivity implements LoginContract.View {

	private LoginPresenter loginPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		loginPresenter = new LoginPresenter(this);

		// 避免从桌面启动程序后，会重新实例化入口类的activity
		if (!this.isTaskRoot()) {
			Intent intent = getIntent();
			if (intent != null) {
				String action = intent.getAction();
				if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
					finish();
					return;
				}
			}
		}

		LoadTinkerManager.getInstance().checkUpdatePatch();

		autoLogin();
	}

	private void autoLogin() {
		String sessionToken = LcTokenKeeper.getInstance().getSessionToken();
		loginPresenter.autoLogin(sessionToken);
	}

	@Override
	public void loginSuccess(User user) {
		LogUtils.showLog("loginSuccess");
		intent2Activity(MainActivity.class);
		finish();
	}

	@Override
	public void loginError() {
		LogUtils.showLog("loginError");
		intent2Activity(LoginActivity.class);
		finish();
	}

}
