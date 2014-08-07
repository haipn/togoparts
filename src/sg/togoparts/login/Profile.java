package sg.togoparts.login;

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
		public Quota quota;
		public PostingPack postingpack;
	}

	public class Info {
		public String username;
		public String picture;
	}

	public class Ratings {
		public String Positive;
		public String Neutral;
		public String Negative;
	}

	public class Quota {
		public String FreeAds;
		public String FreeAdsMax;
		public String FreeAdsLeft;
		public String PaidAds;
		public String CommAds;
		public String TCreds;
	}

	public class PostingPack {
		public String AdsPosted;// (Label: Ads Posted)
		public String AdsQuota;// Label: Ads Quota)
		public String AdsBalance;// ": 11, (Label: Ads Balance)
		public String TCreds;// ": "56" (Label: TCredits balance)
	}

}
