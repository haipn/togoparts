package sg.togoparts;

import java.util.ArrayList;
import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.MyExpandableAdapter;
import sg.togoparts.gallery.MyExpandableAdapter.ClickViewAll;
import sg.togoparts.json.Category;
import sg.togoparts.json.Group;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.MpListCategories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

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

public class MarketPlaceFragment extends Fragment_Main implements ClickViewAll {

	private static final String SCREEN_LABEL = "Marketplace List Categories";
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
				Intent i = new Intent(getActivity(), SearchResultActivity.class);
				Category cat = mAdapter.getChild(groupPosition, childPosition);

				if (cat.mCategoryId == null) {
					i.putExtra(FilterActivity.PARAM,
							mAdapter.getChild(groupPosition, 0).mParameters);
				} else {
					Bundle b = new Bundle();
					b.putString(FilterActivity.MARKETPLACE_CATEGORY,
							cat.mCategoryId);
					i.putExtras(b);
				}
				i.putExtra(Const.TITLE, cat.mTitle);
				i.putExtra(SearchResultActivity.SCREEN_SEARCH_RESULT,
						"Marketplace List Ads By Category");
				startActivity(i);

				return false;
			}
		});
		// mLvGroup.setOnGroupClickListener(new OnGroupClickListener() {
		//
		// @Override
		// public boolean onGroupClick(ExpandableListView parent, View v,
		// int groupPosition, long id) {
		// Intent i = new Intent(getActivity(), SearchResultActivity.class);
		//
		// Group group = mAdapter.getGroup(groupPosition);
		// if (group.mGroupId == null) {
		// i.putExtra(FilterActivity.PARAM,
		// mAdapter.getChild(groupPosition, 0).mParameters);
		// } else {
		// Bundle b = new Bundle();
		// b.putString(FilterActivity.GROUP_ID, group.mGroupId);
		// i.putExtras(b);
		// }
		// i.putExtra(Const.TITLE, group.mTitle);
		// startActivity(i);
		// return true;
		// }
		// });
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("haipn", "onstart marketplace");

		Tracker tracker = GoogleAnalytics.getInstance(getActivity())
				.getTracker(Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onResume() {
		super.onResume();
		AdView adview = (AdView) getActivity().findViewById(R.id.adView);
		AdRequest re = new AdRequest();
		adview.loadAd(re);
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
//				TabsActivityMain tab = (TabsActivityMain) getActivity()
//						.getParent();
//				tab.getTabHost().setCurrentTab(3);
				Intent i = new Intent(getActivity(),
						FSActivity_Search.class);
				startActivity(i);
			}
		});
		headerView.setLogoVisible(View.GONE);
		headerView.setTitleVisible(View.VISIBLE,
				getString(R.string.marketplace));
		headerView.setProgressVisible(View.VISIBLE);
	}

	private Response.Listener<MpListCategories> createMyReqSuccessListener() {
		return new Response.Listener<MpListCategories>() {
			@Override
			public void onResponse(MpListCategories response) {
				mGroups.addAll(response.mGroupList);
				mAdapter.notifyDataSetChanged();
				headerView.setProgressVisible(View.INVISIBLE);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "market place error");
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ErrorDialog newFragment = new ErrorDialog();
				headerView.setProgressVisible(View.INVISIBLE);
				newFragment.show(ft, "error dialog");
			}
		};
	}

	@Override
	public void onViewAllClick(String groupId, String title) {

		Intent i = new Intent(getActivity(), SearchResultActivity.class);
		Bundle b = new Bundle();
		b.putString(FilterActivity.GROUP_ID, groupId);
		i.putExtras(b);
		i.putExtra(Const.TITLE, title);
		i.putExtra(SearchResultActivity.SCREEN_SEARCH_RESULT,
				"Marketplace List Ads By Category");
		startActivity(i);
	}

}
