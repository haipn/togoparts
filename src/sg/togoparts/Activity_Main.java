package sg.togoparts;

import android.app.TabActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Activity_Main extends FragmentActivity implements HeaderView {
	static final String TAG = "FSActivity_Home";
	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;
	ProgressBar mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragmentstackctivity);
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);
		mBtnRight = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mTvTitle = (TextView) findViewById(R.id.title);
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					TabActivity tabs = (TabActivity) getParent();
					tabs.getTabHost().setCurrentTabByTag("1");
				} catch (NullPointerException e) {
					finish();
					e.printStackTrace();
				}
			}
		});
		// add initial fragment, do not add to back stack, no transition
		// animation
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("haipn", "key down on main");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Log.d("haipn" , "home key back");
			// if (Const.isAppExitable) {
			return false;
			// } else
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
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
}
