package sg.togoparts.pro.json;

import com.google.gson.annotations.SerializedName;

public class Category {
	@SerializedName("cid")
	public String mCategoryId;
	
	@SerializedName("title")
	public String mTitle;
	
	@SerializedName("description")
	public 	String mDescription;
	
	@SerializedName("total_ads")
	public String mTotalAds;
	
	@SerializedName("parameters")
	public String mParameters;
}
