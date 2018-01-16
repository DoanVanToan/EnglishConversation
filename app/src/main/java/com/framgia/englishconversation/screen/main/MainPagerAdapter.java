package com.framgia.englishconversation.screen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.framgia.englishconversation.screen.profile.ProfileFragment;
import com.framgia.englishconversation.screen.profileuser.ProfileType;
import com.framgia.englishconversation.screen.timeline.TimelineFragment;

/**
 * Created by toand on 5/13/2017.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public static final int NEW = 0;
    public static final int TOP_VOTE = 1;
    public static final int YOUR_POST = 2;
    public static final int SETTING = 3;
    private static final int TAB_COUNT = 4;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case NEW:
                return TimelineFragment.newInstance(null);
            case TOP_VOTE:

            case YOUR_POST:

            case SETTING:
                return ProfileFragment.newInstance(null, ProfileType.FRAGMENT);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
