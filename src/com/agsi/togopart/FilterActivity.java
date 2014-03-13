package com.agsi.togopart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.ErrorDialog;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.ListCategories;
import com.agsi.togopart.json.ListCategories.Cat;
import com.agsi.togopart.json.ListCategories.Shopname;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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

	HashMap<String, String> listSize;
	HashMap<String, String> listUnit;
	HashMap<String, String> listAdStatus;
	HashMap<String, String> listCatId;
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
	private Spinner mSpnCatId;
	private Spinner mSpnType;
	private Spinner mSpnShop;
	private EditText mEdtPostedBy;

	private LinearLayout headerView;
	private String mStringTitle = "";

	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;
	private String mSort;

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
		mSpnCatId = (Spinner) findViewById(R.id.spnCatId);
		mSpnSize = (Spinner) findViewById(R.id.spnSize);
		mSpnType = (Spinner) findViewById(R.id.spnType);
		mSpnUnit = (Spinner) findViewById(R.id.spnUnit);
		mSpnShop = (Spinner) findViewById(R.id.spnShop);
		
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
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
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
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
	}

	private void createHeader() {
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);
		mBtnRight = (ImageButton) findViewById(R.id.btnSearch);
		mIvLogo = (ImageView) findViewById(R.id.logo);
		mTvTitle = (TextView) findViewById(R.id.title);
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
				Bundle bundle = new Bundle();
				bundle.putString(POSTED_BY, mEdtPostedBy.getText().toString());
				bundle.putString(SIZE, listSize.get(mSpnSize.getSelectedItem()));
				bundle.putString(VALUE, mEdtValue.getText().toString());
				bundle.putString(FROM, mEdtFrom.getText().toString());
				bundle.putString(TO, mEdtTo.getText().toString());
				bundle.putString(UNIT, listUnit.get(mSpnUnit.getSelectedItem()));
				bundle.putString(ADSTATUS,
						listAdStatus.get(mSpnAdStatus.getSelectedItem()));
				bundle.putString(MARKETPLACE_CATEGORY,
						listCatId.get(mSpnCatId.getSelectedItem()));
				bundle.putString(TYPE, listType.get(mSpnType.getSelectedItem()));
				bundle.putString(SHOP_NAME,
						listShop.get(mSpnShop.getSelectedItem()));
				bundle.putString(SORT_BY, mSort);

				Intent i = getIntent();
				i.putExtras(bundle);
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}

	private void setListValues() {
		listSize = new LinkedHashMap<String, String>();
		listUnit = new LinkedHashMap<String, String>();
		listAdStatus = new LinkedHashMap<String, String>();
		listCatId = new LinkedHashMap<String, String>();
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
		listAdStatus.put("Gone", "5");
		listAdStatus.put("Exchanged", "6");

		listType.put("All", "");
		listType.put("For Sale", "1");
		listType.put("Want to Buy", "2");
		listType.put("Exchange + Cash", "3");
		listType.put("Free!", "4");

	}

	public void initSpinner() {
		Bundle b = getIntent().getExtras();
		mEdtFrom.setText(b.getString(FROM));
		mEdtPostedBy.setText(b.getString(POSTED_BY));
		mEdtTo.setText(b.getString(TO));
		mEdtValue.setText(b.getString(VALUE));

		ArrayAdapter<String> adtCategory = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCat = listAdStatus.entrySet().iterator();
		String value = b.getString(ADSTATUS);
		Log.d("haipn", "value:" + value);
		// if (value == null)
		// value = "";
		int i = 0, d = 0;
		while (itCat.hasNext()) {

			Map.Entry pairs = (Map.Entry) itCat.next();
			adtCategory.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtCategory
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnAdStatus.setAdapter(adtCategory);
		mSpnAdStatus.setSelection(d);

		i = 0;
		value = b.getString(MARKETPLACE_CATEGORY);
		ArrayAdapter<String> adtCatId = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCatId = listCatId.entrySet().iterator();
		while (itCatId.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCatId.next();
			adtCatId.add((String) pairs.getKey());
			if (value != null && value.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtCatId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnCatId.setAdapter(adtCatId);
		mSpnCatId.setSelection(d);

		i = 0;
		value = b.getString(SHOP_NAME);
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
		value = b.getString(SIZE);
		// if (value == null)
		// value = "";
		Log.d("haipn", "value:" + value);
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
		value = b.getString(TYPE);
		// if (value == null)
		// value = "";
		Log.d("haipn", "value:" + value);
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

		i = 0;
		value = b.getString(UNIT);
		// if (value == null)
		// value = "";
		Log.d("haipn", "value:" + value);
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

		mSort = b.getString(SORT_BY);
		if (mSort.equals("1")) {
			mGroupSort.check(R.id.rbSort1);
		} else if (mSort.equals("2")) {
			mGroupSort.check(R.id.rbSort2);
		} else if (mSort.equals("4")) {
			mGroupSort.check(R.id.rbSort4);
		} else
			mGroupSort.check(R.id.rbSort3);
	}

	private Response.Listener<ListCategories> createMyReqSuccessListener() {
		return new Response.Listener<ListCategories>() {
			@Override
			public void onResponse(ListCategories response) {
				setListValues();
				ArrayList<Cat> list = response.mp_categories;
				// listCatId.put("All", "");
				for (int i = 0; i < list.size(); i++) {
					listCatId.put(list.get(i).title, list.get(i).value);
				}

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
				ErrorDialog newFragment = new ErrorDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}
}
