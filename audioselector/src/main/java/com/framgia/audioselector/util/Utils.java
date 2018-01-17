package com.framgia.audioselector.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.util.Locale;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class Utils {

    public static int getFileDuration(Context context, String filePath) {
        try {
            MediaPlayer mp = MediaPlayer.create(context, Uri.parse(filePath));
            int duration = mp.getDuration();
            mp.release();
            return duration;
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getFormatTimeFile(long duration) {
        if (duration == -1) {
            return "00:00";
        }
        int minute = (int) Math.floor(duration / 1000 / 60);
        int second = (int) (duration / 1000 - minute * 60);
        return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
    }

    public static String getFormatTimeFile(Context context, String filePath) {
        int duration = getFileDuration(context, filePath);
        return getFormatTimeFile(duration);
    }
}
