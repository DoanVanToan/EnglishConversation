package com.framgia.englishconversation.screen.timeline;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Sony on 1/26/2018.
 */

public class ItemTimelineViewModel extends BaseObservable {

    private TimelineModel mTimelineModel;
    private boolean mIsAllowEditPost;

    public ItemTimelineViewModel(TimelineModel timelineModel) {
        mTimelineModel = timelineModel;
        getCurrentUser();
    }

    @Bindable
    public boolean isAllowEditPost() {
        return mIsAllowEditPost;
    }

    public void setAllowEditPost(boolean allowEditPost) {
        mIsAllowEditPost = allowEditPost;
        notifyPropertyChanged(BR.allowEditPost);
    }

    public void getCurrentUser() {
        AuthenicationRepository authenicationRepository = new
                AuthenicationRepository(new AuthenicationRemoteDataSource());
        authenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                setAllowEditPost(isAllowEdit(mTimelineModel, data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                setAllowEditPost(false);

            }
        });
    }

    /**
     * allow edit post if current user email equa created user
     * @param timelineModel
     * @param firebaseUser
     * @return
     */
    public boolean isAllowEdit(TimelineModel timelineModel, FirebaseUser firebaseUser) {
        return timelineModel != null
                && firebaseUser != null
                && timelineModel.getCreatedUser() != null
                && timelineModel.getCreatedUser().getEmail() != null
                && timelineModel.getCreatedUser().getEmail().equals(firebaseUser.getEmail());
    }

}
