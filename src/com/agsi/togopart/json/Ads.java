package com.agsi.togopart.json;

import com.google.gson.annotations.SerializedName;

public class Ads {
	public String getAdsId() {
		return mAdsId;
	}

	public void setAdsId(String mAdsId) {
		this.mAdsId = mAdsId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getPicture() {
		return mPicture;
	}

	public void setPicture(String mPicture) {
		this.mPicture = mPicture;
	}

	public String getTimeposted() {
		return mTimeposted;
	}

	public void setTimeposted(String mTimeposted) {
		this.mTimeposted = mTimeposted;
	}

	public String getPostedby() {
		return mPostedby;
	}

	public void setPostedby(String mPostedby) {
		this.mPostedby = mPostedby;
	}

	public String getPrice() {
		return mPrice;
	}

	public void setPrice(String mPrice) {
		this.mPrice = mPrice;
	}

	public String getTranstype() {
		return mTranstype;
	}

	public void setTranstype(String mTranstype) {
		this.mTranstype = mTranstype;
	}

	@SerializedName("aid")
	String mAdsId;

	@SerializedName("title")
	String mTitle;

	@SerializedName("picture")
	String mPicture;

	@SerializedName("timeposted")
	String mTimeposted;

	@SerializedName("postedby")
	String mPostedby;

	@SerializedName("price")
	String mPrice;

	@SerializedName("transtype")
	String mTranstype;

}
