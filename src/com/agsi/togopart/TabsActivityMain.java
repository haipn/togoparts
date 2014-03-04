package com.agsi.togopart;

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

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.doubleclick.DfpAdView;

@SuppressWarnings("deprecation")
public class TabsActivityMain extends TabActivity implements AdListener
{
	private DfpAdView dfpAdView;
	RelativeLayout rlAdMain;
	boolean isAdShown = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_activity_main);
		
//		String android_id = Secure.getString(getApplicationContext().getContentResolver(),
//                Secure.ANDROID_ID); 
//
//        ads:testDevices="4c1aaf1e7e9ea04a"
		final TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
//		try
//		{
//			tabHost.getTabWidget().setDividerDrawable(R.drawable.footer_divider);
//		}
//		catch (Exception e)
//		{
//			
//		}
		
		intent = new Intent().setClass(this, FSActivity_Home.class);
		spec = tabHost.newTabSpec("1").setIndicator(null,getResources().getDrawable(R.drawable.tab_home)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FSActivity_MarketPlace.class);
		spec = tabHost.newTabSpec("2").setIndicator(null,getResources().getDrawable(R.drawable.tab_marketplace)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FSActivity_ShortList.class);
		spec = tabHost.newTabSpec("3").setIndicator(null,getResources().getDrawable(R.drawable.tab_shortlist)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FSActivity_Search.class);
		spec = tabHost.newTabSpec("4").setIndicator(null,getResources().getDrawable(R.drawable.tab_search)).setContent(intent);
		tabHost.addTab(spec);
		
		getTabWidget().setStripEnabled(false);
		
//		tabHost.setOnTabChangedListener(new OnTabChangeListener()
//		{
//			@Override
//			public void onTabChanged(String tabId)
//			{
//				Globals.isAppExitable = false;
//			}
//		});
		// Create the interstitial
//	    interstitial = new DfpInterstitialAd(this, "/78685291/Android320x480");
////		interstitial = new DfpInterstitialAd(this, "/78685291/Android640x960");
//
//	    // Create ad request
//	    AdRequest adRequest = new AdRequest();
//	    
//	    DfpExtras extras = new DfpExtras();
//		extras.addExtra("screenWidth", getScreenWidthInDp());
//		extras.addExtra("screenHeight", getScreenHeightInDp());
//		adRequest.setNetworkExtras(extras);
//	    // Begin loading your interstitial
//	    interstitial.loadAd(adRequest);
//
//	    // Set Ad Listener to use the callbacks below
//	    interstitial.setAdListener(this);
		
		rlAdMain = (RelativeLayout)findViewById(R.id.rl_main_adview);
		
//		dfpAdView = (DfpAdView)findViewById(R.id.adView);
		if(getScreenWidthInDp() >= 540)
		{
			dfpAdView = new DfpAdView(this, new AdSize(540, 810), "/78685291/Android540x810");
		}
		else if(getScreenWidthInDp() >= 360)
		{
			dfpAdView = new DfpAdView(this, new AdSize(360, 540), "/78685291/Android360x540");
		}
		else
		{
			dfpAdView = new DfpAdView(this, new AdSize(320, 480), "/78685291/Android320x480");
		}
		
		
		AdRequest adRequest = new AdRequest();

		LinearLayout llAdContainer = (LinearLayout)findViewById(R.id.ll_ad_container);
		llAdContainer.addView(dfpAdView);
		
		ImageView ivCloseAd = (ImageView)findViewById(R.id.iv_ad_close);
		ivCloseAd.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hideAd();
			}
		});
		
		dfpAdView.loadAd(adRequest);
		dfpAdView.setAdListener(this);
		
		int i = getScreenWidthInDp();
		Log.e("Screen Width", String.valueOf(i));
	  }
	
	private int getScreenWidthInDp()
	{
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		return (int) (dm.widthPixels / dm.density);
	}

	private int getScreenHeightInDp()
	{
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		return (int) (dm.heightPixels / dm.density);
	}
	
	  @Override
	  public void onReceiveAd(Ad ad) 
	  {
		  Log.e("Ad DFP", "onReceiveAd");
		  showAd();
	  }

	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
        	if(isAdShown)
        	{
        		hideAd();
        		return false;
        	}
//        	else
//        	{
//        		if(Globals.isAppExitable)
//            	{	showAlert();
//            		return false;
//            	}
//        	}
        }
        return super.onKeyDown(keyCode, event);
    }
	
	private void showAd()
	{
		rlAdMain.setVisibility(View.VISIBLE);
		isAdShown = true;
	}
	
	private void hideAd()
	{
		rlAdMain.setVisibility(View.GONE);
		dfpAdView.stopLoading();
		isAdShown = false;
	}
	
	private void showAlert()
	{
		new AlertDialog.Builder(this)
        .setTitle("Confirm Quit")
        .setMessage("You really want to quit?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
        {

            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
            	finish();
            }

        })
        .setNegativeButton("No", null)
        .show();
	}

	@Override
	public void onDismissScreen(Ad arg0)
	{
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onDismissScreen");
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1)
	{
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onFailedToReceiveAd");
	}

	@Override
	public void onLeaveApplication(Ad arg0)
	{
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onLeaveApplication");
		hideAd();
	}

	@Override
	public void onPresentScreen(Ad arg0)
	{
		// TODO Auto-generated method stub
		Log.e("Ad DFP", "onPresentScreen");
	}
}
