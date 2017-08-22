package com.boredream.weibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.net.CommonRxComposer;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.view.TitleBarView;
import com.boredream.weibo.BaseActivity;
import com.boredream.weibo.R;
import com.boredream.weibo.adapter.StatusDetailAdapter;
import com.boredream.weibo.entity.Comment;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.net.RxComposer;
import com.boredream.weibo.net.WbHttpRequest;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;

public class StatusDetail2Activity extends BaseActivity implements OnClickListener {

	// 跳转到写评论页面code
	private static final int REQUEST_CODE_WRITE_COMMENT = 2333;

	// tab list
	private TabLayout tab;
	private TabLayout tab_mock;
	private SmartRefreshLayout refresh;
	private RecyclerView rv;
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
	private StatusDetailAdapter adapter;
	private List<Comment> comments = new ArrayList<>();

	public static void start(Context context, Goods status) {
		Intent intent = new Intent(context, StatusDetail2Activity.class);
		intent.putExtra("status", status);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_status_detail2);

		// 获取intent传入的信息
		status = (Goods) getIntent().getSerializableExtra("status");
		scroll2Comment = getIntent().getBooleanExtra("scroll2Comment", false);

		// 初始化View
		initView();
		loadComments(1);
	}

	private void initView() {
		// title - 标题栏
		initTitle();
		// tab list
		initTabList();
		// bottom_control - 底部互动栏,包括转发/评论/点赞
		initControlBar();
	}

	private void initTitle() {
		TitleBarView titlebar = (TitleBarView) findViewById(R.id.titlebar);
		titlebar.setTitleText("微博正文");
		titlebar.setLeftBack(this);
	}

	private void initTabList() {
		tab_mock = (TabLayout) findViewById(R.id.tab_mock);
		tab_mock.setVisibility(View.GONE);

		rv = (RecyclerView) findViewById(R.id.rv);
		rv.setLayoutManager(new LinearLayoutManager(this));
		adapter = new StatusDetailAdapter(this, status, comments);
		rv.setAdapter(adapter);

		// 下拉刷新控件和列表
		refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
		// 下拉刷新监听
		refresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				loadComments(1);
			}
		});
		// 滑动到底部最后一个item监听
		refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				loadComments(curPage + 1);
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

	private void loadComments(final int page) {
		// TODO: 2017/8/18 所屬
		Map<String, Object> request = new HashMap<>();
		request.put("page", page);
		request.put("include", "user");

		WbHttpRequest.getInstance()
				.getApiService()
				.commentsShow(request)
				.compose(LcRxCompose.<Comment>handleListResponse())
				.compose(RxComposer.<ArrayList<Comment>>commonRefresh(this, page>1, refresh))
				.subscribe(new SimpleDisObserver<ArrayList<Comment>>() {
					@Override
					public void onNext(@NonNull ArrayList<Comment> response) {
						if(page == 1) {
							comments.clear();
						}
						comments.addAll(response);
						adapter.notifyDataSetChanged();
					}
				});
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
				loadComments(1);
			}
			break;

		default:
			break;
		}
	}

}
