package com.framgia.englishconversation.data.model;

import android.support.annotation.IntDef;

import static com.framgia.englishconversation.data.model.Status.DELETE;
import static com.framgia.englishconversation.data.model.Status.NORMAl;


/**
 * Description: status comment
 */

@IntDef({NORMAl, DELETE})
public @interface Status {
    int NORMAl = 0;
    int DELETE = 1;
}
