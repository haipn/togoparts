package sg.togoparts.pro.gallery;

import java.util.ArrayList;

import sg.togoparts.pro.R;
import sg.togoparts.pro.json.CategoryResult.Category;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Category> mListResult;
	int id;
	public CategoryAdapter(Context context, ArrayList<Category> result, int idselected) {
		super();
		mContext = context;
		mListResult = result;
		id = idselected;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListResult.size();
	}

	@Override
	public Category getItem(int arg0) {
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
			holder.tick = (ImageView) convertView.findViewById(R.id.imvSelected);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Category promos = getItem(position);
		if (promos.id == id) {
			holder.tick.setVisibility(View.VISIBLE);
		} else {
			holder.tick.setVisibility(View.GONE);
		}
		holder.label.setText(promos.title);
		return convertView;
	}

	class ViewHolder {
		public TextView label;
		public ImageView tick;
	}

}
