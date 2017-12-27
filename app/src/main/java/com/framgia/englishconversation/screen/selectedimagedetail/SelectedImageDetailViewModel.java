package com.framgia.englishconversation.screen.selectedimagedetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.MediaModel;
import java.util.List;

/**
 * Exposes the data to be used in the SelectedImageDetail screen.
 */

public class SelectedImageDetailViewModel extends BaseObservable
        implements SelectedImageDetailContract.ViewModel {

    private SelectedImageDetailContract.Presenter mPresenter;
    private List<MediaModel> mMediaModels;
    private int mPosition;

    public SelectedImageDetailViewModel(List<MediaModel> mediaModels, int position) {
        mMediaModels = mediaModels;
        mPosition = position;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(SelectedImageDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public List<MediaModel> getMediaModels() {
        return mMediaModels;
    }

    public void setMediaModels(List<MediaModel> mediaModels) {
        mMediaModels = mediaModels;
        notifyPropertyChanged(BR.mediaModels);
    }

    public void nextImage() {
        if (mPosition + 1 >= mMediaModels.size()) {
            return;
        }
        setPosition(++mPosition);
    }

    public void prevImage() {
        if (mPosition - 1 < 0) {
            return;
        }
        setPosition(--mPosition);
    }

    @Bindable
    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
        notifyPropertyChanged(BR.position);
    }
}
