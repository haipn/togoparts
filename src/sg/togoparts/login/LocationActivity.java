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
import android.location.Geocoder;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationActivity extends Activity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
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

	// A request to connect to Location Services
	private LocationRequest mLocationRequest;

	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	boolean mUpdatesRequested = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.location_activity);

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

		// Create a new global location parameters object
		mLocationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
		mLocationRequest
				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		mLocationRequest
				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		// Note that location updates are off until the user turns them on
		mUpdatesRequested = true;

		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(this, this, this);
		
	}
	 public void startUpdates() {
	        mUpdatesRequested = true;

	        if (servicesConnected()) {
	            startPeriodicUpdates();
	        }
	    }
	/*
	 * Called when the Activity is restarted, even before it becomes visible.
	 */
	@Override
	public void onStart() {

		super.onStart();

		/*
		 * Connect the client. Don't re-start any requests here; instead, wait
		 * for onResume()
		 */
		mLocationClient.connect();
		

	}

	/*
	 * Called when the Activity is no longer visible at all. Stop updates and
	 * disconnect.
	 */
	@Override
	public void onStop() {

		// If the client is connected
		if (mLocationClient.isConnected()) {
			stopPeriodicUpdates();
		}

		// After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();

		super.onStop();
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
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG,
					getString(R.string.play_services_available));

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			// Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
			// this, 0);
			// if (dialog != null) {
			// ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// errorFragment.setDialog(dialog);
			// errorFragment.show(getSupportFragmentManager(),
			// LocationUtils.APPTAG);
			// }
			return false;
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
					if (servicesConnected()) {

						// Get the current location
						Location currentLocation = mLocationClient
								.getLastLocation();
						if (currentLocation == null)
							return null;
						addresses = geocoder.getFromLocation(
								currentLocation.getLatitude(),
								currentLocation.getLongitude(), MAX_VALUE);
					}
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
			} else {
				mListAddress.clear();
				adapterLocation.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (mUpdatesRequested) {
			startPeriodicUpdates();
			new GetAddressTask(this).execute("");
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/**
	 * Report location updates to the UI.
	 * 
	 * @param location
	 *            The updated location.
	 */
	@Override
	public void onLocationChanged(Location location) {

		// Report to the UI that the location was updated

		// In the UI, set the latitude and longitude to the value received
	}

	/**
	 * In response to a request to start updates, send a request to Location
	 * Services
	 */
	private void startPeriodicUpdates() {

		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	/**
	 * In response to a request to stop updates, send a request to Location
	 * Services
	 */
	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
	}

}
