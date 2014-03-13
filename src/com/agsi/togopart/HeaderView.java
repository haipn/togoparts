package com.agsi.togopart;

import android.view.View;

public interface HeaderView {
	public void setLeftButton(int visible);
	public void setRightButton(int visible, View.OnClickListener click);
	public void setLogoVisible(int  visible);
	public void setTitleVisible(int visible, String text);
	public void setProgressVisible(int visible);
}
