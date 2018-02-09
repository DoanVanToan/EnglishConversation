package com.framgia.englishconversation.screen.changepassword;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.framgia.englishconversation.R;

import com.framgia.englishconversation.databinding.FragmentChangePasswordBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends DialogFragment {

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        FragmentChangePasswordBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_change_password,
                        container, true);
        binding.setViewModel(new ChangePasswordViewModel());
        return binding.getRoot();
    }

}
