package com.framgia.audioselector.util;

import java.util.Locale;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class Utils {

    public static String updateDuration(long duration) {
        long s = duration % Constant.SECOND_PER_MINUTE;
        long m = (duration / Constant.SECOND_PER_MINUTE) % Constant.SECOND_PER_MINUTE;
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }
}
