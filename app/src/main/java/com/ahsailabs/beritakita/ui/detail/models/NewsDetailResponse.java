package com.ahsailabs.beritakita.ui.detail.models;

import com.google.gson.annotations.SerializedName;

public class NewsDetailResponse {

	@SerializedName("data")
	private NewsDetail newsDetail;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public NewsDetail getNewsDetail(){
		return newsDetail;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}