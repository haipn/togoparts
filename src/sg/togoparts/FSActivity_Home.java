package sg.togoparts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

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
