package sg.togoparts.app;

import java.security.MessageDigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Const {
	public static final String GA_PROPERTY_ID = "UA-479713-13";
	public static final String SHARE_PREF = "togoparts pref";
	public static final String KEY_SHORTLIST = "shortlists";
	public static final String SESSION_ID = "session id";
	public static final String USER_ID = "user id";
	public static final String ACCESS_TOKEN = "access token";
	public static final String REFRESH_ID = "refresh id";
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
	public static final String IS_MY_AD = "is my ad";

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
	public static String URL_LOGOUT = "https://www.togoparts.com/iphone_ws/user-logout.php?source=android";
	public static String URL_MERGE = "https://www.togoparts.com/iphone_ws/fb-user-merge.php?source=android";
	public static String URL_SIGNUP = "https://www.togoparts.com/iphone_ws/fb-user-new.php?source=android";
	public static String URL_PROFILE = "https://www.togoparts.com/iphone_ws/user-profile.php?source=android";
	public static String URL_POST_AD = "https://www.togoparts.com/iphone_ws/mp-postad.php?source=android";
	public static String URL_POST_AD_TEST = "http://www.togoparts.com/iphone_ws/mp-postad-test.php";
	public static String URL_GET_COUNTRYS = "http://www.togoparts.com/iphone_ws/get-dropdown-values.php?country=1";
	public static String URL_GET_MY_ADS = "http://www.togoparts.com/iphone_ws/mp_list_ads.php?profilename=%s";
	public static String URL_GET_PROFILE = "https://www.togoparts.com/iphone_ws/user-profile.php?source=android";
	public static String URL_SESSION_REFRESH = "https://www.togoparts.com/iphone_ws/user-session-refresh.php?source=android";
	public static String URL_POST_AD_ONLOAD = "https://www.togoparts.com/iphone_ws/mp-postad-requirements.php?source=android";
	public static String URL_GET_SECTION = "https://www.togoparts.com/iphone_ws/get-option-values.php?source=android&section=1";
	public static String URL_GET_CATEGORY = "https://www.togoparts.com/iphone_ws/get-option-values.php?source=android&category=1&cid=%d";
	public static String URL_GET_SUBCATEGORY = "https://www.togoparts.com/iphone_ws/get-option-values.php?source=android&subcat=1&cid=%d&gid=%d";
	public static String URL_GET_BRAND = "https://www.togoparts.com/iphone_ws/get-option-values.php?source=android&brands=1&search=%s";
	public static String URL_GET_MODEL = "https://www.togoparts.com/iphone_ws/get-option-values.php?source=android&models=1&search=%s&brandname=%s";
	public static String URL_MANAGE_AD = "https://www.togoparts.com/iphone_ws/mp-manage-ad.php?source=android";
	public static String URL_ALL_MESSAGE = "https://www.togoparts.com/iphone_ws/mp_ad_comments.php?source=android&aid=%s";
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
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(encTarget.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static boolean isLogin(Context context) {
		String session_id = getTogoPartsPreferences(context).getString(
				SESSION_ID, "");
		if (session_id != null && session_id.length() > 0) {
			return true;
		}
		return false;
	}

	public static String getSessionId(Context context) {
		String session_id = getTogoPartsPreferences(context).getString(
				SESSION_ID, "");
		return session_id;
	}

	public static String getRefreshId(Context context) {
		String session_id = getTogoPartsPreferences(context).getString(
				REFRESH_ID, "");
		return session_id;
	}

	public static String getTokenFb(Context context) {
		String token = getTogoPartsPreferences(context).getString(ACCESS_TOKEN,
				"");
		return token;
	}

	public static void writeSessionId(Context context, String session_id,
			String refresh_id) {
		Editor edit = getTogoPartsPreferences(context).edit();
		edit.putString(SESSION_ID, session_id);
		edit.putString(REFRESH_ID, refresh_id);
		edit.commit();
	}

	public static void writeAccessTokenFb(Context context, String token) {
		Editor edit = getTogoPartsPreferences(context).edit();
		edit.putString(ACCESS_TOKEN, token);
		edit.commit();
	}

	public static void deleteSessionId(Context context) {
		Editor edit = getTogoPartsPreferences(context).edit();
		edit.putString(SESSION_ID, "");
		edit.putString(REFRESH_ID, "");
		edit.commit();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop()
				+ listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			// if (listItem instanceof ViewGroup) {
			// listItem.setLayoutParams(new LayoutParams(
			// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// }
			listItem.measure(0, 0);
			Log.d("haipn", "measuare height:" + listItem.getMeasuredHeight());
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void setListViewHeightBasedOnChildren1(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0) {
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));
			}
			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void setGridViewHeightBasedOnChildren(GridView listView,
			int columns, int value) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop()
				+ listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i = i + columns) {
			int height = 0;
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			View listItem2;
			listItem.measure(0, 0);
			try {
				listItem2 = listAdapter.getView(i + 1, null, listView);
				if (listItem2 instanceof ViewGroup) {
					listItem2.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}
				listItem2.measure(0, 0);
				height = listItem.getMeasuredHeight() > listItem2
						.getMeasuredHeight() ? listItem.getMeasuredHeight()
						: listItem2.getMeasuredHeight();
			} catch (IndexOutOfBoundsException e) {
				height = listItem.getMeasuredHeight();
				e.printStackTrace();
			}

			totalHeight += height;
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight;
		listView.setLayoutParams(params);
	}
}
