package com.agsi.togopart.gallery;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.agsi.togopart.R;
import com.agsi.togopart.json.Category;
import com.agsi.togopart.json.Group;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private Context _context;
	private List<Group> _listDataHeader; // header titles
	private ClickViewAll mViewAll;
	// child data in format of header title, child title

	public MyExpandableAdapter(Context context, List<Group> listDataHeader, ClickViewAll viewAll) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		inflater = ((Activity) _context).getLayoutInflater();
		mViewAll = viewAll;
	}

	private class ViewHolder {
		public TextView tvTitleGroup;
		public TextView btnBrowseAllAds;
		public TextView tvTitleChild;
		public TextView tvNoAds;
		public TextView tvDescription;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_marketplace, null);
			holder = new ViewHolder();
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
		Category cat = getChild(groupPosition, childPosition);
		holder.tvDescription.setText(cat.mDescription);
		holder.tvNoAds.setText(cat.mTotalAds + " Ads");
		holder.tvTitleChild.setText(cat.mTitle);
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
				mViewAll.onViewAllClick(group.mGroupId);
			}
		});
		holder.tvTitleGroup.setText(group.mTitle);
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
	public Category getChild(int groupPosition, int childPosititon) {
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
		public void onViewAllClick(String groupId);
	}
}