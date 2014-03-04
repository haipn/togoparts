package com.agsi.togopart;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Main extends FragmentActivity implements HeaderView {
	static final String TAG = "FSActivity_Home";
	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragmentstackctivity);
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);
		mBtnRight = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mTvTitle = (TextView) findViewById(R.id.title);
		mBtnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		// add initial fragment, do not add to back stack, no transition
		// animation
	}
	@Override
	public void setLeftButton(int visible) {
		mBtnLeft.setVisibility(visible);
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
}
