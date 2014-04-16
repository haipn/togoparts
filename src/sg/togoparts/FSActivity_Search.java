package sg.togoparts;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import sg.togoparts.R;
import sg.togoparts.app.Const;

public class FSActivity_Search extends FragmentActivity implements HeaderView {
	public static String SCREEN_NAME = "Marketplace Search Form";
	static final String TAG = "FSActivity_Search";
	private static final int FILTER_RETURN = 0;
	private ImageButton mBtnLeft;
	private ImageButton mBtnRight;
	private ImageView mIvLogo;
	private TextView mTvTitle;
	private ProgressBar mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragmentstackctivity);
		// add initial fragment, do not add to back stack, no transition
		// animation
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);

		mBtnRight = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mIvLogo.setVisibility(View.GONE);
		mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setText(R.string.marketplace_search);
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mProgress.setVisibility(View.GONE);
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		addFragment(new SearchFragment(), false,
				FragmentTransaction.TRANSIT_NONE);
		
		Tracker tracker = GoogleAnalytics.getInstance(FSActivity_Search.this)
				.getTracker(Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_NAME);
		tracker.send(MapBuilder.createAppView().build());
	}

	void addFragment(Fragment fragment, boolean addToBackStack, int transition) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.simple_fragment, fragment, fragment.getClass()
				.getName());
		ft.setTransition(transition);
		if (addToBackStack)
			ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void setLeftButton(int visible) {
		try {
			mBtnLeft.setVisibility(visible);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setRightButton(int visible, OnClickListener click) {
		mBtnRight.setVisibility(visible);
		mBtnRight.setOnClickListener(click);
	}

	@Override
	public void setLogoVisible(int visible) {
		mIvLogo.setVisibility(visible);

	}

	@Override
	public void setTitleVisible(int visible, String text) {
		mTvTitle.setVisibility(visible);
		mTvTitle.setText(text);

	}

	@Override
	public void setProgressVisible(int visible) {
		mProgress.setVisibility(visible);
	}

	// @Override
	// protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	// if (arg0 == FILTER_RETURN) {
	// if (arg1 == RESULT_CANCELED) {
	// Log.d("haipn", "result cancel");
	// } else {
	// Fragment fragment =
	// getSupportFragmentManager().findFragmentByTag(SearchResultFragment.class.getName());
	// if (fragment == null)
	// fragment = new SearchResultFragment();
	// fragment.setArguments(arg2.getExtras());
	// FragmentTransaction ft = getSupportFragmentManager()
	// .beginTransaction();
	// ft.replace(R.id.simple_fragment, fragment,
	// fragment.getClass().getName());
	// ft.setTransition(FragmentTransaction.TRANSIT_NONE);
	// ft.commitAllowingStateLoss();
	// Log.d("haipn", "result ok");
	// }
	// }
	// super.onActivityResult(arg0, arg1, arg2);
	// }
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// // if (Const.isAppExitable) {
	// return false;
	// // } else
	// // return super.onKeyDown(keyCode, event);
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}