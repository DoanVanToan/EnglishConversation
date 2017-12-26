package com.framgia.englishconversation.screen.comment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.BaseFragment;
import com.framgia.englishconversation.databinding.FragmentCommentBinding;

/**
 * Comment Screen.
 */
public class CommentFragment extends BaseFragment {

    private CommentContract.ViewModel mViewModel;

    public static CommentFragment newInstance() {
        return new CommentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new CommentViewModel();

        CommentContract.Presenter presenter = new CommentPresenter(mViewModel);
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
}
