package sg.togoparts.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ResultLogin;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.sromku.simple.fb.SimpleFacebook;

public class Signup extends FragmentActivity {
	protected String mFbId;
	protected String mFbEmail;
	protected String mUsername;
	protected String mAccessToken;
	protected String mCountry;
	private String mFbName;

	private TextView mTvName;
	private TextView mTvEmail;
	private TextView mTvDob;

	private Spinner mSpnCountry;
	private EditText mEdtUsername;
	private Button mBtnSignup;
	private Button mBtnCancel;
	private ErrorDialog mDialog;
	protected ProgressDialog mProgressDialog;

	private long mDate;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.sign_up);
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, "Facebook Signup Form");
		tracker.send(MapBuilder.createAppView().build());
		Intent i = getIntent();
		mFbId = i.getStringExtra(ChooseLogin.FACEBOOK_ID);
		mFbEmail = i.getStringExtra(ChooseLogin.FACEBOOK_MAIL);
		mFbName = i.getStringExtra(ChooseLogin.FACEBOOK_FIRST_LAST_NAME);
		mAccessToken = i.getStringExtra(ChooseLogin.ACCESSTOKEN);

		mTvName = (TextView) findViewById(R.id.tvName);
		mTvEmail = (TextView) findViewById(R.id.tvEmail);
		mEdtUsername = (EditText) findViewById(R.id.edtUserName);
		mSpnCountry = (Spinner) findViewById(R.id.spnCountry);
		mBtnSignup = (Button) findViewById(R.id.btnRegister);
		mBtnCancel = (Button) findViewById(R.id.btnCancel);
		mTvDob = (TextView) findViewById(R.id.tvDob);
		mDate = System.currentTimeMillis();
		// setTextDob();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<Country> myReq = new GsonRequest<Country>(Method.GET,
				Const.URL_GET_COUNTRYS, Country.class,
				createCountrySuccessListener(), createMyReqErrorListener());
		queue.add(myReq);

		mTvName.setText(mFbName);
		mTvEmail.setText(mFbEmail);
		mBtnSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEdtUsername.getText().toString().length() == 0) {
					Toast.makeText(Signup.this, "Please insert username",
							Toast.LENGTH_LONG).show();
					mEdtUsername.requestFocus();
				} else if (mTvDob.getText().toString().length() == 0) {
					Toast.makeText(Signup.this, "Please select Date of Birth",
							Toast.LENGTH_LONG).show();
					mTvDob.requestFocus();
				} else {
					mProgressDialog.show();
					signup();
				}

			}
		});

		mSpnCountry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("haipn",
						"spiner country:"
								+ (String) mSpnCountry.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		mBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimpleFacebook fb = SimpleFacebook.getInstance(Signup.this);
				fb.logout(null);
				finish();
			}
		});

		mTvDob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDatePicker();
			}
		});
		mTvDob.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					openDatePicker();

			}
		});
		mDialog = new ErrorDialog();
		mProgressDialog = new ProgressDialog(this);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void openDatePicker() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mDate);
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		System.out.println("the selected " + mDay);
		DatePickerDialog dialog = new DatePickerDialog(Signup.this,
				new DateSetListener(), mYear, mMonth, mDay);
		dialog.getDatePicker().setMaxDate(new Date().getTime());
		dialog.show();
	}

	private void setTextDob() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mDate);
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = c.getTime();
		mTvDob.setText(format.format(date));
	}

	class DateSetListener implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			// getCalender();
			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			mDate = c.getTimeInMillis();
			setTextDob();
		}

	}

	private void signup() {
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_SIGNUP, ResultLogin.class,
				createSignupSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams() throws AuthFailureError {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(mDate);
				Date date = c.getTime();
				String dob = format.format(date);
				Map<String, String> params = new HashMap<String, String>();
				params.put("FBid", mFbId);
				params.put("FBemail", mFbEmail);
				params.put("Username", mEdtUsername.getText().toString());
				params.put("AccessToken", mAccessToken);
				params.put("Country", (String) mSpnCountry.getSelectedItem());
				params.put("dob", dob);
				return params;
			};
		};
		queue.add(myReq);
	}

	private Response.Listener<ResultLogin> createSignupSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				mProgressDialog.dismiss();
				Log.d("haipn", "signup success:" + response.Result.Return);
				if (response.Result.Return.equals("success")) {
					Const.writeSessionId(Signup.this,
							response.Result.session_id,
							response.Result.refresh_id);
					Toast.makeText(Signup.this, "Signup Account successful",
							Toast.LENGTH_LONG).show();
					mProgressDialog.dismiss();
					setResult(RESULT_OK);
					finish();
				} else {
					showError(response.Result.Message);
				}
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

	private Response.Listener<Country> createCountrySuccessListener() {
		return new Response.Listener<Country>() {
			@Override
			public void onResponse(Country response) {
				ArrayList<String> list = response.Result.Country;
				// listCatId.put("All", "");
				ArrayAdapter<String> adtCountry = new ArrayAdapter<String>(
						Signup.this, R.layout.spinner_text, list);
				adtCountry
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpnCountry.setAdapter(adtCountry);
			}
		};
	}

	private void showError(String msg) {
		mDialog.setMessage(msg);
		mDialog.show(getSupportFragmentManager(), "error");
	}

	public class Country {
		public CountryList Result;

		public class CountryList {
			public ArrayList<String> Country;
		}
	}
}
