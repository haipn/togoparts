package sg.togoparts.json;

import java.util.ArrayList;

public class ListPromos {
	public String scrtitle;
	public ArrayList<Promos> list_promos;

	public class Promos {
		public String title;
		public String thumbnail;
		public String shopname;
		public String dateposted;
		public String content;
	}
}
