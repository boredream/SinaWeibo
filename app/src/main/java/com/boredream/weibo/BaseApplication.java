package com.boredream.weibo;

import android.app.Application;

import com.boredream.weibo.entity.User;

public class BaseApplication extends Application {

	public static User currentUser;
	public static BaseApplication app;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
	}
}
