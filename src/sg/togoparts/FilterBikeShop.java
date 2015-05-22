package sg.togoparts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import sg.togoparts.app.Const;
import sg.togoparts.app.MyLocation;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class FilterBikeShop extends FragmentActivity {

	public static final String BIKESHOP_NAME = "bikeshop_name";
	public static final String AREA = "area";
	public static final String OPENNOW = "opennow";
	public static final String MECHANIC = "mechanic";
	// public static final String FROM = "from";
	// public static final String TO = "to";
	// public static final String ADSTATUS = "ad status";
	// public static final String MARKETPLACE_CATEGORY = "marketplace category";
	// public static final String TYPE = "type";
	// public static final String GROUP_ID = "group id";
	// public static final String PARAM = "parameters";
	// public static final String KEYWORD = "keyword";
	// public static final String SHOP_NAME = "shop name";
	public static final String SORT_BY = "sort by";
	private static final String SCREEN_LABEL = "Marketplace Filter Form";

	HashMap<String, String> listArea;

	private RadioGroup mGroupSort;
	private RadioButton mRb1;
	private RadioButton mRb2;
	private RadioButton mRb3;
	private RadioButton mRb4;

	private Spinner mSpnArea;
	private EditText mEdtShopName;
	private CheckBox mCbOpenNow;
	private CheckBox mCbMechanic;

	private LinearLayout headerView;
	private String mStringTitle = "";

	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;
	Button mBtnReset;
	Button mBtnApplyFilter;
	private String mSort;
	private boolean onResult;
	private MyLocation mLoc;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_bikeshop);

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, "Bikeshop Search Form");
		tracker.send(MapBuilder.createAppView().build());

		createHeader();
		mGroupSort = (RadioGroup) findViewById(R.id.group);
		mRb1 = (RadioButton) findViewById(R.id.rbSort1);
		mRb2 = (RadioButton) findViewById(R.id.rbSort2);
		mRb3 = (RadioButton) findViewById(R.id.rbSort3);
		mRb4 = (RadioButton) findViewById(R.id.rbSort4);
		mGroupSort.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.rbSort1:
					mSort = "0";
					break;
				case R.id.rbSort2:
					mSort = "1";
					break;
				case R.id.rbSort3:
					mSort = "2";
					break;
				case R.id.rbSort4:
					mSort = "3";
					break;
				default:
					mSort = "2";
					break;
				}
				Log.d("haipn", "msort = " + mSort);
			}
		});
		mLoc = new MyLocation();

		if (!mLoc.isSupportLocation(this)) {
			mGroupSort.check(R.id.rbSort3);
			mSort = "2";
		} else {
			mGroupSort.check(R.id.rbSort1);
			mSort = "0";
		}
		mEdtShopName = (EditText) findViewById(R.id.edtBikeShop);
		mSpnArea = (Spinner) findViewById(R.id.spnArea);
		mCbMechanic = (CheckBox) findViewById(R.id.cbMechanic);
		mCbOpenNow = (CheckBox) findViewById(R.id.cbOpenNow);

		mBtnReset = (Button) findViewById(R.id.btnReset);
		mBtnApplyFilter = (Button) findViewById(R.id.btnApplyFilter);
		mBtnApplyFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				applyFilter();
			}
		});
		mBtnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				resetData();
			}
		});
		setListValues();
		onResult = getIntent().getExtras() != null;
		if (onResult)
			initSpinner();
		else {
			ArrayAdapter<String> adtArea = new ArrayAdapter<String>(this,
					R.layout.spinner_text);
			adtArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Iterator itCatId = listArea.entrySet().iterator();
			while (itCatId.hasNext()) {
				Map.Entry pairs = (Map.Entry) itCatId.next();
				adtArea.add((String) pairs.getKey());
			}
			mSpnArea.setAdapter(adtArea);
		}
	}

	protected void resetData() {
		mEdtShopName.setText("");

		mSpnArea.setSelection(0);

		mLoc = new MyLocation();

		if (!mLoc.isSupportLocation(this)) {
			mGroupSort.check(R.id.rbSort3);
			mSort = "2";
		} else {
			mGroupSort.check(R.id.rbSort1);
			mSort = "0";
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
		// Const.GA_PROPERTY_ID);
		// tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		// tracker.send(MapBuilder.createAppView().build());
	}

	private void createHeader() {
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);
		mBtnRight = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mIvLogo.setVisibility(View.GONE);

		mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvTitle.setText(R.string.filter_bikeshop_title);
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();

			}
		});
		mBtnRight.setBackgroundResource(R.drawable.btn_apply);
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				applyFilter();
			}
		});
	}

	protected void applyFilter() {
		Bundle bundle = new Bundle();
		bundle.putString(BIKESHOP_NAME, mEdtShopName.getText().toString());
		bundle.putString(AREA, listArea.get(mSpnArea.getSelectedItem()));
		bundle.putBoolean(MECHANIC, mCbMechanic.isChecked());
		bundle.putBoolean(OPENNOW, mCbOpenNow.isChecked());
		bundle.putString(SORT_BY, mSort);
		if (onResult) {
			Intent i = getIntent();
			i.putExtras(bundle);
			setResult(RESULT_OK, i);
		} else {
			Intent i = new Intent(FilterBikeShop.this, TabsActivityMain.class);
			i.putExtra(Const.TAG_NAME, "4");
			i.putExtras(bundle);
			i.setFlags(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB ? Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					: Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
		finish();
	}

	private void setListValues() {
		listArea = new LinkedHashMap<String, String>();

		listArea.put("All in Singapore", "");
		listArea.put("North", "n");
		listArea.put("South", "s");
		listArea.put("East", "e");
		listArea.put("West", "w");
		listArea.put("Central", "c");

	}

	public void initSpinner() {
		Bundle b = getIntent().getExtras();
		mEdtShopName.setText(b.getString(BIKESHOP_NAME));
		int i = 0, d = 0;
		String value = null;

		i = 0;
		value = b.getString(AREA);
		ArrayAdapter<String> adtArea = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCatId = listArea.entrySet().iterator();
		while (itCatId.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCatId.next();
			adtArea.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnArea.setAdapter(adtArea);
		mSpnArea.setSelection(d);

		mCbMechanic.setChecked(b.getBoolean(MECHANIC));
		mCbOpenNow.setChecked(b.getBoolean(OPENNOW));

		mSort = b.getString(SORT_BY);
		if (mLoc.isSupportLocation(this)) {
			if (mSort.equals("0")) {
				mGroupSort.check(R.id.rbSort1);
			} else if (mSort.equals("1")) {
				mGroupSort.check(R.id.rbSort2);
			} else if (mSort.equals("2")) {
				mGroupSort.check(R.id.rbSort3);
			} else
				mGroupSort.check(R.id.rbSort4);
		} else {
			if (mSort.equals("0") || mSort.equals("1") || mSort.equals("2")) {
				mGroupSort.check(R.id.rbSort3);
			} else {
				mGroupSort.check(R.id.rbSort4);
			}
		}
	}

}
