package com.framgia.englishconversation.screen.comment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishconversation.BaseFragment;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.remote.comment.CommentRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.comment.CommentRepository;
import com.framgia.englishconversation.databinding.FragmentCommentBinding;

import static com.framgia.englishconversation.utils.Constant.EXTRA_TIMELINE;
import static com.framgia.englishconversation.utils.Constant.EXTRA_USER;

/**
 * Comment Screen.
 */
public class CommentFragment extends BottomSheetDialogFragment implements CallBack {

    private CommentContract.ViewModel mViewModel;

    public static CommentFragment newInstance(String timelineModelId, UserModel timelineUser) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TIMELINE, timelineModelId);
        args.putParcelable(EXTRA_USER, timelineUser);
        commentFragment.setArguments(args);
        return commentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String timelineModelId = getArguments().getString(EXTRA_TIMELINE);
        UserModel userModel = getArguments().getParcelable(EXTRA_USER);
        mViewModel = new CommentViewModel(getActivity(), timelineModelId, getChildFragmentManager(),
                userModel);
        CommentRepository repository =
                new CommentRepository(new CommentRemoteDataSource(timelineModelId));
        CommentContract.Presenter presenter = new CommentPresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentCommentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_comment,
                        container, false);
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

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal =
                        d.findViewById(android.support.design.R.id.design_bottom_sheet);
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) bottomSheetInternal.getLayoutParams();
                layoutParams.setMargins(15, 0, 15, 0);
                bottomSheetInternal.setLayoutParams(layoutParams);
                bottomSheetInternal.setBackground(null);
                final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetInternal);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public void replaceFragment(BaseFragment baseFragment) {
        mViewModel.replaceFragment(baseFragment);
    }

    @Override
    public void onPostCommentSuccess() {
        mViewModel.onCommmentSuccess();
    }
}
