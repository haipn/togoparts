package sg.togoparts.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import sg.togoparts.login.Profile.Value;

public class PostAdOnLoadResult {
	public ResultValue Result;

	public class ResultValue {
		public String Return;
		public int TCreds;
		public ArrayList<Value> merchant;
		public ArrayList<Value> postingpack;
		public ArrayList<Value> quota;
		public int min_newitem_cost;
		public int min_priority_cost;
		public String Message;
		public AdDetails ad_details;
	}

	public class AdDetails {
		public int adtype;
		public String city;
		public String region;
		public String country;
		public String postalcode;
		public String address;
		public String lat;

		@SerializedName("long")
		public String longitude;
		public String contactno;
		public String contactperson;
		public String time_to_contact;
		public String section;
		public String cat;
		public String sub_cat;
		public String brand;
		public String item;
		public int item_year;
		public String title;
		public String transtype;
		public String description;
		public int d_mtb;
		public int d_road;
		public int d_commute;
		public int d_folding;
		public int d_bmx;
		public int d_others;
		public String size;
		public String colour;
		public int weight;
		public int condition;
		public int warranty;
		public String picturelink;
		public String price;
		public int pricetype;
		public int original_price;
		public ArrayList<Picture> picture;
	}

	public class Picture {
		public String pid;
		public String picture;
	}
}
