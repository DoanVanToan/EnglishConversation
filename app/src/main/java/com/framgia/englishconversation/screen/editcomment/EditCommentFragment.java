package com.framgia.englishconversation.screen.editcomment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.framgia.englishconversation.BaseFragment;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.databinding.FragmentEditCommentBinding;
import com.framgia.englishconversation.screen.comment.CallBack;
import com.framgia.englishconversation.screen.createcomment.CreateCommentContract;
import com.framgia.englishconversation.screen.createcomment.CreateCommentPresenter;
import com.framgia.englishconversation.screen.createcomment.CreateCommentViewModel;
import com.framgia.englishconversation.utils.Constant;

import static android.app.Activity.RESULT_OK;

/**
 * Editcomment Screen.
 */
public class EditCommentFragment extends BaseFragment {

    private CreateCommentContract.ViewModel mViewModel;
    private PopupMenu mPopupMenu;

    public static EditCommentFragment getInstance(Comment comment) {
        EditCommentFragment activity = new EditCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_COMMENT, comment);
        activity.setArguments(bundle);
        return activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentEditCommentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_edit_comment, container,
                        false);
        binding.setViewModel((CreateCommentViewModel) mViewModel);
        mPopupMenu = new PopupMenu(getActivity(), binding.ivMultimedia);
        mPopupMenu.inflate(R.menu.multimedia_popup);
        mPopupMenu.setOnMenuItemClickListener((CreateCommentViewModel) mViewModel);
        mViewModel.setPopUpMenu(mPopupMenu);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Comment comment = getArguments().getParcelable(Constant.EXTRA_COMMENT);
        mViewModel = new CreateCommentViewModel(this, comment);
        AuthenicationRepository authenicationRepository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        CreateCommentContract.Presenter presenter =
                new CreateCommentPresenter(mViewModel, comment,
                        authenicationRepository);
        mViewModel.setPresenter(presenter);
        mViewModel.setListener((CallBack) getParentFragment());
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onResume() {
        mViewModel.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mViewModel.onPause();
        super.onPause();
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

    /**
     * Dispatch incoming result to the correct fragment.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.RequestCode.RECORD_VIDEO:
                if (resultCode != RESULT_OK) {
                    //TODO: Handle exception
                    return;
                }
                mViewModel.onGetMultimediaDataDone(data, MediaModel.MediaType.VIDEO);
                break;
            case Constant.RequestCode.SELECT_IMAGE:
                if (resultCode != RESULT_OK) {
                    //TODO: Handle exception
                    return;
                }
                mViewModel.onGetMultimediaDataDone(data, MediaModel.MediaType.IMAGE);
                break;
        }
    }
}
