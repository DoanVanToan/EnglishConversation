package com.framgia.englishconversation.screen.comment;

/**
 * Listens to user actions from the UI ({@link CommentFragment}), retrieves the data and updates
 * the UI as required.
 */
final class CommentPresenter implements CommentContract.Presenter {
    private static final String TAG = CommentPresenter.class.getName();

    private final CommentContract.ViewModel mViewModel;

    CommentPresenter(CommentContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
