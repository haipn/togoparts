package sg.togoparts.login;

import java.util.ArrayList;

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
	boolean mMtb, mRoad, mCommute, mFolding, mBmx, mOther;
	String mBrand, mModel, mTitle, mDescription, mYear;
	int mTranstype;


	ArrayList<String> mListBrand;
	ArrayList<String> mListModel;
	private ArrayAdapter<String> adapterBrand;
	private ArrayAdapter<String> adapterModel;
	
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
		mFolding =  i.getBooleanExtra(PostAdActivity.D_FOLDING, false);
		mBmx =  i.getBooleanExtra(PostAdActivity.D_BMX, false);
		mOther =  i.getBooleanExtra(PostAdActivity.D_OTHERS, false);
		
		mBrand = i.getStringExtra(PostAdActivity.BRAND);
		mModel = i.getStringExtra(PostAdActivity.ITEM);
		mTitle = i.getStringExtra(PostAdActivity.TITLE);
		mDescription = i.getStringExtra(PostAdActivity.DESCRIPTION);
		mYear = i.getStringExtra(PostAdActivity.ITEM_YEAR);
		mTranstype = i.getIntExtra(PostAdActivity.TRANSTYPE, 0);
		
		mCbBmx.setChecked(mBmx);
		mCbCommute.setChecked(mCommute);
		mCbFolding.setChecked(mFolding);
		mCbMtb.setChecked(mMtb);
		mCbOther.setChecked(mOther);
		mCbRoad.setChecked(mRoad);
		
//		mEdtDescription.setText(mDescription);
//		mEdtTitle.setText(mTitle);
//		mAtvBrand.setText(mBrand);
//		mAtvModel.setText(mModel);
		
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
									mAtvBrand.getText().toString()), BrandResult.class,
							createBrandSuccessListener(),
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
						Method.GET, String.format(Const.URL_GET_MODEL, s.toString()),
						ModelResult.class, createModelSuccessListener(),
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
							Method.GET, String.format(Const.URL_GET_MODEL, mAtvModel.getText().toString()),
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
			}
		});
	}

	private void init() {
		mAtvBrand = (AutoCompleteTextView) findViewById(R.id.atvBrand);
		mListBrand = new ArrayList<String>();
		adapterBrand = new ArrayAdapter<String>(
				ItemInfo.this,
				android.R.layout.simple_dropdown_item_1line,
				mListBrand);
		adapterBrand.setNotifyOnChange(true);
		mAtvBrand.setAdapter(adapterBrand);
		
		mSpnYear = (Spinner) findViewById(R.id.spnYear);
		mAtvModel = (AutoCompleteTextView) findViewById(R.id.atvModel);
		mListModel = new ArrayList<String>();
		adapterModel = new ArrayAdapter<String>(
				ItemInfo.this,
				android.R.layout.simple_dropdown_item_1line,
				mListModel);
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
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
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
