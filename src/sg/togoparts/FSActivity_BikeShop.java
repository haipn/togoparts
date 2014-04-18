package sg.togoparts;

import java.util.ArrayList;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorDialog;
import sg.togoparts.app.MyLocation;
import sg.togoparts.app.MyLocation.LocationResult;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.BikeShopAdapter;
import sg.togoparts.json.BikeShop;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ListBikeShop;
import android.app.TabActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FSActivity_BikeShop extends FragmentActivity implements
		LocationResult, BikeShopAdapter.ButtonClickListener {

	public static final String PAGE_ID = "&page_id=";

	protected static final int FILTER_RETURN = 0;
	public static final String SCREEN_SEARCH_RESULT = "screen name";
	private Bundle mQueryBundle;
	private String mQuery;
	protected BikeShopAdapter mAdapter;
	private PullToRefreshListView mLvResult;
	private ArrayList<BikeShop> mResult;
	private int mPageId;
	private int mPageTotal;
	protected boolean enableLoadMore = false;
	private String mStringTitle = "";
	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;
	ProgressBar mProgress;
	TextView mTvNoResult;
	String mScreenName = "Bike shop";
	Intent mIntent;
	String mLat = "0.0";
	String mLong = "0.0";

	protected void loadMore() {

		mPageId++;
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ListBikeShop> myReq = new GsonRequest<ListBikeShop>(
				Method.GET, mQuery + PAGE_ID + mPageId, ListBikeShop.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		// Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
		// Const.GA_PROPERTY_ID);
		// tracker.set(Fields.SCREEN_NAME, mScreenName + " More");
		// tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (Const.isAppExitable) {
			return false;
			// } else
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onResume() {
		AdView adview = (AdView) findViewById(R.id.adView);
		adview.setVisibility(View.GONE);
		// AdRequest re = new AdRequest();
		// adview.loadAd(re);
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setRetainInstance(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);

		mLvResult = (PullToRefreshListView) findViewById(R.id.lvSearchResult);
		mLvResult.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mPageId = 1;
				enableLoadMore = false;
				searchResult(mIntent);
				// Tracker tracker =
				// GoogleAnalytics.getInstance(FSActivity_BikeShop.this).getTracker(
				// Const.GA_PROPERTY_ID);
				// tracker.set(Fields.SCREEN_NAME, mScreenName +
				// " Pull Refresh");
				// tracker.send(MapBuilder.createAppView().build());
			}
		});
		mLvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mResult.get(arg2 - 1).sid == null) {

				} else {
					Intent i = new Intent(FSActivity_BikeShop.this,
							BikeShopDetail.class);
					i.putExtra(Const.SHOP_ID, mResult.get(arg2 - 1).sid);
					Log.d("haipn", "shop id:" + mResult.get(arg2 - 1).sid);
					i.putExtra(Const.LATITUDE, mLat);
					i.putExtra(Const.LONGITUDE, mLong);
					startActivity(i);
				}
			}
		});
		mLvResult.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.d("haipn", "padid: " + mPageId + " page total:"
						+ mPageTotal);
				if (mPageId >= mPageTotal)
					return;
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& enableLoadMore) {
					loadMore();
					enableLoadMore = false;
					mProgress.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

		});
		mTvNoResult = (TextView) findViewById(R.id.tvNoResult);
		mIntent = getIntent();
		mResult = new ArrayList<BikeShop>();
		mAdapter = new BikeShopAdapter(this, mResult, this);
		mLvResult.setAdapter(mAdapter);

		createHeader();
		MyLocation myLocation = new MyLocation();
		if (!myLocation.getLocation(this, this)) {
			mLat = "";
			mLong = "";
			searchResult(mIntent);
		}
	}

	private void searchResult(Intent i) {
		// mScreenName = i.getStringExtra(SCREEN_SEARCH_RESULT);
		// Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
		// Const.GA_PROPERTY_ID);
		// tracker.set(Fields.SCREEN_NAME, mScreenName);
		// tracker.send(MapBuilder.createAppView().build());

		mQueryBundle = i.getExtras();
		mPageId = 1;
		if (mQueryBundle != null)
			getQuery();
		Log.d("haipn", "query:" + mQuery);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ListBikeShop> myReq = new GsonRequest<ListBikeShop>(
				Method.GET, mQuery + PAGE_ID + mPageId, ListBikeShop.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		Log.d("haipn", "page id:" + mPageId);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == FILTER_RETURN) {
			if (arg1 == RESULT_CANCELED) {
				Log.d("haipn", "result cancel");
			} else {
				mResult.clear();
				enableLoadMore = false;
				mIntent = arg2;
				searchResult(arg2);
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
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

		mQuery = String.format(Const.URL_BIKE_SHOP, bikeShop, "", type, open,
				mechanicStr, mLat, mLong, sort);
	}

	private void createHeader() {
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);
		mBtnRight = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mIvLogo.setVisibility(View.GONE);
		mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setVisibility(View.VISIBLE);

		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TabActivity tabs = (TabActivity) getParent();
				tabs.getTabHost().setCurrentTabByTag("1");
			}
		});
		mBtnRight.setBackgroundResource(R.drawable.btn_filter);
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(FSActivity_BikeShop.this,
						FilterBikeShop.class);
				i.putExtras(mQueryBundle);
				startActivityForResult(i, FILTER_RETURN);
			}
		});
		mProgress = (ProgressBar) findViewById(R.id.progressbar);
		mProgress.setVisibility(View.VISIBLE);
		if (getIntent().getStringExtra(Const.TITLE) != null) {
			mTvTitle.setText(getIntent().getStringExtra(Const.TITLE));
		} else {
			mTvTitle.setText(R.string.bikeshop_title);
		}
	}

	private Response.Listener<ListBikeShop> createMyReqSuccessListener() {
		return new Response.Listener<ListBikeShop>() {
			@Override
			public void onResponse(ListBikeShop response) {
				if (response.bikeshoplist == null
						|| response.bikeshoplist.size() == 0) {
					mLvResult.setVisibility(View.GONE);
					mTvNoResult.setVisibility(View.VISIBLE);
					enableLoadMore = false;
				} else {
					if (mPageId <= 1)
						mResult.clear();

					mLvResult.setVisibility(View.VISIBLE);
					mResult.addAll(response.bikeshoplist);
					mAdapter.notifyDataSetChanged();
					mPageTotal = response.page_details.no_of_pages;
					enableLoadMore = true;
					mTvNoResult.setVisibility(View.GONE);
				}
				mLvResult.onRefreshComplete();
				mProgress.setVisibility(View.GONE);
				// mLvResult.setAdapter(mAdapter);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorDialog newFragment = new ErrorDialog();
				newFragment.show(ft, "error dialog");
				enableLoadMore = true;
				mProgress.setVisibility(View.GONE);
			}
		};
	}

	@Override
	public void gotLocation(Location location) {
		Log.d("haipn",
				"lat:" + location.getLatitude() + " \nlong:"
						+ location.getLongitude());
		mLat = String.valueOf(location.getLatitude());
		mLong = String.valueOf(location.getLongitude());
		searchResult(mIntent);
	}

	@Override
	public void onNewItemsClick(BikeShop shop) {

		Intent i = new Intent(FSActivity_BikeShop.this,
				SearchResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(FilterActivity.SHOP_NAME, shop.sid);
		i.putExtras(bundle);
		i.putExtra(Const.TITLE, "All Ads by " + shop.shopname);
		startActivity(i);
	}

	@Override
	public void onPromosClick(BikeShop shop) {
		Intent i = new Intent(FSActivity_BikeShop.this,
				ListPromosActivity.class);
		i.putExtra(Const.SHOP_ID, shop.sid);
		startActivity(i);
	}

}
