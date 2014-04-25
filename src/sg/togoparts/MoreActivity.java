package sg.togoparts;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MoreActivity extends Activity_Main {
	TextView mTvSearchAds;
	TextView mTvSearchShop;
	TextView mTvAbout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);

		createHeader();
		mTvSearchAds = (TextView) findViewById(R.id.tvAdsSearch);
		mTvSearchShop = (TextView) findViewById(R.id.tvShopSearch);
		mTvAbout = (TextView) findViewById(R.id.tvAbout);

		mTvSearchAds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MoreActivity.this,
						FSActivity_Search.class));
			}
		});

		mTvSearchShop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Bundle b = new Bundle();
//				b.putString(FilterBikeShop.AREA, "");
//				b.putString(FilterBikeShop.BIKESHOP_NAME, "");
//				b.putString(FilterBikeShop.SORT_BY, "2");
//				b.putBoolean(FilterBikeShop.MECHANIC, true);
//				b.putBoolean(FilterBikeShop.OPENNOW, true);
				Intent i = new Intent(MoreActivity.this,
						FilterBikeShop.class);
//				i.putExtras(b);
				startActivity(i);
			}
		});
		mTvAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MoreActivity.this, AboutActivity.class));
			}
		});
	}

	private void createHeader() {
		findViewById(R.id.btnBack).setVisibility(View.GONE);
		findViewById(R.id.btnSearch).setVisibility(View.GONE);
		findViewById(R.id.logo).setVisibility(View.GONE);

		TextView mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvTitle.setText(R.string.more);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (Const.isAppExitable) {
			return false;
			// } else
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
}
