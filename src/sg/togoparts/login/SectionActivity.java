package sg.togoparts.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.SectionAdapter;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.SectionResult;
import sg.togoparts.json.SectionResult.Section;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class SectionActivity extends Activity {

	public static final String SECTION_ID = "section id";
	public static final String CATEGORY_ID = "category id";
	public static final int REQUEST_CATEGORY = 0;
	private ImageButton mBtnBack;
	private TextView mTvTitleHeader;
	private ProgressBar mProgress;
	private ListView mLvSection;
	private SectionAdapter mAdapter;
	private ArrayList<Section> mListSection;
	private int mSessionId;
	private int mCategoryId;
	private int mSubCategoryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		createHeader();
		mListSection = new ArrayList<SectionResult.Section>();
		mLvSection = (ListView) findViewById(R.id.listView);
		mAdapter = new SectionAdapter(this, mListSection);
		mLvSection.setAdapter(mAdapter);

		mLvSection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(SectionActivity.this,
						CategoryActivity.class);
				mSessionId = mListSection.get(position).id;
				i.putExtra(PostAdActivity.SECTION, mSessionId);
				startActivityForResult(i, REQUEST_CATEGORY);
			}
		});
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SectionResult> myReq = new GsonRequest<SectionResult>(
				Method.POST, Const.URL_GET_SECTION, SectionResult.class,
				createProfileSuccessListener(), createMyReqErrorListener()) {
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id",
						Const.getSessionId(SectionActivity.this));
				return params;
			};
		};
		queue.add(myReq);
	}

	private Listener<SectionResult> createProfileSuccessListener() {
		return new Response.Listener<SectionResult>() {

			@Override
			public void onResponse(SectionResult response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response.Result != null && response.Result.Sections != null) {
					Log.d("haipn", "list section lenght:"
							+ response.Result.Sections.size());
					mListSection.addAll(response.Result.Sections);
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
		if (resultCode == RESULT_OK && requestCode == REQUEST_CATEGORY) {
			data.putExtra(PostAdActivity.SECTION, mSessionId);
			setResult(RESULT_OK, data);
			finish();
		}
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.GONE);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_section);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}
}
