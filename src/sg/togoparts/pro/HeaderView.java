package sg.togoparts.pro;

import android.view.View;

public interface HeaderView {
	public void setLeftButton(int visible);
	public void setRightBackground(int res);
	public void setRightButton(int visible, View.OnClickListener click);
	public void setLogoVisible(int  visible);
	public void setTitleVisible(int visible, String text);
	public void setProgressVisible(int visible);
}
