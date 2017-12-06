package com.framgia.englishconversation.screen.createPost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.LocationModel;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.PostType;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.databinding.ItemVideoBinding;
import com.framgia.englishconversation.record.Util;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.service.FirebaseUploadService;
import com.framgia.englishconversation.utils.Utils;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioBuilder;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.framgia.englishconversation.data.model.PostType.IMAGE;
import static com.framgia.englishconversation.service.BaseStorageService.POST_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService
        .ACTION_UPLOAD_MULTI_FILE;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FILES;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_MEDIA_MODEL;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_URI;

/**
 * Exposes the data to be used in the CreatePost screen.
 */

public class MediaAdapter extends RecyclerView.Adapter {
    private List<MediaModel> mMediaModels;

    public MediaAdapter() {
        mMediaModels = new ArrayList<>();
    }

    public void updateData(List<MediaModel> mediaModels) {
        if (mediaModels == null) {
            return;
        }
        mMediaModels.addAll(mediaModels);
        notifyDataSetChanged();
    }

    public void setData(List<MediaModel> mediaModels){
        mMediaModels.clear();
        updateData(mediaModels);
    }

    @Override
    public int getItemViewType(int position) {
        return mMediaModels.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.IMAGE:

                break;
            case MediaModel.MediaType.AUDIO:

                break;
            case MediaModel.MediaType.VIDEO:
                ItemVideoBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_video,
                        parent,
                        false);
                return new VideoViewHolder(binding);
            default:
                return null;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MediaModel.MediaType.IMAGE:

                break;
            case MediaModel.MediaType.AUDIO:

                break;
            case MediaModel.MediaType.VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                videoViewHolder.bindData(mMediaModels.get(position));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMediaModels != null ? mMediaModels.size() : 0;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private ItemVideoBinding mVideoBinding;

        public VideoViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            mVideoBinding = binding;
        }

        public void bindData(MediaModel video) {
            mVideoBinding.setUri(video.getUri());
            mVideoBinding.executePendingBindings();
        }
    }


}
