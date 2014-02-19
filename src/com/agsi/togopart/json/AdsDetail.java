package com.agsi.togopart.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class AdsDetail {
	class Picture {
		String s;
		String l;
	}

	@SerializedName("title")
	String mTitle;

	@SerializedName("price")
	String mPrice;
	
	@SerializedName("price_percent_diff")
	String mPricePercentDiff;
	
	@SerializedName("firm_neg")
	String mFirmNeg;
	
	@SerializedName("canonical_url")
	String mCanonicalUrl;
	
	@SerializedName("picture")
	ArrayList<Picture> mPictures;
	
	@SerializedName("adstatus")
	String mAdStatus;
	
	@SerializedName("aid")
	String mAdid;
	
	@SerializedName("userid")
	String mUserId;
	
	@SerializedName("transtype")
	String mTranstype;
	
	@SerializedName("size")
	String mSize;
	
	@SerializedName("description")
	String mDescription;
	
	@SerializedName("dateposted")
	String mDateposted;
	
	@SerializedName("contactperson")
	String mContactPerson;
	
	@SerializedName("ad_views")
	String mAdViews;
	
	@SerializedName("msg_sent")
	String mMsgSent;
	
	@SerializedName("listinglabel")
	String mListingLabel;
	
	@SerializedName("special")
	String mSpecial;
	
	@SerializedName("postedby_details")
	User mPostedByDetails;
	
	@SerializedName("contact_details")
	Contact mContactDetails;
	
	@SerializedName("related_ads")
	ArrayList<Ads> mRelatedAds;
}
