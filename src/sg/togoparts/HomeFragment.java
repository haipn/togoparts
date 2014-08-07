package sg.togoparts;

import java.util.ArrayList;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.FontableTextView;
import sg.togoparts.gallery.MyExpandableAdapter;
import sg.togoparts.gallery.MyExpandableAdapter.ClickViewAll;
import sg.togoparts.json.Ads;
import sg.togoparts.json.Category;
import sg.togoparts.json.Group;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.MpListCategories;
import sg.togoparts.json.MpListLatestAds;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;

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
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class HomeFragment extends Fragment_Main implements ClickViewAll {
	private static final String SAVED_BUNDLE_TAG = "saved bundle";
	private static final String SCREEN_LABEL = "Home";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	FontableTextView mTvTitle;
	// ListView mLvAds;
	PullToRefreshGridView mGvAds;
	protected ArrayList<Ads> mListAds;
	protected String mTitle;
	private LastestAdsAdapter mAdsAdapter;
	private HeaderView headerView;

	ExpandableListView mLvGroup;
	protected MyExpandableAdapter mAdapter;
	protected ArrayList<Group> mGroups;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container,
				false);

		mTvTitle = (FontableTextView) rootView.findViewById(R.id.tvTitle);
		mGvAds = (PullToRefreshGridView) rootView.findViewById(R.id.gvAds);
		mGvAds.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				updateAds();
			}
		});
		mAdsAdapter = new LastestAdsAdapter(getActivity(), mListAds);
		mGvAds.setAdapter(mAdsAdapter);
		mGvAds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(getActivity(), DetailActivity.class);
				i.putExtra(Const.ADS_ID, mListAds.get(arg2).getAdsId());
				startActivity(i);
			}
		});
		mTvTitle.setText(mTitle);
		Log.d("haipn", "on create view");

		mLvGroup = (ExpandableListView) rootView.findViewById(R.id.elvGroup);
		mLvGroup.setGroupIndicator(null);
		mAdapter = new MyExpandableAdapter(getActivity(), mGroups, this);
		mLvGroup.setAdapter(mAdapter);
		mLvGroup.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent i = new Intent(getActivity(), SearchResultActivity.class);
				Category cat = (Category) mAdapter.getChild(groupPosition, childPosition);

				if (cat.mCategoryId == null) {
					i.putExtra(FilterActivity.PARAM,
							cat.mParameters);
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
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		Log.d("haipn", "on create");
		updateAds();
		createHeader();
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.showImageOnFail(R.drawable.nophoto)
				.showImageForEmptyUri(R.drawable.nophoto)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<MpListCategories> myReq = new GsonRequest<MpListCategories>(
				Method.GET, Const.URL_LIST_CATEGORY, MpListCategories.class,
				createMarketPlaceSuccessListener(), createMarketPlaceErrorListener());
		queue.add(myReq);
		mGroups = new ArrayList<Group>();
		super.onCreate(savedInstanceState);
	}

	private void updateAds() {
		mListAds = new ArrayList<Ads>();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<MpListLatestAds> myReq = new GsonRequest<MpListLatestAds>(
				Method.GET, Const.URL_LIST_LASTEST_ADS, MpListLatestAds.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
	}

	private void createHeader() {
		headerView = (HeaderView) getActivity();
		headerView.setLeftButton(View.INVISIBLE);
		headerView.setRightButton(View.VISIBLE, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TabsActivityMain tab = (TabsActivityMain) getActivity()
				// .getParent();
				// tab.getTabHost().setCurrentTab(3);
				Intent i = new Intent(getActivity(), FSActivity_Search.class);
				startActivity(i);
			}
		});
		headerView.setProgressVisible(View.VISIBLE);
	}

	@Override
	public void onStart() {
		Log.d("haipn", "onstart home");

		super.onStart();
		Tracker tracker = GoogleAnalytics.getInstance(getActivity())
				.getTracker(Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onResume() {
		super.onResume();
		Const.isAppExitable = true;
		AdView adview = (AdView) getActivity().findViewById(R.id.adView);
		AdRequest re = new AdRequest();
		adview.loadAd(re);
	}

	private Response.Listener<MpListLatestAds> createMyReqSuccessListener() {
		return new Response.Listener<MpListLatestAds>() {
			@Override
			public void onResponse(MpListLatestAds response) {
				mListAds.addAll(response.mAds);
				// mAdsAdapter = new LastestAdsAdapter(getActivity(), mListAds);
				// mLvAds.setAdapter(mAdsAdapter);
				mAdsAdapter.notifyDataSetChanged();
				mTitle = response.mTitle;
				mTvTitle.setText(response.mTitle);
				headerView.setProgressVisible(View.INVISIBLE);
				mGvAds.onRefreshComplete();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "home error");
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ErrorDialog newFragment = new ErrorDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}
	private Response.Listener<MpListCategories> createMarketPlaceSuccessListener() {
		return new Response.Listener<MpListCategories>() {
			@Override
			public void onResponse(MpListCategories response) {
				mGroups.addAll(response.mGroupList);
				mAdapter.notifyDataSetChanged();
				headerView.setProgressVisible(View.INVISIBLE);
			}
		};
	}

	private Response.ErrorListener createMarketPlaceErrorListener() {
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

	private class LastestAdsAdapter extends BaseAdapter {

		private class ViewHolder {
			public ImageView image;
			public FontableTextView tvTime;
			public FontableTextView tvTitle;
			public FontableTextView tvPrice;
			public FontableTextView tvPostedBy;
		}

		ArrayList<Ads> listAds;
		Context mContext;

		public LastestAdsAdapter(Context context, ArrayList<Ads> cars) {
			super();
			listAds = cars;
			mContext = context;
		}

		public void setList(ArrayList<Ads> mListAds) {
			listAds = mListAds;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.item_grid_home, null);
				holder = new ViewHolder();
				holder.tvTime = (FontableTextView) convertView
						.findViewById(R.id.tvTimePost);
				holder.tvPrice = (FontableTextView) convertView
						.findViewById(R.id.tvPrice);
				holder.tvTitle = (FontableTextView) convertView
						.findViewById(R.id.tvTitle);
				holder.tvPostedBy = (FontableTextView) convertView
						.findViewById(R.id.tvPostBy);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Ads ads = getItem(position);
			holder.tvTime.setText(ads.getTimeposted());
			if (ads.getTitle() != null)
				holder.tvTitle.setText(Html.fromHtml(ads.getTitle()));
			holder.tvPrice.setText(ads.getPrice());
			holder.tvPostedBy.setText(ads.getPostedby());
			imageLoader.displayImage(ads.getPicture(), holder.image, options);
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAds.size();
		}

		@Override
		public Ads getItem(int arg0) {
			// TODO Auto-generated method stub
			return listAds.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
	}

}
