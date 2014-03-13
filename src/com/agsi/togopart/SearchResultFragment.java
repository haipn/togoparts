package com.agsi.togopart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.gallery.SearchResultAdapter;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.SearchResult;
import com.agsi.togopart.json.SearchResult.AdsResult;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class SearchResultFragment extends Fragment_Main {
	public static final String PAGE_ID = "&page_id=";

	OnFileterSelectedListener mCallback;
	private String mKeyword;
	private Bundle mQueryBundle;
	private String mQuery;
	protected SearchResultAdapter mAdapter;
	private ListView mLvResult;
	private ArrayList<AdsResult> mResult;
	private int mPageId;
	private int mPageTotal;
	protected boolean enableLoadMore;
	private String mStringTitle = "";
	private HeaderView headerView;

	public interface OnFileterSelectedListener {
		public void onFilterSelected(String key);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnFileterSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("haipn", "search result oncreate View");
		View root = inflater.inflate(R.layout.search_result, container, false);
		mLvResult = (ListView) root.findViewById(R.id.lvSearchResult);
		mAdapter = new SearchResultAdapter(getActivity(), mResult);
		mLvResult.setAdapter(mAdapter);
		mLvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Fragment fragment = new DetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Const.ADS_ID, mResult.get(arg2).aid);
				fragment.setArguments(bundle);
				addFragment(fragment, true, FragmentTransaction.TRANSIT_NONE);
			}
		});
		mLvResult.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& enableLoadMore) {
					loadMore();
					enableLoadMore = false;
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

		});
		return root;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void loadMore() {
		if (mPageId >= mPageTotal)
			return;
		mPageId++;
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
				Method.GET, mQuery + PAGE_ID + mPageId, SearchResult.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
	}

	@Override
	public void onResume() {
		Log.d("haipn", "search result on Resume");
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
//		setRetainInstance(true);
		Log.d("haipn", "search result oncreate");
		mKeyword = getArguments().getString(Const.QUERY);
		mQueryBundle = getArguments();
		mPageId = 1;
		if (mQueryBundle != null)
			getQuery();
		else {
			mQuery = String.format(Const.URL_LIST_SEARCH, mKeyword);
		}
		Log.d("haipn", "query:" + mQuery);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
				Method.GET, mQuery + PAGE_ID + mPageId, SearchResult.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		mResult = new ArrayList<AdsResult>();
		createHeader();
		super.onCreate(savedInstanceState);
	}

	private void getQuery() {
		String postBy = mQueryBundle.getString(FilterActivity.POSTED_BY);
		if (postBy == null)
			postBy = "";
		String size = mQueryBundle.getString(FilterActivity.SIZE);
		if (size == null) {
			size = "";
		}
		String value = mQueryBundle.getString(FilterActivity.VALUE);
		if (value == null)
			value = "";
		String unit = mQueryBundle.getString(FilterActivity.UNIT);
		if (unit == null)
			unit = "";
		String from = mQueryBundle.getString(FilterActivity.FROM);
		if (from == null)
			from = "";
		String to = mQueryBundle.getString(FilterActivity.TO);
		if (to == null)
			to = "";
		String category = mQueryBundle.getString(FilterActivity.ADSTATUS);
		if (category == null)
			category = "";
		String market = mQueryBundle.getString(FilterActivity.MARKETPLACE_CATEGORY);
		if (market == null)
			market = "";
		String type = mQueryBundle.getString(FilterActivity.TYPE);
		if (type == null)
			type = "";
		mQuery = String.format(Const.URL_LIST_SEARCH, mKeyword, 
				postBy, 
				size, 
				value,
				unit, 
				from, 
				to, 
				category, 
				market, 
				type);
		
	}

	private void createHeader() {
		headerView = (HeaderView) getActivity();
		headerView.setLeftButton(View.VISIBLE);
		headerView.setRightButton(View.VISIBLE, new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onFilterSelected(mKeyword);
			}
		});
		headerView.setLogoVisible(View.INVISIBLE);
		headerView.setTitleVisible(View.VISIBLE,
				mStringTitle.isEmpty() ? getString(R.string.search_result)
						: mStringTitle);
	}

	private Response.Listener<SearchResult> createMyReqSuccessListener() {
		return new Response.Listener<SearchResult>() {
			@Override
			public void onResponse(SearchResult response) {
				mResult.addAll(response.ads);
				mAdapter.notifyDataSetChanged();
				mPageTotal = response.page_details.total_ads;
				enableLoadMore = true;
				// mLvResult.setAdapter(mAdapter);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
			}
		};
	}
}
