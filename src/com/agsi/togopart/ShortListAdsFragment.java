package com.agsi.togopart;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.ErrorDialog;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.gallery.SearchResultAdapter;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.SearchResult;
import com.agsi.togopart.json.SearchResult.AdsResult;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ShortListAdsFragment extends Fragment_Main {

	protected SearchResultAdapter mAdapter;
	private ListView mLvResult;
	private ArrayList<AdsResult> mResult;
	private HeaderView headerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.shortlist_fragment, container,
				false);
		mResult = new ArrayList<AdsResult>();
		mLvResult = (ListView) root.findViewById(R.id.lvSearchResult);
		mAdapter = new SearchResultAdapter(getActivity(), mResult);
		mLvResult.setAdapter(mAdapter);

		mLvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(getActivity(), DetailActivity.class);

				i.putExtra(Const.ADS_ID, mResult.get(arg2).aid);
				startActivity(i);
			}
		});

		createHeader();
		return root;
	}

	@Override
	public void onStart() {
		SharedPreferences pref = Const.getTogoPartsPreferences(getActivity());
		String stringAds = pref.getString(Const.KEY_SHORTLIST, "");
		if (stringAds.startsWith("+"))
			stringAds.substring(1);
		
		mResult.clear();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
				Method.GET, String.format(Const.URL_SHORTLIST, stringAds),
				SearchResult.class, createMyReqSuccessListener(),
				createMyReqErrorListener());
		queue.add(myReq);
		Log.d("haipn",
				"shortlist ads:"
						+ String.format(Const.URL_SHORTLIST, stringAds));
		headerView.setProgressVisible(View.VISIBLE);
		super.onStart();
	}

	private void createHeader() {
		headerView = (HeaderView) getActivity();
		headerView.setLeftButton(View.VISIBLE);
		headerView.setLogoVisible(View.GONE);
		headerView.setTitleVisible(View.VISIBLE, getString(R.string.shortlist));
		headerView.setRightButton(View.VISIBLE, new OnClickListener() {

			@Override
			public void onClick(View v) {
				TabsActivityMain tab = (TabsActivityMain) getActivity()
						.getParent();
				tab.getTabHost().setCurrentTab(3);
			}
		});
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	private Response.Listener<SearchResult> createMyReqSuccessListener() {
		return new Response.Listener<SearchResult>() {
			@Override
			public void onResponse(SearchResult response) {
				mResult.addAll(response.ads);
				mAdapter.notifyDataSetChanged();
				headerView.setProgressVisible(View.INVISIBLE);
				// mLvResult.setAdapter(mAdapter);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ErrorDialog newFragment = new ErrorDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}
}
