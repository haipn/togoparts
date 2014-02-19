package com.agsi.togopart.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Group {
	@SerializedName("gid")
	String mGroupId;
	
	@SerializedName("title")
	String mTitle;
	
	@SerializedName("total_ads")
	String mTotalAds;
	
	@SerializedName("categories")
	ArrayList<Category> mCategories;
}
