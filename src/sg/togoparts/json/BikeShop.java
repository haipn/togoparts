package sg.togoparts.json;

import java.util.ArrayList;

public class BikeShop {
	public String sid;
	public String shopname;
	public String latitude;
	public String longitude;
	public String shoplogo;
	public String address;
	public String telephone;
	public String distance;
	public Forpaidonly forpaidonly;
	public PinAd pinad;

	public class PinAd {
		public String unit_id;
		public int unit_width;
		public int unit_height;
	}

	public class Forpaidonly {
		public String openlabel;
		public String remarks;
		public String new_item_ads;
		public String promos;
		public String shopphoto;
		public String openinghrs;
		public String mobile;
		public String fax;
		public String email;
		public String website;
		public String bikes_avail;
		public String mechanic_svcs;
		public String delivery;
		public ArrayList<Brand> brands_dist;
		public String brands_retailed;
		public String new_item;
		public ArrayList<String> actualno;

		public String new_item_cnt;
		public String promo;
		public String promo_cnt;
	}

	public class Brand {
		public String name;
		public String img;
	}
}
