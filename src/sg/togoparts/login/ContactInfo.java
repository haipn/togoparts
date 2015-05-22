package sg.togoparts.login;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class ContactInfo extends FragmentActivity {

	protected static final int REQUEST_LOCATION = 0;

	EditText mEdtContactPerson;
	EditText mEdtContactNo;
	Spinner mSpnBestTime;
	ImageView mBtnDelete;
	Button mBtnSave;

	String mContactNo, mContactPerson, mBestTime, mCity, mRegion, mCountry,
			mPostalCode, mAddress;
	double mLat, mLong;

	private ImageButton mBtnBack;
	private ProgressBar mProgress;
	private TextView mTvTitleHeader;
	TextView mEdtLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_info);
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, "Marketplace "
				+ getIntent().getStringExtra("screen") + " Contact Info");
		tracker.send(MapBuilder.createAppView().build());
		
		createHeader();
		Intent i = getIntent();
		mContactNo = i.getStringExtra(PostAdActivity.CONTACTNO);
		mContactPerson = i.getStringExtra(PostAdActivity.CONTACTPERSON);
		mBestTime = i.getStringExtra(PostAdActivity.TIME_TO_CONTACT);
		mCity = i.getStringExtra(PostAdActivity.CITY);
		mRegion = i.getStringExtra(PostAdActivity.REGION);
		mCountry = i.getStringExtra(PostAdActivity.COUNTRY);
		mPostalCode = i.getStringExtra(PostAdActivity.POSTALCODE);
		mAddress = i.getStringExtra(PostAdActivity.ADDRESS);
		mLat = i.getDoubleExtra(PostAdActivity.LAT, 0);
		mLong = i.getDoubleExtra(PostAdActivity.LONGITUDE, 0);

		mEdtContactNo = (EditText) findViewById(R.id.edtContactNo);
		mEdtContactPerson = (EditText) findViewById(R.id.edtContactPerson);
		mEdtLocation = (TextView) findViewById(R.id.edtLocation);

		mEdtLocation.setText(mAddress);
		mSpnBestTime = (Spinner) findViewById(R.id.spnBestTime);
		mBtnSave = (Button) findViewById(R.id.btnSave);

		mEdtContactNo.setText(mContactNo);
		mEdtContactPerson.setText(mContactPerson);
		// mEdtLocation.setText(mAddress + "," + mCity + "," + mRegion + ","
		// + mCountry + "," + mPostalCode);

		initSpinner();
		mBtnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent data = getIntent();
				data.putExtra(PostAdActivity.CONTACTNO, mEdtContactNo.getText()
						.toString());
				data.putExtra(PostAdActivity.CONTACTPERSON, mEdtContactPerson
						.getText().toString());
				data.putExtra(PostAdActivity.TIME_TO_CONTACT,
						(String) mSpnBestTime.getSelectedItem());
				data.putExtra(PostAdActivity.CITY, mCity);
				data.putExtra(PostAdActivity.REGION, mRegion);
				data.putExtra(PostAdActivity.COUNTRY, mCountry);
				data.putExtra(PostAdActivity.POSTALCODE, mPostalCode);
				data.putExtra(PostAdActivity.ADDRESS, mAddress);
				data.putExtra(PostAdActivity.LAT, mLat);
				data.putExtra(PostAdActivity.LONGITUDE, mLong);
				setResult(RESULT_OK, data);
				finish();
			}
		});

		mEdtLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Tracker tracker = GoogleAnalytics.getInstance(ContactInfo.this).getTracker(
						Const.GA_PROPERTY_ID);
				tracker.set(Fields.SCREEN_NAME, "Marketplace "
						+ getIntent().getStringExtra("screen") + " Location");
				tracker.send(MapBuilder.createAppView().build());
				
				Intent i = new Intent(ContactInfo.this, LocationActivity.class);
				startActivityForResult(i, REQUEST_LOCATION);
			}
		});

		mEdtLocation.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Intent i = new Intent(ContactInfo.this,
							LocationActivity.class);
					startActivityForResult(i, REQUEST_LOCATION);
				}
			}
		});
		mBtnDelete = (ImageView) findViewById(R.id.btnDelete);
		mBtnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCity = "";
				mRegion = "";
				mCountry = "";
				mPostalCode = "";
				mAddress = "";
				mLat = 0;
				mLong = 0;
				mEdtLocation.setText("");
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_LOCATION) {
			if (data != null) {
				mAddress = data.getStringExtra(PostAdActivity.ADDRESS);
				mCity = data.getStringExtra(PostAdActivity.CITY);
				mRegion = data.getStringExtra(PostAdActivity.REGION);
				mCountry = data.getStringExtra(PostAdActivity.COUNTRY);
				mPostalCode = data.getStringExtra(PostAdActivity.POSTALCODE);
				mLat = data.getDoubleExtra(PostAdActivity.LAT, 0);
				mLong = data.getDoubleExtra(PostAdActivity.LONGITUDE, 0);
				mEdtLocation.setText(mAddress);
			}
		}
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.INVISIBLE);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_contact_info);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}

	private void initSpinner() {
		int d = 0;
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.best_time, R.layout.spinner_text);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		String[] bests = getResources().getStringArray(R.array.best_time);
		for (int i = 0; i < bests.length; i++) {
			if (mBestTime.equals(bests[i])) {
				d = i;
			}
		}
		mSpnBestTime.setAdapter(adapter);
		mSpnBestTime.setSelection(d);
	}

}
