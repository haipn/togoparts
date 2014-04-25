package sg.togoparts;

import java.util.ArrayList;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorDialog;
import sg.togoparts.app.MyLocation;
import sg.togoparts.app.MyLocation.LocationResult;
import sg.togoparts.app.MyVolley;
import sg.togoparts.app.NewGridView;
import sg.togoparts.app.SMSDialog;
import sg.togoparts.app.SMSDialog.AlertPositiveListener;
import sg.togoparts.gallery.ImagePagerAdapter;
import sg.togoparts.json.BikeShop.Brand;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ShopDetail;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class BikeShopDetail extends FragmentActivity implements LocationResult,
		AlertPositiveListener {

	protected static final int TYPE_SMS = 0;
	protected static final int TYPE_CALL = 1;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	LinearLayout mLlOpen;
	TextView mTvOpenLabel;
	TextView mTvRemark;
	TextView mTvDistance;
	TextView mTvLocation;
	ImageView mIvShopLogo;
	ViewPager mPager;
	ImageButton mBtnRight;
	ImageButton mBtnLeft;
	TextView mTvSMS;
	TextView mTvCall;

	ScrollView mScrollView;
	LinearLayout mLlNotPaying;
	TextView mTvAddress;
	TextView mTvTiming;
	TextView mTvTel;
	TextView mTvMobile;
	TextView mTvFax;
	TextView mTvEmail;
	TextView mTvWebsite;
	TextView mTvAvai;
	TextView mTvMechanic;
	TextView mTvDelivery;

	TextView mTvBrands;
	NewGridView mGvBrandsDist;
	TextView mTvBrandRetailed;
	TextView mTvPromos;
	TextView mTvNewItems;
	ImageView mIvPromos;
	ImageView mIvNewItems;

	TextView mTvAddrestNotPaying;
	TextView mTvTelephoneNotPaying;
	private ImageView mIvLoading;
	private LinearLayout mLlMain;
	private String mShopId;
	private DisplayImageOptions options;
	private DisplayImageOptions optionNewItem;
	private DisplayImageOptions optionPromos;
	private ImageButton mBtnBack;
	private TextView mTvTitleHeader;
	private String mLatitude;
	private String mLongitude;
	private ArrayList<String> actualNo = new ArrayList<String>();
	protected String[] listNumber;
	protected int mTypeDialog;

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		setContentView(R.layout.bikeshop_detail);
		mIvLoading = (ImageView) findViewById(R.id.loading);
		mLlMain = (LinearLayout) findViewById(R.id.llMain);
		// Get the background, which has been compiled to an AnimationDrawable
		// object.
		AnimationDrawable frameAnimation = (AnimationDrawable) mIvLoading
				.getDrawable();

		// Start the animation (looped playback by default).
		frameAnimation.start();
		mShopId = getIntent().getStringExtra(Const.SHOP_ID);

		mLatitude = getIntent().getStringExtra(Const.LATITUDE);
		mLongitude = getIntent().getStringExtra(Const.LONGITUDE);

		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.showImageForEmptyUri(R.drawable.nophoto)
				.showImageOnFail(R.drawable.nophoto)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		optionNewItem = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.showImageForEmptyUri(R.drawable.new_item_tab)
				.showImageOnFail(R.drawable.new_item_tab)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		optionPromos = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.showImageForEmptyUri(R.drawable.promos_tab)
				.showImageOnFail(R.drawable.promos_tab)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		if (mLatitude != null && !mLatitude.isEmpty()) {
			requestBikeShopDetail();
		} else {
			MyLocation myLocation = new MyLocation();
			if (!myLocation.getLocation(this, this)) {
				mLatitude = "";
				mLongitude = "";
				requestBikeShopDetail();
			}
		}
	}

	private void requestBikeShopDetail() {
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ShopDetail> myReq = new GsonRequest<ShopDetail>(Method.GET,
				String.format(Const.URL_BIKESHOP_DETAIL, mShopId, mLatitude,
						mLongitude), ShopDetail.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		Log.d("haipn",
				"request:"
						+ String.format(Const.URL_BIKESHOP_DETAIL, mShopId,
								mLatitude, mLongitude));
	}

	private void initInterface() {

		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mLlNotPaying = (LinearLayout) findViewById(R.id.llNotPaying);
		mLlOpen = (LinearLayout) findViewById(R.id.llOpen);
		mTvOpenLabel = (TextView) findViewById(R.id.tvOpenLabel);
		mTvRemark = (TextView) findViewById(R.id.tvRemark);
		mTvDistance = (TextView) findViewById(R.id.tvDistance);
		mTvLocation = (TextView) findViewById(R.id.tvLocation);
		mIvShopLogo = (ImageView) findViewById(R.id.imvShopLogo);
		mPager = (ViewPager) findViewById(R.id.pager);
		mBtnRight = (ImageButton) findViewById(R.id.btnRight);
		mBtnLeft = (ImageButton) findViewById(R.id.btnLeft);
		mTvSMS = (TextView) findViewById(R.id.tvSMS);
		mTvCall = (TextView) findViewById(R.id.tvCall);
		mTvAddress = (TextView) findViewById(R.id.tvAddress);
		mTvTiming = (TextView) findViewById(R.id.tvOpenHours);
		mTvTel = (TextView) findViewById(R.id.tvTelephone);
		mTvMobile = (TextView) findViewById(R.id.tvMobile);
		mTvFax = (TextView) findViewById(R.id.tvFax);
		mTvEmail = (TextView) findViewById(R.id.tvEmail);
		mTvWebsite = (TextView) findViewById(R.id.tvWebsite);
		mTvAvai = (TextView) findViewById(R.id.tvBikesAvai);
		mTvMechanic = (TextView) findViewById(R.id.tvMechanic);
		mTvDelivery = (TextView) findViewById(R.id.tvDelivery);
		mTvBrands = (TextView) findViewById(R.id.tvBrands);

		mGvBrandsDist = (NewGridView) findViewById(R.id.gvBrands);

		mTvBrandRetailed = (TextView) findViewById(R.id.tvBrandRetailed);
		mTvPromos = (TextView) findViewById(R.id.tvPromos);
		mTvNewItems = (TextView) findViewById(R.id.tvNewItems);

		mIvPromos = (ImageView) findViewById(R.id.ivPromos);
		mIvNewItems = (ImageView) findViewById(R.id.ivNewItems);

		mTvAddrestNotPaying = (TextView) findViewById(R.id.tvAddressNotPaying);
		mTvTelephoneNotPaying = (TextView) findViewById(R.id.tvTelephoneNotPaying);
	}

	protected void fillData(final ShopDetail res) {
		mTvTitleHeader.setText(res.shopname);

		if (res.forpaidonly.openlabel != null
				&& !res.forpaidonly.openlabel.isEmpty()) {
			if (res.forpaidonly.openlabel.contains("OPEN")
					|| res.forpaidonly.openlabel.contains("Open")
					|| res.forpaidonly.openlabel.contains("open")) {
				mLlOpen.setBackgroundColor(getResources().getColor(
						R.color.green));
				mTvOpenLabel.setTextColor(getResources().getColor(
						android.R.color.white));
				mTvRemark.setTextColor(getResources().getColor(
						android.R.color.white));
			} else {
				mLlOpen.setBackgroundColor(getResources().getColor(
						R.color.closed_shop));
				mTvOpenLabel.setTextColor(getResources().getColor(
						android.R.color.black));
				mTvRemark.setTextColor(getResources().getColor(
						android.R.color.black));
			}
			mTvOpenLabel.setText(res.forpaidonly.openlabel);
			mTvRemark.setText(res.forpaidonly.remarks);
			mLlOpen.setVisibility(View.VISIBLE);
		} else {
			mLlOpen.setVisibility(View.INVISIBLE);
		}

		if (res.distance != null && !res.distance.isEmpty()) {
			mTvDistance.setText(res.distance);
			mTvDistance.setVisibility(View.VISIBLE);
		} else {
			mTvDistance.setVisibility(View.GONE);
		}

		findViewById(R.id.llLocation).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr="
								+ mLatitude + "," + mLongitude + "&daddr="
								+ res.latitude + "," + res.longitude));
				startActivity(intent);
			}
		});
		ArrayList<String> images = new ArrayList<String>();
		if (res.shopphotos != null) {
			for (String picture : res.shopphotos) {
				images.add(picture);
			}
		}
		ImagePagerAdapter adapter = new ImagePagerAdapter(images, this);
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(new PageListener());
		if (images.size() == 0) {
			mBtnLeft.setVisibility(View.GONE);
			mBtnRight.setVisibility(View.GONE);
			mPager.setVisibility(View.GONE);
		} else if (images.size() == 1) {
			mBtnLeft.setVisibility(View.GONE);
			mBtnRight.setVisibility(View.GONE);
		} else {
			mPager.setCurrentItem(0);
			mBtnLeft.setVisibility(View.GONE);
		}
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPager.getCurrentItem() > 0)
					mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);

			}
		});
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPager.getCurrentItem() < mPager.getAdapter().getCount() - 1)
					mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);

			}
		});
		mTvAddress.setText(res.address);
		if (res.forpaidonly.openinghrs != null) {
			Log.d("haipn", "open:" + res.forpaidonly.openinghrs);
			String newOpen = unescape(res.forpaidonly.openinghrs);
			Log.d("haipn", "new open" + newOpen);
			mTvTiming.setText(newOpen);
		}

		imageLoader.displayImage(res.shoplogo, mIvShopLogo, options);
		if (res.telephone != null && !res.telephone.isEmpty()) {
			mTvTel.setText(res.telephone);
			findViewById(R.id.llTel).setVisibility(View.VISIBLE);
		} else
			findViewById(R.id.llTel).setVisibility(View.GONE);
		if (res.forpaidonly.mobile != null && !res.forpaidonly.mobile.isEmpty()) {
			mTvMobile.setText(res.forpaidonly.mobile);
			findViewById(R.id.llMobile).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llMobile).setVisibility(View.GONE);
		}
		if (res.forpaidonly.fax != null && !res.forpaidonly.fax.isEmpty()) {
			mTvFax.setText(res.forpaidonly.fax);
			findViewById(R.id.llFax).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llFax).setVisibility(View.GONE);
		}

		if (res.forpaidonly.email != null && !res.forpaidonly.email.isEmpty()) {
			mTvEmail.setText(res.forpaidonly.email);
			findViewById(R.id.llEmail).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llEmail).setVisibility(View.GONE);
		}
		if (res.forpaidonly.website != null
				&& !res.forpaidonly.website.isEmpty()) {
			mTvWebsite.setText(res.forpaidonly.website);
			findViewById(R.id.llWeb).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llWeb).setVisibility(View.GONE);
		}
		if (res.forpaidonly.bikes_avail != null
				&& !res.forpaidonly.bikes_avail.isEmpty()) {
			mTvAvai.setText(res.forpaidonly.bikes_avail);
			findViewById(R.id.llBikeAvai).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llBikeAvai).setVisibility(View.GONE);
		}
		if (res.forpaidonly.mechanic_svcs != null
				&& !res.forpaidonly.mechanic_svcs.isEmpty()) {
			mTvMechanic.setText(res.forpaidonly.mechanic_svcs);
			findViewById(R.id.llBikeAvai).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llBikeAvai).setVisibility(View.GONE);
		}
		if (res.forpaidonly.delivery != null
				&& !res.forpaidonly.delivery.isEmpty()) {
			mTvDelivery.setText(res.forpaidonly.delivery);
			findViewById(R.id.llDelivery).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llDelivery).setVisibility(View.GONE);
		}

		mTvBrands.setText(getString(R.string.label_brand, res.shopname));
		mTvBrandRetailed.setText(res.forpaidonly.brands_retailed);
		imageLoader
				.displayImage(res.forpaidonly.promo, mIvPromos, optionPromos);
		imageLoader.displayImage(res.forpaidonly.new_item, mIvNewItems,
				optionNewItem);
		mTvPromos.setText(getString(R.string.view_promos,
				res.forpaidonly.promo_cnt));
		mTvNewItems.setText(getString(R.string.view_new_item,
				res.forpaidonly.new_item_cnt));
		if (checkVisiblePromos(res)) {
			findViewById(R.id.llPromos).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llPromos).setVisibility(View.GONE);
		}

		if (checkVisibleNewItems(res)) {
			findViewById(R.id.llNewItems).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.llNewItems).setVisibility(View.GONE);
		}

		if (checkVisibleNewItems(res) && checkVisiblePromos(res)) {
			findViewById(R.id.separate1).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.separate1).setVisibility(View.GONE);
		}
		mTvPromos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(BikeShopDetail.this,
						ListPromosActivity.class);
				i.putExtra(Const.SHOP_ID, res.sid);
				startActivity(i);
			}
		});
		mIvPromos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(BikeShopDetail.this,
						ListPromosActivity.class);
				i.putExtra(Const.SHOP_ID, res.sid);
				startActivity(i);
			}
		});

		mTvNewItems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoAllAds(res);
			}
		});
		mIvNewItems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoAllAds(res);
			}
		});
		mGvBrandsDist.setExpanded(true);
		if (res.forpaidonly.brands_dist != null
				&& !res.forpaidonly.brands_dist.isEmpty()) {
			mGvBrandsDist.setAdapter(new ImageAdapter(this,
					res.forpaidonly.brands_dist));
			mTvBrands.setVisibility(View.VISIBLE);
		} else {
			mTvBrands.setVisibility(View.GONE);
		}
		final String address = res.telephone;
		LinearLayout mLlContact = (LinearLayout) findViewById(R.id.llContact);
		if (address != null && !address.isEmpty()) {
			if (res.forpaidonly.actualno != null
					&& res.forpaidonly.actualno.size() > 0) {
				actualNo = res.forpaidonly.actualno;
			}
			mTvSMS.setVisibility(View.VISIBLE);
			mTvCall.setVisibility(View.VISIBLE);
			mLlContact.setVisibility(View.VISIBLE);

			if (actualNo.get(0).length() != 8) {
				mLlContact.setVisibility(View.GONE);
			} else {
				if (actualNo.get(0).startsWith("8")
						|| actualNo.get(0).startsWith("9")) {
					mTvSMS.setVisibility(View.VISIBLE);
				} else
					mTvSMS.setVisibility(View.INVISIBLE);
				mLlContact.setVisibility(View.VISIBLE);
			}
		} else {
			mLlContact.setVisibility(View.GONE);
		}
		mTvSMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (actualNo == null || actualNo.size() == 0)
					return;
				if (actualNo.size() > 1) {
					FragmentTransaction ft = getSupportFragmentManager()
							.beginTransaction();
					SMSDialog dialog = new SMSDialog(BikeShopDetail.this);
					Bundle b = new Bundle();
					ArrayList<String> smsNos = new ArrayList<String>();
					for (int i = 0; i < actualNo.size(); i++) {
						if (actualNo.get(i).startsWith("8")
								|| actualNo.get(i).startsWith("9")) {
							smsNos.add(actualNo.get(i));
						}
					}
					if (smsNos.size() == 1) {
						Intent smsIntent = new Intent(Intent.ACTION_VIEW);
						smsIntent.setType("vnd.android-dir/mms-sms");
						smsIntent.putExtra("address", smsNos.get(0));
						startActivity(smsIntent);
					} else {
						listNumber = new String[smsNos.size()];
						for (int i = 0; i < smsNos.size(); i++) {
							listNumber[i] = smsNos.get(i);
						}
						b.putStringArray(SMSDialog.DATA, listNumber);
						dialog.setArguments(b);
						dialog.show(ft, "sms dialog");
						mTypeDialog = TYPE_SMS;
					}
				} else {
					Intent smsIntent = new Intent(Intent.ACTION_VIEW);
					smsIntent.setType("vnd.android-dir/mms-sms");
					smsIntent.putExtra("address", actualNo.get(0));
					startActivity(smsIntent);
					// postSMSLog();
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
					SMSDialog dialog = new SMSDialog(BikeShopDetail.this);
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
					// postCallLog();
				}
			}
		});

		startAnimation();
	}

	private boolean checkVisiblePromos(ShopDetail shop) {
		int count = 0;
		try {
			count = Integer.valueOf(shop.forpaidonly.promo_cnt);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		if (count == 0)
			return false;
		return true;
	}

	private boolean checkVisibleNewItems(ShopDetail shop) {
		int count = 0;
		try {
			count = Integer.valueOf(shop.forpaidonly.new_item_cnt);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		if (count == 0)
			return false;
		return true;
	}

	protected void gotoAllAds(ShopDetail detail) {
		int count = 0;
		try {
			count = Integer.valueOf(detail.forpaidonly.new_item_cnt);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return;
		}
		if (count == 0)
			return;
		Intent i = new Intent(BikeShopDetail.this, SearchResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(FilterActivity.SHOP_NAME, detail.sid);
		i.putExtras(bundle);
		i.putExtra(Const.TITLE, "All Ads by " + detail.shopname);
		startActivity(i);
	}

	private String unescape(String description) {
		return description.replaceAll("\\\\t", "\\\t");
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

			if (arg0 >= mPager.getAdapter().getCount() - 1) {
				mBtnRight.setVisibility(View.GONE);
			} else {
				mBtnRight.setVisibility(View.VISIBLE);
			}
		}

	}

	private void createHeader() {
		View header = findViewById(R.id.header);
		mBtnBack = (ImageButton) header.findViewById(R.id.btnBack);
		header.findViewById(R.id.btnSearch).setVisibility(View.GONE);
		header.findViewById(R.id.logo).setVisibility(View.GONE);

		mTvTitleHeader = (TextView) header.findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_ad_detail);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}

	private Response.Listener<ShopDetail> createMyReqSuccessListener() {
		return new Response.Listener<ShopDetail>() {
			@Override
			public void onResponse(ShopDetail response) {
				Log.d("haipn", "sussecc");
				initInterface();
				if (response.forpaidonly != null
						&& (response.forpaidonly.openlabel != null || response.forpaidonly.openinghrs != null)) {
					createHeader();
					fillData(response);
				} else {
					createHeaderNotPaying();
					fillDataNotPaying(response);
				}
			}
		};
	}

	protected void createHeaderNotPaying() {
		View header = findViewById(R.id.header2);
		mBtnBack = (ImageButton) header.findViewById(R.id.btnBack);
		header.findViewById(R.id.btnSearch).setVisibility(View.GONE);
		header.findViewById(R.id.logo).setVisibility(View.GONE);

		mTvTitleHeader = (TextView) header.findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_ad_detail);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}

	protected void fillDataNotPaying(ShopDetail response) {
		mTvTitleHeader.setText(response.shopname);
		mTvAddrestNotPaying.setText(response.address);
		mTvTelephoneNotPaying.setText(response.telephone);
		startAnimationNotPaying();
	}

	private void startAnimationNotPaying() {
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
				mLlNotPaying.setVisibility(View.VISIBLE);

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
		mLlNotPaying.startAnimation(in);
		mIvLoading.startAnimation(out);
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorDialog newFragment = new ErrorDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<Brand> mListImages;

		public ImageAdapter(Context c, ArrayList<Brand> images) {
			mContext = c;
			mListImages = images;
		}

		public int getCount() {
			return mListImages.size();
		}

		public Brand getItem(int position) {
			return mListImages.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public ImageView logo;
			public TextView brand;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.row_bandist, null);
				holder = new ViewHolder();
				holder.logo = (ImageView) convertView.findViewById(R.id.ivLogo);
				holder.brand = (TextView) convertView
						.findViewById(R.id.tvBrand);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Brand br = getItem(position);
			holder.brand.setText(br.name);
			if (br.img != null && !br.img.isEmpty()) {
				holder.logo.setVisibility(View.VISIBLE);
				holder.brand.setVisibility(View.GONE);
				imageLoader.displayImage(br.img, holder.logo, options,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								holder.logo.setVisibility(View.GONE);
								holder.brand.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								holder.logo.setVisibility(View.VISIBLE);
								holder.brand.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
								holder.logo.setVisibility(View.GONE);
								holder.brand.setVisibility(View.VISIBLE);
							}
						});
			} else {
				holder.logo.setVisibility(View.GONE);
				holder.brand.setVisibility(View.VISIBLE);
			}
			return convertView;
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
				// postCallLog();
			}
		} else {
			if (address != null && !address.isEmpty()) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", address);
				startActivity(smsIntent);
				// postSMSLog();
			}
		}
	}

	@Override
	public void gotLocation(Location location) {
		mLatitude = String.valueOf(location.getLatitude());
		mLongitude = String.valueOf(location.getLongitude());
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ShopDetail> myReq = new GsonRequest<ShopDetail>(Method.GET,
				String.format(Const.URL_BIKESHOP_DETAIL, mShopId, mLatitude,
						mLongitude), ShopDetail.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
		Log.d("haipn",
				"request:"
						+ String.format(Const.URL_BIKESHOP_DETAIL, mShopId,
								mLatitude, mLongitude));
	}
}
