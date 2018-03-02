package com.framgia.audioselector.screen.audioselector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.framgia.audioselector.BaseActivity;
import com.framgia.audioselector.R;
import com.framgia.audioselector.databinding.ActivityAudioSelectorBinding;

public class AudioSelectorActivity extends BaseActivity implements ActionMode.Callback {

    public static final String EXTRA_AUDIO =
            "com.framgia.audioselector.intent.extra.EXTRA_AUDIO";
    public static final String permissions[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int RC_READ_EXTERNAL_STORAGE = 1;
    private AudioSelectorViewModel mViewModel;
    private ActivityAudioSelectorBinding mBinding;
    private ActionMode mActionMode;

    public static Intent getInstance(Context context) {
        return new Intent(context, AudioSelectorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_audio_selector);
        if (getSupportActionBar() == null) {
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setTitle(R.string.title_audio_selector);
        checkPermission();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_READ_EXTERNAL_STORAGE:
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    onPermissionDenied();
                } else {
                    onPermissionGranted();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mViewModel == null) {
            return;
        }
        mViewModel.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewModel == null) {
            return;
        }
        mViewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mViewModel == null) {
            return;
        }
        mViewModel.onPause();
    }

    @Override
    protected void onStop() {
        if (mViewModel != null) {
            mViewModel.onStop();
        }
        super.onStop();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissions, RC_READ_EXTERNAL_STORAGE);
        }
    }

    private void onPermissionGranted() {
        mViewModel = new AudioSelectorViewModel(this);
        AudioSelectorContract.Presenter presenter =
                new AudioSelectorPresenter(mViewModel);
        mViewModel.setPresenter(presenter);
        mBinding.setViewModel(mViewModel);
    }

    private void onPermissionDenied() {
        Snackbar.make(mBinding.constraint,
                R.string.msg_permission_denied,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(
                                AudioSelectorActivity.this,
                                permissions, RC_READ_EXTERNAL_STORAGE);
                    }
                }).show();
    }

    public void onItemAudioClick(int selectedCount) {
        if (mActionMode == null) {
            mActionMode = startActionMode(this);
        }
        mActionMode.setTitle(selectedCount + " " + getString(R.string.title_select));
        if (selectedCount == 0) {
            mActionMode.finish();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = actionMode.getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_add) {
            mViewModel.finishActivity();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        mViewModel.clearCheck();
    }
}
