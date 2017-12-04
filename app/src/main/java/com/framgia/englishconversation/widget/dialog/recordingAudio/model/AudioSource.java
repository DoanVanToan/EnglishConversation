package com.framgia.englishconversation.widget.dialog.recordingAudio.model;

import android.media.MediaRecorder;

/**
 * Created by fs-sournary.
 * Date on 12/3/17.
 * Description:
 */

public enum AudioSource {

    MIC,
    VOICE_RECOGNITION;

    public int getAudioSource() {
        switch (this) {
            case VOICE_RECOGNITION:
                return MediaRecorder.AudioSource.VOICE_RECOGNITION;
            default:
                return MediaRecorder.AudioSource.MIC;
        }
    }

}
