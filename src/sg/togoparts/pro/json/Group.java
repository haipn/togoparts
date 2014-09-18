package sg.togoparts.pro.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Group {
	@SerializedName("gid")
	public String mGroupId;
	
	@SerializedName("title")
	public String mTitle;
	
	@SerializedName("total_ads")
	public String mTotalAds;
	
	@SerializedName("categories")
	public ArrayList<Category> mCategories;
	
	@SerializedName("featured")
	public ArrayList<Feature> mFeatures;
}
