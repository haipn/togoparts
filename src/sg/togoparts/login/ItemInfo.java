package sg.togoparts.login;

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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
	private ImageButton mBtnNextTop;
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
	Spinner mSpnCondition;
	EditText mEdtWarranty;
	EditText mEdtPictureLink;
	Button mBtnSave;
	boolean mMtb, mRoad, mCommute, mFolding, mBmx, mOther;
	String mBrand, mModel, mTitle, mDescription, mYear, mSize, mColour,
			mPictureLink;
	int mTranstype, mWeight, mCondition, mWarranty;

	private ArrayAdapter<String> adapterBrand;
	private ArrayAdapter<String> adapterModel;
	HashMap<String, String> listSize;
	HashMap<String, Integer> listTranstype;
	HashMap<String, String> listYear;
	HashMap<String, String> listColour;
	HashMap<String, Integer> listCondition;
	private BrandRunable mBrandRunnable;
	private ModelRunable mModelRunnable;
	private BrandWatcher brandWatcher;
	private ModelWatcher modelWatcher;
	protected boolean mEnableBrandWatcher = true;
	protected boolean mEnableModelWatcher = true;
	public boolean mIsEdit;

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
		mIsEdit = i.getBooleanExtra(PostAdActivity.EDIT_AD, false);
		if (mIsEdit) {
			mAtvBrand.setEnabled(false);
			mAtvModel.setEnabled(false);
			mSpnYear.setEnabled(false);
			mEdtTitle.setEnabled(false);
			mSpnTranstype.setEnabled(false);
		}
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
		if (mWarranty > 0)
			mEdtWarranty.setText(mWarranty + "");
		if (mWeight > 0)
			mEdtWeight.setText(mWeight + "");
		setListValues();
		initSpinner();

		mBrandRunnable = new BrandRunable();
		mModelRunnable = new ModelRunable();
		brandWatcher = new BrandWatcher();
		modelWatcher = new ModelWatcher();
	}

	private void setListener() {

		mAtvBrand.addTextChangedListener(brandWatcher);
		mAtvBrand.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("haipn", "auto click:" + position);
				mEnableBrandWatcher = false;
			}
		});

		mAtvBrand.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mAtvBrand.showDropDown();
					mEnableBrandWatcher = true;
				} else {
					mBrandHandler.removeCallbacks(mBrandRunnable);
				}

			}
		});
		mAtvModel.addTextChangedListener(modelWatcher);
		mAtvModel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mEnableModelWatcher = false;
			}

		});
		mAtvModel.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mAtvModel.showDropDown();
					mEnableModelWatcher = true;
				} else {
					mModelHandler.removeCallbacks(mModelRunnable);
				}
			}
		});

		mBtnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextScreen();
			}
		});

		mBtnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveButton();
			}
		});

		mCbBmx.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					mCbOther.setChecked(false);
			}
		});

		mCbCommute.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					mCbOther.setChecked(false);
			}
		});

		mCbFolding.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					mCbOther.setChecked(false);
			}
		});

		mCbMtb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					mCbOther.setChecked(false);
			}
		});

		mCbOther.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mCbBmx.setChecked(false);
					mCbCommute.setChecked(false);
					mCbFolding.setChecked(false);
					mCbMtb.setChecked(false);
					mCbRoad.setChecked(false);
				}
			}
		});

		mCbRoad.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					mCbOther.setChecked(false);
			}
		});

		mBtnSelectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCbBmx.setChecked(true);
				mCbCommute.setChecked(true);
				mCbFolding.setChecked(true);
				mCbMtb.setChecked(true);
				mCbRoad.setChecked(true);
			}
		});
	}

	private class ModelWatcher implements TextWatcher {

		public void afterTextChanged(Editable s) {
			mModelHandler.removeCallbacks(mModelRunnable);
			if (mEnableModelWatcher) {
				mModelRunnable.setData(s.toString());
				mModelHandler.postDelayed(mModelRunnable, 1500);
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	private class BrandWatcher implements TextWatcher {

		public void afterTextChanged(Editable s) {
			Log.d("haipn", "text change:" + s.toString());
			mBrandHandler.removeCallbacks(mBrandRunnable);
			if (mEnableBrandWatcher) {
				mBrandRunnable.setData(s.toString());
				mBrandHandler.postDelayed(mBrandRunnable, 1500);
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	private Handler mModelHandler = new Handler();
	private Handler mBrandHandler = new Handler();

	private class ModelRunable implements Runnable {
		String data;

		public void setData(String s) {
			data = s;
		}

		@Override
		public void run() {
			if (data != null && data.length() > 0 && mEnableModelWatcher) {
				mProgress.setVisibility(View.VISIBLE);
				RequestQueue queue = MyVolley.getRequestQueue();
				GsonRequest<ModelResult> brandReq = new GsonRequest<ModelResult>(
						Method.GET, String.format(Const.URL_GET_MODEL, data),
						ModelResult.class, createModelSuccessListener(),
						createMyReqErrorListener());
				queue.add(brandReq);
			}
		}

	}

	private class BrandRunable implements Runnable {
		String data;

		public void setData(String s) {
			data = s;
		}

		@Override
		public void run() {
			if (data != null && data.length() > 0 && mEnableBrandWatcher) {
				mProgress.setVisibility(View.VISIBLE);
				RequestQueue queue = MyVolley.getRequestQueue();
				GsonRequest<BrandResult> brandReq = new GsonRequest<BrandResult>(
						Method.GET, String.format(Const.URL_GET_BRAND, data),
						BrandResult.class, createBrandSuccessListener(),
						createMyReqErrorListener());
				queue.add(brandReq);
			}
		}

	}

	private void init() {
		mAtvBrand = (AutoCompleteTextView) findViewById(R.id.atvBrand);
		// mListBrand = new ArrayList<String>();
		// adapterBrand = new ArrayAdapter<String>(ItemInfo.this,
		// android.R.layout.simple_dropdown_item_1line, mListBrand);
		// adapterBrand.setNotifyOnChange(true);
		// mAtvBrand.setAdapter(adapterBrand);
		//
		mSpnYear = (Spinner) findViewById(R.id.spnYear);
		mAtvModel = (AutoCompleteTextView) findViewById(R.id.atvModel);
		// mListModel = new ArrayList<String>();
		// adapterModel = new ArrayAdapter<String>(ItemInfo.this,
		// android.R.layout.simple_dropdown_item_1line, mListModel);
		// adapterModel.setNotifyOnChange(true);
		// mAtvModel.setAdapter(adapterModel);

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
		mSpnCondition = (Spinner) findViewById(R.id.spnCondition);
		mEdtPictureLink = (EditText) findViewById(R.id.edtOtherLinks);
		mEdtWarranty = (EditText) findViewById(R.id.edtWarranty);
		mEdtWeight = (EditText) findViewById(R.id.edtWeight);

		mBtnSave = (Button) findViewById(R.id.btnSave);
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		mBtnNextTop = (ImageButton) findViewById(R.id.btnSearch);
		mBtnNextTop.setVisibility(View.VISIBLE);
		mBtnNextTop.setBackgroundResource(R.drawable.btn_next);
		mBtnNextTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextScreen();
			}
		});
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
					previouScreen();
				}
			}
		});
	}

	private void setListValues() {
		listSize = new LinkedHashMap<String, String>();
		listColour = new LinkedHashMap<String, String>();
		listTranstype = new LinkedHashMap<String, Integer>();
		listYear = new LinkedHashMap<String, String>();
		listCondition = new LinkedHashMap<String, Integer>();

		listSize.put("", "");
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

		listColour.put("", "");
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
		listYear.put("", "");
		for (int i = 0; i <= 20; i++) {
			listYear.put(String.valueOf(year - i), String.valueOf(year - i));
		}

		listTranstype.put("Want to Sell", 1);
		listTranstype.put("Want to Buy", 2);
		listTranstype.put("Free", 3);
		listTranstype.put("Exchange + Cash", 4);

		listCondition.put("", 0);
		for (int i = 10; i > 0; i--) {
			listCondition.put(String.valueOf(i), i);
		}
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

		i = d = 0;

		ArrayAdapter<String> adtCondition = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCondition = listCondition.entrySet().iterator();
		while (itCondition.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCondition.next();
			adtCondition.add((String) pairs.getKey());
			if (mCondition == (Integer) pairs.getValue()) {
				d = i;
			}
			i++;
		}
		adtCondition
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnCondition.setAdapter(adtCondition);
		mSpnCondition.setSelection(d);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mSwitcher.getDisplayedChild() == 0)
			super.onBackPressed();
		else {
			previouScreen();
		}
	}

	private Listener<BrandResult> createBrandSuccessListener() {
		return new Response.Listener<BrandResult>() {

			@Override
			public void onResponse(BrandResult response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response != null && response.Result != null
						&& response.Result.Brands != null) {
					// adapterBrand.clear();
					// mListBrand = response.Result.Brands;
					// for (String item : mListBrand) {
					// adapterBrand.add(item);
					// }
					// adapterBrand.notifyDataSetChanged();
					adapterBrand = new ArrayAdapter<String>(ItemInfo.this,
							android.R.layout.simple_dropdown_item_1line,
							response.Result.Brands);
					adapterBrand.setNotifyOnChange(true);
					mAtvBrand.setAdapter(adapterBrand);
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
				if (response != null && response.Result != null
						&& response.Result.Models != null) {
					// adapterModel.clear();
					// mListModel = response.Result.Models;
					// for (String item : mListModel) {
					// adapterModel.add(item);
					// }
					// adapterModel.notifyDataSetChanged();
					adapterModel = new ArrayAdapter<String>(ItemInfo.this,
							android.R.layout.simple_dropdown_item_1line,
							response.Result.Models);
					adapterModel.setNotifyOnChange(true);
					mAtvModel.setAdapter(adapterModel);
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

	private void nextScreen() {
		if (mEdtTitle.getText().length() == 0) {
			Toast.makeText(this, R.string.msg_input_title, Toast.LENGTH_LONG)
					.show();
			mEdtTitle.requestFocus();
			return;
		}

		if (mEdtDescription.getText().length() == 0) {
			Toast.makeText(this, R.string.msg_input_description,
					Toast.LENGTH_LONG).show();
			mEdtDescription.requestFocus();
			return;
		}
		mSwitcher.showNext();
		mTvTitleHeader.setText(R.string.title_more_ad_detail);
		mBtnNextTop.setBackgroundResource(R.drawable.btn_apply_icon);
		mBtnNextTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveButton();
			}
		});
	}

	private void previouScreen() {
		mSwitcher.showPrevious();
		mTvTitleHeader.setText(R.string.title_item_info);
		mBtnNextTop.setBackgroundResource(R.drawable.btn_next);
		mBtnNextTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextScreen();
			}
		});
	}

	private void saveButton() {
		Intent data = getIntent();
		data.putExtra(PostAdActivity.D_MTB, mCbMtb.isChecked());
		data.putExtra(PostAdActivity.D_ROAD, mCbRoad.isChecked());
		data.putExtra(PostAdActivity.D_COMMUTE, mCbCommute.isChecked());
		data.putExtra(PostAdActivity.D_FOLDING, mCbFolding.isChecked());
		data.putExtra(PostAdActivity.D_BMX, mCbBmx.isChecked());
		data.putExtra(PostAdActivity.D_OTHERS, mCbOther.isChecked());

		data.putExtra(PostAdActivity.BRAND, mAtvBrand.getText().toString());
		data.putExtra(PostAdActivity.ITEM, mAtvModel.getText().toString());
		data.putExtra(PostAdActivity.TITLE, mEdtTitle.getText().toString());
		data.putExtra(PostAdActivity.DESCRIPTION, mEdtDescription.getText()
				.toString());

		data.putExtra(PostAdActivity.ITEM_YEAR,
				listYear.get(mSpnYear.getSelectedItem()));
		int trans = listTranstype.get(mSpnTranstype.getSelectedItem());
		data.putExtra(PostAdActivity.TRANSTYPE, trans);
		data.putExtra(PostAdActivity.SIZE,
				listSize.get(mSpnSize.getSelectedItem()));
		data.putExtra(PostAdActivity.COLOUR,
				listColour.get(mSpnColour.getSelectedItem()));
		data.putExtra(PostAdActivity.PICTURELINK, mEdtPictureLink.getText()
				.toString());

		int weight;
		try {
			weight = Integer.valueOf(mEdtWeight.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			weight = 0;
		}
		data.putExtra(PostAdActivity.WEIGHT, weight);
		int warranty;
		try {
			warranty = Integer.valueOf(mEdtWarranty.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			warranty = 0;
		}
		data.putExtra(PostAdActivity.WARRANTY, warranty);
		int condition = listCondition.get(mSpnCondition.getSelectedItem());
		data.putExtra(PostAdActivity.CONDITION, condition);
		setResult(RESULT_OK, data);
		finish();
	}

}
