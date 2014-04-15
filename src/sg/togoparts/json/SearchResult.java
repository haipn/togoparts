package sg.togoparts.json;

import java.util.ArrayList;

import sg.togoparts.json.AdsDetail.Special;
import sg.togoparts.json.BikeShop.PinAd;


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
		public PinAd pinad;
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
