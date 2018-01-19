package com.framgia.audioselector.util.recording;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by fs-sournary.
 * Date on 12/14/17.
 * Description:
 */

public class Recording {

    private MediaRecorder mMediaRecorder;

    public Recording() {
        mMediaRecorder = new MediaRecorder();
    }

    public void initRecorder(String filePath) {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
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

    public void onRecording(boolean isRecording) {
        if (mMediaRecorder == null) return;
        if (isRecording) {
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            mMediaRecorder.start();
        }
    }

}
