package sg.togoparts.gallery;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.json.SectionResult.Section;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SectionAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Section> mListResult;

	public SectionAdapter(Context context, ArrayList<Section> result) {
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
	public Section getItem(int arg0) {
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
					R.layout.row_section, null);
			holder = new ViewHolder();
			holder.label = (TextView) convertView.findViewById(R.id.textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Section promos = getItem(position);
		holder.label.setText(promos.data.title);
		return convertView;
	}

	class ViewHolder {
		public TextView label;
	}

}
