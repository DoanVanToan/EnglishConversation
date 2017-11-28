package com.framgia.englishconversation.screen.forgotPassword;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.databinding.ActivityForgotPasswordBinding;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * ForgotPassword Screen.
 */
public class ForgotPasswordActivity extends BaseActivity {

    private ForgotPasswordContract.ViewModel mViewModel;

    public static Intent getInstance(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ForgotPasswordViewModel(this, new Navigator(this));
        AuthenicationRepository repository = new AuthenicationRepository(
                new AuthenicationRemoteDataSource());

        ForgotPasswordContract.Presenter presenter =
                new ForgotPasswordPresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);

        ActivityForgotPasswordBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binding.setViewModel((ForgotPasswordViewModel) mViewModel);
        getSupportActionBar().hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
