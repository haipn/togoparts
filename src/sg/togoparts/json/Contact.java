package sg.togoparts.json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Contact {
	
	@SerializedName("shop_logo")
	public String mShopLogo;
	
	@SerializedName("sid")
	public String mShopId;
	
	@SerializedName("contact_email")
	public String mContactEmail;
	
	@SerializedName("contact")
	public Labelvalue mContact;
	
	@SerializedName("location")
	public Labelvalue mLocation;

	@SerializedName("contactno")
	public Labelvalue mContactNo;
	
	public class Labelvalue {
		public String label;
		public String value;
		public ArrayList<String> actualno;
	}
}
