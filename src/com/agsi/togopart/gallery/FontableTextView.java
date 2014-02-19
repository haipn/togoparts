package com.agsi.togopart.gallery;

import com.agsi.togopart.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontableTextView extends TextView {

    public FontableTextView(Context context) {
        super(context);
    }

    public FontableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        UiUtil.setCustomFont(this,context);
    }

    public FontableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        UiUtil.setCustomFont(this,context);
    }
}