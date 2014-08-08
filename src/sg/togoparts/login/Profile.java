package sg.togoparts.login;

import java.util.ArrayList;

import sg.togoparts.login.ResultLogin.ResultValue;

public class Profile {
	// {
	// "Result": { "Return": "success",
	// "info": {
	// "username": "goodealking",
	// "picture": "http://www.togoparts.com/members/avatars/icons/10-1.gif" (if
	// fb login use image from facebook)
	// },
	// "ratings": {
	// "Positive": "4",
	// "Neutral": "0",
	// "Negative": "0"
	// },
	// "quota": {
	// "FreeAds": "14",
	// "FreeAdsMax": 15,
	// "FreeAdsLeft": 1,
	// "PaidAds": "0",
	// "CommAds": "0",
	// "TCreds": "0"
	// }
	// }
	// }
	public ProfileValue Result;

	public class ProfileValue {
		public String Return;
		public Info info;
		public Ratings ratings;
		public ArrayList<Value> quota;
		public ArrayList<Value> postingpack;
	}

	public class Info {
		public String username;
		public String picture;
	}

	public class Ratings {
		public int Positive;
		public int Neutral;
		public int Negative;
	}

	public class Value {
		public String label;
		public int value;
	}

}
