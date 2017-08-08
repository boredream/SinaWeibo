package com.boredream.weibo;

import android.app.Application;

import com.boredream.weibo.entity.User;

public class BaseApplication extends Application {

	public static User currentUser;

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
