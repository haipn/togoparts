package com.agsi.togopart.json;

import com.google.gson.annotations.SerializedName;

public class Category {
	public class Parameter {
		public String adtype;
	}
	@SerializedName("cid")
	public String mCategoryId;
	
	@SerializedName("title")
	public String mTitle;
	
	@SerializedName("description")
	public 	String mDescription;
	
	@SerializedName("total_ads")
	public String mTotalAds;
	
	@SerializedName("parameters")
	public Parameter mParameters;
}
