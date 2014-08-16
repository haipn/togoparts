package sg.togoparts.login;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sg.togoparts.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class Price extends Activity {
	EditText mEdtPrice;
	EditText mEdtOriginalPrice;
	Spinner mSpnPriceType;
	Button mBtnSave;
	CheckBox mCbClearance;
	int price, originalPrice, firmNeg;
	boolean clearance;
	HashMap<String, Integer> listPricetype;
	public boolean mIsPostingPack;
	private boolean mIsMerchant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price);
		Intent i = getIntent();
		mIsPostingPack = i.getBooleanExtra(PostAdActivity.POSTING_PACK, false);
		mIsMerchant = i.getBooleanExtra(PostAdActivity.MERCHANT_PACK, false);
		price = i.getIntExtra(PostAdActivity.PRICE, 0);
		originalPrice = i.getIntExtra(PostAdActivity.ORIGINAL_PRICE, 0);
		firmNeg = i.getIntExtra(PostAdActivity.PRICETYPE, 0);
		clearance = i.getBooleanExtra(PostAdActivity.CLEARANCE, false);

		mEdtOriginalPrice = (EditText) findViewById(R.id.edtOriginalPrice);
		mEdtPrice = (EditText) findViewById(R.id.edtPrice);
		mSpnPriceType = (Spinner) findViewById(R.id.spnFirmNeg);
		mBtnSave = (Button) findViewById(R.id.btnSave);
		mCbClearance = (CheckBox) findViewById(R.id.cbClearance);

		if (originalPrice != 0)
			mEdtOriginalPrice.setText(originalPrice + "");
		if (price != 0)
			mEdtPrice.setText(price + "");
		mCbClearance.setChecked(clearance);

		if (mIsMerchant || mIsPostingPack) {
			findViewById(R.id.llOriginalPrice).setVisibility(View.VISIBLE);
		} else {
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
				Intent i = getIntent();
				int price;
				if (mEdtPrice.getText().toString().length() > 0)
					price = Integer.valueOf(mEdtPrice.getText().toString());
				else
					price = 0;
				i.putExtra(PostAdActivity.PRICE, price);
				int original;
				if (mEdtOriginalPrice.getText().toString().length() > 0)
					original = Integer.valueOf(mEdtOriginalPrice.getText()
							.toString());
				else
					original = 0;
				i.putExtra(PostAdActivity.ORIGINAL_PRICE, original);
				i.putExtra(PostAdActivity.PRICETYPE,
						listPricetype.get(mSpnPriceType.getSelectedItem()));
				i.putExtra(PostAdActivity.CLEARANCE, mCbClearance.isChecked());
				setResult(RESULT_OK, i);
				finish();
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

		listPricetype = new HashMap<String, Integer>();
		listPricetype.put("Do not specify", 3);
		listPricetype.put("Firm", 1);
		listPricetype.put("Negotiable", 2);
	}
}
