package sg.togoparts.pro.gallery;

import java.util.ArrayList;

import sg.togoparts.pro.R;
import sg.togoparts.pro.json.BikeShop;
import sg.togoparts.pro.json.BikeShop.PinAd;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class BikeShopAdapter extends BaseAdapter {

	static int TYPE_1 = 0;
	static int TYPE_2 = 1;

	Context mContext;
	ArrayList<BikeShop> mListResult;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ButtonClickListener mBtListener;

	public interface ButtonClickListener {
		public void onNewItemsClick(BikeShop shop);

		public void onPromosClick(BikeShop shop);
	}

	public BikeShopAdapter(Context context, ArrayList<BikeShop> result,
			ButtonClickListener btListener) {
		super();
		mBtListener = btListener;
		mContext = context;
		mListResult = result;
		options = new DisplayImageOptions.Builder()
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
	public int getItemViewType(int position) {
		BikeShop shop = getItem(position);
		if (shop.pinad == null)
			return TYPE_1;
		else
			return shop.pinad.index;
	}

	@Override
	public int getViewTypeCount() {
		return 100;
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
		ViewHolder holder;
		final BikeShop shop = getItem(position);
		int type = getItemViewType(position);
		Log.d("haipn", "type:" + type + "  position:" + position);

		if (convertView == null) {
			holder = new ViewHolder();
			if (type == TYPE_1) {
				// inflate
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.row_bikeshop, null);

				holder.title = (TextView) convertView
						.findViewById(R.id.tvTitle);
				holder.address = (TextView) convertView
						.findViewById(R.id.tvAddress);
				holder.remark = (TextView) convertView
						.findViewById(R.id.tvRemark);
				holder.distance = (TextView) convertView
						.findViewById(R.id.tvDistance);
				holder.newItem = (TextView) convertView
						.findViewById(R.id.tvNewItem);
				holder.promos = (TextView) convertView
						.findViewById(R.id.tvPromos);
				holder.openLabel = (TextView) convertView
						.findViewById(R.id.tvOpenLabel);
				holder.logo = (ImageView) convertView
						.findViewById(R.id.imvLogo);
				holder.photo = (ImageView) convertView
						.findViewById(R.id.imvPhoto);
				holder.llOpen = (LinearLayout) convertView
						.findViewById(R.id.llOpen);
				// holder.llAdView = (LinearLayout)
				// convertView.findViewById(R.id.llAd);
				holder.main = (LinearLayout) convertView
						.findViewById(R.id.llMain);
				convertView.setTag(holder);

			} else {
				// ad
				PinAd ad = shop.pinad;
				PublisherAdRequest.Builder request = new PublisherAdRequest.Builder();
				Log.d("haipn", "ad id:" + ad.unit_id);
				AdSize size = new AdSize(ad.unit_width, ad.unit_height);
				convertView = new PublisherAdView( mContext);
				((PublisherAdView)convertView).setAdSizes(size);
				((PublisherAdView)convertView).setAdUnitId(ad.unit_id);
				((PublisherAdView) convertView).loadAd(request.build());
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (type == TYPE_1) {
			setDataShop(holder, shop);
		} 
		return convertView;
	}

	private void setDataShop(final ViewHolder holder, final BikeShop shop) {
		holder.main.setVisibility(View.VISIBLE);
		// holder.llAdView.setVisibility(View.GONE);
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
					int count;
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
					int count;
					try {
						count = Integer
								.valueOf(shop.forpaidonly.new_item_ads);
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
			if (shop.distance != null && !shop.distance.isEmpty()) {
				holder.distance.setText(shop.distance);
				holder.distance.setVisibility(View.VISIBLE);
			} else {
				holder.distance.setVisibility(View.GONE);
			}
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

			if (shop.forpaidonly.shopphoto != null
					&& !shop.forpaidonly.shopphoto.isEmpty()) {
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
				if (shop.forpaidonly.openlabel.contains("OPEN")
						|| shop.forpaidonly.openlabel.contains("Open")
						|| shop.forpaidonly.openlabel.contains("open")) {
					holder.llOpen.setBackgroundColor(mContext
							.getResources().getColor(R.color.green));
					holder.openLabel.setTextColor(mContext.getResources()
							.getColor(android.R.color.white));
					holder.remark.setTextColor(mContext.getResources()
							.getColor(android.R.color.white));

				} else {
					holder.llOpen.setBackgroundColor(mContext
							.getResources().getColor(R.color.closed_shop));
					holder.openLabel.setTextColor(mContext.getResources()
							.getColor(android.R.color.black));
					holder.remark.setTextColor(mContext.getResources()
							.getColor(android.R.color.black));
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
	}

	private void addDfpView(View parent, BikeShop shop) {
		
	}

	static class ViewHolder {
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
		public PublisherAdView dfpView;
	}

}
