package mouselab.projectcriticsandroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Jef on 10/07/2015.
 */
public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {
    public MainActivityPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                Fragment profileFrag = new ProfileFragment();
                Bundle args = new Bundle();
                // Our object is just an integer :-P
                args.putString(ProfileFragment.ARG_OBJECT, ProfileFragment.ARG_OBJECT);
                profileFrag.setArguments(args);
                return profileFrag;
            case 1:
                Fragment cameraFrag = new CameraFragment();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putString(CameraFragment.ARG_OBJECT, CameraFragment.ARG_OBJECT);
                cameraFrag.setArguments(args);
                return cameraFrag;
            case 2:
                Fragment topicsFragment = new TopicsFragment();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putString(TopicsFragment.ARG_OBJECT,TopicsFragment.ARG_OBJECT);
                topicsFragment.setArguments(args);
                return topicsFragment;
        }
       return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}

