package com.framgia.englishconversation.screen.comment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.englishconversation.BaseFragment;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.comment.CommentRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.comment.CommentRepository;
import com.framgia.englishconversation.databinding.FragmentCommentBinding;

import static com.framgia.englishconversation.utils.Constant.EXTRA_TIMELINE;

/**
 * Comment Screen.
 */
public class CommentFragment extends BaseFragment {

    private CommentContract.ViewModel mViewModel;

    public static CommentFragment newInstance(TimelineModel timelineModel) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_TIMELINE, timelineModel);
        commentFragment.setArguments(args);
        return commentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimelineModel timelineModel = getArguments().getParcelable(EXTRA_TIMELINE);
        mViewModel = new CommentViewModel(getActivity());
        CommentRepository repository =
                new CommentRepository(new CommentRemoteDataSource(timelineModel.getId()));
        CommentContract.Presenter presenter = new CommentPresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        FragmentCommentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false);
        binding.setViewModel((CommentViewModel) mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mViewModel.onDestroy();
        super.onDestroy();
    }
}
