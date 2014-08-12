package sg.togoparts.login;

import java.util.HashMap;
import java.util.Map;

import sg.togoparts.HeaderView;
import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.InfoAdapter;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.PostAd;
import sg.togoparts.json.PostAdOnLoadResult;
import sg.togoparts.json.PostAdOnLoadResult.ResultValue;
import sg.togoparts.json.ResultLogin;
import sg.togoparts.login.MyDialogFragment.OnSelectAction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;

public class PostAdActivity extends FragmentActivity implements
		View.OnClickListener, OnSelectAction {

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

	// Request code in activity for result
	public static final int REQUEST_CATEGORY = 0;
	public static final int REQUEST_ITEM = 1;
	public static final int REQUEST_PRICE = 2;
	public static final int REQUEST_CONTACT = 3;
	private static final int REQUEST_CAMERA = 4;
	private static final int REQUEST_GALLERY = 5;
	private static final int REQUEST_AVIARY = 6;

	// type post ad
	private static final int MERCHANT = 2;
	private static final int POSTINGPACK = 1;
	private static final int QUOTA = 0;

	private TextView mTvCategory;
	private TextView mTvItem;
	private TextView mTvPrice;
	private TextView mTvContact;

	private RadioButton mRdoFreeAd;
	private RadioButton mRdoPriorityAd;
	private RadioButton mRdoNewItemAd;
	private TextView mTvNote;
	private HeaderView headerView;
	private ImageButton mBtnBack;
	private ImageButton mBtnSearch;
	private ProgressBar mProgress;
	private ImageView mIvLogo;
	private TextView mTvTitleHeader;

	private ImageView mImv1;
	private ImageView mImv2;
	private ImageView mImv3;
	private ImageView mImv4;
	private ImageView mImv5;
	private ImageView mImv6;
	private GridView mGvInfo;
	private TextView mTvInfo;
	private int mIdSelect;
	private PostAd mPostAd;
	private int mTypePostAd;

	public void launchInstaFiverr(Uri uri) {
		//
		Intent newIntent = new Intent(this, FeatherActivity.class);
		newIntent.setData(uri);
		newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET,
				"6cedc33767b3b37c");
		startActivityForResult(newIntent, REQUEST_AVIARY);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_ad);
		mPostAd = new PostAd();
		createHeader();
		init();
		setListener();
		onLoad();

	}

	private void onLoad() {
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<PostAdOnLoadResult> myReq = new GsonRequest<PostAdOnLoadResult>(
				Method.POST, Const.URL_POST_AD_ONLOAD,
				PostAdOnLoadResult.class, createOnLoadSuccessListener(),
				createMyReqErrorListener()) {
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id",
						Const.getSessionId(PostAdActivity.this));
				return params;
			};
		};
		queue.add(myReq);
	}

	protected void processExpired() {
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_SESSION_REFRESH, ResultLogin.class,
				createExpireSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id",
						Const.getSessionId(PostAdActivity.this));
				params.put("refresh_id", Const.getSHA256EncryptedString(Const
						.getRefreshId(PostAdActivity.this)
						+ ChooseLogin.CLIENT_ID));
				return params;
			};
		};
		queue.add(myReq);
	}

	private Listener<ResultLogin> createExpireSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				if (response.Result.Return.equals("success")) {
					Const.writeSessionId(PostAdActivity.this,
							response.Result.session_id,
							response.Result.refresh_id);
					mProgress.setVisibility(View.GONE);
					onLoad();
				} else {
					showError(response.Result.Message);
				}
			}
		};
	}

	protected void showError(String message) {
		// TODO Auto-generated method stub

	}

	private Listener<PostAdOnLoadResult> createOnLoadSuccessListener() {
		return new Response.Listener<PostAdOnLoadResult>() {

			@Override
			public void onResponse(PostAdOnLoadResult response) {
				mProgress.setVisibility(View.GONE);
				Log.d("haipn", "profile response:" + response.Result.Return);
				if (response.Result.Return.equals("expired")) {
					processExpired();
				} else if (response.Result.Return.equals("success")) {
					onLoadProcess(response.Result);
				} else if (response.Result.Return.equals("error")) {

				}
			}
		};
	}

	protected void onLoadProcess(ResultValue result) {
		InfoAdapter adapter = null;
		if (result.merchant != null) {
			mTvInfo.setText(R.string.merchant_pack_ads);
			adapter = new InfoAdapter(this, result.merchant);
			mTypePostAd = MERCHANT;
		} else if (result.postingpack != null) {
			mTvInfo.setText(R.string.posting_pack_ads);
			adapter = new InfoAdapter(this, result.postingpack);
			mTypePostAd = POSTINGPACK;
		} else {
			mTypePostAd = QUOTA;
			adapter = new InfoAdapter(this, result.quota);
		}

		validationTypePost(result);
		mGvInfo.setAdapter(adapter);
		Const.setGridViewHeightBasedOnChildren(mGvInfo, 3);
	}

	private void validationTypePost(ResultValue ret) {
		if (mTypePostAd == MERCHANT || mTypePostAd == POSTINGPACK) {
			mRdoFreeAd.setEnabled(false);
			mRdoPriorityAd.setEnabled(false);
			mRdoNewItemAd.setEnabled(true);
			mRdoNewItemAd.setChecked(true);
			mTvNote.setText(R.string.note_newitem_ad);
		} else {
			if (ret.TCreds >= ret.min_newitem_cost) {
				mRdoNewItemAd.setEnabled(true);
			} else
				mRdoNewItemAd.setEnabled(false);
			if (ret.TCreds >= ret.min_priority_cost) {
				mRdoPriorityAd.setEnabled(true);
			} else
				mRdoPriorityAd.setEnabled(false);
			mRdoFreeAd.setEnabled(true);
			mRdoFreeAd.setChecked(true);
			mTvNote.setText("");
		}
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
			}
		};
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		mBtnSearch = (ImageButton) findViewById(R.id.btnSearch);
		findViewById(R.id.logo).setVisibility(View.GONE);
		mProgress = (ProgressBar) findViewById(R.id.progress);
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
		mTvNote = (TextView) findViewById(R.id.tvNote);
		mTvCategory = (TextView) findViewById(R.id.tvCategory);
		mTvItem = (TextView) findViewById(R.id.tvItem);
		mTvPrice = (TextView) findViewById(R.id.tvPrice);
		mTvContact = (TextView) findViewById(R.id.tvContact);
		mImv1 = (ImageView) findViewById(R.id.imv1);
		mImv1.setOnClickListener(this);
		mImv2 = (ImageView) findViewById(R.id.imv2);
		mImv2.setOnClickListener(this);
		mImv3 = (ImageView) findViewById(R.id.imv3);
		mImv3.setOnClickListener(this);
		mImv4 = (ImageView) findViewById(R.id.imv4);
		mImv4.setOnClickListener(this);
		mImv5 = (ImageView) findViewById(R.id.imv5);
		mImv5.setOnClickListener(this);
		mImv6 = (ImageView) findViewById(R.id.imv6);
		mImv6.setOnClickListener(this);

		mGvInfo = (GridView) findViewById(R.id.gvInfo);
		mTvInfo = (TextView) findViewById(R.id.tvInfo);

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
				i.putExtra(PRICETYPE, mPostAd.getPricetype());
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

		mRdoFreeAd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					mTvNote.setText("");
					mImv2.setEnabled(false);
					mImv2.setImageResource(R.drawable.unselected_pic);
					mImv3.setEnabled(false);
					mImv3.setImageResource(R.drawable.unselected_pic);
					mImv4.setEnabled(false);
					mImv4.setImageResource(R.drawable.unselected_pic);
					mImv5.setEnabled(false);
					mImv5.setImageResource(R.drawable.unselected_pic);
					mImv6.setEnabled(false);
					mImv6.setImageResource(R.drawable.unselected_pic);
				} else {
					mImv2.setEnabled(true);
					mImv2.setImageResource(R.drawable.upload_pic);
					mImv3.setEnabled(true);
					mImv3.setImageResource(R.drawable.upload_pic);
					mImv4.setEnabled(true);
					mImv4.setImageResource(R.drawable.upload_pic);
					mImv5.setEnabled(true);
					mImv5.setImageResource(R.drawable.upload_pic);
					mImv6.setEnabled(true);
					mImv6.setImageResource(R.drawable.upload_pic);
				}
			}
		});

		mRdoNewItemAd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mTvNote.setText(R.string.note_newitem_ad);
				}
			}
		});
		mRdoPriorityAd
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mTvNote.setText(R.string.note_priority_ad);
						}
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

				Log.d("haipn", "Section:" + mPostAd.getSection()
						+ ", category:" + mPostAd.getCat() + ", sub cat:"
						+ mPostAd.getSub_cat());
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
			if (resultCode == RESULT_OK) {
				mPostAd.setPrice(data.getIntExtra(PRICE, 0));
				mPostAd.setOriginal_price(data.getIntExtra(ORIGINAL_PRICE, 0));
				mPostAd.setClearance(data.getBooleanExtra(CLEARANCE, false));
				mPostAd.setPricetype(data.getIntExtra(PRICETYPE, 3));
			}
			break;
		case REQUEST_CONTACT:
			if (resultCode == RESULT_OK) {
				mPostAd.setCity(data.getStringExtra(CITY));
				mPostAd.setRegion(data.getStringExtra(REGION));
				mPostAd.setCountry(data.getStringExtra(COUNTRY));
				mPostAd.setPostalcode(data.getStringExtra(POSTALCODE));
				mPostAd.setAddress(data.getStringExtra(ADDRESS));
				mPostAd.setLatitude(data.getDoubleExtra(LAT, 0));
				mPostAd.setLongitude(data.getDoubleExtra(LONGITUDE, 0));
				mPostAd.setContactno(data.getStringExtra(CONTACTNO));
				mPostAd.setContactperson(data.getStringExtra(CONTACTPERSON));
				mPostAd.setTime_to_contact(data.getIntExtra(TIME_TO_CONTACT, 0));
			}
			break;
		case REQUEST_CAMERA:
			if (resultCode == RESULT_OK) {
				Uri imageUri = data.getData();
				launchInstaFiverr(imageUri);
			}
			break;
		case REQUEST_GALLERY:
			if (resultCode == RESULT_OK) {
				Uri imageUri = data.getData();
				launchInstaFiverr(imageUri);
			}
			break;
		case REQUEST_AVIARY:
			if (resultCode == RESULT_OK) {
				Uri mImageUri = data.getData();
				switch (mIdSelect) {
				case 1:
					mImv1.setImageURI(mImageUri);
					break;
				case 2:
					mImv2.setImageURI(mImageUri);
					break;
				case 3:
					mImv3.setImageURI(mImageUri);
					break;
				case 4:
					mImv4.setImageURI(mImageUri);
					break;
				case 5:
					mImv5.setImageURI(mImageUri);
					break;
				case 6:
					mImv6.setImageURI(mImageUri);
					break;
				default:
					break;
				}
			}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imv1:
			mIdSelect = 1;
			break;
		case R.id.imv2:
			mIdSelect = 2;
			break;
		case R.id.imv3:
			mIdSelect = 3;
			break;
		case R.id.imv4:
			mIdSelect = 4;
			break;
		case R.id.imv5:
			mIdSelect = 5;
			break;
		case R.id.imv6:
			mIdSelect = 6;
			break;
		}
		DialogFragment newFragment = new MyDialogFragment(this);
		newFragment.show(getSupportFragmentManager(), "dialog");
	}

	@Override
	public void onCaptureSelect() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, REQUEST_CAMERA);
	}

	@Override
	public void onPickSelect() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, REQUEST_GALLERY);
	}

}
