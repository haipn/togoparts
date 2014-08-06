package sg.togoparts.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import sg.togoparts.Activity_Main;
import sg.togoparts.R;

public class FSActivity_PostAd extends Activity_Main {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addFragment(new ProfileFragment(), false, FragmentTransaction.TRANSIT_NONE);
	}

	void addFragment(Fragment fragment, boolean addToBackStack, int transition) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.simple_fragment, fragment);
		ft.setTransition(transition);
		if (addToBackStack)
			ft.addToBackStack(null);
		ft.commit();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Log.d("haipn" , "home key back");
//			if (Const.isAppExitable) {
				return false;
//			} else
//				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
}
