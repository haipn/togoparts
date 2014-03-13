package com.agsi.togopart;

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

public class SearchFragment extends Fragment_Main {
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
				TabsActivityMain tab = (TabsActivityMain) getActivity()
						.getParent();
				tab.getTabHost().setCurrentTab(3);
			}
		});
		headerView.setProgressVisible(View.GONE);
		
		
	}
	protected void performSearch() {
		String extra = "";
		extra = mEdtKeyword.getText().toString();

		Intent i = new Intent(getActivity(), SearchResultActivity.class);
		i.putExtra(FilterActivity.KEYWORD, extra);
		startActivity(i);
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdtKeyword.getWindowToken(), 0);
	}
}
