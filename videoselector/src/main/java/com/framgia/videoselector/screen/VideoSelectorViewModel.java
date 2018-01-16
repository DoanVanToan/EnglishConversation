package com.framgia.videoselector.screen;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.framgia.videoselector.R;
import com.framgia.videoselector.data.model.VideoModel;
import com.framgia.videoselector.data.source.VideoDataSource;

import java.util.ArrayList;
import java.util.List;

public class VideoSelectorViewModel extends BaseObservable {
    public static final int REQUEST_RECORD_VIDEO = 1;

    private VideoDataSource mVideoRepository;
    private VideoSelectorAdapter mAdapter;
    private VideoSelectorActivity mActivity;
    private int mSelectedCount;
    private int mLimitItem;

    public VideoSelectorViewModel(VideoSelectorActivity activity, VideoDataSource videoRepository,
                                  int limitItem) {
        mActivity = activity;
        mVideoRepository = videoRepository;
        mLimitItem = limitItem;
        mAdapter = new VideoSelectorAdapter(this);
    }

    public void getData() {
        mVideoRepository.getVideos(new VideoDataSource.Callback<List<VideoModel>>() {
            @Override
            public void onStart() {
                // no ops
            }

            @Override
            public void onSuccess(List<VideoModel> data) {
                mAdapter.addData(data);
            }

            @Override
            public void onFailure(String message) {
                // no ops
            }

            @Override
            public void onComplete() {
                // no ops
            }
        });
    }

    public void clearSelectedItem() {
        mSelectedCount = 0;
        mAdapter.clearSelectedItem();
    }

    public ArrayList<VideoModel> getSelectedItem() {
        return (ArrayList<VideoModel>) mAdapter.getSelectedItem();
    }

    public void onItemVideoClicked(VideoModel videoModel) {
        if (!videoModel.isSelected() && mSelectedCount >= mLimitItem) {
            Toast.makeText(mActivity,
                    String.format(mActivity.getString(R.string.limit_exceeded), mLimitItem),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        videoModel.setSelected(!videoModel.isSelected());
        if (videoModel.isSelected()) {
            mSelectedCount++;
        } else {
            mSelectedCount--;
        }
        mActivity.onItemVideoClick(mSelectedCount);
    }

    public void onRecordVideoClick() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivityForResult(intent, REQUEST_RECORD_VIDEO);
        }
    }

    public VideoSelectorAdapter getAdapter() {
        return mAdapter;
    }

}
