package com.boredream.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.boredream.bdcodehelper.lean.LcUtils;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.view.TitleBarView;
import com.boredream.emotion.EmotionUtils;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.constants.UserInfoKeeper;
import com.boredream.weibo.entity.Comment;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.net.RxComposer;
import com.boredream.weibo.net.WbHttpRequest;

import java.util.HashMap;
import java.util.Map;

public class WriteCommentActivity extends BaseActivity implements OnClickListener {
	// 评论输入框
	private EditText et_write_status;
	// 底部按钮
	private ImageView iv_image;
	private ImageView iv_at;
	private ImageView iv_topic;
	private ImageView iv_emoji;
	private View ll_emotion_dashboard;
	private ViewPager vp_emotion_dashboard;
	private ImageView iv_add;
	// 待评论的微博
	private Goods status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_write_status);

		// 获取Intent传入的微博
		status = (Goods) getIntent().getSerializableExtra("status");
		
		initView();

	}

	private void initView() {
		TitleBarView titlebar = (TitleBarView) findViewById(R.id.titlebar);
		titlebar.setTitleText("发评论");
		titlebar.setLeftBack(this);
		titlebar.setRightText("发送");
		titlebar.setRightOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendComment();
			}
		});

		et_write_status = (EditText) findViewById(R.id.et_write_status);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_at = (ImageView) findViewById(R.id.iv_at);
		iv_topic = (ImageView) findViewById(R.id.iv_topic);
		iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
		ll_emotion_dashboard = findViewById(R.id.ll_emotion_dashboard);
		vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
		iv_add = (ImageView) findViewById(R.id.iv_add);

		iv_image.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
		iv_add.setOnClickListener(this);

		EmotionUtils.initEmotion(vp_emotion_dashboard, et_write_status, 50, 7, 3,
				DisplayUtils.getScreenWidthPixels(this), DisplayUtils.dp2px(this, 8));
	}

	private void sendComment() {
		String comment = et_write_status.getText().toString();
		if(TextUtils.isEmpty(comment)) {
			showTip("评论内容不能为空");
			return;
		}

		final Map<String, Object> request = new HashMap<>();
		request.put("text", comment);
		request.put("user", LcUtils.getPointer(UserInfoKeeper.getInstance().getCurrentUser()));
		request.put("status", LcUtils.getPointer(status));

		WbHttpRequest.getInstance()
				.getApiService()
				.commentsPublish(request)
				.compose(RxComposer.<Comment>commonProgress(this))
				.subscribe(new SimpleDisObserver<Comment>() {
					@Override
					public void onNext(Comment response) {
						showTip("微博发送成功");

						// 微博发送成功后,设置Result结果数据,然后关闭本页面
						Intent data = new Intent();
						data.putExtra("sendCommentSuccess", true);
						setResult(RESULT_OK, data);

						finish();
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_image:
			showTip("coming soon~");
			break;
		case R.id.iv_at:
			showTip("coming soon~");
			break;
		case R.id.iv_topic:
			showTip("coming soon~");
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
			showTip("coming soon~");
			break;
		}
	}

}
