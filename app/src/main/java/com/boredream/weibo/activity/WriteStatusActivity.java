package com.boredream.weibo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.EmotionGvAdapter;
import com.boredream.weibo.adapter.EmotionPagerAdapter;
import com.boredream.weibo.adapter.WriteStatusGridImgsAdapter;
import com.boredream.weibo.entity.Status;
import com.boredream.weibo.net.HttpRequest;
import com.boredream.weibo.net.RxComposer;
import com.boredream.weibo.utils.EmotionUtils;
import com.boredream.weibo.utils.ImageUtils;
import com.boredream.weibo.utils.StringUtils;
import com.boredream.weibo.utils.TitleBuilder;
import com.bumptech.glide.Glide;
import com.sina.weibo.sdk.web.WeiboPageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteStatusActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

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
	private ArrayList<Uri> imgUris = new ArrayList<Uri>();
	private EmotionPagerAdapter emotionPagerGvAdapter;
	
	private Status retweeted_status;
	private Status cardStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_write_status);

		retweeted_status = (Status) getIntent().getSerializableExtra("status");
		
		initView();
	}

	private void initView() {
		// 标题栏
		new TitleBuilder(this)
				.setTitleText("发微博")
				.setLeftText("取消")
				.setLeftOnClickListener(this)
				.setRightText("发送")
				.setRightOnClickListener(this)
				.build();
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

		statusImgsAdapter = new WriteStatusGridImgsAdapter(this, imgUris, gv_write_status);
		gv_write_status.setAdapter(statusImgsAdapter);
		gv_write_status.setOnItemClickListener(this);

		iv_image.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
		iv_add.setOnClickListener(this);

		initRetweetedStatus();
		initEmotion();
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
		
		String imgFilePath = null;
		if(imgUris.size() > 0) {
			Uri uri = imgUris.get(0);
			imgFilePath = ImageUtils.getImageAbsolutePath19(this, uri);
		}
		
		long retweetedStatusId = cardStatus == null ? -1 : cardStatus.getId();

		Map<String, String> request = new HashMap<>();
		request.put("access_token", accessToken.getToken());
		request.put("status", statusContent);
		request.put("pic", imgFilePath);
		request.put("id", String.valueOf(retweetedStatusId));
		HttpRequest.getSingleton()
				.getApiService()
				.statusesRepost(request)
				.compose(RxComposer.<Status>common(this))
				.subscribe(new SimpleDisObserver<Status>() {
					@Override
					public void onNext(Status status) {
						showTip("微博发送成功");
						finish();
					}
				});
	}

	/**
	 * 初始化引用微博内容
	 */
	private void initRetweetedStatus() {
		if(retweeted_status != null) {
			Status rrStatus = retweeted_status.getRetweeted_status();
			if(rrStatus != null) {
				String content = "//@" + retweeted_status.getUser().getName()
						+ ":" + retweeted_status.getText();
				et_write_status.setText(
						StringUtils.getWeiboContent(this, et_write_status, content));
				cardStatus = rrStatus;
			} else {
				et_write_status.setText("转发微博");
				cardStatus = retweeted_status;
			}
			
			String imgUrl = cardStatus.getThumbnail_pic();
			if(TextUtils.isEmpty(imgUrl)) {
				iv_rstatus_img.setVisibility(View.GONE);
			} else {
				iv_rstatus_img.setVisibility(View.VISIBLE);
				Glide.with(this).load(imgUrl).into(iv_rstatus_img);
			}
			
			tv_rstatus_username.setText("@" + cardStatus.getUser().getName());
			tv_rstatus_content.setText(cardStatus.getText());
			
			iv_image.setVisibility(View.GONE);
			
			include_retweeted_status_card.setVisibility(View.VISIBLE);
		} else {
			include_retweeted_status_card.setVisibility(View.GONE);
		}
	}

	/**
	 *  初始化表情面板内容
	 */
	private void initEmotion() {
		int screenWidth = DisplayUtils.getScreenWidthPixels(this);
		int spacing = DisplayUtils.dp2px(this, 8);
		
		int itemWidth = (screenWidth - spacing * 8) / 7;
		int gvHeight = itemWidth * 3 + spacing * 4;
		
		List<GridView> gvs = new ArrayList<GridView>();
		List<String> emotionNames = new ArrayList<String>();
		for(String emojiName : EmotionUtils.emojiMap.keySet()) {
			emotionNames.add(emojiName);
			
			if(emotionNames.size() == 20) {
				GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
				gvs.add(gv);
				
				emotionNames = new ArrayList<String>();
			}
		}
		
		if(emotionNames.size() > 0) {
			GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
			gvs.add(gv);
		}
		
		emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
		vp_emotion_dashboard.setLayoutParams(params);
		vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);
	}

	/**
	 * 创建显示表情的GridView
	 */
	private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
		GridView gv = new GridView(this);
		gv.setBackgroundResource(R.color.bg_gray);
		gv.setSelector(R.color.transparent);
		gv.setNumColumns(7);
		gv.setPadding(padding, padding, padding, padding);
		gv.setHorizontalSpacing(padding);
		gv.setVerticalSpacing(padding);
		
		LayoutParams params = new LayoutParams(gvWidth, gvHeight);
		gv.setLayoutParams(params);
		
		EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(this);
		
		return gv;
	}

	/**
	 * 更新图片显示
	 */
	private void updateImgs() {
		if(imgUris.size() > 0) {
			gv_write_status.setVisibility(View.VISIBLE);
			statusImgsAdapter.notifyDataSetChanged();
		} else {
			gv_write_status.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_tv_left:
			finish();
			break;
		case R.id.titlebar_tv_right:
			sendStatus();
			break;
		case R.id.iv_image:
			ImageUtils.showImagePickDialog(this);
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
		Object itemAdapter = parent.getAdapter();
		if(itemAdapter instanceof WriteStatusGridImgsAdapter) {
			if(position == statusImgsAdapter.getCount() - 1) {
				ImageUtils.showImagePickDialog(this);
			}
		} else if(itemAdapter instanceof EmotionGvAdapter) {
			EmotionGvAdapter emotionAdapter = (EmotionGvAdapter) itemAdapter;
			
			if(position == emotionAdapter.getCount() - 1) {
				et_write_status.dispatchKeyEvent(
						new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			} else {
				String emotionName = emotionAdapter.getItem(position);
				
				int curPosition = et_write_status.getSelectionStart();
				StringBuilder sb = new StringBuilder(et_write_status.getText().toString());
				sb.insert(curPosition, emotionName);
				
				SpannableString weiboContent = StringUtils.getWeiboContent(
						this, et_write_status, sb.toString());
				et_write_status.setText(weiboContent);
				
				et_write_status.setSelection(curPosition + emotionName.length());
			}
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_FROM_ALBUM:
			if(resultCode == RESULT_CANCELED) {
				return;
			}
			Uri imageUri = data.getData();
			
			imgUris.add(imageUri);
			updateImgs();
			break;
		case ImageUtils.REQUEST_CODE_FROM_CAMERA:
			if(resultCode == RESULT_CANCELED) {
				ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
			} else {
				Uri imageUriCamera = ImageUtils.imageUriFromCamera;
				
				imgUris.add(imageUriCamera);
				updateImgs();
			}
			break;

		default:
			break;
		}
	}

}
