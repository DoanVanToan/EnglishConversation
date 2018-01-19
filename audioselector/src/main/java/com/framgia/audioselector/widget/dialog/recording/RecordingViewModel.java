package com.framgia.audioselector.widget.dialog.recording;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.audioselector.BR;
import com.framgia.audioselector.util.Utils;
import com.framgia.audioselector.util.recording.Recording;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fs-sournary.
 * Data: 1/19/18.
 * Description:
 */

public class RecordingViewModel extends BaseObservable implements RecordingContract.ViewModel {

    private static final int PERIOD_INTERVAL = 1;
    private static final String BEGIN_RECORDING_TIME = "00:00";

    private boolean mIsCancelClick;
    private String mRecordingTime;
    private boolean mIsRecording;
    private RecordingContract.Presenter mPresenter;
    private Recording mRecording;
    private CompositeDisposable mCompositeDisposable;
    private RecordingDialog mDialog;

    public RecordingViewModel(RecordingDialog dialog) {
        mDialog = dialog;
    }

    @Override
    public void initRecord(String filePath) {
        mRecording = new Recording();
        mRecording.initRecorder(filePath);
        mCompositeDisposable = new CompositeDisposable();
        mRecordingTime = BEGIN_RECORDING_TIME;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(RecordingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onRecordingClick() {
        mRecording.onRecording(mIsRecording);
        if (mIsRecording) {
            mCompositeDisposable.clear();
            mDialog.dismiss();
        } else {
            Disposable disposable = Observable.interval(PERIOD_INTERVAL, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Long>() {
                        @Override
                        public void onNext(Long beginTime) {
                            String duration = Utils.updateDuration(++beginTime);
                            setRecordingTime(duration);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            mCompositeDisposable.add(disposable);
        }
        setRecording(!mIsRecording);
    }

    public void onCancelClick() {
        mDialog.dismiss();
        mRecording.release();
        mIsCancelClick = true;
    }

    @Override
    public boolean isCancelClick() {
        return mIsCancelClick;
    }

    @Bindable
    public String getRecordingTime() {
        return mRecordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        mRecordingTime = recordingTime;
        notifyPropertyChanged(BR.recordingTime);
    }

    @Bindable
    public boolean isRecording() {
        return mIsRecording;
    }

    public void setRecording(boolean recording) {
        mIsRecording = recording;
        notifyPropertyChanged(BR.recording);
    }
}
