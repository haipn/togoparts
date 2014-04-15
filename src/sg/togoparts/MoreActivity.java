package sg.togoparts;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MoreActivity extends TabActivity {
	TabHost tabHost;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);

		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, FSActivity_Search.class);
		spec = tabHost
				.newTabSpec("1")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_search))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FilterBikeShop.class);
		spec = tabHost
				.newTabSpec("2")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_search))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, AboutActivity.class);
		spec = tabHost
				.newTabSpec("3")
				.setIndicator(null,
						getResources().getDrawable(R.drawable.tab_shortlist))
				.setContent(intent);
		tabHost.addTab(spec);
		getTabWidget().setStripEnabled(false);
	}

}
