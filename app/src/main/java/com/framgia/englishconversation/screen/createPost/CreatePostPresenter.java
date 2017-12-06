package com.framgia.englishconversation.screen.createPost;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;

/**
 * Listens to user actions from the UI ({@link CreatePostActivity}), retrieves the data and updates
 * the UI as required.
 */
final class CreatePostPresenter implements CreatePostContract.Presenter {
    private static final String TAG = CreatePostPresenter.class.getName();

    private final CreatePostContract.ViewModel mViewModel;
    private AuthenicationRepository mAuthenicationRepository;
    private TimelineRepository mTimelineRepository;

    public CreatePostPresenter(CreatePostContract.ViewModel viewModel,
                               AuthenicationRepository authenicationRepository,
                               TimelineRepository timelineRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
        mTimelineRepository = timelineRepository;
        getUser();
    }

    @Override
    public void getUser() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetCurrentUserSuccess(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetCurrentUserFailed(msg);
            }
        });
    }

    @Override
    public void createPost(TimelineModel timelineModel) {
        if (TextUtils.isEmpty(timelineModel.getContent())
                && (timelineModel.getMedias() == null || timelineModel.getMedias().size() == 0)) {
            return;
        }
        mTimelineRepository.createNewPost(timelineModel, new DataCallback() {
            @Override
            public void onGetDataSuccess(Object data) {
                mViewModel.onCreatePostSuccess();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onCreatePostFailed(msg);
            }
        });
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
