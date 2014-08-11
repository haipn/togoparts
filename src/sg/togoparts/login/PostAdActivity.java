package sg.togoparts.login;

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
	private PostAd mPosAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_ad);
		mPosAd = new PostAd();
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
				i.putExtra(BRAND, mPosAd.getBrand());
				i.putExtra(ITEM_YEAR, mPosAd.getItem_year());
				i.putExtra(ITEM, mPosAd.getItem());
				i.putExtra(TITLE, mPosAd.getTitle());
				i.putExtra(TRANSTYPE, mPosAd.getTranstype());
				i.putExtra(DESCRIPTION, mPosAd.getDescription());
				i.putExtra(D_MTB, mPosAd.isD_mtb());
				i.putExtra(D_BMX, mPosAd.isD_bmx());
				i.putExtra(D_COMMUTE, mPosAd.isD_commute());
				i.putExtra(D_FOLDING, mPosAd.isD_folding());
				i.putExtra(D_OTHERS, mPosAd.isD_others());
				i.putExtra(D_ROAD, mPosAd.isD_road());
				i.putExtra(SIZE, mPosAd.getSize());
				i.putExtra(COLOUR, mPosAd.getColour());
				i.putExtra(WEIGHT, mPosAd.getWeight());
				i.putExtra(CONDITION, mPosAd.getCondition());
				i.putExtra(WARRANTY, mPosAd.getWarranty());
				i.putExtra(PICTURELINK, PICTURELINK);
				
				startActivityForResult(i, REQUEST_ITEM);
			}
		});
		mTvPrice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PostAdActivity.this, Price.class);
				i.putExtra(PRICE, mPosAd.getPrice());
				i.putExtra(PRICETYPE,mPosAd.getPricetype());
				i.putExtra(ORIGINAL_PRICE, mPosAd.getOriginal_price());
				i.putExtra(CLEARANCE, mPosAd.isClearance());
				startActivityForResult(i, REQUEST_PRICE);
				
			}
		});
		
		mTvContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PostAdActivity.this, ContactInfo.class);
				i.putExtra(CITY, mPosAd.getCity());
				i.putExtra(REGION, mPosAd.getRegion());
				i.putExtra(COUNTRY, mPosAd.getCountry());
				i.putExtra(POSTALCODE, mPosAd.getPostalcode());
				i.putExtra(ADDRESS, mPosAd.getAddress());
				i.putExtra(LAT, mPosAd.getLatitude());
				i.putExtra(LONGITUDE, mPosAd.getLongitude());
				i.putExtra(CONTACTNO, mPosAd.getContactno());
				i.putExtra(CONTACTPERSON, mPosAd.getContactperson());
				i.putExtra(TIME_TO_CONTACT, mPosAd.getTime_to_contact());
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
				
				mPosAd.setSection(data.getIntExtra(SECTION, 0));
				mPosAd.setCat(data.getIntExtra(CAT, 0));
				mPosAd.setSub_cat(data.getIntExtra(SUB_CAT, 0));
				
				Log.d("haipn", "Section:" + mPosAd.getSection() +  ", category:" + mPosAd.getCat() + ", sub cat:" + mPosAd.getSub_cat());
			}
			break;
		case REQUEST_ITEM: 
			if (resultCode == RESULT_OK) {
				mPosAd.setBrand(data.getStringExtra(BRAND));
				mPosAd.setItem_year(data.getStringExtra(ITEM_YEAR));
				mPosAd.setItem(data.getStringExtra(ITEM));
				mPosAd.setTitle(data.getStringExtra(TITLE));
				mPosAd.setTranstype(data.getIntExtra(TRANSTYPE, 1));
				mPosAd.setDescription(data.getStringExtra(DESCRIPTION));
				mPosAd.setD_bmx(data.getBooleanExtra(D_BMX, false));
				mPosAd.setD_commute(data.getBooleanExtra(D_COMMUTE, false));
				mPosAd.setD_folding(data.getBooleanExtra(D_FOLDING, false));
				mPosAd.setD_mtb(data.getBooleanExtra(D_MTB, false));
				mPosAd.setD_others(data.getBooleanExtra(D_OTHERS, false));
				mPosAd.setD_road(data.getBooleanExtra(D_ROAD, false));
				mPosAd.setSize(data.getStringExtra(SIZE));
				mPosAd.setColour(data.getStringExtra(COLOUR));
				mPosAd.setWeight(data.getIntExtra(WEIGHT, 0));
				mPosAd.setCondition(data.getIntExtra(CONDITION, 0));
				mPosAd.setWarranty(data.getIntExtra(WARRANTY, 0));
				mPosAd.setPicturelink(data.getStringExtra(PICTURELINK));
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
