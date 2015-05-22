package sg.togoparts.gallery;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.login.Profile.Value;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
					R.layout.row_info, null);
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
		holder.value.setText(Html.fromHtml(promos.value));
		holder.value.setTextColor(mContext.getResources().getColor(R.color.orange));
		return convertView;
	}

	class ViewHolder {
		public TextView label;
		public TextView value;
	}

}
