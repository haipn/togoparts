package sg.togoparts.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.CategoryAdapter;
import sg.togoparts.json.CategoryResult;
import sg.togoparts.json.CategoryResult.Category;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.SubCategoryResult;
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

public class SubCategoryActivity extends Activity {
	private ImageButton mBtnBack;
	private TextView mTvTitleHeader;
	private ProgressBar mProgress;
	private ListView mLvCategories;
	private CategoryAdapter mAdapter;
	private ArrayList<CategoryResult.Category> mListSection;

	private int mSectionId;
	private int mCategoryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		mSectionId = getIntent().getIntExtra(PostAdActivity.SECTION, 0);
		mCategoryId = getIntent().getIntExtra(PostAdActivity.CAT, 0);
		Log.d("haipn", "sessionid:" + mSectionId + ", categoryid :"
				+ +mCategoryId);
		createHeader();
		mListSection = new ArrayList<Category>();
		mLvCategories = (ListView) findViewById(R.id.listView);
		mAdapter = new CategoryAdapter(this, mListSection);
		mLvCategories.setAdapter(mAdapter);

		mLvCategories.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent data = getIntent();
				data.putExtra(PostAdActivity.SUB_CAT,
						mListSection.get(position).id);
				setResult(RESULT_OK, data);
				finish();
			}
		});
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SubCategoryResult> myReq = new GsonRequest<SubCategoryResult>(
				Method.POST, String.format(Const.URL_GET_SUBCATEGORY,
						mSectionId, mCategoryId), SubCategoryResult.class,
				createProfileSuccessListener(), createMyReqErrorListener()) {
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id",
						Const.getSessionId(SubCategoryActivity.this));
				return params;
			};
		};
		queue.add(myReq);
	}

	private Listener<SubCategoryResult> createProfileSuccessListener() {
		return new Response.Listener<SubCategoryResult>() {

			@Override
			public void onResponse(SubCategoryResult response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response.Result != null
						&& response.Result.Sub_category != null) {
					Log.d("haipn", "list sub category lenght:"
							+ response.Result.Sub_category.size());
					mListSection.addAll(response.Result.Sub_category);
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
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.INVISIBLE);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_sub_category);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}
}
