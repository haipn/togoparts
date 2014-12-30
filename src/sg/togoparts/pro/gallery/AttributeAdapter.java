package sg.togoparts.pro.gallery;

import java.util.ArrayList;

import sg.togoparts.pro.free.R;
import sg.togoparts.pro.json.AdsDetail.Attribute;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AttributeAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Attribute> mListResult;

	public AttributeAdapter(Context context, ArrayList<Attribute> result) {
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
	public Attribute getItem(int arg0) {
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
		Attribute promos = getItem(position);
		holder.label.setText(promos.label);
		holder.value.setText(promos.value);
		return convertView;
	}

	class ViewHolder {
		public TextView label;
		public TextView value;
	}

}
