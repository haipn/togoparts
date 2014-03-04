package com.agsi.togopart;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.gallery.SearchResultAdapter;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.SearchResult;
import com.agsi.togopart.json.SearchResult.AdsResult;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

public class ShortListAdsFragment extends Fragment_Main {

	protected SearchResultAdapter mAdapter;
	private ListView mLvResult;
	private ArrayList<AdsResult> mResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.search_result, container, false);
		mResult = new ArrayList<AdsResult>();
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
		
		SharedPreferences pref = Const.getTogoPartsPreferences(getActivity());
		String stringAds = pref.getString(Const.KEY_SHORTLIST, "");
		if (stringAds.startsWith("+"))
			stringAds.substring(1);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
				Method.GET, String.format(Const.URL_SHORTLIST, stringAds),
				SearchResult.class, createMyReqSuccessListener(),
				createMyReqErrorListener());
		queue.add(myReq);
		return root;
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
