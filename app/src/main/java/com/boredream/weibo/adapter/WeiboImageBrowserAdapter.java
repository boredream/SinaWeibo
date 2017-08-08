package com.boredream.weibo.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.boredream.bdcodehelper.adapter.ImageBrowserAdapter;
import com.boredream.weibo.entity.PicUrls;

import java.util.List;

public class WeiboImageBrowserAdapter extends ImageBrowserAdapter<PicUrls> {

	public WeiboImageBrowserAdapter(Activity context, List<PicUrls> picUrls) {
		super(context, picUrls);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public PicUrls getPic(int position) {
		int index = position % imageUrls.size();
		return imageUrls.get(index);
	}

	public Bitmap getBitmap(int position) {
		ImageView imageView = getImageView(position);
		if(imageView == null) return null;
		Bitmap bitmap = null;
		Drawable drawable = imageView.getDrawable();
		if(drawable != null && drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			bitmap = bd.getBitmap();
		}
		return bitmap;
	}
}