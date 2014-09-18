package sg.togoparts.pro.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.togoparts.pro.R;
import sg.togoparts.pro.app.Const;
import sg.togoparts.pro.app.MyVolley;
import sg.togoparts.pro.gallery.SectionAdapter;
import sg.togoparts.pro.json.GsonRequest;
import sg.togoparts.pro.json.SectionResult;
import sg.togoparts.pro.json.SectionResult.Section;
import sg.togoparts.pro.login.ExpireProcess.OnExpireResult;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

public class SectionActivity extends FragmentActivity implements OnExpireResult {

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

	private int adType;
	private int mTcred;
	private int mSelectedCost1;
	private int mSelectedCost2;
	String tCredLink;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		createHeader();
		tCredLink = getIntent().getStringExtra("tcredlink");
		adType = getIntent().getIntExtra(PostAdActivity.TYPE_AD_POST, 0);
		mTcred = getIntent().getIntExtra(PostAdActivity.TCRED, 0);
		mSessionId = getIntent().getIntExtra(PostAdActivity.SECTION, -1);
		mCategoryId = getIntent().getIntExtra(PostAdActivity.CAT, -1);
		mSubCategoryId = getIntent().getIntExtra(PostAdActivity.SUB_CAT, -1);
		mListSection = new ArrayList<SectionResult.Section>();
		mLvSection = (ListView) findViewById(R.id.listView);
		mAdapter = new SectionAdapter(this, mListSection, mSessionId);
		mLvSection.setAdapter(mAdapter);

		mLvSection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Section section = mListSection.get(position);
				if (adType == 1 && section.data != null
						&& section.data.priority_cost > mTcred) {
					showTcred("Priority");
				} else if (adType == 2 && section.data != null
						&& section.data.newitem_cost > mTcred) {
					showTcred("New Item");
				} else {
					mSelectedCost1 = section.data.priority_cost;
					mSelectedCost2 = section.data.newitem_cost;
					Intent i = new Intent(SectionActivity.this,
							CategoryActivity.class);
					mSessionId = mListSection.get(position).id;
					i.putExtra(PostAdActivity.SECTION, mSessionId);
					i.putExtra(PostAdActivity.CAT, mCategoryId);
					i.putExtra(PostAdActivity.SUB_CAT, mSubCategoryId);
					startActivityForResult(i, REQUEST_CATEGORY);
				}
			}
		});
		getSections();
	}

	private void getSections() {
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

	protected void showTcred(String string) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.message_tcred, string))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.title_tcred)
				.setPositiveButton("Buy TCredits",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(tCredLink));
								startActivity(i);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private Listener<SectionResult> createProfileSuccessListener() {
		return new Response.Listener<SectionResult>() {

			@Override
			public void onResponse(SectionResult response) {
				mProgress.setVisibility(View.INVISIBLE);
				if (response.Result.Return != null
						&& response.Result.Return.equals("expired")) {
					processExpired();
				} else if (response.Result != null
						&& response.Result.Sections != null) {
					Log.d("haipn", "list section lenght:"
							+ response.Result.Sections.size());
					mListSection.addAll(response.Result.Sections);
					mAdapter.notifyDataSetChanged();
				}
			}
		};
	}

	protected void processExpired() {
		mProgress.setVisibility(View.VISIBLE);
		ExpireProcess expire = new ExpireProcess(this, this);
		expire.processExpired();
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
			data.putExtra("cost1", mSelectedCost1);
			data.putExtra("cost2", mSelectedCost2);
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

	@Override
	public void onSuccess() {
		mProgress.setVisibility(View.INVISIBLE);
		getSections();
	}

	@Override
	public void onError(String message) {
		mProgress.setVisibility(View.INVISIBLE);
		ErrorDialog errorDialog = new ErrorDialog(message);
		errorDialog.show(getSupportFragmentManager(), "error");
	}
}
