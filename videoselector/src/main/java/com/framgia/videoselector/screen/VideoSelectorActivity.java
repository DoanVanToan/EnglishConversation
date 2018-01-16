package com.framgia.videoselector.screen;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.framgia.videoselector.R;
import com.framgia.videoselector.data.model.VideoModel;
import com.framgia.videoselector.data.source.VideoDataSource;
import com.framgia.videoselector.data.source.VideoRepository;
import com.framgia.videoselector.data.source.local.VideoLocalDataSource;
import com.framgia.videoselector.databinding.ActivityVideoSelectorBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.framgia.videoselector.screen.VideoSelectorViewModel.REQUEST_RECORD_VIDEO;

public class VideoSelectorActivity extends AppCompatActivity {
    public static final int LIMIT_ITEM = 1;
    private static final int REQUEST_PERMISSION = 1;
    private static final String WRITE_EXTENAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String EXTRA_LIMIT_ITEM = "EXTRA_LIMIT_ITEM";
    public static final String EXTRA_DATA = "EXTRA_DATA";

    private VideoSelectorViewModel mViewModel;
    private ActionBar mActionBar;
    private ActionMode mActionMode;

    public static Intent getInstance(Context context, int limitItem) {
        return new Intent(context, VideoSelectorActivity.class).putExtra(EXTRA_LIMIT_ITEM,
                limitItem);
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, VideoSelectorActivity.class).putExtra(EXTRA_LIMIT_ITEM,
                LIMIT_ITEM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int limit = getIntent().getExtras().getInt(EXTRA_LIMIT_ITEM, LIMIT_ITEM);
        VideoDataSource repository = new VideoRepository(new VideoLocalDataSource(this));
        mViewModel = new VideoSelectorViewModel(this, repository, limit);

        ActivityVideoSelectorBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_video_selector);
        binding.setViewModel(mViewModel);

        if (isPermissionGranted()) {
            mViewModel.getData();
        }

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);

            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setTitle(R.string.title_select_video);
        }
    }

    private boolean isPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTENAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTENAL_STORAGE)) {
                showDialogRequestPermission();
                return false;
            }
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTENAL_STORAGE},
                    REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    public void onItemVideoClick(int selectedItem) {
        if (mActionMode == null) {
            mActionMode = startActionMode(callback);
        }
        mActionMode.setTitle(selectedItem + " " + getString(R.string.title_selected));
        if (selectedItem == 0) {
            mActionMode.finish();
        }
    }

    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_contextual_action_bar, menu);
            mActionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.menu_item_add_image) {
                ArrayList<VideoModel> models = mViewModel.getSelectedItem();
                if (models == null || models.isEmpty()) {
                    return false;
                }
                setResult(models);
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            mViewModel.clearSelectedItem();
        }
    };

    private void setResult(ArrayList<VideoModel> videoModels) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_DATA, videoModels);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showDialogRequestPermission() {
        new AlertDialog.Builder(this).setTitle(R.string.title_request_permission)
                .setMessage(getString(R.string.msg_request_permission))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(VideoSelectorActivity.this,
                                new String[]{WRITE_EXTENAL_STORAGE}, REQUEST_PERMISSION);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || requestCode != REQUEST_RECORD_VIDEO) {
            return;
        }
        String url = getFilePath(this, data.getData());
        String id = UUID.randomUUID().toString();
        VideoModel model = new VideoModel();
        model.setFilePath(url);
        model.setId(id);

        ArrayList<VideoModel> result = new ArrayList<>();
        result.add(model);

        setResult(result);
    }

    public String getFilePath(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSION) {
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mViewModel.getData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
