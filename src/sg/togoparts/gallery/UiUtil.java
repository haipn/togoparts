package sg.togoparts.gallery;

import java.lang.ref.SoftReference;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UiUtil {

	public static final String TAG = "UiUtil";

	public static void setCustomFont(View textViewOrButton, Context ctx) {
		setCustomFont(textViewOrButton, ctx, "alternate-gothic-no3.ttf");
	}

	private static boolean setCustomFont(View textViewOrButton, Context ctx,
			String asset) {
		if (TextUtils.isEmpty(asset))
			return false;
		Typeface tf = null;
		try {
			tf = getFont(ctx, asset);
			if (textViewOrButton instanceof TextView) {
				((TextView) textViewOrButton).setTypeface(tf);
			} else if (textViewOrButton instanceof Button) {
				((Button) textViewOrButton).setTypeface(tf);
			} else if (textViewOrButton instanceof EditText) {
				((EditText) textViewOrButton).setTypeface(tf);
			}
			// else if (textViewOrButton instanceof Spinner) {
			// ((Spinner) textViewOrButton).setTypeface(tf);
			// }
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + asset, e);
			return false;
		}

		return true;
	}

	private static final Hashtable<String, SoftReference<Typeface>> fontCache = new Hashtable<String, SoftReference<Typeface>>();

	public static Typeface getFont(Context c, String name) {
		synchronized (fontCache) {
			if (fontCache.get(name) != null) {
				SoftReference<Typeface> ref = fontCache.get(name);
				if (ref.get() != null) {
					return ref.get();
				}
			}

			Typeface typeface = Typeface.createFromAsset(c.getAssets(), name);
			fontCache.put(name, new SoftReference<Typeface>(typeface));

			return typeface;
		}
	}

}
