package com.agsi.togopart;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class MainActivity extends ActionBarActivity  implements ActionBar.TabListener {

	private ViewPager mViewPager;
	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), this);
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		
	    mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setIcon(mAppSectionsPagerAdapter.getIcon(i))
                            .setTabListener(this));
        }
	    if (savedInstanceState != null) {
	        actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
	    }  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
    	for (int i = 0; i < actionBar.getTabCount(); i++) {
			actionBar.getTabAt(i).setIcon(mAppSectionsPagerAdapter.getIcon(i));
			if (tab.equals(actionBar.getTabAt(i))) {
				switch (i) {
				case 0:
					tab.setIcon(R.drawable.home_hover);
					break;
				case 1:
					tab.setIcon(R.drawable.marketplace_hover);
					break;
				case 2:
					tab.setIcon(R.drawable.shortlisted_hover);
					break;
				case 3:
					tab.setIcon(R.drawable.search_hover);
					break;
				default:
					break;
				}
			}
		}
    	
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
	
	/**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
    	Context mContext;
        public AppSectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
            
                case 0:
                    return new HomeFragment();
                case 1:
                	return new MarketPlaceFragment();
                case 2:
                	return new ShortlistedAds();
                default:
                    return new SearchFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
        
        public int getIcon(int position) {
        	switch (position) {
            case 0:
                return R.drawable.home;
            case 1:
            	return R.drawable.marketplace;
            case 2:
            	return R.drawable.shortlisted;
            default:
                return R.drawable.search;
        }
        }
    }
}
