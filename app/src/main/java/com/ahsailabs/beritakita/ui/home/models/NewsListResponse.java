package com.ahsailabs.beritakita.ui.home.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NewsListResponse {

	@SerializedName("data")
	private List<News> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(List<News> data){
		this.data = data;
	}

	public List<News> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}