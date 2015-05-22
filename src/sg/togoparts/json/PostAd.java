package sg.togoparts.json;

import java.util.ArrayList;

import sg.togoparts.json.PostAdOnLoadResult.Picture;

public class PostAd {

	public PostAd() {
		super();
		session_id = "";
		aid = 0;
		adtype = 0;
		city = "";
		region = "";
		country = "";
		postalcode = "";
		address = "";
		latitude = 0.0;
		longitude = 0;
		contactno = "";
		contactperson = "";
		time_to_contact = "";
		section = -1;
		cat = -1;
		sub_cat = -1;
		brand = "";
		item = "";
		item_year = "";
		title = "";
		transtype = 0;
		description = "";
		d_mtb = false;
		d_road = false;
		d_commute = false;
		d_folding = false;
		d_bmx = false;
		d_others = false;
		size = "";
		colour = "";
		weight = 0;
		condition = 0;
		warranty = 0;
		picturelink = "";
		price = 0;
		pricetype = 0;
		original_price = 0;
		clearance = false;
		adpic1 = "";
		adpic2 = "";
		adpic3 = "";
		adpic4 = "";
		adpic5 = "";
		adpic6 = "";
	}

	private String session_id;
	private int aid;
	private int adtype; // 0 - free (default), 1 - priority, 2 - new item

	// //From Contact details
	private String city;
	private String region;
	private String country;
	private String postalcode;
	private String address;
	private double latitude;
	private double longitude;
	private String contactno;
	private String contactperson;
	private String time_to_contact;

	// /// From Category - All fields required
	private int section;
	private int cat;
	private int sub_cat;

	// /// Item Screen
	private String brand;
	private String item;
	private String item_year;
	private String title;
	private int transtype;
	private String description;

	// /// More ad details screen
	private boolean d_mtb;
	private boolean d_road;
	private boolean d_commute;
	private boolean d_folding;
	private boolean d_bmx;
	private boolean d_others;
	private String size;
	private String colour;
	private int weight;
	private int condition;
	private int warranty;
	private String picturelink;
	// /// Price screen
	private double price;
	private int pricetype;
	private double original_price;
	private boolean clearance;
	// /// Images
	private String adpic1;
	private String adpic2;
	private String adpic3;
	private String adpic4;
	private String adpic5;
	private String adpic6;
	
	private ArrayList<Picture> mExistPics;
	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getAdtype() {
		return adtype;
	}

	public void setAdtype(int adtype) {
		this.adtype = adtype;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getContactperson() {
		return contactperson;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public String getTime_to_contact() {
		return time_to_contact;
	}

	public void setTime_to_contact(String time_to_contact) {
		this.time_to_contact = time_to_contact;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public int getCat() {
		return cat;
	}

	public void setCat(int cat) {
		this.cat = cat;
	}

	public int getSub_cat() {
		return sub_cat;
	}

	public void setSub_cat(int sub_cat) {
		this.sub_cat = sub_cat;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItem_year() {
		return item_year;
	}

	public void setItem_year(String item_year) {
		this.item_year = item_year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTranstype() {
		return transtype;
	}

	public void setTranstype(int transtype) {
		this.transtype = transtype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isD_mtb() {
		return d_mtb;
	}

	public void setD_mtb(boolean d_mtb) {
		this.d_mtb = d_mtb;
	}

	public boolean isD_road() {
		return d_road;
	}

	public void setD_road(boolean d_road) {
		this.d_road = d_road;
	}

	public boolean isD_commute() {
		return d_commute;
	}

	public void setD_commute(boolean d_commute) {
		this.d_commute = d_commute;
	}

	public boolean isD_folding() {
		return d_folding;
	}

	public void setD_folding(boolean d_folding) {
		this.d_folding = d_folding;
	}

	public boolean isD_bmx() {
		return d_bmx;
	}

	public void setD_bmx(boolean d_bmx) {
		this.d_bmx = d_bmx;
	}

	public boolean isD_others() {
		return d_others;
	}

	public void setD_others(boolean d_others) {
		this.d_others = d_others;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	public int getWarranty() {
		return warranty;
	}

	public void setWarranty(int warranty) {
		this.warranty = warranty;
	}

	public String getPicturelink() {
		return picturelink;
	}

	public void setPicturelink(String picturelink) {
		this.picturelink = picturelink;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getPricetype() {
		return pricetype;
	}

	public void setPricetype(int pricetype) {
		this.pricetype = pricetype;
	}

	public double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(double original_price) {
		this.original_price = original_price;
	}

	public boolean isClearance() {
		return clearance;
	}

	public void setClearance(boolean clearance) {
		this.clearance = clearance;
	}

	public String getAdpic1() {
		return adpic1;
	}

	public void setAdpic1(String adpic1) {
		this.adpic1 = adpic1;
	}

	public String getAdpic2() {
		return adpic2;
	}

	public void setAdpic2(String adpic2) {
		this.adpic2 = adpic2;
	}

	public String getAdpic3() {
		return adpic3;
	}

	public void setAdpic3(String adpic3) {
		this.adpic3 = adpic3;
	}

	public String getAdpic4() {
		return adpic4;
	}

	public void setAdpic4(String adpic4) {
		this.adpic4 = adpic4;
	}

	public String getAdpic5() {
		return adpic5;
	}

	public void setAdpic5(String adpic5) {
		this.adpic5 = adpic5;
	}

	public String getAdpic6() {
		return adpic6;
	}

	public void setAdpic6(String adpic6) {
		this.adpic6 = adpic6;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public ArrayList<Picture> getmExistPics() {
		return mExistPics;
	}

	public void setmExistPics(ArrayList<Picture> mExistPics) {
		this.mExistPics = mExistPics;
	}
}
