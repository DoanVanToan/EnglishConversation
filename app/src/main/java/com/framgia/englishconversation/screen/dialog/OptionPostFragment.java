package com.framgia.englishconversation.screen.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.databinding.FragmentOptionPostBinding;
import com.framgia.englishconversation.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionPostFragment extends BottomSheetDialogFragment {

    private OptionPostContract.ViewModel mViewModel;

    public static OptionPostFragment newInstance(TimelineModel timelineModel) {
        OptionPostFragment fragment = new OptionPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.PARCEL_TIMELINE, timelineModel);
        fragment.setArguments(args);
        return fragment;
    }

    public OptionPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TimelineRepository timelineRepository = new TimelineRepository(
                new TimelineRemoteDataSource());
        mViewModel = new OptionPostViewModel((TimelineModel) getArguments()
                .getParcelable(Constant.PARCEL_TIMELINE));
        OptionPostPresenter optionPostPresenter = new OptionPostPresenter(mViewModel,
                timelineRepository);
        mViewModel.setPresenter(optionPostPresenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentOptionPostBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_option_post, container, false);
        binding.setViewModel((OptionPostViewModel) mViewModel);

        return binding.getRoot();
    }

}
