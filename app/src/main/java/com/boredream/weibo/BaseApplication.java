package com.boredream.weibo;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.boredream.bdcodehelper.base.AppKeeper;
import com.boredream.bdcodehelper.net.GlideHelper;
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

public class BaseApplication extends Application {

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

	@Override
	public void onCreate() {
		super.onCreate();

		AppKeeper.init(this);

		BoxingMediaLoader.getInstance().init(new IBoxingMediaLoader() {
			@Override
			public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
				GlideHelper.loadImg(img, absPath);
			}

			@Override
			public void displayRaw(@NonNull final ImageView img, @NonNull String absPath, int width, int height, final IBoxingCallback callback) {
				Glide.with(getApplicationContext())
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
