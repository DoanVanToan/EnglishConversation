package com.framgia.englishconversation.data.model;

import android.support.annotation.IntDef;

import static com.framgia.englishconversation.data.model.GravityType.LEFT;
import static com.framgia.englishconversation.data.model.GravityType.RIGHT;

/**
 * Created by fs-sournary.
 * Date on 12/7/17.
 * Description:
 */

@IntDef({LEFT, RIGHT})
public @interface GravityType {
    int LEFT = 0;
    int RIGHT = 1;
}
