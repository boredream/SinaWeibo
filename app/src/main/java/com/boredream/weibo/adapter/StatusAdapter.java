package com.boredream.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.bdcodehelper.lean.LcUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.boredream.weibo.R;
import com.boredream.weibo.activity.StatusDetail2Activity;
import com.boredream.weibo.activity.UserInfoActivity;
import com.boredream.weibo.activity.WeiboImageBrowserActivity;
import com.boredream.weibo.activity.WriteCommentActivity;
import com.boredream.weibo.activity.WriteStatusActivity;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.utils.StringUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class StatusAdapter extends BaseAdapter {

	private Context context;
	private List<Goods> datas;

	public StatusAdapter(Context context, List<Goods> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Goods getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_status, null);
			holder.ll_card_content = (LinearLayout) convertView
					.findViewById(R.id.ll_card_content);
			holder.iv_avatar = (ImageView) convertView
					.findViewById(R.id.iv_avatar);
			holder.rl_content = (RelativeLayout) convertView
					.findViewById(R.id.rl_content);
			holder.tv_subhead = (TextView) convertView
					.findViewById(R.id.tv_subhead);
			holder.tv_caption = (TextView) convertView
					.findViewById(R.id.tv_caption);

			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.include_status_image = (FrameLayout) convertView
					.findViewById(R.id.include_status_image);
			holder.gv_images = (GridView) holder.include_status_image
					.findViewById(R.id.gv_images);
			holder.iv_image = (ImageView) holder.include_status_image
					.findViewById(R.id.iv_image);

			holder.include_retweeted_status = (LinearLayout) convertView
					.findViewById(R.id.include_retweeted_status);
			holder.tv_retweeted_content = (TextView) holder.include_retweeted_status
					.findViewById(R.id.tv_retweeted_content);
			holder.include_retweeted_status_image = (FrameLayout) holder.include_retweeted_status
					.findViewById(R.id.include_status_image);
			holder.gv_retweeted_images = (GridView) holder.include_retweeted_status_image
					.findViewById(R.id.gv_images);
			holder.iv_retweeted_image = (ImageView) holder.include_retweeted_status_image
					.findViewById(R.id.iv_image);

			holder.ll_share_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_share_bottom);
			holder.iv_share_bottom = (ImageView) convertView
					.findViewById(R.id.iv_share_bottom);
			holder.tv_share_bottom = (TextView) convertView
					.findViewById(R.id.tv_share_bottom);
			holder.ll_comment_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_comment_bottom);
			holder.iv_comment_bottom = (ImageView) convertView
					.findViewById(R.id.iv_comment_bottom);
			holder.tv_comment_bottom = (TextView) convertView
					.findViewById(R.id.tv_comment_bottom);
			holder.ll_like_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_like_bottom);
			holder.iv_like_bottom = (ImageView) convertView
					.findViewById(R.id.iv_like_bottom);
			holder.tv_like_bottom = (TextView) convertView
					.findViewById(R.id.tv_like_bottom);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// bind data
		final Goods status = getItem(position);
		final User user = status.getUser();
		Glide.with(context).load(user.getAvatarUrl()).into(holder.iv_avatar);
		holder.tv_subhead.setText(user.getNickname());
		holder.tv_caption.setText(LcUtils.getDate(status.getCreatedAt()));
		holder.tv_content.setText(StringUtils.getWeiboContent(
				context, holder.tv_content, status.getName()));
		
		holder.iv_avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserInfoActivity.class);
				intent.putExtra("userName", user.getNickname());
				context.startActivity(intent);
			}
		});
		
		holder.tv_subhead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserInfoActivity.class);
				intent.putExtra("userName", user.getNickname());
				context.startActivity(intent);
			}
		});
		
		setImages(status, holder.include_status_image, holder.gv_images, holder.iv_image);

		holder.tv_share_bottom.setText("转发");
		holder.tv_comment_bottom.setText("评论");
		holder.tv_like_bottom.setText(StringUtils.getStringWithCount("赞", status.getWorth()));
		
		holder.ll_card_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StatusDetail2Activity.start(context, status);
			}
		});

		holder.ll_share_bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, WriteStatusActivity.class);
				intent.putExtra("status", status);
				context.startActivity(intent);
			}
		});
		
		holder.ll_comment_bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if(status.getComments_count() > 0) {
//					Intent intent = new Intent(context, StatusDetailActivity.class);
//					intent.putExtra("status", status);
//					intent.putExtra("scroll2Comment", true);
//					context.startActivity(intent);
//				} else {
					Intent intent = new Intent(context, WriteCommentActivity.class);
					intent.putExtra("status", status);
					context.startActivity(intent);
//				}
			}
		});
		
		holder.ll_like_bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(context, "点个赞~", Toast.LENGTH_SHORT);
			}
		});
		
		return convertView;
	}

	private void setImages(final Goods status, FrameLayout imgContainer,
			GridView gv_images, ImageView iv_image) {
		ArrayList<String> pic_urls = status.getImages();
		String thumbnail_pic = status.getImage();
		
		if(pic_urls != null && pic_urls.size() > 1) {
			imgContainer.setVisibility(View.VISIBLE);
			gv_images.setVisibility(View.VISIBLE);
			iv_image.setVisibility(View.GONE);
			
			StatusGridImgsAdapter gvAdapter = new StatusGridImgsAdapter(context, pic_urls);
			gv_images.setAdapter(gvAdapter);
			gv_images.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(context, WeiboImageBrowserActivity.class);
					intent.putExtra("status", status);
					intent.putExtra("position", position);
					context.startActivity(intent);
				}
			});
		} else if(thumbnail_pic != null) {
			imgContainer.setVisibility(View.VISIBLE);
			gv_images.setVisibility(View.GONE);
			iv_image.setVisibility(View.VISIBLE);

			Glide.with(context).load(thumbnail_pic).into(iv_image);
			iv_image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, WeiboImageBrowserActivity.class);
					intent.putExtra("status", status);
					context.startActivity(intent);
				}
			});
		} else {
			imgContainer.setVisibility(View.GONE);
		}
	}

	public static class ViewHolder {
		public LinearLayout ll_card_content;
		public ImageView iv_avatar;
		public RelativeLayout rl_content;
		public TextView tv_subhead;
		public TextView tv_caption;

		public TextView tv_content;
		public FrameLayout include_status_image;
		public GridView gv_images;
		public ImageView iv_image;

		public LinearLayout include_retweeted_status;
		public TextView tv_retweeted_content;
		public FrameLayout include_retweeted_status_image;
		public GridView gv_retweeted_images;
		public ImageView iv_retweeted_image;

		public LinearLayout ll_share_bottom;
		public ImageView iv_share_bottom;
		public TextView tv_share_bottom;
		public LinearLayout ll_comment_bottom;
		public ImageView iv_comment_bottom;
		public TextView tv_comment_bottom;
		public LinearLayout ll_like_bottom;
		public ImageView iv_like_bottom;
		public TextView tv_like_bottom;
	}

}
