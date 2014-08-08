package sg.togoparts.gallery;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.json.AdsDetail.Attribute;
import sg.togoparts.json.ListPromos.Promos;
import sg.togoparts.login.Profile.Value;
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

public class InfoAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Value> mListResult;

	public InfoAdapter(Context context, ArrayList<Value> result) {
		super();
		mContext = context;
		mListResult = result;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListResult.size();
	}

	@Override
	public Value getItem(int arg0) {
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
					R.layout.row_attribute, null);
			holder = new ViewHolder();
			holder.label = (TextView) convertView.findViewById(R.id.tvLabel);
			holder.value = (TextView) convertView
					.findViewById(R.id.tvValue);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Value promos = getItem(position);
		holder.label.setText(promos.label);
		holder.label.setTextColor(mContext.getResources().getColor(android.R.color.black));
		holder.value.setText(promos.value + "");
		holder.value.setTextColor(mContext.getResources().getColor(R.color.orange));
		return convertView;
	}

	class ViewHolder {
		public TextView label;
		public TextView value;
	}

}
