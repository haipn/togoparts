package sg.togoparts.login;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import sg.togoparts.DetailActivity;
import sg.togoparts.HeaderView;
import sg.togoparts.MoreActivity;
import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.InfoAdapter;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.PostAd;
import sg.togoparts.json.PostAdOnLoadResult;
import sg.togoparts.json.PostAdOnLoadResult.AdDetails;
import sg.togoparts.json.PostAdOnLoadResult.ResultValue;
import sg.togoparts.json.ResultLogin;
import sg.togoparts.login.MyDialogFragment.OnSelectAction;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;

public class PostAdActivity extends FragmentActivity implements
		View.OnClickListener, OnSelectAction {

	public static final String SESSION_ID = "session_id";
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
	protected static final String EDIT_AD = "edit ad";
	protected static final String POSTING_PACK = "posting pack";
	protected static final String MERCHANT_PACK = "merchant pack";
	private static final String FILE_PATH = Environment
			.getExternalStorageDirectory() + "/temp.jpg";

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
	// private GridView mGvInfo;
	// private TextView mTvInfo;
	private Button mBtnSubmit;
	private int mIdSelect;
	private PostAd mPostAd;
	private int mTypePostAd;
	private String mAdId;
	private boolean isEdit;
	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ResultValue mResultValue;
	private ProgressDialog mProgressDialog;
	private CheckBox mCbEmail;

	public void launchInstaFiverr(Uri uri) {
		//
		Intent newIntent = new Intent(this, FeatherActivity.class);
		newIntent.setData(uri);
		newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET,
				"72ef1fcfae120bfa");
		startActivityForResult(newIntent, REQUEST_AVIARY);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_ad);
		mAdId = getIntent().getStringExtra(Const.ADS_ID);
		mPostAd = new PostAd();
		if (mAdId == null || mAdId.length() == 0) {
			isEdit = false;
		} else {
			isEdit = true;
		}
		createHeader();
		init();
		setListener();
		onLoad();
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	private void onLoad() {
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<PostAdOnLoadResult> myReq = new GsonRequest<PostAdOnLoadResult>(
				Method.POST, Const.URL_POST_AD_ONLOAD,
				PostAdOnLoadResult.class, createOnLoadSuccessListener(),
				createMyReqErrorListener()) {
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id",
						Const.getSessionId(PostAdActivity.this));
				if (isEdit) {
					params.put("aid", mAdId);
				}
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

			protected Map<String, String> getParams() throws AuthFailureError {
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.error)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
								dialog.dismiss();
							}
						});
		builder.show();
	}

	protected void showBanned(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.error)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								logout();
								dialog.dismiss();
							}
						});
		builder.show();
	}

	protected void logout() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.show();
		SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance(this);
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_LOGOUT, ResultLogin.class,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				String key = Const.getRefreshId(PostAdActivity.this)
						+ ChooseLogin.CLIENT_ID;
				key = Const.getSHA256EncryptedString(key);
				params.put("session_id",
						Const.getSessionId(PostAdActivity.this));
				params.put("refresh_id", key);
				return params;
			};
		};
		queue.add(myReq);

		// logout listener

		mSimpleFacebook.logout(onLogoutListener);
	}

	private Response.Listener<ResultLogin> createMyReqSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				Log.d("haipn", "response success:" + response.Result.Return);
				Toast.makeText(PostAdActivity.this, "Logout successful",
						Toast.LENGTH_LONG).show();
				Const.deleteSessionId(PostAdActivity.this);
				Const.writeAccessTokenFb(PostAdActivity.this, "");
				mProgressDialog.dismiss();
			}
		};
	}

	OnLogoutListener onLogoutListener = new OnLogoutListener() {
		@Override
		public void onLogout() {
			Log.i("haipn", "Facebook You are logged out");
		}

		@Override
		public void onThinking() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub

		}
	};

	private Listener<PostAdOnLoadResult> createOnLoadSuccessListener() {
		return new Response.Listener<PostAdOnLoadResult>() {

			@Override
			public void onResponse(PostAdOnLoadResult response) {
				mProgress.setVisibility(View.GONE);
				Log.d("haipn", "profile response:" + response.Result.Return);
				if (response.Result.Return.equals("expired")) {
					processExpired();
				} else if (response.Result.Return.equals("success")) {
					mResultValue = response.Result;
					onLoadProcess(response.Result);
				} else if (response.Result.Return.equals("error")) {
					showError(response.Result.Message);
				} else if (response.Result.Return.equals("banned")) {
					showBanned(response.Result.Message);
				}
			}
		};
	}

	protected void onLoadProcess(ResultValue result) {
		InfoAdapter adapter = null;
		if (result.merchant != null) {
			// mTvInfo.setText(R.string.merchant_pack_ads);
			// adapter = new InfoAdapter(this, result.merchant);
			mTypePostAd = MERCHANT;
		} else if (result.postingpack != null) {
			// mTvInfo.setText(R.string.posting_pack_ads);
			// adapter = new InfoAdapter(this, result.postingpack);
			mTypePostAd = POSTINGPACK;
		} else {
			mTypePostAd = QUOTA;
			// adapter = new InfoAdapter(this, result.quota);
		}
		if (result.Shopid > 0) {
			mTvContact.setVisibility(View.GONE);
		} else {
			mTvContact.setVisibility(View.VISIBLE);
		}
		validationTypePost(result);
		// mGvInfo.setAdapter(adapter);

		// Const.setGridViewHeightBasedOnChildren(mGvInfo, 3, 0);

		if (isEdit) {
			findViewById(R.id.llCheckEmail).setVisibility(View.GONE);
			fillAdData(result.ad_details);
		} else {
			findViewById(R.id.llCheckEmail).setVisibility(View.VISIBLE);
		}
	}

	private void fillAdData(AdDetails ad) {
		if (ad.adtype == 0) {
			mRdoFreeAd.setChecked(true);
		} else if (ad.adtype == 1)
			mRdoPriorityAd.setChecked(true);
		else
			mRdoNewItemAd.setChecked(true);
		mRdoFreeAd.setEnabled(false);
		mRdoNewItemAd.setEnabled(false);
		mRdoPriorityAd.setEnabled(false);
		if (ad.picture != null && ad.picture.size() > 0) {
			for (int i = 0; i < ad.picture.size(); i++) {
				if (i == 0) {
					imageLoader.displayImage(ad.picture.get(i).picture, mImv1,
							options);
					mImv1.setTag(ad.picture.get(i).pid);
				} else if (i == 1) {
					imageLoader.displayImage(ad.picture.get(i).picture, mImv2,
							options);
					mImv2.setTag(ad.picture.get(i).pid);
				} else if (i == 2) {
					imageLoader.displayImage(ad.picture.get(i).picture, mImv3,
							options);
					mImv3.setTag(ad.picture.get(i).pid);
				} else if (i == 3) {
					imageLoader.displayImage(ad.picture.get(i).picture, mImv4,
							options);
					mImv4.setTag(ad.picture.get(i).pid);
				} else if (i == 4) {
					imageLoader.displayImage(ad.picture.get(i).picture, mImv5,
							options);
					mImv5.setTag(ad.picture.get(i).pid);
				} else if (i == 5) {
					imageLoader.displayImage(ad.picture.get(i).picture, mImv6,
							options);
					mImv6.setTag(ad.picture.get(i).pid);
				}
			}
		}

		mTvCategory.setEnabled(false);
		mPostAd.setAddress(ad.address);
		mPostAd.setAdtype(ad.adtype);
		mPostAd.setAid(Integer.valueOf(mAdId));
		mPostAd.setBrand(ad.brand);
		mPostAd.setCity(ad.city);
		mPostAd.setColour(ad.colour);
		mPostAd.setCondition(ad.condition);
		mPostAd.setContactno(ad.contactno);
		mPostAd.setContactperson(ad.contactperson);
		mPostAd.setCountry(ad.country);
		mPostAd.setD_bmx(ad.d_bmx == 1 ? true : false);
		mPostAd.setD_commute(ad.d_commute == 1 ? true : false);
		mPostAd.setD_folding(ad.d_folding == 1 ? true : false);
		mPostAd.setD_mtb(ad.d_mtb == 1 ? true : false);
		mPostAd.setD_others(ad.d_others == 1 ? true : false);
		mPostAd.setD_road(ad.d_road == 1 ? true : false);
		mPostAd.setDescription(ad.description);
		mPostAd.setItem(ad.item);
		mPostAd.setItem_year(ad.item_year + "");
		mPostAd.setLatitude(Double.valueOf(ad.lat));
		mPostAd.setLongitude(Double.valueOf(ad.longitude));
		mPostAd.setOriginal_price(ad.original_price);
		mPostAd.setPicturelink(ad.picturelink);
		mPostAd.setPostalcode(ad.postalcode);
		mPostAd.setWarranty(ad.warranty);
		mPostAd.setWeight(ad.weight);
		mPostAd.setTranstype(Integer.valueOf(ad.transtype));
		mPostAd.setTitle(ad.title);
		mPostAd.setTime_to_contact(ad.time_to_contact);
		mPostAd.setSize(ad.size);
		mPostAd.setRegion(ad.region);
		mPostAd.setPrice(Integer.valueOf(ad.price));
		mPostAd.setPricetype(ad.pricetype);
		mPostAd.setSection(Integer.valueOf(ad.section));
		mPostAd.setCat(Integer.valueOf(ad.cat));
		mPostAd.setSub_cat(Integer.valueOf(ad.sub_cat));
		mPostAd.setmExistPics(ad.picture);
		mTvItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.item_icon,
				0, R.drawable.tick, 0);
		mTvPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.price_icon,
				0, R.drawable.tick, 0);
		mTvContact.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.contactinfo_icon, 0, R.drawable.tick, 0);
		mTvCategory.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.category_icon, 0, R.drawable.tick, 0);
	}

	private void validationTypePost(ResultValue ret) {
		if (mTypePostAd == MERCHANT || mTypePostAd == POSTINGPACK) {
			mRdoFreeAd.setVisibility(View.GONE);
			mRdoPriorityAd.setVisibility(View.GONE);
			mRdoNewItemAd.setVisibility(View.VISIBLE);
			mRdoNewItemAd.setChecked(true);
			mTvNote.setText(R.string.note_newitem_ad);
		} else {
			mRdoFreeAd.setVisibility(View.VISIBLE);
			mRdoPriorityAd.setVisibility(View.VISIBLE);
			mRdoNewItemAd.setVisibility(View.VISIBLE);
			// if (ret.TCreds >= ret.min_newitem_cost) {
			// mRdoNewItemAd.setEnabled(true);
			// } else
			// mRdoNewItemAd.setEnabled(false);
			// if (ret.TCreds >= ret.min_priority_cost) {
			// mRdoPriorityAd.setEnabled(true);
			// } else
			// mRdoPriorityAd.setEnabled(false);
			// mRdoFreeAd.setEnabled(true);
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
		mBtnBack.setBackgroundResource(R.drawable.btn_cancel);
		mBtnSearch = (ImageButton) findViewById(R.id.btnSearch);
		mBtnSearch.setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.GONE);
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_postad);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showConfirmDialog();
			}
		});
	}

	protected void showConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.msg_confirm_cancel)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Confirm Cancellation")
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								onBackPressed();
							}
						});
		builder.create().show();
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

		// mGvInfo = (GridView) findViewById(R.id.gvInfo);
		// mTvInfo = (TextView) findViewById(R.id.tvInfo);
		mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
		mCbEmail = (CheckBox) findViewById(R.id.cbEmail);
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
				i.putExtra(EDIT_AD, isEdit);
				i.putExtra(POSTING_PACK, mTypePostAd == MERCHANT
						|| mTypePostAd == POSTINGPACK);
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
				i.putExtra(EDIT_AD, isEdit);
				i.putExtra(POSTING_PACK, mTypePostAd == POSTINGPACK);
				i.putExtra(MERCHANT_PACK, mTypePostAd == MERCHANT);
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
				i.putExtra(EDIT_AD, isEdit);
				i.putExtra(POSTING_PACK, mTypePostAd == MERCHANT
						|| mTypePostAd == POSTINGPACK);
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
					mPostAd.setAdtype(0);
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
					if (mResultValue.TCreds >= mResultValue.min_newitem_cost) {
						mTvNote.setText(R.string.note_newitem_ad);
						mPostAd.setAdtype(2);
					} else {
						AlertTcredDialog dialog = new AlertTcredDialog(
								getString(R.string.msg_purchase, "New Item"));
						dialog.show(getSupportFragmentManager(),
								"new item confirm");
						buttonView.setChecked(false);
						mRdoFreeAd.setChecked(true);
					}

				}

			}
		});
		mRdoPriorityAd
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (mResultValue.TCreds >= mResultValue.min_priority_cost) {
								mTvNote.setText(R.string.note_priority_ad);
								mPostAd.setAdtype(1);
							} else {
								AlertTcredDialog dialog = new AlertTcredDialog(
										getString(R.string.msg_purchase,
												"Priority"));
								dialog.show(getSupportFragmentManager(),
										"priority confirm");
								buttonView.setChecked(false);
								mRdoFreeAd.setChecked(true);
							}
						}
					}
				});
		mBtnSubmit.setOnClickListener(new OnPostAdClick());
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
				mTvCategory.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.category_icon, 0, R.drawable.tick, 0);
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
				mTvItem.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.item_icon, 0, R.drawable.tick, 0);
			}
			break;
		case REQUEST_PRICE:
			if (resultCode == RESULT_OK) {
				mPostAd.setPrice(data.getIntExtra(PRICE, 0));
				mPostAd.setOriginal_price(data.getIntExtra(ORIGINAL_PRICE, 0));
				mPostAd.setClearance(data.getBooleanExtra(CLEARANCE, false));
				mPostAd.setPricetype(data.getIntExtra(PRICETYPE, 3));
				mTvPrice.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.price_icon, 0, R.drawable.tick, 0);
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
				mPostAd.setTime_to_contact(data.getStringExtra(TIME_TO_CONTACT));
				mTvContact.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.contactinfo_icon, 0, R.drawable.tick, 0);
			}
			break;
		case REQUEST_CAMERA:
			if (resultCode == RESULT_OK) {
				Uri imageUri = Uri.fromFile(new File(FILE_PATH));
				Options op = new Options();
				op.inJustDecodeBounds = true;
				Bitmap bm = BitmapFactory.decodeFile(FILE_PATH, op);
				Log.d("haipn", "bitmap camera widgh x height:" + op.outWidth
						+ " x " + op.outHeight);
				launchInstaFiverr(imageUri);
			}
			break;
		case REQUEST_GALLERY:
			if (resultCode == RESULT_OK) {
				Uri mImageUri = data.getData();
				Options op = new Options();
				op.inJustDecodeBounds = true;
				Bitmap bm = BitmapFactory.decodeFile(
						getRealPathFromURI(mImageUri), op);
				Log.d("haipn", "bitmap galeery widgh x height:" + op.outWidth
						+ " x " + op.outHeight);
				launchInstaFiverr(mImageUri);

			}
			break;
		case REQUEST_AVIARY:
			if (resultCode == RESULT_OK) {
				Uri mImageUri = data.getData();
				String path = getRealPathFromURI(mImageUri);
				Log.d("haipn", "path image:" + path);
				Options op = new Options();
				op.inJustDecodeBounds = true;
				Bitmap bm = BitmapFactory.decodeFile(path, op);
				Log.d("haipn", "bitmap aviary widgh x height:" + op.outWidth
						+ " x " + op.outHeight);
				switch (mIdSelect) {
				case 1:
					mImv1.setImageURI(mImageUri);
					mPostAd.setAdpic1(path);
					break;
				case 2:
					mImv2.setImageURI(mImageUri);
					mPostAd.setAdpic2(path);
					break;
				case 3:
					mImv3.setImageURI(mImageUri);
					mPostAd.setAdpic3(path);
					break;
				case 4:
					mImv4.setImageURI(mImageUri);
					mPostAd.setAdpic4(path);
					break;
				case 5:
					mImv5.setImageURI(mImageUri);
					mPostAd.setAdpic5(path);
					break;
				case 6:
					mImv6.setImageURI(mImageUri);
					mPostAd.setAdpic6(path);
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

	public String getRealPathFromURI(Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = getContentResolver().query(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showConfirmDialog();
			return false;
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
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(cameraIntent, REQUEST_CAMERA);
	}

	@Override
	public void onPickSelect() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, REQUEST_GALLERY);
	}

	public String getAdtype() {
		if (mRdoFreeAd.isChecked())
			return "0";
		if (mRdoNewItemAd.isChecked())
			return "2";

		if (mRdoPriorityAd.isChecked())
			return "1";
		return "0";

	}

	private Listener<PostAdResult> createPostAdSuccessListener() {
		return new Response.Listener<PostAdResult>() {

			@Override
			public void onResponse(PostAdResult response) {
				mProgress.setVisibility(View.INVISIBLE);
			}
		};
	}

	private String buildMultipartEntity(PostAd post)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(Const.URL_POST_AD);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		File file1 = new File(post.getAdpic1());
		String del_pics = "";
		if (file1.exists()) {
			builder.addPart(PostAdActivity.ADPIC1, new FileBody(file1));
			Log.d("haipn", "postad image 1:" + post.getAdpic1());
			if (isEdit && mImv1.getTag() != null) {
				del_pics = del_pics + (String) mImv1.getTag() + ",";
			}
		}
		File file2 = new File(post.getAdpic2());

		if (file2.exists()) {
			builder.addPart(PostAdActivity.ADPIC2, new FileBody(file2));
			Log.d("haipn", "postad image 2:" + post.getAdpic2());
			if (isEdit && mImv2.getTag() != null) {
				del_pics = del_pics + (String) mImv2.getTag() + ",";
			}
		}
		File file3 = new File(post.getAdpic3());

		if (file3.exists()) {
			builder.addPart(PostAdActivity.ADPIC3, new FileBody(file3));
			Log.d("haipn", "postad image 3:" + post.getAdpic3());
			if (isEdit && mImv3.getTag() != null) {
				del_pics = del_pics + (String) mImv3.getTag() + ",";
			}
		}
		File file4 = new File(post.getAdpic4());

		if (file4.exists()) {
			builder.addPart(PostAdActivity.ADPIC4, new FileBody(file4));
			Log.d("haipn", "postad image 4:" + post.getAdpic4());
			if (isEdit && mImv4.getTag() != null) {
				del_pics = del_pics + (String) mImv4.getTag() + ",";
			}
		}
		File file5 = new File(post.getAdpic5());

		if (file5.exists()) {
			builder.addPart(PostAdActivity.ADPIC5, new FileBody(file5));
			Log.d("haipn", "postad image 5:" + post.getAdpic5());
			if (isEdit && mImv5.getTag() != null) {
				del_pics = del_pics + (String) mImv5.getTag() + ",";
			}
		}
		File file6 = new File(post.getAdpic6());

		if (file6.exists()) {
			builder.addPart(PostAdActivity.ADPIC6, new FileBody(file6));
			Log.d("haipn", "postad image 6:" + post.getAdpic6());
			if (isEdit && mImv6.getTag() != null) {
				del_pics = del_pics + (String) mImv6.getTag() + ",";
			}
		}

		if (isEdit && del_pics.length() > 0) {
			builder.addTextBody("del_pids",
					del_pics.substring(0, del_pics.length() - 1));
			Log.d("haipn", "del pids:" + del_pics);
			Log.d("haipn",
					"del pids after: "
							+ del_pics.substring(0, del_pics.length() - 1));
		}
		builder.addTextBody(PostAdActivity.SESSION_ID, post.getSession_id());
		builder.addTextBody(PostAdActivity.ADDRESS, post.getAddress());
		builder.addTextBody(PostAdActivity.ADTYPE, post.getAdtype() + "");
		builder.addTextBody(PostAdActivity.AID, post.getAid() + "");
		builder.addTextBody(PostAdActivity.BRAND, post.getBrand());
		builder.addTextBody(PostAdActivity.CAT, post.getCat() + "");
		builder.addTextBody(PostAdActivity.CITY, post.getCity());
		builder.addTextBody(PostAdActivity.CLEARANCE, post.isClearance() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.COLOUR, post.getColour());
		builder.addTextBody(
				PostAdActivity.CONDITION,
				post.getCondition() == 0 ? "" : String.valueOf(post
						.getCondition()));
		builder.addTextBody(PostAdActivity.CONTACTNO, post.getContactno());
		builder.addTextBody(PostAdActivity.CONTACTPERSON,
				post.getContactperson());
		builder.addTextBody(PostAdActivity.COUNTRY, post.getCountry());
		builder.addTextBody(PostAdActivity.D_BMX, post.isD_bmx() ? "1" : "0");
		builder.addTextBody(PostAdActivity.D_COMMUTE, post.isD_commute() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.D_FOLDING, post.isD_folding() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.D_MTB, post.isD_mtb() ? "1" : "0");
		builder.addTextBody(PostAdActivity.D_OTHERS, post.isD_others() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.D_ROAD, post.isD_road() ? "1" : "0");
		builder.addTextBody(PostAdActivity.DESCRIPTION, post.getDescription());
		builder.addTextBody(PostAdActivity.ITEM, post.getItem());
		builder.addTextBody(PostAdActivity.ITEM_YEAR, post.getItem_year());
		builder.addTextBody(PostAdActivity.LAT, post.getLatitude() + "");
		builder.addTextBody(PostAdActivity.LONGITUDE, post.getLongitude() + "");
		builder.addTextBody(PostAdActivity.ORIGINAL_PRICE,
				post.getOriginal_price() + "");
		builder.addTextBody(PostAdActivity.PICTURELINK, post.getPicturelink());
		builder.addTextBody(PostAdActivity.POSTALCODE, post.getPostalcode());
		builder.addTextBody(PostAdActivity.PRICE, post.getPrice() + "");
		builder.addTextBody(PostAdActivity.PRICETYPE, post.getPricetype() + "");
		builder.addTextBody(PostAdActivity.REGION, post.getRegion());
		builder.addTextBody(PostAdActivity.SECTION, post.getSection() + "");
		builder.addTextBody(PostAdActivity.SIZE, post.getSize());
		builder.addTextBody(PostAdActivity.SUB_CAT, post.getSub_cat() + "");
		builder.addTextBody(PostAdActivity.TIME_TO_CONTACT,
				post.getTime_to_contact());
		builder.addTextBody(PostAdActivity.TITLE, post.getTitle());
		builder.addTextBody(PostAdActivity.TRANSTYPE, post.getTranstype() + "");
		builder.addTextBody(PostAdActivity.WARRANTY, post.getWarranty() + "");
		builder.addTextBody(PostAdActivity.WEIGHT, post.getWeight() + "");
		if (!isEdit)
			builder.addTextBody("email_me", mCbEmail.isChecked() ? "1" : "0");
		// builder.addTextBody(KEY_ROUTE_ID, mRouteId);
		HttpEntity entity = builder.build();

		httpPost.setEntity(entity);

		HttpResponse response = client.execute(httpPost);

		HttpEntity httpEntity = response.getEntity();

		String result = EntityUtils.toString(httpEntity);
		return result;
	}

	public class OnPostAdClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			mPostAd.setSession_id(Const.getSessionId(PostAdActivity.this));
			new FileUploadTask().execute(mPostAd);
			// mProgress.setVisibility(View.VISIBLE);
			// RequestQueue queue = MyVolley.getRequestQueue();
			// MultipartRequest<PostAdResult> myReq = new
			// MultipartRequest<PostAdResult>(
			// Method.POST, Const.URL_POST_AD, PostAdResult.class,
			// createPostAdSuccessListener(), createMyReqErrorListener(),
			// mPostAd);
			// queue.add(myReq);
		}

	}

	private String buildMultipartEntityTest(PostAd post)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(Const.URL_POST_AD_TEST);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		File file1 = new File(post.getAdpic1());

		if (file1.exists()) {
			builder.addPart("image", new FileBody(file1));
			Log.d("haipn", "test 1:" + post.getAdpic1());
		}
		HttpEntity entity = builder.build();

		httpPost.setEntity(entity);

		HttpResponse response = client.execute(httpPost);

		HttpEntity httpEntity = response.getEntity();

		String result = EntityUtils.toString(httpEntity);
		return result;
	}

	private class FileUploadTask extends AsyncTask<PostAd, Integer, String> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PostAdActivity.this);
			dialog.setMessage("Uploading...");
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@Override
		protected String doInBackground(PostAd... arg0) {
			try {
				return buildMultipartEntity(arg0[0]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error IOexception";
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			ResultLogin res = gson.fromJson(result, ResultLogin.class);
			if (res.Result.Return.equals("success")) {
				if (isEdit) {
					setResult(RESULT_OK);
					finish();
				} else {
					resetAll();
					Intent i = new Intent(PostAdActivity.this,
							DetailActivity.class);
					i.putExtra(Const.ADS_ID, res.Result.adid);
					startActivity(i);
					finish();
				}
			} else {
				showError(res.Result.Message);
			}
		}

	}

	public void resetAll() {
		mPostAd = new PostAd();
		onLoad();
		mRdoFreeAd.setChecked(true);
		mImv1.setImageResource(R.drawable.upload_pic);
		mTvContact.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.contactinfo_icon, 0, 0, 0);
		mTvCategory.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.category_icon, 0, 0, 0);
		mTvPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.price_icon,
				0, 0, 0);
		mTvItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.item_icon,
				0, 0, 0);
	}

	public class AlertTcredDialog extends DialogFragment {
		public AlertTcredDialog(String mMessage) {
			super();
			this.mMessage = mMessage;
		}

		public AlertTcredDialog() {
			super();
		}

		public void setMessage(String msg) {
			mMessage = msg;
		}

		String mMessage;

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(mMessage)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mRdoFreeAd.setChecked(true);
								}
							});
			return builder.create();
		}
	}

}
