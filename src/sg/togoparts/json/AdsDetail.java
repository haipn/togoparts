package sg.togoparts.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class AdsDetail {
	
	@SerializedName("title")
	public String mTitle;

	@SerializedName("price")
	public String mPrice;
	
	@SerializedName("price_percent_diff")
	public String mPricePercentDiff;
	
	@SerializedName("firm_neg")
	public String mFirmNeg;
	
	@SerializedName("canonical_url")
	public String mCanonicalUrl;
	
	@SerializedName("picture")
	public ArrayList<Picture> mPictures;
	
	@SerializedName("adstatus")
	public String mAdStatus;
	
	@SerializedName("aid")
	public String mAdid;
	
	@SerializedName("adtype")
	public String mAdType;
	
	@SerializedName("size")
	public String mSize;
	
	@SerializedName("description")
	public String mDescription;
	
	@SerializedName("dateposted")
	public String mDateposted;
	
	@SerializedName("ad_views")
	public String mAdViews;
	
	@SerializedName("msg_sent")
	public String mMsgSent;
	
	@SerializedName("listinglabel")
	public String mListingLabel;
	
	@SerializedName("special")
	public Special mSpecial;
	
	@SerializedName("postedby_details")
	public User mPostedByDetails;
	
	@SerializedName("contact_details")
	public Contact mContactDetails;
	
	@SerializedName("related_ads")
	public ArrayList<Ads> mRelatedAds;
	
	public class Picture {
		public String s;
		public String l;
	}

	public class Special {
		public String text;
		public String bgcolor;
		public String textcolor;
	}
	 
}
