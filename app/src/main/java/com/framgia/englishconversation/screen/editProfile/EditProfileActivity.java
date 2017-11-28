package com.framgia.englishconversation.screen.editProfile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.databinding.ActivityEditProfileBinding;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * EditProfile Screen.
 */
public class EditProfileActivity extends BaseActivity {

    private EditProfileContract.ViewModel mViewModel;

    public static Intent getInstance(Context context) {
        return new Intent(context, EditProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new EditProfileViewModel(this, new Navigator(this));
        AuthenicationRepository repository = new AuthenicationRepository(
                new AuthenicationRemoteDataSource());

        EditProfileContract.Presenter presenter = new EditProfilePresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);

        ActivityEditProfileBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        binding.setViewModel((EditProfileViewModel) mViewModel);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }
}
