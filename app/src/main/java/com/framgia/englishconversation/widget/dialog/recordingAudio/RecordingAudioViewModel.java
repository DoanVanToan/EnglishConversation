package com.framgia.englishconversation.widget.dialog.recordingAudio;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fs-sournary.
 * Date on 12/1/17.
 * Description:
 */

public class RecordingAudioViewModel extends BaseObservable {

    private static final int PERIOD_INTERVAL = 1;

    private RecordingAudioDialog mDialog;
    private boolean mIsRecording;
    private boolean mIsCancelClick;
    private CompositeDisposable mCompositeDisposable;

    RecordingAudioViewModel(RecordingAudioDialog dialog) {
        mDialog = dialog;
        mCompositeDisposable = new CompositeDisposable();
        setRecording(false);
        mIsCancelClick = false;
    }

    @Bindable
    public boolean isRecording() {
        return mIsRecording;
    }

    public void setRecording(boolean recording) {
        mIsRecording = recording;
        notifyPropertyChanged(BR.recording);
    }

    public void onDismissDialogClick() {
        mIsCancelClick = true;
        mDialog.releaseRecordingAudio();
        mCompositeDisposable.clear();
        mDialog.dismiss();
    }

    public boolean isCancelClick() {
        return mIsCancelClick;
    }

    public void onRecordingAudioButtonClick() {
        mDialog.onRecordingAudio(mIsRecording);
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
                            mDialog.updateDuration(++beginTime);
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

}
