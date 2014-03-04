package com.agsi.togopart;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.agsi.togopart.app.Const;

public class SearchFragment extends Fragment_Main {
	private EditText mEdtKeyword;
	private Button mBtnSearch;

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
		return root;
	}

	protected void performSearch() {
		String extra = "";
		extra = mEdtKeyword.getText().toString();

		Fragment fragment;
		fragment = new SearchResultFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Const.QUERY, extra);
		fragment.setArguments(bundle);
		addFragment(fragment, true, SearchResultFragment.class.getName(),
				FragmentTransaction.TRANSIT_NONE);
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdtKeyword.getWindowToken(), 0);
	}
}
