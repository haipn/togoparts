package sg.togoparts.gallery;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.json.BikeShop;
import sg.togoparts.json.BikeShop.PinAd;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.doubleclick.DfpAdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class BikeShopAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<BikeShop> mListResult;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ButtonClickListener mBtListener;
	
	public interface ButtonClickListener {
		public void onNewItemsClick(BikeShop shop);
		public void onPromosClick(BikeShop shop);
	}
	
	public BikeShopAdapter(Context context, ArrayList<BikeShop> result, ButtonClickListener btListener) {
		super();
		mBtListener = btListener;
		mContext = context;
		mListResult = result;
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListResult.size();
	}

	@Override
	public BikeShop getItem(int arg0) {
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
					R.layout.row_bikeshop, null);
			holder = new ViewHolder();
			holder.main = (LinearLayout) convertView.findViewById(R.id.llMain);
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.address = (TextView) convertView
					.findViewById(R.id.tvAddress);
			holder.remark = (TextView) convertView.findViewById(R.id.tvRemark);
			holder.distance = (TextView) convertView
					.findViewById(R.id.tvDistance);
			holder.newItem = (TextView) convertView
					.findViewById(R.id.tvNewItem);
			holder.promos = (TextView) convertView.findViewById(R.id.tvPromos);
			holder.openLabel = (TextView) convertView
					.findViewById(R.id.tvOpenLabel);
			holder.logo = (ImageView) convertView.findViewById(R.id.imvLogo);
			holder.photo = (ImageView) convertView.findViewById(R.id.imvPhoto);
			holder.llOpen = (LinearLayout) convertView
					.findViewById(R.id.llOpen);
			holder.adView = (LinearLayout) convertView.findViewById(R.id.llAd);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final BikeShop shop = getItem(position);
		if (shop.pinad == null) { // bikeshop normal
			holder.main.setVisibility(View.VISIBLE);
			holder.adView.setVisibility(View.GONE);
			if (shop.forpaidonly == null || shop.forpaidonly.promos == null) {
				holder.promos.setVisibility(View.GONE);
				holder.distance.setVisibility(View.GONE);
				holder.logo.setVisibility(View.GONE);
				holder.photo.setVisibility(View.GONE);
				holder.llOpen.setVisibility(View.GONE);
				holder.newItem.setVisibility(View.GONE);
			} else {
				holder.promos.setVisibility(View.VISIBLE);
				holder.promos.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int count ;
						try {
							count = Integer.valueOf(shop.forpaidonly.promos);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return;
						} catch (NullPointerException e) {
							e.printStackTrace();
							return;
						} 
						if (count == 0)
							return;
						mBtListener.onPromosClick(shop);
					}
				});
				holder.distance.setVisibility(View.VISIBLE);
				holder.logo.setVisibility(View.VISIBLE);
				holder.photo.setVisibility(View.VISIBLE);
				holder.llOpen.setVisibility(View.VISIBLE);
				holder.newItem.setVisibility(View.VISIBLE);
				holder.newItem.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int count ;
						try {
							count = Integer.valueOf(shop.forpaidonly.new_item_ads);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return;
						} catch (NullPointerException e) {
							e.printStackTrace();
							return;
						} 
						if (count == 0)
							return;
						mBtListener.onNewItemsClick(shop);
					}
				});

				holder.promos.setText(mContext.getString(R.string.promos,
						shop.forpaidonly.promos));
				holder.distance.setText(shop.distance);
				if (shop.shoplogo != null && !shop.shoplogo.isEmpty()) {
					holder.logo.setVisibility(View.VISIBLE);
					Object tag = holder.logo.getTag();
					if (tag == null || !tag.equals(shop.shoplogo)) {
						imageLoader.displayImage(shop.shoplogo, holder.logo,
								options, new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String arg0,
											View arg1) {
									}

									@Override
									public void onLoadingFailed(String arg0,
											View arg1, FailReason arg2) {
										holder.logo.setVisibility(View.GONE);
									}

									@Override
									public void onLoadingComplete(String arg0,
											View arg1, Bitmap arg2) {
										holder.logo.setVisibility(View.VISIBLE);
									}

									@Override
									public void onLoadingCancelled(String arg0,
											View arg1) {
										holder.logo.setVisibility(View.GONE);
									}
								});
						holder.logo.setTag(shop.shoplogo);
					}
				} else {
					holder.logo.setVisibility(View.GONE);
				}
				
				if (shop.forpaidonly.shopphoto != null && !shop.forpaidonly.shopphoto.isEmpty()) {
					holder.photo.setVisibility(View.VISIBLE);
					Object tagPhoto = holder.photo.getTag();
					if (tagPhoto == null
							|| !tagPhoto.equals(shop.forpaidonly.shopphoto)) {
						imageLoader.displayImage(shop.forpaidonly.shopphoto,
								holder.photo, options,
								new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String arg0,
											View arg1) {
									}

									@Override
									public void onLoadingFailed(String arg0,
											View arg1, FailReason arg2) {
										holder.photo.setVisibility(View.GONE);
									}

									@Override
									public void onLoadingComplete(String arg0,
											View arg1, Bitmap arg2) {
										holder.photo
												.setVisibility(View.VISIBLE);
									}

									@Override
									public void onLoadingCancelled(String arg0,
											View arg1) {
										holder.photo.setVisibility(View.GONE);
									}
								});
						holder.photo.setTag(shop.forpaidonly.shopphoto);
					}
				} else {
					holder.photo.setVisibility(View.GONE);
				}
				holder.openLabel.setText(shop.forpaidonly.openlabel);

				if (shop.forpaidonly.openlabel != null
						&& !shop.forpaidonly.openlabel.isEmpty()) {
					if (shop.forpaidonly.openlabel.contains("OPEN")) {
						holder.llOpen.setBackgroundColor(mContext
								.getResources().getColor(R.color.green));
						holder.openLabel.setTextColor(mContext.getResources().getColor(android.R.color.white));
						holder.remark.setTextColor(mContext.getResources().getColor(android.R.color.white));
						
					} else {
						holder.llOpen.setBackgroundColor(mContext
								.getResources().getColor(R.color.closed_shop));
						holder.openLabel.setTextColor(mContext.getResources().getColor(android.R.color.black));
						holder.remark.setTextColor(mContext.getResources().getColor(android.R.color.black));
					}
					holder.llOpen.setVisibility(View.VISIBLE);
				} else
					holder.llOpen.setVisibility(View.GONE);
				holder.remark.setText(shop.forpaidonly.remarks);
				holder.newItem.setText(mContext.getString(R.string.new_item,
						shop.forpaidonly.new_item_ads));

			}
			holder.address.setText(shop.address);
			holder.title.setText(shop.shopname);

		} else { // pinad
			holder.main.setVisibility(View.GONE);
			holder.adView.setVisibility(View.VISIBLE);
			PinAd ad = shop.pinad;
			AdSize size = new AdSize(ad.unit_width, ad.unit_height);
			DfpAdView adView = new DfpAdView((Activity) mContext, size,
					ad.unit_id);
			AdRequest request = new AdRequest();
			adView.loadAd(request);
			if (holder.adView.getChildCount() == 0)
				holder.adView.addView(adView);
		}
		return convertView;
	}

	class ViewHolder {
		public LinearLayout main;
		public TextView title;
		public TextView address;
		public TextView remark;
		public TextView distance;
		public LinearLayout llOpen;
		public TextView openLabel;
		public TextView newItem;
		public TextView promos;
		public ImageView photo;
		public ImageView logo;
		public LinearLayout adView;
	}

}
