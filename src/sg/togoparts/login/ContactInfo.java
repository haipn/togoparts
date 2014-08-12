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
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class ContactInfo extends FragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	EditText mEdtContactPerson;
	EditText mEdtContactNo;
	Spinner mSpnBestTime;
	EditText mEdtLocation;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_info);
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
		mEdtLocation = (EditText) findViewById(R.id.edtLocation);
		mSpnBestTime = (Spinner) findViewById(R.id.spnBestTime);
		mBtnSave = (Button) findViewById(R.id.btnSave);

		mEdtContactNo.setText(mContactNo);
		mEdtContactPerson.setText(mContactPerson);
		mEdtLocation.setText(mAddress + "," + mCity + "," + mRegion + ","
				+ mCountry + "," + mPostalCode);

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
		mUpdatesRequested = false;

		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(this, this, this);
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

	/**
	 * In response to a request to stop updates, send a request to Location
	 * Services
	 */
	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
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

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	/**
	 * An AsyncTask that calls getFromLocation() in the background. The class
	 * uses the following generic types: Location - A
	 * {@link android.location.Location} object containing the current location,
	 * passed as the input parameter to doInBackground() Void - indicates that
	 * progress units are not used by this subclass String - An address passed
	 * to onPostExecute()
	 */
	protected class GetAddressTask extends AsyncTask<Location, Void, ArrayList<Address>> {

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
		protected ArrayList<Address> doInBackground(Location... params) {
			/*
			 * Get a new geocoding service instance, set for localized
			 * addresses. This example uses android.location.Geocoder, but other
			 * geocoders that conform to address standards can also be used.
			 */
			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

			// Get the current location from the input parameter list
			Location location = params[0];

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
				addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 10);

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

				// Construct a message containing the invalid arguments
				String errorString = getString(
						R.string.illegal_argument_exception,
						location.getLatitude(), location.getLongitude());
				// Log the error and print the stack trace
				Log.e(LocationUtils.APPTAG, errorString);
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
			
		}
	}
}
