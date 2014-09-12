package sg.togoparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorInternetDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ListCategories;
import sg.togoparts.json.ListCategories.Cat;
import sg.togoparts.json.ListCategories.Shopname;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class SearchActivity extends FragmentActivity {
	private static final String SCREEN_LABEL = "Marketplace Search Form";
	private EditText mEdtKeyword;
	private Button mBtnSearch;
	private HeaderView headerView;
	HashMap<String, String> listCatId;
	private Spinner mSpnCatId;
	private ImageButton mBtnRight;
	private ImageButton mBtnLeft;
	private TextView mTvTitle;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.search_fragment);
		mEdtKeyword = (EditText) findViewById(R.id.edtKeyword);
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

		mSpnCatId = (Spinner) findViewById(R.id.spnCatId);
		listCatId = new LinkedHashMap<String, String>();
		mEdtKeyword
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							performSearch();
							return true;
						}
						return false;
					}
				});

		mBtnSearch = (Button) findViewById(R.id.btnSearchResult);
		mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				performSearch();
			}
		});
		createHeader();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ListCategories> myReq = new GsonRequest<ListCategories>(
				Method.GET, Const.URL_SEARCH_CATEGORY, ListCategories.class,
				createMyReqSuccessListener(), createMyReqErrorListener());

		queue.add(myReq);
	}

	private Response.Listener<ListCategories> createMyReqSuccessListener() {
		return new Response.Listener<ListCategories>() {
			@Override
			public void onResponse(ListCategories response) {
				setListValues();
				ArrayList<Cat> list = response.mp_categories;
				// listCatId.put("All", "");
				for (int i = 0; i < list.size(); i++) {
					listCatId.put(list.get(i).title, list.get(i).value);
				}

				initSpinner();
			}
		};
	}

	protected void initSpinner() {
//		Bundle b = getIntent().getExtras();
//		int i = 0, d = 0;
//		String value = b.getString(FilterActivity.MARKETPLACE_CATEGORY);
		ArrayAdapter<String> adtCatId = new ArrayAdapter<String>(this,
				R.layout.spinner_text);
		Iterator itCatId = listCatId.entrySet().iterator();
		while (itCatId.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCatId.next();
			adtCatId.add((String) pairs.getKey());
		}
		adtCatId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnCatId.setAdapter(adtCatId);
//		mSpnCatId.setSelection(d);
	}

	protected void setListValues() {
		listCatId = new LinkedHashMap<String, String>();
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorInternetDialog newFragment = new ErrorInternetDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}

	private void createHeader() {
		// headerView = (HeaderView) getActivity();
		// headerView.setLeftButton(View.VISIBLE);
		// headerView.setLogoVisible(View.GONE);
		// headerView.setTitleVisible(View.VISIBLE,
		// getString(R.string.marketplace_search));
		// headerView.setRightButton(View.GONE, new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(getActivity(), FSActivity_Search.class);
		// startActivity(i);
		//
		// }
		// });
		// headerView.setProgressVisible(View.GONE);
		mBtnLeft = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.GONE);
		findViewById(R.id.logo).setVisibility(View.GONE);

		mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvTitle.setText(R.string.marketplace_search);
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		final PublisherAdView adview = (PublisherAdView) findViewById(R.id.adView);
		PublisherAdRequest.Builder re = new PublisherAdRequest.Builder();
		adview.loadAd(re.build());
		adview.setVisibility(View.GONE);
		adview.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				adview.setVisibility(View.VISIBLE);
				super.onAdLoaded();
			}
		});
	}

	@Override
	public void onPause() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdtKeyword.getWindowToken(), 0);
		super.onPause();
	}

	@Override
	public void onStart() {
		super.onStart();
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		tracker.send(MapBuilder.createAppView().build());
	}

	protected void performSearch() {
		String extra = "";
		extra = mEdtKeyword.getText().toString();

		Intent i = new Intent(this, SearchResultActivity.class);
		Bundle b = new Bundle();
		b.putString(FilterActivity.KEYWORD, extra);
		b.putString(FilterActivity.MARKETPLACE_CATEGORY,
				listCatId.get(mSpnCatId.getSelectedItem()));
		i.putExtra(SearchResultActivity.SCREEN_SEARCH_RESULT,
				"Marketplace Search Result");
		i.putExtras(b);
		startActivity(i);

	}
}
