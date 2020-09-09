package com.ahsailabs.beritakita.ui.detail.models;

import com.google.gson.annotations.SerializedName;

public class NewsDetail {

	@SerializedName("summary")
	private String summary;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("group_id")
	private String groupId;

	@SerializedName("updated_by")
	private String updatedBy;

	@SerializedName("photo")
	private String photo;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	@SerializedName("created_by")
	private String createdBy;

	public String getSummary(){
		return summary;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getGroupId(){
		return groupId;
	}

	public String getUpdatedBy(){
		return updatedBy;
	}

	public String getPhoto(){
		return photo;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getBody(){
		return body;
	}

	public String getCreatedBy(){
		return createdBy;
	}
}