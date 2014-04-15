package sg.togoparts.gallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.togoparts.R;
import sg.togoparts.json.BikeShop.PinAd;
import sg.togoparts.json.SearchResult.AdsResult;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.doubleclick.DfpAdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class SearchResultAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<AdsResult> mListResult;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	DisplayImageOptions optionsLogo;

	public SearchResultAdapter(Context context, ArrayList<AdsResult> result) {
		super();
		mContext = context;
		mListResult = result;
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.showImageOnFail(R.drawable.nophoto)
				.showImageForEmptyUri(R.drawable.nophoto).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		optionsLogo = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListResult.size();
	}

	@Override
	public AdsResult getItem(int arg0) {
		// TODO Auto-generated method stub
		return mListResult.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.row_search_result, null);
			holder = new ViewHolder();
			holder.dateAndPostedBy = (TextView) convertView
					.findViewById(R.id.tvDateAndPostedBy);
			holder.firm = (TextView) convertView.findViewById(R.id.tvFirm);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.logo = (ImageView) convertView
					.findViewById(R.id.ibCompanyLogo);
			holder.mailCount = (TextView) convertView
					.findViewById(R.id.tvMailCount);
			holder.newItem = (TextView) convertView
					.findViewById(R.id.tvNewItem);
			holder.price = (TextView) convertView.findViewById(R.id.tvPrice);
//			holder.priority = (TextView) convertView
//					.findViewById(R.id.tvPriority);
			holder.special = (TextView) convertView
					.findViewById(R.id.tvSpecial);
			holder.status = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.viewCount = (TextView) convertView
					.findViewById(R.id.tvViewCount);
			holder.llAdView = (LinearLayout) convertView.findViewById(R.id.llAd);
			holder.llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AdsResult ads = getItem(position);
		if (ads.pinad == null) { //normal row
			holder.llAdView.setVisibility(View.GONE);
			holder.llMain.setVisibility(View.VISIBLE);
			holder.title.setText(ads.title);
			holder.firm.setText(ads.firm_neg);
			Object tag = holder.image.getTag();
			if (tag == null || !tag.equals(ads.picture)) {
				imageLoader.displayImage(ads.picture, holder.image, options);
				holder.image.setTag(ads.picture);
			}
			Object tagShop = holder.logo.getTag();
			if (tagShop == null
					|| !tagShop.equals(ads.merchant_details.shop_logo)) {
				imageLoader.displayImage(ads.merchant_details.shop_logo,
						holder.logo, optionsLogo);
				holder.logo.setTag(ads.merchant_details.shop_logo);
			}
			holder.dateAndPostedBy.setText(ads.dateposted + "\nPosted by "
					+ ads.postedby);
			holder.mailCount.setText(ads.msg_sent);
			if (ads.listinglabel != null && !ads.listinglabel.isEmpty()) {
				if (ads.listinglabel.equalsIgnoreCase("priority")) {
					holder.newItem.setTextColor(mContext.getResources()
							.getColor(android.R.color.black));
					holder.newItem.setBackgroundColor(mContext.getResources()
							.getColor(R.color.yellow));
				} else {
					holder.newItem.setTextColor(mContext.getResources()
							.getColor(android.R.color.white));
					holder.newItem.setBackgroundColor(mContext.getResources()
							.getColor(R.color.dark_blue));
				}
				holder.newItem.setText(ads.listinglabel);
				holder.newItem.setVisibility(View.VISIBLE);
			} else {
				holder.newItem.setVisibility(View.INVISIBLE);
			}
			holder.price.setText(ads.price);
			// holder.priority.setText()
			if (ads.special != null && ads.special.textcolor != null) {
				holder.special.setText(ads.special.text);
				holder.special.setTextColor(Color
						.parseColor(ads.special.textcolor));
				holder.special.setBackgroundColor(Color
						.parseColor(ads.special.bgcolor));
				holder.special.setVisibility(View.VISIBLE);
			} else {
				holder.special.setVisibility(View.GONE);
			}
			if (ads.adstatus != null && ads.adstatus.equalsIgnoreCase("sold")) {
				holder.status.setText("Sold");
				holder.status.setTextColor(mContext.getResources().getColor(
						R.color.red));
			} else {
				holder.status.setText(ads.adstatus);
				holder.status.setTextColor(mContext.getResources().getColor(
						R.color.green));
			}
			holder.viewCount.setText(ads.ad_views);
		} else {
			holder.llAdView.setVisibility(View.VISIBLE);
			holder.llMain.setVisibility(View.GONE);
			PinAd ad = ads.pinad;
			AdSize size = new AdSize(ad.unit_width, ad.unit_height);
			DfpAdView adView = new DfpAdView((Activity) mContext, size,
					ad.unit_id);
			AdRequest request = new AdRequest();
			adView.loadAd(request);
			if (holder.llAdView.getChildCount() == 0)
				holder.llAdView.addView(adView);
		}
		return convertView;
	}

	class ViewHolder {
		public TextView title;
		public TextView price;
		public TextView viewCount;
		public TextView mailCount;
//		public TextView priority;
		public TextView newItem;
		public TextView special;
		public TextView firm;
		public TextView status;
		public TextView dateAndPostedBy;
		public ImageView image;
		public ImageView logo;
		public LinearLayout llMain;
		public LinearLayout llAdView;
	}
}