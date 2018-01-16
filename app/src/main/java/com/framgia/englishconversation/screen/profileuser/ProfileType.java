package com.framgia.englishconversation.screen.profileuser;

import android.support.annotation.IntDef;

import static com.framgia.englishconversation.screen.profileuser.ProfileType.ACTIVITY;
import static com.framgia.englishconversation.screen.profileuser.ProfileType.FRAGMENT;

/**
 * Created by fs-sournary.
 * Date on 12/7/17.
 * Description:
 */

@IntDef({ ACTIVITY, FRAGMENT })
public @interface ProfileType {
    int ACTIVITY = 0;
    int FRAGMENT = 1;
}
