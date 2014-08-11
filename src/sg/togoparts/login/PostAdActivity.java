package sg.togoparts.login;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import sg.togoparts.HeaderView;
import sg.togoparts.R;
import sg.togoparts.json.PostAd;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class PostAdActivity extends Activity {

	public static final String AID = "aid";
	public static final String ADTYPE = "adtype";
	public static final String CITY = "city";
	public static final String REGION = "region";
	public static final String COUNTRY = "country";
	public static final String POSTALCODE = "postalcode";
	public static final String ADDRESS = "address";
	public static final String LAT = "lat";
	public static final String LONGITUDE = "long";
	public static final String CONTACTNO = "contactno";
	public static final String CONTACTPERSON = "contactperson";
	public static final String TIME_TO_CONTACT = "time_to_contact";
	public static final String SECTION = "section";
	public static final String CAT = "cat";
	public static final String SUB_CAT = "sub_cat";
	public static final String BRAND = "brand";
	public static final String ITEM = "item";
	public static final String ITEM_YEAR = "item_year";
	public static final String TITLE = "title";
	public static final String TRANSTYPE = "transtype";
	public static final String DESCRIPTION = "description";
	public static final String D_MTB = "d_mtb";
	public static final String D_ROAD = "d_road";
	public static final String D_COMMUTE = "d_commute";
	public static final String D_FOLDING = "d_folding";
	public static final String D_BMX = "d_bmx";
	public static final String D_OTHERS = "d_others";
	public static final String SIZE = "size";
	public static final String COLOUR = "colour";
	public static final String WEIGHT = "weight";
	public static final String CONDITION = "condition";
	public static final String WARRANTY = "warranty";
	public static final String PICTURELINK = "picturelink";
	public static final String PRICE = "price";
	public static final String PRICETYPE = "pricetype";
	public static final String ORIGINAL_PRICE = "original_price";
	public static final String CLEARANCE = "clearance";
	public static final String ADPIC1 = "adpic1";
	public static final String ADPIC2 = "adpic2";
	public static final String ADPIC3 = "adpic3";
	public static final String ADPIC4 = "adpic4";
	public static final String ADPIC5 = "adpic5";
	public static final String ADPIC6 = "adpic6";
	public static final int REQUEST_CATEGORY = 0;
	public static final int REQUEST_ITEM = 1;
	public static final int REQUEST_PRICE = 2;
	public static final int REQUEST_CONTACT = 3;

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
	private PostAd mPostAd;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_ad);
		mPostAd = new PostAd();
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
				startActivityForResult(i, REQUEST_CATEGORY);
			}
		});
		mTvItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PostAdActivity.this, ItemInfo.class);
				i.putExtra(BRAND, mPostAd.getBrand());
				i.putExtra(ITEM_YEAR, mPostAd.getItem_year());
				i.putExtra(ITEM, mPostAd.getItem());
				i.putExtra(TITLE, mPostAd.getTitle());
				i.putExtra(TRANSTYPE, mPostAd.getTranstype());
				i.putExtra(DESCRIPTION, mPostAd.getDescription());
				i.putExtra(D_MTB, mPostAd.isD_mtb());
				i.putExtra(D_BMX, mPostAd.isD_bmx());
				i.putExtra(D_COMMUTE, mPostAd.isD_commute());
				i.putExtra(D_FOLDING, mPostAd.isD_folding());
				i.putExtra(D_OTHERS, mPostAd.isD_others());
				i.putExtra(D_ROAD, mPostAd.isD_road());
				i.putExtra(SIZE, mPostAd.getSize());
				i.putExtra(COLOUR, mPostAd.getColour());
				i.putExtra(WEIGHT, mPostAd.getWeight());
				i.putExtra(CONDITION, mPostAd.getCondition());
				i.putExtra(WARRANTY, mPostAd.getWarranty());
				i.putExtra(PICTURELINK, PICTURELINK);
				
				startActivityForResult(i, REQUEST_ITEM);
			}
		});
		mTvPrice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PostAdActivity.this, Price.class);
				i.putExtra(PRICE, mPostAd.getPrice());
				i.putExtra(PRICETYPE,mPostAd.getPricetype());
				i.putExtra(ORIGINAL_PRICE, mPostAd.getOriginal_price());
				i.putExtra(CLEARANCE, mPostAd.isClearance());
				startActivityForResult(i, REQUEST_PRICE);
				
			}
		});
		
		mTvContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PostAdActivity.this, ContactInfo.class);
				i.putExtra(CITY, mPostAd.getCity());
				i.putExtra(REGION, mPostAd.getRegion());
				i.putExtra(COUNTRY, mPostAd.getCountry());
				i.putExtra(POSTALCODE, mPostAd.getPostalcode());
				i.putExtra(ADDRESS, mPostAd.getAddress());
				i.putExtra(LAT, mPostAd.getLatitude());
				i.putExtra(LONGITUDE, mPostAd.getLongitude());
				i.putExtra(CONTACTNO, mPostAd.getContactno());
				i.putExtra(CONTACTPERSON, mPostAd.getContactperson());
				i.putExtra(TIME_TO_CONTACT, mPostAd.getTime_to_contact());
				startActivityForResult(i, REQUEST_CONTACT);
				
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CATEGORY:
			if (resultCode == RESULT_OK) {
				
				mPostAd.setSection(data.getIntExtra(SECTION, 0));
				mPostAd.setCat(data.getIntExtra(CAT, 0));
				mPostAd.setSub_cat(data.getIntExtra(SUB_CAT, 0));
				
				Log.d("haipn", "Section:" + mPostAd.getSection() +  ", category:" + mPostAd.getCat() + ", sub cat:" + mPostAd.getSub_cat());
			}
			break;
		case REQUEST_ITEM: 
			if (resultCode == RESULT_OK) {
				mPostAd.setBrand(data.getStringExtra(BRAND));
				mPostAd.setItem_year(data.getStringExtra(ITEM_YEAR));
				mPostAd.setItem(data.getStringExtra(ITEM));
				mPostAd.setTitle(data.getStringExtra(TITLE));
				mPostAd.setTranstype(data.getIntExtra(TRANSTYPE, 1));
				mPostAd.setDescription(data.getStringExtra(DESCRIPTION));
				mPostAd.setD_bmx(data.getBooleanExtra(D_BMX, false));
				mPostAd.setD_commute(data.getBooleanExtra(D_COMMUTE, false));
				mPostAd.setD_folding(data.getBooleanExtra(D_FOLDING, false));
				mPostAd.setD_mtb(data.getBooleanExtra(D_MTB, false));
				mPostAd.setD_others(data.getBooleanExtra(D_OTHERS, false));
				mPostAd.setD_road(data.getBooleanExtra(D_ROAD, false));
				mPostAd.setSize(data.getStringExtra(SIZE));
				mPostAd.setColour(data.getStringExtra(COLOUR));
				mPostAd.setWeight(data.getIntExtra(WEIGHT, 0));
				mPostAd.setCondition(data.getIntExtra(CONDITION, 0));
				mPostAd.setWarranty(data.getIntExtra(WARRANTY, 0));
				mPostAd.setPicturelink(data.getStringExtra(PICTURELINK));
			}
			break;
		case REQUEST_PRICE:
			break;
		case REQUEST_CONTACT:
			break;
		default:
			break;
		}
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
