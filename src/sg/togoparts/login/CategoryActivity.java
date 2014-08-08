package sg.togoparts.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.SectionAdapter;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.login.SectionResult.Section;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryActivity extends Activity {
	private ImageButton mBtnBack;
	private TextView mTvTitleHeader;
	private ProgressBar mProgress;
	private ListView mLvCategories;
	private SectionAdapter mAdapter;
	private ArrayList<Section> mListSection;
	
	private int mSectionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		mSectionId = getIntent().getIntExtra(Const.QUERY, 0);
		createHeader();
		mListSection = new ArrayList<SectionResult.Section>();
		mLvCategories = (ListView) findViewById(R.id.listView);
		mAdapter = new SectionAdapter(this, mListSection);
		mLvCategories.setAdapter(mAdapter);
		
		mLvCategories.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(CategoryActivity.this, CategoryActivity.class);
				i.putExtra(Const.QUERY, String.format(Const.URL_GET_SUBCATEGORY, mListSection.get(position).id));
				startActivity(i);
			}
		});
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SectionResult> myReq = new GsonRequest<SectionResult>(Method.POST, String.format(
				Const.URL_GET_SECTION, mS), SectionResult.class,
				createProfileSuccessListener(), createMyReqErrorListener()) {
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id", Const.getSessionId(CategoryActivity.this));
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
					Log.d("haipn", "list section lenght:" + response.Result.Sections.size());
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

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.GONE);
		findViewById(R.id.logo).setVisibility(View.GONE);
		
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_postad);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}
}
