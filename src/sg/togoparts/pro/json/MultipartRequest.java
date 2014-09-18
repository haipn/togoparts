package sg.togoparts.pro.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import sg.togoparts.pro.login.PostAdActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;

public class MultipartRequest<T> extends GsonRequest<T> {
	private HttpEntity httpEntity;

	public MultipartRequest(int method, String url, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener, PostAd ad) {
		super(method, url, clazz, listener, errorListener);
		httpEntity = buildMultipartEntity(ad);

	}

	private HttpEntity buildMultipartEntity(PostAd post) {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody(PostAdActivity.ADDRESS, post.getAddress());
//		if (post.getAdpic1().length() > 0)
//			builder.addBinaryBody(PostAdActivity.ADPIC1,
//					new File(post.getAdpic1()));
//		if (post.getAdpic2().length() > 0)
//			builder.addBinaryBody(PostAdActivity.ADPIC2,
//					new File(post.getAdpic2()));
//		if (post.getAdpic3().length() > 0)
//			builder.addBinaryBody(PostAdActivity.ADPIC3,
//					new File(post.getAdpic3()));
//		if (post.getAdpic4().length() > 0)
//			builder.addBinaryBody(PostAdActivity.ADPIC4,
//					new File(post.getAdpic4()));
//		if (post.getAdpic5().length() > 0)
//			builder.addBinaryBody(PostAdActivity.ADPIC5,
//					new File(post.getAdpic5()));
//		if (post.getAdpic6().length() > 0)
//			builder.addBinaryBody(PostAdActivity.ADPIC6,
//					new File(post.getAdpic6()));
		if (post.getAdpic1().length() > 0)
			builder.addPart(PostAdActivity.ADPIC1,
					new FileBody(new File(post.getAdpic1())));
		if (post.getAdpic2().length() > 0)
			builder.addPart(PostAdActivity.ADPIC2,
					new FileBody(new File(post.getAdpic2())));
		Log.d("haipn", "pic1:" + post.getAdpic1());
		if (post.getAdpic3().length() > 0)
			builder.addBinaryBody(PostAdActivity.ADPIC3,
					new File(post.getAdpic3()));
		if (post.getAdpic4().length() > 0)
			builder.addBinaryBody(PostAdActivity.ADPIC4,
					new File(post.getAdpic4()));
		if (post.getAdpic5().length() > 0)
			builder.addBinaryBody(PostAdActivity.ADPIC5,
					new File(post.getAdpic5()));
		if (post.getAdpic6().length() > 0)
			builder.addBinaryBody(PostAdActivity.ADPIC6,
					new File(post.getAdpic6()));
		builder.addTextBody(PostAdActivity.SESSION_ID, post.getSession_id());
		builder.addTextBody(PostAdActivity.ADTYPE, post.getAdtype() + "");
		builder.addTextBody(PostAdActivity.AID, post.getAid() + "");
		builder.addTextBody(PostAdActivity.BRAND, post.getBrand());
		builder.addTextBody(PostAdActivity.CAT, post.getCat() + "");
		builder.addTextBody(PostAdActivity.CITY, post.getCity());
		builder.addTextBody(PostAdActivity.CLEARANCE, post.isClearance() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.COLOUR, post.getColour());
		builder.addTextBody(PostAdActivity.CONDITION, post.getCondition() + "");
		builder.addTextBody(PostAdActivity.CONTACTNO, post.getContactno());
		builder.addTextBody(PostAdActivity.CONTACTPERSON,
				post.getContactperson());
		builder.addTextBody(PostAdActivity.COUNTRY, post.getCountry());
		builder.addTextBody(PostAdActivity.D_BMX, post.isD_bmx() ? "1" : "0");
		builder.addTextBody(PostAdActivity.D_COMMUTE, post.isD_commute() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.D_FOLDING, post.isD_folding() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.D_MTB, post.isD_mtb() ? "1" : "0");
		builder.addTextBody(PostAdActivity.D_OTHERS, post.isD_others() ? "1"
				: "0");
		builder.addTextBody(PostAdActivity.D_ROAD, post.isD_road() ? "1" : "0");
		builder.addTextBody(PostAdActivity.DESCRIPTION, post.getDescription());
		builder.addTextBody(PostAdActivity.ITEM, post.getItem());
		builder.addTextBody(PostAdActivity.ITEM_YEAR, post.getItem_year());
		builder.addTextBody(PostAdActivity.LAT, post.getLatitude() + "");
		builder.addTextBody(PostAdActivity.LONGITUDE, post.getLongitude() + "");
		builder.addTextBody(PostAdActivity.ORIGINAL_PRICE,
				post.getOriginal_price() + "");
		builder.addTextBody(PostAdActivity.PICTURELINK, post.getPicturelink());
		builder.addTextBody(PostAdActivity.POSTALCODE, post.getPostalcode());
		builder.addTextBody(PostAdActivity.PRICE, post.getPrice() + "");
		builder.addTextBody(PostAdActivity.PRICETYPE, post.getPricetype() + "");
		builder.addTextBody(PostAdActivity.REGION, post.getRegion());
		builder.addTextBody(PostAdActivity.SECTION, post.getSection() + "");
		builder.addTextBody(PostAdActivity.SIZE, post.getSize());
		builder.addTextBody(PostAdActivity.SUB_CAT, post.getSub_cat() + "");
		builder.addTextBody(PostAdActivity.TIME_TO_CONTACT,
				post.getTime_to_contact());
		builder.addTextBody(PostAdActivity.TITLE, post.getTitle());
		builder.addTextBody(PostAdActivity.TRANSTYPE, post.getTranstype() + "");
		builder.addTextBody(PostAdActivity.WARRANTY, post.getWarranty() + "");
		builder.addTextBody(PostAdActivity.WEIGHT, post.getWeight() + "");
		// builder.addTextBody(KEY_ROUTE_ID, mRouteId);
		return builder.build();
	}
	
	@Override
    public String getBodyContentType() {
        return httpEntity.getContentType().getValue();
    }
 
    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
        	httpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
}
