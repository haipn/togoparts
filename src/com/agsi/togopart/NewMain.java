package com.agsi.togopart;


//public class NewMain extends ActionBarActivity {
//	private ViewPager viewPager;
//	private TabsAdapter tabsAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		viewPager = new ViewPager(this);
//		viewPager.setId(R.id.pager);
//		setContentView(viewPager);
//		ActionBar bar = getSupportActionBar();
//		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		tabsAdapter = new TabsAdapter(this, viewPager);
//		tabsAdapter.addTab(bar.newTab().setText("Tab One"), HomeFragment.class,
//				null);
//		tabsAdapter.addTab(bar.newTab().setText("Tab Two"),
//				FilterActivity.class, null);
//	}
//
//	public static class TabsAdapter extends FragmentPagerAdapter implements
//			ActionBar.TabListener, ViewPager.OnPageChangeListener {
//		private final Context context;
//		private final ActionBar actionBar;
//		private final ViewPager viewPager;
//		private final ArrayList<TabInfo> tabs = new ArrayList<TabInfo>();
//
//		static final class TabInfo {
//			private final Class<?> clss;
//			private final Bundle args;
//
//			TabInfo(Class<?> _class, Bundle _args) {
//				clss = _class;
//				args = _args;
//			}
//		}
//
//		public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
//			super(activity.getSupportFragmentManager());
//			context = activity;
//			actionBar = activity.getSupportActionBar();
//			viewPager = pager;
//			viewPager.setAdapter(this);
//			viewPager.setOnPageChangeListener(this);
//		}
//
//		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
//			TabInfo info = new TabInfo(clss, args);
//			tab.setTag(info);
//			tab.setTabListener(this);
//			tabs.add(info);
//			actionBar.addTab(tab);
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public int getCount() {
//			return tabs.size();
//		}
//
//		@Override
//		public Fragment getItem(int position) {
//			TabInfo info = tabs.get(position);
//			return Fragment
//					.instantiate(context, info.clss.getName(), info.args);
//		}
//
//		public void onPageScrolled(int position, float positionOffset,
//				int positionOffsetPixels) {
//		}
//
//		public void onPageSelected(int position) {
//			actionBar.setSelectedNavigationItem(position);
//		}
//
//		public void onPageScrollStateChanged(int state) {
//		}
//
//		public void onTabSelected(Tab tab, FragmentTransaction ft) {
//			Object tag = tab.getTag();
//			for (int i = 0; i < tabs.size(); i++) {
//				if (tabs.get(i) == tag) {
//					viewPager.setCurrentItem(i);
//				}
//			}
//		}
//
//		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//		}
//
//		public void onTabReselected(Tab tab, FragmentTransaction ft) {
//		}
//	}
//
//}
