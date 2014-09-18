package sg.togoparts.pro.gallery;

import java.util.ArrayList;

import sg.togoparts.pro.R;
import sg.togoparts.pro.json.ListPromos.Promos;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PromosAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Promos> mListResult;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public PromosAdapter(Context context, ArrayList<Promos> result) {
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
	public Promos getItem(int arg0) {
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
					R.layout.row_promos, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.shopname = (TextView) convertView
					.findViewById(R.id.tvShopName);
			holder.datepost = (TextView) convertView
					.findViewById(R.id.tvDatePost);
//			holder.thumb = (ImageView) convertView.findViewById(R.id.ivThumb);
			holder.content = (WebView) convertView.findViewById(R.id.wvPromos);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Promos promos = getItem(position);
		holder.title.setText(promos.title);
		holder.shopname.setText(promos.shopname);
		holder.datepost.setText(promos.dateposted);
//		imageLoader.displayImage(promos.thumbnail, holder.thumb, options);
		holder.content.loadData(promos.content, "text/html", "UTF-8");
		return convertView;
	}

	class ViewHolder {
		public TextView title;
		public TextView shopname;
		public TextView datepost;
		public ImageView thumb;
		public WebView content;
	}

}
