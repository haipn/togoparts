package sg.togoparts.pro.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MpListLatestAds {
	@SerializedName("title")
	public String mTitle;
	
	@SerializedName("ads")
	public ArrayList<Ads> mAds;
}
