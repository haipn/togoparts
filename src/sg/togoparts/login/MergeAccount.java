package sg.togoparts.login;

import java.util.HashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.TabsActivityMain;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ResultLogin;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MergeAccount extends FragmentActivity {
	protected String mAccessToken;
	protected String mFBemail;
	protected String mFBid;
	private String mUserId;
	private String mCountry;
	private String mGender;
	private String mUsername;
	private String mPicture;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	private TextView mTvUsername;
	private TextView mTvCountry;
	private TextView mTvGender;
	private ImageView mImvAvatar;
	private Button mBtnYes;
	private Button mBtnNo;

	private ProgressDialog mProgressDialog;
	private ErrorDialog mDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.merge_account);
		mTvUsername = (TextView) findViewById(R.id.tvUsername);
		mTvCountry = (TextView) findViewById(R.id.tvLocation);
		mTvGender = (TextView) findViewById(R.id.tvGender);
		mImvAvatar = (ImageView) findViewById(R.id.imvAvatar);

		Intent i = getIntent();

		mAccessToken = i.getStringExtra(ChooseLogin.ACCESSTOKEN);
		mFBemail = i.getStringExtra(ChooseLogin.FACEBOOK_MAIL);
		mFBid = i.getStringExtra(ChooseLogin.FACEBOOK_ID);
		mUserId = i.getStringExtra(ChooseLogin.USER_ID);
		mCountry = i.getStringExtra(ChooseLogin.COUNTRY);
		mGender = i.getStringExtra(ChooseLogin.GENDER);
		mUsername = i.getStringExtra(ChooseLogin.USERNAME);
		mPicture = i.getStringExtra(ChooseLogin.PICTURE);

		mTvCountry.setText(mCountry);
		mTvGender.setText(mGender);
		mTvUsername.setText(mUsername);

		imageLoader.displayImage(mPicture, mImvAvatar);

		mBtnYes = (Button) findViewById(R.id.btnYes);
		mBtnNo = (Button) findViewById(R.id.btnNo);
		mDialog = new ErrorDialog();
		mProgressDialog = new ProgressDialog(this);
		mBtnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mProgressDialog.show();
				merge();
			}
		});
		mBtnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	private void merge() {
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_MERGE, ResultLogin.class,
				createMergeSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("FBid", mFBid);
				params.put("FBemail", mFBemail);
				params.put("Userid", mUserId);
				params.put("AccessToken", mAccessToken);
				return params;
			};
		};
		queue.add(myReq);
	}

	private Response.Listener<ResultLogin> createMergeSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				mProgressDialog.dismiss();
				Log.d("haipn", "merge success:" + response.Result.Return);
				if (response.Result.Return.equals("success")) {
					Const.writeSessionId(MergeAccount.this,
							response.Result.session_id,
							response.Result.refresh_id);
					Toast.makeText(MergeAccount.this,
							"Merge Account successful", Toast.LENGTH_LONG)
							.show();
					mProgressDialog.dismiss();
					setResult(RESULT_OK);
					finish();
					
				} else {
					showError(response.Result.Message);
				}
			}
		};
	}

	private void showError(String msg) {
		mDialog.setMessage(msg);
		mDialog.show(getSupportFragmentManager(), "error");
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "response error:" + error.getMessage());
				mProgressDialog.dismiss();
			}
		};
	}
}
