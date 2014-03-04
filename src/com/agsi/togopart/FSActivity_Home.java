package com.agsi.togopart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FSActivity_Home extends Activity_Main {
	static final String TAG = "FSActivity_Home";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addFragment(new HomeFragment(), false, FragmentTransaction.TRANSIT_NONE);
	}

	void addFragment(Fragment fragment, boolean addToBackStack, int transition) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.simple_fragment, fragment);
		ft.setTransition(transition);
		if (addToBackStack)
			ft.addToBackStack(null);
		ft.commit();
	}

	
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event)
	// {
	// if (keyCode == KeyEvent.KEYCODE_BACK)
	// {
	// if(Globals.isAppExitable)
	// {
	// return false;
	// }
	// else
	// return super.onKeyDown(keyCode, event);
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}
