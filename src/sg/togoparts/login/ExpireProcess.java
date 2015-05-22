package sg.togoparts.login;

import java.util.HashMap;
import java.util.Map;

import sg.togoparts.R;
import sg.togoparts.app.Const;
import sg.togoparts.app.MyVolley;
import sg.togoparts.json.GsonRequest;
import sg.togoparts.json.ResultLogin;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class ExpireProcess {
	public interface OnExpireResult {
		public void onSuccess();

		public void onError(String message);
	}

	public ExpireProcess(Context context, OnExpireResult callback) {
		super();
		this.mContext = context;
		mCallback = callback;
		
	}
	OnExpireResult mCallback;
	Context mContext;

	protected void processExpired() {
		RequestQueue queue = MyVolley.getRequestQueue();
		GsonRequest<ResultLogin> myReq = new GsonRequest<ResultLogin>(
				Method.POST, Const.URL_SESSION_REFRESH, ResultLogin.class,
				createExpireSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("session_id", Const.getSessionId(mContext));
				params.put("refresh_id", Const.getSHA256EncryptedString(Const
						.getRefreshId(mContext) + ChooseLogin.CLIENT_ID));
				return params;
			};
		};
		queue.add(myReq);
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("haipn", "error:" + error.networkResponse);
				mCallback.onError(mContext.getString(R.string.error));
			}
		};
	}

	private Listener<ResultLogin> createExpireSuccessListener() {
		return new Response.Listener<ResultLogin>() {
			@Override
			public void onResponse(ResultLogin response) {
				if (response.Result.Return.equals("success")) {
					Const.writeSessionId(mContext, response.Result.session_id,
							response.Result.refresh_id);
					mCallback.onSuccess();
				} else {
					mCallback.onError(response.Result.Message);
				}
			}
		};
	}
}
