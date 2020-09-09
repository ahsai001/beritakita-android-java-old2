package com.ahsailabs.beritakita.ui.login.models;

import com.google.gson.annotations.SerializedName;

public class LoginData {

	@SerializedName("name")
	private String name;

	@SerializedName("token")
	private String token;

	@SerializedName("username")
	private String username;

	public String getName(){
		return name;
	}

	public String getToken(){
		return token;
	}

	public String getUsername(){
		return username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}