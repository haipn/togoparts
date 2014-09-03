package sg.togoparts.gallery;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.json.SectionResult.Section;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SectionAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Section> mListResult;
	int id;
	int adType;
	int mTcred;

	public SectionAdapter(Context context, ArrayList<Section> result,
			int idSelect, int type, int tCred) {
		super();
		mContext = context;
		mListResult = result;
		id = idSelect;
		adType = type;
		mTcred = tCred;
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
			holder.tick = (ImageView) convertView
					.findViewById(R.id.imvSelected);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Section promos = getItem(position);
		if (promos.id == id) {
			holder.tick.setVisibility(View.VISIBLE);
		} else {
			holder.tick.setVisibility(View.GONE);
		}
		holder.label.setText(promos.data.title);
		if (adType == 1) {
			if (promos.data != null && promos.data.priority_cost <= mTcred) {
				// ((ListView) parent).getChildAt(position).setEnabled(true);
				holder.label.setEnabled(true);
			} else {
				// ((ListView) parent).getChildAt(position).setEnabled(false);
				holder.label.setEnabled(false);
			}
		} else if (adType == 2) {
			if (promos.data != null && promos.data.newitem_cost <= mTcred) {
				// ((ListView) parent).getChildAt(position).setEnabled(true);
				holder.label.setEnabled(true);
			} else {
				// ((ListView) parent).getChildAt(position).setEnabled(false);
				holder.label.setEnabled(false);
			}
		} else {
			// ((ListView) parent).getChildAt(position).setEnabled(true);
			holder.label.setEnabled(true);
		}
		return convertView;
	}

	public boolean isEnabled(int position) {
		Section promos = getItem(position);
		if (adType == 1) {
			if (promos.data != null && promos.data.priority_cost <= mTcred) {
				return true;
			} else {
				return false;
			}
		} else if (adType == 2) {
			if (promos.data != null && promos.data.newitem_cost <= mTcred) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	class ViewHolder {
		public TextView label;
		public ImageView tick;
	}

}
