package com.framgia.englishconversation.utils.recording;

import android.app.Activity;
import android.media.MediaRecorder;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.utils.navigator.Navigator;

import java.io.IOException;

/**
 * Created by fs-sournary.
 * Date on 12/14/17.
 * Description:
 */

public class RecordingAudio {

    private Navigator mNavigator;
    private MediaRecorder mMediaRecorder;

    public RecordingAudio(Activity activity) {
        mNavigator = new Navigator(activity);
        mMediaRecorder = new MediaRecorder();
    }

    public void initRecorder(int source, String filePath) {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(source);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(filePath);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            // TODO: Handle exception when MediaRecoder does not prepare() method completely
            // TODO: Example: User starts before prepare() method is run completely
            // TODO: We wil find a solution about this problem later.
        }
    }

    public void release() {
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void onRecordingAudio(boolean isRecording) {
        if (mMediaRecorder == null) return;
        if (isRecording) {
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException e) {
                mNavigator.showToast(R.string.message_error_prepare);
            }
        } else {
            mMediaRecorder.start();
        }
    }

}
