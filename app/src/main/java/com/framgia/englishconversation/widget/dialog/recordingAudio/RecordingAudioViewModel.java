package com.framgia.englishconversation.widget.dialog.recordingAudio;

/**
 * Created by fs-sournary.
 * Date on 12/1/17.
 * Description:
 */

public class RecordingAudioViewModel {

    private RecordingAudioDialog mDialog;

    RecordingAudioViewModel(RecordingAudioDialog dialog) {
        mDialog = dialog;
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }

}
