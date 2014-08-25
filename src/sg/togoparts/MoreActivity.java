package sg.togoparts;

import java.util.HashMap;
import java.util.Map;

import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ResultLogin;
import sg.togoparts.login.ChooseLogin;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;

public class MoreActivity extends Activity_Main {
	TextView mTvSearchAds;
	TextView mTvSearchShop;
	TextView mTvAbout;
	TextView mTvShortlist;
	TextView mTvLoginOrLogout;
	protected SimpleFacebook mSimpleFacebook;
	ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		mSimpleFacebook = SimpleFacebook.getInstance(this);
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

				Intent i = new Intent(MoreActivity.this,
						FSActivity_ShortList.class);
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
		mProgressDialog = new ProgressDialog(this);
		mTvLoginOrLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Const.isLogin(MoreActivity.this)) {
					AlertDialog.Builder builder = new Builder(MoreActivity.this);
					builder.setMessage(R.string.msg_confirm_logout)
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											logout();
											mProgressDialog.show();
											dialog.dismiss();
										}
									})
							.setNegativeButton(android.R.string.no,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									});
					builder.show();

				} else {
					Intent i = new Intent(MoreActivity.this, ChooseLogin.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					startActivity(i);
					finish();
				}

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
	}

	protected void logout() {
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_LOGOUT, ResultLogin.class,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				String key = Const.getRefreshId(MoreActivity.this)
						+ ChooseLogin.CLIENT_ID;
				key = Const.getSHA256EncryptedString(key);
				params.put("session_id", Const.getSessionId(MoreActivity.this));
				params.put("refresh_id", key);
				return params;
			};
		};
		queue.add(myReq);

		// logout listener

		mSimpleFacebook.logout(onLogoutListener);
	}

	OnLogoutListener onLogoutListener = new OnLogoutListener() {
		@Override
		public void onLogout() {
			Log.i("haipn", "Facebook You are logged out");
		}

		@Override
		public void onThinking() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub

		}
	};

	private Response.Listener<ResultLogin> createMyReqSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				Log.d("haipn", "response success:" + response.Result.Return);
				Toast.makeText(MoreActivity.this, "Logout successful",
						Toast.LENGTH_LONG).show();
				Const.deleteSessionId(MoreActivity.this);
				Const.writeAccessTokenFb(MoreActivity.this, "");
				mTvLoginOrLogout.setText(R.string.label_login);
				mTvLoginOrLogout.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.login_icon, 0, 0, 0);
				mProgressDialog.dismiss();
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
		findViewById(R.id.btnBack).setVisibility(View.INVISIBLE);
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.INVISIBLE);

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
