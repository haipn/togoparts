package sg.togoparts.login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.BrandResult;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ModelResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class ItemInfo extends Activity {
	private ImageButton mBtnBack;
	private TextView mTvTitleHeader;
	private ProgressBar mProgress;

	AutoCompleteTextView mAtvBrand;
	Spinner mSpnYear;
	AutoCompleteTextView mAtvModel;
	Spinner mSpnTranstype;
	EditText mEdtTitle;
	CheckBox mCbMtb;
	CheckBox mCbRoad;
	CheckBox mCbCommute;
	CheckBox mCbFolding;
	CheckBox mCbBmx;
	CheckBox mCbOther;

	Button mBtnSelectAll;
	EditText mEdtDescription;
	Button mBtnNext;
	ViewSwitcher mSwitcher;

	Spinner mSpnSize;
	Spinner mSpnColour;
	EditText mEdtWeight;
	EditText mEdtCondition;
	EditText mEdtWarranty;
	EditText mEdtPictureLink;
	Button mBtnSave;
	boolean mMtb, mRoad, mCommute, mFolding, mBmx, mOther;
	String mBrand, mModel, mTitle, mDescription, mYear, mSize, mColour,
			mPictureLink;
	int mTranstype, mWeight, mCondition, mWarranty;

	ArrayList<String> mListBrand;
	ArrayList<String> mListModel;
	private ArrayAdapter<String> adapterBrand;
	private ArrayAdapter<String> adapterModel;
	HashMap<String, String> listSize;
	HashMap<String, Integer> listTranstype;
	HashMap<String, String> listYear;
	HashMap<String, String> listColour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_info);
		createHeader();
		init();
		getValuesIntent();
		setListener();
		// RequestQueue queue = MyVolley.getRequestQueue();
		// GsonRequest<BrandResult> brandReq = new GsonRequest<BrandResult>(
		// Method.GET, String.format(Const.URL_GET_BRAND, ""),
		// BrandResult.class, createBrandSuccessListener(),
		// createMyReqErrorListener());
		// queue.add(brandReq);
		//
		// GsonRequest<ModelResult> modelReq = new GsonRequest<ModelResult>(
		// Method.GET, String.format(Const.URL_GET_MODEL, ""),
		// ModelResult.class, createModelSuccessListener(),
		// createMyReqErrorListener());
		// queue.add(modelReq);
	}

	private void getValuesIntent() {
		Intent i = getIntent();
		mMtb = i.getBooleanExtra(PostAdActivity.D_MTB, false);
		mRoad = i.getBooleanExtra(PostAdActivity.D_ROAD, false);
		mCommute = i.getBooleanExtra(PostAdActivity.D_COMMUTE, false);
		mFolding = i.getBooleanExtra(PostAdActivity.D_FOLDING, false);
		mBmx = i.getBooleanExtra(PostAdActivity.D_BMX, false);
		mOther = i.getBooleanExtra(PostAdActivity.D_OTHERS, false);

		mBrand = i.getStringExtra(PostAdActivity.BRAND);
		mModel = i.getStringExtra(PostAdActivity.ITEM);
		mTitle = i.getStringExtra(PostAdActivity.TITLE);
		mDescription = i.getStringExtra(PostAdActivity.DESCRIPTION);
		mYear = i.getStringExtra(PostAdActivity.ITEM_YEAR);
		mTranstype = i.getIntExtra(PostAdActivity.TRANSTYPE, 0);
		mSize = i.getStringExtra(PostAdActivity.SIZE);
		mColour = i.getStringExtra(PostAdActivity.COLOUR);
		mPictureLink = i.getStringExtra(PostAdActivity.PICTURELINK);
		mWeight = i.getIntExtra(PostAdActivity.WEIGHT, 0);
		mWarranty = i.getIntExtra(PostAdActivity.WARRANTY, 0);
		mCondition = i.getIntExtra(PostAdActivity.CONDITION, 0);

		mCbBmx.setChecked(mBmx);
		mCbCommute.setChecked(mCommute);
		mCbFolding.setChecked(mFolding);
		mCbMtb.setChecked(mMtb);
		mCbOther.setChecked(mOther);
		mCbRoad.setChecked(mRoad);

		if (mDescription != null)
			mEdtDescription.setText(mDescription);
		if (mTitle != null)
			mEdtTitle.setText(mTitle);
		if (mBrand != null)
			mAtvBrand.setText(mBrand);
		if (mModel != null)
			mAtvModel.setText(mModel);
		mEdtWarranty.setText(mWarranty + "");
		mEdtCondition.setText(mCondition + "");
		mEdtWeight.setText(mWeight + "");
		setListValues();
		initSpinner();
	}

	private void setListener() {

		mAtvBrand.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				mProgress.setVisibility(View.VISIBLE);
				RequestQueue queue = MyVolley.getRequestQueue();
				GsonRequest<BrandResult> brandReq = new GsonRequest<BrandResult>(
						Method.GET, String.format(Const.URL_GET_BRAND,
								s.toString()), BrandResult.class,
						createBrandSuccessListener(),
						createMyReqErrorListener());
				queue.add(brandReq);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		mAtvBrand.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mProgress.setVisibility(View.VISIBLE);
					RequestQueue queue = MyVolley.getRequestQueue();
					GsonRequest<BrandResult> brandReq = new GsonRequest<BrandResult>(
							Method.GET, String.format(Const.URL_GET_BRAND,
									mAtvBrand.getText().toString()),
							BrandResult.class, createBrandSuccessListener(),
							createMyReqErrorListener());
					queue.add(brandReq);
				}

			}
		});
		mAtvModel.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				mProgress.setVisibility(View.VISIBLE);
				RequestQueue queue = MyVolley.getRequestQueue();
				GsonRequest<ModelResult> modelReq = new GsonRequest<ModelResult>(
						Method.GET, String.format(Const.URL_GET_MODEL,
								s.toString()), ModelResult.class,
						createModelSuccessListener(),
						createMyReqErrorListener());
				queue.add(modelReq);

				Log.d("haipn", "afterTextchange:" + s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		mAtvModel.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mProgress.setVisibility(View.VISIBLE);
					RequestQueue queue = MyVolley.getRequestQueue();
					GsonRequest<ModelResult> modelReq = new GsonRequest<ModelResult>(
							Method.GET, String.format(Const.URL_GET_MODEL,
									mAtvModel.getText().toString()),
							ModelResult.class, createModelSuccessListener(),
							createMyReqErrorListener());
					queue.add(modelReq);
				}
			}
		});

		mBtnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSwitcher.showNext();
				mTvTitleHeader.setText(R.string.title_more_ad_detail);
			}
		});

		mBtnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent data = getIntent();
				data.putExtra(PostAdActivity.D_MTB, mCbMtb.isChecked());
				data.putExtra(PostAdActivity.D_ROAD, mCbRoad.isChecked());
				data.putExtra(PostAdActivity.D_COMMUTE, mCbCommute.isChecked());
				data.putExtra(PostAdActivity.D_FOLDING, mCbFolding.isChecked());
				data.putExtra(PostAdActivity.D_BMX, mCbBmx.isChecked());
				data.putExtra(PostAdActivity.D_OTHERS, mCbOther.isChecked());

				data.putExtra(PostAdActivity.BRAND, mAtvBrand.getText()
						.toString());
				data.putExtra(PostAdActivity.ITEM, mAtvModel.getText()
						.toString());
				data.putExtra(PostAdActivity.TITLE, mEdtTitle.getText()
						.toString());
				data.putExtra(PostAdActivity.DESCRIPTION, mEdtDescription
						.getText().toString());

				data.putExtra(PostAdActivity.ITEM_YEAR,
						listYear.get(mSpnYear.getSelectedItem()));
				int trans = listTranstype.get(mSpnTranstype.getSelectedItem());
				data.putExtra(PostAdActivity.TRANSTYPE, trans);
				data.putExtra(PostAdActivity.SIZE,
						listSize.get(mSpnSize.getSelectedItem()));
				data.putExtra(PostAdActivity.COLOUR,
						listColour.get(mSpnColour.getSelectedItem()));
				data.putExtra(PostAdActivity.PICTURELINK, mEdtPictureLink
						.getText().toString());
				int weight = Integer.valueOf(mEdtWeight.getText().toString());
				data.putExtra(PostAdActivity.WEIGHT, weight);
				int warranty = Integer.valueOf(mEdtWarranty.getText()
						.toString());
				data.putExtra(PostAdActivity.WARRANTY, warranty);
				int condition = Integer.valueOf(mEdtCondition.getText()
						.toString());
				data.putExtra(PostAdActivity.CONDITION, condition);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	private void init() {
		mAtvBrand = (AutoCompleteTextView) findViewById(R.id.atvBrand);
		mListBrand = new ArrayList<String>();
		adapterBrand = new ArrayAdapter<String>(ItemInfo.this,
				android.R.layout.simple_dropdown_item_1line, mListBrand);
		adapterBrand.setNotifyOnChange(true);
		mAtvBrand.setAdapter(adapterBrand);

		mSpnYear = (Spinner) findViewById(R.id.spnYear);
		mAtvModel = (AutoCompleteTextView) findViewById(R.id.atvModel);
		mListModel = new ArrayList<String>();
		adapterModel = new ArrayAdapter<String>(ItemInfo.this,
				android.R.layout.simple_dropdown_item_1line, mListModel);
		adapterModel.setNotifyOnChange(true);
		mAtvModel.setAdapter(adapterModel);

		mSpnTranstype = (Spinner) findViewById(R.id.spnTranstype);
		mEdtTitle = (EditText) findViewById(R.id.edtTitle);
		mCbBmx = (CheckBox) findViewById(R.id.cbBmx);
		mCbRoad = (CheckBox) findViewById(R.id.cbRoad);
		mCbCommute = (CheckBox) findViewById(R.id.cbCommute);
		mCbFolding = (CheckBox) findViewById(R.id.cbFolding);
		mCbMtb = (CheckBox) findViewById(R.id.cbMtb);
		mCbOther = (CheckBox) findViewById(R.id.cbOther);
		mBtnSelectAll = (Button) findViewById(R.id.btnSelectAll);
		mBtnNext = (Button) findViewById(R.id.btnNext);
		mSwitcher = (ViewSwitcher) findViewById(R.id.flipper);

		mEdtDescription = (EditText) findViewById(R.id.edtDescription);

		mSpnSize = (Spinner) findViewById(R.id.spnSize);
		mSpnColour = (Spinner) findViewById(R.id.spnColour);
		mEdtCondition = (EditText) findViewById(R.id.edtCondition);
		mEdtPictureLink = (EditText) findViewById(R.id.edtOtherLinks);
		mEdtWarranty = (EditText) findViewById(R.id.edtWarranty);
		mEdtWeight = (EditText) findViewById(R.id.edtWeight);

		mBtnSave = (Button) findViewById(R.id.btnSave);
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.INVISIBLE);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_item_info);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSwitcher.getDisplayedChild() == 0) {
					setResult(RESULT_CANCELED);
					onBackPressed();
				} else {
					mSwitcher.showPrevious();
				}
			}
		});
	}

	private void setListValues() {
		listSize = new LinkedHashMap<String, String>();
		listColour = new HashMap<String, String>();
		listTranstype = new HashMap<String, Integer>();
		listYear = new HashMap<String, String>();

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

		listColour.put("Choose Colour", "");
		listColour.put("Beige", "Beige");
		listColour.put("Black", "Black");
		listColour.put("Blue", "Blue");
		listColour.put("Brown", "Brown");
		listColour.put("Gold", "Gold");
		listColour.put("Green", "Green");
		listColour.put("Grey", "Grey");
		listColour.put("Orange", "Orange");
		listColour.put("Purple", "Purple");
		listColour.put("Red", "Red");
		listColour.put("Silver", "Silver");
		listColour.put("White", "White");
		listColour.put("Yellow", "Yellow");
		listColour.put("MultiColour", "MultiColour");

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		for (int i = 0; i < 20; i++) {
			listYear.put(String.valueOf(year - i), String.valueOf(year - i));
		}

		listTranstype.put("Want to Sell", 1);
		listTranstype.put("Want to Buy", 2);
		listTranstype.put("Free", 3);
		listTranstype.put("Exchange + Cash", 4);
	}

	public void initSpinner() {
		int i = 0, d = 0;

		i = 0;
		ArrayAdapter<String> adtSize = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itSize = listSize.entrySet().iterator();
		while (itSize.hasNext()) {
			Map.Entry pairs = (Map.Entry) itSize.next();
			adtSize.add((String) pairs.getKey());
			if (mSize != null && mSize.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnSize.setAdapter(adtSize);
		mSpnSize.setSelection(d);

		i = 0;
		d = 0;
		ArrayAdapter<String> adtColour = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itColour = listColour.entrySet().iterator();
		while (itColour.hasNext()) {
			Map.Entry pairs = (Map.Entry) itColour.next();
			adtColour.add((String) pairs.getKey());
			if (mColour != null && mColour.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtColour
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnColour.setAdapter(adtColour);
		mSpnColour.setSelection(d);

		i = 0;
		d = 0;
		ArrayAdapter<String> adtYear = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itYear = listYear.entrySet().iterator();
		while (itYear.hasNext()) {
			Map.Entry pairs = (Map.Entry) itYear.next();
			adtYear.add((String) pairs.getKey());
			if (mYear != null && mYear.equals(pairs.getValue())) {
				d = i;
			}
			i++;
		}
		adtYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnYear.setAdapter(adtYear);
		mSpnYear.setSelection(d);

		i = d = 0;

		ArrayAdapter<String> adtTranstype = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itTranstype = listTranstype.entrySet().iterator();
		while (itTranstype.hasNext()) {
			Map.Entry pairs = (Map.Entry) itTranstype.next();
			adtTranstype.add((String) pairs.getKey());
			if (mTranstype == (Integer) pairs.getValue()) {
				d = i;
			}
			i++;
		}
		adtTranstype
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnTranstype.setAdapter(adtTranstype);
		mSpnTranstype.setSelection(d);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mSwitcher.getDisplayedChild() == 0)
			super.onBackPressed();
		else {
			mSwitcher.showPrevious();
		}
	}

	private Listener<BrandResult> createBrandSuccessListener() {
		return new Response.Listener<BrandResult>() {

			@Override
			public void onResponse(BrandResult response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response.Result != null && response.Result.Brands != null) {
					adapterBrand.clear();
					mListBrand = response.Result.Brands;
					for (String item : mListBrand) {
						adapterBrand.add(item);
					}
					adapterBrand.notifyDataSetChanged();
					mAtvBrand.showDropDown();
				}
			}
		};
	}

	private Listener<ModelResult> createModelSuccessListener() {
		return new Response.Listener<ModelResult>() {

			@Override
			public void onResponse(ModelResult response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response.Result != null && response.Result.Models != null) {
					adapterModel.clear();
					mListModel = response.Result.Models;
					for (String item : mListModel) {
						adapterModel.add(item);
					}
					adapterModel.notifyDataSetChanged();
					mAtvModel.showDropDown();
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
				mProgress.setVisibility(View.INVISIBLE);
			}
		};
	}

}
