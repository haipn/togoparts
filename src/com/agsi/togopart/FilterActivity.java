package com.agsi.togopart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.ListCategories;
import com.agsi.togopart.json.ListCategories.Cat;
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
	public static final String CATEGORY = "category";
	public static final String MARKETPLACE_CATEGORY = "marketplace category";
	public static final String TYPE = "type";
	
	HashMap<String, String> listSize;
	HashMap<String, String> listUnit;
	HashMap<String, String> listCat;
	HashMap<String, String> listCatId;
	HashMap<String, String> listType;
	
	private EditText mEdtPostedBy;
	private Spinner mSpnSize;
	private Spinner mSpnUnit;
	private EditText mEdtValue;
	private EditText mEdtFrom;
	private EditText mEdtTo;
	private Spinner mSpnCategory;
	private Spinner mSpnCatId;
	private Spinner mSpnType;
	private LinearLayout headerView;
	private String mStringTitle = "";

	ImageButton mBtnLeft;
	ImageButton mBtnRight;
	ImageView mIvLogo;
	TextView mTvTitle;

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
		mEdtFrom = (EditText) findViewById(R.id.edtFrom);
		mEdtPostedBy = (EditText) findViewById(R.id.edtPostedBy);
		mEdtTo = (EditText) findViewById(R.id.edtTo);
		mEdtValue = (EditText) findViewById(R.id.edtValue);

		mSpnCategory = (Spinner) findViewById(R.id.spnCategory);
		mSpnCatId = (Spinner) findViewById(R.id.spnCatId);
		mSpnSize = (Spinner) findViewById(R.id.spnSize);
		mSpnType = (Spinner) findViewById(R.id.spnType);
		mSpnUnit = (Spinner) findViewById(R.id.spnUnit);

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

		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString(Const.QUERY, getIntent().getStringExtra(Const.QUERY));
				bundle.putString(POSTED_BY, mEdtPostedBy.getText().toString());
				bundle.putString(SIZE, listSize.get(mSpnSize.getSelectedItemPosition()));
				bundle.putString(VALUE, mEdtValue.getText().toString());
				bundle.putString(FROM, mEdtFrom.getText().toString());
				bundle.putString(TO, mEdtTo.getText().toString());
				bundle.putString(UNIT, listUnit.get(mSpnUnit.getSelectedItemPosition()));
				bundle.putString(CATEGORY, listCat.get(mSpnCategory.getSelectedItemPosition()));
				bundle.putString(MARKETPLACE_CATEGORY, listCatId.get(mSpnCatId.getSelectedItemPosition()));
				bundle.putString(TYPE, listType.get(mSpnType.getSelectedItemPosition()));
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
		listCat = new LinkedHashMap<String, String>();
		listCatId = new LinkedHashMap<String, String>();
		listType = new LinkedHashMap<String, String>();

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

		listCat.put("All", "");
		listCat.put("Commercial Merchants", "1");
		listCat.put("Commercial Member", "2");
		listCat.put("Priority", "3");

		listType.put("All", "");
		listType.put("For Sale", "1");
		listType.put("Want to Buy", "2");
		listType.put("Exchange + Cash", "3");
		listType.put("Free!", "4");
	}

	public void initSpinner() {
		ArrayAdapter<String> adtCategory = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCat = listCat.entrySet().iterator();
		while (itCat.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCat.next();
			adtCategory.add((String) pairs.getKey());
		}
		adtCategory
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnCategory.setAdapter(adtCategory);

		ArrayAdapter<String> adtCatId = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCatId = listCatId.entrySet().iterator();
		while (itCatId.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCatId.next();
			adtCatId.add((String) pairs.getKey());
		}
		adtCatId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnCatId.setAdapter(adtCatId);

		ArrayAdapter<String> adtSize = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itSize = listSize.entrySet().iterator();
		while (itSize.hasNext()) {
			Map.Entry pairs = (Map.Entry) itSize.next();
			adtSize.add((String) pairs.getKey());
		}
		adtSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnSize.setAdapter(adtSize);

		ArrayAdapter<String> adtType = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itType = listType.entrySet().iterator();
		while (itType.hasNext()) {
			Map.Entry pairs = (Map.Entry) itType.next();
			adtType.add((String) pairs.getKey());
		}
		adtType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnType.setAdapter(adtType);

		ArrayAdapter<String> adtUnit = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itUnit = listUnit.entrySet().iterator();
		while (itUnit.hasNext()) {
			Map.Entry pairs = (Map.Entry) itUnit.next();
			adtUnit.add((String) pairs.getKey());
		}
		adtUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnUnit.setAdapter(adtUnit);
	}

	private Response.Listener<ListCategories> createMyReqSuccessListener() {
		return new Response.Listener<ListCategories>() {
			@Override
			public void onResponse(ListCategories response) {
				setListValues();
				ArrayList<Cat> list = response.mp_categories;
				listCatId.put("All", "");
				for (int i = 0; i < list.size(); i++) {
					listCatId.put(list.get(i).title, list.get(i).value);
				}
				initSpinner();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO: show dialog error
			}
		};
	}
}
