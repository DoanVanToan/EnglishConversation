package com.framgia.englishconversation.screen.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.databinding.ActivityMainBinding;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Main Screen.
 */
public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    private MainContract.ViewModel mViewModel;
    private BottomNavigationView mNavigationView;
    private ViewPager mViewPager;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new MainViewModel(this, new Navigator(this));
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        MainContract.Presenter presenter = new MainPresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel((MainViewModel) mViewModel);
        mNavigationView = binding.navigation;
        mViewPager = binding.viewPager;
        mNavigationView.setOnNavigationItemSelectedListener(this);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.navigation_profile:
                mViewPager.setCurrentItem(1, true);
                break;
            default:
                break;
        }
        return false;
    }
}
