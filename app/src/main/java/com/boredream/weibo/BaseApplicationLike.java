package com.boredream.weibo;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.boredream.bdcodehelper.constants.AppKeeper;
import com.boredream.bdcodehelper.net.GlideHelper;
import com.boredream.weibo.tinker.Log.MyLogImp;
import com.boredream.weibo.tinker.util.SampleApplicationContext;
import com.boredream.weibo.tinker.util.TinkerManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;


@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.boredream.weibo.BaseApplication",
		flags = ShareConstants.TINKER_ENABLE_ALL,
		loadVerifyFlag = false)
public class BaseApplicationLike extends DefaultApplicationLike {

	public static BaseApplicationLike instance;

	static {
		//设置全局的Header构建器
		SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
				return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
			}
		});
		//设置全局的Footer构建器
		SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
			@Override
			public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
				//指定为经典Footer，默认是 BallPulseFooter
				return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
			}
		});
	}

	public BaseApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
		super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
	}

	/**
	 * install multiDex before install tinker
	 * so we don't need to put the tinker lib classes in the main dex
	 *
	 * @param base
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onBaseContextAttached(Context base) {
		super.onBaseContextAttached(base);
		//you must install multiDex whatever tinker is installed!
		MultiDex.install(base);

		//installTinker after load multiDex
		//or you can put com.tencent.tinker.** to main dex
//		TinkerInstaller.install(this);

		SampleApplicationContext.application = getApplication();
		SampleApplicationContext.context = getApplication();
		TinkerManager.setTinkerApplicationLike(this);

		TinkerManager.initFastCrashProtect();
		//should set before tinker is installed
		TinkerManager.setUpgradeRetryEnable(true);

		//optional set logIml, or you can use default debug log
		TinkerInstaller.setLogIml(new MyLogImp());

		//installTinker after load multiDex
		//or you can put com.tencent.tinker.** to main dex
		TinkerManager.installTinker(this);
		Tinker tinker = Tinker.with(getApplication());
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
		getApplication().registerActivityLifecycleCallbacks(callback);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		AppKeeper.init(getApplication());

		BoxingMediaLoader.getInstance().init(new IBoxingMediaLoader() {
			@Override
			public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
				GlideHelper.loadImg(img, absPath);
			}

			@Override
			public void displayRaw(@NonNull final ImageView img, @NonNull String absPath, int width, int height, final IBoxingCallback callback) {
				Glide.with(getApplication())
						.load(absPath)
						.into(new SimpleTarget<Drawable>() {

							@Override
							public void onLoadFailed(@Nullable Drawable errorDrawable) {
								super.onLoadFailed(errorDrawable);
								callback.onFail(new Exception("load image error"));
							}

							@Override
							public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
								img.setImageDrawable(resource);
								callback.onSuccess();
							}
						});
			}
		});
	}
}
