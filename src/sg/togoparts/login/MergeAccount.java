package sg.togoparts.login;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MergeAccount extends FragmentActivity {
	protected String mAccessToken;
	protected String mEmailFb;
	protected String mIdFb;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.merge_account);
	}

	private void merge(final String userid) {
		RequestQueue queue = MyVolley.getRequestQueue();

		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_MERGE, ResultLogin.class,
				createMergeSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("FBid", mIdFb);
				params.put("FBemail", mEmailFb);
				params.put("Userid", userid);
				params.put("AccessToken", mAccessToken);
				return params;
			};
		};
		queue.add(myReq);
	}

	private Response.Listener<ResultLogin> createMergeSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				Log.d("haipn", "merge success:" + response.Result.Return);
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "response error:" + error.getMessage());
			}
		};
	}
}
