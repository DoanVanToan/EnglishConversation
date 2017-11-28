package com.framgia.englishconversation.data.model;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.framgia.englishconversation.data.model.PostType.IMAGE;
import static com.framgia.englishconversation.data.model.PostType.LOCATION;
import static com.framgia.englishconversation.data.model.PostType.RECORD;
import static com.framgia.englishconversation.data.model.PostType.TEXT_CONTENT;
import static com.framgia.englishconversation.data.model.PostType.VIDEO;

/**
 * Created by framgia on 11/11/17.
 */

@IntDef({ TEXT_CONTENT, VIDEO, IMAGE, LOCATION, RECORD })
@Retention(RetentionPolicy.SOURCE)
public @interface PostType {
    int TEXT_CONTENT = 0;
    int VIDEO = 1;
    int IMAGE = 2;
    int LOCATION = 3;
    int RECORD = 4;
}
