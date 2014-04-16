package sg.togoparts;

import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.About;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ShopDetail;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutActivity extends FragmentActivity {
	TextView mTitle;
	WebView mWvAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		mWvAbout = (WebView) findViewById(R.id.wvAbout);
		mTitle = (TextView) findViewById(R.id.tvTitle);
		createHeader();
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<About> myReq = new GsonRequest<About>(Method.GET,
				String.format(Const.URL_ABOUT), About.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
	}

	private Response.Listener<About> createMyReqSuccessListener() {
		return new Response.Listener<About>() {
			@Override
			public void onResponse(About response) {
				Log.d("haipn", "sussecc");
				mWvAbout.loadData(response.content, "text/html", "UTF-8");
				mTitle.setText(response.title);
			}
		};
	}

	private void createHeader() {
		findViewById(R.id.btnBack).setVisibility(View.GONE);
		findViewById(R.id.btnSearch).setVisibility(View.GONE);
		findViewById(R.id.logo).setVisibility(View.VISIBLE);

		findViewById(R.id.title).setVisibility(View.GONE);
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ErrorDialog newFragment = new ErrorDialog();
				newFragment.show(ft, "error dialog");
			}
		};
	}

}
