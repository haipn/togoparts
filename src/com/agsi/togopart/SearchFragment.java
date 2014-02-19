package com.agsi.togopart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.agsi.togopart.app.Const;
import com.agsi.togopart.app.MyVolley;
import com.agsi.togopart.json.GsonRequest;
import com.agsi.togopart.json.ListCategories;
import com.agsi.togopart.json.ListCategories.Cat;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class SearchFragment extends Fragment {

	HashMap<String, String> listSize;
	HashMap<String, String> listUnit;
	HashMap<String, String> listCat;
	HashMap<String, String> listCatId;
	HashMap<String, String> listType;

	private EditText mEdtKeyWord;
	private EditText mEdtPostedBy;
	private Spinner mSpnSize;
	private Spinner mSpnUnit;
	private EditText mEdtValue;
	private EditText mEdtFrom;
	private EditText mEdtTo;
	private Spinner mSpnCategory;
	private Spinner mSpnCatId;
	private Spinner mSpnType;

	private Button mBtnSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_fragment, container,
				false);
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ListCategories> myReq = new GsonRequest<ListCategories>(
				Method.GET, Const.URL_SEARCH_CATEGORY, ListCategories.class,
				createMyReqSuccessListener(), createMyReqErrorListener());

		queue.add(myReq);
		mEdtFrom = (EditText) rootView.findViewById(R.id.edtFrom);
		mEdtKeyWord = (EditText) rootView.findViewById(R.id.edtKeyword);
		mEdtPostedBy = (EditText) rootView.findViewById(R.id.edtPostedBy);
		mEdtTo = (EditText) rootView.findViewById(R.id.edtTo);
		mEdtValue = (EditText) rootView.findViewById(R.id.edtValue);

		mSpnCategory = (Spinner) rootView.findViewById(R.id.spnCategory);
		mSpnCatId = (Spinner) rootView.findViewById(R.id.spnCatId);
		mSpnSize = (Spinner) rootView.findViewById(R.id.spnSize);
		mSpnType = (Spinner) rootView.findViewById(R.id.spnType);
		mSpnUnit = (Spinner) rootView.findViewById(R.id.spnUnit);

		mBtnSearch = (Button) rootView.findViewById(R.id.btnSearch);

		mBtnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String request = presetParamenters();
			}
		});
		
		return rootView;
	}

	protected String presetParamenters() {
		String keyword = mEdtKeyWord.getText().toString();
		String postedby = mEdtPostedBy.getText().toString();
		String size;
		if (mSpnSize.getSelectedItemPosition() == 0) {
			size = "";
		} else {
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	private void setListValues() {
		listSize = new LinkedHashMap<String, String>();
		listUnit = new LinkedHashMap<String, String>();
		listCat = new LinkedHashMap<String, String>();
		listCatId = new LinkedHashMap<String, String>();
		listType = new LinkedHashMap<String, String>();
		
		listSize.put("all", "All");
		listSize.put("na", "N-A");
		listSize.put("fs", "Free Size");
		listSize.put("xxs", "Extra Extra Small");
		listSize.put("xs", "Extra Small");
		listSize.put("s", "Small");
		listSize.put("ms", "Medium Small");
		listSize.put("m", "Medium");
		listSize.put("ml", "Medium Large");
		listSize.put("l", "Large");
		listSize.put("xl", "Extra Large");
		listSize.put("xxl", "Extra Extra Large");

		listUnit.put("all", "Unit");
		listUnit.put("n-a", "n-a");
		listUnit.put("C", "C");
		listUnit.put("cm", "cm");
		listUnit.put("grams", "grams");
		listUnit.put("inches", "inches");
		listUnit.put("kg", "kg");
		listUnit.put("lbs", "lbs");
		listUnit.put("mm", "mm");
		listUnit.put("speed", "speed");
		listUnit.put("teeth", "teeth");

		listCat.put("all", "All");
		listCat.put("1", "Commercial Merchants");
		listCat.put("2", "Commercial Member");
		listCat.put("3", "Priority");

		listType.put("all", "All");
		listType.put("1", "For Sale");
		listType.put("2", "Want to Buy");
		listType.put("3", "Exchange + Cash");
		listType.put("4", "Free!");
	}

	public void initSpinner() {
		ArrayAdapter<String> adtCategory = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_text);
		Iterator itCat = listCat.entrySet().iterator();
		while (itCat.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCat.next();
			adtCategory.add((String) pairs.getValue());
		}
		adtCategory
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnCategory.setAdapter(adtCategory);

		ArrayAdapter<String> adtCatId = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_text);
		Iterator itCatId = listCatId.entrySet().iterator();
		while (itCatId.hasNext()) {
			Map.Entry pairs = (Map.Entry) itCatId.next();
			adtCatId.add((String) pairs.getValue());
		}
		adtCatId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnCatId.setAdapter(adtCatId);

		ArrayAdapter<String> adtSize = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_text);
		Iterator itSize = listSize.entrySet().iterator();
		while (itSize.hasNext()) {
			Map.Entry pairs = (Map.Entry) itSize.next();
			adtSize.add((String) pairs.getValue());
		}
		adtSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnSize.setAdapter(adtSize);
		
		ArrayAdapter<String> adtType = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_text);
		Iterator itType = listType.entrySet().iterator();
		while (itType.hasNext()) {
			Map.Entry pairs = (Map.Entry) itType.next();
			adtType.add((String) pairs.getValue());
		}
		adtType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnType.setAdapter(adtType);
		
		ArrayAdapter<String> adtUnit = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_text);
		Iterator itUnit = listUnit.entrySet().iterator();
		while (itUnit.hasNext()) {
			Map.Entry pairs = (Map.Entry) itUnit.next();
			adtUnit.add((String) pairs.getValue());
		}
		adtUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnUnit.setAdapter(adtUnit);
	}
	private Response.Listener<ListCategories> createMyReqSuccessListener() {
		return new Response.Listener<ListCategories>() {
			@Override
			public void onResponse(ListCategories response) {
				setListValues();
				ArrayList<Cat> list = response.mp_categories;
				for (int i = 0; i < list.size(); i++) {
					listCatId.put(list.get(i).value, list.get(i).title);
				}
				initSpinner();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO: show dialog error
			}
		};
	}
}
