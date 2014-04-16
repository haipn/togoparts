package sg.togoparts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import sg.togoparts.R;
import sg.togoparts.app.Const;

public class SearchFragment extends Fragment_Main {
	private static final String SCREEN_LABEL = "Marketplace Search Form";
	private EditText mEdtKeyword;
	private Button mBtnSearch;
	private HeaderView headerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater
				.inflate(R.layout.search_fragment, container, false);
		mEdtKeyword = (EditText) root.findViewById(R.id.edtKeyword);
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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

		mBtnSearch = (Button) root.findViewById(R.id.btnSearch);
		mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				performSearch();
			}
		});
		createHeader();
		return root;
	}

	private void createHeader() {
		headerView = (HeaderView) getActivity();
		headerView.setLeftButton(View.VISIBLE);
		headerView.setLogoVisible(View.GONE);
		headerView.setTitleVisible(View.VISIBLE, getString(R.string.search));
		headerView.setRightButton(View.GONE, new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),
						FSActivity_Search.class);
				startActivity(i);
				
			}
		});
		headerView.setProgressVisible(View.GONE);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AdView adview = (AdView) getActivity().findViewById(R.id.adView);
		AdRequest re = new AdRequest();
		adview.loadAd(re);
	}

	@Override
	public void onPause() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdtKeyword.getWindowToken(), 0);
		super.onPause();
	}

	@Override
	public void onStart() {
		super.onStart();
		Tracker tracker = GoogleAnalytics.getInstance(getActivity())
				.getTracker(Const.GA_PROPERTY_ID);
		tracker.set(Fields.SCREEN_NAME, SCREEN_LABEL);
		tracker.send(MapBuilder.createAppView().build());
	}

	protected void performSearch() {
		String extra = "";
		extra = mEdtKeyword.getText().toString();

		Intent i = new Intent(getActivity(), SearchResultActivity.class);
		i.putExtra(FilterActivity.KEYWORD, extra);
		i.putExtra(SearchResultActivity.SCREEN_SEARCH_RESULT,
				"Marketplace Search Result");
		startActivity(i);

	}
}
