package sg.togoparts.login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ResultLogin;
import sg.togoparts.json.ResultLogin.ResultValue;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class LoginActivity extends FragmentActivity {

	public static String CLIENT_ID = "G101vptA69sVpvlr";
	public static String USER = "tgptestuser3";
	public static String PASS = "hx77WTF3";
	EditText mEdtUser;
	EditText mEdtPass;
	Button mBtnLogin;
	protected Profile mProfileFb;
	protected SimpleFacebook mSimpleFacebook;
	protected ErrorDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mSimpleFacebook = SimpleFacebook.getInstance(this);
		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
		// "sg.togoparts", PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// Log.d("KeyHash:",
		// Base64.encodeToString(md.digest(), Base64.DEFAULT));
		// }
		// } catch (NameNotFoundException e) {
		//
		// } catch (NoSuchAlgorithmException e) {
		//
		// }
		mEdtPass = (EditText) findViewById(R.id.edtPass);
		mEdtPass.setText(PASS);

		mEdtUser = (EditText) findViewById(R.id.edtUser);
		mEdtUser.setText(USER);
		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		mBtnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login(mEdtUser.getText().toString(), mEdtPass.getText()
						.toString());
			}
		});

		// setLogin();
		// setLogout();

		mDialog = new ErrorDialog();

	}

	OnProfileListener onProfileListener = new OnProfileListener() {
		@Override
		public void onComplete(Profile profile) {
			Log.i("haipn", "My profile id = " + profile.getId() + ", "
					+ profile.getEmail() + ","
					+ mSimpleFacebook.getSession().getAccessToken());
			mProfileFb = profile;
			// loginFB(profile.getId(), profile.getEmail(), mSimpleFacebook
			// .getSession().getAccessToken());
		}
	};

	/**
	 * Logout example
	 */
	// private void setLogout() {
	// final OnLogoutListener onLogoutListener = new OnLogoutListener() {
	//
	// @Override
	// public void onFail(String reason) {
	// Log.w("haipn", "Failed to login");
	// }
	//
	// @Override
	// public void onException(Throwable throwable) {
	// Log.e("haipn", "Bad thing happened", throwable);
	// }
	//
	// @Override
	// public void onThinking() {
	// // show progress bar or something to the user while login is
	// // happening
	// Log.d("haipn", "Thinking...");
	// }
	//
	// @Override
	// public void onLogout() {
	// // change the state of the button or do whatever you want
	// Log.d("haipn", "logout...");
	// }
	//
	// };
	//
	// mBtnLogout.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View arg0) {
	// mSimpleFacebook.logout(onLogoutListener);
	// }
	// });
	// }

	public void login(final String user, final String pass) {
		RequestQueue queue = MyVolley.getRequestQueue();

		if (user != null && !user.equals("") && pass != null
				&& !pass.equals("")) {
			GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
					Method.POST, Const.URL_LOGIN, ResultLogin.class,
					createMyReqSuccessListener(), createMyReqErrorListener()) {

				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					String key = pass + System.currentTimeMillis() / 1000
							+ CLIENT_ID;
					key = Const.getSHA256EncryptedString(key);
					params.put("TgpUserName", user);
					params.put("logintime", System.currentTimeMillis() / 1000
							+ "");
					params.put("TgpKey", key);
					return params;
				};
			};
			queue.add(myReq);
		}
	}
	private Response.Listener<ResultLogin> createMyReqSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				Log.d("haipn", "response success:" + response.Result.Return);
				processResult(response);
			}
		};
	}

	private Response.Listener<ResultLogin> createMergeSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				Log.d("haipn", "merge success:" + response.Result.Return);
			}
		};
	}

	private Response.Listener<ResultLogin> createSignupSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				Log.d("haipn", "signup success:" + response.Result.Return);
			}
		};
	}

	protected void processResult(ResultLogin response) {
		ResultValue result = response.Result;
		if (result.Return.equals("success")) {
			Log.d("haipn", "Username:" + mProfileFb.getUsername());
			success();
		} else if (result.Return.equals("error")
				|| result.Return.equals("banned")) {
			showError(result.Message);
		}
	}

	private void signup() {
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_SIGNUP, ResultLogin.class,
				createSignupSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("FBid", mProfileFb.getId());
				params.put("FBemail", mProfileFb.getEmail());

				params.put("Username", mProfileFb.getUsername());
				params.put("AccessToken", mSimpleFacebook.getSession()
						.getAccessToken());
				params.put("Country", "Vietnam");
				return params;
			};
		};
		queue.add(myReq);
	}

	private void merge(final String userid) {
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_MERGE, ResultLogin.class,
				createMergeSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("FBid", mProfileFb.getId());
				params.put("FBemail", mProfileFb.getEmail());
				params.put("Userid", userid);
				params.put("AccessToken", mSimpleFacebook.getSession()
						.getAccessToken());
				return params;
			};
		};
		queue.add(myReq);
	}

	private void showError(String msg) {
		mDialog.setMessage(msg);
		mDialog.show(getSupportFragmentManager(), "error");
	}

	private void success() {
		// TODO Auto-generated method stub

	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "response error:" + error.getMessage());
			}
		};
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getProfileFb() {
		Profile.Properties properties = new Profile.Properties.Builder()
				.add(Properties.ID).add(Properties.FIRST_NAME)
				.add(Properties.LAST_NAME).add(Properties.EMAIL)
				.add(Properties.COVER).add(Properties.WORK)
				.add(Properties.EDUCATION).build();
		mSimpleFacebook.getProfile(properties, onProfileListener);
	}

}
