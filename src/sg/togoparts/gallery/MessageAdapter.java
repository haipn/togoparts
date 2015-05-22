package sg.togoparts.gallery;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.json.AdsDetail.Message;
import android.content.Context;
import android.graphics.Bitmap;
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

public class MessageAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Message> mListResult;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public MessageAdapter(Context context, ArrayList<Message> result) {
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
	public Message getItem(int arg0) {
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
					R.layout.row_comment, null);
			holder = new ViewHolder();
			holder.user = (TextView) convertView.findViewById(R.id.tvUser);
			holder.comment = (TextView) convertView
					.findViewById(R.id.tvComment);
			holder.datepost = (TextView) convertView.findViewById(R.id.tvDate);
			holder.thumb = (ImageView) convertView.findViewById(R.id.imvAvatar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Message promos = getItem(position);
		holder.user.setText(promos.username);
		holder.comment.setText(promos.message);
		holder.datepost.setText(promos.datesent);
		imageLoader.displayImage(promos.picture, holder.thumb, options);
		return convertView;
	}

	class ViewHolder {
		public TextView user;
		public TextView comment;
		public TextView datepost;
		public ImageView thumb;
	}

}
