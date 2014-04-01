package sg.togoparts.json;

import com.google.gson.annotations.SerializedName;

public class User {
	@SerializedName("userid")
	public String mUserId;
	
	@SerializedName("username")
	public String mUserName;
	
	@SerializedName("hasemail")
	public String mHasEmail;
}
