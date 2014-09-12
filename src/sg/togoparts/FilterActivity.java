package sg.togoparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorInternetDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ListCategories;
import sg.togoparts.json.ListCategories.Shopname;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class FilterActivity extends FragmentActivity {

	public static final String POSTED_BY = "post by";
	public static final String SIZE = "size";
	public static final String VALUE = "value";
	public static final String UNIT = "unit";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String ADSTATUS = "ad status";
	public static final String MARKETPLACE_CATEGORY = "marketplace category";
	public static final String TYPE = "type";
	public static final String GROUP_ID = "group id";
	public static final String PARAM = "parameters";
	public static final String KEYWORD = "keyword";
	public static final String SHOP_NAME = "shop name";
	public static final String SORT_BY = "sort by";
	private static final String SCREEN_LABEL = "Marketplace Filter Form";

	HashMap<String, String> listSize;
	HashMap<String, String> listUnit;
	HashMap<String, String> listAdStatus;
	
	HashMap<String, String> listType;
	HashMap<String, String> listShop;

	private RadioGroup mGroupSort;
	private RadioButton mRb1;
	private RadioButton mRb2;
	private RadioButton mRb3;
	private RadioButton mRb4;

	private Spinner mSpnSize;
	private Spinner mSpnUnit;
	private EditText mEdtValue;
	private EditText mEdtFrom;
	private EditText mEdtTo;
	private Spinner mSpnAdStatus;
	
	private Spinner mSpnType;
	private Spinner mSpnShop;
	private EditText mEdtPostedBy;

	private LinearLayout headerView;
	private String mStringTitle = "";

	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;
	Button mBtnReset;
	Button mBtnApplyFilter;
	private String mSort;
	private Bundle mBundle;
	private PublisherAdView adview;

	protected String presetParamenters() {
		String postedby = mEdtPostedBy.getText().toString();
		String size;
		if (mSpnSize.getSelectedItemPosition() == 0) {
			size = "";
		} else {
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_fragment);
		adview = (PublisherAdView) findViewById(R.id.adView);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ListCategories> myReq = new GsonRequest<ListCategories>(
				Method.GET, Const.URL_SEARCH_CATEGORY, ListCategories.class,
				createMyReqSuccessListener(), createMyReqErrorListener());

		queue.add(myReq);
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
					mSort = "1";
					break;
				case R.id.rbSort2:
					mSort = "2";
					break;
				case R.id.rbSort3:
					mSort = "3";
					break;
				case R.id.rbSort4:
					mSort = "4";
					break;
				default:
					mSort = "3";
					break;
				}
				Log.d("haipn", "msort = " + mSort);
			}
		});
		mGroupSort.check(R.id.rbSort3);
		mSort = "3";

		mEdtFrom = (EditText) findViewById(R.id.edtFrom);
		mEdtPostedBy = (EditText) findViewById(R.id.edtPostedBy);
		mEdtTo = (EditText) findViewById(R.id.edtTo);
		mEdtValue = (EditText) findViewById(R.id.edtValue);

		mSpnAdStatus = (Spinner) findViewById(R.id.spnAdStatus);
		mSpnSize = (Spinner) findViewById(R.id.spnSize);
		mSpnType = (Spinner) findViewById(R.id.spnType);

		mSpnUnit = (Spinner) findViewById(R.id.spnUnit);
		mSpnShop = (Spinner) findViewById(R.id.spnShop);
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
				mEdtFrom.setText("");
				mEdtTo.setText("");
				mEdtPostedBy.setText("");
				mEdtValue.setText("");

				mSpnAdStatus.setSelection(0);
				mSpnSize.setSelection(0);
				mSpnShop.setSelection(0);
				mSpnType.setSelection(0);
				mSpnUnit.setSelection(0);

				mSort = "3";
				mGroupSort.check(R.id.rbSort3);
			}
		});
		mSpnSize.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 != 0) {
					mEdtValue.setEnabled(false);
					mSpnUnit.setEnabled(false);
				} else {
					mEdtValue.setEnabled(true);
					mSpnUnit.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		mEdtValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					mSpnSize.setEnabled(false);
				} else {
					mSpnSize.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		mSpnShop.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					mEdtPostedBy.setEnabled(true);
				} else {
					mEdtPostedBy.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		mEdtPostedBy.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					mSpnShop.setEnabled(false);
				} else {
					mSpnShop.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

	}
	@Override
	protected void onResume() {
		PublisherAdRequest.Builder re = new PublisherAdRequest.Builder();
		adview.loadAd(re.build());
		adview.setVisibility(View.GONE);
		adview.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				adview.setVisibility(View.VISIBLE);
				super.onAdLoaded();
			}
		});
		super.onResume();
	}
	@Override
	public void onStart() {
		super.onStart();
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		tracker.send(MapBuilder.createAppView().build());
	}

	private void createHeader() {
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);
		mBtnRight = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mIvLogo.setVisibility(View.GONE);

		mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvTitle.setText(R.string.filter_title);
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
		mBundle.putString(POSTED_BY, mEdtPostedBy.getText().toString());
		mBundle.putString(SIZE, listSize.get(mSpnSize.getSelectedItem()));
		mBundle.putString(VALUE, mEdtValue.getText().toString());
		mBundle.putString(FROM, mEdtFrom.getText().toString());
		mBundle.putString(TO, mEdtTo.getText().toString());
		mBundle.putString(UNIT, listUnit.get(mSpnUnit.getSelectedItem()));
		mBundle.putString(ADSTATUS,
				listAdStatus.get(mSpnAdStatus.getSelectedItem()));
//		bundle.putString(MARKETPLACE_CATEGORY,
//				listCatId.get(mSpnCatId.getSelectedItem()));
		mBundle.putString(TYPE, listType.get(mSpnType.getSelectedItem()));
		mBundle.putString(SHOP_NAME, listShop.get(mSpnShop.getSelectedItem()));
		mBundle.putString(SORT_BY, mSort);

		Intent i = getIntent();
		i.putExtras(mBundle);
		setResult(RESULT_OK, i);
		finish();
	}

	private void setListValues() {
		listSize = new LinkedHashMap<String, String>();
		listUnit = new LinkedHashMap<String, String>();
		listAdStatus = new LinkedHashMap<String, String>();
		
		listType = new LinkedHashMap<String, String>();
		listShop = new LinkedHashMap<String, String>();

		listSize.put("All", "");
		listSize.put("N-A", "na");
		listSize.put("Free Size", "fs");
		listSize.put("Extra Extra Small", "xxs");
		listSize.put("Extra Small", "xs");
		listSize.put("Small", "s");
		listSize.put("Medium Small", "ms");
		listSize.put("Medium", "m");
		listSize.put("Medium Large", "ml");
		listSize.put("Large", "l");
		listSize.put("Extra Large", "xl");
		listSize.put("Extra Extra Large", "xxl");

		listUnit.put("Unit", "");
		listUnit.put("n-a", "n-a");
		listUnit.put("C", "C");
		listUnit.put("cm", "cm");
		listUnit.put("grams", "grams");
		listUnit.put("inches", "inches");
		listUnit.put("kg", "kg");
		listUnit.put("lbs", "lbs");
		listUnit.put("mm", "mm");
		listUnit.put("speed", "speed");
		listUnit.put("teeth", "teeth");

		listAdStatus.put("All", "0");
		listAdStatus.put("Available", "1");
		listAdStatus.put("Sold", "2");
		listAdStatus.put("Looking", "3");
		listAdStatus.put("Found", "4");
		listAdStatus.put("Given", "5");
		listAdStatus.put("Exchanged", "6");

		listType.put("All", "");
		listType.put("For Sale", "1");
		listType.put("Want to Buy", "2");
		listType.put("Exchange + Cash", "4");
		listType.put("Free!", "3");

	}

	public void initSpinner() {
		mBundle = getIntent().getExtras();
		mEdtFrom.setText(mBundle.getString(FROM));
		mEdtPostedBy.setText(mBundle.getString(POSTED_BY));
		mEdtTo.setText(mBundle.getString(TO));
		mEdtValue.setText(mBundle.getString(VALUE));
		int i = 0, d = 0;
		String value = null;
		i = 0;
		value = mBundle.getString(SHOP_NAME);
		ArrayAdapter<String> adtShop = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itShop = listShop.entrySet().iterator();
		while (itShop.hasNext()) {
			Map.Entry pairs = (Map.Entry) itShop.next();
			adtShop.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnShop.setAdapter(adtShop);
		mSpnShop.setSelection(d);

		i = 0;
		value = mBundle.getString(SIZE);
		ArrayAdapter<String> adtSize = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itSize = listSize.entrySet().iterator();
		while (itSize.hasNext()) {
			Map.Entry pairs = (Map.Entry) itSize.next();
			adtSize.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnSize.setAdapter(adtSize);
		mSpnSize.setSelection(d);

		i = 0;
		value = mBundle.getString(TYPE);
		ArrayAdapter<String> adtType = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itType = listType.entrySet().iterator();
		while (itType.hasNext()) {
			Map.Entry pairs = (Map.Entry) itType.next();
			adtType.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnType.setAdapter(adtType);
		mSpnType.setSelection(d);
		resetListAdStatus(d);

		mSpnType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				resetListAdStatus(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		i = 0;
		ArrayAdapter<String> adtCategory = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCat = listAdStatus.entrySet().iterator();
		value = mBundle.getString(ADSTATUS);
		Log.d("haipn", "value:" + value);
		while (itCat.hasNext()) {

			Map.Entry pairs = (Map.Entry) itCat.next();
			adtCategory.add((String) pairs.getKey());
			Log.d("haipn",
					"key:" + pairs.getKey() + " value:" + pairs.getValue());
			if (value != null && value.equals(pairs.getValue())) {
				Log.d("haipn", "true");
				d = i;
			}
			i++;
		}
		Log.d("haipn", "d = " + d);
		adtCategory
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnAdStatus.setAdapter(adtCategory);
		mSpnAdStatus.setSelection(d);

		i = 0;
		value = mBundle.getString(UNIT);
		ArrayAdapter<String> adtUnit = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itUnit = listUnit.entrySet().iterator();
		while (itUnit.hasNext()) {
			Map.Entry pairs = (Map.Entry) itUnit.next();
			adtUnit.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnUnit.setAdapter(adtUnit);
		mSpnUnit.setSelection(d);

		mSort = mBundle.getString(SORT_BY);
		if (mSort.equals("1")) {
			mGroupSort.check(R.id.rbSort1);
		} else if (mSort.equals("2")) {
			mGroupSort.check(R.id.rbSort2);
		} else if (mSort.equals("4")) {
			mGroupSort.check(R.id.rbSort4);
		} else
			mGroupSort.check(R.id.rbSort3);
	}

	protected void resetListAdStatus(int arg2) {
		switch (arg2) {
		case 1:
			listAdStatus.clear();
			listAdStatus.put("All", "0");
			listAdStatus.put("Available", "1");
			listAdStatus.put("Sold", "2");
			break;
		case 2:
			listAdStatus.clear();
			listAdStatus.put("All", "0");
			listAdStatus.put("Looking", "3");
			listAdStatus.put("Found", "4");
			break;
		case 3:
			listAdStatus.clear();
			listAdStatus.put("All", "0");
			listAdStatus.put("Available", "1");
			listAdStatus.put("Exchanged", "6");
			break;
		case 4:
			listAdStatus.clear();
			listAdStatus.put("All", "0");
			listAdStatus.put("Available", "1");
			listAdStatus.put("Given", "5");
			break;
		default:
			listAdStatus.clear();
			listAdStatus.put("All", "0");
			listAdStatus.put("Available", "1");
			listAdStatus.put("Sold", "2");
			listAdStatus.put("Looking", "3");
			listAdStatus.put("Found", "4");
			listAdStatus.put("Given", "5");
			listAdStatus.put("Exchanged", "6");
			break;
		}
		int i = 0, d = 0;
		Bundle b = getIntent().getExtras();
		ArrayAdapter<String> adtCategory = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCat = listAdStatus.entrySet().iterator();
		String value = b.getString(ADSTATUS);
		Log.d("haipn", "value:" + value);
		while (itCat.hasNext()) {

			Map.Entry pairs = (Map.Entry) itCat.next();
			adtCategory.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				Log.d("haipn", "true");
				d = i;
			}
			i++;
		}
		adtCategory
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnAdStatus.setAdapter(adtCategory);
		mSpnAdStatus.setSelection(d);
	}

	private Response.Listener<ListCategories> createMyReqSuccessListener() {
		return new Response.Listener<ListCategories>() {
			@Override
			public void onResponse(ListCategories response) {
				setListValues();
//				ArrayList<Cat> list = response.mp_categories;
//				// listCatId.put("All", "");
//				for (int i = 0; i < list.size(); i++) {
//					listCatId.put(list.get(i).title, list.get(i).value);
//				}

				ArrayList<Shopname> shops = response.bikeshops;
				for (int i = 0; i < shops.size(); i++) {
					listShop.put(shops.get(i).shopname, shops.get(i).value);
				}
				initSpinner();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorInternetDialog newFragment = new ErrorInternetDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}
}
