package sg.togoparts;

import java.util.ArrayList;
import java.util.HashMap;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorInternetDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.BikeShop;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ListBikeShop;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

	public static final String LIST_BIKE_SHOP = "list bike shop";
	protected static final int FILTER_RETURN = 0;
	public static final String IS_SEARCH = "is search";
	public static double Lat  = 1.352083000000000000;
	public static double Long = 103.819836000000010000;
	// Google Map
	private GoogleMap googleMap;
	private ImageButton mBtnBack;
	private ImageButton mBtnSearch;
	private TextView mTvTitleHeader;
	private HashMap<String, String> mHmId;
	private Bundle mQueryBundle;
	private String mQuery;
	private boolean isChange;
	private String mLat;
	private String mLong;
	private ToggleButton mBtnStandard;
	private ToggleButton mBtnHybrid;
	private ToggleButton mBtnSatelite;
	private ProgressBar mProgress;
	public ArrayList<String> mListId;
	private ProgressDialog mProDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		mProDialog = new ProgressDialog(this);
		mHmId = new HashMap<String, String>();
		mQueryBundle = getIntent().getExtras();
		mLat = getIntent().getStringExtra(Const.LATITUDE);
		mLong = getIntent().getStringExtra(Const.LONGITUDE);
		createHeader();
		mBtnHybrid = (ToggleButton) findViewById(R.id.btnHybrid);
		mBtnSatelite = (ToggleButton) findViewById(R.id.btnSate);
		mBtnStandard = (ToggleButton) findViewById(R.id.btnStandard);

		mBtnStandard.setChecked(true);
		mBtnHybrid.setChecked(false);
		mBtnSatelite.setChecked(false);

		mBtnHybrid.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					mBtnSatelite.setChecked(false);
					mBtnStandard.setChecked(false);
				}

			}
		});

		mBtnSatelite.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					mBtnHybrid.setChecked(false);
					mBtnStandard.setChecked(false);
				}
			}
		});

		mBtnStandard.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					mBtnHybrid.setChecked(false);
					mBtnSatelite.setChecked(false);
				}
			}
		});

		try {
			// Loading map
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ArrayList<String> listStringShop = mQueryBundle
		// .getStringArrayList(LIST_BIKE_SHOP);
		// createListPins(listStringShop);
		// Log.d("haipn", "map activity:" + listStringShop.size());
		searchResult(getIntent());

	}

	private class loadPinAsyncTask extends
			AsyncTask<String, Void, ArrayList<MarkerOptions>> {
		protected ArrayList<MarkerOptions> doInBackground(String... args) {
			ArrayList<MarkerOptions> listMo = new ArrayList<MarkerOptions>();
			mListId = new ArrayList<String>();
			if (args == null || args.length == 0) {
				Toast.makeText(MapActivity.this, R.string.no_result_found,
						Toast.LENGTH_LONG).show();
				return null;
			} else {
				Log.d("haipn", "list size map:" + args.length);
			}

			for (String shop : args) {
				if (shop == null)
					continue;
				String[] split = shop.split(":::");
				BikeShop bikeshop = new BikeShop();
				bikeshop.sid = split[0];
				bikeshop.shopname = split[1];
				bikeshop.address = split[2];
				LatLng latlong = new LatLng(Double.valueOf(split[3]),
						Double.valueOf(split[4]));
				MarkerOptions mo = new MarkerOptions();
				mo.anchor(0.7f, 0.6f);
				if (split[6].contains("OPEN") || split[6].contains("Open")
						|| split[6].contains("open")) {
					mo.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				} else if (split[6].contains("CLOSE")
						|| split[6].contains("Close")
						|| split[6].contains("close")) {
					mo.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				} else {

					mo.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
				}
				mo.title(split[1] + split[6]);
				mo.snippet(split[2] + "\n" + split[5]);
				mListId.add(split[0]);
				mo.position(latlong);
				listMo.add(mo);

			}
			return listMo;
		}

		protected void onPostExecute(ArrayList<MarkerOptions> listMo) {

			final LatLngBounds.Builder builder = new LatLngBounds.Builder();
			int i = 0;
			for (MarkerOptions markerOptions : listMo) {
				Marker a = googleMap.addMarker(markerOptions);
				mHmId.put(a.getId(), mListId.get(i++));
				builder.include(a.getPosition());
			}
			LatLngBounds bounds = builder.build();
			int padding = 10; // offset from edges of the map in pixels
			CameraUpdate cu = CameraUpdateFactory
					.newLatLngBounds(bounds, MapActivity.this.getResources()
							.getDisplayMetrics().widthPixels, MapActivity.this
							.getResources().getDisplayMetrics().heightPixels,
							padding);
			googleMap.animateCamera(cu);
			mProDialog.dismiss();
		}
	}

	private void createListPins(ArrayList<String> shops) {
		final LatLngBounds.Builder builder = new LatLngBounds.Builder();
		if (shops == null || shops.size() == 0) {
			Toast.makeText(this, R.string.no_result_found, Toast.LENGTH_LONG)
					.show();
			return;
		}

		for (String shop : shops) {
			String[] split = shop.split(":::");
			BikeShop bikeshop = new BikeShop();
			bikeshop.sid = split[0];
			bikeshop.shopname = split[1];
			bikeshop.address = split[2];
			LatLng latlong = new LatLng(Double.valueOf(split[3]),
					Double.valueOf(split[4]));
			MarkerOptions mo = new MarkerOptions();
			mo.anchor(0.7f, 0.6f);

			if (split[6].contains("OPEN") || split[6].contains("Open")
					|| split[6].contains("open")) {
				mo.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			} else if (split[6].contains("CLOSE") || split[6].contains("Close")
					|| split[6].contains("close")) {
				mo.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			} else {

				mo.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
			}
			mo.title(split[1] + split[6]);
			mo.snippet(split[2] + "\n" + split[5]);
			mo.position(latlong);
			Marker a = googleMap.addMarker(mo);
			mHmId.put(a.getId(), split[0]);
			builder.include(a.getPosition());
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
			CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(
					Double.valueOf(mLat), Double.valueOf(mLong)), 10.0f);
			googleMap.moveCamera(cu);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// Setting a custom info window adapter for the google map
			// CameraPosition cameraPosition = new CameraPosition.Builder()
			// .target(myPosition).zoom(12).build();
			// googleMap.animateCamera(CameraUpdateFactory
			// .newCameraPosition(cameraPosition));
			// Adding and showing marker while touching the GoogleMap
			googleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng arg0) {
					// Clears any existing markers from the GoogleMap
					// Creating an instance of MarkerOptions to set position

					// Animating to the currently touched position
					googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

				}
			});
			googleMap
					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker marker) {
							String shopId = mHmId.get(marker.getId());
							Intent i = new Intent(MapActivity.this,
									BikeShopDetail.class);
							i.putExtra(Const.SHOP_ID, shopId);
							if (googleMap.getMyLocation() != null) {
								mLat = String.valueOf(googleMap.getMyLocation()
										.getLatitude());
								mLong = String.valueOf(googleMap
										.getMyLocation().getLongitude());
							}

							i.putExtra(Const.LATITUDE, mLat);
							i.putExtra(Const.LONGITUDE, mLong);
							startActivity(i);

						}
					});
			googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					View myContentView = getLayoutInflater().inflate(
							R.layout.custom_infowindow, null);
					TextView tvTitle = ((TextView) myContentView
							.findViewById(R.id.title));
					tvTitle.setText(marker.getTitle());
					TextView tvSnippet = ((TextView) myContentView
							.findViewById(R.id.snippet));
					tvSnippet.setText(marker.getSnippet());

					return myContentView;
				}
			});
		} else {
			try {
				CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(
						Double.valueOf(mLat), Double.valueOf(mLong)), 10.0f);
				Log.d("haipn", "mLat:" + mLat + ", mLong:" + mLong);
				googleMap.moveCamera(cu);
			} catch (NumberFormatException e) {
				CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(
						Lat, Long), 10.0f);
				googleMap.moveCamera(cu);
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	protected String[] generateListShop(ArrayList<BikeShop> result) {
		String[] ret = new String[result.size()];
		for (int i = 0; i < ret.length; i++) {
			BikeShop shop = result.get(i);
			if (shop.sid == null)
				continue;
			if (shop.address == null || shop.address.isEmpty())
				continue;
			if (shop.latitude == null || shop.latitude.isEmpty())
				continue;
			if (shop.longitude == null || shop.longitude.isEmpty())
				continue;
			String st = new String();
			st += shop.sid;
			st += ":::";
			st += shop.shopname;
			st += ":::";
			st += shop.address;
			st += ":::";
			st += shop.latitude;
			st += ":::";
			st += shop.longitude;
			st += ":::";
			st += shop.distance != null ? shop.distance : " ";
			st += ":::";
			if (shop.forpaidonly != null && shop.forpaidonly.openlabel != null) {
				st += " - ";
				st += shop.forpaidonly.openlabel;
			} else {
				st += " - ";
				st += "Unknown";
			}
			ret[i] = st;
		}
		return ret;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_CANCELED) {
			Log.d("haipn", "result cancel");
		} else {
			isChange = true;
			mTvTitleHeader.setText(R.string.title_search_bikeshop);
			searchResult(arg2);
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	private void searchResult(Intent i) {
		mProgress.setVisibility(View.VISIBLE);
		mProDialog.show();
		mQueryBundle = i.getExtras();
		if (mQueryBundle.getBoolean(IS_SEARCH)) {
			Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
					Const.GA_PROPERTY_ID);
			tracker.set(Fields.SCREEN_NAME, "Bikeshop Map Search Result");
			tracker.send(MapBuilder.createAppView().build());
		} else {
			Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
					Const.GA_PROPERTY_ID);
			tracker.set(Fields.SCREEN_NAME, "Bikeshop Map");
			tracker.send(MapBuilder.createAppView().build());
		}
		if (mQueryBundle != null)
			getQuery();
		Log.d("haipn", "query:" + mQuery);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ListBikeShop> myReq = new GsonRequest<ListBikeShop>(
				Method.GET, mQuery + "&map=on", ListBikeShop.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);

	}

	private Response.Listener<ListBikeShop> createMyReqSuccessListener() {
		return new Response.Listener<ListBikeShop>() {
			@Override
			public void onResponse(ListBikeShop response) {
				googleMap.clear();
				mProgress.setVisibility(View.GONE);
				if (response != null && response.bikeshoplist != null) {
					String[] listStringShop = generateListShop(response.bikeshoplist);
					// createListPins(listStringShop);
					new loadPinAsyncTask().execute(listStringShop);
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mProgress.setVisibility(View.GONE);
				Log.d("haipn", "error:" + error.networkResponse);
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorInternetDialog newFragment = new ErrorInternetDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}

	private void getQuery() {
		String bikeShop = mQueryBundle.getString(FilterBikeShop.BIKESHOP_NAME);
		if (bikeShop == null) {
			bikeShop = "";
			mQueryBundle.putString(FilterBikeShop.BIKESHOP_NAME, "");
		}
		String type = mQueryBundle.getString(FilterBikeShop.AREA);
		if (type == null) {
			type = "";
			mQueryBundle.putString(FilterBikeShop.AREA, "");
		}

		boolean opennow = mQueryBundle
				.getBoolean(FilterBikeShop.OPENNOW, false);
		boolean mechanic = mQueryBundle.getBoolean(FilterBikeShop.MECHANIC,
				false);
		String open;
		if (opennow) {
			open = "on";

		} else {
			open = "";
		}

		String mechanicStr;
		if (mechanic) {
			mechanicStr = "on";

		} else {
			mechanicStr = "";
		}
		String sort = mQueryBundle.getString(FilterActivity.SORT_BY);

		if (sort == null) {
			sort = "2";
			mQueryBundle.putString(FilterActivity.SORT_BY, "2");
		}

		try {
			if (googleMap.getMyLocation() != null) {
				mLat = String.valueOf(googleMap.getMyLocation().getLatitude());
				mLong = String
						.valueOf(googleMap.getMyLocation().getLongitude());
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mQuery = String.format(Const.URL_BIKE_SHOP, bikeShop, "", type, open,
				mechanicStr, mLat, mLong, sort);
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		mBtnSearch = (ImageButton) findViewById(R.id.btnSearch);
		findViewById(R.id.logo).setVisibility(View.GONE);

		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);

		if (mQueryBundle.getString(Const.TITLE) != null) {
			mTvTitleHeader.setText(getIntent().getStringExtra(Const.TITLE));
		} else {
			mTvTitleHeader.setText(R.string.bikeshop_title);
		}
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mProgress.setVisibility(View.VISIBLE);
		mBtnBack.setBackgroundResource(R.drawable.btn_list);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isChange) {
					Intent i = getIntent();
					i.putExtras(mQueryBundle);
					setResult(RESULT_OK, i);
					googleMap.clear();
					finish();
				} else {
					setResult(RESULT_CANCELED);
					onBackPressed();
				}
			}
		});
		mBtnSearch.setVisibility(View.VISIBLE);
		mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MapActivity.this, FilterBikeShop.class);
				i.putExtras(mQueryBundle);
				startActivityForResult(i, FILTER_RETURN);
			}
		});
	}
}