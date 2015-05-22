package sg.togoparts;

import java.util.ArrayList;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorInternetDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.SearchResultAdapter;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.SearchResult;
import sg.togoparts.json.SearchResult.AdsResult;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter.DeleteItemCallback;

public class ShortListAdsFragment extends Fragment_Main implements
		DeleteItemCallback {

	private static final String SCREEN_LABEL = "Marketplace Shortlisted Ads";
	protected BaseAdapter mAdapter;
	private ListView mLvResult;
	private ArrayList<AdsResult> mResult;

	private HeaderView headerView;
	protected TextView mTvNoShortlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.shortlist_fragment, container,
				false);
		mResult = new ArrayList<AdsResult>();
		mLvResult = (ListView) root.findViewById(R.id.lvSearchResult);
		mTvNoShortlist = (TextView) root.findViewById(R.id.tvNoResult);
		mAdapter = new SearchResultAdapter(getActivity(), mResult);
		ContextualUndoAdapter adapter = new ContextualUndoAdapter(mAdapter,
				R.layout.undo_row, R.id.undo_row_undobutton, 3000, this);
		adapter.setAbsListView(mLvResult);
		mLvResult.setAdapter(adapter);

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

		Log.d("haipn", "onstart ShortListAds");

		Tracker tracker = GoogleAnalytics.getInstance(getActivity())
				.getTracker(Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		tracker.send(MapBuilder.createAppView().build());
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			SharedPreferences pref = Const.getTogoPartsPreferences(getActivity());
			String stringAds = pref.getString(Const.KEY_SHORTLIST, "");
			stringAds = stringAds.trim();
			stringAds = stringAds.replaceAll("\\s", "+");
			mResult.clear();
			RequestQueue queue = MyVolley.getRequestQueue();
			GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
					Method.GET, String.format(Const.URL_SHORTLIST, stringAds),
					SearchResult.class, createMyReqSuccessListener(),
					createMyReqErrorListener());
			queue.add(myReq);
			headerView.setProgressVisible(View.VISIBLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final PublisherAdView adview = (PublisherAdView) getActivity().findViewById(R.id.adView);
		PublisherAdRequest.Builder re = new PublisherAdRequest.Builder();
		adview.loadAd(re.build());
		adview.setVisibility(View.GONE);
		adview.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				adview.setVisibility(View.VISIBLE);
				super.onAdLoaded();
			}
		});
	}

	private void createHeader() {
		headerView = (HeaderView) getActivity();
		headerView.setLeftButton(View.VISIBLE);
		headerView.setTitleVisible(View.VISIBLE, getString(R.string.shortlist));
		headerView.setRightButton(View.VISIBLE, new OnClickListener() {

			@Override
			public void onClick(View v) {
//				TabsActivityMain tab = (TabsActivityMain) getActivity()
//						.getParent();
//				tab.getTabHost().setCurrentTab(3);
				Intent i = new Intent(getActivity(),
						SearchActivity.class);
				startActivity(i);
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
				if (response != null && response.ads != null && !response.ads.isEmpty()) {
					mResult.addAll(response.ads);
					mAdapter.notifyDataSetChanged();
					mTvNoShortlist.setVisibility(View.GONE);
					mLvResult.setVisibility(View.VISIBLE);
				} else {
					mTvNoShortlist.setVisibility(View.VISIBLE);
					mLvResult.setVisibility(View.GONE);
				}
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
				ErrorInternetDialog newFragment = new ErrorInternetDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}

	@Override
	public void deleteItem(int arg0) {
		Const.removeFromShortList(getActivity(), mResult.get(arg0).aid);
		mResult.remove(arg0);
		mAdapter.notifyDataSetChanged();
	}

}
