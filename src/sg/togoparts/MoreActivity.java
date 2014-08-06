package sg.togoparts;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.login.ChooseLogin;
import sg.togoparts.login.ResultLogin;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity_Main {
	TextView mTvSearchAds;
	TextView mTvSearchShop;
	TextView mTvAbout;
	TextView mTvShortlist;
	TextView mTvLoginOrLogout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);

		createHeader();
		mTvSearchAds = (TextView) findViewById(R.id.tvAdsSearch);
		mTvSearchShop = (TextView) findViewById(R.id.tvShopSearch);
		mTvAbout = (TextView) findViewById(R.id.tvAbout);
		mTvShortlist = (TextView) findViewById(R.id.tvShortlistedAds);
		mTvLoginOrLogout = (TextView) findViewById(R.id.tvLoginOrLogout);

		mTvSearchAds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MoreActivity.this,
						FSActivity_Search.class));
			}
		});

		mTvSearchShop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Bundle b = new Bundle();
				// b.putString(FilterBikeShop.AREA, "");
				// b.putString(FilterBikeShop.BIKESHOP_NAME, "");
				// b.putString(FilterBikeShop.SORT_BY, "2");
				// b.putBoolean(FilterBikeShop.MECHANIC, true);
				// b.putBoolean(FilterBikeShop.OPENNOW, true);
				Intent i = new Intent(MoreActivity.this, FilterBikeShop.class);
				// i.putExtras(b);
				startActivity(i);
			}
		});
		mTvAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MoreActivity.this, AboutActivity.class));
			}
		});

		mTvShortlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(MoreActivity.this, FSActivity_ShortList.class);
				startActivity(i);
			}
		});
		if (Const.isLogin(this)) {
			mTvLoginOrLogout.setText(R.string.label_logout);
			mTvLoginOrLogout.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.logout_icon, 0, 0, 0);
		} else {
			mTvLoginOrLogout.setText(R.string.label_login);
			mTvLoginOrLogout.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.login_icon, 0, 0, 0);
		}

		mTvLoginOrLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Const.isLogin(MoreActivity.this)) {
					logout();
				} else {
					Intent i = new Intent(MoreActivity.this, ChooseLogin.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					startActivity(i);
					finish();
				}

			}
		});
	}

	protected void logout() {
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_LOGOUT, ResultLogin.class,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				String key = Const.getRefreshId(MoreActivity.this) + ChooseLogin.CLIENT_ID;
				key = Const.getSHA256EncryptedString(key);
				params.put("session_id", Const.getSessionId(MoreActivity.this));
				params.put("refresh_id", key);
				return params;
			};
		};
		queue.add(myReq);

	}

	private Response.Listener<ResultLogin> createMyReqSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				Log.d("haipn", "response success:" + response.Result.Return);
				Toast.makeText(MoreActivity.this, "Logout successful", Toast.LENGTH_LONG).show();
				Const.deleteSessionId(MoreActivity.this);
				mTvLoginOrLogout.setText(R.string.label_login);
				mTvLoginOrLogout.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.login_icon, 0, 0, 0);
				
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "response error:" + error.getMessage());
			}
		};
	}

	private void createHeader() {
		findViewById(R.id.btnBack).setVisibility(View.GONE);
		findViewById(R.id.btnSearch).setVisibility(View.GONE);
		findViewById(R.id.logo).setVisibility(View.GONE);

		TextView mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvTitle.setText(R.string.more);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (Const.isAppExitable) {
			return false;
			// } else
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
}
