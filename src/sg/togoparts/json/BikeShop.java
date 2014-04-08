package sg.togoparts.json;

public class BikeShop {
	public String sid;
	public String shopname;
	public String shoplogo;
	public String address;
	public String telephone;
	public String distance;
	public Forpaidonly forpaidonly;

	public class Forpaidonly {
		public String openlabel;
		public String remarks;
		public String new_item_ads;
		public String promos;
		public String shopphoto;
	}
}
