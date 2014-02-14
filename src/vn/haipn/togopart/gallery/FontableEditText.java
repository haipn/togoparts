package vn.haipn.togopart.gallery;

import vn.haipn.togopart.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class FontableEditText extends EditText {

	public FontableEditText(Context context) {
        super(context);
    }

    public FontableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        UiUtil.setCustomFont(this, context);
    }

    public FontableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        UiUtil.setCustomFont(this, context);
    }

}
