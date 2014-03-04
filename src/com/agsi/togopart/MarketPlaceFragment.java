package com.agsi.togopart;

import java.util.ArrayList;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.gallery.MyExpandableAdapter;
import com.agsi.togopart.json.Category;
import com.agsi.togopart.json.Group;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.MpListCategories;
import com.agsi.togopart.json.MpListLatestAds;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class MarketPlaceFragment extends Fragment_Main implements
		MyExpandableAdapter.ClickViewAll {

	ExpandableListView mLvGroup;
	protected MyExpandableAdapter mAdapter;
	protected ArrayList<Group> mGroups;
	HeaderView headerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.market_place_layout,
				container, false);
		Log.d("haipn", "oncreate view");
		mLvGroup = (ExpandableListView) rootView.findViewById(R.id.elvGroup);
		mLvGroup.setGroupIndicator(null);
		mAdapter = new MyExpandableAdapter(getActivity(), mGroups, this);
		mLvGroup.setAdapter(mAdapter);
		mLvGroup.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Category cat = mAdapter.getChild(groupPosition, childPosition);
				String extra = "";
				if (cat.mCategoryId == null) {
					extra = "parameters=" + cat.mParameters.adtype;
				} else {
					extra = "cid=" + cat.mCategoryId;
				}

				Fragment fragment;
				fragment = new SearchResultFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Const.QUERY, extra);
				fragment.setArguments(bundle);
				addFragment(fragment, true, SearchResultFragment.class.getName(),
						FragmentTransaction.TRANSIT_NONE);
				return false;
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<MpListCategories> myReq = new GsonRequest<MpListCategories>(
				Method.GET, Const.URL_LIST_CATEGORY, MpListCategories.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		mGroups = new ArrayList<Group>();
		createHeader();
		super.onCreate(savedInstanceState);
	}

	private void createHeader() {
		headerView = (HeaderView) getActivity();
		headerView.setLeftButton(View.INVISIBLE);
		headerView.setRightButton(View.VISIBLE, new OnClickListener() {

			@Override
			public void onClick(View v) {
				TabsActivityMain tab = (TabsActivityMain) getActivity()
						.getParent();
				tab.getTabHost().setCurrentTab(3);
			}
		});
		headerView.setLogoVisible(View.GONE);
		headerView.setTitleVisible(View.VISIBLE,
				getString(R.string.marketplace));
	}

	private Response.Listener<MpListCategories> createMyReqSuccessListener() {
		return new Response.Listener<MpListCategories>() {
			@Override
			public void onResponse(MpListCategories response) {
				mGroups.addAll(response.mGroupList);
				mAdapter.notifyDataSetChanged();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "market place error");

			}
		};
	}

	@Override
	public void onViewAllClick(String groupId) {

		String extra = "gid=" + groupId;

		Fragment fragment;
		fragment = new SearchResultFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Const.QUERY, extra);
		fragment.setArguments(bundle);
		addFragment(fragment, true, SearchResultFragment.class.getName(),
				FragmentTransaction.TRANSIT_NONE);
	}

}
