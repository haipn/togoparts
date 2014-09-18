package sg.togoparts.pro.json;

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
		public int index;
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

//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(sid);
//		dest.writeString(shopname);
//		dest.writeString(address);
//		dest.writeString(distance);
//		dest.writeString(latitude);
//		dest.writeString(longitude);
////		if (forpaidonly != null && forpaidonly.openlabel != null)
////			dest.writeString(forpaidonly.openlabel);
//	}
//
//	public BikeShop(Parcel source) {
//		sid = source.readString();
//		shopname = source.readString();
//		address = source.readString();
//		distance = source.readString();
//		latitude = source.readString();
//		longitude = source.readString();
////		forpaidonly = new Forpaidonly();
////		forpaidonly.openlabel = source.readString();
//	}
//
//	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//		public BikeShop createFromParcel(Parcel in) {
//			return new BikeShop(in);
//		}
//
//		public BikeShop[] newArray(int size) {
//			return new BikeShop[size];
//		}
//	};
}
