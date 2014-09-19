package sg.togoparts.pro.login;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import sg.togoparts.pro.R;
import sg.togoparts.pro.app.Const;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Price extends Activity {
	EditText mEdtPrice;
	EditText mEdtOriginalPrice;
	Spinner mSpnPriceType;
	Button mBtnSave;
	CheckBox mCbClearance;
	int firmNeg;
	double price, originalPrice;
	boolean clearance;
	HashMap<String, Integer> listPricetype;
	public boolean mIsPostingPack;
	private boolean mIsMerchant;
	private ImageButton mBtnBack;
	private ImageButton mBtnApply;

	private ProgressBar mProgress;
	private TextView mTvTitleHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price);
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, "Marketplace "
				+ getIntent().getStringExtra("screen") + " Price");
		tracker.send(MapBuilder.createAppView().build());
		createHeader();
		Intent i = getIntent();
		mIsPostingPack = i.getBooleanExtra(PostAdActivity.POSTING_PACK, false);
		mIsMerchant = i.getBooleanExtra(PostAdActivity.MERCHANT_PACK, false);
		DecimalFormat df = new DecimalFormat("###.##");
		price = i.getDoubleExtra(PostAdActivity.PRICE, 0);
		originalPrice = i.getDoubleExtra(PostAdActivity.ORIGINAL_PRICE, 0);
		firmNeg = i.getIntExtra(PostAdActivity.PRICETYPE, 0);
		clearance = i.getBooleanExtra(PostAdActivity.CLEARANCE, false);

		mEdtOriginalPrice = (EditText) findViewById(R.id.edtOriginalPrice);
		mEdtPrice = (EditText) findViewById(R.id.edtPrice);
		mSpnPriceType = (Spinner) findViewById(R.id.spnFirmNeg);
		mBtnSave = (Button) findViewById(R.id.btnSave);
		mCbClearance = (CheckBox) findViewById(R.id.cbClearance);

		if (originalPrice != 0)
			mEdtOriginalPrice.setText(df.format(originalPrice));
		if (price != 0)
			mEdtPrice.setText(df.format(price));
		mCbClearance.setChecked(clearance);

		if (mIsMerchant || mIsPostingPack) {
			findViewById(R.id.tvLabelOriginal).setVisibility(View.VISIBLE);
			findViewById(R.id.llOriginalPrice).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.tvLabelOriginal).setVisibility(View.GONE);
			findViewById(R.id.llOriginalPrice).setVisibility(View.GONE);
		}

		if (mIsPostingPack)
			findViewById(R.id.llClearance).setVisibility(View.VISIBLE);
		else
			findViewById(R.id.llClearance).setVisibility(View.GONE);
		setListValues();
		initSpinner();

		mBtnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveButton();
			}
		});
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		mBtnApply = (ImageButton) findViewById(R.id.btnSearch);
		mBtnApply.setBackgroundResource(R.drawable.btn_apply_icon);
		mBtnApply.setVisibility(View.VISIBLE);
		findViewById(R.id.logo).setVisibility(View.INVISIBLE);
		mBtnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveButton();
			}
		});
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_price);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}

	private void initSpinner() {
		int i = 0, d = 0;

		ArrayAdapter<String> adtPricetype = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itTranstype = listPricetype.entrySet().iterator();
		while (itTranstype.hasNext()) {
			Map.Entry pairs = (Map.Entry) itTranstype.next();
			adtPricetype.add((String) pairs.getKey());
			if (firmNeg == (Integer) pairs.getValue()) {
				d = i;
			}
			i++;
		}
		adtPricetype
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnPriceType.setAdapter(adtPricetype);
		mSpnPriceType.setSelection(d);
	}

	private void setListValues() {

		listPricetype = new LinkedHashMap<String, Integer>();
		listPricetype.put("Do not specify", 3);
		listPricetype.put("Firm", 1);
		listPricetype.put("Negotiable", 2);
	}

	private void saveButton() {
		Intent i = getIntent();
		double price;
		if (mEdtPrice.getText().toString().length() > 0)
			price = Double.valueOf(mEdtPrice.getText().toString());
		else
			price = 0;

		i.putExtra(PostAdActivity.PRICE, price);
		double original;
		if (mEdtOriginalPrice.getText().toString().length() > 0)
			original = Double.valueOf(mEdtOriginalPrice.getText().toString());
		else
			original = 0;
		i.putExtra(PostAdActivity.ORIGINAL_PRICE, original);
		i.putExtra(PostAdActivity.PRICETYPE,
				listPricetype.get(mSpnPriceType.getSelectedItem()));
		i.putExtra(PostAdActivity.CLEARANCE, mCbClearance.isChecked());
		setResult(RESULT_OK, i);
		finish();
	}
}
