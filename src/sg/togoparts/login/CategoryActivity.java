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

public class CategoryActivity extends Activity {
	protected static final int REQUEST_SUB_CATEGORY = 0;
	private ImageButton mBtnBack;
	private TextView mTvTitleHeader;
	private ProgressBar mProgress;
	private ListView mLvCategories;
	private CategoryAdapter mAdapter;
	private ArrayList<Category> mListSection;

	private int mSectionId;
	private int mCategoryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		mSectionId = getIntent().getIntExtra(PostAdActivity.SECTION, 0);
		createHeader();
		mListSection = new ArrayList<Category>();
		mLvCategories = (ListView) findViewById(R.id.listView);
		mAdapter = new CategoryAdapter(this, mListSection);
		mLvCategories.setAdapter(mAdapter);

		mLvCategories.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(CategoryActivity.this,
						SubCategoryActivity.class);
				i.putExtra(PostAdActivity.SECTION, mSectionId);
				mCategoryId = mListSection.get(position).id;
				i.putExtra(PostAdActivity.CAT, mCategoryId);
				startActivityForResult(i, REQUEST_SUB_CATEGORY);
			}
		});
		mProgress.setVisibility(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<CategoryResult> myReq = new GsonRequest<CategoryResult>(
				Method.POST, String.format(Const.URL_GET_CATEGORY, mSectionId),
				CategoryResult.class, createProfileSuccessListener(),
				createMyReqErrorListener()) {
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id",
						Const.getSessionId(CategoryActivity.this));
				return params;
			};
		};
		queue.add(myReq);
	}

	private Listener<CategoryResult> createProfileSuccessListener() {
		return new Response.Listener<CategoryResult>() {

			@Override
			public void onResponse(CategoryResult response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response.Result != null && response.Result.Category != null) {
					Log.d("haipn", "list Category lenght:"
							+ response.Result.Category.size());
					mListSection.addAll(response.Result.Category);
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
		if (resultCode == RESULT_OK && requestCode == REQUEST_SUB_CATEGORY) {
			data.putExtra(PostAdActivity.CAT, mCategoryId);
			setResult(RESULT_OK, data);
			finish();
		}
	}

	private void createHeader() {
		mBtnBack = (ImageButton) findViewById(R.id.btnBack);
		findViewById(R.id.btnSearch).setVisibility(View.INVISIBLE);
		findViewById(R.id.logo).setVisibility(View.INVISIBLE);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mTvTitleHeader = (TextView) findViewById(R.id.title);
		mTvTitleHeader.setVisibility(View.VISIBLE);
		mTvTitleHeader.setText(R.string.title_category);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();
			}
		});
	}
}
