package sg.togoparts.app;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Const {
	public static final String GA_PROPERTY_ID = "UA-479713-13";
	public static final String SHARE_PREF = "togoparts pref";
	public static final String KEY_SHORTLIST = "shortlists";
	public static final String ADS_ID = "ads id";
	public static final String CAT_ID = "category id";
	public static final String QUERY = "query";
	public static final String BUNDLE_QUERY = "bundle query";
	public static final String TAG_NAME = "tag name";
	public static final String TITLE = "title";
	public static final String SHOP_ID = "shop id";
	public static final String URL_BIKESHOP_DETAIL = "http://www.togoparts.com/iphone_ws/bs_details.php?source=android&sid=%s&lat=%s&long=%s";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	
	public static String URL_LIST_LASTEST_ADS = "http://www.togoparts.com/iphone_ws/mp_list_latest_ads.php?source=android";
	public static String URL_LIST_CATEGORY = "http://www.togoparts.com/iphone_ws/mp_list_categories.php?source=android";
	public static String URL_LIST_SEARCH_PARAM = "searchtext=%s"
			+ "&usersearchname=%s" + "&size=%s" + "&sizeft=%s" + "&sizelb=%s"
			+ "&bfrom=%s" + "&bto=%s" + "&status=%s" + "&cid=%s" + "&adtype=%s"
			+ "&sid=%s" + "&sort=%s";
	public static String URL_SEARCH_CATEGORY = "http://www.togoparts.com/iphone_ws/mp_search_variables.php?source=android";
	public static String URL_ADS_DETAIL = "http://www.togoparts.com/iphone_ws/mp_ad_details.php?source=android&aid=%s";
	public static String URL_LIST_SEARCH = "http://www.togoparts.com/iphone_ws/mp_list_ads.php?v=1.1&source=android&%s";
	public static String URL_SHORTLIST = "http://www.togoparts.com/iphone_ws/mp_shortlist_ads.php?source=android&aid=%s";
	public static String URL_CONTACT_LOG = "http://www.togoparts.com/iphone_ws/contact_log.php?source=android&id=%s&fktype=%s&category=%s";
	public static String URL_BIKE_SHOP = "http://www.togoparts.com/iphone_ws/bs_listings.php?source=android&shopsearch=%s&country=%s&area=%s&open=%s&mechanic=%s&lat=%s&long=%s&sortedby=%s";
	public static String URL_ABOUT = "http://www.togoparts.com/iphone_ws/about-us.php?source=android";
	public static String URL_PROMOS = "http://www.togoparts.com/iphone_ws/bs_promos.php?source=android&sid=%s";
	public static String URL_LOGIN = "https://www.togoparts.com/iphone_ws/user-login.php?source=android";
	public static String URL_MERGE = "https://www.togoparts.com/iphone_ws/fb-user-merge.php?source=android";
	public static String URL_SIGNUP = "https://www.togoparts.com/iphone_ws/fb-user-new.php?source=android";
	public static String URL_PROFILE = "https://www.togoparts.com/iphone_ws/user-profile.php?source=android";
	public static String URL_POST_AD = "http://www.togoparts.com/iphone_ws/mp-postad-test.php?debugcode=n1vJuAis&source=android";
	public static boolean isAppExitable = false;

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	public static SharedPreferences getTogoPartsPreferences(Context context) {
		return context.getSharedPreferences(SHARE_PREF, Context.MODE_PRIVATE);
	}

	public static boolean hasInShortList(Context context, String aid) {
		String list = getTogoPartsPreferences(context).getString(KEY_SHORTLIST,
				"");
		String[] array = convertStringToStringArray(list);
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(aid))
				return true;
		}

		return false;
	}

	public static void addToShortList(Context context, String aid) {
		String list = getTogoPartsPreferences(context).getString(KEY_SHORTLIST,
				"");
		if (list.isEmpty()) {
			list = aid;
		} else {
			list = list + " " + aid;
		}
		Editor edit = getTogoPartsPreferences(context).edit();
		edit.putString(KEY_SHORTLIST, list);
		edit.commit();
	}

	public static void removeFromShortList(Context context, String aid) {
		String list = getTogoPartsPreferences(context).getString(KEY_SHORTLIST,
				"");
		String[] array = convertStringToStringArray(list);
		list = "";

		for (int i = 0; i < array.length; i++) {
			if (!array[i].equals(aid)) {
				list = list + array[i] + " ";
			}
		}
		Editor edit = getTogoPartsPreferences(context).edit();
		edit.putString(KEY_SHORTLIST, list);
		edit.commit();
	}

	private static String[] convertStringToStringArray(String text) {
		String[] ret;
		ret = text.split(" ");
		return ret;
	}
	
	public static String getSHA256EncryptedString(String encTarget) {
		try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(encTarget.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
}
