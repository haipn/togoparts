package sg.togoparts.login;

import java.util.HashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.login.ResultLogin.ResultValue;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class ChooseLogin extends FragmentActivity {
	public static final String ACCESSTOKEN = "access token";
	public static final String FACEBOOK_ID = "facebook id";
	public static final String FACEBOOK_MAIL = "facebook mail";
	public static String CLIENT_ID = "G101vptA69sVpvlr";
	protected Profile mProfileFb;
	protected SimpleFacebook mSimpleFacebook;
	private Button mBtnLoginFb;
	private Button mBtnLoginNormal;
	private ErrorDialog mDialog;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.choose_login);
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		mBtnLoginFb = (Button) findViewById(R.id.btnLoginFb);
		mBtnLoginNormal = (Button) findViewById(R.id.btnLoginNormal);

		setLoginFacebook();
		setLogin();
		mDialog = new ErrorDialog();
		mProgressDialog = new ProgressDialog(this);
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

	private void setLogin() {
		mBtnLoginNormal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ChooseLogin.this, LoginActivity.class));
			}
		});
	}

	/**
	 * Login facebook.
	 */
	private void setLoginFacebook() {
		// Login listener
		final OnLoginListener onLoginListener = new OnLoginListener() {

			@Override
			public void onFail(String reason) {
				// mTextStatus.setText(reason);
				Log.w("haipn", "Failed to login");
				mProgressDialog.dismiss();
			}

			@Override
			public void onException(Throwable throwable) {
				// mTextStatus.setText("Exception: " + throwable.getMessage());
				Log.e("haipn", "Bad thing happened", throwable);
				mProgressDialog.dismiss();
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while login is
				// happening
				Log.w("haipn", "Thinking....");
				mProgressDialog.show();
			}

			@Override
			public void onLogin() {
				// change the state of the button or do whatever you want
				// mTextStatus.setText("Logged in");
				// loggedInUIState();

				Log.d("haipn", "login fb");
				getProfileFb();
			}

			@Override
			public void onNotAcceptingPermissions(Permission.Type type) {
				// toast(String.format("You didn't accept %s permissions",
				// type.name()));
			}
		};

		mBtnLoginFb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mSimpleFacebook.login(onLoginListener);
			}
		});
	}

	private void getProfileFb() {
		Profile.Properties properties = new Profile.Properties.Builder()
				.add(Properties.ID).add(Properties.FIRST_NAME)
				.add(Properties.LAST_NAME).add(Properties.EMAIL)
				.add(Properties.COVER).add(Properties.WORK)
				.add(Properties.EDUCATION).build();
		mSimpleFacebook.getProfile(properties, onProfileListener);
	}

	OnProfileListener onProfileListener = new OnProfileListener() {
		@Override
		public void onComplete(Profile profile) {
			Log.i("haipn", "My profile id = " + profile.getId() + ", "
					+ profile.getEmail() + ","
					+ mSimpleFacebook.getSession().getAccessToken());
			mProfileFb = profile;
			loginFB(profile.getId(), profile.getEmail(), mSimpleFacebook
					.getSession().getAccessToken());
		}
	};

	public void loginFB(final String id, final String email, final String token) {
		RequestQueue queue = MyVolley.getRequestQueue();

		if (id != null && !id.equals("") && email != null && !email.equals("")
				&& token != null && !token.equals("")) {
			GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
					Method.POST, Const.URL_LOGIN, ResultLogin.class,
					createMyReqSuccessListener(), createMyReqErrorListener()) {

				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					String tkey = id + System.currentTimeMillis() / 1000
							+ CLIENT_ID;
					tkey = Const.getSHA256EncryptedString(tkey);
					params.put("FBid", id);
					params.put("FBemail", email);
					params.put("TgpKey", tkey);
					params.put("logintime", System.currentTimeMillis() / 1000
							+ "");
					params.put("AccessToken", token);
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

	protected void processResult(ResultLogin response) {
		ResultValue result = response.Result;
		if (result.Return.equals("success")) {
			Log.d("haipn", "Username:" + mProfileFb.getUsername());
			// success();
		} else if (result.Return.equals("error")) {
			showError(result.Message);
		} else if (result.Return.equals("merge")) {
			Intent merge = new Intent(this, MergeAccount.class);
			merge.putExtra(ACCESSTOKEN, mSimpleFacebook.getSession().getAccessToken());
			merge.putExtra(FACEBOOK_ID, mProfileFb.getId());
			merge.putExtra(FACEBOOK_MAIL, mProfileFb.getEmail());
			startActivity(merge);
		} else if (result.Return.equals("new")) {
			Intent signup = new Intent(this, Signup.class);
			signup.putExtra(ACCESSTOKEN, mSimpleFacebook.getSession().getAccessToken());
			signup.putExtra(FACEBOOK_ID, mProfileFb.getId());
			signup.putExtra(FACEBOOK_MAIL, mProfileFb.getEmail());
			startActivity(signup);
		}
	}

	private void showError(String msg) {
		mDialog.setMessage(msg);
		mDialog.show(getSupportFragmentManager(), "error");
	}
}
