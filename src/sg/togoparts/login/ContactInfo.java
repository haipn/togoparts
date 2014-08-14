package sg.togoparts.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sg.togoparts.R;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class ContactInfo extends FragmentActivity {

	public static final int MAX_VALUE = 10;
	public static double lowerLeftLatitude = 1.1663552;
	public static double lowerLeftLongitude = 103.6065099;
	public static double upperRightLatitude = 1.4707717;
	public static double upperRightLongitude = 104.0855574;
	EditText mEdtContactPerson;
	EditText mEdtContactNo;
	Spinner mSpnBestTime;
	AutoCompleteTextView mEdtLocation;
	Button mBtnSave;

	String mContactNo, mContactPerson, mBestTime, mCity, mRegion, mCountry,
			mPostalCode, mAddress;
	double mLat, mLong;
	// A request to connect to Location Services
	private LocationRequest mLocationRequest;

	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	/*
	 * Note if updates have been turned on. Starts out as "false"; is set to
	 * "true" in the method handleRequestSuccess of LocationUpdateReceiver.
	 */
	boolean mUpdatesRequested = false;

	private ArrayAdapter<String> adapterLocation;
	private BrandWatcher brandWatcher;
	private BrandRunable mBrandRunnable;
	public boolean mEnableBrandWatcher;
	private ImageButton mBtnBack;
	private ProgressBar mProgress;
	private TextView mTvTitleHeader;
	public Handler mBrandHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_info);
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
		mEdtLocation = (AutoCompleteTextView) findViewById(R.id.edtLocation);
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

			}
		});

		mEdtLocation.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});
		mBrandRunnable = new BrandRunable();
		mEnableBrandWatcher = true;
		brandWatcher = new BrandWatcher();
		mEdtLocation.addTextChangedListener(brandWatcher);
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
				this, R.array.best_time,
				android.R.layout.simple_spinner_dropdown_item);

		String[] bests = getResources().getStringArray(R.array.best_time);
		for (int i = 0; i < bests.length; i++) {
			if (mBestTime.equals(bests[i])) {
				d = i;
			}
		}
		mSpnBestTime.setAdapter(adapter);
		mSpnBestTime.setSelection(d);
	}

	private class BrandRunable implements Runnable {
		String data;

		public void setData(String s) {
			data = s;
		}

		@Override
		public void run() {
			if (data != null && data.length() > 0 && mEnableBrandWatcher) {
				mProgress.setVisibility(View.VISIBLE);
				new GetAddressTask(ContactInfo.this).execute(data);
			}
		}

	}

	private class BrandWatcher implements TextWatcher {

		public void afterTextChanged(Editable s) {
			Log.d("haipn", "text change:" + s.toString());
			mBrandHandler.removeCallbacks(mBrandRunnable);
			if (mEnableBrandWatcher) {
				mBrandRunnable.setData(s.toString());
				mBrandHandler.postDelayed(mBrandRunnable, 1000);
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	/**
	 * An AsyncTask that calls getFromLocation() in the background. The class
	 * uses the following generic types: Location - A
	 * {@link android.location.Location} object containing the current location,
	 * passed as the input parameter to doInBackground() Void - indicates that
	 * progress units are not used by this subclass String - An address passed
	 * to onPostExecute()
	 */
	protected class GetAddressTask extends
			AsyncTask<String, Void, ArrayList<Address>> {

		// Store the context passed to the AsyncTask when the system
		// instantiates it.
		Context localContext;

		// Constructor called by the system to instantiate the task
		public GetAddressTask(Context context) {

			// Required by the semantics of AsyncTask
			super();

			// Set a Context for the background task
			localContext = context;
		}

		/**
		 * Get a geocoding service instance, pass latitude and longitude to it,
		 * format the returned address, and return the address to the UI thread.
		 */
		@Override
		protected ArrayList<Address> doInBackground(String... params) {
			/*
			 * Get a new geocoding service instance, set for localized
			 * addresses. This example uses android.location.Geocoder, but other
			 * geocoders that conform to address standards can also be used.
			 */
			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

			// Get the current location from the input parameter list
			String location = params[0];

			// Create a list to contain the result address
			List<Address> addresses = null;

			// Try to get an address for the current location. Catch IO or
			// network problems.
			try {

				/*
				 * Call the synchronous getFromLocation() method with the
				 * latitude and longitude of the current location. Return at
				 * most 1 address.
				 */
				// addresses = geocoder.getFromLocation(location.getLatitude(),
				// location.getLongitude(), 10);
				addresses = geocoder.getFromLocationName(location, MAX_VALUE,
						lowerLeftLatitude, lowerLeftLongitude,
						upperRightLatitude, upperRightLongitude);

				// Catch network or other I/O problems.
			} catch (IOException exception1) {

				// Log an error and return an error message
				Log.e(LocationUtils.APPTAG,
						getString(R.string.IO_Exception_getFromLocation));

				// print the stack trace
				exception1.printStackTrace();

				// Return an error message
				return null;

				// Catch incorrect latitude or longitude values
			} catch (IllegalArgumentException exception2) {

				// Log the error and print the stack trace
				exception2.printStackTrace();

				//
				return null;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {

				// Get the first address

				return (ArrayList<Address>) addresses;

				// If there aren't any addresses, post a message
			} else {
				return null;
			}
		}

		/**
		 * A method that's called once doInBackground() completes. Set the text
		 * of the UI element that displays the address. This method runs on the
		 * UI thread.
		 */
		@Override
		protected void onPostExecute(ArrayList<Address> address) {
			mProgress.setVisibility(View.INVISIBLE);
			if (address != null && address.size() > 0) {

				ArrayList<String> strings = new ArrayList<String>();
				for (Address ad : address) {
					String str = "";
					for (int i = 0; i < ad.getMaxAddressLineIndex(); i++) {
						// String str = ad.getAddressLine(0) + ", "
						// + ad.getFeatureName() + ", "
						// + ad.getPostalCode() + ","
						// + ad.getThoroughfare() + ","
						// + ad.getCountryName();
						// strings.add(str);
						str = str + "," + ad.getAddressLine(i);
					}
					strings.add(str);
				}
				adapterLocation = new ArrayAdapter<String>(ContactInfo.this,
						android.R.layout.simple_dropdown_item_1line, strings);
				adapterLocation.setNotifyOnChange(true);
				mEdtLocation.setAdapter(adapterLocation);
				mEdtLocation.showDropDown();
			}
		}
	}
}
