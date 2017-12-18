package com.framgia.englishconversation.widget.dialog.recordingAudio;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.utils.Utils;
import com.framgia.englishconversation.utils.recording.RecordingAudio;

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
    private static final String INIT_RECORD_TIME = "00:00";

    private RecordingAudioDialog mDialog;
    private CompositeDisposable mCompositeDisposable;
    private RecordingAudio mRecordingAudio;
    private boolean mIsRecording;
    private boolean mIsCancelClick;
    private String mRecordTime;

    RecordingAudioViewModel(RecordingAudioDialog dialog) {
        mDialog = dialog;
        initDefaultData();
        mCompositeDisposable = new CompositeDisposable();
        mRecordingAudio = new RecordingAudio(mDialog.getActivity());
        mRecordingAudio.initRecorder(mDialog.getAudioSource().getSource(), mDialog.getFilePath());
    }

    @Bindable
    public boolean isRecording() {
        return mIsRecording;
    }

    private void setRecording(boolean recording) {
        mIsRecording = recording;
        notifyPropertyChanged(BR.recording);
    }

    private void initDefaultData() {
        mRecordTime = INIT_RECORD_TIME;
        mIsCancelClick = false;
        mIsRecording = false;
    }

    public void onDismissDialogClick() {
        mIsCancelClick = true;
        mRecordingAudio.release();
        mCompositeDisposable.clear();
        mDialog.dismiss();
    }

    boolean isCancelClick() {
        return mIsCancelClick;
    }

    public void onRecordingAudioButtonClick() {
        mRecordingAudio.onRecordingAudio(mIsRecording);
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
                            setRecordTime(duration);
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

    @Bindable
    public String getRecordTime() {
        return mRecordTime;
    }

    private void setRecordTime(String recordTime) {
        mRecordTime = recordTime;
        notifyPropertyChanged(BR.recordTime);
    }

}
