package sg.togoparts.login;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.json.SearchResult.AdsResult;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class AdProfileAdapter extends BaseAdapter {

	private static final int TYPE_1 = 0;
	Context mContext;
	ArrayList<AdsResult> mListResult;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	DisplayImageOptions optionsLogo;

	public interface QuickActionSelect {
		public void onRepostClick(String aid);

		public void onTakeDownClick(String aid);

		public void onMarkAsSoldClick(String aid);

		public void onRefreshClick(String aid);
	}

	QuickActionSelect mCallback;

	public AdProfileAdapter(Context context, ArrayList<AdsResult> result,
			QuickActionSelect callback) {
		super();
		mContext = context;
		mListResult = result;
		mCallback = callback;
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
		AdsResult ads = getItem(position);
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.package_row, null);
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
			// holder.priority = (TextView) convertView
			// .findViewById(R.id.tvPriority);
			holder.special = (TextView) convertView
					.findViewById(R.id.tvSpecial);
			holder.status = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.viewCount = (TextView) convertView
					.findViewById(R.id.tvViewCount);
			holder.llAdView = (LinearLayout) convertView
					.findViewById(R.id.llAd);
			holder.llMain = (LinearLayout) convertView
					.findViewById(R.id.llMain);
			holder.btnTakeDown = (Button) convertView
					.findViewById(R.id.btnTakeDown);
			holder.btnMarkAsSold = (Button) convertView
					.findViewById(R.id.btnMarkAsSold);
			holder.btnRefresh = (Button) convertView
					.findViewById(R.id.btnRefresh);
			holder.btnRepost = (Button) convertView
					.findViewById(R.id.btnRepost);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		((SwipeListView) parent).recycle(convertView, position);
		setDataAds(holder, ads);
		return convertView;
	}

	private void setDataAds(ViewHolder holder, final AdsResult ads) {
		// normal row
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
		if (tagShop == null || !tagShop.equals(ads.merchant_details.shop_logo)) {
			imageLoader.displayImage(ads.merchant_details.shop_logo,
					holder.logo, optionsLogo);
			holder.logo.setTag(ads.merchant_details.shop_logo);
		}
		holder.dateAndPostedBy.setText(ads.dateposted + "\nPosted by "
				+ ads.postedby);
		holder.mailCount.setText(ads.msg_sent);
		if (ads.listinglabel != null && !ads.listinglabel.isEmpty()) {
			if (ads.listinglabel.equalsIgnoreCase("priority")) {
				holder.newItem.setTextColor(mContext.getResources().getColor(
						android.R.color.black));
				holder.newItem.setBackgroundColor(mContext.getResources()
						.getColor(R.color.yellow));
				holder.btnRepost.setVisibility(View.GONE);
				holder.btnMarkAsSold.setVisibility(View.VISIBLE);
				holder.btnRefresh.setVisibility(View.VISIBLE);
				holder.btnTakeDown.setVisibility(View.GONE);
			} else if (ads.listinglabel.equalsIgnoreCase("new item")) {
				holder.newItem.setTextColor(mContext.getResources().getColor(
						android.R.color.white));
				holder.newItem.setBackgroundColor(mContext.getResources()
						.getColor(R.color.dark_blue));
				if (ads.adstatus != null
						&& ads.adstatus.equalsIgnoreCase("expired")) {
					holder.btnRepost.setVisibility(View.VISIBLE);
					holder.btnMarkAsSold.setVisibility(View.GONE);
					holder.btnRefresh.setVisibility(View.GONE);
					holder.btnTakeDown.setVisibility(View.GONE);

				} else {
					holder.btnRepost.setVisibility(View.GONE);
					holder.btnMarkAsSold.setVisibility(View.GONE);
					holder.btnRefresh.setVisibility(View.VISIBLE);
					holder.btnTakeDown.setVisibility(View.VISIBLE);
				}
			}
			holder.newItem.setText(ads.listinglabel);
			holder.newItem.setVisibility(View.VISIBLE);
		} else {

			holder.newItem.setVisibility(View.INVISIBLE);

			holder.btnRepost.setVisibility(View.GONE);
			holder.btnMarkAsSold.setVisibility(View.VISIBLE);
			holder.btnRefresh.setVisibility(View.VISIBLE);
			holder.btnTakeDown.setVisibility(View.GONE);
		}
		holder.price.setText(ads.price);
		// holder.priority.setText()
		if (ads.special != null && ads.special.textcolor != null) {
			holder.special.setText(ads.special.text);
			holder.special
					.setTextColor(Color.parseColor(ads.special.textcolor));
			holder.special.setBackgroundColor(Color
					.parseColor(ads.special.bgcolor));
			holder.special.setVisibility(View.VISIBLE);
		} else {
			holder.special.setVisibility(View.GONE);
		}
		holder.status.setText(ads.adstatus);
		if (ads.adstatus != null
				&& (ads.adstatus.equalsIgnoreCase("available")
						|| ads.adstatus.equalsIgnoreCase("looking") || ads.adstatus
							.equalsIgnoreCase("for exchange"))) {
			holder.status.setTextColor(mContext.getResources().getColor(
					R.color.green));
			if (ads.adstatus.equalsIgnoreCase("available")) {
				if (ads.adtype.equalsIgnoreCase("for sale"))
					holder.btnMarkAsSold
							.setBackgroundResource(R.drawable.mark_as_sold);
				else if (ads.adtype.equalsIgnoreCase("free")) {
					holder.btnMarkAsSold
							.setBackgroundResource(R.drawable.mark_as_given);
				}
			} else if (ads.adstatus.equalsIgnoreCase("looking")) {
				holder.btnMarkAsSold
						.setBackgroundResource(R.drawable.mark_as_found);
			} else if (ads.adstatus.equalsIgnoreCase("for exchange")) {
				holder.btnMarkAsSold
						.setBackgroundResource(R.drawable.mark_as_exchange);
			}

		} else {
			holder.status.setTextColor(mContext.getResources().getColor(
					R.color.red));
		}
		holder.viewCount.setText(ads.ad_views);

		holder.btnMarkAsSold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onMarkAsSoldClick(ads.aid);
			}
		});
		holder.btnTakeDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onTakeDownClick(ads.aid);
			}
		});
		holder.btnRepost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onRepostClick(ads.aid);
			}
		});
		holder.btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onRefreshClick(ads.aid);
			}
		});

	}

	class ViewHolder {
		public TextView title;
		public TextView price;
		public TextView viewCount;
		public TextView mailCount;
		// public TextView priority;
		public TextView newItem;
		public TextView special;
		public TextView firm;
		public TextView status;
		public TextView dateAndPostedBy;
		public ImageView image;
		public ImageView logo;
		public LinearLayout llMain;
		public LinearLayout llAdView;

		public Button btnTakeDown;
		public Button btnMarkAsSold;
		public Button btnRefresh;
		public Button btnRepost;
	}
}
