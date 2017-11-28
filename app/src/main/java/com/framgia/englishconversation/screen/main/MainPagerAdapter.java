package com.framgia.englishconversation.screen.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.screen.profile.ProfileFragment;
import com.framgia.englishconversation.screen.timeline.TimelineFragment;

/**
 * Created by toand on 5/13/2017.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private static final int TAB_COUNT = 2;
    public static final int TIME_LINE = 0;
    public static final int PROFILE = 1;


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TIME_LINE:
                return TimelineFragment.newInstance();
            case PROFILE:
                return ProfileFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
