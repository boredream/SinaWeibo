package com.boredream.weibo.utils;

import android.app.Activity;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.boredream.weibo.R;

public class ImageUtils {

	public static final int REQUEST_CODE_PICKER_IMAGES = 110;

	public static void pickImages(Activity activity, int REQUEST_CODE) {
		// 启动缩略图界面, 依赖boxing-impl.
		BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG)
				.needCamera(R.drawable.ic_photo_camera_white)
				.withMaxCount(9)
				.withMediaPlaceHolderRes(R.drawable.default_image);
		Boxing.of(config)
				.withIntent(activity, BoxingActivity.class)
				.start(activity, REQUEST_CODE);
	}

}
