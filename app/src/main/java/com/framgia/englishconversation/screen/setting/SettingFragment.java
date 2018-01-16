package com.framgia.englishconversation.screen.setting;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.databinding.FragmentSettingBinding;
import com.framgia.englishconversation.screen.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    private SettingContract.ViewModel mViewModel;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new SettingViewModel((MainActivity) getActivity());

        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        SettingContract.Presenter presenter = new SettingPresenter(mViewModel, repository);

        mViewModel.setPresenter(presenter);

        FragmentSettingBinding binding = DataBindingUtil
                .inflate(inflater,
                        R.layout.fragment_setting,
                        container,
                        false);
        binding.setViewModel((SettingViewModel) mViewModel);
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
