package sg.togoparts.pro.free;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import sg.togoparts.pro.free.R;

public class FSActivity_MarketPlace extends Activity_Main
{

	static final String TAG = "FSActivity_NewCars";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// add initial fragment, do not add to back stack, no transition animation
		addFragment(new MarketPlaceFragment(), false, FragmentTransaction.TRANSIT_NONE);
	}

	void addFragment(Fragment fragment, boolean addToBackStack, int transition)
	{
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.simple_fragment, fragment);
		ft.setTransition(transition);
		if (addToBackStack)
			ft.addToBackStack(null);
		ft.commit();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
//        	if(Const.isAppExitable)
//        	{	
        		return false;
//        	}
//        	else
//        		return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}