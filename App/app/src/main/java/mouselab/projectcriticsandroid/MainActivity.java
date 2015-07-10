package mouselab.projectcriticsandroid;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by benjamin on 10.07.15.
 */
public class MainActivity extends AppCompatActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    MainActivityPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =
                new MainActivityPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

        final ActionBar actionBar = getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        actionBar.setTitle("");
        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new TabListener<Fragment>(this,"collection_fragment",Fragment.class) {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.notification_template_icon_bg).setTabListener(tabListener).setText(ProfileFragment.ARG_OBJECT));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.notification_template_icon_bg).setTabListener(tabListener).setText(CameraFragment.ARG_OBJECT));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.notification_template_icon_bg).setTabListener(tabListener).setText(TopicsFragment.ARG_OBJECT));
    }
}
