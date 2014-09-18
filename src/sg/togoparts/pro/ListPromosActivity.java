package sg.togoparts.pro;

import java.util.ArrayList;

import sg.togoparts.pro.app.Const;
import sg.togoparts.pro.app.ErrorInternetDialog;
import sg.togoparts.pro.app.MyVolley;
import sg.togoparts.pro.gallery.PromosAdapter;
import sg.togoparts.pro.json.GsonRequest;
import sg.togoparts.pro.json.ListPromos;
import sg.togoparts.pro.json.ListPromos.Promos;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ListPromosActivity extends FragmentActivity {
	PullToRefreshListView mLvPromos;
	PromosAdapter mAdapter;
	ArrayList<Promos> mListPromos;
	String mShopId;
	private TextView mTitle;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.promos_activity);
		
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, "Bikeshop Promos");
		tracker.send(MapBuilder.createAppView().build());
		
		mLvPromos = (PullToRefreshListView) findViewById(R.id.lvPromos);
		
		mShopId = getIntent().getStringExtra(Const.SHOP_ID);
		createHeader();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ListPromos> myReq = new GsonRequest<ListPromos>(Method.GET,
				String.format(Const.URL_PROMOS, mShopId), ListPromos.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
	}

	private Response.Listener<ListPromos> createMyReqSuccessListener() {
		return new Response.Listener<ListPromos>() {
			@Override
			public void onResponse(ListPromos response) {
				Log.d("haipn", "sussecc");
				findViewById(R.id.progress).setVisibility(View.GONE);
				mListPromos = response.list_promos;
				mAdapter = new PromosAdapter(ListPromosActivity.this, mListPromos);
				mLvPromos.setAdapter(mAdapter);
				mTitle.setText(response.scrtitle);
			}
		};
	}

	private void createHeader() {
		findViewById(R.id.btnBack).setVisibility(View.GONE);
		findViewById(R.id.btnSearch).setVisibility(View.GONE);
		findViewById(R.id.logo).setVisibility(View.GONE);

		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.bikeshop_promos);
		findViewById(R.id.progress).setVisibility(View.VISIBLE);
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				findViewById(R.id.progress).setVisibility(View.GONE);
				Log.d("haipn", "error:" + error.networkResponse);
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorInternetDialog newFragment = new ErrorInternetDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}
}
