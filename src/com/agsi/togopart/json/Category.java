package com.agsi.togopart.json;

import com.google.gson.annotations.SerializedName;

public class Category {
	class Parameter {
		String adtype;
	}
	@SerializedName("cid")
	String mCategoryId;
	
	@SerializedName("title")
	String mTitle;
	
	@SerializedName("description")
	String mDescription;
	
	@SerializedName("total_ads")
	String mTotalAds;
	
	@SerializedName("parameters")
	Parameter mParameters;
}
