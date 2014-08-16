package sg.togoparts.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.togoparts.DetailActivity;
import sg.togoparts.DetailFragment;
import sg.togoparts.Fragment_Main;
import sg.togoparts.HeaderView;
import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.ErrorInternetDialog;
import sg.togoparts.app.MyVolley;
import sg.togoparts.gallery.FontableTextView;
import sg.togoparts.gallery.InfoAdapter;
import sg.togoparts.gallery.SearchResultAdapter;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ResultLogin;
import sg.togoparts.json.SearchResult;
import sg.togoparts.json.SearchResult.AdsResult;
import sg.togoparts.login.AdProfileAdapter.QuickActionSelect;
import sg.togoparts.login.Profile.ProfileValue;
import sg.togoparts.login.Profile.Value;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileFragment extends Fragment_Main implements QuickActionSelect {
	public static final String PAGE_ID = "&page_id=";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected AdProfileAdapter mAdapter;
	private SwipeListView mLvResult;
	private TextView mTvNoShortlist;
	private ArrayList<AdsResult> mResult;
	private int mPageId;
	private int mPageTotal;
	protected boolean enableLoadMore;
	private String mStringTitle = "";

	private HeaderView headerView;
	private String mQuery;

	private ImageView mImvAvatar;
	private TextView mTvPositive;
	private TextView mTvNeutral;
	private TextView mTvNegative;
	private GridView mGvInfo;
	private InfoAdapter mInfoAdapter;
	private ArrayList<Value> mListValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Const.isLogin(getActivity())) {
			View rootView = inflater
					.inflate(R.layout.profile, container, false);
			mLvResult = (SwipeListView) rootView
					.findViewById(R.id.lvSearchResult);
			mTvNoShortlist = (TextView) rootView.findViewById(R.id.tvNoResult);
			mAdapter = new AdProfileAdapter(getActivity(), mResult, this);
			mLvResult.setAdapter(mAdapter);
			mLvResult.setSwipeListViewListener(new BaseSwipeListViewListener() {
				@Override
				public void onClickFrontView(int position) {
					super.onClickFrontView(position);
					Intent i = new Intent(getActivity(), DetailActivity.class);
					i.putExtra(Const.ADS_ID, mResult.get(position).aid);
					i.putExtra(Const.IS_MY_AD, true);
					startActivity(i);
				}
			});

			mImvAvatar = (ImageView) rootView.findViewById(R.id.imvAvatar);
			mTvPositive = (TextView) rootView.findViewById(R.id.tvPositive);
			mTvNeutral = (TextView) rootView.findViewById(R.id.tvNeutral);
			mTvNegative = (TextView) rootView.findViewById(R.id.tvNegative);

			mGvInfo = (GridView) rootView.findViewById(R.id.gvInfo);
			mInfoAdapter = new InfoAdapter(getActivity(), mListValue);
			mGvInfo.setAdapter(mInfoAdapter);
			return rootView;
		} else {
			TextView rootView = new FontableTextView(getActivity());
			rootView.setText(R.string.msg_signin);
			return rootView;
		}
	}

	protected void loadMore() {
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<SearchResult> myReq = new GsonRequest<SearchResult>(
				Method.GET, mQuery, SearchResult.class,
				createMyReqSuccessListener(), createMyReqErrorListener());
		queue.add(myReq);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setRetainInstance(true);
		Log.d("haipn", "search result oncreate");
		createHeader();
		if (Const.isLogin(getActivity())) {
			enableLoadMore = false;
			mQuery = Const.URL_GET_MY_ADS;
			getProfile();
			mResult = new ArrayList<AdsResult>();
			mListValue = new ArrayList<Profile.Value>();
		}

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		mResult.clear();
		loadMore();
		super.onResume();
	}

	private void getProfile() {
		Log.d("haipn",
				"url:"
						+ (Const.URL_GET_PROFILE + Const
								.getSessionId(getActivity())));
		headerView.setProgressVisible(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<Profile> myReq = new GsonRequest<Profile>(Method.POST,
				Const.URL_PROFILE, Profile.class,
				createProfileSuccessListener(), createMyReqErrorListener()) {
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id", Const.getSessionId(getActivity()));
				return params;
			};
		};
		queue.add(myReq);
	}

	private Listener<Profile> createProfileSuccessListener() {
		return new Response.Listener<Profile>() {

			@Override
			public void onResponse(Profile response) {
				headerView.setProgressVisible(View.GONE);
				Log.d("haipn", "profile response:" + response.Result.Return);
				if (response.Result.Return.equals("expired")) {
					processExpired();
				} else if (response.Result.Return.equals("success")) {
					mQuery = String.format(Const.URL_GET_MY_ADS,
							response.Result.info.username);
					updateProfile(response.Result);
					loadMore();
				} else {
					showError(response.Result.Message);
				}
			}
		};
	}

	protected void updateProfile(ProfileValue result) {
		mTvNegative.setText(result.ratings.Negative + "");
		mTvNeutral.setText(result.ratings.Neutral + "");
		mTvPositive.setText(result.ratings.Positive + "");
		imageLoader.displayImage(result.info.picture, mImvAvatar);
		if (result.quota != null)
			mListValue.addAll(result.quota);
		else if (result.postingpack != null)
			mListValue.addAll(result.postingpack);
		mInfoAdapter.notifyDataSetChanged();
		Const.setGridViewHeightBasedOnChildren(mGvInfo, 2);
	}

	protected void processExpired() {
		headerView.setProgressVisible(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_SESSION_REFRESH, ResultLogin.class,
				createExpireSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id", Const.getSessionId(getActivity()));
				params.put("refresh_id", Const.getSHA256EncryptedString(Const
						.getRefreshId(getActivity()) + ChooseLogin.CLIENT_ID));
				return params;
			};
		};
		queue.add(myReq);
		mResult = new ArrayList<AdsResult>();
	}

	private Listener<ResultLogin> createExpireSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				if (response.Result.Return.equals("success")) {
					Const.writeSessionId(getActivity(),
							response.Result.session_id,
							response.Result.refresh_id);
					headerView.setProgressVisible(View.GONE);
					getProfile();
				} else {
					showError(response.Result.Message);
				}
			}
		};
	}

	protected void showError(String mesg) {
		if (mesg != null) {
			ErrorDialog dialog = new ErrorDialog(mesg);
			dialog.show(getActivity().getSupportFragmentManager(),
					"error dialog");
		} else {
			ErrorInternetDialog internet = new ErrorInternetDialog();
			internet.show(getActivity().getSupportFragmentManager(),
					"error internet");
		}
	}

	private Response.Listener<SearchResult> createMyReqSuccessListener() {
		return new Response.Listener<SearchResult>() {
			@Override
			public void onResponse(SearchResult response) {
				if (response.ads != null && !response.ads.isEmpty()) {
					mResult.addAll(response.ads);
					mAdapter.notifyDataSetChanged();
					mTvNoShortlist.setVisibility(View.GONE);
					mLvResult.setVisibility(View.VISIBLE);

				} else {
					mTvNoShortlist.setVisibility(View.VISIBLE);
					mLvResult.setVisibility(View.GONE);
				}
				// mLvResult.setAdapter(mAdapter);
			}
		};
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
		headerView = (HeaderView) getActivity();
		headerView.setLeftButton(View.VISIBLE);
		headerView.setLogoVisible(View.GONE);
		headerView.setTitleVisible(View.VISIBLE,
				getString(R.string.title_profile));
		headerView.setRightButton(View.INVISIBLE, null);
	}

	protected void manageAd(final String action, final String aid) {
		headerView.setProgressVisible(View.VISIBLE);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_MANAGE_AD, ResultLogin.class,
				createManageSuccessListener(), createMyReqErrorListener()) {
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id", Const.getSessionId(getActivity()));
				params.put("aid", aid);
				params.put("action", action);
				return params;
			};
		};
		queue.add(myReq);
	}

	@Override
	public void onRepostClick(final String aid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.msg_repost_confirm)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								manageAd("repost", aid);
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						});
		builder.show();
	}

	@Override
	public void onTakeDownClick(final String aid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.msg_take_down_confirm)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								manageAd("takedown", aid);
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						});
		builder.show();
	}

	@Override
	public void onMarkAsSoldClick(final String aid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.msg_mark_as_sold)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								manageAd("sold", aid);
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						});
		builder.show();
	}

	@Override
	public void onRefreshClick(final String aid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.msg_refresh_confirm)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								manageAd("refresh", aid);
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								mLvResult.closeOpenedItems();
							}
						});
		builder.show();
	}

	private Listener<ResultLogin> createManageSuccessListener() {
		return new Response.Listener<ResultLogin>() {

			@Override
			public void onResponse(ResultLogin response) {
				headerView.setProgressVisible(View.GONE);
				Log.d("haipn", "profile response:" + response.Result.Return);
				if (response.Result.Return.equals("success")) {
					mResult.clear();
					loadMore();
					MessageDialog success = new MessageDialog(
							response.Result.Message);
					success.show(getActivity().getSupportFragmentManager(),
							"success");
				} else {
					showError(response.Result.Message);
				}
			}
		};
	}
}
