package com.agsi.togopart.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MpListLatestAds {
	@SerializedName("title")
	String mTitle;
	
	@SerializedName("ads")
	public ArrayList<Ads> mAds;
}
