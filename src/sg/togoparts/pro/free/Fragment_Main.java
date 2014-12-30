package sg.togoparts.pro.free;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import sg.togoparts.pro.free.R;

public class Fragment_Main extends Fragment {
	protected void addFragment(Fragment fragment, boolean addToBackStack,
			int transition) {
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.replace(R.id.simple_fragment, fragment);
		ft.setTransition(transition);
		if (addToBackStack)
			ft.addToBackStack(null);
		ft.commit();
	}

	protected void addFragment(Fragment fragment, boolean addToBackStack,
			String backStackTag, int transition) {
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.replace(R.id.simple_fragment, fragment, backStackTag);
		ft.setTransition(transition);
		if (addToBackStack)
			ft.addToBackStack(backStackTag);
		ft.commit();
	}

//	protected ImageButton getButtonLeft(Activity context) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View root = inflater.inflate(R.id.header, null);
//		return (ImageButton) root.findViewById(R.id.btnBack);
//	}
//
//	protected ImageButton getButtonRight(Activity context) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View root = inflater.inflate(R.layout.fragmentstackctivity, null);
//		View header = root.findViewById(R.id.header);
//		return (ImageButton) header.findViewById(R.id.btnSearch);
//	}
//
//	protected ImageView getLogo(Activity context) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View root = inflater.inflate(R.layout.fragmentstackctivity, null);
//		View header = root.findViewById(R.id.header);
//		return (ImageView) header.findViewById(R.id.logo);
//	}
//
//	protected TextView getTitle(Activity context) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View root = inflater.inflate(R.id.fragmentstackctivity, null);
//		View header = root.findViewById(R.id.header);
//		return (TextView) header.findViewById(R.id.title);
//	}
}
