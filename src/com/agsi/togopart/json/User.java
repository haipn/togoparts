package com.agsi.togopart.json;

import com.google.gson.annotations.SerializedName;

public class User {
	@SerializedName("userid")
	String mUserId;
	
	@SerializedName("username")
	String mUserName;
	
	@SerializedName("hasemail")
	String mHasEmail;
}
