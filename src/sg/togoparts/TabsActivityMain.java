package sg.togoparts;

import sg.togoparts.app.Const;
import sg.togoparts.login.ChooseLogin;
import sg.togoparts.login.FSActivity_PostAd;
import sg.togoparts.login.PostAdActivity;
import sg.togoparts.login.FSActivity_Profile;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.doubleclick.DfpAdView;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

@SuppressWarnings("deprecation")
public class TabsActivityMain extends TabActivity implements AdListener {
	private DfpAdView dfpAdView;
	RelativeLayout rlAdMain;
	boolean isAdShown = false;
	TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_activity_main);

		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, FSActivity_Home.class);
		spec = tabHost
				.newTabSpec("1")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_marketplace))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FSActivity_Profile.class);
		spec = tabHost
				.newTabSpec("2")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_myads))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FSActivity_PostAd.class);
		spec = tabHost
				.newTabSpec("3")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_sell))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FSActivity_BikeShop.class);
		Bundle b = getIntent().getExtras();
		if (b == null) {
			b = new Bundle();
			b.putString(FilterBikeShop.AREA, "");
			b.putString(FilterBikeShop.BIKESHOP_NAME, "");
			b.putString(FilterBikeShop.SORT_BY, "0");
			b.putBoolean(FilterBikeShop.MECHANIC, false);
			b.putBoolean(FilterBikeShop.OPENNOW, false);
		}

		intent.putExtras(b);
		spec = tabHost
				.newTabSpec("4")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_bikeshop))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MoreActivity.class);
		spec = tabHost
				.newTabSpec("5")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_more))
				.setContent(intent);
		tabHost.addTab(spec);

		getTabWidget().setStripEnabled(false);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				Log.d("haipn", "tabid:" + tabId);
				Tracker tracker = GoogleAnalytics.getInstance(
						TabsActivityMain.this).getTracker(Const.GA_PROPERTY_ID);

				if (tabId.equals("1")) {
					tracker.set(Fields.SCREEN_NAME,
							"Marketplace List Categories");
					Const.isAppExitable = true;
				} else {
					if (tabId.equals("2")) {
						if (Const.isLogin(TabsActivityMain.this))
							tracker.set(Fields.SCREEN_NAME, "Profile");

						else {
							startActivity(new Intent(TabsActivityMain.this,
									ChooseLogin.class));
							tabHost.setCurrentTabByTag("1");
						}
					} else if (tabId.equals("3")) {

						if (Const.isLogin(TabsActivityMain.this)) {
							tracker.set(Fields.SCREEN_NAME, "Post Ad");
							startActivity(new Intent(TabsActivityMain.this,
									PostAdActivity.class));

						} else
							startActivity(new Intent(TabsActivityMain.this,
									ChooseLogin.class));
						tabHost.setCurrentTabByTag("1");
					} else if (tabId.equals("4")) {
						tracker.set(Fields.SCREEN_NAME, "Bikeshop Listing");
					} else {

					}
					Const.isAppExitable = false;
				}
				tracker.send(MapBuilder.createAppView().build());
			}
		});
		// Create the interstitial
		// interstitial = new DfpInterstitialAd(this,
		// "/78685291/Android320x480");
		// // interstitial = new DfpInterstitialAd(this,
		// "/78685291/Android640x960");
		//
		// // Create ad request
		// AdRequest adRequest = new AdRequest();
		//
		// DfpExtras extras = new DfpExtras();
		// extras.addExtra("screenWidth", getScreenWidthInDp());
		// extras.addExtra("screenHeight", getScreenHeightInDp());
		// adRequest.setNetworkExtras(extras);
		// // Begin loading your interstitial
		// interstitial.loadAd(adRequest);
		//
		// // Set Ad Listener to use the callbacks below
		// interstitial.setAdListener(this);

		rlAdMain = (RelativeLayout) findViewById(R.id.rl_main_adview);
		rlAdMain.setVisibility(View.GONE);
		dfpAdView = new DfpAdView(this, new AdSize(320, 480),
				"/4689451/Android320x480");

		AdRequest adRequest = new AdRequest();

		LinearLayout llAdContainer = (LinearLayout) findViewById(R.id.ll_ad_container);
		llAdContainer.addView(dfpAdView);

		ImageView ivCloseAd = (ImageView) findViewById(R.id.iv_ad_close);
		ivCloseAd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideAd();
			}
		});

		dfpAdView.loadAd(adRequest);
		dfpAdView.setAdListener(this);
		if (getIntent().getStringExtra(Const.TAG_NAME) == null)
			tabHost.setCurrentTabByTag("1");
		else {
			tabHost.setCurrentTabByTag(getIntent().getStringExtra(
					Const.TAG_NAME));
			hideAd();
		}
	}

	private int getScreenWidthInDp() {
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		return (int) (dm.widthPixels / dm.density);
	}

	private int getScreenHeightInDp() {
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		return (int) (dm.heightPixels / dm.density);
	}

	@Override
	public void onReceiveAd(Ad ad) {
		Log.e("Ad DFP", "onReceiveAd");
		showAd();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("haipn", "onKey down");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("haipn", "key back");
			if (isAdShown) {
				Log.d("haipn", "showadd");
				hideAd();
				return false;
			} else {
				Log.d("haipn", "hide ad");
				if (Const.isAppExitable) {
					Log.d("haipn", "is exit abble");
					showAlert();
					return false;
				} else {
					Log.d("haipn", "not exit abble");
					tabHost.setCurrentTabByTag("1");
					Const.isAppExitable = true;
					return false;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showAd() {
		rlAdMain.setVisibility(View.VISIBLE);
		isAdShown = true;
	}

	private void hideAd() {
		rlAdMain.setVisibility(View.GONE);
		dfpAdView.stopLoading();
		isAdShown = false;
	}

	private void showAlert() {
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to close application?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).setNegativeButton("No", null).show();
	}

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onDismissScreen");
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onFailedToReceiveAd");
		hideAd();
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onLeaveApplication");
		hideAd();
	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onPresentScreen");
	}
}
