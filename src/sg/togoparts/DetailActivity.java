package sg.togoparts;

import java.util.ArrayList;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorInternetDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.app.SMSDialog;
import sg.togoparts.app.SMSDialog.AlertPositiveListener;
import sg.togoparts.gallery.HorizontalListView;
import sg.togoparts.json.Ads;
import sg.togoparts.json.AdsDetail;
import sg.togoparts.json.AdsDetail.Attribute;
import sg.togoparts.json.AdsDetail.Message;
import sg.togoparts.json.AdsDetail.Picture;
import sg.togoparts.json.ContactLog;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.login.MessageActivity;
import sg.togoparts.login.PostAdActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressLint("ValidFragment")
public class DetailActivity extends FragmentActivity implements
		AlertPositiveListener {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageButton mBtnBack;
	private ImageButton mBtnSearch;
	private TextView mTvTitleHeader;
	private ImageView mIvLogo;

	private TextView mTvTitle;
	private TextView mTvPrice;
	private TextView mTvPriceDiff;
	private TextView mTvFirm;

	private TextView mTvAddShortList;
	private TextView mTvShare;
	private ViewPager mPagerImage;
	private ImageButton mBtnLeft;
	private ImageButton mBtnRight;
	private TextView mTvSold;
	private TextView mTvSpecial;
	private TextView mTvListingLabel;
	// private TextView mTvPriority;
	private TextView mTvAdType;

	private TextView mTvDescription;
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
	// private TextView mTvEmail;
	private LinearLayout mLlContact;
	private View mSeparate;
	private HorizontalListView mHlRelate;

	private LinearLayout mLlSize;
	private LinearLayout mLlShop;
	private LinearLayout mLlContactNo;
	private LinearLayout mLlAddress;
	private LinearLayout mLlRelate;

	// private GridView mGvAttribute;
	private TableLayout mTlAttribute;
	// private AttributeAdapter mAttributeAdapter;
	// private ArrayList<Attribute> mListAttribute;

	private String mAdsId;

	private RelateAdapter mRelateAdapter;
	private ArrayList<Ads> mListRelateAds;

	// private ListView mLvMessage;
	private ArrayList<Message> mListMessage;
	// private MessageAdapter mMsgAdapter;
	private LinearLayout mLlMessage;
	private TextView mTvViewAllMsg;

	private TextView mTvPositive;
	private TextView mTvNeutral;
	private TextView mTvNegative;
	ImageView mIvLoading;
	LinearLayout mLlMain;
	private String[] listNumber;
	private int mTypeDialog;
	private ArrayList<String> actualNo;
	public static final int TYPE_CALL = 0;
	public static final int TYPE_SMS = 1;
	private static final String SCREEN_LABEL = "Marketplace Ad Details";
	protected static final String POST_BY = "Marketplace List Ads by User";
	protected static final String SHARE = "Marketplace Share";
	protected static final int REQUEST_EDIT_AD = 0;
	private int mWidthScreen;
	private boolean isMyAd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		mIvLoading = (ImageView) findViewById(R.id.loading);
		mLlMain = (LinearLayout) findViewById(R.id.llMain);
		// Get the background, which has been compiled to an AnimationDrawable
		// object.
		AnimationDrawable frameAnimation = (AnimationDrawable) mIvLoading
				.getDrawable();

		// Start the animation (looped playback by default).
		frameAnimation.start();

		mAdsId = getIntent().getStringExtra(Const.ADS_ID);
		isMyAd = getIntent().getBooleanExtra(Const.IS_MY_AD, false);
		Log.d("haipn", "id :" + mAdsId);
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		mListRelateAds = new ArrayList<Ads>();
		// mListAttribute = new ArrayList<AdsDetail.Attribute>();
		mListMessage = new ArrayList<AdsDetail.Message>();

		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<AdsDetail> myReq = new GsonRequest<AdsDetail>(Method.GET,
				String.format(Const.URL_ADS_DETAIL, mAdsId), AdsDetail.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);

	}

	private void initInterface() {
		// TODO Auto-generated method stub

		// mIvLoading.setAnimation(getResources().getAnimation(android.R.animator.));
		createHeader();
		mTvTitle = (TextView) findViewById(R.id.tvTitle);
		mTvPrice = (TextView) findViewById(R.id.tvPrice);
		mTvPriceDiff = (TextView) findViewById(R.id.tvPriceDiff);
		mTvFirm = (TextView) findViewById(R.id.tvFirm);

		mTvAddShortList = (TextView) findViewById(R.id.btnAddShortList);
		mTvShare = (TextView) findViewById(R.id.btnShare);
		mPagerImage = (ViewPager) findViewById(R.id.pager);
		mBtnLeft = (ImageButton) findViewById(R.id.btnLeft);
		mBtnRight = (ImageButton) findViewById(R.id.btnRight);
		mTvSold = (TextView) findViewById(R.id.tvSold);

		mTvSpecial = (TextView) findViewById(R.id.tvSpecial);
		mTvListingLabel = (TextView) findViewById(R.id.tvListinglabel);
		// mTvPriority = (TextView) findViewById(R.id.tvPriority);
		mTvAdType = (TextView) findViewById(R.id.tvAdTypes);

		mTvDescription = (TextView) findViewById(R.id.tvDescription);
		mTvPostebyAndPostTime = (TextView) findViewById(R.id.tvPostebyAndPostTime);
		mTvAllPostedBy = (TextView) findViewById(R.id.btnAllPostedBy);
		mIvShopLogo = (ImageView) findViewById(R.id.imvShopLogo);
		mTvPositive = (TextView) findViewById(R.id.tvPositive);
		mTvNeutral = (TextView) findViewById(R.id.tvNeutral);
		mTvNegative = (TextView) findViewById(R.id.tvNegative);
		mTvShopOrContact = (TextView) findViewById(R.id.tvShopOrContact);
		mTvNameContact = (TextView) findViewById(R.id.tvNameContact);
		mTvContactNo = (TextView) findViewById(R.id.tvContactNo);
		mTvAddress = (TextView) findViewById(R.id.tvAddress);

		mTvViewCount = (TextView) findViewById(R.id.tvViewCount);
		mTvMailCount = (TextView) findViewById(R.id.tvMailCount);
		mTvSMS = (TextView) findViewById(R.id.tvSMS);
		mTvCall = (TextView) findViewById(R.id.tvCall);
		// mTvEmail = (TextView) findViewById(R.id.tvEmail);
		mLlContact = (LinearLayout) findViewById(R.id.llContact);
		mSeparate = findViewById(R.id.separate);

		mHlRelate = (HorizontalListView) findViewById(R.id.hlvRelate);

		mLlAddress = (LinearLayout) findViewById(R.id.llAddress);
		mLlContactNo = (LinearLayout) findViewById(R.id.llContactNo);
		mLlShop = (LinearLayout) findViewById(R.id.llShop);
		mLlRelate = (LinearLayout) findViewById(R.id.llRelate);

		// mGvAttribute = (GridView) findViewById(R.id.gvAttribute);
		// mAttributeAdapter = new AttributeAdapter(this, mListAttribute);
		// mGvAttribute.setAdapter(mAttributeAdapter);

		mTlAttribute = (TableLayout) findViewById(R.id.tlAttribute);
		// mLvMessage = (ListView) findViewById(R.id.lvMessage);
		// mMsgAdapter = new MessageAdapter(this, mListMessage);
		// mLvMessage.setAdapter(mMsgAdapter);
		mLlMessage = (LinearLayout) findViewById(R.id.llComment);
		mTvViewAllMsg = (TextView) findViewById(R.id.tvViewAllComment);

		mRelateAdapter = new RelateAdapter(this, mListRelateAds);
		mHlRelate.setAdapter(mRelateAdapter);
		mHlRelate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(DetailActivity.this, DetailActivity.class);
				i.putExtra(Const.ADS_ID, mListRelateAds.get(arg2).getAdsId());
				startActivity(i);
			}
		});

		mRelateAdapter = new RelateAdapter(this, mListRelateAds);
		mHlRelate.setAdapter(mRelateAdapter);
		mHlRelate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(DetailActivity.this, DetailActivity.class);
				i.putExtra(Const.ADS_ID, mListRelateAds.get(arg2).getAdsId());
				startActivity(i);
			}
		});
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		mWidthScreen = metrics.widthPixels;
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		mBtnSearch = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mIvLogo.setVisibility(View.GONE);

		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_ad_detail);
		mTvTitleHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon,
				0, 0, 0);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				setResult(RESULT_CANCELED);
//				onBackPressed();
				finish();
			}
		});
		mBtnSearch.setVisibility(View.VISIBLE);
		if (isMyAd) {
			mBtnSearch.setImageResource(android.R.drawable.ic_menu_edit);
			mBtnSearch.setPadding(8, 8, 8, 8);
			mBtnSearch.setBackgroundResource(R.drawable.btn_click);
		}
		mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMyAd) {
					Intent i = new Intent(DetailActivity.this,
							PostAdActivity.class);
					i.putExtra(Const.ADS_ID, mAdsId);
					startActivityForResult(i, REQUEST_EDIT_AD);
				} else {
					Intent i = new Intent(DetailActivity.this,
							SearchActivity.class);
					startActivity(i);
					finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_EDIT_AD && resultCode == RESULT_OK) {
			RequestQueue queue = MyVolley.getRequestQueue();
			GsonRequest<AdsDetail> myReq = new GsonRequest<AdsDetail>(
					Method.GET, String.format(Const.URL_ADS_DETAIL, mAdsId),
					AdsDetail.class, createEditSuccessListener(),
					createMyReqErrorListener());
			queue.add(myReq);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	protected void onResume() {
		final PublisherAdView adview = (PublisherAdView) findViewById(R.id.adView);

		PublisherAdRequest.Builder re = new PublisherAdRequest.Builder();
		adview.loadAd(re.build());
		adview.setVisibility(View.GONE);
		adview.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				adview.setVisibility(View.VISIBLE);
				super.onAdLoaded();
			}
		});
		super.onResume();
	}

	protected void fillData(final AdsDetail res) {
		if (Const.hasInShortList(this, res.mAdid)) {
			// mTvAddShortList.setText(R.string.remove_shortlist);
			mTvAddShortList.setBackgroundColor(getResources().getColor(
					R.color.red));
			mTvAddShortList.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.star, 0, 0, 0);
		}
		mTvAddShortList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Const.hasInShortList(DetailActivity.this, res.mAdid)) {
					Log.d("haipn", "has shortlist");
					Const.removeFromShortList(DetailActivity.this, res.mAdid);
					// mTvAddShortList.setText(R.string.btn_add_shortlist);
					mTvAddShortList.setBackgroundColor(getResources().getColor(
							R.color.orange));
					mTvAddShortList.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.shortlist_ad_small_button, 0, 0, 0);
				} else {
					Log.d("haipn", "don't have shortlist");
					Const.addToShortList(DetailActivity.this, res.mAdid);
					// mTvAddShortList.setText(R.string.remove_shortlist);
					mTvAddShortList.setBackgroundColor(getResources().getColor(
							R.color.red));
					mTvAddShortList.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.star, 0, 0, 0);
				}
			}
		});
		mTvShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					// SHARED_SUBJECT + shareContentSubject);
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							res.mCanonicalUrl);
					startActivity(Intent.createChooser(sharingIntent,
							"Share with"));
					Tracker tracker = GoogleAnalytics.getInstance(
							DetailActivity.this).getTracker(
							Const.GA_PROPERTY_ID);
					tracker.set(Fields.SCREEN_NAME, SHARE);
					tracker.send(MapBuilder.createAppView().build());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mTvTitle.setText(res.mTitle);
		mTvPrice.setText(res.mPrice);
		// save price
		if (res.mPricePercentDiff != null && !res.mPricePercentDiff.isEmpty()
				&& !res.mPricePercentDiff.equals("0")) {
			mTvPriceDiff.setText("Save " + res.mPricePercentDiff);
			if (res.mFirmNeg != null && !res.mFirmNeg.isEmpty()) {
				mTvPriceDiff.setText(mTvPriceDiff.getText());
				mTvFirm.setText(" - " + res.mFirmNeg.trim());
			}
		} else {
			mTvPriceDiff.setVisibility(View.GONE);
			mTvFirm.setText(res.mFirmNeg.trim());
		}
		ArrayList<String> images = new ArrayList<String>();
		for (Picture picture : res.mPictures) {
			images.add(picture.l);
		}
		ImagePagerAdapter adapter = new ImagePagerAdapter(images);
		mPagerImage.setAdapter(adapter);
		mPagerImage.setOnPageChangeListener(new PageListener());
		if (images.size() == 0) {
			mBtnLeft.setVisibility(View.GONE);
			mBtnRight.setVisibility(View.GONE);
			mPagerImage.setVisibility(View.GONE);
		} else if (images.size() == 1) {
			mBtnLeft.setVisibility(View.GONE);
			mBtnRight.setVisibility(View.GONE);
		} else {
			mPagerImage.setCurrentItem(0);
			mBtnLeft.setVisibility(View.GONE);
		}

		if (res.mAdStatus != null && !res.mAdStatus.isEmpty()) {
			if (res.mAdStatus.equalsIgnoreCase("available")
					|| res.mAdStatus.equalsIgnoreCase("looking")
					|| res.mAdStatus.equalsIgnoreCase("for exchange")) {
				mTvSold.setVisibility(View.GONE);
				mBtnSearch.setVisibility(View.VISIBLE);
			} else {
				mTvSold.setText(res.mAdStatus);
				mTvSold.setVisibility(View.VISIBLE);
				mBtnSearch.setVisibility(View.GONE);
			}
		} else {
			mTvSold.setVisibility(View.GONE);
		}

		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPagerImage.getCurrentItem() > 0)
					mPagerImage.setCurrentItem(
							mPagerImage.getCurrentItem() - 1, true);

			}
		});
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPagerImage.getCurrentItem() < mPagerImage.getAdapter()
						.getCount() - 1)
					mPagerImage.setCurrentItem(
							mPagerImage.getCurrentItem() + 1, true);

			}
		});
		if (res.mSpecial != null && res.mSpecial.textcolor != null) {
			mTvSpecial.setText(res.mSpecial.text);
			mTvSpecial.setTextColor(Color.parseColor(res.mSpecial.textcolor));
			mTvSpecial.setBackgroundColor(Color
					.parseColor(res.mSpecial.bgcolor));
			mTvSpecial.setVisibility(View.VISIBLE);
		} else {
			mTvSpecial.setVisibility(View.GONE);
		}
		if (res.mAdType != null && !res.mAdType.isEmpty())
			mTvAdType.setText(res.mAdType);
		else
			mTvAdType.setVisibility(View.GONE);

		if (res.mListingLabel != null && !res.mListingLabel.isEmpty()) {
			if (res.mListingLabel.equalsIgnoreCase("priority")) {
				mTvListingLabel.setTextColor(getResources().getColor(
						android.R.color.black));
				mTvListingLabel.setBackgroundColor(getResources().getColor(
						R.color.yellow));
			} else {
				mTvListingLabel.setTextColor(getResources().getColor(
						android.R.color.white));
				mTvListingLabel.setBackgroundColor(getResources().getColor(
						R.color.dark_blue));
			}
			mTvListingLabel.setText(res.mListingLabel);
			mTvListingLabel.setVisibility(View.VISIBLE);
		} else {
			mTvListingLabel.setVisibility(View.INVISIBLE);
		}
		res.mDescription = res.mDescription.replace("\\n",
				System.getProperty("line.separator"));
		mTvDescription.setText(res.mDescription);
		mTvPostebyAndPostTime.setText(res.mPostedByDetails.mUserName + " on "
				+ res.mDateposted);
		mTvAllPostedBy.setText(getString(R.string.label_all_postby,
				res.mPostedByDetails.mUserName));

		if (res.mPostedByDetails.mRatings != null) {
			mTvPositive.setVisibility(View.VISIBLE);
			mTvNeutral.setVisibility(View.VISIBLE);
			mTvNegative.setVisibility(View.VISIBLE);
			mTvPositive.setText(res.mPostedByDetails.mRatings.Positive + "");
			mTvNeutral.setText(res.mPostedByDetails.mRatings.Neutral + "");
			mTvNegative.setText(res.mPostedByDetails.mRatings.Negative + "");
		} else {
			mTvPositive.setVisibility(View.GONE);
			mTvNeutral.setVisibility(View.GONE);
			mTvNegative.setVisibility(View.GONE);

		}
		mTvAllPostedBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DetailActivity.this,
						SearchResultActivity.class);

				Bundle b = new Bundle();
				b.putString(FilterActivity.POSTED_BY,
						res.mPostedByDetails.mUserName);

				i.putExtras(b);
				i.putExtra(Const.TITLE, "All Ads by "
						+ res.mPostedByDetails.mUserName);
				i.putExtra(SearchResultActivity.SCREEN_SEARCH_RESULT,
						"Marketplace List Ads by User");
				Tracker tracker = GoogleAnalytics.getInstance(
						DetailActivity.this).getTracker(Const.GA_PROPERTY_ID);
				tracker.set(Fields.SCREEN_NAME, POST_BY);
				tracker.send(MapBuilder.createAppView().build());
				startActivity(i);
			}
		});
		imageLoader.displayImage(res.mContactDetails.mShopLogo, mIvShopLogo);
		mIvShopLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DetailActivity.this, BikeShopDetail.class);
				i.putExtra(Const.SHOP_ID, res.mContactDetails.mShopId);
				// i.putExtra(Const.LATITUDE, mLat);
				// i.putExtra(Const.LONGITUDE, mLong);
				startActivity(i);
			}
		});

		if (res.mContactDetails.mContact != null
				&& res.mContactDetails.mContact.value != null
				&& !res.mContactDetails.mContact.value.isEmpty()) {
			mTvShopOrContact.setText(res.mContactDetails.mContact.label);
			mTvNameContact.setText(res.mContactDetails.mContact.value);
			if (res.mContactDetails.mShopId != null
					&& !res.mContactDetails.mShopId.isEmpty()) {
				mTvNameContact.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(DetailActivity.this,
								BikeShopDetail.class);
						i.putExtra(Const.SHOP_ID, res.mContactDetails.mShopId);
						// i.putExtra(Const.LATITUDE, mLat);
						// i.putExtra(Const.LONGITUDE, mLong);
						startActivity(i);
					}
				});
			}
			mLlShop.setVisibility(View.VISIBLE);
		} else {
			mLlShop.setVisibility(View.GONE);
		}

		if (res.mContactDetails.mContactNo != null
				&& res.mContactDetails.mContactNo.value != null
				&& !res.mContactDetails.mContactNo.value.isEmpty()) {
			mTvContactNo.setText(res.mContactDetails.mContactNo.value);
			mLlContactNo.setVisibility(View.VISIBLE);
		} else
			mLlContactNo.setVisibility(View.GONE);
		if (res.mContactDetails.mBestTime != null
				&& res.mContactDetails.mBestTime.value != null
				&& !res.mContactDetails.mBestTime.value.isEmpty()) {
			((TextView) findViewById(R.id.tvBesttime))
					.setText(res.mContactDetails.mBestTime.value);
			((TextView) findViewById(R.id.tvLabelBesttime))
					.setText(res.mContactDetails.mBestTime.label);
			findViewById(R.id.llBestTime).setVisibility(View.VISIBLE);
		} else
			findViewById(R.id.llBestTime).setVisibility(View.GONE);

		if (res.mContactDetails.mLocation != null
				&& res.mContactDetails.mLocation.value != null
				&& !res.mContactDetails.mLocation.value.isEmpty()) {
			mTvAddress.setText(res.mContactDetails.mLocation.value);
			mLlAddress.setVisibility(View.VISIBLE);
		} else
			mLlAddress.setVisibility(View.GONE);

		mTvMailCount.setText(res.mMsgSent);
		mTvViewCount.setText(res.mAdViews);
		final String address = res.mContactDetails.mContactNo.value;
		actualNo = new ArrayList<String>();
		if (address != null && !address.isEmpty()) {

			if (res.mContactDetails.mContactNo.actualno != null
					&& res.mContactDetails.mContactNo.actualno.size() > 0)
				actualNo = res.mContactDetails.mContactNo.actualno;
			else
				actualNo.add(address);
			mTvSMS.setVisibility(View.VISIBLE);
			mTvCall.setVisibility(View.VISIBLE);
			mLlContact.setVisibility(View.VISIBLE);
			mSeparate.setVisibility(View.VISIBLE);
			if (actualNo.get(0).length() != 8) {
				mLlContact.setVisibility(View.GONE);
				mSeparate.setVisibility(View.GONE);
			} else {
				if (actualNo.get(0).startsWith("8")
						|| actualNo.get(0).startsWith("9")) {
					mTvSMS.setVisibility(View.VISIBLE);
				} else
					mTvSMS.setVisibility(View.INVISIBLE);
				mLlContact.setVisibility(View.VISIBLE);
				mSeparate.setVisibility(View.VISIBLE);
			}
		} else {
			mLlContact.setVisibility(View.GONE);
			mSeparate.setVisibility(View.GONE);
		}
		mTvSMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (actualNo == null || actualNo.size() == 0)
					return;
				// listNumber = address.split("/");
				if (actualNo.size() > 1) {
					FragmentTransaction ft = getSupportFragmentManager()
							.beginTransaction();
					SMSDialog dialog = new SMSDialog(DetailActivity.this);
					Bundle b = new Bundle();
					listNumber = new String[actualNo.size()];
					for (int i = 0; i < actualNo.size(); i++) {
						listNumber[i] = actualNo.get(i);
					}
					b.putStringArray(SMSDialog.DATA, listNumber);
					dialog.setArguments(b);
					dialog.show(ft, "sms dialog");
					mTypeDialog = TYPE_SMS;
				} else {
					Intent smsIntent = new Intent(Intent.ACTION_VIEW);
					smsIntent.setType("vnd.android-dir/mms-sms");
					smsIntent.putExtra("address", actualNo.get(0));
					startActivity(smsIntent);
					postSMSLog();
				}
			}
		});
		mTvCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (actualNo == null || actualNo.size() == 0)
					return;
				// listNumber = address.split("/");
				if (actualNo.size() > 1) {
					FragmentTransaction ft = getSupportFragmentManager()
							.beginTransaction();
					SMSDialog dialog = new SMSDialog(DetailActivity.this);
					Bundle b = new Bundle();
					listNumber = new String[actualNo.size()];
					for (int i = 0; i < actualNo.size(); i++) {
						listNumber[i] = actualNo.get(i);
					}
					b.putStringArray(SMSDialog.DATA, listNumber);
					dialog.setArguments(b);
					dialog.show(ft, "call dialog");
					mTypeDialog = TYPE_CALL;
				} else {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + actualNo.get(0)));
					startActivity(callIntent);
					postCallLog();
				}
			}
		});
		if (res.mRelatedAds != null && res.mRelatedAds.size() > 0) {
			mListRelateAds.addAll(res.mRelatedAds);
			mRelateAdapter.notifyDataSetChanged();
		} else {
			mLlRelate.setVisibility(View.GONE);
		}

		if (res.Attributes != null) {
			// mListAttribute.addAll(res.Attributes);
			// mAttributeAdapter.notifyDataSetChanged();
			// int value = (int) TypedValue.applyDimension(
			// TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
			// .getDisplayMetrics());
			// Const.setGridViewHeightBasedOnChildren(mGvAttribute, 2, value);

			addAtribute(res.Attributes);
		}
		if (res.mTotalMessages == 0) {
			mLlMessage.setVisibility(View.GONE);
		} else {

			mLlMessage.setVisibility(View.VISIBLE);
			if (res.mTotalMessages > 3) {
				mTvViewAllMsg.setVisibility(View.VISIBLE);
				mTvViewAllMsg
						.setText(getString(R.string.label_view_all_comment,
								res.mTotalMessages - 3));
				mTvViewAllMsg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(DetailActivity.this,
								MessageActivity.class);
						i.putExtra(Const.ADS_ID, res.mAdid);
						startActivity(i);
					}
				});
			} else {
				mTvViewAllMsg.setVisibility(View.GONE);

			}
			mListMessage.addAll(res.mListMessages);
			createListMessage();
			// mMsgAdapter.notifyDataSetChanged();
			// Const.setListViewHeightBasedOnChildren(mLvMessage);
		}

	}

	private void addAtribute(ArrayList<Attribute> attributes) {
		View line = LayoutInflater.from(this).inflate(R.layout.separate, null);
		mTlAttribute.addView(line);
		for (int i = 0; i < attributes.size(); i = i + 2) {
			TableRow row = new TableRow(this);
			View column1 = LayoutInflater.from(this).inflate(
					R.layout.row_attribute, null);
			TextView label1 = (TextView) column1.findViewById(R.id.tvLabel);
			TextView value1 = (TextView) column1.findViewById(R.id.tvValue);
			Attribute att1 = attributes.get(i);

			label1.setText(att1.label);
			value1.setText(att1.value);
			row.addView(column1, new LayoutParams(mWidthScreen / 2,
					LayoutParams.WRAP_CONTENT));
			if (i + 1 < attributes.size()) {
				View column2 = LayoutInflater.from(this).inflate(
						R.layout.row_attribute, null);
				TextView label2 = (TextView) column2.findViewById(R.id.tvLabel);
				TextView value2 = (TextView) column2.findViewById(R.id.tvValue);
				Attribute att2 = attributes.get(i + 1);
				label2.setText(att2.label);
				value2.setText(att2.value);
				row.addView(column2, new LayoutParams(mWidthScreen / 2,
						LayoutParams.WRAP_CONTENT));
			}
			mTlAttribute.addView(row);
		}
		View line2 = LayoutInflater.from(this).inflate(R.layout.separate, null);
		mTlAttribute.addView(line2);
	}

	private void createListMessage() {
		for (int i = 0; i < mListMessage.size(); i++) {
			View convertView = LayoutInflater.from(this).inflate(
					R.layout.row_comment, null);
			TextView user = (TextView) convertView.findViewById(R.id.tvUser);
			TextView comment = (TextView) convertView
					.findViewById(R.id.tvComment);
			TextView datepost = (TextView) convertView
					.findViewById(R.id.tvDate);
			ImageView thumb = (ImageView) convertView
					.findViewById(R.id.imvAvatar);
			Message promos = mListMessage.get(i);
			user.setText(promos.username);
			comment.setText(promos.message);
			datepost.setText(promos.datesent);
			imageLoader.displayImage(promos.picture, thumb, options);

			mLlMessage.addView(convertView);
			View line = LayoutInflater.from(this).inflate(R.layout.separate,
					null);

			mLlMessage.addView(line);
		}
	}

	private void startAnimation() {
		Animation in = AnimationUtils
				.loadAnimation(this, R.anim.slide_right_in);
		Animation out = AnimationUtils.loadAnimation(this,
				R.anim.slide_left_out);
		in.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mLlMain.setVisibility(View.VISIBLE);

			}
		});
		out.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mIvLoading.setVisibility(View.GONE);
			}
		});
		mLlMain.startAnimation(in);
		mIvLoading.startAnimation(out);
	}

	private Response.Listener<AdsDetail> createMyReqSuccessListener() {
		return new Response.Listener<AdsDetail>() {
			@Override
			public void onResponse(AdsDetail response) {
				Log.d("haipn", "sussecc");
				initInterface();
				fillData(response);
				startAnimation();
			}
		};
	}

	private Response.Listener<AdsDetail> createEditSuccessListener() {
		return new Response.Listener<AdsDetail>() {
			@Override
			public void onResponse(AdsDetail response) {
				Log.d("haipn", "sussecc");
				mListMessage.clear();
				mListRelateAds.clear();
				mTlAttribute.removeAllViews();
				fillData(response);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorInternetDialog newFragment = new ErrorInternetDialog();
				newFragment.show(ft, "error dialog");
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
				convertView = LayoutInflater.from(mContext).inflate(
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

	private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			Log.d("haipn", "page select:" + arg0);
			if (arg0 == 0) {
				mBtnLeft.setVisibility(View.GONE);
			} else {
				mBtnLeft.setVisibility(View.VISIBLE);
			}

			if (arg0 >= mPagerImage.getAdapter().getCount() - 1) {
				mBtnRight.setVisibility(View.GONE);
			} else {
				mBtnRight.setVisibility(View.VISIBLE);
			}
		}

	}

	private class ImagePagerAdapter extends PagerAdapter {

		private ArrayList<String> images;
		private LayoutInflater inflater;

		ImagePagerAdapter(ArrayList<String> images) {
			this.images = images;
			inflater = DetailActivity.this.getLayoutInflater();
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
							Toast.makeText(DetailActivity.this, message,
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

	@Override
	public void onPositiveClick(int position) {
		String address = listNumber[position];
		if (mTypeDialog == TYPE_CALL) {

			if (address != null && !address.isEmpty()) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + address));
				startActivity(callIntent);
				postCallLog();
			}
		} else {
			if (address != null && !address.isEmpty()) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", address);
				startActivity(smsIntent);
				postSMSLog();
			}
		}
	}

	private void postSMSLog() {
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ContactLog> myReq = new GsonRequest<ContactLog>(Method.GET,
				String.format(Const.URL_CONTACT_LOG, mAdsId, "aid",
						"sms_android"), ContactLog.class,
				createPostLogSuccessListener(), null);
		queue.add(myReq);
	}

	private void postCallLog() {
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ContactLog> myReq = new GsonRequest<ContactLog>(Method.GET,
				String.format(Const.URL_CONTACT_LOG, mAdsId, "aid",
						"call_android"), ContactLog.class,
				createPostLogSuccessListener(), null);
		queue.add(myReq);
	}

	private Response.Listener<ContactLog> createPostLogSuccessListener() {
		return new Response.Listener<ContactLog>() {
			@Override
			public void onResponse(ContactLog response) {
				Log.d("haipn", "response:" + response.message + "  "
						+ response.msg_type);
			}
		};
	}
}
