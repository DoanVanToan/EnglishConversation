package com.framgia.englishconversation.screen.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsImpl;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.databinding.ActivityLoginBinding;
import com.framgia.englishconversation.utils.Utils;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Login Screen.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String[] FACEBOOK_PERMISIONS = { "email", "public_profile" };
    private LoginContract.ViewModel mViewModel;
    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        mViewModel = new LoginViewModel(this, new Navigator(this));

        LoginContract.Presenter presenter =
                new LoginPresenter(mViewModel, repository, new SharedPrefsImpl(this));
        mViewModel.setPresenter(presenter);

        ActivityLoginBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel((LoginViewModel) mViewModel);
        binding.signInButton.setOnClickListener(this);
        mLoginButton = binding.loginButton;

        getSupportActionBar().hide();
        initFacebookSDK();

        Utils.getKeyHash(this);
    }

    private void initFacebookSDK() {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions(FACEBOOK_PERMISIONS);
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult == null) return;
                AccessToken accessToken = loginResult.getAccessToken();
                mViewModel.onLoginFacebookSuccess(accessToken);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, R.string.login_facebook_cancel,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                mViewModel.onLoginGoogleClick();
                break;
        }
    }
}
