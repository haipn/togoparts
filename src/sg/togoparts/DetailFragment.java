package sg.togoparts;

import java.util.ArrayList;

import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.AttributeAdapter;
import sg.togoparts.gallery.HorizontalListView;
import sg.togoparts.gallery.MessageAdapter;
import sg.togoparts.json.Ads;
import sg.togoparts.json.AdsDetail;
import sg.togoparts.json.AdsDetail.Attribute;
import sg.togoparts.json.AdsDetail.Message;
import sg.togoparts.json.AdsDetail.Picture;
import sg.togoparts.json.GsonRequest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class DetailFragment extends Fragment_Main {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private TextView mTvTitle;
	private TextView mTvPrice;
	private TextView mTvPriceDiff;
	private TextView mTvFirm;

	private TextView mTvAddShortList;
	private TextView mTvShare;
	private ViewPager mPagerImage;
	private TextView mTvSpecial;
	private TextView mTvListingLabel;
	private TextView mTvPriority;
	private TextView mTvAdType;

	private TextView mTvDescription;
	private TextView mTvSize;
	private TextView mTvPostebyAndPostTime;
	private TextView mTvAllPostedBy;
	private ImageView mIvShopLogo;

	private TextView mTvShopOrContact;
	private TextView mTvNameContact;

	private TextView mTvContactNo;
	private TextView mTvAddress;

	private TextView mTvViewCount;
	private TextView mTvMailCount;

	private TextView mTvSMS;
	private TextView mTvCall;
	private TextView mTvEmail;
	private HorizontalListView mHlRelate;

	private GridView mGvAttribute;
	private AttributeAdapter mAttributeAdapter;
	private ArrayList<Attribute> mListAttribute;
	private String mAdsId;

	private RelateAdapter mRelateAdapter;
	private ArrayList<Ads> mListRelateAds;

	private ListView mLvMessage;
	private ArrayList<Message> mListMessage;
	private MessageAdapter mMsgAdapter;
	private LinearLayout mLlMessage;
	private TextView mTvViewAllMsg;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rv = inflater.inflate(R.layout.detail, container, false);

		mTvTitle = (TextView) rv.findViewById(R.id.tvTitle);
		mTvPrice = (TextView) rv.findViewById(R.id.tvPrice);
		mTvPriceDiff = (TextView) rv.findViewById(R.id.tvPriceDiff);
		mTvFirm = (TextView) rv.findViewById(R.id.tvFirm);

		mTvAddShortList = (TextView) rv.findViewById(R.id.btnAddShortList);
		mTvShare = (TextView) rv.findViewById(R.id.btnShare);
		mPagerImage = (ViewPager) rv.findViewById(R.id.pager);

		mTvSpecial = (TextView) rv.findViewById(R.id.tvSpecial);
		mTvListingLabel = (TextView) rv.findViewById(R.id.tvListinglabel);
		// mTvPriority = (TextView) rv.findViewById(R.id.tvPriority);
		mTvAdType = (TextView) rv.findViewById(R.id.tvAdTypes);

		mTvDescription = (TextView) rv.findViewById(R.id.tvDescription);
		mTvSize = (TextView) rv.findViewById(R.id.tvSize);
		mTvPostebyAndPostTime = (TextView) rv
				.findViewById(R.id.tvPostebyAndPostTime);
		mTvAllPostedBy = (TextView) rv.findViewById(R.id.btnAllPostedBy);
		mIvShopLogo = (ImageView) rv.findViewById(R.id.imvShopLogo);

		mTvShopOrContact = (TextView) rv.findViewById(R.id.tvShopOrContact);
		mTvNameContact = (TextView) rv.findViewById(R.id.tvNameContact);
		mTvContactNo = (TextView) rv.findViewById(R.id.tvContactNo);
		mTvAddress = (TextView) rv.findViewById(R.id.tvAddress);

		mTvViewCount = (TextView) rv.findViewById(R.id.tvViewCount);
		mTvMailCount = (TextView) rv.findViewById(R.id.tvMailCount);
		mTvSMS = (TextView) rv.findViewById(R.id.tvSMS);
		mTvCall = (TextView) rv.findViewById(R.id.tvCall);
		// mTvEmail = (TextView) rv.findViewById(R.id.tvEmail);

		mHlRelate = (HorizontalListView) rv.findViewById(R.id.hlvRelate);
		mGvAttribute = (GridView) rv.findViewById(R.id.gvAttribute);
		mAttributeAdapter = new AttributeAdapter(getActivity(), mListAttribute);

		mLvMessage = (ListView) rv.findViewById(R.id.lvMessage);
		mMsgAdapter = new MessageAdapter(getActivity(), mListMessage);
		mLlMessage = (LinearLayout) rv.findViewById(R.id.llComment);
		mTvViewAllMsg = (TextView) rv.findViewById(R.id.tvViewAllComment);
		
		mRelateAdapter = new RelateAdapter(getActivity(), mListRelateAds);
		mHlRelate.setAdapter(mRelateAdapter);
		mHlRelate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Fragment fragment = new DetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Const.ADS_ID, mListRelateAds.get(arg2)
						.getAdsId());
				fragment.setArguments(bundle);
				addFragment(fragment, true, FragmentTransaction.TRANSIT_NONE);

			}
		});
		return rv;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mAdsId = getArguments().getString(Const.ADS_ID);
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		mListRelateAds = new ArrayList<Ads>();
		mListAttribute = new ArrayList<AdsDetail.Attribute>();
		mListMessage = new ArrayList<AdsDetail.Message>();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<AdsDetail> myReq = new GsonRequest<AdsDetail>(Method.GET,
				String.format(Const.URL_ADS_DETAIL, mAdsId), AdsDetail.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		Log.d("haipn", String.format(Const.URL_ADS_DETAIL, mAdsId));
		// new Task_UsedCarsList().execute();
		super.onStart();
	}

	protected void fillData(final AdsDetail res) {
		if (Const.hasInShortList(getActivity(), res.mAdid)) {
			mTvAddShortList.setText(R.string.remove_shortlist);
			mTvAddShortList.setBackgroundColor(getResources().getColor(
					R.color.splash_background));

		}
		mTvAddShortList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Const.hasInShortList(getActivity(), res.mAdid)) {
					Const.removeFromShortList(getActivity(), res.mAdid);
					mTvAddShortList.setText(R.string.btn_add_shortlist);
					mTvAddShortList.setBackgroundColor(getResources().getColor(
							R.color.orange));
				} else {
					Const.addToShortList(getActivity(), res.mAdid);
					mTvAddShortList.setText(R.string.remove_shortlist);
					mTvAddShortList.setBackgroundColor(getResources().getColor(
							R.color.splash_background));
				}
			}
		});
		mTvTitle.setText(res.mTitle);
		mTvPrice.setText(res.mPrice);
		mTvPriceDiff.setText(res.mPricePercentDiff);
		mTvFirm.setText(res.mFirmNeg);
		ArrayList<String> images = new ArrayList<String>();
		for (Picture picture : res.mPictures) {
			images.add(picture.l);
		}
		ImagePagerAdapter adapter = new ImagePagerAdapter(images);
		mPagerImage.setAdapter(adapter);
		if (res.mSpecial != null && res.mSpecial.textcolor != null) {
			mTvSpecial.setText(res.mSpecial.text);
			mTvSpecial.setTextColor(Color.parseColor(res.mSpecial.textcolor));
			mTvSpecial.setBackgroundColor(Color
					.parseColor(res.mSpecial.bgcolor));
			mTvSpecial.setVisibility(View.VISIBLE);
		} else {
			mTvSpecial.setVisibility(View.GONE);
		}
		if (res.mAdType != null)
			mTvAdType.setText(res.mAdType);
		else
			mTvAdType.setVisibility(View.GONE);

		if (res.mListingLabel != null)
			mTvListingLabel.setText(res.mListingLabel);
		else
			mTvListingLabel.setVisibility(View.GONE);
		res.mDescription = res.mDescription.replace("\\n",
				System.getProperty("line.separator"));
		mTvDescription.setText(res.mDescription);
		mTvSize.setText(res.mSize);
		mTvPostebyAndPostTime.setText(res.mPostedByDetails.mUserName + " on "
				+ res.mDateposted);
		mTvAllPostedBy.setText(getString(R.string.label_all_postby,
				res.mPostedByDetails.mUserName));

		mTvShopOrContact.setText(res.mContactDetails.mContact.label);
		mTvNameContact.setText(res.mContactDetails.mContact.value);

		mTvContactNo.setText(res.mContactDetails.mContactNo.value);
		mTvAddress.setText(res.mContactDetails.mLocation.value);
		mTvMailCount.setText(res.mMsgSent);
		mTvViewCount.setText(res.mAdViews);
		Log.d("haipn"," total message:" + res.mTotalMessages);
		
		mListRelateAds.addAll(res.mRelatedAds);
		mRelateAdapter.notifyDataSetChanged();
		mListAttribute.addAll(res.Attributes);
		mAttributeAdapter.notifyDataSetChanged();
		if (res.mTotalMessages == 0) {
			mLlMessage.setVisibility(View.GONE);
		} else {
			mLlMessage.setVisibility(View.VISIBLE);
			if (res.mTotalMessages > 3) {
				mTvViewAllMsg.setVisibility(View.VISIBLE);;
				mTvViewAllMsg.setText(getString(R.string.label_view_all_comment, res.mTotalMessages - 3));
				mListMessage.addAll(res.mListMessages.subList(0, 2));
			} else {
				mTvViewAllMsg.setVisibility(View.GONE);
				mListMessage.addAll(res.mListMessages);
				
			}
			
			mMsgAdapter.notifyDataSetChanged();
		}
		

	}

	private Response.Listener<AdsDetail> createMyReqSuccessListener() {
		return new Response.Listener<AdsDetail>() {
			@Override
			public void onResponse(AdsDetail response) {
				Log.d("haipn", "sussecc");
				fillData(response);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
			}
		};
	}

	private class RelateAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView tvTitle;
			public ImageView image;
		}

		ArrayList<Ads> listAds;
		Context mContext;

		public RelateAdapter(Context context, ArrayList<Ads> cars) {
			super();
			listAds = cars;
			mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.row_relate, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tvTitle);
				holder.image = (ImageView) convertView.findViewById(R.id.image);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Ads ads = getItem(position);
			imageLoader.displayImage(ads.getPicture(), holder.image, options);
			holder.tvTitle.setText(ads.getTitle());
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAds.size();
		}

		@Override
		public Ads getItem(int arg0) {
			// TODO Auto-generated method stub
			return listAds.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private ArrayList<String> images;
		private LayoutInflater inflater;

		ImagePagerAdapter(ArrayList<String> images) {
			this.images = images;
			inflater = getActivity().getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image,
					view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			imageLoader.displayImage(images.get(position), imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							Toast.makeText(getActivity(), message,
									Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}
