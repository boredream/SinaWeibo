package com.boredream.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.bdcodehelper.utils.ToastUtils;
import com.boredream.weibo.R;
import com.boredream.weibo.activity.UserInfoActivity;
import com.boredream.weibo.entity.Comment;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.utils.DateUtils;
import com.boredream.weibo.utils.StringUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class StatusCommentAdapter extends BaseAdapter {
	
	private Context context;
	private List<Comment> comments;

	public StatusCommentAdapter(Context context, List<Comment> comments) {
		this.context = context;
		this.comments = comments;
	}
	
	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Comment getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderList holder;
		if (convertView == null) {
			holder = new ViewHolderList();
			convertView = View.inflate(context, R.layout.item_comment, null);
			holder.ll_comments = (LinearLayout) convertView
					.findViewById(R.id.ll_comments);
			holder.iv_avatar = (ImageView) convertView
					.findViewById(R.id.iv_avatar);
			holder.tv_subhead = (TextView) convertView
					.findViewById(R.id.tv_subhead);
			holder.tv_caption = (TextView) convertView
					.findViewById(R.id.tv_caption);
			holder.tv_comment = (TextView) convertView
					.findViewById(R.id.tv_comment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderList) convertView.getTag();
		}

		Comment comment = getItem(position);
		final User user = comment.getUser();

		Glide.with(context).load(user.getProfile_image_url()).into(holder.iv_avatar);
		holder.tv_subhead.setText(user.getName());
		holder.tv_caption.setText(DateUtils.getShortTime(comment.getCreated_at()));
		SpannableString weiboContent = StringUtils.getWeiboContent(
				context, holder.tv_comment, comment.getText());
		holder.tv_comment.setText(weiboContent);
		
		holder.iv_avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserInfoActivity.class);
				intent.putExtra("userName", user.getName());
				context.startActivity(intent);
			}
		});
		
		holder.tv_subhead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserInfoActivity.class);
				intent.putExtra("userName", user.getName());
				context.startActivity(intent);
			}
		});
		
		holder.ll_comments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(context, "回复评论", Toast.LENGTH_SHORT);
			}
		});
		
		return convertView;
	}
	
	public static class ViewHolderList {
		public LinearLayout ll_comments;
		public ImageView iv_avatar;
		public TextView tv_subhead;
		public TextView tv_caption;
		public TextView tv_comment;
	}

}
