package sg.togoparts.login;

import java.util.ArrayList;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.MessageAdapter;
import sg.togoparts.json.AdsDetail.Message;
import sg.togoparts.json.AllComment;
import sg.togoparts.json.GsonRequest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class MessageActivity extends FragmentActivity {
	private ImageButton mBtnBack;
	private TextView mTvTitleHeader;
	private ProgressBar mProgress;
	private ListView mLvComment;
	private MessageAdapter mAdapter;
	private ArrayList<Message> mListComment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, "Marketplace Ad Comments");
		tracker.send(MapBuilder.createAppView().build());
		
		setContentView(R.layout.list_activity);
		createHeader();
		mListComment = new ArrayList<Message>();
		mLvComment = (ListView) findViewById(R.id.listView);
		mAdapter = new MessageAdapter(this, mListComment);
		mLvComment.setAdapter(mAdapter);

		// mLvComment.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Intent i = new Intent(MessageActivity.this,
		// CategoryActivity.class);
		// mSessionId = mListComment.get(position).id;
		// i.putExtra(PostAdActivity.SECTION, mSessionId);
		// startActivityForResult(i, REQUEST_CATEGORY);
		// }
		// });
		String aid = getIntent().getStringExtra(Const.ADS_ID);
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<AllComment> myReq = new GsonRequest<AllComment>(Method.GET,
				String.format(Const.URL_ALL_MESSAGE, aid), AllComment.class,
				createProfileSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
	}

	private Listener<AllComment> createProfileSuccessListener() {
		return new Response.Listener<AllComment>() {

			@Override
			public void onResponse(AllComment response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response.messages != null) {
					mListComment.addAll(response.messages);
					mAdapter.notifyDataSetChanged();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.GONE);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_all_comment);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}
}
