package com.framgia.englishconversation.screen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.framgia.englishconversation.screen.timeline.hometimeline.HomeTimelineFragment;
import com.framgia.englishconversation.screen.nextcoming.NextComingFragment;
import com.framgia.englishconversation.screen.profile.ProfileFragment;
import com.framgia.englishconversation.screen.setting.SettingFragment;

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
                return HomeTimelineFragment.newInstance();
            case TOP_VOTE:
                return NextComingFragment.newInstance();
            case YOUR_POST:
                return ProfileFragment.newInstance(null);
            case SETTING:
                return SettingFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
