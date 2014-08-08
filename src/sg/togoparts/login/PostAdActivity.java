package sg.togoparts.login;

import sg.togoparts.HeaderView;
import sg.togoparts.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class PostAdActivity extends Activity {

	private TextView mTvCategory;
	private TextView mTvItem;
	private TextView mTvPrice;
	private TextView mTvContact;

	private RadioButton mRdoFreeAd;
	private RadioButton mRdoPriorityAd;
	private RadioButton mRdoNewItemAd;
	private HeaderView headerView;
	private ImageButton mBtnBack;
	private ImageButton mBtnSearch;
	private ImageView mIvLogo;
	private TextView mTvTitleHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_ad);
		createHeader();
		init();
		setListener();
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		mBtnSearch = (ImageButton) findViewById(R.id.btnSearch);
		findViewById(R.id.logo).setVisibility(View.GONE);

		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_postad);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		mBtnSearch.setVisibility(View.VISIBLE);
		mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}

	private void init() {
		mRdoFreeAd = (RadioButton) findViewById(R.id.rdoFreeAd);
		mRdoNewItemAd = (RadioButton) findViewById(R.id.rdoNewItemAd);
		mRdoPriorityAd = (RadioButton) findViewById(R.id.rdoPriorityAd);

		mTvCategory = (TextView) findViewById(R.id.tvCategory);
		mTvItem = (TextView) findViewById(R.id.tvItem);
		mTvPrice = (TextView) findViewById(R.id.tvPrice);
		mTvContact = (TextView) findViewById(R.id.tvContact);

	}

	private void setListener() {
		mTvCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PostAdActivity.this,
						SectionActivity.class);
				startActivity(i);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Log.d("haipn" , "home key back");
			// if (Const.isAppExitable) {
			return false;
			// } else
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
}
