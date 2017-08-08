package com.boredream.weibo.entity.response;

import com.boredream.weibo.entity.Comment;

import java.util.ArrayList;

public class CommentListResponse {
	private ArrayList<Comment> comments;
	private int total_number;

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public int getTotal_number() {
		return total_number;
	}

	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}

}