package com.agsi.togopart.json;

import com.google.gson.annotations.SerializedName;

public class Contact {
	
	@SerializedName("contactperson")
	String mContactPerson;
	
	@SerializedName("contactno")
	String mContactNumber;
	
	@SerializedName("location")
	String mContactLocation;
	
	@SerializedName("contact_email")
	String mContactEmail;
}
