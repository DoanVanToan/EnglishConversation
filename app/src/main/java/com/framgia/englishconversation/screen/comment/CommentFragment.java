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
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.comment.CommentRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.comment.CommentRepository;
import com.framgia.englishconversation.databinding.FragmentCommentBinding;

import static com.framgia.englishconversation.utils.Constant.EXTRA_TIMELINE;

/**
 * Comment Screen.
 */
public class CommentFragment extends BottomSheetDialogFragment {

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
        mViewModel = new CommentViewModel(getActivity(), timelineModel);
        AuthenicationRepository authenicationRepository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        CommentRepository repository =
                new CommentRepository(new CommentRemoteDataSource(timelineModel.getId()));
        CommentContract.Presenter presenter =
                new CommentPresenter(mViewModel, repository, authenicationRepository);
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
}