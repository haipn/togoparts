package sg.togoparts.pro.gallery;

import java.util.ArrayList;
import java.util.List;

import sg.togoparts.pro.free.DetailActivity;
import sg.togoparts.pro.free.R;
import sg.togoparts.pro.app.Const;
import sg.togoparts.pro.json.Category;
import sg.togoparts.pro.json.Feature;
import sg.togoparts.pro.json.Group;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private Context _context;
	private List<Group> _listDataHeader; // header titles
	// child data in format of header title, child title
	protected ClickViewAll mViewAll;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public MyExpandableAdapter(Context context, List<Group> listDataHeader,
			ClickViewAll view) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		inflater = ((Activity) _context).getLayoutInflater();
		mViewAll = view;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.nophoto)
				.showImageOnFail(R.drawable.nophoto)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	private class ViewHolder {
		public TextView tvTitleGroup;
		public TextView btnBrowseAllAds;
		public TextView tvTitleChild;
		public TextView tvNoAds;
		public TextView tvDescription;
		public HorizontalListView lvFeature;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_marketplace, null);
			holder.tvTitleChild = (TextView) convertView
					.findViewById(R.id.tvTitle);
			holder.tvNoAds = (FontableTextView) convertView
					.findViewById(R.id.tvCount);
			holder.tvDescription = (FontableTextView) convertView
					.findViewById(R.id.tvDescription);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Category cat = (Category) getChild(groupPosition, childPosition);
		holder.tvDescription.setText(cat.mDescription);
		holder.tvNoAds.setText(cat.mTotalAds + " Ads");
		holder.tvTitleChild.setText(cat.mTitle);
		// final ArrayList<Feature> features = (ArrayList<Feature>)
		// getChild(groupPosition, childPosition);
		// RelateAdapter mRelateAdapter = new RelateAdapter(this._context,
		// features);
		// ((HorizontalListView) convertView).setAdapter(mRelateAdapter);
		// ((HorizontalListView) convertView)
		// .setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0,
		// View arg1, int arg2, long arg3) {
		// Intent i = new Intent(
		// MyExpandableAdapter.this._context,
		// DetailActivity.class);
		// i.putExtra(
		// Const.ADS_ID,
		// features.get(arg2).getAid());
		// MyExpandableAdapter.this._context
		// .startActivity(i);
		// }
		// });
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_group, null);
			holder = new ViewHolder();
			holder.btnBrowseAllAds = (TextView) convertView
					.findViewById(R.id.btnViewAll);
			holder.tvTitleGroup = (TextView) convertView
					.findViewById(R.id.tvTitle);
			holder.lvFeature = (HorizontalListView) convertView
					.findViewById(R.id.hlvFeature);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Group group = getGroup(groupPosition);
		if (group.mGroupId == null) {
			holder.btnBrowseAllAds.setVisibility(View.GONE);
		} else {
			holder.btnBrowseAllAds.setVisibility(View.VISIBLE);
		}
		holder.btnBrowseAllAds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewAll.onViewAllClick(group.mGroupId, group.mTitle);
			}
		});
		holder.tvTitleGroup.setText(group.mTitle);
		final ArrayList<Feature> features = group.mFeatures;
		RelateAdapter mRelateAdapter = new RelateAdapter(this._context,
				features);
		holder.lvFeature.setAdapter(mRelateAdapter);
		holder.lvFeature.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(MyExpandableAdapter.this._context,
						DetailActivity.class);
				i.putExtra(Const.ADS_ID, features.get(arg2).getAid());
				MyExpandableAdapter.this._context.startActivity(i);
			}
		});

		ExpandableListView eLV = (ExpandableListView) parent;
		eLV.expandGroup(groupPosition);
		return convertView;
	}

	@Override
	public Group getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataHeader.get(groupPosition).mCategories
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataHeader.get(groupPosition).mCategories.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public interface ClickViewAll {
		public void onViewAllClick(String groupId, String grouptitle);
	}

	private class RelateAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView tvTitle;
			public ImageView image;
			public TextView tvPrice;
		}

		ArrayList<Feature> listAds;
		Context mContext;

		public RelateAdapter(Context context, ArrayList<Feature> cars) {
			super();
			listAds = cars;
			mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.row_feature, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tvTitle);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				holder.tvPrice = (TextView) convertView
						.findViewById(R.id.tvPrice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Feature ads = getItem(position);
			Object tag = holder.image.getTag();
			if (tag == null || !tag.equals(ads.getPicture())) {
				imageLoader.displayImage(ads.getPicture(), holder.image, options);
				holder.image.setTag(ads.getPicture());
			}
			holder.tvTitle.setText(ads.getTitle());
			holder.tvPrice.setText(ads.getPrice());
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAds.size();
		}

		@Override
		public Feature getItem(int arg0) {
			// TODO Auto-generated method stub
			return listAds.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
	}

}