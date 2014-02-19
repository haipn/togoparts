package com.agsi.togopart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.gallery.FontableTextView;
import com.agsi.togopart.json.Ads;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.ListCategories;
import com.agsi.togopart.json.ListCategories.Cat;
import com.agsi.togopart.json.MpListLatestAds;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	FontableTextView mTvTitle;
	ListView mLvAds;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container,
				false);
		mTvTitle = (FontableTextView) rootView.findViewById(R.id.tvTitle);
		mLvAds = (ListView) rootView.findViewById(R.id.lvAds);

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<MpListLatestAds> myReq = new GsonRequest<MpListLatestAds>(
				Method.GET, Const.URL_LIST_LASTEST_ADS, MpListLatestAds.class,
				createMyReqSuccessListener(), createMyReqErrorListener());

		queue.add(myReq);
		super.onCreate(savedInstanceState);
	}

	private Response.Listener<MpListLatestAds> createMyReqSuccessListener() {
		return new Response.Listener<MpListLatestAds>() {
			@Override
			public void onResponse(MpListLatestAds response) {
				LastestAdsAdapter adapter = new LastestAdsAdapter(getActivity(), 0, response.mAds);
				mLvAds.setAdapter(adapter);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO: show dialog error
			}
		};
	}

	private class LastestAdsAdapter extends ArrayAdapter<Ads> {

		private class ViewHolder {
			public FontableTextView tvTime;
			public FontableTextView tvStatus;
			public FontableTextView tvDes;
			public FontableTextView tvPrice;
			public FontableTextView tvPostedBy;
		}

		ArrayList<Ads> listAds;
		
		public LastestAdsAdapter(Context context, int text, ArrayList<Ads> cars) {
			super(context, 0, cars);
			listAds = cars;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.row_home, null);
				holder = new ViewHolder();
				holder.tvTime = (FontableTextView) view
						.findViewById(R.id.tvTime);
				holder.tvStatus = (FontableTextView) view
						.findViewById(R.id.tvStatus);
				holder.tvPrice = (FontableTextView) view
						.findViewById(R.id.tvPrice);
				holder.tvDes = (FontableTextView) view
						.findViewById(R.id.tvDescription);
				holder.tvPostedBy = (FontableTextView) view
						.findViewById(R.id.tvPostedBy);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			Ads ads = listAds.get(position);
			holder.tvTime.setText(ads.getTimeposted());
			holder.tvStatus.setText(ads.getTranstype());
			holder.tvDes.setText(ads.getTitle());
			holder.tvPrice.setText(ads.getPrice());
			holder.tvPostedBy.setText(ads.getPostedby());

			return view;
		}
	}
}
