package com.framgia.englishconversation.screen.createPost;

import android.app.Activity;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.screen.basePost.BasePostViewModel;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the CreatePost screen.
 */

public class CreatePostViewModel extends BasePostViewModel implements CreatePostContract.ViewModel {

    public CreatePostViewModel(Activity activity, Navigator navigator) {
        super(activity, navigator);
    }

    public CreatePostViewModel(Activity activity, Navigator navigator, TimelineModel timelineModel) {
        super(activity, navigator, timelineModel);
    }

}
