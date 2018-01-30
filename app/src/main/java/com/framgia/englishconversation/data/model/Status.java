package com.framgia.englishconversation.data.model;

import android.support.annotation.IntDef;

import static com.framgia.englishconversation.data.model.Status.DELETE;
import static com.framgia.englishconversation.data.model.Status.NORMAL;


/**
 * Description: status comment
 */

@IntDef({NORMAL, DELETE})
public @interface Status {
    int NORMAL = 0;
    int DELETE = 1;
}
