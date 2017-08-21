package com.boredream.weibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.view.TitleBarView;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.StatusDetailPagerAdapter;
import com.boredream.weibo.adapter.StatusGridImgsAdapter;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class StatusDetailActivity extends BaseActivity implements OnClickListener {

	// 跳转到写评论页面code
	private static final int REQUEST_CODE_WRITE_COMMENT = 2333;

	// header - 微博信息
	private ImageView iv_avatar;
	private TextView tv_subhead;
	private TextView tv_caption;
	private FrameLayout include_status_image;
	private GridView gv_images;
	private ImageView iv_image;
	private TextView tv_content;
	private View include_retweeted_status;
	private TextView tv_retweeted_content;
	private FrameLayout fl_retweeted_imageview;
	private GridView gv_retweeted_images;
	private ImageView iv_retweeted_image;
	// tab list
	private TabLayout tab;
	private ViewPager vp;
	private SmartRefreshLayout refresh;
	// bottom_control - 底部互动栏,包括转发/评论/点赞
	private LinearLayout ll_bottom_control;
	private LinearLayout ll_share_bottom;
	private TextView tv_share_bottom;
	private LinearLayout ll_comment_bottom;
	private TextView tv_comment_bottom;
	private LinearLayout ll_like_bottom;
	private TextView tv_like_bottom;

	// 详情页的微博信息
	private Goods status;
	// 是否需要滚动至评论部分
	private boolean scroll2Comment;
	// 评论当前已加载至的页数
	private int curPage = 1;
	
	public static void start(Context context, Goods status) {
		Intent intent = new Intent(context, StatusDetailActivity.class);
		intent.putExtra("status", status);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_status_detail);

		// 获取intent传入的信息
		status = (Goods) getIntent().getSerializableExtra("status");
		scroll2Comment = getIntent().getBooleanExtra("scroll2Comment", false);

		// 初始化View
		initView();

		// 设置数据信息
		setData();
	}

	private void initView() {
		// title - 标题栏
		initTitle();
		// headerView - 微博信息
		initDetailHead();
		// tab list
		initTab();
		// 下拉刷新控件
		initRefreshList();
		// bottom_control - 底部互动栏,包括转发/评论/点赞
		initControlBar();
	}

	private void initTitle() {
		TitleBarView titlebar = (TitleBarView) findViewById(R.id.titlebar);
		titlebar.setTitleText("微博正文");
		titlebar.setLeftBack(this);
	}

	private void initDetailHead() {
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_subhead = (TextView) findViewById(R.id.tv_subhead);
		tv_caption = (TextView) findViewById(R.id.tv_caption);
		include_status_image = (FrameLayout) findViewById(R.id.include_status_image);
		gv_images = (GridView) findViewById(R.id.gv_images);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		tv_content = (TextView) findViewById(R.id.tv_content);
		include_retweeted_status = findViewById(R.id.include_retweeted_status);
		tv_retweeted_content = (TextView) findViewById(R.id.tv_retweeted_content);
		fl_retweeted_imageview = (FrameLayout) include_retweeted_status.findViewById(R.id.include_status_image);
		gv_retweeted_images = (GridView) fl_retweeted_imageview.findViewById(R.id.gv_images);
		iv_retweeted_image = (ImageView) fl_retweeted_imageview.findViewById(R.id.iv_image);
		iv_image.setOnClickListener(this);
	}

	private void initTab() {
		String[] titles = {"转发", "评论", "赞"};
		tab = (TabLayout) findViewById(R.id.tab);

		vp = (ViewPager) findViewById(R.id.vp);
		vp.setAdapter(new StatusDetailPagerAdapter(getSupportFragmentManager(), titles, status));

		tab.setupWithViewPager(vp);
		vp.setCurrentItem(1);
	}

	private void initRefreshList() {
		// 下拉刷新控件和列表
		refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
		// 下拉刷新监听
		refresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				// TODO: 2017/8/21
//				loadComments(1);
			}
		});
		// 滑动到底部最后一个item监听
		refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				// TODO: 2017/8/21
//				loadComments(curPage + 1);
			}
		});
	}

	private void initControlBar() {
		ll_bottom_control = (LinearLayout) findViewById(R.id.status_detail_controlbar);
		ll_share_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_share_bottom);
		tv_share_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_share_bottom);
		ll_comment_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_comment_bottom);
		tv_comment_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_comment_bottom);
		ll_like_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_like_bottom);
		tv_like_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_like_bottom);
		ll_bottom_control.setBackgroundResource(R.color.white);
		ll_share_bottom.setOnClickListener(this);
		ll_comment_bottom.setOnClickListener(this);
		ll_like_bottom.setOnClickListener(this);
	}

	private void setData() {
		// listView headerView - 微博信息
		User user = status.getUser();
		if(user != null) {
			Glide.with(this).load(user.getAvatarUrl()).into(iv_avatar);
			tv_subhead.setText(user.getNickname());
		}

		// FIXME: 2017/8/15
//		tv_caption.setText(DateUtils.getShortTime(status.getCreated_at()) +
//				"  来自" + Html.fromHtml(status.getSource()));

		setImages(status, include_status_image, gv_images, iv_image);

		if (TextUtils.isEmpty(status.getName())) {
			tv_content.setVisibility(View.GONE);
		} else {
			tv_content.setVisibility(View.VISIBLE);
			SpannableString weiboContent = StringUtils.getWeiboContent(
					this, tv_content, status.getName());
			tv_content.setText(weiboContent);
		}

		// FIXME: 2017/8/15 转发
//		Goods retweetedStatus = status.getRetweeted_status();
//		if (retweetedStatus != null) {
//			include_retweeted_status.setVisibility(View.VISIBLE);
//			String retweetContent = "@" + retweetedStatus.getUser().getName()
//					+ ":" + retweetedStatus.getText();
//			SpannableString weiboContent = StringUtils.getWeiboContent(
//					this, tv_retweeted_content, retweetContent);
//			tv_retweeted_content.setText(weiboContent);
//			setImages(retweetedStatus, fl_retweeted_imageview,
//					gv_retweeted_images, iv_retweeted_image);
//		} else {
//			include_retweeted_status.setVisibility(View.GONE);
//		}
		
		// bottom_control - 底部互动栏,包括转发/评论/点赞
		tv_share_bottom.setText("转发");
		tv_comment_bottom.setText("评论");
		tv_like_bottom.setText("赞");
	}

	private void setImages(final Goods status, ViewGroup vgContainer, GridView gvImgs, final ImageView ivImg) {
		if (status == null) {
			return;
		}

		ArrayList<String> picUrls = status.getImages();
		String picUrl = status.getImage();

		if (picUrls != null && picUrls.size() == 1) {
			vgContainer.setVisibility(View.VISIBLE);
			gvImgs.setVisibility(View.GONE);
			ivImg.setVisibility(View.VISIBLE);

			Glide.with(this).load(picUrl).into(ivImg);
			ivImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(StatusDetailActivity.this, WeiboImageBrowserActivity.class);
					intent.putExtra("status", status);
					startActivity(intent);
				}
			});
		} else if (picUrls != null && picUrls.size() > 1) {
			vgContainer.setVisibility(View.VISIBLE);
			gvImgs.setVisibility(View.VISIBLE);
			ivImg.setVisibility(View.GONE);

			StatusGridImgsAdapter imagesAdapter = new StatusGridImgsAdapter(this, picUrls);
			gvImgs.setAdapter(imagesAdapter);
			gvImgs.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(StatusDetailActivity.this, WeiboImageBrowserActivity.class);
					intent.putExtra("status", status);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
		} else {
			vgContainer.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_image:
			break;
		case R.id.ll_share_bottom:
			Intent intentWriteStatus = new Intent(this, WriteStatusActivity.class);
			intentWriteStatus.putExtra("status", status);
			startActivity(intentWriteStatus);
			break;
		case R.id.ll_comment_bottom:
			// 跳转至写评论页面
			Intent intent = new Intent(this, WriteCommentActivity.class);
			intent.putExtra("status", status);
			startActivityForResult(intent, REQUEST_CODE_WRITE_COMMENT);
			break;
		case R.id.ll_like_bottom:
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// 如果Back键返回,取消发评论等情况,则直接return,不做后续处理
		if(resultCode == RESULT_CANCELED) {
			return;
		}
		
		switch (requestCode) {
		case REQUEST_CODE_WRITE_COMMENT:
			// 如果是评论发送成功的返回结果,则重新加载最新评论,同时要求滚动至评论部分
			boolean sendCommentSuccess = data.getBooleanExtra("sendCommentSuccess", false);
			if(sendCommentSuccess) {
				scroll2Comment = true;
				// TODO: 2017/8/21
//				loadComments(1);
			}
			break;

		default:
			break;
		}
	}

}
