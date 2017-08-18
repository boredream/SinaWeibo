package com.boredream.weibo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.boredream.bdcodehelper.utils.CollectionUtils;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.view.TitleBarView;
import com.boredream.emotion.EmotionUtils;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.WriteStatusGridImgsAdapter;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.presenter.WriteStatusContract;
import com.boredream.weibo.presenter.WriteStatusPresenter;
import com.boredream.weibo.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class WriteStatusActivity extends BaseActivity implements OnClickListener, OnItemClickListener, WriteStatusContract.View {

	private WriteStatusPresenter presenter;
	// 输入框
	private EditText et_write_status;
	// 添加的九宫格图片
	private GridView gv_write_status;
	// 转发微博内容
	private View include_retweeted_status_card;
	private ImageView iv_rstatus_img;;
	private TextView tv_rstatus_username;;
	private TextView tv_rstatus_content;;
	// 底部添加栏
	private ImageView iv_image;
	private ImageView iv_at;
	private ImageView iv_topic;
	private ImageView iv_emoji;
	private ImageView iv_add;
	// 表情选择面板
	private LinearLayout ll_emotion_dashboard;
	private ViewPager vp_emotion_dashboard;
	// 进度框
	private ProgressDialog progressDialog;

	private WriteStatusGridImgsAdapter statusImgsAdapter;
	private ArrayList<String> imgPaths = new ArrayList<>();

	private Goods retweeted_status;
	private Goods cardStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_write_status);

		retweeted_status = (Goods) getIntent().getSerializableExtra("status");
		
		initView();
	}

	private void initView() {
		presenter = new WriteStatusPresenter(this);
		// 标题栏
		TitleBarView titlebar = (TitleBarView) findViewById(R.id.titlebar);
		titlebar.setTitleText("发微博");
		titlebar.setLeftBack(this);
		titlebar.setRightText("发送");
		titlebar.setRightOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendStatus();
			}
		});
		// 输入框
		et_write_status = (EditText) findViewById(R.id.et_write_status);
		// 添加的九宫格图片
		gv_write_status = (GridView) findViewById(R.id.gv_write_status);
		// 转发微博内容
		include_retweeted_status_card = findViewById(R.id.include_retweeted_status_card);
		iv_rstatus_img = (ImageView) findViewById(R.id.iv_rstatus_img);
		tv_rstatus_username = (TextView) findViewById(R.id.tv_rstatus_username);
		tv_rstatus_content = (TextView) findViewById(R.id.tv_rstatus_content);
		// 底部添加栏
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_at = (ImageView) findViewById(R.id.iv_at);
		iv_topic = (ImageView) findViewById(R.id.iv_topic);
		iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		// 表情选择面板
		ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
		vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
		// 进度框
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("微博发布中...");

		statusImgsAdapter = new WriteStatusGridImgsAdapter(this, imgPaths, gv_write_status);
		gv_write_status.setAdapter(statusImgsAdapter);
		gv_write_status.setOnItemClickListener(this);

		iv_image.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
		iv_add.setOnClickListener(this);

		initRetweetedStatus();
		EmotionUtils.initEmotion(vp_emotion_dashboard, et_write_status, 140, 7, 3,
				DisplayUtils.getScreenWidthPixels(this), DisplayUtils.dp2px(this, 8));
	}
	
	/**
	 * 发送微博
	 */
	private void sendStatus() {
		String statusContent = et_write_status.getText().toString();
		if(TextUtils.isEmpty(statusContent)) {
			showTip("微博内容不能为空");
			return;
		}
		
//		long retweetedStatusId = cardStatus == null ? -1 : cardStatus.getId();
//
		presenter.publishStatus(this, statusContent, imgPaths);
	}

	@Override
	public void publishStatusSuccess(Goods goods) {
		showTip("微博发布成功");
		finish();
	}

	/**
	 * 初始化引用微博内容
	 */
	private void initRetweetedStatus() {
//		if(retweeted_status != null) {
//			Status rrStatus = retweeted_status.getRetweeted_status();
//			if(rrStatus != null) {
//				String content = "//@" + retweeted_status.getUser().getName()
//						+ ":" + retweeted_status.getText();
//				et_write_status.setText(
//						StringUtils.getWeiboContent(this, et_write_status, content));
//				cardStatus = rrStatus;
//			} else {
//				et_write_status.setText("转发微博");
//				cardStatus = retweeted_status;
//			}
//
//			String imgUrl = cardStatus.getThumbnail_pic();
//			if(TextUtils.isEmpty(imgUrl)) {
//				iv_rstatus_img.setVisibility(View.GONE);
//			} else {
//				iv_rstatus_img.setVisibility(View.VISIBLE);
//				Glide.with(this).load(imgUrl).into(iv_rstatus_img);
//			}
//
//			tv_rstatus_username.setText("@" + cardStatus.getUser().getName());
//			tv_rstatus_content.setText(cardStatus.getText());
//
//			iv_image.setVisibility(View.GONE);
//
//			include_retweeted_status_card.setVisibility(View.VISIBLE);
//		} else {
//			include_retweeted_status_card.setVisibility(View.GONE);
//		}
	}

	/**
	 * 更新图片显示
	 */
	private void updateImgs() {
		if(imgPaths.size() > 0) {
			gv_write_status.setVisibility(View.VISIBLE);
			statusImgsAdapter.notifyDataSetChanged();
		} else {
			gv_write_status.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_image:
			ImageUtils.pickImages(this, ImageUtils.REQUEST_CODE_PICKER_IMAGES);
			break;
		case R.id.iv_at:
			break;
		case R.id.iv_topic:
			break;
		case R.id.iv_emoji:
			if(ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
				ll_emotion_dashboard.setVisibility(View.GONE);
				iv_emoji.setImageResource(R.drawable.btn_insert_emotion);
			} else {
				ll_emotion_dashboard.setVisibility(View.VISIBLE);
				iv_emoji.setImageResource(R.drawable.btn_insert_keyboard);
			}
			break;
		case R.id.iv_add:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position == statusImgsAdapter.getCount() - 1) {
			ImageUtils.pickImages(this, ImageUtils.REQUEST_CODE_PICKER_IMAGES);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_PICKER_IMAGES:
			List<BaseMedia> medias = Boxing.getResult(data);
			if(CollectionUtils.isEmpty(medias)) return;
			for (BaseMedia media : medias) {
				imgPaths.add(media.getPath());
				updateImgs();
			}
			break;
		default:
			break;
		}
	}

}
