package com.boredream.weibo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.boredream.bdcodehelper.adapter.ImageBrowserAdapter;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.entity.Goods;

import java.util.ArrayList;

public class WeiboImageBrowserActivity extends BaseActivity implements OnClickListener {
	private ViewPager vp_image_brower;
	private TextView tv_image_index;
	private Button btn_save;
	private Button btn_original_image;

	private Goods status;
	private int position;
	private ImageBrowserAdapter adapter;
	private ArrayList<String> imgUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_brower);
		
		initData();
		initView();
		setData();
	}

	private void initData() {
		status = (Goods) getIntent().getSerializableExtra("status");
		position = getIntent().getIntExtra("position", 0);
		// 获取图片数据集合(单图也有对应的集合,集合的size为1)
		imgUrls = status.getImages();
	}

	private void initView() {
		vp_image_brower = (ViewPager) findViewById(R.id.vp_image_brower);
		tv_image_index = (TextView) findViewById(R.id.tv_image_index);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_original_image = (Button) findViewById(R.id.btn_original_image);

		btn_save.setOnClickListener(this);
		btn_original_image.setOnClickListener(this);
	}
	
	private void setData() {
		adapter = new ImageBrowserAdapter(this);
		adapter.setImageStrs(imgUrls);
		vp_image_brower.setAdapter(adapter);
		
		final int size = imgUrls.size();
		int initPosition = Integer.MAX_VALUE / 2 / size * size + position;
		
		if(size > 1) {
			tv_image_index.setVisibility(View.VISIBLE);
			tv_image_index.setText((position+1) + "/" + size);
		} else {
			tv_image_index.setVisibility(View.GONE);
		}

		vp_image_brower.setCurrentItem(initPosition);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			Bitmap bitmap = adapter.getBitmap(vp_image_brower.getCurrentItem());
			
			String title = "image-" + System.currentTimeMillis();
			String insertImage = MediaStore.Images.Media.insertImage(
					getContentResolver(), bitmap, title, "BoreWBImage");
			if(insertImage == null) {
				showTip("图片保存失败");
			} else {
				showTip("图片保存成功");
			}
			break;
		case R.id.btn_original_image:
			// TODO: 2017/8/15
			break;
		}
	}
}
