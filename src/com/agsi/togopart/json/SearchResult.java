package com.agsi.togopart.json;

import java.util.ArrayList;

import com.agsi.togopart.json.AdsDetail.Special;

public class SearchResult {
	
	public SearchResult()	 {
		ads = new ArrayList<AdsResult>();
	}
	public String title;
	public ArrayList<AdsResult> ads;
	public PageDetails page_details;
	public class AdsResult {
		public String aid;
		public String title;
		public String price;
		public String picture;
		public String firm_neg;
		public String dateposted;
		public String ad_views;
		public String msg_sent;
		public String adstatus;
		public String listinglabel;
		public Special special;
		public String postedby;
		public MerchantDetails merchant_details;
	}
	public class MerchantDetails {
		public String shop_name;
		public String shop_logo;
	}
	public class PageDetails {
		public int page_id;
		public int no_of_pages;
		public int total_ads;
		}
}
