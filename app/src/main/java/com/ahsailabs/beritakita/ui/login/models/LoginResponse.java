package com.ahsailabs.beritakita.ui.login.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("data")
	private LoginData data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public LoginData getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}