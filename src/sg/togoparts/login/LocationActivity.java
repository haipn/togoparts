package sg.togoparts.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sg.togoparts.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LocationActivity extends Activity {
	public static double lowerLeftLatitude = 1.1663552;
	public static double lowerLeftLongitude = 103.6065099;
	public static double upperRightLatitude = 1.4707717;
	public static double upperRightLongitude = 104.0855574;

	public static final int MAX_VALUE = 10;
	public Handler mBrandHandler = new Handler();
	public ArrayList<Address> mListAddress;
	private ArrayAdapter<String> adapterLocation;
	private BrandWatcher brandWatcher;
	private BrandRunable mBrandRunnable;
	public boolean mEnableBrandWatcher;

	private ImageButton mBtnBack;
	private ProgressBar mProgress;
	private TextView mTvTitleHeader;
	EditText mEdtLocation;
	ListView mLvLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.location_activity);
		new GetAddressTask(LocationActivity.this).execute("");

		createHeader();
		mEdtLocation = (EditText) findViewById(R.id.edtLocation);
		mLvLocation = (ListView) findViewById(R.id.listView);

		mBrandRunnable = new BrandRunable();
		mEnableBrandWatcher = true;
		brandWatcher = new BrandWatcher();
		mEdtLocation.addTextChangedListener(brandWatcher);
		mLvLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Address add = mListAddress.get(position);
				String mCity = "";
				if (add.getLocality() != null)
					mCity = add.getLocality();
				String mRegion = "";
				if (add.getAdminArea() != null)
					mRegion = add.getAdminArea();
				String mAddress = "";
				if (add.getAddressLine(0) != null)
					mAddress = add.getAddressLine(0);
				String mPostalCode = "";
				if (add.getPostalCode() != null)
					mPostalCode = add.getPostalCode();
				String mCountry = "";
				if (add.getCountryName() != null)
					mCountry = add.getCountryName();
				double mLat = add.getLatitude();
				double mLong = add.getLongitude();
				Intent i = getIntent();
				i.putExtra(PostAdActivity.CITY, mCity);
				i.putExtra(PostAdActivity.REGION, mRegion);
				i.putExtra(PostAdActivity.ADDRESS, mAddress);
				i.putExtra(PostAdActivity.POSTALCODE, mPostalCode);
				i.putExtra(PostAdActivity.COUNTRY, mCountry);
				i.putExtra(PostAdActivity.LAT, mLat);
				i.putExtra(PostAdActivity.LONGITUDE, mLong);
				String str = add.getAddressLine(0);
				for (int i1 = 1; i1 < add.getMaxAddressLineIndex(); i1++) {
					str = str + "," + add.getAddressLine(i1);
				}

				i.putExtra("full_address", str);
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.INVISIBLE);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(getString(R.string.location) + "s");
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
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
				new GetAddressTask(LocationActivity.this).execute(data);
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
				if (location.length() == 0) {
					LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
					Criteria criteria = new Criteria();
					String bestProvider = locationManager.getBestProvider(
							criteria, false);
					Location myLocation = locationManager
							.getLastKnownLocation(bestProvider);
					addresses = geocoder.getFromLocation(
							myLocation.getLatitude(),
							myLocation.getLongitude(), 10);
				} else {

					addresses = geocoder.getFromLocationName(location,
							MAX_VALUE, lowerLeftLatitude, lowerLeftLongitude,
							upperRightLatitude, upperRightLongitude);
				}
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
				mListAddress = address;
				ArrayList<String> strings = new ArrayList<String>();
				for (Address ad : address) {
					String str = ad.getAddressLine(0);
					for (int i = 1; i < ad.getMaxAddressLineIndex(); i++) {
						str = str + "," + ad.getAddressLine(i);
					}
					strings.add(str);
				}

				adapterLocation = new ArrayAdapter<String>(
						LocationActivity.this,
						android.R.layout.simple_list_item_1, strings);
				adapterLocation.setNotifyOnChange(true);
				mLvLocation.setAdapter(adapterLocation);
				// mEdtLocation.showDropDown();
			}
		}
	}
}
