package com.framgia.englishconversation.screen.editPost;

import android.app.Activity;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.screen.basePost.BasePostViewModel;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Created by Sony on 2/2/2018.
 */

public class EditPostViewModel extends BasePostViewModel implements EditPostContract.ViewModel{

    public EditPostViewModel(Activity activity, Navigator navigator) {
        super(activity, navigator);
    }

    public EditPostViewModel(Activity activity, Navigator navigator, TimelineModel timelineModel) {
        super(activity, navigator, timelineModel);
    }

}
