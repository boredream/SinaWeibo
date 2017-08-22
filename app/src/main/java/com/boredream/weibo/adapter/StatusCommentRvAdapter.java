package com.boredream.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.bdcodehelper.utils.ToastUtils;
import com.boredream.weibo.R;
import com.boredream.weibo.activity.UserInfoActivity;
import com.boredream.weibo.entity.Comment;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.utils.StringUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class StatusCommentRvAdapter extends RecyclerView.Adapter<StatusCommentRvAdapter.ViewHolder> {

	private Context context;
	private List<Comment> comments;

	public StatusCommentRvAdapter(Context context, List<Comment> comments) {
		this.context = context;
		this.comments = comments;
	}
	
	@Override
	public int getItemCount() {
		return comments.size();
	}

	@Override
	public StatusCommentRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(StatusCommentRvAdapter.ViewHolder holder, int position) {
		Comment comment = comments.get(position);
		holder.bindData(comment);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private Context context;
		public LinearLayout ll_comments;
		public ImageView iv_avatar;
		public TextView tv_subhead;
		public TextView tv_caption;
		public TextView tv_comment;

		public ViewHolder(View itemView) {
			super(itemView);

			context = itemView.getContext();
			ll_comments = (LinearLayout) itemView.findViewById(R.id.ll_comments);
			iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
			tv_subhead = (TextView) itemView.findViewById(R.id.tv_subhead);
			tv_caption = (TextView) itemView.findViewById(R.id.tv_caption);
			tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
		}

		public void bindData(Comment comment) {
			final User user = comment.getUser();

			Glide.with(context).load(user.getAvatarUrl()).into(iv_avatar);
			tv_subhead.setText(user.getNickname());
//		tv_caption.setText(DateUtils.getShortTime(comment.getCreated_at()));
			SpannableString weiboContent = StringUtils.getWeiboContent(
					context, tv_comment, comment.getText());
			tv_comment.setText(weiboContent);

			iv_avatar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, UserInfoActivity.class);
					intent.putExtra("userName", user.getNickname());
					context.startActivity(intent);
				}
			});

			tv_subhead.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, UserInfoActivity.class);
					intent.putExtra("userName", user.getNickname());
					context.startActivity(intent);
				}
			});

			ll_comments.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtils.showToast(context, "回复评论", Toast.LENGTH_SHORT);
				}
			});
		}
	}

}
