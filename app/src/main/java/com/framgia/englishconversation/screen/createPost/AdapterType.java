package com.framgia.englishconversation.screen.createPost;

import android.support.annotation.IntDef;

import static com.framgia.englishconversation.screen.createPost.AdapterType.CONVERSATION;
import static com.framgia.englishconversation.screen.createPost.AdapterType.MEDIA;

/**
 * Created by fs-sournary.
 * Date on 12/16/17.
 * Description:
 */

@IntDef({CONVERSATION, MEDIA})
public @interface AdapterType {
    int CONVERSATION = 0;
    int MEDIA = 1;
}
