package sg.togoparts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import sg.togoparts.R;

public class FSActivity_Search extends Activity_Main {

	static final String TAG = "FSActivity_Search";
	private static final int FILTER_RETURN = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// add initial fragment, do not add to back stack, no transition
		// animation
		addFragment(new SearchFragment(), false,
				FragmentTransaction.TRANSIT_NONE);
	}

	void addFragment(Fragment fragment, boolean addToBackStack, int transition) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.simple_fragment, fragment, fragment.getClass()
				.getName());
		ft.setTransition(transition);
		if (addToBackStack)
			ft.addToBackStack(null);
		ft.commit();
	}

	// @Override
	// protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	// if (arg0 == FILTER_RETURN) {
	// if (arg1 == RESULT_CANCELED) {
	// Log.d("haipn", "result cancel");
	// } else {
	// Fragment fragment =
	// getSupportFragmentManager().findFragmentByTag(SearchResultFragment.class.getName());
	// if (fragment == null)
	// fragment = new SearchResultFragment();
	// fragment.setArguments(arg2.getExtras());
	// FragmentTransaction ft = getSupportFragmentManager()
	// .beginTransaction();
	// ft.replace(R.id.simple_fragment, fragment,
	// fragment.getClass().getName());
	// ft.setTransition(FragmentTransaction.TRANSIT_NONE);
	// ft.commitAllowingStateLoss();
	// Log.d("haipn", "result ok");
	// }
	// }
	// super.onActivityResult(arg0, arg1, arg2);
	// }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (Const.isAppExitable) {
				return false;
//			} else
//				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
}