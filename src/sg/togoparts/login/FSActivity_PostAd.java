package sg.togoparts.login;

import sg.togoparts.Activity_Main;
import sg.togoparts.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

public class FSActivity_PostAd extends Activity_Main {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (Const.isLogin(this))
//			startActivity(new Intent(this, PostAdActivity.class));
		
//		else {
//			startActivity(new Intent(this, ChooseLogin.class));
//			
//		}
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
			// Log.d("haipn" , "home key back");
			// if (Const.isAppExitable) {
			return false;
			// } else
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
}
