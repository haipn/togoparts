package sg.togoparts.gallery;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.gallery.SearchResultAdapter.ViewHolder;
import sg.togoparts.json.BikeShop;
import sg.togoparts.json.SearchResult.AdsResult;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class BikeShopAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<BikeShop> mListResult;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public BikeShopAdapter(Context context, ArrayList<BikeShop> result) {
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
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.address = (TextView) convertView
					.findViewById(R.id.tvAddress);
			holder.tel = (TextView) convertView.findViewById(R.id.tvTel);
			holder.distance = (TextView) convertView
					.findViewById(R.id.tvDistance);
			holder.newItem = (TextView) convertView
					.findViewById(R.id.tvNewItem);
			holder.promos = (TextView) convertView.findViewById(R.id.tvPromos);
			// holder.priority = (TextView) convertView
			// .findViewById(R.id.tvPriority);
			holder.openLabel = (TextView) convertView
					.findViewById(R.id.tvOpenLabel);
			holder.logo = (ImageView) convertView.findViewById(R.id.imvLogo);
			holder.photo = (ImageView) convertView.findViewById(R.id.imvPhoto);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BikeShop shop = getItem(position);
		if (shop.forpaidonly == null) {
			holder.promos.setVisibility(View.GONE);
			holder.distance.setVisibility(View.GONE);
			holder.logo.setVisibility(View.GONE);
			holder.photo.setVisibility(View.GONE);
			holder.openLabel.setVisibility(View.GONE);
			holder.newItem.setVisibility(View.GONE);
		} else {
			holder.promos.setVisibility(View.VISIBLE);
			holder.distance.setVisibility(View.VISIBLE);
			holder.logo.setVisibility(View.VISIBLE);
			holder.photo.setVisibility(View.VISIBLE);
			holder.openLabel.setVisibility(View.VISIBLE);
			holder.newItem.setVisibility(View.VISIBLE);

			holder.promos.setText(mContext.getString(R.string.promos,
					shop.forpaidonly.promos));
			holder.distance.setText(shop.distance);
			imageLoader.displayImage(shop.shoplogo, holder.logo, options);
			imageLoader.displayImage(shop.forpaidonly.shopphoto, holder.photo, options);
			holder.openLabel.setText(shop.forpaidonly.remarks);
			holder.newItem.setText(mContext.getString(R.string.new_item, shop.forpaidonly.new_item_ads));
			
		}
		holder.address.setText(shop.address);
		holder.title.setText(shop.shopname);

		return convertView;
	}

	class ViewHolder {
		public TextView title;
		public TextView address;
		public TextView tel;
		public TextView distance;
		// public TextView priority;
		public TextView openLabel;
		public TextView newItem;
		public TextView promos;
		public ImageView photo;
		public ImageView logo;
	}

}
