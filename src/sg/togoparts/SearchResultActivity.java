package sg.togoparts;

import java.util.ArrayList;
import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.SearchResultAdapter;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.SearchResult;
import sg.togoparts.json.SearchResult.AdsResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SearchResultActivity extends FragmentActivity {

	public static final String PAGE_ID = "&page_id=";

	protected static final int FILTER_RETURN = 0;
	public static final String SCREEN_SEARCH_RESULT = "screen name";
	private String mKeyword = "";
	private Bundle mQueryBundle;
	private String mQuery;
	protected SearchResultAdapter mAdapter;
	private PullToRefreshListView mLvResult;
	private ArrayList<AdsResult> mResult;
	private int mPageId;
	private int mPageTotal;
	protected boolean enableLoadMore = false;
	private String mStringTitle = "";
	private String mParameter;
	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;
	ProgressBar mProgress;
	TextView mTvNoResult;
	String mScreenName;
	Intent mIntent;

	protected void loadMore() {

		mPageId++;
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
				Method.GET, mQuery + PAGE_ID + mPageId, SearchResult.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, mScreenName + " More");
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onResume() {
		AdView adview = (AdView) findViewById(R.id.adView);
		AdRequest re = new AdRequest();
		adview.loadAd(re);
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
				Tracker tracker = GoogleAnalytics.getInstance(SearchResultActivity.this).getTracker(
						Const.GA_PROPERTY_ID);
				tracker.set(Fields.SCREEN_NAME, mScreenName + " Pull Refresh");
				tracker.send(MapBuilder.createAppView().build());
			}
		});
		mLvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(SearchResultActivity.this,
						DetailActivity.class);
				Log.d("haipn", "item:" + arg2);
				i.putExtra(Const.ADS_ID, mResult.get(arg2 - 1).aid);
				Log.d("haipn", "id:" + mResult.get(arg2 - 1).aid);
				startActivity(i);
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
		mResult = new ArrayList<AdsResult>();
		mAdapter = new SearchResultAdapter(this, mResult);
		mLvResult.setAdapter(mAdapter);
		searchResult(mIntent);
		createHeader();

	}

	private void searchResult(Intent i) {
		mScreenName = i.getStringExtra(SCREEN_SEARCH_RESULT);
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, mScreenName);
		tracker.send(MapBuilder.createAppView().build());

		mParameter = i.getStringExtra(FilterActivity.PARAM);
		mQueryBundle = i.getExtras();
		if (i.getStringExtra(FilterActivity.KEYWORD) != null)
			mKeyword = i.getStringExtra(FilterActivity.KEYWORD);
		mPageId = 1;
		if (mQueryBundle != null)
			getQuery();
		else {
			mQuery = String.format(Const.URL_LIST_SEARCH, mParameter);
		}
		Log.d("haipn", "query:" + mQuery);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
				Method.GET, mQuery + PAGE_ID + mPageId, SearchResult.class,
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
		String postBy = mQueryBundle.getString(FilterActivity.POSTED_BY);
		if (postBy == null) {
			postBy = "";
			mQueryBundle.putString(FilterActivity.POSTED_BY, "");
		}
		String size = mQueryBundle.getString(FilterActivity.SIZE);
		if (size == null) {
			size = "";
			mQueryBundle.putString(FilterActivity.SIZE, "");
		}
		String value = mQueryBundle.getString(FilterActivity.VALUE);
		if (value == null) {
			value = "";
			mQueryBundle.putString(FilterActivity.VALUE, "");
		}
		String unit = mQueryBundle.getString(FilterActivity.UNIT);
		if (unit == null) {
			unit = "";
			mQueryBundle.putString(FilterActivity.UNIT, "");
		}
		String from = mQueryBundle.getString(FilterActivity.FROM);
		if (from == null) {
			from = "";
			mQueryBundle.putString(FilterActivity.FROM, "");
		}
		String to = mQueryBundle.getString(FilterActivity.TO);
		if (to == null) {
			to = "";
			mQueryBundle.putString(FilterActivity.TO, "");
		}
		String adStatus = mQueryBundle.getString(FilterActivity.ADSTATUS);
		if (adStatus == null) {
			adStatus = "";
			mQueryBundle.putString(FilterActivity.ADSTATUS, "");
		}
		String market = mQueryBundle
				.getString(FilterActivity.MARKETPLACE_CATEGORY);
		if (market == null) {
			market = "";
			mQueryBundle.putString(FilterActivity.MARKETPLACE_CATEGORY, "");
		}
		String type = mQueryBundle.getString(FilterActivity.TYPE);
		if (type == null) {
			type = "";
			mQueryBundle.putString(FilterActivity.TYPE, "");
		}

		String shopname = mQueryBundle.getString(FilterActivity.SHOP_NAME);
		if (shopname == null) {
			shopname = "";
			mQueryBundle.putString(FilterActivity.SHOP_NAME, "");
		}

		String sort = mQueryBundle.getString(FilterActivity.SORT_BY);

		if (sort == null) {
			sort = "3";
			mQueryBundle.putString(FilterActivity.SORT_BY, "3");
		}

		String group = mQueryBundle.getString(FilterActivity.GROUP_ID);
		if (group == null) {
			mQuery = String.format(Const.URL_LIST_SEARCH, String.format(
					Const.URL_LIST_SEARCH_PARAM, mKeyword, postBy, size, value,
					unit, from, to, adStatus, market, type, shopname, sort));
		} else {
			mQuery = String.format(Const.URL_LIST_SEARCH, String.format(
					Const.URL_LIST_SEARCH_PARAM, mKeyword, postBy, size, value,
					unit, from, to, adStatus, market, type, shopname, sort))
					+ "&gid=" + group;
		}
		if (mParameter != null)
			mQuery = mQuery + "&" + mParameter;
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
				onBackPressed();
			}
		});
		mBtnRight.setBackgroundResource(R.drawable.btn_filter);
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SearchResultActivity.this,
						FilterActivity.class);
				i.putExtras(mQueryBundle);
				startActivityForResult(i, FILTER_RETURN);
			}
		});
		mProgress = (ProgressBar) findViewById(R.id.progressbar);
		mProgress.setVisibility(View.VISIBLE);
		if (getIntent().getStringExtra(Const.TITLE) != null) {
			mTvTitle.setText(getIntent().getStringExtra(Const.TITLE));
		} else {
			mTvTitle.setText(R.string.search_result);
		}
	}

	private Response.Listener<SearchResult> createMyReqSuccessListener() {
		return new Response.Listener<SearchResult>() {
			@Override
			public void onResponse(SearchResult response) {
				Log.d("haipn", "respse: " + response.ads.size());
				if (response.ads == null || response.ads.size() == 0) {
					mLvResult.setVisibility(View.GONE);
					mTvNoResult.setVisibility(View.VISIBLE);
					enableLoadMore = false;
				} else {
					if (mPageId <= 1)
						mResult.clear();
					
					mLvResult.setVisibility(View.VISIBLE);
					mResult.addAll(response.ads);
					mAdapter.notifyDataSetChanged();
					mPageTotal = response.page_details.no_of_pages;
					Log.d("haipn", "page id:" + mPageId + " page total:"
							+ mPageTotal);
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

}