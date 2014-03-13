package com.agsi.togopart;

import java.util.ArrayList;

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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.ErrorDialog;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.gallery.FontableTextView;
import com.agsi.togopart.json.Ads;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.MpListLatestAds;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class HomeFragment extends Fragment_Main {
	private static final String SAVED_BUNDLE_TAG = "saved bundle";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	FontableTextView mTvTitle;
	// ListView mLvAds;
	GridView mGvAds;
	protected ArrayList<Ads> mListAds;
	protected String mTitle;
	private LastestAdsAdapter mAdsAdapter;
	private HeaderView headerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container,
				false);

		mTvTitle = (FontableTextView) rootView.findViewById(R.id.tvTitle);
		mGvAds = (GridView) rootView.findViewById(R.id.gvAds);
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
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		Log.d("haipn", "on create");
		mListAds = new ArrayList<Ads>();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<MpListLatestAds> myReq = new GsonRequest<MpListLatestAds>(
				Method.GET, Const.URL_LIST_LASTEST_ADS, MpListLatestAds.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		createHeader();
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
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
		headerView.setProgressVisible(View.VISIBLE);
	}

	@Override
	public void onStart() {
		Log.d("haipn", "on start");
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		Const.isAppExitable = true;
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
