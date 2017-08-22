package com.boredream.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.weibo.R;
import com.boredream.weibo.activity.UserInfoActivity;
import com.boredream.weibo.activity.WeiboImageBrowserActivity;
import com.boredream.weibo.entity.Comment;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.utils.StringUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class StatusDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_STATUS = 110;
    private static final int ITEM_VIEW_TYPE_TAB = 119;
    private static final int ITEM_VIEW_TYPE_COMMENT = 120;
    
    private Context context;
    private Goods status;
    private int curTab;
    private List<Comment> comments;
    private final LayoutInflater inflater;

    public StatusDetailAdapter(Context context, Goods status, List<Comment> comments) {
        this.context = context;
        this.status = status;
        this.comments = comments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return ITEM_VIEW_TYPE_STATUS;
        } else if(position == 1) {
            return ITEM_VIEW_TYPE_TAB;
        }
        return ITEM_VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        // status + tab + comment list
        return comments.size() + 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_STATUS) {
            return new StatusDetailViewHolder(inflater.inflate(R.layout.include_status_content, parent, false));
        } else if(viewType == ITEM_VIEW_TYPE_TAB) {
            return new TabViewHolder(inflater.inflate(R.layout.include_status_detail_tab, parent, false));
        } else {
            return new StatusCommentRvAdapter.ViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType == ITEM_VIEW_TYPE_STATUS) {
            ((StatusDetailViewHolder)holder).bindData(status);
        } else if(viewType == ITEM_VIEW_TYPE_TAB) {
            ((TabViewHolder)holder).tab.getTabAt(curTab).select();
        } else {
            ((StatusCommentRvAdapter.ViewHolder)holder).bindData(comments.get(position-2));
        }
    }

    public static class TabViewHolder extends RecyclerView.ViewHolder {

        public TabLayout tab;

        public TabViewHolder(View itemView) {
            super(itemView);

            tab = (TabLayout) itemView;
        }
    }

    public static class StatusDetailViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        public ImageView iv_avatar;
        public TextView tv_subhead;
        public TextView tv_caption;
        public FrameLayout include_status_image;
        public GridView gv_images;
        public ImageView iv_image;
        public TextView tv_content;

        public StatusDetailViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_subhead = (TextView) itemView.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) itemView.findViewById(R.id.tv_caption);
            include_status_image = (FrameLayout) itemView.findViewById(R.id.include_status_image);
            gv_images = (GridView) itemView.findViewById(R.id.gv_images);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            // TODO: 2017/8/22
//            iv_image.setOnClickListener(this);
        }

        public void bindData(final Goods status) {
            if(status == null) return;
            final User user = status.getUser();
            Glide.with(itemView.getContext()).load(user.getAvatarUrl()).into(iv_avatar);
            tv_subhead.setText(user.getNickname());
            tv_content.setText(StringUtils.getWeiboContent(
                    context, tv_content, status.getName()));

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

            // set image
            ArrayList<String> pic_urls = status.getImages();
            String thumbnail_pic = status.getImage();

            if(pic_urls != null && pic_urls.size() > 1) {
                include_status_image.setVisibility(View.VISIBLE);
                gv_images.setVisibility(View.VISIBLE);
                iv_image.setVisibility(View.GONE);

                StatusGridImgsAdapter gvAdapter = new StatusGridImgsAdapter(context, pic_urls);
                gv_images.setAdapter(gvAdapter);
                gv_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, WeiboImageBrowserActivity.class);
                        intent.putExtra("status", status);
                        intent.putExtra("position", position);
                        context.startActivity(intent);
                    }
                });
            } else if(thumbnail_pic != null) {
                include_status_image.setVisibility(View.VISIBLE);
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
                include_status_image.setVisibility(View.GONE);
            }
        }
    }

}
